package Model;

public class Endereco {
    private int id;
    private String cep;
    private String rua;
    private int numero;
    private String estado;
    private String cidade;

    public Endereco(String cep, String rua, int numero, String estado, String cidade) {
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.estado = estado;
        this.cidade = cidade;
    }


}
