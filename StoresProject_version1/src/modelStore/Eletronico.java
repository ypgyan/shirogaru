package modelStore;

public class Eletronico extends Produto {
	/* Atributos */
	private String marca;
	private String cor;
	private double peso;
	
	
	public Eletronico(String nome, String cod, String marca, String cor, double peso) {
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
		String s1 = "Eletronico\n" + super.toString();
		String s2 = "\nMarca: " + this.marca + "\tPeso: " + this.peso + "\tCor: " + this.cor; 
		return s1 + s2;
	}
}
