package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.exchange.ExchangeRates;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonPropertyOrder({ "date", "base", "rates" })
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
