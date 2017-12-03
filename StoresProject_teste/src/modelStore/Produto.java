package modelStore;

public abstract class Produto implements Comparable<Produto> {
	/* Atributos */
	private String nome;
	private int codigo;
    
	/* Construtor */
	public Produto(String nome, int cod) 
	{
		this.nome = nome;
		this.codigo = cod;
	}
	
	/* Métodos */
	public String getNome() 
	{
		return this.nome;
	}
	
	public int getCodigo() 
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
		
		else if (!(obj instanceof Produto)) { return false; } 
		
		else if (((Produto) obj).codigo != this.codigo) { return false; }

		return true;
	}
	
	@Override
	public int compareTo(Produto p) {
		return this.codigo - p.codigo;
	}
}
