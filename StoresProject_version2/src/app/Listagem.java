package app;

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
    
    public static void listarItens(List<Item> itens) 
    { 	
    	int unidades = 0;
    	
    	List<Loja> lojas = new LinkedList<>();
    	List<Produto> produtos = new LinkedList<>();
    	
    	for (int i = 0; i < itens.size(); i++) {
    		System.out.println((i+1) + " - " + itens.get(i).toString());
    		unidades += itens.get(i).getQuantidade();
    		
    		// Recebe a loja e compara com um list para contar sem repetir a mesma loja.
    		Loja loja = itens.get(i).getLoja();
    		if(!(lojas.contains(loja))) {
    			lojas.add(loja);
    		}
    		// Recebe o produto e compara com um list para contar sem repetir o mesma produto.
    		Produto produto = itens.get(i).getProduto();
    		if (!(produtos.contains(produto))) {
    			produtos.add(produto);
			}
    	}
    	System.out.println("\nLojas: "+ lojas.size() +", Produtos: "+produtos.size()+", Total de Unidades: "+unidades);
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
}
