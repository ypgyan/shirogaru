package modelStore;

public class Loja implements Comparable<Loja> {
    /* Atributos */
    private String id;
    private String nome;
    private int avaliacao; 
    
    /* Construtor */
    public Loja(String id, String nome, int avaliacao) 
    {
        this.id = id;
        this.nome = nome;
        this.avaliacao = avaliacao;
    }
    
    /* Metodos */
    public void setNome(String nomeLoja) 
    {
        this.nome = nomeLoja;
    }
    
    public String getNome() 
    {
        return this.nome;
    }
    
    public void setAvaliacao(int avaliacao) 
    {
        this.avaliacao = avaliacao;
    }
    
    public int getAvaliacao() 
    {
        return this.avaliacao;
    }
    
    public void setId(String id) 
    {
        this.id = id;
    }
    
    public String getId() 
    {
        return this.id;
    }
    
    @Override
    public String toString() 
    {
    	return String.format("%-20s %-10s", nome, id) + " " + String.format("%d estrelas", avaliacao); 
    }
    
    @Override
    public boolean equals(Object obj) 
    {
    	if ((obj == null) || !(obj instanceof Loja))
    		return false; 
    	
    	if (this.getId().equals( ((Loja)obj).getId() ))
    		return true;
    	
    	return false;
    }

	@Override
	public int compareTo(Loja l) 
	{
		int valor = ((this.avaliacao - l.avaliacao) * -1);
		
		if (valor == 0) {
			valor = this.nome.compareToIgnoreCase(l.nome);
		}  

		return valor;
	}
    
}
