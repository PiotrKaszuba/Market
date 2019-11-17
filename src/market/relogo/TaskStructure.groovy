package market.relogo
import market.ReLogoTurtle
class TaskStructure {
	
	float attractiveness
	float cost
	float gain
	
	List<ReLogoTurtle> targets
	List actions
	List needSoFar
	List already_evaluated
	
	def TaskStructure(float attractiveness = 0, float cost = 0, float gain = 0, List<ReLogoTurtle> targets = [], List actions = [], List needSoFar = [], List already_evaluated = []) {
		this.cost = cost
		this.gain = gain
		this.targets = targets
		this.actions = actions
		this.needSoFar = needSoFar
		this.already_evaluated = already_evaluated
	}
	
	def merge(TaskStructure subTask) {
		this.cost = this.cost + subTask.cost
		this.gain = this.gain + subTask.gain
		this.targets.addAll(subTask.targets)
		this.actions.addAll(subTask.actions)
		this.needSoFar.addAll(subTask.needSoFar)
		this.already_evaluated.addAll(subTask.already_evaluated)
		this.attractiveness = this.gain - this.cost
		return this
	}
	
}
