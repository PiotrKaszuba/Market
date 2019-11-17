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
	def velocity = 5
	
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
		if(water<=rice) {
			panicRes = 'water'
			otherResourceRisk = sqrt(max(1, 1.2*panicThreshold-rice))
			panicResConsumeAmount = water*100+thirst
			otherResConsumeAmount = rice*100+hunger
		}
		else {
			panicRes = 'rice'
			otherResourceRisk = (panicThreshold*1.2)/(rice+1)
			panicResConsumeAmount = rice*100+hunger
			otherResConsumeAmount = water*100+thirst
		}
		
		List<TaskAttractiveness> taskAtts = []
		
		ask(turtles()){
			def dist = distance(it)
			def cost = dist * travelCost + dist
			if(it instanceof Resource && it.type.equals(panicRes)) {
				
				def gain = 5 + it.r * 10
				def attractiveness = (gain - cost - cost*otherResourceRisk)*(Math.random()/2.5 + 0.6)
				taskAtts.add(new TaskAttractiveness(attractiveness, cost, gain, it, panicRes))
			}
			if(it instanceof Market && it.alive) {
				def gain = it.meanAmountSoldPerStep(panicRes) + 0.2 * it.meanOfResourceLft(panicRes)
				def priceDiff = globalPrice[panicRes]-it.discountedMeanPrice(panicRes)
				def attractiveness = (gain - + priceDiff - cost - cost*otherResourceRisk) * (Math.random()/2.5 + 0.6)
				taskAtts.add(new TaskAttractiveness(attractiveness, cost, gain, it, panicRes))
			}
		}
		taskAtts = taskAtts.sort { it.attractiveness }
		if(! taskAtts.empty)
			task = taskAtts.pop()
		else 
			task = null
		while(! checkIfPossibleToFinish(task, panicResConsumeAmount)) {
			if(! taskAtts.empty)
				task = taskAtts.pop()
			else
				task = null
		}
		if(task==null) return
		
		
		def taskGoal
		def timeout
		if (task.target instanceof Market) {
			taskGoal = 'buy'
			timeout = 20 + ambition * 3
		}
		else {
			taskGoal = 'mine'
			timeout = 3 + ambition
			
			def timeoutCost = timeout * miningFatigue
			
			def otherResLeft = otherResConsumeAmount - task.cost - timeoutCost
			
			def timeoutFactor = min(1, otherResLeft/ (panicThreshold*100))
			
			//here
			timeout =  max(1, Math.floor(timeoutFactor * timeout ).toInteger())
			
			taskSteps = [['do' : 'goto', 'target' : task.target], ['do' : taskGoal, 'timeout' : timeout, 'resource': panicRes ] ]
			
			
		}
		
			}
	
	def checkIfPossibleToFinish(TaskAttractiveness ta, int amount) {
		//here
		if(amount - ta.cost-25 <=0) {
			return false
		}
		else return true
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
	
	def continueTask() {
		if(taskSteps.isEmpty()) {
			if(taskSteps[0].get('do').equals('goto')) {
				if(pxCor()==taskSteps[0].get('target').pxCor() && pyCor()==taskSteps[0].get('target').pyCor()) {
					taskSteps.remove(0)
				}else {
					face(taskSteps[0].get('target'))
					forward(velocity)
				}
			}
			else if(taskSteps[0].get('do').equals('buy')){
				if(state.equals("free")) {
					taskSteps[0].get('target').register(this, false, taskSteps[0].get('resource').equals('rice'), taskSteps[0].get('amount'), taskSteps[0].get('pricePerUnit'))
					state = 'registered'
				}
				else {
					unregister_after_timeout(this)
				}
			}
			else if(taskSteps[0].get('do').equals('sell')){
				if(state.equals("free")) {
					taskSteps[0].get('target').register(this, true, taskSteps[0].get('resource').equals('rice'), taskSteps[0].get('amount'), taskSteps[0].get('pricePerUnit'))
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
		finishedTask()
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
				continueTask()
				return
			}
			
			
			if(water<panicThreshold || rice<panicThreshold) {
				panic(globalPrice)
				return
			}
			
			
			
			
		}
		
	}
	
}
