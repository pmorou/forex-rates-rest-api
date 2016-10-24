package forex.rates.api.repository.impl.jpa;

import forex.rates.api.model.entity.CurrencyDefinition;
import forex.rates.api.repository.CurrencyDefinitionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Profile("hibernate-jpa")
@Repository
public class CurrencyDefinitionRepositoryJpaImpl implements CurrencyDefinitionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CurrencyDefinition> findAll() {
	Query query = entityManager.createQuery("FROM CurrencyDefinition");
	return query.getResultList();
    }

    @Override
    public List<CurrencyDefinition> findAllByCodeNameIn(List<String> codeName) {
	Query query = entityManager.createQuery("FROM CurrencyDefinition cd WHERE cd.codeName IN :codeName");
	query.setParameter("codeName", codeName);
	return query.getResultList();
    }

    @Override
    public CurrencyDefinition findOneByCodeName(String codeName) {
	Query query = entityManager.createQuery("FROM CurrencyDefinition cd WHERE cd.codeName = :codeName");
	query.setParameter("codeName", codeName);
	CurrencyDefinition currencyDefinition = null;
	try {
	    currencyDefinition = (CurrencyDefinition) query.getSingleResult();
	} catch (NoResultException e) {
	}
	return currencyDefinition;
    }

    @Override
    public CurrencyDefinition save(CurrencyDefinition currencyDefinition) {
	CurrencyDefinition managedInstance = currencyDefinition;
	if (currencyDefinition.getId() == null) {
	    entityManager.persist(currencyDefinition);
	} else {
	    managedInstance = entityManager.merge(currencyDefinition);
	}
	return managedInstance;
    }

    @Override
    public List<CurrencyDefinition> save(Iterable<CurrencyDefinition> currencyDefinitions) {
	List<CurrencyDefinition> managedInstances = new ArrayList<>();
	currencyDefinitions.forEach(cd -> managedInstances.add(save(cd)));
	return managedInstances;
    }

}
