package lucas.com.passin.repositories;

import lucas.com.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

    public List<Attendee> findbyEventId(String eventId);
}
