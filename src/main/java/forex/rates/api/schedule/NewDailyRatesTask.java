package forex.rates.api.schedule;

import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRatesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NewDailyRatesTask implements RunnableTask {

    private final DataSetUpdate dataSetUpdate;
    private final CurrencyRatesRepository currencyRatesRepository;

    @Autowired
    public NewDailyRatesTask(DataSetUpdate dataSetUpdate, CurrencyRatesRepository currencyRatesRepository) {
	this.dataSetUpdate = dataSetUpdate;
	this.currencyRatesRepository = currencyRatesRepository;
    }

    @Override
    public void run() {
	log.info("Scheduled task has started: adding new rates");

	List<CurrencyRate> currencyRates = dataSetUpdate.getNewCurrencyRates();
	for (CurrencyRate currencyRate : currencyRates) {
	    currencyRatesRepository.save(currencyRate);
	}

	log.info("New rates has been added");
    }

}