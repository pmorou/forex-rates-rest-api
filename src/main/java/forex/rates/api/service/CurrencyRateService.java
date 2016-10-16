package forex.rates.api.service;

import forex.rates.api.model.entity.CurrencyRate;

public interface CurrencyRateService {

    void save(CurrencyRate currencyRate);

    void save(Iterable<CurrencyRate> currencyRates);

}
