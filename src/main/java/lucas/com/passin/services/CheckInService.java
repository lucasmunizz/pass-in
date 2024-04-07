package lucas.com.passin.services;

import lombok.RequiredArgsConstructor;
import lucas.com.passin.domain.attendee.Attendee;
import lucas.com.passin.domain.checkin.CheckIn;
import lucas.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import lucas.com.passin.repositories.CheckinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckinRepository checkinRepository;

    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee);
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(newCheckIn);
    }

    private void verifyCheckInExists(Attendee attendee){
        Optional<CheckIn> isCheckedIn = this.checkinRepository.findByAttendeeId(attendee.getId());
        if (isCheckedIn.isPresent()){
            throw new CheckInAlreadyExistsException("Attendee already checked in");
        }
    }
}
