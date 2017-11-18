package modelStore;

public class Livro extends Produto {
	/* Atributos */
	private String autor;
	private int nPaginas;
	
	
	/* Construtor */
	public Livro(String nome, String cod, String autor, int nPag) {
		super(nome, cod);
		this.autor = autor;
		this.nPaginas = nPag;
	}
	
	
	/* Métodos */
	public String getAutor() {
		return this.autor;
	}
	
	
	public int getNumPaginas() {
		return this.nPaginas;
	}
	
	
	@Override
	public String toString() {
		String s1 = "Livro\n" + super.toString();
		String s2 = "\nAutor: " + this.autor + "\t\tPaginas: " + this.nPaginas; 
		return s1 + s2;
	}
}
