package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.exchange.ExchangeTransactions;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonPropertyOrder({ "date", "amount", "from", "to" })
public class DailyExchangeResponse {

    private final String date;
    private final BigDecimal amount;
    private final String from;
    private final Map<String, BigDecimal> to;

    public DailyExchangeResponse(ExchangeTransactions exchangeTransactions) {
	this.date = exchangeTransactions.getStartDate().toString();
	this.amount = exchangeTransactions.getAmount();
	this.from = exchangeTransactions.getFrom();
	this.to = exchangeTransactions.getTo()
		.get(exchangeTransactions.getStartDate())
		.getTo();
    }

}
