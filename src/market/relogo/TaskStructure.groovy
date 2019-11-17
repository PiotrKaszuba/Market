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
	
	def TaskStructure(def attractiveness = 0, def cost = 0, def gain = 0, List<ReLogoTurtle> targets = [], List actions = [], List needSoFar = [], List already_evaluated = []) {
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
	
	def toTaskSteps() {
		
		List taskSteps = []
		for(int i =0;i< this.actions.size(); i++ ) {
			taskSteps.add(['do' : 'goto', 'target' : targets[i] ]);
			taskSteps.add(actions[i])
		}
		return taskSteps
	}
	
}
