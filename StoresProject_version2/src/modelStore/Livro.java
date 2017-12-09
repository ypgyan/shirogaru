package modelStore;

public class Livro extends Produto {
	/* Atributos */
	private String autor;
	private int nPaginas;
	
	
	/* Construtor */
	public Livro(String nome, int cod, String autor, int nPag) {
		super(nome, cod);
		this.autor = autor;
		this.nPaginas = nPag;
		super.setTipoProd("Livro");
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
		return super.toString() + String.format("%-15s", "Livro");
	}
}
