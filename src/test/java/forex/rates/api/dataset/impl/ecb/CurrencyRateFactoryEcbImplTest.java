package forex.rates.api.dataset.impl.ecb;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyRateFactoryEcbImplTest {

    @Test
    public void shouldReturnRatesForUsd() throws Exception {
	// Given
	CurrencyDefinition currencyDefinition = createCurrencyDefinition("USD", 4);
	Map.Entry<String, String> entry = createRateEntry("2001-01-01", "1.0305");

	// When
	CurrencyRate currencyRate =
		new CurrencyRateFactoryEcbImpl().getCurrencyRate(currencyDefinition, entry);

	// Then
	assertThat(currencyRate.getDate()).isEqualTo(LocalDate.of(2001, 1, 1));
	assertThat(currencyRate.getExchangeRate()).isEqualTo(new BigDecimal("1.0305"));
	assertThat(currencyRate.getCurrency()).isEqualTo(currencyDefinition);
    }

    @Test
    public void shouldReturnRatesForJpy() throws Exception {
	// Given
	CurrencyDefinition currencyDefinition = createCurrencyDefinition("JPY", 2);
	Map.Entry<String, String> entry = createRateEntry("2001-01-02", "102.75");

	// When
	CurrencyRate currencyRate =
		new CurrencyRateFactoryEcbImpl().getCurrencyRate(currencyDefinition, entry);

	// Then
	assertThat(currencyRate.getDate()).isEqualTo(LocalDate.of(2001, 1, 2));
	assertThat(currencyRate.getExchangeRate()).isEqualTo(new BigDecimal("102.75"));
	assertThat(currencyRate.getCurrency()).isEqualTo(currencyDefinition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenInvalidDate() throws Exception {
	// Given
	CurrencyDefinition currencyDefinition = createCurrencyDefinition("JPY", 2);
	Map.Entry<String, String> entry = createRateEntry("2001/01/01", "102.75");

	// When
	CurrencyRate currencyRate =
		new CurrencyRateFactoryEcbImpl().getCurrencyRate(currencyDefinition, entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenInvalidRate() throws Exception {
	// Given
	CurrencyDefinition currencyDefinition = createCurrencyDefinition("JPY", 2);
	Map.Entry<String, String> entry = createRateEntry("2001-01-01", "102,75");

	// When
	CurrencyRate currencyRate =
		new CurrencyRateFactoryEcbImpl().getCurrencyRate(currencyDefinition, entry);
    }

    private CurrencyDefinition createCurrencyDefinition(String codeName, Integer precision) {
	return new CurrencyDefinition(codeName, precision);
    }

    private Map.Entry<String, String> createRateEntry(String date, String rate) {
	Map<String, String> rates = new HashMap<>(1);
	rates.put(date, rate);
	return rates.entrySet().iterator().next();
    }

}