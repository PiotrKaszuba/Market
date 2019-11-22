package market.relogo
import repast.simphony.engine.environment.RunEnvironment;
import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;
import market.ReLogoObserver;
import market.context.SimBuilder
import repast.simphony.parameter.Parameters;
class UserObserver extends ReLogoObserver{
		
	
	def globalPriceWater() {
		
		return globalPrice['water'];
	}
	def globalPriceRice() {
		
		return globalPrice['rice'];
	}
		int turn = 0
		def i
		def traders=0
		def markets=0
		def businessmans = 0
		def balancers = 0
		def volunteers = 0
		def ricer
		def waterer
		def golder
		def maxX = SimBuilder.maxPxcor
		def maxY = SimBuilder.maxPycor
		int startRiceWaterRand = 10
		int startRiceWater = 7
		int startGold = 25
		int travelCost = 2
		int volunteerCount = 70
		int businessmanCount = 70
		def characters = ['volunteer', 'businessman', 'balance']
		def characterSamples = []
		public static def globalPrice = ['rice':10, 'water':10]
		public static def transactions = 0
		@Setup
		def setup(){
			
			clearAll()
			
			
			Parameters p = RunEnvironment.getInstance().getParameters();
			int riceMines = p.getValue("riceMines");
			int waterMines = p.getValue("waterMines");
			businessmanCount = p.getValue("businessmanCount");
			volunteerCount = p.getValue("volunteerCount");
			startRiceWater = p.getValue("startRiceWater");
			startGold = p.getValue("startGold");
			travelCost = p.getValue("travelCost");
			
			
			
			i=0
			
			def characterCount = ['businessman' : businessmanCount, 'volunteer':volunteerCount, 'balance' : 70]
			
			
			characterCount.each { character, count ->
				for(int h =0; h<count;h++) {
					characterSamples.add(character)
				}
				createTraders(Math.round(count/2).toInteger()){ trader ->
					trader.xcor= random(maxX)
					trader.ycor= random(maxY)
					trader.construct(i, random(startRiceWaterRand)+startRiceWater, random(startRiceWaterRand)+startRiceWaterRand, random(40)+startGold, random(100)+100, random(100)+100, random(10)+1, character, travelCost)
					trader.resourceMiningAbility['rice'] = false
					i+=1
				}
				createTraders(Math.round(count/2).toInteger()){ trader ->
					trader.xcor= random(maxX)
					trader.ycor= random(maxY)
					trader.construct(i, random(startRiceWaterRand)+startRiceWater, random(startRiceWaterRand)+startRiceWaterRand, random(40)+startGold, random(100)+100, random(100)+100, random(10)+1, character, travelCost)
					trader.resourceMiningAbility['water'] = false
					i+=1
				}
			
			}
			
			createMarkets(25){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i,random(300)+200, random(10+1))
				i+=1
				}
			createResources(riceMines){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'rice')
				i+=1
			}
			createResources(waterMines){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'water')
				i+=1
			}
//			createResources(1){
//				it.setXcor(10)
//				it.setYcor(90)
//				it.facexy(it.getXcor(), it.getYcor()-1)
//				it.construct(i, 'water')
//				i+=1
//			}
//			createResources(1){
//				it.setXcor(90)
//				it.setYcor(10)
//				it.facexy(it.getXcor(), it.getYcor()-1)
//				it.construct(i, 'rice')
//				i+=1
//			}
			createResources(1){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'gold')
				i+=1
			}
//			createResources(1){
//				it.setXcor(random(maxX))
//				it.setYcor(random(maxX))
//				it.facexy(it.getXcor(), it.getYcor()-1)
//				it.construct(i, 'gold')
//				i+=1
//			}
		}
		

	
		@Go
		def go(){
			turn +=1
			
//			if(turn%500==0) {
//				createMarkets(1){
//					it.setXcor(random(maxX))
//					it.setYcor(random(maxX))
//					it.facexy(it.getXcor(), it.getYcor()-1)
//					it.construct(i,random(400)+800, random(10+1))
//					i+=1
//					}
//			}
				
			if(turn%100 == 0) {
//				println("@@@@@@@@@@@@@@@@@")
//				println("Total transactions: " + transactions)
//				println("Global price: " + globalPrice)
//				println("Traders: " + traders )
//				println("Markets: " + markets )
//				println("Rice: " + ricer)
//				println("Water: " +waterer)
//				println("Gold: " + golder)
				
				ask(turtles()){
					if (it instanceof Market) {
						
						if(it.alive) {
//							println("Market " + it.id+", X: " +it.xcor+", Y: "+it.ycor +", Rice: wnt: " + it.meanOfBuyerWnt('rice') +", lft: " + it.meanOfResourceLft('rice') +", sell: " + it.meanAmountSoldPerStep('rice')
//								+", price: " + it.discountedMeanPrice('rice') + "; Water: wnt: " + it.meanOfBuyerWnt('water') +", lft: " + it.meanOfResourceLft('water') + ", sell: " + it.meanAmountSoldPerStep('water')
//								+", price: " + it.discountedMeanPrice('water'))
						}
							
						
					}
					
				}
				ask(turtles()){
					if (it instanceof Resource) {
						
						
							println("Resource " + it.type+", X: " +it.xcor+", Y: "+it.ycor)
						
							
						
					}
					
				}
//				println("@@@@@@@@@@@@@@@@@")
				
				createTraders(2){
					it.xcor= random(maxX)
					it.ycor= random(maxY)
					it.construct(i, random(startRiceWaterRand)+startRiceWater, random(startRiceWaterRand)+startRiceWater, random(40)+startGold, random(100)+100, random(100)+100, random(10)+1, characterSamples[random(characterSamples.size())], travelCost)
					it.resourceMiningAbility['rice'] = false
					i+=1
				}
				createTraders(2){
					it.xcor= random(maxX)
					it.ycor= random(maxY)
					it.construct(i, random(startRiceWaterRand)+startRiceWater, random(startRiceWaterRand)+startRiceWater, random(40)+startGold, random(100)+100, random(100)+100, random(10)+1, characterSamples[random(characterSamples.size())], travelCost)
					it.resourceMiningAbility['water'] = false
					i+=1
				}
				
			}
			println("===========================turn "+ turn + "==================")
			def sum = [0,0]
			def counter = [0,0]
			List res = ['rice', 'water']
			
			ricer= 0
			waterer = 0
			golder = 0
			traders =0 
			markets = 0
			balancers = 0
			businessmans = 0
			volunteers = 0
			//long now = System.currentTimeMillis();
			ask(turtles()){
				if (it instanceof Trader) {
					it.step(globalPrice)
					traders+=1
					if(it.character.equals("balance"))
						balancers +=1
					if(it.character.equals("businessman"))
						businessmans +=1
					if(it.character.equals("volunteer"))
						volunteers +=1
//					ricer+=it.rice
//					waterer +=it.water
//					golder += it.gold
//					if(it.traderInfo!=null) {
//						if(traderInfo.sell) {
//							if(traderInfo.rice) ricer += traderInfo.amount
//							if(!traderInfo.rice) waterer += traderInfo.amount
//						}
//						else {
//							golder += traderInfo.amount * traderInfo.pricePerUnit
//						}
//					}
				}
			}
			
			//System.out.println( (System.currentTimeMillis() - now) + " ms");
			//now = System.currentTimeMillis();
			ask(turtles()){
				if (it instanceof Market) {
					it.step(globalPrice)
					if(it.alive)
						for(int k = 0; k< res.size(); k++ ) {
							def val = it.discountedMeanPrice(res[k])
							if(val) {
								sum[k] += val
								counter[k]+=1
							}
							
						}
					markets+=1
				}
				
			}
			//System.out.println( (System.currentTimeMillis() - now) + " ms");
			//now = System.currentTimeMillis();
			ask(turtles()){
				if (it instanceof Resource) {
					it.step()
					
				}
				
			}
			//System.out.println( (System.currentTimeMillis() - now) + " ms");
			for(int k = 0; k< res.size(); k++ ) {
				globalPrice[res[k]] =  counter[k] != 0 ? sum[k] / counter[k] : 10
			}
			if(markets == 0)
				RunEnvironment.getInstance().endRun();
		}


}