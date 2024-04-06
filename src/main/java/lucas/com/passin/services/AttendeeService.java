package lucas.com.passin.services;

import lombok.RequiredArgsConstructor;
import lucas.com.passin.domain.attendee.Attendee;
import lucas.com.passin.domain.checkin.CheckIn;
import lucas.com.passin.dto.attendees.AttendeeDetails;
import lucas.com.passin.dto.attendees.AttendeesListResponseDTO;
import lucas.com.passin.repositories.AttendeeRepository;
import lucas.com.passin.repositories.CheckinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt );
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }
}
