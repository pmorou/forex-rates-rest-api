package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.ExchangeTransaction;
import forex.rates.api.model.Transaction;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@JsonPropertyOrder({ "startDate", "endDate", "amount", "from", "to" })
public class SeriesExchangeResponse {

    private final String startDate;
    private final String endDate;
    private final int amount;
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
