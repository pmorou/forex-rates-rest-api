package forex.rates.api.repository;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRatesRepository {

    List<CurrencyRate> findAllByDateAndCurrencyIn(LocalDate date, List<CurrencyDefinition> currencies);

    CurrencyRate findOneByDateAndCurrency(LocalDate date, CurrencyDefinition currency);

    CurrencyRate save(CurrencyRate currencyRate);

}
