package forex.rates.api.dataset;

import forex.rates.api.model.entity.CurrencyDefinition;

import java.util.Map;

public interface CurrencyDefinitionFactory {

    CurrencyDefinition getCurrencyDefinition(Map<String, String> attributes);

}
