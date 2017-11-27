package app;

enum TipoProduto {
	ELETRONICO("Eletronico", 1),
	ITEMCASA("Item Casa", 2),
	LIVRO("Livro", 3);
	
	private final String tipo;
	private final int id;
	
	private TipoProduto(String tipo, int id) {
		this.tipo = tipo;
		this.id = id;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public int getId() {
		return this.id;
	}
}
