package forex.rates.api.service.impl;

import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.service.AvailableCurrenciesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableCurrenciesServiceImpl implements AvailableCurrenciesService {

    private final CurrencyDefinitionRepository currencyDefinitionRepository;

    public AvailableCurrenciesServiceImpl(CurrencyDefinitionRepository currencyDefinitionRepository) {
	this.currencyDefinitionRepository = currencyDefinitionRepository;
    }

    @Override
    public List<String> getList() {
	return currencyDefinitionRepository.findAll().stream()
		.map(cD -> cD.getCodeName())
		.collect(Collectors.toList());
    }

}
