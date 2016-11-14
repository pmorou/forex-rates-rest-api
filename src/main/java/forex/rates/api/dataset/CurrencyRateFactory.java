package forex.rates.api.dataset;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;

import java.util.Map;

public interface CurrencyRateFactory {

    CurrencyRate getCurrencyRate(CurrencyDefinition currencyDefinition, Map.Entry<String, String> rate);

}
