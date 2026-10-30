package Model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sale {
    private int id;
    private String sugestionType;
    private LocalDate sugestionDate;
    private double discountPercentage;
    private LocalDate promotionValidFrom;
    private LocalDate promotionValidUntil;
    private String productName;
    private int ProductId;

    public Sale(
            String sugestionType,
            LocalDate sugestionDate,
            double discountPercentage,
            LocalDate promotionValidFrom,
            LocalDate promotionValidUntil,
            String productName
            ){
        this.sugestionType = sugestionType;
        this.sugestionDate = sugestionDate;
        this.discountPercentage = discountPercentage;
        this.promotionValidFrom = promotionValidFrom;
        this.promotionValidUntil = promotionValidUntil;
        this.productName = productName;
    }
}