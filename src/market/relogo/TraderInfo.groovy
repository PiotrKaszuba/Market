package market.relogo

class TraderInfo {
	Trader trader
	def sell
	def rice
	def amount
	def pricePerUnit
	def TraderInfo(Trader trader, boolean sell, boolean rice, int amount, int pricePerUnit) {
		this.trader = trader
		this.sell = sell
		this.rice = rice
		this.amount = amount
		this.pricePerUnit = pricePerUnit
	}
}
