package forex.rates.api.autostart.dataset;

import forex.rates.api.model.entity.CurrencyDefinition;

import java.util.Map;

public interface ExtractedCurrencyDefinition {

    CurrencyDefinition getCurrencyDefinition(Map<String, String> attributes);

}
