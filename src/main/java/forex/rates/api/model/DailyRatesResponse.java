package forex.rates.api.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DailyRatesResponse {

    private long timestamp;
    private String date;
    private String base;
    private Map<String, BigDecimal> rates;

    public DailyRatesResponse(long currentTimestamp, ExchangeRates exchangeRates) {
	this.timestamp = currentTimestamp;
	this.date = exchangeRates.getDate();
	this.base = exchangeRates.getBase();
	this.rates = new HashMap<>(exchangeRates.getRates());
    }

    public long getTimestamp() {
	return timestamp;
    }

    public String getBase() {
	return base;
    }

    public Map<String, BigDecimal> getRates() {
	return Collections.unmodifiableMap(rates);
    }

    public String getDate() {
	return date;
    }

}
