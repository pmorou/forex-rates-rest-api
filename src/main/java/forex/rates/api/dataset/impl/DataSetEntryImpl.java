package forex.rates.api.dataset.impl;

import forex.rates.api.dataset.CompleteDataSet;
import forex.rates.api.dataset.DataSetEntry;
import forex.rates.api.dataset.CurrencyDefinitionFactory;
import forex.rates.api.dataset.CurrencyRateFactory;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DataSetEntryImpl implements DataSetEntry, CompleteDataSet {

    private final CurrencyRateFactory currencyRateFactory;
    private final CurrencyDefinitionFactory currencyDefinitionFactory;

    private List<CurrencyDefinition> currencyDefinitions;
    private List<CurrencyRate> currencyRates;
    private Map<String, String> tempAttributesHolder;
    private Map<String, String> tempRatesHolder;

    @Autowired
    public DataSetEntryImpl(CurrencyRateFactory currencyRateFactory, CurrencyDefinitionFactory currencyDefinitionFactory) {
	this.currencyRateFactory = currencyRateFactory;
	this.currencyDefinitionFactory = currencyDefinitionFactory;
	this.currencyDefinitions = new ArrayList<>();
	this.currencyRates = new ArrayList<>();
    }

    @Override
    public void newCurrency() {
	tempAttributesHolder = new HashMap<>();
	tempRatesHolder = new HashMap<>();
    }

    @Override
    public void addAttribute(String key, String value) {
	tempAttributesHolder.put(key, value);
    }

    @Override
    public void addRate(String date, String value) {
	tempRatesHolder.put(date, value);
    }

    @Override
    public void saveCurrency() {
	convertToEntities();
    }

    private void convertToEntities() {
	CurrencyDefinition currencyDefinition = currencyDefinitionFactory.getCurrencyDefinition(tempAttributesHolder);
	currencyDefinitions.add(currencyDefinition);

	for (Map.Entry<String, String> rate : tempRatesHolder.entrySet()) {
	    try {
		CurrencyRate currencyRate = currencyRateFactory.getCurrencyRate(currencyDefinition, rate);
		currencyRates.add(currencyRate);
	    } catch (IllegalArgumentException e) {
		log.warn("Invalid date or exchange rate (currency: {}, date: {}, exchange rate: {})",
			currencyDefinition.getCodeName(), rate.getKey(), rate.getValue());
	    }
	}
    }

    @Override
    public List<CurrencyRate> getCurrencyRates() {
	return currencyRates;
    }

    @Override
    public List<CurrencyDefinition> getCurrencyDefinitions() {
	return currencyDefinitions;
    }

}
