package Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Pedido {
    String dtPedido;
    String idItem;
    double total;
}
