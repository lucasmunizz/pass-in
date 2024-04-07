package lucas.com.passin.services;

import lombok.RequiredArgsConstructor;
import lucas.com.passin.domain.attendee.Attendee;
import lucas.com.passin.domain.attendee.exceptions.EventFullException;
import lucas.com.passin.domain.event.Event;
import lucas.com.passin.domain.event.exceptions.EventNotFoundException;
import lucas.com.passin.dto.attendees.AttendeeIdDTO;
import lucas.com.passin.dto.attendees.AttendeeRequestDTO;
import lucas.com.passin.dto.event.EventDetailDTO;
import lucas.com.passin.dto.event.EventIdDTO;
import lucas.com.passin.dto.event.EventRequestDTO;
import lucas.com.passin.dto.event.EventResponseDTO;
import lucas.com.passin.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event event = new Event();
        event.setTitle(eventDTO.title());
        event.setDetails(eventDTO.details());
        event.setMaximumAttendees(eventDTO.maximumAttendees());
        event.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(event);

        return new EventIdDTO(event.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(),eventId);
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size()) {
            throw new EventFullException("Event is full");
        }

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAteendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());

    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID:" + eventId));
    }

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return  normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]","")
                .replaceAll("\\s+","-")
                .toLowerCase();
    }

}
