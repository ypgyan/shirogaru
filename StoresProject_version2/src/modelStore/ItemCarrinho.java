package modelStore;

import java.io.Serializable;

public class ItemCarrinho implements Comparable<ItemCarrinho>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1545927667744145405L;
	/* Atributos */
	private int quantidade;
	private Item item;
	
	/* Construtor */
	public ItemCarrinho(Item item, int quantidade) 
	{
		this.item = item;
		this.quantidade = quantidade;
	}
	
	/* Metodos */
	public int getQuantidade() 
	{
		return this.quantidade;
	}
	
	public Item getItem() 
	{
		return this.item;
	}
	
	@Override
	public String toString() 
	{
		int codigo = this.getItem().getProduto().getCodigo();
		String nome = this.getItem().getProduto().getNome();
		String idLoja = this.item.getLoja().getId();
		
		return  String.format("%-5s %-5d %-35s", idLoja, codigo, nome) + String.format("%-2d %-7s R$ %.2f", 
				this.quantidade, "unid", this.item.getPreco());
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof ItemCarrinho))
			return false;
		
		if (this.item.equals( ((ItemCarrinho)obj).item ))
			return true;
		
		return false;
	}
	
	@Override
	public int compareTo(ItemCarrinho itc) 
	{
		int comparador = this.quantidade - itc.quantidade;
		
		// Caso a quantidade seja a mesma de ambos itens compara pelo código do produto
		if (comparador == 0)
			comparador = this.item.getProduto().compareTo(itc.item.getProduto());
			
		return comparador;
	}
	
	/* Adiciona mais unidades itens caso para o determinado item carrinho já existente */
	public boolean adicionaQtdItens(int quantidade)
	{
		if (quantidade > 0 && this.item.getQuantidade() - quantidade >= 0) {
			this.quantidade += quantidade;
			return true;
		}
			
		return false;
	}
	
	/* Remove unidades itens caso para o determinado item carrinho já existente */
	public boolean removeQtdItens(int quantidade) 
	{
		if (quantidade > 0 && quantidade <= this.quantidade) {
			this.quantidade -= quantidade;
			return true;
		} 
		
		return false;
	}

}
