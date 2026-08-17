package Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    String dtPedido;
    String idItem;
    double total;
}
