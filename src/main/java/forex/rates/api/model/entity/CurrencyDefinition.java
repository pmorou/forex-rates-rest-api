package forex.rates.api.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = "id")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "currencies")
public class CurrencyDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String codeName;

    @NonNull
    private Integer precision;

}
