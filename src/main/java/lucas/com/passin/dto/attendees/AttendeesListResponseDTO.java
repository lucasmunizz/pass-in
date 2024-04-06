package lucas.com.passin.dto.attendees;

import lombok.Getter;

import java.util.List;


public record AttendeesListResponseDTO(List<AttendeeDetails> attendees) {
}
