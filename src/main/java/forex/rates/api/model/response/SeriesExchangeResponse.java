package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.exchange.ExchangeTransaction;
import forex.rates.api.model.exchange.Transaction;
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
    private final Map<LocalDate, Transaction> to;

    public SeriesExchangeResponse(ExchangeTransaction exchangeTransaction) {
	this.startDate = exchangeTransaction.getStartDate().toString();
	this.endDate = exchangeTransaction.getEndDate().toString();
	this.amount = exchangeTransaction.getAmount();
	this.from = exchangeTransaction.getFrom();
	this.to = exchangeTransaction.getTo();
    }

}
