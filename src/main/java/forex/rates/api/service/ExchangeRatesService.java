package forex.rates.api.service;

import forex.rates.api.model.exchange.ExchangeRates;
import forex.rates.api.model.request.ExchangeRatesRequest;

public interface ExchangeRatesService {

    ExchangeRates perform(ExchangeRatesRequest exchangeRatesRequest);

}
