package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bottoms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bottomId;

    private String denimPants;
    private String cottonPants;
    private String shortPants;
    private String slacks;
    private String miniSkirt;
    private String longSkirt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users users;
}
