package Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String brand;
    private int store_id;
    private String store_Name;
}