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
	public int getQuantidade() {
		return this.quantidade;
	}
	
	public Item getItem() {
		return this.item;
	}
}
