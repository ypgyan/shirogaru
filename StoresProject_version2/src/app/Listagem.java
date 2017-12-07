package app;

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
	
	public static void listarItensCarrinho(List<ItemCarrinho> cart) 
	{
		for (ItemCarrinho it : cart) {
    		System.out.println(it.toString());
    	}
    }
    
    public static void listarItens(List<Item> itens) 
    {
    	for (int i = 0; i < itens.size(); i++) {
    		System.out.println((i+1) + " - " + itens.get(i).toString());
    	}
    }
    
    public static void listarLojas(List<Loja> lojas) 
    {
    	for (int i = 0; i < lojas.size(); i++) {
    		System.out.println((i+1) + " - " + lojas.get(i).toString());
    	}
    }
    
    public static void listarProdutos(List<Produto> produtos) 
    {
    	for (Produto p : produtos) {
    		System.out.println(p);
    	}
    }
}
