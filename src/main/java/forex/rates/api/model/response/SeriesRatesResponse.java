package forex.rates.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import forex.rates.api.model.exchange.ExchangeRates;
import forex.rates.api.model.exchange.Rates;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonPropertyOrder({ "startDate", "endDate", "base", "rates" })
public class SeriesRatesResponse {

    private final String startDate;
    private final String endDate;
    private final String base;
    private final Map<LocalDate, Rates> rates;

    public SeriesRatesResponse(ExchangeRates exchangeRates) {
	this.startDate = exchangeRates.getStartDate().toString();
	this.endDate = exchangeRates.getEndDate().toString();
	this.base = exchangeRates.getBase();
	this.rates = new HashMap<>(exchangeRates.getRatesByDate());
    }

}
