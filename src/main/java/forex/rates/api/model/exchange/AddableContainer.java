package forex.rates.api.model.exchange;

import java.math.BigDecimal;

public interface AddableContainer<T> {

    void add(String currency, BigDecimal value);

    void add(T container);

}
