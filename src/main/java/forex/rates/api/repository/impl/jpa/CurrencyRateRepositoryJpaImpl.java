package forex.rates.api.repository.impl.jpa;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.model.entity.CurrencyRate;
import forex.rates.api.repository.CurrencyRateRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("hibernate-jpa")
@Repository
public class CurrencyRateRepositoryJpaImpl implements CurrencyRateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CurrencyRate> findAllByDateAndCurrencyIn(LocalDate date, List<CurrencyDefinition> currencies) {
	Query query = entityManager.createQuery("FROM CurrencyRate cr WHERE cr.date = :date AND cr.currency IN :currencies")
		.setParameter("date", date)
		.setParameter("currencies", currencies);
	return query.getResultList();
    }

    @Override
    public CurrencyRate findOneByDateAndCurrency(LocalDate date, CurrencyDefinition currency) {
	Query query = entityManager.createQuery("FROM CurrencyRate cr WHERE cr.date = :date AND cr.currency = :currency")
		.setParameter("date", date)
		.setParameter("currency", currency);
	CurrencyRate currencyRate = null;
	try {
	    currencyRate = (CurrencyRate) query.getSingleResult();
	} catch (NoResultException e) {
	}
	return currencyRate;
    }

    @Override
    public CurrencyRate save(CurrencyRate currencyRate) {
	CurrencyRate managedInstance = currencyRate;
	if (currencyRate.getId() == null) {
	    entityManager.persist(currencyRate);
	} else {
	    managedInstance = entityManager.merge(currencyRate);
	}
	return managedInstance;
    }

    @Override
    public List<CurrencyRate> save(Iterable<CurrencyRate> currencyRates) {
	List<CurrencyRate> detachedInstances = new LinkedList<>();
	int i = 0;
	int batchSize = 100;
	for (CurrencyRate currencyRate : currencyRates) {
	    detachedInstances.add(save(currencyRate));
	    if (++i % batchSize == 0) {
		flushAndClear();
	    }
	}
	return detachedInstances;
    }

    private void flushAndClear() {
	entityManager.flush();
	entityManager.clear();
    }

}
