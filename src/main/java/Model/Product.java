package Model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private String batch;
    private double price;
    private int quantity;
    private String brand;
    private int store_id;
    private String store_Name;
    private LocalDate expirationDate;
    private String line;
}