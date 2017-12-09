package app;

import modelStore.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* This class is responsible for searching */
class Busca {
	
	//return a list all stores ordered by rating stars 
    public static List<Loja> todasLojas(Map<String, Loja> lojas) 
    {
    	List<Loja> listaAux = new ArrayList<>(lojas.values());
    	
    	//stores is used sort method from Collections Class
    	Collections.sort(listaAux);
    	
    	return listaAux;
    }
    
    //return a list all products ordered by code
    public static List<Produto> todosProdutos(Map<Integer, Produto> produtos) 
    {	
    	List<Produto> listaAux = new ArrayList<>(produtos.values());
    	
    	//stores is used sort method from Collections Class
    	Collections.sort(listaAux);
    	
    	return listaAux;
    }
	
	//Search by product name and print all elements where the search == true
	public static List<Item> nomeProduto(List<Item> itens, String nomeBusca) 
	{	
		List<Item> nomeProdutoFiltrado = new ArrayList<>();
		 
		for (Item it : itens) {
			if (it.getProduto().getNome().toLowerCase().contains(nomeBusca.toLowerCase())) {
				nomeProdutoFiltrado.add(it);
			}
		}
		
		return nomeProdutoFiltrado;
	}
	
	//Search by store name
	public static List<Item> porLoja(List<Item> itens, Loja loja) 
	{
		List<Item> nomeLojaFiltrado = new ArrayList<>();
		
		for (Item it : itens) {
			if (it.getLoja().equals(loja)) {
				nomeLojaFiltrado.add(it);
			}
		}
		
		return nomeLojaFiltrado;
	}
	
	//Search by product type
	public static List<Item> tipoProduto(List<Item> itens, int opcao) 
	{
		List<Item> tipoProdutoFiltrado = new ArrayList<>();
		
		for (Item it : itens) {
				if (identificadorTipoProduto(it.getProduto()) == opcao) {
					tipoProdutoFiltrado.add(it);
			}
		}
		
		return tipoProdutoFiltrado;
	}
	
	
	//This method identify the correctly product instance and returns respective product type
	private static int identificadorTipoProduto(Produto prod) 
	{	
		int identifica = (prod instanceof Eletronico) ? TipoProduto.ELETRONICO.getId() :
			             (prod instanceof ItemCasa) ? TipoProduto.ITEMCASA.getId() : 
			              TipoProduto.LIVRO.getId();

		return identifica;
	}
	
	//Search by product code
	public static List<Item> codigoProduto(List<Item> itens, int codigo) 
	{	
		List<Item> codigoProdutoFiltrado = new ArrayList<>();
		
		//First has to sort the elements by where the priority is code then price
		Collections.sort(itens,  new Comparator<Item>() {
			@Override
			public int compare (Item item1 , Item item2) {
				
				int comp = item1.getProduto().compareTo(item2.getProduto());
				
				if (comp == 0) {
					comp = (item1.compareTo(item2));
				}
				return comp;
			}
		});
		
		for (Item it : itens) {
			if (it.getProduto().getCodigo() == codigo) {
				codigoProdutoFiltrado.add(it);
			}
		}
		
		return codigoProdutoFiltrado;
	}
}
