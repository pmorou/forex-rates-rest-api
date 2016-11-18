package forex.rates.api.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeTransactionTest {

    @Test
    public void shouldReturnExchangeTransactionWith15UsdAnd10Jpy() throws Exception {
	// Given
	HashMap<String, BigDecimal> currencyRatePairs = new HashMap<String, BigDecimal>() {{
	    put("EUR", new BigDecimal("1.5"));
	    put("JPY", new BigDecimal("1.0"));
	}};
	ExchangeRates exchangeRates = new ExchangeRates(
		LocalDate.of(2001,1,1),
		LocalDate.of(2001,1,1),
		"USD",
		Collections.singletonMap(
			LocalDate.of(2001,1,1),
			createRates(currencyRatePairs)
		),
		false);

	// When
	ExchangeTransaction result = new ExchangeTransaction(exchangeRates, 10);

	// Then
	assertThat(result.getAmount()).isEqualTo(10);
	assertThat(result.getDate()).isEqualTo(LocalDate.of(2001,1,1));
	assertThat(result.getFrom()).isEqualTo("USD");
	assertThat(result.getTo()).isNotNull();
	Map<String, BigDecimal> exchangeResult = result.getTo();
	assertThat(exchangeResult.get("EUR")).isEqualTo(new BigDecimal("15.0"));
	assertThat(exchangeResult.get("JPY")).isEqualTo(new BigDecimal("10.0"));
    }

    private Rates createRates(Map<String, BigDecimal> currencyValuePairs) {
	return currencyValuePairs.entrySet().stream().collect(Rates::new,
		(rates, entry) -> rates.addRate(entry.getKey(), entry.getValue()),
		(rates1, rates2) -> rates2.getRates().entrySet().stream()
			.forEach(entry -> rates1.addRate(entry.getKey(), entry.getValue())));
    }

}