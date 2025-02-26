package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Shoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoesId;

    private String sneakers;
    private String boots;
    private String sandals;
    private String sportsShoes;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users users;
}
