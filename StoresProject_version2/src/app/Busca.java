package app;

import modelStore.*;
import java.util.List;
import java.util.Collections;

/* This class just has static methods responsible for search */
class Busca {
	
	//Print all stores
    public static void listarLojas(List<Loja> lojas) 
    {
    	//Before print the stores is used sort method from Collections Class
    	Collections.sort(lojas);
    	
    	for (Loja loja : lojas) {
    		System.out.println(loja);
    	}
    }
    
    // Print all products
    public static void listarProdutos(List<Produto> produtos) {

    	for (Produto prod : produtos) {
    		System.out.println(prod);
    	}
    }    
	
	// Print all items from List 
    public static void todosItens(List<Item> itens) {
    	
    	for (Item it : itens) {
    		System.out.println(it.toString());
    	}
    }
	
	//Search by product name and print all elements where the search == true
	public static void nomeProduto(List<Item> itens, String nomeBusca) {

		for (Item it : itens) {
			if (it.getProduto().getNome().toLowerCase().contains(nomeBusca.toLowerCase())) {
				System.out.println(it.toString());
			}
		}
	}
	
	//Search by store name
	public static void nomeLoja(List<Item> itens, String loja) {
		
		for (Item it : itens) {
			if (it.getLoja().getNome().toLowerCase().equals(loja.toLowerCase()) || 
				it.getLoja().getId().toLowerCase().equals(loja.toLowerCase())) {
				System.out.println(it.toString());
			}
		}
	}
	
	//Search by product type
	public static void tipoProduto(List<Item> itens, TipoProduto tp) {
		
		for (Item it : itens) {
				if (identificadorTipoProduto(it.getProduto()) == tp.getId()) {
					System.out.println(it.toString());
			}
		}
	}
	
	
	//This method identify the correctly product instance and returns respective product type
	private static int identificadorTipoProduto(Produto prod) {
		
		int identifica = (prod instanceof Eletronico) ? TipoProduto.ELETRONICO.getId() :
			             (prod instanceof ItemCasa) ? TipoProduto.ITEMCASA.getId() : 
			              TipoProduto.LIVRO.getId();

		return identifica;
	}
	
	//Search by product code
	public static void codigoProduto(List<Item> itens, int codigo) {
		
		//First has to sort the elements by where the priority is code then price
		Collections.sort(itens);
		
		for (Item it : itens) {
			if (it.getProduto().getCodigo() == codigo) {
				System.out.println(it.toString());
			}
		}
	}
}
