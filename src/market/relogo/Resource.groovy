package market.relogo

import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF

import javassist.expr.Instanceof
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
	
	Resource(resource_type, r_factor, max_resource, resource_renewal) {
		this.type = resource_type
		this.r = r_factor
		this.max_resource = max_resource
		this.current_resource = max_resource
		this.resource_renewal = resource_renewal
	}
	
	def register(trader) {
		registered.put(trader.id, 0)
	}
	
	def unregister(trader) {
		elapsed_time = registered.get(trader.id)
		registered.remove(trader.id)
		mined = (1 + (elapsed_time - 1)*r/2) * elapsed_time
		mined
	}
	
	def step() {
		registered.each { trader, time ->
			increment = current_resource/max_resource
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