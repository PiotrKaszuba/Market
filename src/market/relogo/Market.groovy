package market.relogo

import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*

import market.ReLogoTurtle
import repast.simphony.relogo.Plural
import repast.simphony.relogo.Stop
import repast.simphony.relogo.Utility
import repast.simphony.relogo.UtilityG
import repast.simphony.relogo.schedule.Go
import repast.simphony.relogo.schedule.Setup

class Market extends ReLogoTurtle {

	def construct(int destroy_after, int transactions_per_step) {
		this.destroy_after = destroy_after
		this.transactions_per_step = transactions_per_step
	}
	boolean alive = true
	int last_transaction = 0
	int destroy_after
	int transactions_per_step = 1
	// 'water' : 'sell', 'buy'
	Map registered = [false : [true : [], false : []], true: [true : [], false : []]]
	
	def step() {
		if(alive) {
		
		if(last_transaction > destroy_after) {
			alive = false
			println('market dead')
			
			registered.each { key,value -> value.each { k,v -> v.each { el -> 
				unregister(el.trader)
				}}}
			die()
			return
		}
		def last_transactions = [true : true, false : true]
		def transactions_current_step = 0
		while((last_transactions[true] || last_transactions[false]) && transactions_current_step < transactions_per_step) {
			[true, false].each { v-> 
				last_transactions[v] = checkForTransaction(registered[v], v)
				if(last_transactions[v] == true) transactions_current_step +=1
				}
		}
		last_transaction+=1
		}
	}
	def doTransaction(boolean rice, TraderInfo seller, TraderInfo buyer) {
		def amount = min([seller.amount, buyer.amount])
		if (rice){
			buyer.trader.rice += amount
		}else {
			buyer.trader.water += amount
		}
		seller.trader.gold += amount * seller.pricePerUnit
		buyer.trader.gold += amount * max([0, buyer.pricePerUnit - seller.pricePerUnit])
		seller.amount -= amount
		buyer.amount -= amount
	}
	
	def checkForTransaction(def productDict, boolean rice) {
		List sellList = productDict[true]
		List buyList = productDict[false]
		TraderInfo tInfo =  sellList[0]
		List potentialBuyers = buyList.findAll { it.pricePerUnit >= tInfo.pricePerUnit} as List
		if(potentialBuyers.isEmpty()) return false
		Random rnd = new Random()
		TraderInfo buyer = potentialBuyers[rnd.nextInt(potentialBuyers.size)]
		
		doTransaction(rice, tInfo, buyer)
		[tInfo, buyer].each { a -> if (a.amount == 0) { 
			registered[a.rice][a.sell].removeIf{v-> v.trader.id == a.trader.id } 
			a.trader.finishedTask()
			}}
		return true
	}
	
	
	
	def unregister(Trader trader) {
		TraderInfo ti
		registered.each { key, value -> value.each { k,v -> 
			 def col = v.findAll{ it.trader.id == trader.id }
			 if (!col.empty) ti = col[0]
			 v.removeIf {it.trader.id == trader.id }
			 
			  } }
		if(ti.sell){
			if(ti.rice) {
				trader.rice += ti.amount
			}
			else {
				trader.water+=ti.amount
			}
		}
		else {
			trader.gold += ti.amount * ti.pricePerUnit
		}
		return true
	}
	
	def register(Trader trader, boolean sell, boolean rice, int amount, int pricePerUnit) {
		if(!alive) return false
		if(sell) {
			if(rice) {
				if(trader.rice < amount) return false
				trader.rice -= amount
			}
			else {
				if(trader.water < amount) return false
				trader.water -= amount
			}
		}
		else {
			if(trader.gold < amount * pricePerUnit) return false
			trader.gold -= amount * pricePerUnit
		}
		registered[rice][sell].add(new TraderInfo(trader, sell, rice, amount, pricePerUnit))
		return true
	}
	
}