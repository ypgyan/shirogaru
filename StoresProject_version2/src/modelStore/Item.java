package modelStore;

import java.io.Serializable;

public class Item implements Comparable<Item>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6234200967984844710L;
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
		return this.loja.getId() + " " + String.format("%-10s", this.produto) + " " + String.format("%d %-5s R$ %.2f", this.quantidade, "unid", this.preco);
	}

	@Override
	public int compareTo(Item it)
	{
		int comparador = 0;
		double valor = this.preco - it.preco;
		
		/* Pergunta se o valor é diferente que zero, pois quando o valor é muito pequeno o casting de double para int pode dar zero
		o que poderia trazer uma ordenação errada */
		if (valor != 0) {
			if (valor < 0)
				comparador = -1; 
			else
				comparador = 1; // valor > 0
		}
		
		return comparador;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if ((obj == null) || !(obj instanceof Item))
    		return false; 
		
		Loja loja = ((Item)obj).loja;
		Produto produto = ((Item)obj).produto;
		
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
