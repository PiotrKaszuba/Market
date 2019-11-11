package market.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;
import market.ReLogoObserver;

class UserObserver extends ReLogoObserver{

		int turn = 0
		@Setup
		def setup(){
			clearAll()
			createTraders(10){
				it.construct(random(10), random(10), random(10), random(10), random(10), random(10))
			}
			createMarkets(10){
				it.facexy(it.getXcor(), it.getYcor()-1)
				it.construct(random(10), random(10))}
		}
		

	
		@Go
		def go(){
			turn +=1
			println("turn "+ turn)
			ask(turtles()){
				if(it instanceof Trader) {
					left(random(90))
					right(random(90))
					forward(random(10))
				}
				it.step()

				
			}
		}


}