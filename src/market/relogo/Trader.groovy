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
	
	int riceNeed
	int waterNeed
	int needThreshold = 25
	boolean alive = true
	String state = 'free'
	def construct(int id, int rice, int water, int gold, int riceNeed, int waterNeed) {
		this.id = id
		this.rice = rice
		this.water = water
		this.gold = gold
		this.riceNeed = riceNeed
		this.waterNeed = waterNeed
	}
	
	def finishedTask() {
		
	}
	
	
	def step() {
		if(alive) {
			if(riceNeed <=0 || waterNeed <= 0) {
				alive = false
				println("trader dead")
				die()
				return
			}
			riceNeed -=1
			waterNeed -=1
			
			if(riceNeed<=needThreshold && rice >0) {
				rice -=1
				riceNeed+=100
				println("rice eat")
			}
			if(waterNeed<=needThreshold && water >0) {
				water -=1
				waterNeed+=100
				println("water drink")
			}
			
		}
		
	}
	
}
