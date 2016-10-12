package forex.rates.api.autostart;

import forex.rates.api.autostart.dataset.CompleteDataSet;
import forex.rates.api.autostart.dataset.DataSetSource;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.service.CurrencyDefinitionService;
import forex.rates.api.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AutostartImpl implements Autostart {

    private final DataSetSource dataSetSource;
    private final CurrencyDefinitionService currencyDefinitionService;
    private final CurrencyRateService currencyRateService;

    @Autowired
    public AutostartImpl(DataSetSource dataSetSource, CurrencyDefinitionService currencyDefinitionService, CurrencyRateService currencyRateService) {
        this.dataSetSource = dataSetSource;
        this.currencyDefinitionService = currencyDefinitionService;
        this.currencyRateService = currencyRateService;
    }

    @Override
    @PostConstruct
    public void start() {
        CompleteDataSet dataSet = dataSetSource.getCompleteDataSet();

        long start = System.currentTimeMillis();

        for (CurrencyDefinition currencyDefinition : dataSet.getCurrencyDefinitions()) {
            currencyDefinitionService.save(currencyDefinition);
        }

        System.out.println("Inserting currency definitions took " + (System.currentTimeMillis() - start) + " milliseconds");
        System.out.println(dataSet.getCurrencyDefinitions().size() + " items");

        start = System.currentTimeMillis();

        for (CurrencyRate currencyRate : dataSet.getCurrencyRates()) {
            currencyRateService.save(currencyRate);
        }

        System.out.println("Inserting currency rates took " + (System.currentTimeMillis() - start) + " milliseconds");
        System.out.println(dataSet.getCurrencyRates().size() + " items");
    }

}
