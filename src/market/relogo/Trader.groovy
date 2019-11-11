package market.relogo

import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*

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
	boolean alive = true
	String state = 'free'
	def construct(int id, int rice, int water, int gold, int riceNeed, int waterNeed) {
		this.id = id
		this.rice = rice
		this.water = water
		this.gold = gold
		this.hunger = riceNeed
		this.thirst = waterNeed
	}
	
	def finishedTask() {
		
	}
	
	def panic() {
		def panicRes
		def otherResourceRisk
		if(water<=rice) {
			panicRes = 'water'
			otherResourceRisk = sqrt(max(1, 1.2*panicThreshold-rice))
		}
		else {
			panicRes = 'rice'
			otherResourceRisk = (panicThreshold*1.2)/(rice+1)
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
				def gain = it.meanAmountSoldPerStep(panicRes)		
			}
		}
	}
	
	def step() {
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
			if(water<panicThreshold || rice<panicThreshold) {
				panic()
			}
			
			
			
			
		}
		
	}
	
}
