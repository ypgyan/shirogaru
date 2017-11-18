package modelStore;

public class Loja {
    /* Atributos */
    private String id;
    private String nome;
    private int avaliacao;
    private Item[] itens;
    private int posAtualItem = 0; //variável somente usada para identificar qual a última posição do vetor estático que pode ser armazenado 
    
    /* Construtor */
    //nItens identifica o número máximo de itens para a loja lidos do arquivo
    public Loja(String id, String nome, int avaliacao, int nItens) 
    {
        this.id = id;
        this.nome = nome;
        this.avaliacao = avaliacao;
        this.itens = new Item[nItens];
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
    
    //Retorna a referência de todos os itens da loja
    public Item[] getItens() 
    {
    	return this.itens;
    }
    
    
    public Item getItemPos(int pos) {
    	if (pos >= 0 && pos < this.itens.length) {
    		return this.itens[pos];
    	}
    	
    	return null; //Nao existe o item na posicao indicada
    }
    
    
    @Override
    public String toString() 
    {
    	return this.nome + "(" + this.id + ")" + " com avaliacao: " + this.avaliacao + " estrela(s).";
    }
    
    
    public boolean registraItem(Produto prod, double preco) {
    	this.itens[posAtualItem] = new Item(prod, preco);
    	
    	//Armazenou corretamente
    	if (itens[posAtualItem++] != null)
    		return true;
    	
    	return false;
    }
    
    
    //Faz a busca pelo nome passado como parametro na loja e retorna a posicao do item caso encontre
    public int buscaNome(String nomeProd) {
    	int pos = 0;
    	for (Item i : this.itens) {

    		if (i.getProduto().getNome().toLowerCase().equals(nomeProd.toLowerCase()))
    			return pos;
    		pos++;
    	}
    	return -1; //Nao achou o produto com o determinado nome
    } 
}
