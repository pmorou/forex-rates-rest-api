package forex.rates.api.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ExchangeTransaction {

    @Getter(AccessLevel.PRIVATE)
    private final ExchangeRates exchangeRates;
    private final int amount;

    public String getFrom() {
	return exchangeRates.getBase();
    }

    public LocalDate getDate() {
	return exchangeRates.getStartDate();
    }

    public Map<LocalDate, Transaction> getTo() {
	Map<LocalDate, Transaction> transactionsByDate = new LinkedHashMap<>();
	for (Map.Entry<LocalDate, Rates> ratesByDate : exchangeRates.getRatesByDate().entrySet()) {
	    Rates dailyRates = ratesByDate.getValue();
	    Transaction transaction = new Transaction();
	    for (Map.Entry<String, BigDecimal> currencyRatePair : dailyRates.getRates().entrySet()) {
		transaction.addAmount(currencyRatePair.getKey(), multiplyWithAmount(currencyRatePair.getValue()));
	    }
	    transactionsByDate.put(ratesByDate.getKey(), transaction);
	}
	return transactionsByDate;
    }

    private BigDecimal multiplyWithAmount(BigDecimal rate) {
	return rate.multiply(new BigDecimal(this.amount));
    }

}
