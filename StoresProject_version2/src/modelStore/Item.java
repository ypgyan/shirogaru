package modelStore;

public class Item {
	/* Atributos */
	private double preco;
	private Produto produto;
	private Loja loja;
	private int quantidade; //Quantidade em estoque
	
	
	/* Construtor */
	public Item(Loja loja, Produto prod, double preco, int quantidade) {
		this.preco = preco;
		this.produto = prod; //Ja recebe a referencia do produto construido
		this.loja = loja;
		this.quantidade = quantidade;
	}


	/* Métodos */
	public double getPreco() {
		return preco;
	}


	public Produto getProduto() {
		return produto;
	}
	
	
	public Loja getLoja() {
		return this.loja;
	}
	
	public int getQuantidade() {
		return quantidade;
	}
	
	@Override
	public String toString() {
		return this.loja.getId() + " " + String.format("%-10s", this.produto) + " " + String.format("%-5d %.2f", this.quantidade, this.preco);
	}	
}
