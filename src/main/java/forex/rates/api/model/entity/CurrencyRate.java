package forex.rates.api.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal exchangeRate;

    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "currencies_id")
    private CurrencyDefinition currency;

}
