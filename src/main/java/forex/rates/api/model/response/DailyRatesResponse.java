package forex.rates.api.model.response;

import forex.rates.api.model.ExchangeRates;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class DailyRatesResponse {

    private final String date;
    private final String base;
    private final Map<String, BigDecimal> rates;

    public DailyRatesResponse(ExchangeRates exchangeRates) {
	this.date = exchangeRates.getStartDate().toString();
	this.base = exchangeRates.getBase();
	this.rates = exchangeRates.getRatesByDate().get(exchangeRates.getStartDate()).getRates();
    }

}
