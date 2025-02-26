package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bottomwear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bottomId;

    private Boolean denimPants;
    private Boolean cottonPants;
    private Boolean shortPants;
    private Boolean slacks;
    private Boolean miniSkirt;
    private Boolean longSkirt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
