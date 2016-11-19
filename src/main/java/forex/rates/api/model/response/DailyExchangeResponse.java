package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.exchange.ExchangeTransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonPropertyOrder({ "date", "amount", "from", "to" })
public class DailyExchangeResponse {

    private final String date;
    private final int amount;
    private final String from;
    private final Map<String, BigDecimal> to;

    public DailyExchangeResponse(ExchangeTransaction exchangeTransaction) {
	this.date = exchangeTransaction.getStartDate().toString();
	this.amount = exchangeTransaction.getAmount();
	this.from = exchangeTransaction.getFrom();
	this.to = exchangeTransaction.getTo()
		.get(exchangeTransaction.getStartDate())
		.getTo();
    }

}
