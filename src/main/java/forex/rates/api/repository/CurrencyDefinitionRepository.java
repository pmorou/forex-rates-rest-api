package forex.rates.api.repository;

import forex.rates.api.model.entity.CurrencyDefinition;

public interface CurrencyDefinitionRepository {

    void save(CurrencyDefinition currencyDefinition);

}
