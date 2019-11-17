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
	def r = 1
	def base_mine = 1
	
	def construct(def resource_type, def r_factor, def max_resource, def resource_renewal) {
		this.type = resource_type
		this.r = r_factor
		this.max_resource = max_resource
		this.current_resource = max_resource
		this.resource_renewal = resource_renewal
	}
	
	def register(Trader trader) {
		registered.put(trader.id, 0)
	}
	
	def sumOfArithmeticSequence(int n, def n1, def change) {
		 return (n1 + (n - 1)*change/2) * n
	}
	
	def unregister(Trader trader) {
		def elapsed_time = registered.get(trader.id)
		registered.remove(trader.id)
		def mined = sumOfArithmeticSequence(elapsed_time, base_mine, r)
		return mined
	}
	
	def step() {
		registered.each { trader, time ->
			def increment = current_resource/max_resource
			time = time + increment
			current_resource = current_resource - increment
		}
		def delta_resource = max_resource - current_resource
		if(delta_resource>0 && delta_resource>resource_renewal) {
			current_resource = current_resource + resource_renewal
		} else if (delta_resource>0 && delta_resource<resource_renewal) {
			current_resource = max_resource
		}
		
	}
	
}
