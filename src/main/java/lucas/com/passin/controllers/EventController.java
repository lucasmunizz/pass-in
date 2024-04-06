package lucas.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import lucas.com.passin.dto.event.EventIdDTO;
import lucas.com.passin.dto.event.EventRequestDTO;
import lucas.com.passin.dto.event.EventResponseDTO;
import lucas.com.passin.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = this.service.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO event = this.service.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(event.id()).toUri();
        return ResponseEntity.created(uri).body(event);
    }
}
