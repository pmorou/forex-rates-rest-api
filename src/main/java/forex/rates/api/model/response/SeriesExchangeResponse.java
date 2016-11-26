package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.exchange.ExchangeTransactions;
import forex.rates.api.model.exchange.Transactions;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@JsonPropertyOrder({ "startDate", "endDate", "amount", "from", "to" })
public class SeriesExchangeResponse {

    private final String startDate;
    private final String endDate;
    private final BigDecimal amount;
    private final String from;
    private final Map<LocalDate, Transactions> to;

    public SeriesExchangeResponse(ExchangeTransactions exchangeTransactions) {
	this.startDate = exchangeTransactions.getStartDate().toString();
	this.endDate = exchangeTransactions.getEndDate().toString();
	this.amount = exchangeTransactions.getAmount();
	this.from = exchangeTransactions.getFrom();
	this.to = exchangeTransactions.getTo();
    }

}
