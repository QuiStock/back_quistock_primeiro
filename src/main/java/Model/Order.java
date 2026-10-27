package Model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private String suggestionType;
    private LocalDate suggestionDate;
    private double totalOrderPrice;
    private int productId;
}
