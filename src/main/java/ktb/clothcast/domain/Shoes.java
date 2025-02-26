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

    private Boolean sneakers;
    private Boolean boots;
    private Boolean sandals;
    private Boolean sportsShoes;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
