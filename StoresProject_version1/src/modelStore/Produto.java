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
		return "Nome: " + this.nome; 
	}
}
