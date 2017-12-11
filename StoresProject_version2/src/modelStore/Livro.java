package modelStore;

public class Livro extends Produto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7771543743479052937L;
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
		return String.format("%-15s %-20s %s", "Livro", autor, nPaginas);
	}
}
