package modelStore;

public class Item implements Comparable<Item> {
	/* Atributos */
	private double preco;
	private Produto produto;
	private Loja loja;
	private int quantidade; //Quantidade em estoque
	
	
	/* Construtor */
	public Item(Loja loja, Produto prod, double preco, int quantidade) 
	{
		this.preco = preco;
		this.produto = prod; //Ja recebe a referencia do produto construido
		this.loja = loja;
		this.quantidade = quantidade;
	}


	/* Métodos */
	public double getPreco() 
	{
		return preco;
	}


	public Produto getProduto() 
	{
		return produto;
	}
	
	
	public Loja getLoja()
	{
		return this.loja;
	}
	
	public int getQuantidade() 
	{
		return quantidade;
	}
	
	@Override
	public String toString() 
	{
		return this.loja.getId() + " " + String.format("%-10s", this.produto) + " " + String.format("%d %-5s R$ %.2f", this.quantidade, "unid",this.preco);
	}

	@Override
	public int compareTo(Item it)
	{		
		return (int) (this.preco - it.preco);
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if ((obj == null) || !(obj instanceof Item))
    		return false; 
		
		Loja loja = ((Item)obj).getLoja();
		Produto produto = ((Item)obj).getProduto();
		
		if (this.produto.equals(produto) && this.loja.equals(loja))
			return true;
		
		return false;
	}
	
	public boolean debitarEstoque(int quantidade) 
	{
		if (quantidade <= this.quantidade && quantidade > 0) {
			this.quantidade -= quantidade;
			return true;
		}
		
		return false;
	}
	
	//Usado principalmente se o usuário cancelar a compra
	public boolean inserirEstoque(int quantidade) 
	{
		if (quantidade > 0) {
			this.quantidade += quantidade;
			return true;
		}
		
		return false;
	}
}
