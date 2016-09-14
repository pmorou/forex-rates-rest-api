package forex.rates.api.autostart.dataset.impl.ecb;

import forex.rates.api.autostart.dataset.ExtractedCurrencyRate;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Component
public class ExtractedCurrencyRateEcbImpl implements ExtractedCurrencyRate {

    @Override
    public CurrencyRate getCurrencyRate(CurrencyDefinition currencyDefinition, Map.Entry<String, String> rate) {
	CurrencyRate currencyRate = new CurrencyRate();
	currencyRate.setCurrency(currencyDefinition);

	if ("NaN".equals(rate.getValue())) {
	    rate.setValue("0");
	}

	LocalDate date = null;
	BigDecimal exchangeRate = null;

	try {
	    date = LocalDate.parse(rate.getKey());
	    exchangeRate = new BigDecimal(rate.getValue());
	} catch (NumberFormatException | DateTimeParseException e) {
	    throw new IllegalArgumentException("Couldn't parse given value");
	}

	currencyRate.setDate(date);
	currencyRate.setExchangeRate(exchangeRate);
	return currencyRate;
    }

}