package Model;

public class Pedido {
    String dtPedido;
    String idItem;
    double total;

    public Pedido(String dtPedido, String idItem, double total) {
        this.dtPedido = dtPedido;
        this.idItem = idItem;
        this.total = total;
    }

    public String getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(String dtPedido) {
        this.dtPedido = dtPedido;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
