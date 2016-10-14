package forex.rates.api.dataset;

import forex.rates.api.model.entity.CurrencyRate;

import java.util.List;

public interface DataSetUpdate {

    List<CurrencyRate> getNewCurrencyRates();

}
