package lucas.com.passin.domain.checkin;


import jakarta.persistence.*;
import lucas.com.passin.domain.attendee.Attendee;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins")
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;
}
