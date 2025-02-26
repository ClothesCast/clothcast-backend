package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Outerwear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outerId;

    private Boolean shortPadding;
    private Boolean longPadding;
    private Boolean coat;
    private Boolean leatherJacket;
    private Boolean cardigan;
    private Boolean hoodZipUp;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
