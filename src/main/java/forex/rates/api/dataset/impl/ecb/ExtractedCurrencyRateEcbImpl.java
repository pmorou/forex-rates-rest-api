package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.dataset.ExtractedCurrencyRate;
import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Profile("european-central-bank")
@Component
public class ExtractedCurrencyRateEcbImpl implements ExtractedCurrencyRate {

    @Override
    public CurrencyRate getCurrencyRate(CurrencyDefinition currencyDefinition, Map.Entry<String, String> rate) {
	try {
	    return new CurrencyRate(
		    new BigDecimal(rate.getValue()),
		    LocalDate.parse(rate.getKey()),
		    currencyDefinition);
	} catch (NumberFormatException | DateTimeParseException e) {
	    throw new IllegalArgumentException("Couldn't parse given value");
	}
    }

}