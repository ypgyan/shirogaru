package app;

enum IdentificadorLoja {
	AMERICANAS("Americanas.com", "AMER", 1),
	SARAIVA("Livraria Saraiva", "SARA", 2),
	SUBMARINO("Submarino.com", "SUBM", 3),
	RICELETRO("Ricardo Eletro", "RICE", 4),
	MAGANIZE("Magazine Luiza", "MAGA", 5),
	MEGALOJA("Megaloja Palace", "MEGA", 6);
	
	private final String nome;
	private final String id;
	private final int serial;
	
	private IdentificadorLoja(String nome, String id, int serial) {
		this.nome = nome;
		this.id = id;
		this.serial = serial;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getSerial() {
		return this.serial;
	}
}