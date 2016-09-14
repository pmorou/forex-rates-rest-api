package forex.rates.api.autostart.dataset;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;

import java.util.List;

public interface CompleteDataSet {

    List<CurrencyRate> getCurrencyRates();

    List<CurrencyDefinition> getCurrencyDefinitions();

}
