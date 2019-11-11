package market.relogo
import market.ReLogoTurtle
class TaskAttractiveness {
	float attractiveness
	float cost
	float gain
	ReLogoTurtle target
	def resource
	
	def TaskAttractiveness(float attractiveness, float cost, float gain, ReLogoTurtle target, def resource) {
		this.attractiveness = attractiveness
		this.cost = cost
		this.gain = gain
		this.target = target
		this.resource = resource
	}
}
