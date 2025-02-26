package ktb.clothcast.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Outers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outerId;

    private String shortPadding;
    private String longPadding;
    private String coat;
    private String leatherJacket;
    private String cardigan;
    private String hoodZipUp;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;
}
