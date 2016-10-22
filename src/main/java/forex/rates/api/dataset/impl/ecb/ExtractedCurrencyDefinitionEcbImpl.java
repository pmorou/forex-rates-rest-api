package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.dataset.ExtractedCurrencyDefinition;
import forex.rates.api.model.entity.CurrencyDefinition;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Profile("european-central-bank")
@Component
public class ExtractedCurrencyDefinitionEcbImpl implements ExtractedCurrencyDefinition {

    @Override
    public CurrencyDefinition getCurrencyDefinition(Map<String, String> attributes) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition();
	currencyDefinition.setCodeName(attributes.get("UNIT"));
	currencyDefinition.setPrecision(new Integer(attributes.get("DECIMALS")));
	return currencyDefinition;
    }

}
