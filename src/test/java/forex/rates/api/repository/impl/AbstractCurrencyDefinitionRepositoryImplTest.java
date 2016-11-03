package forex.rates.api.repository.impl;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractCurrencyDefinitionRepositoryImplTest {

    private final CurrencyDefinition USD_DEFINITION = createCurrencyDefinition(1L, "USD", 4);
    private final CurrencyDefinition JPY_DEFINITION = createCurrencyDefinition(2L, "JPY", 2);
    private final CurrencyDefinition GBP_DEFINITION = createCurrencyDefinition(3L, "GBP", 5);

    @Autowired
    private CurrencyDefinitionRepository currencyDefinitionRepository;

    @Test
    public void shouldFindSingleCurrencyDefinitionByCodeName() throws Exception {
	// When
	CurrencyDefinition result = currencyDefinitionRepository.findOneByCodeName("USD");

	// Then
	assertThat(result).isEqualTo(USD_DEFINITION);
    }

    @Test
    public void shouldFindAllCurrencyDefinitionsByCodeNameIn() throws Exception {
	// When
	List<CurrencyDefinition> result = currencyDefinitionRepository.findAllByCodeNameIn(asList("USD", "JPY"));

	// Then
	assertThat(result).containsOnly(USD_DEFINITION, JPY_DEFINITION);
    }

    @Test
    public void shouldFindAllCurrencyDefinitions() throws Exception {
	// When
	List<CurrencyDefinition> result = currencyDefinitionRepository.findAll();

	// Then
	assertThat(result).containsOnly(USD_DEFINITION, JPY_DEFINITION, GBP_DEFINITION);
    }

    @Test
    @Transactional
    public void shouldInsertSingleCurrencyDefinitionWithGeneratedId() throws Exception {
	// Given
	List<CurrencyDefinition> before = currencyDefinitionRepository.findAllByCodeNameIn(asList("PLN"));
	CurrencyDefinition currencyDefinition = new CurrencyDefinition("PLN", 4);

	// When
	currencyDefinitionRepository.save(currencyDefinition);

	// Then
	List<CurrencyDefinition> after = currencyDefinitionRepository.findAllByCodeNameIn(asList("PLN"));
	assertThat(after).hasSize(before.size() + 1);
	assertThat(currencyDefinition.getId()).isNotNull();
    }

    @Test
    public void shouldNotFindCurrencyDefinition() throws Exception {
	// When
	CurrencyDefinition result = currencyDefinitionRepository.findOneByCodeName("PLN");

	// Then
	assertThat(result).isNull();
    }

    @Test
    @Transactional
    public void shouldInsertAllCurrencyDefinitionsWithGeneratedId() throws Exception {
	// Given
	List<CurrencyDefinition> before = currencyDefinitionRepository.findAll();
	CurrencyDefinition plnDefinition = new CurrencyDefinition("PLN", 4);
	CurrencyDefinition eurDefinition = new CurrencyDefinition("EUR", 4);

	// When
	currencyDefinitionRepository.save(asList(plnDefinition, eurDefinition));

	// Then
	List<CurrencyDefinition> after = currencyDefinitionRepository.findAll();
	assertThat(after).hasSize(before.size() + 2);
	assertThat(plnDefinition.getId()).isNotNull();
	assertThat(eurDefinition.getId()).isNotNull();
    }

    @Test
    public void shouldNotFindCurrencyDefinitionsByCodeNameIn() throws Exception {
	// When
	List<CurrencyDefinition> result = currencyDefinitionRepository.findAllByCodeNameIn(asList("PLN", "EUR"));

	// Then
	assertThat(result).isEmpty();
    }

    private CurrencyDefinition createCurrencyDefinition(Long id, String codeName, Integer precision) {
	CurrencyDefinition currencyDefinition = new CurrencyDefinition(codeName, precision);
	currencyDefinition.setId(id);
	return currencyDefinition;
    }

}
