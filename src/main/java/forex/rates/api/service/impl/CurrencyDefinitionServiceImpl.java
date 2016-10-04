package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.service.CurrencyDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CurrencyDefinitionServiceImpl implements CurrencyDefinitionService {

    private final CurrencyDefinitionRepository currencyDefinitionRepository;

    @Autowired
    public CurrencyDefinitionServiceImpl(CurrencyDefinitionRepository currencyDefinitionRepository) {
	this.currencyDefinitionRepository = currencyDefinitionRepository;
    }

    @Transactional
    @Override
    public void save(CurrencyDefinition currencyDefinition) {
	currencyDefinitionRepository.save(currencyDefinition);
    }

}
