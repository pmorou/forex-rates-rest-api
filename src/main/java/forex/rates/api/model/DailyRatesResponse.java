package forex.rates.api.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class DailyRatesResponse {

    private final long timestamp;
    private final String date;
    private final String base;
    private final Map<String, BigDecimal> rates;

    public DailyRatesResponse(long currentTimestamp, ExchangeRates exchangeRates) {
	this.timestamp = currentTimestamp;
	this.date = exchangeRates.getStartDate().toString();
	this.base = exchangeRates.getBase();
	this.rates = exchangeRates.getRatesByDate().get(exchangeRates.getStartDate()).getRates();
    }

}
