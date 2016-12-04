package forex.rates.api.model.exchange;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Transactions {

    @Getter(onMethod = @__(@JsonAnyGetter))
    private final Map<String, BigDecimal> to = new HashMap<>();

    public void add(String currency, BigDecimal amount) {
	to.put(currency, amount);
    }

}
