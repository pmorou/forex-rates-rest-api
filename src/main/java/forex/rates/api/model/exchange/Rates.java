package forex.rates.api.model.exchange;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Rates {

    @Getter(onMethod = @__(@JsonAnyGetter))
    private final Map<String, BigDecimal> rates = new HashMap<>();

    public void addRate(String currency, BigDecimal rate) {
	rates.put(currency, rate);
    }

}
