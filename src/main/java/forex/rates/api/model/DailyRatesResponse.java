package forex.rates.api.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DailyRatesResponse {

    private long timestamp;
    private String date;
    private String base;
    private Map<String, BigDecimal> rates;

    public DailyRatesResponse() {
    }

    public DailyRatesResponse(long currentTimestamp, ExchangeRates exchangeRates) {
	this.timestamp = currentTimestamp;
	this.date = exchangeRates.getDate();
	this.base = exchangeRates.getBase();
	this.rates = new HashMap<>(exchangeRates.getRates());
    }

    public long getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
    }

    public String getBase() {
	return base;
    }

    public void setBase(String base) {
	this.base = base;
    }

    public Map<String, BigDecimal> getRates() {
	return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
	this.rates = rates;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }
}
