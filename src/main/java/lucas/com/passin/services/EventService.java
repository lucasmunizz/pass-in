package lucas.com.passin.services;

import lombok.RequiredArgsConstructor;
import lucas.com.passin.domain.attendee.Attendee;
import lucas.com.passin.domain.event.Event;
import lucas.com.passin.domain.event.exceptions.EventNotFoundException;
import lucas.com.passin.dto.event.EventDetailDTO;
import lucas.com.passin.dto.event.EventIdDTO;
import lucas.com.passin.dto.event.EventRequestDTO;
import lucas.com.passin.dto.event.EventResponseDTO;
import lucas.com.passin.repositories.AttendeeRepository;
import lucas.com.passin.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeRepository;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID:" + eventId));
        List<Attendee> attendeeList = this.attendeRepository.findEventById(eventId);
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

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return  normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]","")
                .replaceAll("\\s+","-")
                .toLowerCase();
    }

}
