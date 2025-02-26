package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Topwear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topId;

    private Boolean knit;
    private Boolean mantoman;
    private Boolean hoodt;
    private Boolean shirt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
