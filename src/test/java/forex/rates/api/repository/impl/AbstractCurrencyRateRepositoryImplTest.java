package forex.rates.api.repository.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRateRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractCurrencyRateRepositoryImplTest {

    private final LocalDate DATE_2001_01_01 = LocalDate.of(2001, 1, 1);
    private final LocalDate DATE_2001_01_02 = DATE_2001_01_01.plusDays(1);
    private final CurrencyDefinition USD_DEFINITION = createCurrencyDefinition(1L, "USD", 4);
    private final CurrencyDefinition JPY_DEFINITION = createCurrencyDefinition(2L, "JPY", 2);
    private final CurrencyRate USD_RATE_2001_01_01 = createCurrencyRate(1L, new BigDecimal("1.0000001"), DATE_2001_01_01, USD_DEFINITION);
    private final CurrencyRate JPY_RATE_2001_01_01 = createCurrencyRate(2L, new BigDecimal("1.0000002"), DATE_2001_01_01, JPY_DEFINITION);

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Test
    public void shouldFindSingleCurrencyRateByDateAndCurrency() throws Exception {
	// When
	CurrencyRate result = currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_01, USD_DEFINITION);

	// Then
	assertThat(result).isEqualTo(USD_RATE_2001_01_01);
    }

    @Test
    public void shouldNotFindCurrencyRateByDateAndCurrency() throws Exception {
	// When
	CurrencyRate result = currencyRateRepository.findOneByDateAndCurrency(DATE_2001_01_01.plusWeeks(1), USD_DEFINITION);

	// Then
	assertThat(result).isNull();
    }

    @Test
    public void shouldFindAllCurrencyRatesByDateAndCurrencyIn() throws Exception {
	// When
	List<CurrencyRate> result = currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01, asList(USD_DEFINITION, JPY_DEFINITION));

	// Then
	assertThat(result).containsOnly(USD_RATE_2001_01_01, JPY_RATE_2001_01_01);
    }

    @Test
    public void shouldNotFindCurrencyRatesByDateAndCurrencyIn() throws Exception {
	// When
	List<CurrencyRate> result = currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_01.plusWeeks(1), asList(USD_DEFINITION, JPY_DEFINITION));

	// Then
	assertThat(result).isEmpty();
    }

    @Test
    @Transactional
    public void shouldInsertSingleCurrencyRateWithGeneratedId() throws Exception {
	// Given
	List<CurrencyRate> before = currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION));
	CurrencyRate usdRate2001_01_02 = createCurrencyRate(null, new BigDecimal("2.0000001"), DATE_2001_01_02, USD_DEFINITION);

	// When
	currencyRateRepository.save(usdRate2001_01_02);

	// Then
	List<CurrencyRate> after = currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION));
	assertThat(after).hasSize(before.size() + 1);
	assertThat(usdRate2001_01_02.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void shouldInsertAllCurrencyRatesWithGeneratedId() throws Exception {
	// Given
	List<CurrencyRate> before = currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION, JPY_DEFINITION));
	CurrencyRate usdRate2001_01_02 = createCurrencyRate(null, new BigDecimal("2.0000001"), DATE_2001_01_02, USD_DEFINITION);
	CurrencyRate jpyRate2001_01_02 = createCurrencyRate(null, new BigDecimal("2.0000002"), DATE_2001_01_02, JPY_DEFINITION);

	// When
	currencyRateRepository.save(asList(usdRate2001_01_02, jpyRate2001_01_02));

	// Then
	List<CurrencyRate> after = currencyRateRepository.findAllByDateAndCurrencyIn(DATE_2001_01_02, asList(USD_DEFINITION, JPY_DEFINITION));
	assertThat(after).hasSize(before.size() + 2);
	assertThat(usdRate2001_01_02.getId()).isNotNull();
	assertThat(jpyRate2001_01_02.getId()).isNotNull();
    }

    private CurrencyDefinition createCurrencyDefinition(long id, String codeName, int precision) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition(codeName, precision);
	currencyDefinition.setId(id);
	return currencyDefinition;
    }

    private CurrencyRate createCurrencyRate(Long id, BigDecimal exchangeRate, LocalDate date, CurrencyDefinition currencyDefinition) {
	CurrencyRate currencyRate = new CurrencyRate(exchangeRate, date, currencyDefinition);
	currencyRate.setId(id);
	return currencyRate;
    }

}
