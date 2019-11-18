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

class Resource extends ReLogoTurtle {
	def type
	def registered = [:]
	def max_resource
	def resource_renewal
	def current_resource
	def r = 0.5
	def base_mine = 1.5
	int id
	def construct(int id, def resource_type, def r_factor=0, def max_resource=60, def resource_renewal=1) {
		this.id = id
		this.type = resource_type
		this.r = r_factor
		this.max_resource = max_resource
		this.current_resource = max_resource
		this.resource_renewal = resource_renewal
	}
	
	def register(Trader trader) {
		registered.put(trader.id, [trader, 0])
	}
	
	def sumOfArithmeticSequence(def n, def n1, def change) {
		 return (n1 + (n - 1)*change/2) * n
	}
	
	def unregister(Trader trader) {
		def record = registered.get(trader.id)
		def elapsed_time = record[1]
		registered.remove(trader.id)
		def mined = sumOfArithmeticSequence(elapsed_time, base_mine, r)
		mined = Math.round(mined).toInteger()
		increaseTraderRes(trader, mined)
	}
	
	def increaseTraderRes(Trader trader, def mined) {
		if(type.equals('gold')) trader.gold += mined
		if(type.equals('rice')) trader.rice += mined
		if(type.equals('water')) trader.water += mined
		
		//println("Trader " + trader.id +" mined " + mined + " " + type +".")
	}
	
	def step() {
		
		List toDel = []
		registered.each { traderId, record ->
			
			def time = record[1]
			def trader = record[0]
			if(trader.alive) {
			def increment = current_resource/max_resource
			time = time + increment
			registered[traderId][1] = time
			current_resource = current_resource - increment
			trader.mine()
			}
			else {
				toDel.add(trader)
			}
		}
		toDel.each {
			unregister(it)
		}
		def delta_resource = max_resource - current_resource
		if(delta_resource>0 && delta_resource>resource_renewal) {
			current_resource = current_resource + resource_renewal
		} else if (delta_resource>0 && delta_resource<resource_renewal) {
			current_resource = max_resource
		}
		//println("Current " + this.type+" in mine: " + current_resource)
		
	}
	
}
