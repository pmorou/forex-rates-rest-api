package forex.rates.api.schedule;

import forex.rates.api.dataset.DataSetUpdate;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.service.CurrencyRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NewDailyRatesTask implements RunnableTask {

    private final DataSetUpdate dataSetUpdate;
    private final CurrencyRateService currencyRateService;

    @Autowired
    public NewDailyRatesTask(DataSetUpdate dataSetUpdate, CurrencyRateService currencyRateService) {
	this.dataSetUpdate = dataSetUpdate;
	this.currencyRateService = currencyRateService;
    }

    @Override
    public void run() {
	log.info("Scheduled task has started: adding new rates");

	List<CurrencyRate> currencyRates = dataSetUpdate.getNewCurrencyRates();
	currencyRateService.save(currencyRates);

	log.info("New rates has been added");
    }

}