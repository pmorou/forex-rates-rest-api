package forex.rates.api.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = "id")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(precision = 15, scale = 7)
    private BigDecimal exchangeRate;

    @NonNull
    private LocalDate date;

    @NonNull
    @OneToOne
    @JoinColumn(name = "currencies_id")
    private CurrencyDefinition currency;

}
