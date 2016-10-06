package forex.rates.api.repository;

import forex.rates.api.model.entity.CurrencyDefinition;

import java.util.List;

public interface CurrencyDefinitionRepository {

    List<CurrencyDefinition> findAll();

    List<CurrencyDefinition> findAllByCodeNameIn(List<String> codeName);

    CurrencyDefinition findOneByCodeName(String codeName);

    CurrencyDefinition save(CurrencyDefinition currencyDefinition);

}
