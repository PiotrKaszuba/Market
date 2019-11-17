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
		@Setup
		def setup(){
			def maxX = SimBuilder.maxPxcor
			def maxY = SimBuilder.maxPycor
			clearAll()
			def i=0
			createTraders(20){
				it.xcor= random(maxX)
				it.ycor= random(maxY)
				it.construct(i, random(10)+7, random(10)+7, random(10)+9, random(100)+100, random(100)+100, random(10)+1)
				i+=1
			}
			createMarkets(10){
				it.setXcor(random(maxX))
				it.setYcor(random(maxX))
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(random(500)+500, random(10))}
		}
		

	
		@Go
		def go(){
			turn +=1
			//println("turn "+ turn)
			def sum = [0,0]
			def counter = [0,0]
			List res = ['rice', 'water']
			ask(turtles()){
				if (it instanceof Trader) {
				it.step(globalPrice)
				}
			}
			ask(turtles()){
				if (it instanceof Market) {
					it.step(globalPrice)
					for(int k = 0; k< res.size(); k++ ) {
						sum[k] += it.discountedMeanPrice(res[k])
						counter[k]+=1
					}
				}
				
			}
			for(int k = 0; k< res.size(); k++ ) {
				globalPrice[res[k]] =  counter[k] != 0 ? sum[k] / counter[k] : 10
			}
		}


}