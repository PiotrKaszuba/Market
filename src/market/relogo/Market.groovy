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

	def construct(int id, int destroy_after, int transactions_per_step) {
		this.id = id
		this.destroy_after = destroy_after
		this.transactions_per_step = transactions_per_step
	}
	int id
	int total_transactions = 0
	boolean alive = true
	int last_transaction = 0
	int destroy_after
	int transactions_per_step = 1
	int history_length = 250
	def meanOfResourceLftVar = ['rice' : null, 'water' : null]
	def meanAmountSoldPerStepVar= ['rice' : null, 'water' : null]
	def discountedMeanPriceVar= ['rice' : null, 'water' : null]
	def meanOfBuyerWntVar = ['rice' : null, 'water' : null]
	List history = []
	
	
	def convertTrueFalse(boolean b) {
		return b ? 1 : 0
	}
	
	def resetStatistics() {
		meanOfResourceLftVar = ['rice' : null, 'water' : null]
		meanOfBuyerWntVar = ['rice' : null, 'water' : null]
		meanAmountSoldPerStepVar= ['rice' : null, 'water' : null]
		discountedMeanPriceVar= ['rice' : null, 'water' : null]
	}
	
	def getHistoryPureRecord() {
		return ['water' : ['transactions': [], 'resourceLeft': 0, 'buyerWants': 0], 'rice': ['transactions': [], 'resourceLeft': 0, 'buyerWants': 0]]
	}
	def transactionPureRecord() {
		return ['amount':0, 'pricePerUnit':0]
	}
	def meanOfBuyerWnt(def resource) {
		if(meanOfBuyerWntVar[resource] != null)
			return meanOfBuyerWntVar[resource]
		
		
		def sum = 0
		def counter = 0
		history.each {
			sum = sum + it.get(resource).get('buyerWants')
			counter = counter + 1
		}
		meanOfBuyerWntVar[resource] = counter!=0 ? sum/counter  : 0
		return meanOfBuyerWntVar[resource]
	}
	
	
	def meanOfResourceLft(def resource) {
		if(meanOfResourceLftVar[resource] != null)
			return meanOfResourceLftVar[resource]
		
		
		def sum = 0
		def counter = 0
		history.each {
			sum = sum + it.get(resource).get('resourceLeft')
			counter = counter + 1
		}
		meanOfResourceLftVar[resource] = counter!=0 ? sum/counter  : 0
		return meanOfResourceLftVar[resource]
	}
	def meanAmountSoldPerStep(def resource) {
		if(meanAmountSoldPerStepVar[resource] != null)
			return meanAmountSoldPerStepVar[resource]
		def sum = 0
		def counter = 0
		history.each {
			counter = counter + 1
			it.get(resource).get('transactions').each { t ->
				sum = sum + t.get('amount')
				
			}
		}
		meanAmountSoldPerStepVar[resource] = counter!=0 ? sum/counter  : 0
		return meanAmountSoldPerStepVar[resource]
	}
	def discountedMeanPrice(def resource) {
		if(discountedMeanPriceVar[resource] != null)
			return discountedMeanPriceVar[resource]
		def length = 0
		history.each { 
			length = length + 1 //it.get(resource).getAt('transactions').size()
		}
		
		def sum = 0
		def counter = 0
		history.eachWithIndex { it, i ->
			it.get(resource).get('transactions').each { t ->
				sum = sum + t.get('pricePerUnit')*(length - i)
				counter = counter + (length - i)
			}
		}
		discountedMeanPriceVar[resource] = counter!=0 ? sum/counter  : 9.5+Math.random()-(meanOfResourceLft(resource)-meanOfBuyerWnt(resource))*0.75
		return discountedMeanPriceVar[resource]
	}
	
	
	
	// 'water' : 'sell', 'buy'
	def registered = [0 : [1 : [], 0 : []], 1: [1 : [], 0 : []]]
	
	def step(def globalPrice) {
		if(alive) {
		resetStatistics()
		if(last_transaction > destroy_after) {
			alive = false
			println('Market dead, id - ' + this.id + ", transactions: " + this.total_transactions)
			
			registered.each { key,value -> value.each { k,v -> 
				def clon = v.clone()
				clon.each { el -> 
				unregister(el.trader)
				}}}
			die()
			return
		}
		history.add(0, getHistoryPureRecord())
		if(history.size() > history_length) history.pop()
		
		def last_transactions = [1 : true, 0 : true]
		def transactions_current_step = 0
		while((last_transactions[1] || last_transactions[0]) && transactions_current_step < transactions_per_step) {
			[1, 0].each { v-> 
				last_transactions[v] = checkForTransaction(registered[v], v)
				if(last_transactions[v] == true) transactions_current_step +=1
				}
		}
		
		
		[['rice', true], ['water', false]].forEach { product, productBool -> 
			def resourceLeft = 0
			def buyerWants = 0
			registered[convertTrueFalse( productBool )][convertTrueFalse( true )].each { v->
				resourceLeft+=v.amount
			}
			history[0][product]['resourceLeft'] = resourceLeft
			
			registered[convertTrueFalse( productBool )][convertTrueFalse( false )].each { v->
				buyerWants+=v.amount
			}
			history[0][product]['buyerWants'] = buyerWants
		}
		
		
		last_transaction+=1
		}
	}
	def doTransaction(int rice, TraderInfo seller, TraderInfo buyer) {
		def amount = Math.min(seller.amount, buyer.amount)
		if (rice){
			buyer.trader.rice += amount
		}else {
			buyer.trader.water += amount
		}
		seller.trader.gold += amount * seller.pricePerUnit
		buyer.trader.gold += amount * Math.max(0, buyer.pricePerUnit - seller.pricePerUnit)
		seller.amount -= amount
		buyer.amount -= amount
		
		
		def productString =  rice ? 'rice' : 'water'
		
		seller.trader.personalSellBuy[productString] *= (1+0.025*amount)
		buyer.trader.personalSellBuy[productString] *= (1-0.05*amount)
		def transactionRecord = transactionPureRecord()
		transactionRecord['amount'] = amount
		transactionRecord['pricePerUnit'] = seller.pricePerUnit
		history[0][productString]['transactions'].add(0,transactionRecord)
		this.total_transactions +=1
		this.destroy_after += 25
		this.last_transaction = 0
		this.history_length += 1
		UserObserver.transactions +=1
		println("------------------")
		println("Transaction!! Market " + this.id +", X: " +xcor +", Y: " +ycor)
		println("Sold " + amount +" " + productString + " for " + seller.pricePerUnit)
		println("Seller: " + "offered: " + seller.pricePerUnit +"; id: " + seller.trader.id + ", rice: " + seller.trader.rice + ", water: " + seller.trader.water + ", gold: " + seller.trader.gold)
		println("Buyer: " + "offered: " + buyer.pricePerUnit +"; id: " + buyer.trader.id + ", rice: " + buyer.trader.rice + ", water: " + buyer.trader.water + ", gold: " + buyer.trader.gold)
		println("------------------")
	}
	
	def checkForTransaction(def productDict, int rice) {
		List sellList = productDict[1]
		
		List buyList = productDict[0]
		if(sellList.empty || buyList.empty) {
//			if(!sellList.empty || ! buyList.empty) {
//				println("One of lists empty! Market " + this.id)
//			}
			return false
		}
		TraderInfo tInfo =  sellList[0]
		List potentialBuyers = buyList.findAll { v -> v.pricePerUnit >= tInfo.pricePerUnit} as List
		
		if(potentialBuyers.isEmpty()) {
			//println("No price match! Market " + this.id)
			return false
			
		}
		Random rnd = new Random()
		TraderInfo buyer = potentialBuyers[rnd.nextInt(potentialBuyers.size)]
		
		doTransaction(rice, tInfo, buyer)
		[tInfo, buyer].each { a -> if (a.amount == 0) { 
			registered[convertTrueFalse(a.rice)][convertTrueFalse(a.sell )].removeIf{v-> v.trader.id == a.trader.id } 
			a.trader.finishedStep()
			}
		}
		return true
	}
	
	
	
	def unregister(Trader trader) {
		TraderInfo ti = trader.traderInfo
		
		registered.each { key, value -> value.each { k,v -> 
			 //def col = v.findAll{ it.trader.id == trader.id }
			 //if (!col.empty) ti = col[0]
			 
			v.removeIf {it.trader.id == trader.id }
			 
			  } }
		trader.traderInfo = null
		if(ti==null) return false
		if(ti.sell){
			if(ti.rice) {
				trader.rice += ti.amount
				trader.personalSellBuy['rice'] *= 1-0.025*ti.amount
			}
			else {
				trader.water+=ti.amount
				trader.personalSellBuy['water'] *= 1-0.025*ti.amount
			}
		}
		else {
			trader.gold += ti.amount * ti.pricePerUnit
			if(ti.rice){
				trader.personalSellBuy['rice'] *= 1+0.025*ti.amount
				
			}
			else {
				trader.personalSellBuy['water'] *= 1+0.025*ti.amount
			}
		}
		trader.traderInfo = null
		return true
	}
	
	def register(Trader trader, boolean sell, boolean rice, int amount, def pricePerUnit) {
		
		if(!alive) {
			return false
		}
		if(amount <= 0) {
			return false
		}
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
		TraderInfo ti = new TraderInfo(trader, sell, rice, amount, pricePerUnit)
		trader.traderInfo = ti
		registered[convertTrueFalse( rice)][convertTrueFalse( sell)].add(ti)
		println("Register - Market " + this.id +  "!! " + (sell ? "Sell - " : "Buy - ") + (rice ? "Rice - " : "Water - ") + amount +" for " +pricePerUnit + "per unit.")
		return true
	}
	
}
