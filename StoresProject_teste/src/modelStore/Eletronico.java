package modelStore;

public class Eletronico extends Produto {
	/* Atributos */
	private String marca;
	private String cor;
	private double peso;
	
	
	public Eletronico(String nome, int cod, String marca, String cor, double peso) {
		super(nome, cod);
		this.marca = marca;
		this.cor = cor;
		this.peso = peso;
	}
	
	
	public String getMarca() 
	{
		return this.marca;
	}
	
	
	public String getCor() 
	{
		return this.cor;
	}

	
	public double getPeso() 
	{
		return this.peso;
	}
	
	@Override
	public String toString() { 
		return super.toString() + String.format("%-15s", "Eletronico");
	}
}
