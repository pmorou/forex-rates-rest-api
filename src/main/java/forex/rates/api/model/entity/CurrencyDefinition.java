package forex.rates.api.model.entity;

import lombok.Data;

@Data
public class CurrencyDefinition {

    private Long id;
    private String codeName;
    private int precision;

}
