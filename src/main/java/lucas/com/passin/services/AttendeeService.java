package lucas.com.passin.services;

import lombok.RequiredArgsConstructor;
import lucas.com.passin.domain.attendee.Attendee;
import lucas.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import lucas.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import lucas.com.passin.domain.checkin.CheckIn;
import lucas.com.passin.dto.attendees.AttendeeBadgeResponseDTO;
import lucas.com.passin.dto.attendees.AttendeeDetails;
import lucas.com.passin.dto.attendees.AttendeesListResponseDTO;
import lucas.com.passin.dto.attendees.AttendeeBadgeDTO;
import lucas.com.passin.repositories.AttendeeRepository;
import lucas.com.passin.repositories.CheckinRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt );
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if(isAttendeeRegistered.isPresent()) {
            throw new AttendeeAlreadyExistException("Attendee is already registered");
        }
    }

    public Attendee registerAteendee (Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void checkInAttendee(String attendeeId){

        Attendee attendee = getAttendee(attendeeId);

        this.checkInService.registerCheckIn(attendee);

    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }
}
