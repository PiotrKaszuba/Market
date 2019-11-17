package market.relogo
import market.ReLogoTurtle
class StandardBehaviour {
	
	ReLogoTurtle turtle //resourceMiningAbility
	def rootNeed
	def rootNeedAmount
	def globalPrice
	def resourceUsageFactors
	def resourceConsumableAmount
	def resourceSteps
	def randomDiv
	def randomShift
	def resourceLeftFactor
	def base_mine_timeout
	def base_market_timeout
	def StandardBehaviour(ReLogoTurtle turtle, def rootNeed, def rootNeedAmount, def globalPrice, def resourceUsageFactors=[1,1], def resourceConsumableAmount =[null,null], resourceSteps = 5, randomDiv = 2.5, randomShift= 0.6, resourceLeftFactor=0.25, base_mine_timeout=3, base_market_timeout=20) {
		this.turtle = turtle
		this.rootNeed = rootNeed
		this.rootNeedAmount = rootNeedAmount
		this.globalPrice = globalPrice
		this.resourceUsageFactors = resourceUsageFactors
		this.resourceConsumableAmount= resourceConsumableAmount
		this.resourceSteps = resourceSteps
		this.randomDiv = randomDiv
		this.randomShift = randomShift
		this.resourceLeftFactor = resourceLeftFactor
		this.base_mine_timeout = base_mine_timeout
		this.base_market_timeout = base_market_timeout
	}
	
	
	def resourceMineGain(Resource mine, def timeout) {
		def gain = mine.sumOfArithmeticSequence(timeout, mine.base_mine, mine.r)
	}
	
	def randomFactor() {
		return Math.random()/randomDiv + randomShift
	}
	
	def miningAppendAttractiveness(List<TaskStructure> taskAtts, Resource mine, def cost, def needRes) {
		def timeout = this.base_mine_timeout + turtle.ambition
		def gain = resourceMineGain(mine, timeout)
		def miningCost = timeout * (turtle.miningFatigue+1)/100 * (resourceUsageFactors['rice'] + resourceUsageFactors['water'])
		cost = cost + miningCost
		def attractiveness = (gain - cost)*randomFactor()
		
		taskAtts.add(new TaskStructure(attractiveness, cost, gain, [mine], ['do': 'mine', 'timeout':timeout, 'resource' : needRes], [needRes], [true]))
	}
	
	
	def marketAppendAttractivenessBuy(List<TaskStructure> taskAtts, Market market, def cost, def needRes, def needResAmount, def wantToBuy) {
		def timeout = this.base_market_timeout + turtle.ambition * 3
		cost = cost + timeout
		def expectedBuy = timeout * (market.meanAmountSoldPerStep(needRes)/2 + resourceLeftFactor * market.meanOfResourceLft(needRes))
		
		def priceDiff = globalPrice[needRes]-market.discountedMeanPrice(needRes)
		
		if(needResAmount < turtle.panicThreshold) {
			for(int i =1; i<=wantToBuy; i++) {
				def willBuy = Math.min(i, expectedBuy)
				def gain = willBuy * priceDiff + (needResAmount < turtle.panicThreshold ? willBuy/2 : 0)
				def attractiveness = (gain - cost) * randomFactor()
				taskAtts.add(new TaskStructure(attractiveness, cost, gain, [market], ['do' : 'buy', 'timeout' : timeout, 'resource' : needRes, 'amount' : i], [needRes], [false]))
			}
			
		}
		else {
			def willBuy = Math.min(wantToBuy, expectedBuy)
			def gain = willBuy * priceDiff + (needResAmount < turtle.panicThreshold ? willBuy/2 : 0)
			def attractiveness = (gain - cost) * randomFactor()
			taskAtts.add(new TaskStructure(attractiveness, cost, gain, [market], [ ['do' : 'buy', 'timeout' : timeout, 'resource' : needRes, 'amount' : wantToBuy] ], [needRes], [false]))
		}
		
		
		
	}
	
	def marketAppendAttractivenessSell(List<TaskStructure> taskAtts, Market market, def cost, def sellRes, def sellResAmount, def wantToSell) {
		def timeout = this.base_market_timeout + turtle.ambition * 3
		cost = cost + timeout
		def expectedSell = timeout * (market.meanAmountSoldPerStep(sellRes) - resourceLeftFactor * market.meanOfResourceLft(sellRes))
		
		def priceDiff = globalPrice[sellRes]-market.discountedMeanPrice(sellRes)
		def willSell = Math.min(wantToSell, expectedSell)
		def gain = willSell * priceDiff
		def attractiveness = (gain - cost) * randomFactor()
		taskAtts.add(new TaskStructure(attractiveness, cost, gain, [market], [ ['do' : 'sell', 'timeout' : timeout, 'resource' : sellRes, 'wantGetGoldAmount' : wantToSell] ], [sellRes], [false]))
		
		
		
	}
	
	
	
//	def prepareRiceWaterVariables(def resource) {
//		def otherResource = 'rice' 
//	}
	
	def mineAcceptCondition(Resource mine, def resource) {
		return mine instanceof Resource && mine.type.equals(resource) && turtle.resourceMiningAbility[resource]
	}
	
	def marketAcceptCondition(Market market) {
		return market instanceof Market && market.alive
	}
	
	
	
	
	def invokeTask(def label, def resource) {
		TaskStructure taskStructure = TaskStructure()
		return label(resource, taskStructure)
	}
	
	def needGold(def resource, TaskStructure taskStructure, def amountNeed) {
		
		ReLogoTurtle startLocationTurtle = taskStructure.targets.empty ? turtle : taskStructure.targets[0]
		
		List<TaskStructure> tasks = []
		
		
		
		
		
		
		
		//CHECK IF NEEDSOFAR has Rice OR Water
		
		def forSale = ['rice', 'water'].toSet()-taskStructure.needSoFar.toSet()
		
		turtle.ask(turtle.turtles()){
			def cost = startLocationTurtle.distance(it) * (1+ startLocationTurtle.travelCost) / 100
			cost = cost*resourceUsageFactors['rice'] + cost*resourceUsageFactors['water']
			
			//overall - choose if it is better to
			//1. mine
			//or
			//2. SELL WITH POTENTIAL TREE EXPANSION
			
			
			//mine - always possible - finished the tree
			if(mineAcceptCondition(it, resource))
				miningAppendAttractiveness(tasks, it, cost, resource)
				
			//market - type: SELL (cus need GOLD)
			// 1. WE SPEND Rice/Water - so if there was NEED Rice OR Water IN THE LIST (needSoFar) we cannot evaluate it !!
			// if it is fine:
			// 2. also we need Rice OR Water - check if enough Rice OR WATER for expected resource amount
			// enough res = go sell it - tree finished
			// not enough res = go get res! - tree expansion
				
				
			if(marketAcceptCondition(it)) {
				forSale.each { resourceToSell ->
					def resourceAmount = resourceToSell.equals('rice') ? turtle.rice : turtle.water
					marketAppendAttractivenessSell(tasks, it, cost, resourceToSell, resourceAmount, amountNeed)
				}
			}
			
		}
		
		
		def task = null
		tasks = tasks.sort { it.attractiveness }
		if(! tasks.empty)
			task = tasks.pop()
		else {
			task = null
			return task
		}
		
		def amounts = [turtle.rice*100 + turtle.hunger, turtle.water*100 + turtle.thirst]
		
		while(true) {
			if(! tasks.empty) {
				task = tasks.pop()
				
				if (! task.already_evaluated[0]) {
					task = evaluateSellTask(task)
					tasks.add(task)
					tasks = tasks.sort {it.attractiveness}
					continue
				}
				
				if(! checkIfPossibleToFinish(task, amounts))
					continue
				return task
				
				
			}
			else {
				task = null
				return task
			}
		}
		
		
	}
	
	
	def needRiceOrWater(def resource, TaskStructure taskStructure, def amountNeed) {
		
		
		ReLogoTurtle startLocationTurtle = taskStructure.targets.empty ? turtle : taskStructure.targets[0]
		
		List<TaskStructure> tasks = []
		
		
		
		def resourceAmount = resource.equals('rice') ? turtle.rice : turtle.water
		turtle.ask(turtle.turtles()){
			def cost = startLocationTurtle.distance(it) * (1+ startLocationTurtle.travelCost) / 100
			cost = cost*resourceUsageFactors['rice'] + cost*resourceUsageFactors['water']
			
			//overall - choose if it is better to
			//1. mine
			//or
			//2. buy WITH POTENTIAL TREE EXPANSION
			
			
			//mine - always possible - finished the tree
			if(mineAcceptCondition(it, resource)) 
				miningAppendAttractiveness(tasks, it, cost, resource)
				
			//market - type: BUY (cus need Rice/Water) 
			// 1. WE SPEND GOLD - so if there was NEED GOLD IN THE LIST (needSoFar) we cannot evaluate it !!
			// if it is fine:
			// 2. also we need GOLD - check if enough gold for expected resource amount
			// enough gold = go buy it - tree finished
			// not enough gold = go get gold! - tree expansion
			if(! taskStructure.needSoFar.contains('gold')) {
				
				if(marketAcceptCondition(it)) {
					
					
					marketAppendAttractivenessBuy(tasks, it, cost, resource, resourceAmount, amountNeed)
				}
			}
			// in the end - we have to pass some attractiveness/cost/gain or smth up
			// to evaluate how good it is to choose THE NODE above THIS SPLIT
				
				
			
		}
		def task = null
		tasks = tasks.sort { it.attractiveness }
		if(! tasks.empty)
			task = tasks.pop()
		else {
			task = null
			return task
		}
		
		def amounts = [turtle.rice*100 + turtle.hunger, turtle.water*100 + turtle.thirst]
		
		while(true) {
			if(! tasks.empty) {
				task = tasks.pop()
				
				if (! task.already_evaluated[0]) {
					task = evaluateBuyTask(task)
					tasks.add(task)
					tasks = tasks.sort {it.attractiveness}
					continue
				}
				
				if(! checkIfPossibleToFinish(task, amounts))
					continue
				return task
				
				
			}
			else {
				task = null
				return task
			}
		}
		
		
		
	}
	
	def evaluateBuyTask(TaskStructure task) {
		task.already_evaluated[0] = true
		def goldCost = task.targets[0].discountedMeanPrice(task.needSoFar[0]) * task.actions[0]['amount']
		def canSpendGold = needResAmount < turtle.panicThreshold ? turtle.gold : turtle.gold - turtle.goldSaving
		if( canSpendGold >= goldCost)
			return task
		else {
			def subTask = needGold('gold', task, goldCost - canSpendGold)
			return task.merge(subTask)
		}
			
	}
	
	
	def evaluateSellTask(TaskStructure task) {
		task.already_evaluated[0] = true
		
		def resourceCost = Math.ceil(task.actions[0]['wantGetGoldAmount']/task.targets[0].discountedMeanPrice(task.actions[0]['resource'])).toInteger()
		
		def resourceHave =   (task.actions[0]['resource'].equals('rice') ? turtle.rice : turtle.water) - turtle.panicThreshold * turtle.overPanicBuyFactor
		
		if( resourceHave >= resourceCost)
			return task
		else {
			def subTask = needRiceOrWater(task.actions[0]['resource'], task, (resourceCost-resourceHave).toInteger())
			return task.merge(subTask)
			
		}
			
	}
	
	
	def checkIfPossibleToFinish(TaskStructure ta, def amounts) {
		
		amounts.each { if (it - ta.cost <=0) return false }
		return true
	}
	
	
}
