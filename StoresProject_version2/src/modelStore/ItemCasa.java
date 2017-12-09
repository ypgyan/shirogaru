package modelStore;

public class ItemCasa extends Produto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1739917250841450028L;
	/* Atributos */
	private String material;
	private String cor;
	private String tipo;
	
	
	/* Construtor */
	public ItemCasa(String nome, int cod, String material, String cor, String tipo) {
		super(nome, cod);
		this.cor = cor;
		this.material = material;
		super.setTipoProd("Item casa");
		this.tipo = tipo;
		
	}

	
	/* M�todos */
	public String getMaterial() {
		return material;
	}


	public String getCor() {
		return cor;
	}


	public String getTipo() {
		return tipo;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + String.format("%-15s", "Item de Casa");
	}
}
