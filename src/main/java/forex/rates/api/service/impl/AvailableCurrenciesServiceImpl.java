package forex.rates.api.service.impl;

import forex.rates.api.configuration.DataSetContext;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.service.AvailableCurrenciesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableCurrenciesServiceImpl implements AvailableCurrenciesService {

    private final CurrencyDefinitionRepository currencyDefinitionRepository;
    private final DataSetContext dataSetContext;

    public AvailableCurrenciesServiceImpl(CurrencyDefinitionRepository currencyDefinitionRepository, DataSetContext dataSetContext) {
	this.currencyDefinitionRepository = currencyDefinitionRepository;
	this.dataSetContext = dataSetContext;
    }

    @Override
    public List<String> getList() {
	List<String> availableCurrencies = currencyDefinitionRepository.findAll().stream()
		.map(cD -> cD.getCodeName())
		.collect(Collectors.toList());
	availableCurrencies.add(dataSetContext.getBaseCurrency());
	return availableCurrencies;
    }

}
