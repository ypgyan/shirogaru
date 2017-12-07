package modelStore;

public class ItemCarrinho {
	/* Atributos */
	private int quantidade;
	private Item item;
	
	/* Construtor */
	public ItemCarrinho(Item item, int quantidade) {
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
}
