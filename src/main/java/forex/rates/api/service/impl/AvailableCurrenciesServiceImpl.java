package forex.rates.api.service.impl;

import forex.rates.api.dataset.DataSetContext;
import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.service.CurrencyDefinitionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableCurrenciesServiceImpl implements AvailableCurrenciesService {

    private final CurrencyDefinitionService currencyDefinitionService;
    private final DataSetContext dataSetContext;

    private List<String> availableCurrencyCodeNames;

    public AvailableCurrenciesServiceImpl(CurrencyDefinitionService currencyDefinitionService, DataSetContext dataSetContext) {
	this.currencyDefinitionService = currencyDefinitionService;
	this.dataSetContext = dataSetContext;
	cacheAvailableCurrencyCodeNames();
    }

    private void cacheAvailableCurrencyCodeNames() {
	availableCurrencyCodeNames = getAvailableCurrencyCodeNames();
	availableCurrencyCodeNames.add(dataSetContext.getBaseCurrency());
    }

    private List<String> getAvailableCurrencyCodeNames() {
	return currencyDefinitionService.getAll().stream()
		    .map(cD -> cD.getCodeName())
		    .collect(Collectors.toList());
    }

    @Override
    public List<String> getCodeList() {
	return Collections.unmodifiableList(availableCurrencyCodeNames);
    }

}
