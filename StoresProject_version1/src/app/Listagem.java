package app;

import modelStore.*;

//Classe friendly que só serve para listagem dos elementos do vetor
class Listagem {
	
	/* Imprimindo as lojas extraidas */
    public static void listarLojas(Loja[] lojas) 
    {
    	for (int i = 0; i < lojas.length; i++) {
    		System.out.println(lojas[i].toString());
    	}
    }
    
    /* Imprimindo os produtos extraidos */
    public static void listarProdutos(Produto[] produtos) {
    	for (int i = 0; i < produtos.length; i++) {
    		System.out.println(produtos[i].toString());
    	}
    }    
    
    /* Imprimindo os itens da loja desejada */
    public static void listarItensLoja(String nomeLoja, Item[] itens) {
    	System.out.println(nomeLoja);
    	for (int i = 0; i < itens.length; i++) {
    		System.out.println(itens[i].toString());
    	}
    }
	
    /* Limpar a tela da linha de comando */
    public static void saltarLinhas(int nLinhas) {
    	for (int i = 0; i < nLinhas; i++) {
    		System.out.println();
    	}
    }
}
