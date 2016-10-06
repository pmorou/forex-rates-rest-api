package forex.rates.api.model.response;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.Rates;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class SeriesRatesResponse {

    private final long timestamp;
    private final String startDate;
    private final String endDate;
    private final String base;
    private final Map<LocalDate, Rates> rates;

    public SeriesRatesResponse(long currentTimestamp, ExchangeRates exchangeRates) {
	this.timestamp = currentTimestamp;
	this.startDate = exchangeRates.getStartDate().toString();
	this.endDate = exchangeRates.getEndDate().toString();
	this.base = exchangeRates.getBase();
	this.rates = new HashMap<>(exchangeRates.getRatesByDate());
    }

}
