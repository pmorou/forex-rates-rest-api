package forex.rates.api.service;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateService {

    List<CurrencyRate> getAllByDateAndCurrencyIn(LocalDate date, List<CurrencyDefinition> currencies);

    CurrencyRate getOneByDateAndCurrency(LocalDate date, CurrencyDefinition currency);

    void save(CurrencyRate currencyRate);

    void save(Iterable<CurrencyRate> currencyRates);

}
