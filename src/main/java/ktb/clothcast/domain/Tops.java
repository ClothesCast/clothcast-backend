package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Tops {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topId;

    private String knit;
    private String mantoman;
    private String hoodT;
    private String shirt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users users;
}
