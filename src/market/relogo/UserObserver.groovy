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
		public static def globalPrice = ['rice':10, 'water':10]
		public static def transactions = 0
		@Setup
		def setup(){
			def maxX = SimBuilder.maxPxcor
			def maxY = SimBuilder.maxPycor
			clearAll()
			def i=0
			createTraders(100){
				it.xcor= random(maxX)
				it.ycor= random(maxY)
				it.construct(i, random(10)+7, random(10)+7, random(40)+35, random(100)+100, random(100)+100, random(10)+1)
				it.resourceMiningAbility['rice'] = false
				i+=1
			}
			createTraders(200){
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
			createResources(1){
				it.setXcor(10)
				it.setYcor(10)
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'rice')
				i+=1
			}
			createResources(1){
				it.setXcor(90)
				it.setYcor(10)
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'water')
				i+=1
			}
			createResources(1){
				it.setXcor(10)
				it.setYcor(90)
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'water')
				i+=1
			}
			createResources(1){
				it.setXcor(90)
				it.setYcor(90)
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'rice')
				i+=1
			}
			createResources(1){
				it.setXcor(50)
				it.setYcor(50)
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(i, 'gold')
				i+=1
			}
		}
		

	
		@Go
		def go(){
			turn +=1
			if(turn%100 == 0) {
				println("@@@@@@@@@@@@@@@@@")
				println("Total transactions: " + transactions)
				println("Global price: " + globalPrice)
				println("@@@@@@@@@@@@@@@@@")
			}
			//println("turn "+ turn)
			def sum = [0,0]
			def counter = [0,0]
			List res = ['rice', 'water']
			//long now = System.currentTimeMillis();
			ask(turtles()){
				if (it instanceof Trader) {
				it.step(globalPrice)
				}
			}
			//System.out.println( (System.currentTimeMillis() - now) + " ms");
			//now = System.currentTimeMillis();
			ask(turtles()){
				if (it instanceof Market) {
					it.step(globalPrice)
					if(it.alive)
						for(int k = 0; k< res.size(); k++ ) {
							sum[k] += it.discountedMeanPrice(res[k])
							counter[k]+=1
						}
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