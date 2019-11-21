package market.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;
import market.ReLogoObserver;
import market.context.SimBuilder
class UserObserver extends ReLogoObserver{
		
		int turn = 0
		def i
		def traders=0
		def markets=0
		def rice
		def water
		def gold
		def maxX = SimBuilder.maxPxcor
		def maxY = SimBuilder.maxPycor
		public static def globalPrice = ['rice':10, 'water':10]
		public static def transactions = 0
		@Setup
		def setup(){
			
			clearAll()
			i=0
			createTraders(150){
				it.xcor= random(maxX)
				it.ycor= random(maxY)
				it.construct(i, random(10)+7, random(10)+7, random(40)+35, random(100)+100, random(100)+100, random(10)+1)
				it.resourceMiningAbility['rice'] = false
				i+=1
			}
			createTraders(150){
				it.xcor= random(maxX)
				it.ycor= random(maxY)
				it.construct(i, random(10)+7, random(10)+7, random(40)+35, random(100)+100, random(100)+100, random(10)+1)
				it.resourceMiningAbility['water'] = false
				i+=1
			}
			createMarkets(30){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i,random(300)+250, random(10+1))
				i+=1
				}
			createResources(2){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'rice')
				i+=1
			}
			createResources(2){
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
			createResources(1){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'gold')
				i+=1
			}
		}
		

	
		@Go
		def go(){
			turn +=1
			if(turn%199==0) {
				createMarkets(1){
					it.setXcor(random(maxX))
					it.setYcor(random(maxX))
					it.facexy(it.getXcor(), it.getYcor()-1)
					it.construct(i,random(300)+150, random(10+1))
					i+=1
					}
			}
				
			if(turn%100 == 0) {
				println("@@@@@@@@@@@@@@@@@")
				println("Total transactions: " + transactions)
				println("Global price: " + globalPrice)
				println("Traders: " + traders )
				println("Markets: " + markets )
				println("Rice: " + rice)
				println("Water: " +water)
				println("Gold: " + gold)
				
				ask(turtles()){
					if (it instanceof Market) {
						
						if(it.alive) {
							println("Market " + it.id+", X: " +it.xcor+", Y: "+it.ycor +", Rice: wnt: " + it.meanOfBuyerWnt('rice') +", lft: " + it.meanOfResourceLft('rice') +", sell: " + it.meanAmountSoldPerStep('rice')
								+", price: " + it.discountedMeanPrice('rice') + "; Water: wnt: " + it.meanOfBuyerWnt('water') +", lft: " + it.meanOfResourceLft('water') + ", sell: " + it.meanAmountSoldPerStep('water')
								+", price: " + it.discountedMeanPrice('water'))
						}
							
						
					}
					
				}
				ask(turtles()){
					if (it instanceof Resource) {
						
						
							println("Resource " + it.type+", X: " +it.xcor+", Y: "+it.ycor)
						
							
						
					}
					
				}
				println("@@@@@@@@@@@@@@@@@")
				
				createTraders(2){
					it.xcor= random(maxX)
					it.ycor= random(maxY)
					it.construct(i, random(10)+7, random(10)+7, random(40)+35, random(100)+100, random(100)+100, random(10)+1)
					it.resourceMiningAbility['rice'] = false
					i+=1
				}
				createTraders(2){
					it.xcor= random(maxX)
					it.ycor= random(maxY)
					it.construct(i, random(10)+7, random(10)+7, random(40)+35, random(100)+100, random(100)+100, random(10)+1)
					it.resourceMiningAbility['water'] = false
					i+=1
				}
				
			}
			//println("turn "+ turn)
			def sum = [0,0]
			def counter = [0,0]
			List res = ['rice', 'water']
			traders = 0
			markets = 0
			rice = 0
			water = 0
			gold = 0
			//long now = System.currentTimeMillis();
			ask(turtles()){
				if (it instanceof Trader) {
					it.step(globalPrice)
					traders+=1
//					rice+=it.rice
//					water +=it.water
//					gold += it.gold
//					if(it.traderInfo!=null) {
//						if(traderInfo.sell) {
//							if(traderInfo.rice) rice += traderInfo.amount
//							if(!traderInfo.rice) water += traderInfo.amount
//						}
//						else {
//							gold += traderInfo.amount * traderInfo.pricePerUnit
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
		}


}