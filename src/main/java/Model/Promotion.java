package Model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class Promotion {
    private int id;
    private String sugestionType;
    private LocalDate sugestionDate;
    private double discountPercentage;
    private LocalDate promotionValidFrom;
    private LocalDate promotionValidUntil;
    private int productId;

    public Promotion(
            int id,
            String sugestionType,
            LocalDate sugestionDate,
            double discountPercentage,
            LocalDate promotionValidFrom,
            LocalDate promotionValidUntil,
            int productId
            ) {
        this.id = id;
        this.sugestionType = sugestionType;
        this.sugestionDate = sugestionDate;
        this.discountPercentage = discountPercentage;
        this.promotionValidFrom = promotionValidFrom;
        this.promotionValidUntil = promotionValidUntil;
        this.productId = productId;
    }
}