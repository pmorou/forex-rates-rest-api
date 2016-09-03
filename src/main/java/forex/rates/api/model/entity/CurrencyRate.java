package forex.rates.api.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CurrencyRate {

    private Long id;
    private CurrencyDefinition currency;
    private BigDecimal exchangeRate;
    private LocalDate date;

}
