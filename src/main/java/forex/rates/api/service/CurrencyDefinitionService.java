package forex.rates.api.service;

import forex.rates.api.model.entity.CurrencyDefinition;

import java.util.List;

public interface CurrencyDefinitionService {

    List<CurrencyDefinition> getAll();

    List<CurrencyDefinition> getAllByCodeNameIn(List<String> codeName);

    CurrencyDefinition getOneByCodeName(String codeName);

    void save(CurrencyDefinition currencyDefinition);

    void save(Iterable<CurrencyDefinition> currencyDefinitions);

}
