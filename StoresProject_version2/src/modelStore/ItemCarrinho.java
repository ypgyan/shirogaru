package modelStore;

public class ItemCarrinho {
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
		return this.item.getLoja().getId() + " " + this.getItem().getProduto() + String.format("%-3d %.2f", 
				this.quantidade, this.item.getPreco());
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof ItemCarrinho))
			return false;
		
		if (this.item.equals( ((ItemCarrinho)obj).getItem() ))
			return true;
		
		return false;
	}
	
	/* Adiciona mais unidades itens caso para o determinado item carrinho já existente */
	public boolean adicionaQtdItens(int quantidade)
	{
		if (quantidade > 0 && item.getQuantidade() - quantidade >= 0) {
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
