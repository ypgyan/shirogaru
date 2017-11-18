package modelStore;

public abstract class Produto {
	/* Atributos */
	private String nome;
	private String codigo;
    
	/* Construtor */
	public Produto(String nome, String cod) 
	{
		this.nome = nome;
		this.codigo = cod;
	}
	
	/* Métodos */
	public String getNome() 
	{
		return this.nome;
	}
	
	public String getCodigo() 
	{
		return this.codigo;
	}
	
	@Override
	public String toString() {
		return String.format("%-5s", this.codigo) + String.format("%-35s", this.nome); 
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) { return false; }
		
		if (!(obj instanceof Produto)) { return false; } 
		
		((Produto) obj).codigo.equals(this.codigo); 

		return true;
	}
}
