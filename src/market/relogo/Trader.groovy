package market.relogo

import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*

import javax.measure.quantity.Power

import market.ReLogoTurtle
import repast.simphony.relogo.Plural
import repast.simphony.relogo.Stop
import repast.simphony.relogo.Utility
import repast.simphony.relogo.UtilityG
import repast.simphony.relogo.schedule.Go
import repast.simphony.relogo.schedule.Setup

class Trader extends ReLogoTurtle {
	int id
	int rice
	int water
	def gold
	def costAlready = 0
	TaskStructure taskStr = null
	int panicThreshold = 10
	def overPanicBuyFactor = 1.5
	int hunger
	int thirst
	int travelCost = 2
	int needThreshold = 25
	def task = null
	List taskSteps = null
	def miningFatigue = 8
	int ambition
	boolean alive = true
	def traderInfo = null
	String state = 'free'
	def velocity = 1.25
	def goldSaving = 0
	def resourceMiningAbility = ['water':true,'rice':true, 'gold':true]
	def construct(int id, int rice, int water, int gold, int riceNeed, int waterNeed, int ambition) {
		this.id = id
		this.rice = rice
		this.water = water
		this.gold = gold
		this.hunger = riceNeed
		this.thirst = waterNeed
		this.ambition = ambition
	}
	
	def finishedStep() {
		taskSteps.remove(0)
		state = 'free'
	}
	
	def finishedTask() {
		task = null
		taskSteps = null
		state = 'free'
		costAlready = 0
		taskStr = null
	}
	
	def m(def globalPrice, def localPrice, def localPriceCut = 7){
		localPrice = localPrice -7
		def GPriceDiff = Math.abs(localPrice-globalPrice) + globalPrice
		def numerator = Math.abs(localPrice - localPriceCut - globalPrice) + globalPrice
		def denominator = - Math.pow(globalPrice - (localPrice-localPriceCut), 2) * 5/ambition
		
		return numerator/denominator
	}
	
	def w(def globalPrice, def localPrice) {
		def numerator = Math.pow(globalPrice - localPrice, 2) * ambition/5
		def denominator = localPrice
		
		return numerator/denominator
	}
	
	def localGlobalAngerFunction(def globalPrice, def localPrice, def localPriceCut = 7){
		def sign = Math.signum(globalPrice - localPrice)
		if(sign==0) sign = 1
		
		// Function m takes two parameters from `globalPrice`, it is intended behavior
		return sign * ((sign < 0) ? (w(globalPrice, localPrice) - m(globalPrice, globalPrice, localPriceCut)) : m(globalPrice, localPrice, localPriceCut))
	}
	
	def panic(globalPrice) {
		def panicRes
		def otherResourceRisk
		def panicResConsumeAmount
		def otherResConsumeAmount
		def panicResAmount
		def resourceUsageFactors 
		if(water<=rice) {
			panicRes = 'water'
			panicResAmount = water
			otherResourceRisk = Math.sqrt(Math.max(1.0, 1.2*panicThreshold-rice))
			panicResConsumeAmount = water*100+thirst
			otherResConsumeAmount = rice*100+hunger
			resourceUsageFactors = ['rice' : otherResourceRisk, 'water':1]
		}
		else {
			panicRes = 'rice'
			panicResAmount = rice
			otherResourceRisk = Math.sqrt(Math.max(1.0, 1.2*panicThreshold-water))
			panicResConsumeAmount = rice*100+hunger
			otherResConsumeAmount = water*100+thirst
			resourceUsageFactors = ['water' : otherResourceRisk, 'rice':1]
		}
		
		def needAmount = panicThreshold * overPanicBuyFactor - panicResAmount
		
		
		StandardBehaviour sb = new StandardBehaviour(this, panicRes, needAmount, globalPrice, resourceUsageFactors)
		TaskStructure ts =  sb.invokeTask()
		
		if(ts!= null) {
			costAlready = 0
			this.taskSteps = ts.toTaskSteps()
			this.taskStr = ts
		}
		else 
			this.taskSteps = ts
		this.task=taskSteps
			
			
	}
	def trace() {
		if(id != -1) return
		println("------------------------------")
		println("Ambition: " + ambition)
		println("X: " + xcor +", Y: " + ycor)
		println("Hunger: " + hunger +", Thirst: " + thirst)
		println("Rice: " + rice +", Water: " + water + ", Gold: " + gold)
		println("Task Steps: " + taskSteps != null ? taskSteps : "null")
		println("Task initial cost: " + (taskStr? taskStr.cost : "-")+ "; initial amounts: " + (taskStr ? taskStr.initialAmounts : []))
		println("Cost already: " + this.costAlready)
		println("------------------------------")
	}
	def sellPriceAndAmount(Market market, def resource, def globalPrice, def wantToGainGold) {
		def resourceAmount = resource.equals('rice') ? rice : water
		def localPrice = market.discountedMeanPrice(resource)
		def resourceToSpend = Math.max(0, resourceAmount - panicThreshold).toInteger()
		
		def howMuchShouldSell = wantToGainGold/localPrice
		
		def howMuchWillSell = Math.min(Math.ceil(howMuchShouldSell), Math.ceil((resourceToSpend + howMuchShouldSell/3) * (resourceAmount-1)/panicThreshold  ) ).toInteger()
		
		def howMuchWantAfford = resourceToSpend*localPrice
		//def howMuchCanAfford = gold/localPrice
		
		def howMuchWantedToBuyButDontWantToSpend = wantToGainGold - howMuchWantAfford
		
		def meanResLeft = market.meanOfResourceLft(resource)
		def meanSoldPerStep = market.meanAmountSoldPerStep(resource)
		
		def goldFactor = Math.log((resourceAmount-panicThreshold/2)/panicThreshold +1) * Math.log(gold/10+1) * 2/ambition
		
		
		
		def ambitionBasedAnger = localGlobalAngerFunction(globalPrice, localPrice)
		//println(ambitionBasedAnger)
		def price = ((localPrice - ambitionBasedAnger + meanSoldPerStep*2 - meanResLeft/4 - goldFactor)*(Math.random()/4+0.85))
		price = price >= 1 ? price : 1/(2-price)
		def ret = [price, Math.max(1,howMuchWillSell)]
		return ret
		
	}
	
	def buyPriceAndAmount(Market market, def resource, def globalPrice, def wantToBuy) {
				
				
				//ile ma
				//ile chcia³by kupiæ?
				//ile w sumie mo¿e kupiæ za swoje z³oto bazujac na dMP
				
				//no to do progu 1.5*panicThresh za cene rynkow¹ chcemy kupiæ
				
				// pytanie - jak atrakcyjna jest dla nas cena rynkowa??
				
				// od czego zalezy atrakcyjnosc ceny na rynku?
				
				//1. cena globalna
				//2. moja potrzeba - jak ma³o mam tego zasobu??/ jak du¿o mi brakuje/czy ryzyko œmierci?
				//3. prawdopodobieñstwo zakupu w krótkim czasie - jakim? jak bardoz liczy siê krótki czas? ryzyko smierci? jakie prawdop? - analiza statystyk marketu
				//4. ambicja -> chêæ zyskania a nie straty -> taniej!!
				//5. "humor" -> stochastycznoœæ!!!
				//6. ile mam z³ota / ile mogê kupic
				
				def goldToSpendAdditionally = Math.max(0,gold - goldSaving)
				def resourceAmount = resource.equals('rice') ? rice : water
				//def wantToBuy = 1.5*panicThreshold - resourceAmount
				
				
				def localPrice = market.discountedMeanPrice(resource)
				
				
				def howMuchWantAfford = goldToSpendAdditionally/localPrice
				//def howMuchCanAfford = gold/localPrice
				
				def howMuchWantedToBuyButDontWantToSpend = wantToBuy - howMuchWantAfford
				
				def meanResLeft = market.meanOfResourceLft(resource)
				def meanSoldPerStep = market.meanAmountSoldPerStep(resource)
				
				def ambitionBasedAnger = localGlobalAngerFunction(globalPrice, localPrice)
				
				def goldFactor = Math.log((resourceAmount-panicThreshold/2)/panicThreshold +1) * Math.log(gold/10+1) * 2/ambition
				def price = (localPrice + ambitionBasedAnger + meanSoldPerStep*2 - meanResLeft/4 - howMuchWantedToBuyButDontWantToSpend/8 + goldFactor + panicThreshold/(resourceAmount+0.1*panicThreshold) -1  )*(Math.random()/4+0.9)
				price = price >= 1 ? price : 1/(2-price)
				def ret = [price, Math.max(1, Math.min(wantToBuy, Math.floor(gold/price)))]
				return ret
			}
			
	
	def unregister_after_timeout(turtle) {
		if(state.equals('registered') && taskSteps[0].get('timeout')>0) {
			taskSteps[0].put('timeout',taskSteps[0].get('timeout') - 1)
			//println("Timeout! " + taskSteps[0].get('timeout'))
		}
		else {
			state = 'free'
			taskSteps[0].get('target').unregister(turtle)
			taskSteps.remove(0)
			finishedTask()
			//println("Unregister")
		}
	}
	
	def continueTask(def globalPrice) {
		if(! taskSteps.isEmpty()) {
			if(taskSteps[0].get('do').equals('goto')) {
				if(abs(xcor-taskSteps[0].get('target').xcor) <= 1.5 && abs(ycor-taskSteps[0].get('target').ycor) <=1.5) {
					taskSteps.remove(0)
				}else {
					face(taskSteps[0].get('target'))
					forward(velocity)
					thirst -= travelCost
					hunger -= travelCost
					costAlready += travelCost
				}
			}
			else if(taskSteps[0].get('do').equals('buy')){
				if(state.equals("free")) {
					def priceAmount = buyPriceAndAmount(taskSteps[0]['target'], taskSteps[0]['resource'], globalPrice[taskSteps[0]['resource']], taskSteps[0]['amount'])
					def price = priceAmount[0]
					def amount = priceAmount[1]
					
					def ret = taskSteps[0].get('target').register(this, false, taskSteps[0].get('resource').equals('rice'), amount.toInteger(), price)
					
					if(ret) {
						state = 'registered'
					
					}else
						finishedTask()

				}
				else {
					unregister_after_timeout(this)
				}
			}
			else if(taskSteps[0].get('do').equals('sell')){
				if(state.equals("free")) {
					def priceAmount = sellPriceAndAmount(taskSteps[0]['target'], taskSteps[0]['resource'], globalPrice[taskSteps[0]['resource']], taskSteps[0]['wantGetGoldAmount'])
					def price = priceAmount[0]
					def amount = priceAmount[1]
					def ret = taskSteps[0].get('target').register(this, true, taskSteps[0].get('resource').equals('rice'), amount, price)
					
					if(ret) {
						state = 'registered'
					
					}
					else
						finishedTask()
					
					
				}
				else {
					unregister_after_timeout(this)
				}
			}
			else if(taskSteps[0].get('do').equals('mine')){
				if(state.equals("free")) {
					taskSteps[0].get('target').register(this)
					state = 'registered'
				}
				else {
					unregister_after_timeout(this)
				}
			}
		}
		if (taskSteps != null && taskSteps.isEmpty()) finishedTask()
	}
	
	def mine() {
		thirst -= miningFatigue
		hunger -= miningFatigue
		costAlready += miningFatigue
	}
	
	def eat() {
		if(hunger<=needThreshold && rice >0) {
			rice -=1
			hunger+=100
			//println("rice eat")
		}
		if(thirst<=needThreshold && water >0) {
			water -=1
			thirst+=100
			//println("water drink")
		}
	}
	
	def deathCheck() {
		if(hunger <=0 || thirst <= 0) {
			if(state=='registered' && taskSteps != null) {
				taskSteps[0]['target'].unregister(this)
				state = 'free'
				eat()
				deathCheck()
			}
			alive = false
			println("+++++++++++++++++")
			println("Trader DEAD;  id: " + id + ", rice: " + rice + ", water: " + water + ", gold: " + gold)
			println("Task Steps: " + taskSteps)
			println("State: " + state)
			println("+++++++++++++++++")
			die()
			return true
		}
		return false
	}
	
	def step(def globalPrice) {
		if(alive) {
			if(taskStr!=null && costAlready > taskStr.cost*100)
			{
				//println("-----------------" + costAlready+"--------------BAAAAAD_________________" + taskStr.cost)
			}
			if(deathCheck()) return
			else {
				hunger -=1
				thirst -=1
				costAlready+=1
				
				eat()
				
				
				if(task != null) {
					continueTask(globalPrice)
					
				}else {
					if(water<panicThreshold || rice<panicThreshold) {
						panic(globalPrice)
						
					}
					else
					{
//						left(random(90))
//						right(random(90))
//						forward(random(velocity))
//						thirst -= travelCost
//						hunger -= travelCost
					}
				}
				
				
				
				
				
				
				
			}
			
			
			
			
			
		}
		trace()
		
	}
	
}
