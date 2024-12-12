package store.aurora.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class JwtRequestDto {
    private String username;
    private String role;
    private Long expiredMs;
}
