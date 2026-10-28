package Model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Faq {
    private int id;
    private String question;
    private String answer;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}