package market.relogo
import market.ReLogoTurtle
class TaskStructure {
	
	def attractiveness
	def cost
	def gain
	def initialAmounts = []
	List<ReLogoTurtle> targets
	List actions
	List needSoFar
	List already_evaluated
	
	def TaskStructure(def attractiveness, def cost , def gain , List<ReLogoTurtle> targets , List actions , List needSoFar , List already_evaluated ) {
		this.attractiveness = attractiveness
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
		this.attractiveness = this.attractiveness + subTask.attractiveness
		this.attractiveness -= Math.abs(subTask.attractiveness) * 0.5
		return this
	}
	
	def toTaskSteps() {
		
		List taskSteps = []
		for(int i=this.actions.size()-1; i>= 0; i-- ) {
			taskSteps.add(['do' : 'goto', 'target' : targets[i]]);
			taskSteps.add(actions[i])
		}
		return taskSteps
	}
	
}
