package forex.rates.api.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @Column(precision = 15, scale = 7)
    private BigDecimal exchangeRate;

    @NonNull
    @NotNull
    private LocalDate date;

    @NonNull
    @NotNull
    @OneToOne
    @JoinColumn(name = "currencies_id")
    private CurrencyDefinition currency;

}
