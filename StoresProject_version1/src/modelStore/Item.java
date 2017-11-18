package modelStore;

public class Item {
	/* Atributos */
	private double preco;
	private Produto produto;
	
	
	/* Construtor */
	public Item(Produto prod, double preco) {
		this.preco = preco;
		this.produto = prod; //Ja recebe a referencia do produto construido
	}


	/* Métodos */
	public double getPreco() {
		return preco;
	}


	public Produto getProduto() {
		return produto;
	}
	
	
	@Override
	public String toString() {
		return this.produto.toString() + "\nPreco: " + this.preco + "R$\n";
	}
}
