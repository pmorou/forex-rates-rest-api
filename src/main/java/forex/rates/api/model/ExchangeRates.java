package forex.rates.api.model;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRates {
    private String date;
    private String base;
    private Map<String, BigDecimal> rates;
    private boolean empty;

    public void setDate(String date) {
	this.date = date;
    }

    public void setBase(String base) {
	this.base = base;
    }

    public void setRates(Map<String, BigDecimal> rates) {
	this.rates = rates;
    }

    public String getDate() {
	return date;
    }

    public String getBase() {
	return base;
    }

    public Map<String, BigDecimal> getRates() {
	return rates;
    }

    public void setEmpty() {
	empty = true;
    }

    public boolean isEmpty() {
	return empty;
    }
}
