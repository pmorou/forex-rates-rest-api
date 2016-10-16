package forex.rates.api.service;

import forex.rates.api.model.entity.CurrencyDefinition;

public interface CurrencyDefinitionService {

    void save(CurrencyDefinition currencyDefinition);

    void save(Iterable<CurrencyDefinition> currencyDefinitions);

}
