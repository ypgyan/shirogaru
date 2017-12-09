package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import modelStore.*;

//This class has static methods is responsible for print 
class Listagem {
	
	// Função com o objetivo de pular linha
    public static void pularLinha(int x) 
    {
    	for (int i = 0; i < x; i++) {
    		System.out.println();
    	}
    }
	
    // Carrinho é listado de acordo com a ordem da compra.
	public static void listarItensCarrinho(List<ItemCarrinho> cart) 
	{
		if (cart.isEmpty()) {
			System.out.println("Carrinho vazio.");
		}else {
			double valorTotal = 0.0;
			for (int i = 0; i < cart.size(); i++) {
	    		System.out.println((i+1) + " - " + cart.get(i).toString());
	    		valorTotal += ((cart.get(i).getQuantidade()) * (cart.get(i).getItem().getPreco()));
	    	}
			System.out.println("\nValor total do carrinho: "+ "R$ " +valorTotal);
		}
    }
    
    public static void listarItens(List<Item> itens, int mode) 
    { 	
    	int unidades = 0;
    	
    	List<Loja> lojas = new LinkedList<>();
    	List<Produto> produtos = new LinkedList<>();
    	double valorTotal = 0.0;
    	
    	for (int i = 0; i < itens.size(); i++) {
    		System.out.println((i+1) + " - " + itens.get(i).toString());
    		unidades += itens.get(i).getQuantidade();
    		if (mode == 1) {
    			valorTotal += ((itens.get(i).getQuantidade()) * (itens.get(i).getPreco()));
			}else{
				// Recebe a loja e compara com um list para contar sem repetir a mesma loja.
	    		Loja loja = itens.get(i).getLoja();
	    		if(!(lojas.contains(loja))) {
	    			lojas.add(loja);
	    		}
			}
    		
    		// Recebe o produto e compara com um list para contar sem repetir o mesma produto.
    		Produto produto = itens.get(i).getProduto();
    		if (!(produtos.contains(produto))) {
    			produtos.add(produto);
			}
    	}
    	if (mode == 0) {
    		System.out.println("\nLojas: "+ lojas.size() +", Produtos: "+produtos.size()+", Total de Unidades: "+unidades);
		}else {
			System.out.println("Produtos: "+produtos.size()+", Total de Unidades: "+unidades+", Valor total: R$ "+valorTotal);
		}
    	
    }
    
    public static void listarLojas(List<Loja> lojas) 
    {
    	for (int i = 0; i < lojas.size(); i++) {
    		System.out.println((i+1) + " - " + lojas.get(i).toString());
    	}
    	System.out.println("Total de lojas: "+ (lojas.size()));
    }
    
    public static void listarProdutos(List<Produto> produtos) 
    {
    	for (Produto p : produtos) {
    		System.out.println(p);
    	}
    }
    
    public static void historicoCompras(List<Carrinho> compras) {
    	// Armazena todas os carrinhos de compras feitas até o para ordenar segundo os parâmetros pedidos
    	List<ItemCarrinho> historico = new ArrayList<>();
    	
    	// Passa todos os itens de compras para um arrayList    	
    	for(Carrinho carrinho : compras) {
    		historico.addAll(carrinho.getItensCarrinho());
    	}
    	
    	Collections.sort(historico, new Comparator<ItemCarrinho>() {
			@Override
			public int compare (ItemCarrinho itemc1 , ItemCarrinho itemc2) {
				// Comparação por ID da loja
				int comp = itemc1.getItem().getLoja().getId().compareTo(itemc2.getItem().getLoja().getId());
				if (comp == 0) {
					// Caso lojas iguais compara pelo tipo.
					comp = itemc1.getItem().getProduto().getTipoProd().compareTo(itemc2.getItem().getProduto().getTipoProd());
					if (comp == 0) {
						// Caso o tipo seja igual compara pelo nome do produto
						comp = itemc1.getItem().getProduto().getNome().compareTo(itemc2.getItem().getProduto().getNome());
					}
				}
				return comp;
			}
		});
    	
    	for (ItemCarrinho it : historico) {
    		System.out.println(it);
    	}
    }
}
