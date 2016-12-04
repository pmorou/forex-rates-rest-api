package forex.rates.api.model.exchange;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeTransactionsTest {

    public static final LocalDate DATE_2001_01_01 = LocalDate.of(2001, 1, 1);

    @Test
    public void shouldReturnExchangeTransactionWith15UsdAnd10Jpy() throws Exception {
	// Given
	HashMap<String, BigDecimal> currencyRatePairs = new HashMap<String, BigDecimal>() {{
	    put("EUR", new BigDecimal("0.1234"));
	    put("JPY", new BigDecimal("2.3456"));
	}};
	ExchangeRates exchangeRates = createExchangeRates(DATE_2001_01_01, "USD", currencyRatePairs);

	// When
	ExchangeTransactions result = new ExchangeTransactions(exchangeRates, new BigDecimal(10));

	// Then
	assertThat(result.getAmount()).isEqualTo(new BigDecimal("10"));
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getFrom()).isEqualTo("USD");
	assertThat(result.getTo()).isNotNull();
	Map<LocalDate, Transactions> transactionsByDate = result.getTo();
	Map<String, BigDecimal> singleDayResult = transactionsByDate.get(DATE_2001_01_01).getTo();
	assertThat(singleDayResult.get("EUR")).isEqualTo(new BigDecimal("1.23"));
	assertThat(singleDayResult.get("JPY")).isEqualTo(new BigDecimal("23.5"));
    }

    @Test
    public void shouldReturnExchangeTransactionWith3UsdAnd2JpyFirstDayAnd6UsdAnd4JpySecondDay() throws Exception {
	// Given
	HashMap<String, BigDecimal> currencyRatePairsDayFirst = new HashMap<String, BigDecimal>() {{
	    put("EUR", new BigDecimal("1.5"));
	    put("JPY", new BigDecimal("1.0"));
	}};
	HashMap<String, BigDecimal> currencyRatePairsDaySecond = new HashMap<String, BigDecimal>() {{
	    put("EUR", new BigDecimal("3.0"));
	    put("JPY", new BigDecimal("2.0"));
	}};
	ExchangeRates exchangeRates = createExchangeRates(DATE_2001_01_01, "USD", currencyRatePairsDayFirst,
		currencyRatePairsDaySecond);

	// When
	ExchangeTransactions result = new ExchangeTransactions(exchangeRates, new BigDecimal(2));

	// Then
	assertThat(result.getAmount()).isEqualTo(new BigDecimal("2"));
	assertThat(result.getStartDate()).isEqualTo(DATE_2001_01_01);
	assertThat(result.getFrom()).isEqualTo("USD");
	assertThat(result.getTo()).isNotNull();
	Map<LocalDate, Transactions> transactionsByDate = result.getTo();
	Map<String, BigDecimal> firstDayResult = transactionsByDate.get(DATE_2001_01_01).getTo();
	assertThat(firstDayResult.get("EUR")).isEqualTo(new BigDecimal("3.0"));
	assertThat(firstDayResult.get("JPY")).isEqualTo(new BigDecimal("2.0"));
	Map<String, BigDecimal> secondDayResult = transactionsByDate.get(DATE_2001_01_01.plusDays(1)).getTo();
	assertThat(secondDayResult.get("EUR")).isEqualTo(new BigDecimal("6.0"));
	assertThat(secondDayResult.get("JPY")).isEqualTo(new BigDecimal("4.0"));
    }

    private ExchangeRates createExchangeRates(LocalDate startDate, String baseCurrency, HashMap<String, BigDecimal>... currencyRatePairs) {
	HashMap<LocalDate, Rates> dateRatesPairs = new HashMap<>();
	int i = 0;
	for (HashMap<String, BigDecimal> currencyRatePair : currencyRatePairs) {
	    dateRatesPairs.put(startDate.plusDays(i), createRates(currencyRatePair));
	    i++;
	}

	return new ExchangeRates(
		startDate,
		startDate.plusDays(i),
		baseCurrency,
		dateRatesPairs);
    }

    private Rates createRates(Map<String, BigDecimal> currencyValuePairs) {
	return currencyValuePairs.entrySet().stream().collect(Rates::new,
		(rates, entry) -> rates.add(entry.getKey(), entry.getValue()),
		(rates1, rates2) -> rates2.getRates().entrySet().stream()
			.forEach(entry -> rates1.add(entry.getKey(), entry.getValue())));
    }

}