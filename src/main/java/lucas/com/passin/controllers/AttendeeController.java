package lucas.com.passin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendees")
public class AttendeeController {


    @GetMapping("/{id}")
    public ResponseEntity<String> getEvent(@PathVariable String id){
        return ResponseEntity.ok("sucesso!");
    }
}
