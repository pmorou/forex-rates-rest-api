package forex.rates.api.service.impl;

import forex.rates.api.configuration.DataSetContext;
import forex.rates.api.service.AvailableCurrenciesService;
import forex.rates.api.service.CurrencyDefinitionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableCurrenciesServiceImpl implements AvailableCurrenciesService {

    private final CurrencyDefinitionService currencyDefinitionService;
    private final DataSetContext dataSetContext;

    public AvailableCurrenciesServiceImpl(CurrencyDefinitionService currencyDefinitionService, DataSetContext dataSetContext) {
	this.currencyDefinitionService = currencyDefinitionService;
	this.dataSetContext = dataSetContext;
    }

    @Override
    public List<String> getCodeList() {
	List<String> availableCurrencies = getAvailableCurrenciesCodeNames();
	availableCurrencies.add(dataSetContext.getBaseCurrency());
	return availableCurrencies;
    }

    private List<String> getAvailableCurrenciesCodeNames() {
	return currencyDefinitionService.getAll().stream()
		    .map(cD -> cD.getCodeName())
		    .collect(Collectors.toList());
    }

}
