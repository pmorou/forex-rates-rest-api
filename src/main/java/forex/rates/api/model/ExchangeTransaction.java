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

    public Map<String, BigDecimal> getTo() {
	Map<String, BigDecimal> result = new LinkedHashMap<>();

	Map<LocalDate, Rates> ratesByDate = exchangeRates.getRatesByDate();

	for (Map.Entry<LocalDate, Rates> entry : ratesByDate.entrySet()) {
	    LocalDate date = entry.getKey();
	    Rates rates = entry.getValue();

	    for (Map.Entry<String, BigDecimal> entry1 : rates.getRates().entrySet()) {
		result.put(entry1.getKey(), multiplyWithAmount(entry1.getValue()));
	    }
	}

	return result;
    }

    private BigDecimal multiplyWithAmount(BigDecimal rate) {
	return rate.multiply(new BigDecimal(this.amount));
    }

}
