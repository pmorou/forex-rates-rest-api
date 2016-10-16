package forex.rates.api.service.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import forex.rates.api.service.CurrencyDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class CurrencyDefinitionServiceImpl implements CurrencyDefinitionService {

    private final CurrencyDefinitionRepository currencyDefinitionRepository;

    @Autowired
    public CurrencyDefinitionServiceImpl(CurrencyDefinitionRepository currencyDefinitionRepository) {
	this.currencyDefinitionRepository = currencyDefinitionRepository;
    }

    @Override
    public List<CurrencyDefinition> getAll() {
	return currencyDefinitionRepository.findAll();
    }

    @Override
    public List<CurrencyDefinition> getAllByCodeNameIn(List<String> codeName) {
	return currencyDefinitionRepository.findAllByCodeNameIn(codeName);
    }

    @Override
    public CurrencyDefinition getOneByCodeName(String codeName) {
	return currencyDefinitionRepository.findOneByCodeName(codeName);
    }

    @Transactional
    @Override
    public void save(CurrencyDefinition currencyDefinition) {
	currencyDefinitionRepository.save(currencyDefinition);
    }

    @Transactional
    @Override
    public void save(Iterable<CurrencyDefinition> currencyDefinitions) {
	currencyDefinitionRepository.save(currencyDefinitions);
    }

}
