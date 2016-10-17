package forex.rates.api.autostart;

import forex.rates.api.dataset.CompleteDataSet;
import forex.rates.api.dataset.DataSetSource;
import forex.rates.api.schedule.NewRatesSchedule;
import forex.rates.api.service.CurrencyDefinitionService;
import forex.rates.api.service.CurrencyRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class AutostartImpl implements Autostart {

    private final String OPERATION_SUMMARY_MSG = "Operation took {} milliseconds for total of {} items.";

    private final DataSetSource dataSetSource;
    private final CurrencyDefinitionService currencyDefinitionService;
    private final CurrencyRateService currencyRateService;
    private final NewRatesSchedule newRatesSchedule;

    @Autowired
    public AutostartImpl(DataSetSource dataSetSource, CurrencyDefinitionService currencyDefinitionService, CurrencyRateService currencyRateService, NewRatesSchedule newRatesSchedule) {
        this.dataSetSource = dataSetSource;
        this.currencyDefinitionService = currencyDefinitionService;
        this.currencyRateService = currencyRateService;
        this.newRatesSchedule = newRatesSchedule;
    }

    @Override
    @PostConstruct
    public void start() {
        persistDataSet();
        scheduleRatesUpdate();
    }

    private void persistDataSet() {
        CompleteDataSet dataSet = dataSetSource.getCompleteDataSet();

        long start = System.currentTimeMillis();
        log.info("Inserting currency definitions...");
        currencyDefinitionService.save(dataSet.getCurrencyDefinitions());
        log.info(OPERATION_SUMMARY_MSG, getElapsedTime(start), dataSet.getCurrencyDefinitions().size());

        start = System.currentTimeMillis();
        log.info("Inserting currency rates...");
        currencyRateService.save(dataSet.getCurrencyRates());
        log.info(OPERATION_SUMMARY_MSG, getElapsedTime(start), dataSet.getCurrencyRates().size());
    }

    private long getElapsedTime(long start) {
        return System.currentTimeMillis() - start;
    }

    private void scheduleRatesUpdate() {
        newRatesSchedule.scheduleUpdate();
    }

}
