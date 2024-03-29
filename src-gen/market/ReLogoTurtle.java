package market;

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import groovy.lang.Closure;
import repast.simphony.relogo.*;
import repast.simphony.relogo.builder.GeneratedByReLogoBuilder;
import repast.simphony.relogo.builder.ReLogoBuilderGeneratedFor;
import repast.simphony.space.SpatialException;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

@GeneratedByReLogoBuilder
@SuppressWarnings({"unused","rawtypes","unchecked"})
public class ReLogoTurtle extends BaseTurtle{

	/**
	 * Makes a number of new markets and then executes a set of commands on the
	 * created markets.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created markets
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> hatchMarkets(int number, Closure closure) {
		AgentSet<market.relogo.Market> result = new AgentSet<>();
		AgentSet<Turtle> createResult = this.hatch(number,closure,"Market");
		for (Turtle t : createResult){
			if (t instanceof market.relogo.Market){
				result.add((market.relogo.Market)t);
			}
		} 
		return result;
	}

	/**
	 * Makes a number of new markets and then executes a set of commands on the
	 * created markets.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created markets
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> hatchMarkets(int number) {
		return hatchMarkets(number,null);
	}

	/**
	 * Returns an agentset of markets from the patch of the caller.
	 * 
	 * @return agentset of markets from the caller's patch
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> marketsHere(){
	  Grid grid = getMyObserver().getGrid();
	  GridPoint gridPoint = grid.getLocation(this);
	  AgentSet<market.relogo.Market> result = new AgentSet<market.relogo.Market>();
	  for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"market")){
			if (t instanceof market.relogo.Market)
			result.add((market.relogo.Market)t);
		}
		return result;
	}

	/**
	 * Returns the agentset of markets on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 * @returns agentset of markets at the direction (nX, nY) from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> marketsAt(Number nX, Number nY){
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = {dx,dy};

		try{
		GridPoint gridPoint = Utility.getGridPointAtDisplacement(getTurtleLocation(),displacement,getMyObserver());
		AgentSet<market.relogo.Market> result = new AgentSet<market.relogo.Market>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"market")){
			if (t instanceof market.relogo.Market)
			result.add((market.relogo.Market)t);
		}
		return result;
		}
		catch(SpatialException e){
			return new AgentSet<market.relogo.Market>();
		}
	}

	/**
	 * Returns an agentset of markets on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of markets on patch p
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> marketsOn(Patch p){
		AgentSet<market.relogo.Market> result = new AgentSet<market.relogo.Market>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(p.getGridLocation(),getMyObserver(),"market")){
			if (t instanceof market.relogo.Market)
			result.add((market.relogo.Market)t);
		}
		return result;
	}

	/**
	 * Returns an agentset of markets on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of markets on the same patch as turtle t
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> marketsOn(Turtle t){
		AgentSet<market.relogo.Market> result = new AgentSet<market.relogo.Market>();						
		for (Turtle tt : Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),getMyObserver(),"market")){
			if (tt instanceof market.relogo.Market)
			result.add((market.relogo.Market)tt);
		}
		return result;
	}

	/**
	 * Returns an agentset of markets on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of markets on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> marketsOn(Collection c){

		if (c == null || c.isEmpty()){
			return new AgentSet<market.relogo.Market>();
		}

		Set<market.relogo.Market> total = new HashSet<market.relogo.Market>();
		if (c.iterator().next() instanceof Turtle){
			for (Object o : c){
				if (o instanceof Turtle){
					Turtle t = (Turtle) o;
					total.addAll(marketsOn(t));
				}
			}
		}
		else {
			for (Object o : c){
				if (o instanceof Patch){
					Patch p = (Patch) o;
					total.addAll(marketsOn(p));
				}
			}
		}
		return new AgentSet<market.relogo.Market>(total);
	}

	/**
	 * Queries if object is a market.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a market
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public boolean isMarketQ(Object o){
		return (o instanceof market.relogo.Market);
	}

	/**
	 * Returns an agentset containing all markets.
	 * 
	 * @return agentset of all markets
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public AgentSet<market.relogo.Market> markets(){
		AgentSet<market.relogo.Market> a = new AgentSet<market.relogo.Market>();
		for (Object e : this.getMyObserver().getContext().getObjects(market.relogo.Market.class)) {
			if (e instanceof market.relogo.Market){
				a.add((market.relogo.Market)e);
			}
		}
		return a;
	}

	/**
	 * Returns the market with the given who number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Market")
	public market.relogo.Market market(Number number){
		Turtle turtle = Utility.turtleU(number.intValue(), getMyObserver());
		if (turtle instanceof market.relogo.Market)
			return (market.relogo.Market) turtle;
		return null;
	}

	/**
	 * Makes a number of new resources and then executes a set of commands on the
	 * created resources.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created resources
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> hatchResources(int number, Closure closure) {
		AgentSet<market.relogo.Resource> result = new AgentSet<>();
		AgentSet<Turtle> createResult = this.hatch(number,closure,"Resource");
		for (Turtle t : createResult){
			if (t instanceof market.relogo.Resource){
				result.add((market.relogo.Resource)t);
			}
		} 
		return result;
	}

	/**
	 * Makes a number of new resources and then executes a set of commands on the
	 * created resources.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created resources
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> hatchResources(int number) {
		return hatchResources(number,null);
	}

	/**
	 * Returns an agentset of resources from the patch of the caller.
	 * 
	 * @return agentset of resources from the caller's patch
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> resourcesHere(){
	  Grid grid = getMyObserver().getGrid();
	  GridPoint gridPoint = grid.getLocation(this);
	  AgentSet<market.relogo.Resource> result = new AgentSet<market.relogo.Resource>();
	  for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"resource")){
			if (t instanceof market.relogo.Resource)
			result.add((market.relogo.Resource)t);
		}
		return result;
	}

	/**
	 * Returns the agentset of resources on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 * @returns agentset of resources at the direction (nX, nY) from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> resourcesAt(Number nX, Number nY){
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = {dx,dy};

		try{
		GridPoint gridPoint = Utility.getGridPointAtDisplacement(getTurtleLocation(),displacement,getMyObserver());
		AgentSet<market.relogo.Resource> result = new AgentSet<market.relogo.Resource>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"resource")){
			if (t instanceof market.relogo.Resource)
			result.add((market.relogo.Resource)t);
		}
		return result;
		}
		catch(SpatialException e){
			return new AgentSet<market.relogo.Resource>();
		}
	}

	/**
	 * Returns an agentset of resources on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of resources on patch p
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> resourcesOn(Patch p){
		AgentSet<market.relogo.Resource> result = new AgentSet<market.relogo.Resource>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(p.getGridLocation(),getMyObserver(),"resource")){
			if (t instanceof market.relogo.Resource)
			result.add((market.relogo.Resource)t);
		}
		return result;
	}

	/**
	 * Returns an agentset of resources on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of resources on the same patch as turtle t
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> resourcesOn(Turtle t){
		AgentSet<market.relogo.Resource> result = new AgentSet<market.relogo.Resource>();						
		for (Turtle tt : Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),getMyObserver(),"resource")){
			if (tt instanceof market.relogo.Resource)
			result.add((market.relogo.Resource)tt);
		}
		return result;
	}

	/**
	 * Returns an agentset of resources on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of resources on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> resourcesOn(Collection c){

		if (c == null || c.isEmpty()){
			return new AgentSet<market.relogo.Resource>();
		}

		Set<market.relogo.Resource> total = new HashSet<market.relogo.Resource>();
		if (c.iterator().next() instanceof Turtle){
			for (Object o : c){
				if (o instanceof Turtle){
					Turtle t = (Turtle) o;
					total.addAll(resourcesOn(t));
				}
			}
		}
		else {
			for (Object o : c){
				if (o instanceof Patch){
					Patch p = (Patch) o;
					total.addAll(resourcesOn(p));
				}
			}
		}
		return new AgentSet<market.relogo.Resource>(total);
	}

	/**
	 * Queries if object is a resource.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a resource
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public boolean isResourceQ(Object o){
		return (o instanceof market.relogo.Resource);
	}

	/**
	 * Returns an agentset containing all resources.
	 * 
	 * @return agentset of all resources
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public AgentSet<market.relogo.Resource> resources(){
		AgentSet<market.relogo.Resource> a = new AgentSet<market.relogo.Resource>();
		for (Object e : this.getMyObserver().getContext().getObjects(market.relogo.Resource.class)) {
			if (e instanceof market.relogo.Resource){
				a.add((market.relogo.Resource)e);
			}
		}
		return a;
	}

	/**
	 * Returns the resource with the given who number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Resource")
	public market.relogo.Resource resource(Number number){
		Turtle turtle = Utility.turtleU(number.intValue(), getMyObserver());
		if (turtle instanceof market.relogo.Resource)
			return (market.relogo.Resource) turtle;
		return null;
	}

	/**
	 * Makes a number of new traders and then executes a set of commands on the
	 * created traders.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created traders
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> hatchTraders(int number, Closure closure) {
		AgentSet<market.relogo.Trader> result = new AgentSet<>();
		AgentSet<Turtle> createResult = this.hatch(number,closure,"Trader");
		for (Turtle t : createResult){
			if (t instanceof market.relogo.Trader){
				result.add((market.relogo.Trader)t);
			}
		} 
		return result;
	}

	/**
	 * Makes a number of new traders and then executes a set of commands on the
	 * created traders.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created traders
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> hatchTraders(int number) {
		return hatchTraders(number,null);
	}

	/**
	 * Returns an agentset of traders from the patch of the caller.
	 * 
	 * @return agentset of traders from the caller's patch
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> tradersHere(){
	  Grid grid = getMyObserver().getGrid();
	  GridPoint gridPoint = grid.getLocation(this);
	  AgentSet<market.relogo.Trader> result = new AgentSet<market.relogo.Trader>();
	  for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"trader")){
			if (t instanceof market.relogo.Trader)
			result.add((market.relogo.Trader)t);
		}
		return result;
	}

	/**
	 * Returns the agentset of traders on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 * @returns agentset of traders at the direction (nX, nY) from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> tradersAt(Number nX, Number nY){
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = {dx,dy};

		try{
		GridPoint gridPoint = Utility.getGridPointAtDisplacement(getTurtleLocation(),displacement,getMyObserver());
		AgentSet<market.relogo.Trader> result = new AgentSet<market.relogo.Trader>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"trader")){
			if (t instanceof market.relogo.Trader)
			result.add((market.relogo.Trader)t);
		}
		return result;
		}
		catch(SpatialException e){
			return new AgentSet<market.relogo.Trader>();
		}
	}

	/**
	 * Returns an agentset of traders on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of traders on patch p
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> tradersOn(Patch p){
		AgentSet<market.relogo.Trader> result = new AgentSet<market.relogo.Trader>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(p.getGridLocation(),getMyObserver(),"trader")){
			if (t instanceof market.relogo.Trader)
			result.add((market.relogo.Trader)t);
		}
		return result;
	}

	/**
	 * Returns an agentset of traders on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of traders on the same patch as turtle t
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> tradersOn(Turtle t){
		AgentSet<market.relogo.Trader> result = new AgentSet<market.relogo.Trader>();						
		for (Turtle tt : Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),getMyObserver(),"trader")){
			if (tt instanceof market.relogo.Trader)
			result.add((market.relogo.Trader)tt);
		}
		return result;
	}

	/**
	 * Returns an agentset of traders on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of traders on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> tradersOn(Collection c){

		if (c == null || c.isEmpty()){
			return new AgentSet<market.relogo.Trader>();
		}

		Set<market.relogo.Trader> total = new HashSet<market.relogo.Trader>();
		if (c.iterator().next() instanceof Turtle){
			for (Object o : c){
				if (o instanceof Turtle){
					Turtle t = (Turtle) o;
					total.addAll(tradersOn(t));
				}
			}
		}
		else {
			for (Object o : c){
				if (o instanceof Patch){
					Patch p = (Patch) o;
					total.addAll(tradersOn(p));
				}
			}
		}
		return new AgentSet<market.relogo.Trader>(total);
	}

	/**
	 * Queries if object is a trader.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a trader
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public boolean isTraderQ(Object o){
		return (o instanceof market.relogo.Trader);
	}

	/**
	 * Returns an agentset containing all traders.
	 * 
	 * @return agentset of all traders
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public AgentSet<market.relogo.Trader> traders(){
		AgentSet<market.relogo.Trader> a = new AgentSet<market.relogo.Trader>();
		for (Object e : this.getMyObserver().getContext().getObjects(market.relogo.Trader.class)) {
			if (e instanceof market.relogo.Trader){
				a.add((market.relogo.Trader)e);
			}
		}
		return a;
	}

	/**
	 * Returns the trader with the given who number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.Trader")
	public market.relogo.Trader trader(Number number){
		Turtle turtle = Utility.turtleU(number.intValue(), getMyObserver());
		if (turtle instanceof market.relogo.Trader)
			return (market.relogo.Trader) turtle;
		return null;
	}

	/**
	 * Makes a number of new userTurtles and then executes a set of commands on the
	 * created userTurtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created userTurtles
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> hatchUserTurtles(int number, Closure closure) {
		AgentSet<market.relogo.UserTurtle> result = new AgentSet<>();
		AgentSet<Turtle> createResult = this.hatch(number,closure,"UserTurtle");
		for (Turtle t : createResult){
			if (t instanceof market.relogo.UserTurtle){
				result.add((market.relogo.UserTurtle)t);
			}
		} 
		return result;
	}

	/**
	 * Makes a number of new userTurtles and then executes a set of commands on the
	 * created userTurtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created userTurtles
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> hatchUserTurtles(int number) {
		return hatchUserTurtles(number,null);
	}

	/**
	 * Returns an agentset of userTurtles from the patch of the caller.
	 * 
	 * @return agentset of userTurtles from the caller's patch
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> userTurtlesHere(){
	  Grid grid = getMyObserver().getGrid();
	  GridPoint gridPoint = grid.getLocation(this);
	  AgentSet<market.relogo.UserTurtle> result = new AgentSet<market.relogo.UserTurtle>();
	  for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"userTurtle")){
			if (t instanceof market.relogo.UserTurtle)
			result.add((market.relogo.UserTurtle)t);
		}
		return result;
	}

	/**
	 * Returns the agentset of userTurtles on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 * @returns agentset of userTurtles at the direction (nX, nY) from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> userTurtlesAt(Number nX, Number nY){
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = {dx,dy};

		try{
		GridPoint gridPoint = Utility.getGridPointAtDisplacement(getTurtleLocation(),displacement,getMyObserver());
		AgentSet<market.relogo.UserTurtle> result = new AgentSet<market.relogo.UserTurtle>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"userTurtle")){
			if (t instanceof market.relogo.UserTurtle)
			result.add((market.relogo.UserTurtle)t);
		}
		return result;
		}
		catch(SpatialException e){
			return new AgentSet<market.relogo.UserTurtle>();
		}
	}

	/**
	 * Returns an agentset of userTurtles on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of userTurtles on patch p
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> userTurtlesOn(Patch p){
		AgentSet<market.relogo.UserTurtle> result = new AgentSet<market.relogo.UserTurtle>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(p.getGridLocation(),getMyObserver(),"userTurtle")){
			if (t instanceof market.relogo.UserTurtle)
			result.add((market.relogo.UserTurtle)t);
		}
		return result;
	}

	/**
	 * Returns an agentset of userTurtles on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of userTurtles on the same patch as turtle t
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> userTurtlesOn(Turtle t){
		AgentSet<market.relogo.UserTurtle> result = new AgentSet<market.relogo.UserTurtle>();						
		for (Turtle tt : Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),getMyObserver(),"userTurtle")){
			if (tt instanceof market.relogo.UserTurtle)
			result.add((market.relogo.UserTurtle)tt);
		}
		return result;
	}

	/**
	 * Returns an agentset of userTurtles on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of userTurtles on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> userTurtlesOn(Collection c){

		if (c == null || c.isEmpty()){
			return new AgentSet<market.relogo.UserTurtle>();
		}

		Set<market.relogo.UserTurtle> total = new HashSet<market.relogo.UserTurtle>();
		if (c.iterator().next() instanceof Turtle){
			for (Object o : c){
				if (o instanceof Turtle){
					Turtle t = (Turtle) o;
					total.addAll(userTurtlesOn(t));
				}
			}
		}
		else {
			for (Object o : c){
				if (o instanceof Patch){
					Patch p = (Patch) o;
					total.addAll(userTurtlesOn(p));
				}
			}
		}
		return new AgentSet<market.relogo.UserTurtle>(total);
	}

	/**
	 * Queries if object is a userTurtle.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a userTurtle
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public boolean isUserTurtleQ(Object o){
		return (o instanceof market.relogo.UserTurtle);
	}

	/**
	 * Returns an agentset containing all userTurtles.
	 * 
	 * @return agentset of all userTurtles
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public AgentSet<market.relogo.UserTurtle> userTurtles(){
		AgentSet<market.relogo.UserTurtle> a = new AgentSet<market.relogo.UserTurtle>();
		for (Object e : this.getMyObserver().getContext().getObjects(market.relogo.UserTurtle.class)) {
			if (e instanceof market.relogo.UserTurtle){
				a.add((market.relogo.UserTurtle)e);
			}
		}
		return a;
	}

	/**
	 * Returns the userTurtle with the given who number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserTurtle")
	public market.relogo.UserTurtle userTurtle(Number number){
		Turtle turtle = Utility.turtleU(number.intValue(), getMyObserver());
		if (turtle instanceof market.relogo.UserTurtle)
			return (market.relogo.UserTurtle) turtle;
		return null;
	}

	/**
	 * Makes a directed userLink from a turtle to the caller then executes a set of
	 * commands on the created userLink.
	 * 
	 * @param t
	 *            a turtle
	 * @param closure
	 *            a set of commands
	 * @return created userLink
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink createUserLinkFrom(Turtle t, Closure closure){
		market.relogo.UserLink link = (market.relogo.UserLink)this.getMyObserver().getNetwork("UserLink").addEdge(t,this);
		if (closure != null){
			this.ask(link,closure);
		}
		return link;
	}

	/**
	 * Makes a directed userLink from a turtle to the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return created userLink
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink createUserLinkFrom(Turtle t){
			return createUserLinkFrom(t,null);
	}

	/**
	 * Makes directed userLinks from a collection to the caller then executes a set
	 * of commands on the created userLinks.
	 * 
	 * @param a
	 *            a collection
	 * @param closure
	 *            a set of commands
	 * @return created userLinks
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> createUserLinksFrom(Collection<? extends Turtle> a, Closure closure){
		AgentSet<market.relogo.UserLink> links = new AgentSet<market.relogo.UserLink>();
		for(Turtle t : a){
			links.add((market.relogo.UserLink)this.getMyObserver().getNetwork("UserLink").addEdge(t,this));
		}
		if (closure != null){
			this.ask(links,closure);
		}
		return links;
	}

	/**
	 * Makes directed userLinks from a collection to the caller.
	 * 
	 * @param a
	 *            a collection
	 * @return created userLinks
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> createUserLinksFrom(Collection<? extends Turtle> a){
		return createUserLinksFrom(a,null);
	}

	/**
	 * Makes a directed userLink to a turtle from the caller then executes a set of
	 * commands on the created userLink.
	 * 
	 * @param t
	 *            a turtle
	 * @param closure
	 *            a set of commands
	 * @return created userLink
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink createUserLinkTo(Turtle t, Closure closure){
		market.relogo.UserLink link = (market.relogo.UserLink)this.getMyObserver().getNetwork("UserLink").addEdge(this,t);
		if (closure != null){
			this.ask(link,closure);
		}
		return link;
	}

	/**
	 * Makes a directed userLink to a turtle from the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return created userLink
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink createUserLinkTo(Turtle t){
			return createUserLinkTo(t,null);
	}

	/**
	 * Makes directed userLinks to a collection from the caller then executes a set
	 * of commands on the created userLinks.
	 * 
	 * @param a
	 *            a collection
	 * @param closure
	 *            a set of commands
	 * @return created userLinks
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> createUserLinksTo(Collection<? extends Turtle> a, Closure closure){
		AgentSet<market.relogo.UserLink> links = new AgentSet<market.relogo.UserLink>();
		for(Object t : a){
			if (t instanceof Turtle){
				links.add((market.relogo.UserLink)this.getMyObserver().getNetwork("UserLink").addEdge(this,(Turtle)t));
			}
		}
		if (closure != null){
			this.ask(links,closure);
		}
		return links;
	}

	/**
	 * Makes directed userLinks to a collection from the caller.
	 * 
	 * @param a
	 *            a collection
	 * @return created userLinks
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> createUserLinksTo(Collection<? extends Turtle> a){
		return createUserLinksTo(a,null);
	}

	/**
	 * Queries if there is a directed userLink from a turtle to the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return true or false based on whether there is a directed userLink from
	 *         turtle t to the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public boolean inUserLinkNeighborQ(Turtle t){
		return this.getMyObserver().getNetwork("UserLink").isPredecessor(t, this);
	}

	/**
	 * Returns the agentset with directed userLinks to the caller.
	 * 
	 * @return agentset with directed userLinks to the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet inUserLinkNeighbors(){
		AgentSet result = new AgentSet();
		for(Object o : this.getMyObserver().getNetwork("UserLink").getPredecessors(this)){
			result.add(o);
		}
		return result;
	}

	/**
	 * Returns the directed userLink from a turtle to the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return directed userLink from turtle t to the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink inUserLinkFrom(Turtle t){
		return (market.relogo.UserLink)this.getMyObserver().getNetwork("UserLink").getEdge(t,this);
	}

	/**
	 * Returns an agentset of directed userLinks from other turtles to the caller.
	 * 
	 * @return agentset of directed userLinks from other turtles to the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> myInUserLinks(){
		AgentSet<market.relogo.UserLink> result = new AgentSet<market.relogo.UserLink>();
		for(Object o :  this.getMyObserver().getNetwork("UserLink").getInEdges(this)){
			if (o instanceof market.relogo.UserLink){
				result.add((market.relogo.UserLink) o);
			}
		}
		return result;
	}

	/**
	 * Returns an agentset of directed userLinks to other turtles from the caller.
	 * 
	 * @return agentset of directed userLinks to other turtles from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> myOutUserLinks(){
		AgentSet<market.relogo.UserLink> result = new AgentSet<market.relogo.UserLink>();
		for(Object o :  this.getMyObserver().getNetwork("UserLink").getOutEdges(this)){
			if (o instanceof market.relogo.UserLink){
				result.add((market.relogo.UserLink) o);
			}
		}
		return result;
	}

	/**
	 * Queries if there is a directed userLink to a turtle from the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return true or false based on whether there is a directed userLink to
	 *         turtle t from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public boolean outUserLinkNeighborQ(Turtle t){
		return this.getMyObserver().getNetwork("UserLink").isPredecessor(this, t);
	}

	/**
	 * Returns the agentset with directed userLinks from the caller.
	 * 
	 * @return agentset with directed userLinks from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet outUserLinkNeighbors(){
		AgentSet result = new AgentSet();
		for(Object o : this.getMyObserver().getNetwork("UserLink").getSuccessors(this)){
			result.add(o);
		}
		return result;
	}

	/**
	 * Returns the directed userLink to a turtle from the caller.
	 * 
	 * @param t
	 *            a turtle
	 * @return directed userLink to turtle t from the caller
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink outUserLinkTo(Turtle t){
		return (market.relogo.UserLink)this.getMyObserver().getNetwork("UserLink").getEdge(this, t);
	}

	/**
	 * Reports true if there is a userLink connecting t and the caller.
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public boolean userLinkNeighborQ(Turtle t){
		return this.getMyObserver().getNetwork("UserLink").isAdjacent(this, t);
	}

	/**
	 * Returns the agentset of all turtles found at the other end of
	 * userLinks connected to the calling turtle.
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet userLinkNeighbors(){
		AgentSet result = new AgentSet();
		for(Object o : this.getMyObserver().getNetwork("UserLink").getAdjacent(this)){
			result.add(o);
		}
		return result;
	}

	/**
	 * Returns an agentset of the caller's userLinks.
	 * 
	 * @return agentset of the caller's userLinks
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> myUserLinks(){
		AgentSet<market.relogo.UserLink> result = new AgentSet<market.relogo.UserLink>();
		for(Object o : this.getMyObserver().getNetwork("UserLink").getEdges(this)){
			if (o instanceof market.relogo.UserLink){
				result.add((market.relogo.UserLink)o);
			}
		}
		return result;
	}

	/**
	 * Queries if object is a userLink.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a userLink
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public boolean isUserLinkQ(Object o){
		return (o instanceof market.relogo.UserLink);
	}

	/**
	 * Returns an agentset containing all userLinks.
	 * 
	 * @return agentset of all userLinks
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public AgentSet<market.relogo.UserLink> userLinks(){
		AgentSet<market.relogo.UserLink> a = new AgentSet<market.relogo.UserLink>();
		for (Object e : this.getMyObserver().getContext().getObjects(market.relogo.UserLink.class)) {
			if (e instanceof market.relogo.UserLink){
				a.add((market.relogo.UserLink)e);
			}
		}
		return a;
	}

	/**
	 * Returns the userLink between two turtles.
	 * 
	 * @param oneEnd
	 *            an integer
	 * @param otherEnd
	 *            an integer
	 * @return userLink between two turtles
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink userLink(Number oneEnd, Number otherEnd) {
		return (market.relogo.UserLink)(this.getMyObserver().getNetwork("UserLink").getEdge(turtle(oneEnd),turtle(otherEnd)));
	}

	/**
	 * Returns the userLink between two turtles.
	 * 
	 * @param oneEnd
	 *            a turtle
	 * @param otherEnd
	 *            a turtle
	 * @return userLink between two turtles
	 */
	@ReLogoBuilderGeneratedFor("market.relogo.UserLink")
	public market.relogo.UserLink userLink(Turtle oneEnd, Turtle otherEnd) {
		return userLink(oneEnd.getWho(), otherEnd.getWho());
	}


}