package forex.rates.api.model.exchange;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ExchangeTransactions {

    @Getter(AccessLevel.PRIVATE)
    private final ExchangeRates exchangeRates;
    private final BigDecimal amount;

    public String getFrom() {
	return exchangeRates.getBase();
    }

    public LocalDate getStartDate() {
	return exchangeRates.getStartDate();
    }

    public LocalDate getEndDate() {
	return exchangeRates.getEndDate();
    }

    public Map<LocalDate, Transactions> getTo() {
	Map<LocalDate, Transactions> transactionsByDate = new LinkedHashMap<>();
	for (Map.Entry<LocalDate, Rates> ratesByDate : exchangeRates.getRatesByDate().entrySet()) {
	    Rates dailyRates = ratesByDate.getValue();
	    Transactions transactions = new Transactions();
	    for (Map.Entry<String, BigDecimal> currencyRatePair : dailyRates.getRates().entrySet()) {
		transactions.add(currencyRatePair.getKey(), multiplyWithAmount(currencyRatePair.getValue()));
	    }
	    transactionsByDate.put(ratesByDate.getKey(), transactions);
	}
	return transactionsByDate;
    }

    private BigDecimal multiplyWithAmount(BigDecimal rate) {
	return rate.multiply(this.amount, new MathContext(3, RoundingMode.HALF_UP));
    }

}
