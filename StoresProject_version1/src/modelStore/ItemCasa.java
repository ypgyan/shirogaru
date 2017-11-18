package modelStore;

public class ItemCasa extends Produto{
	/* Atributos */
	private String material;
	private String cor;
	private String tipo;
	
	
	/* Construtor */
	public ItemCasa(String nome, String cod, String material, String cor, String tipo) {
		super(nome, cod);
		this.cor = cor;
		this.material = material;
		this.tipo = tipo;
	}

	
	/* Métodos */
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
		String s1 = "Item de Casa\n" + super.toString();
		String s2 = "\nMaterial: " + this.material + "\tTipo: " + this.tipo + "\tCor: " + this.cor; 
		return s1 + s2;
	}
}
