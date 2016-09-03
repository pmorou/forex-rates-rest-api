package forex.rates.api.service;

import forex.rates.api.model.ExchangeRates;
import forex.rates.api.model.ExchangeRatesRequest;

public interface ExchangeRatesService {

    ExchangeRates perform(ExchangeRatesRequest exchangeRatesRequest);

}
