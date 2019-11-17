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
	int gold
	int panicThreshold = 10
	def overPanicBuyFactor = 1.5
	int hunger
	int thirst
	int travelCost = 1
	int needThreshold = 25
	def task = null
	List taskSteps = null
	def miningFatigue = 10
	int ambition
	boolean alive = true
	String state = 'free'
	def velocity = 1
	def goldSaving = 0
	def construct(int id, int rice, int water, int gold, int riceNeed, int waterNeed, int ambition) {
		this.id = id
		this.rice = rice
		this.water = water
		this.gold = gold
		this.hunger = riceNeed
		this.thirst = waterNeed
		this.ambition = ambition
	}
	
	def finishedTask() {
		task = null
		taskSteps = null
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
		
		this.taskSteps =  sb.invokeTask().toTaskSteps()
		this.task=taskSteps
			
			
	}
			
	def sellPriceAndAmount(Market market, def resource, def globalPrice, def wantToGainGold) {
		def resourceAmount = resource.equals('rice') ? rice : water
		def localPrice = market.discountedMeanPrice(resource)
		def resourceToSpend = max(0, resourceAmount - panicThreshold)
		
		def howMuchShouldSell = wantToGainGold/localPrice
		
		def howMuchWillSell = min(Math.ceil(howMuchShouldSell), Math.ceil((resourceToSpend + howMuchShouldSell/3) * (resourceAmount-1)/panicThreshold  ) )
		
		def howMuchWantAfford = resourceToSpend*localPrice
		//def howMuchCanAfford = gold/localPrice
		
		def howMuchWantedToBuyButDontWantToSpend = wantToGainGold - howMuchWantAfford
		
		def meanResLeft = market.meanOfResourceLft(resource)
		def meanSoldPerStep = market.meanAmountSoldPerStep(resource)
		
		def ambitionBasedAnger = localGlobalAngerFunction(globalPrice, localPrice)
		
		def price = (localPrice - ambitionBasedAnger + meanSoldPerStep - meanResLeft/5 + howMuchWantedToBuyButDontWantToSpend/12  )*(Math.random()/4+0.875)
		
		def ret = [price, howMuchWillSell]
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
				
				
				def price = (localPrice + ambitionBasedAnger + meanSoldPerStep - meanResLeft/5 - howMuchWantedToBuyButDontWantToSpend/8 + panicThreshold/(resourceAmount+0.2*panicThreshold) -1  )*(Math.random()/4+0.875)
				def ret = [price, wantToBuy]
				return ret
			}
			
	
	def unregister_after_timeout(turtle) {
		if(state.equals('regitered') && taskSteps[0].get('timeout')>0) {
			taskSteps[0].put('timeout',taskSteps[0].get('timeout') - 1)
		}
		else {
			state = 'free'
			taskSteps[0].get('target').unregister(turtle)
			taskSteps.remove(0)
		}
	}
	
	def continueTask(def globalPrice) {
		if(! taskSteps.isEmpty()) {
			if(taskSteps[0].get('do').equals('goto')) {
				if(abs(xcor-taskSteps[0].get('target').xcor) <= 1 && abs(ycor-taskSteps[0].get('target').ycor) <=1) {
					taskSteps.remove(0)
				}else {
					face(taskSteps[0].get('target'))
					forward(velocity)
					thirst -= travelCost
					hunger -= travelCost
				}
			}
			else if(taskSteps[0].get('do').equals('buy')){
				if(state.equals("free")) {
					def priceAmount = buyPriceAndAmount(taskSteps[0]['target'], taskSteps[0]['resource'], globalPrice[taskSteps[0]['resource']], taskSteps[0]['amount'])
					def price = priceAmount[0]
					//def amount = priceAmount[1]
					
					taskSteps[0].get('target').register(this, false, taskSteps[0].get('resource').equals('rice'), taskSteps[0].get('amount'), price)
					state = 'registered'
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
					taskSteps[0].get('target').register(this, true, taskSteps[0].get('resource').equals('rice'), amount, price)
					state = 'registered'
				}
				else {
					unregister_after_timeout(this)
				}
			}
			else(taskSteps[0].get('do').equals('mine')){
				if(state.equals("free")) {
					taskSteps[0].get('target').register(this)
					state = 'registered'
				}
				else {
					unregister_after_timeout(this)
				}
			}
		}
		if (taskSteps.isEmpty()) finishedTask()
	}
	
	def mine() {
		thirst -= miningFatigue
		hunger -= miningFatigue
	}
	def step(def globalPrice) {
		if(alive) {
			if(hunger <=0 || thirst <= 0) {
				alive = false
				println("trader dead")
				die()
				return
			}
			hunger -=1
			thirst -=1
			
			if(hunger<=needThreshold && rice >0) {
				rice -=1
				hunger+=100
				println("rice eat")
			}
			if(thirst<=needThreshold && water >0) {
				water -=1
				thirst+=100
				println("water drink")
			}
			
			
			if(task != null) {
				continueTask(globalPrice)
				return
			}
			
			
			
			if(water<panicThreshold || rice<panicThreshold) {
				panic(globalPrice)
				return
			}
			
			left(random(90))
			right(random(90))
			forward(random(velocity))
			thirst -= travelCost
			hunger -= travelCost
			
			
			
			
		}
		
	}
	
}
