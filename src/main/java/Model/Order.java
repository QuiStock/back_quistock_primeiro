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
    private LocalDate orderDate;
    private double totalOrderPrice;
    private String productName;
    private int productId;
}
