package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import modelStore.*;

//This class has static methods is responsible for print 
class Listagem {
	
	// Fun��o com o objetivo de pular linha
    public static void pularLinha(int x) 
    {
    	for (int i = 0; i < x; i++) {
    		System.out.println();
    	}
    }
	
    // Carrinho � listado de acordo com a ordem da compra.
	public static void listarItensCarrinho(List<ItemCarrinho> cart) 
	{
		if (cart.isEmpty()) {
			System.out.println("Carrinho vazio.");
		}else {
			
			List<Loja> lojas = new LinkedList<>();
	    	List<Produto> produtos = new LinkedList<>();
	    	int unidades = 0;
			double valorTotal = 0.0;
			for (int i = 0; i < cart.size(); i++) {
	    		System.out.println(String.format("%-3s %-3s", (i+1), "-") + cart.get(i).toString());
	    		valorTotal += ((cart.get(i).getQuantidade()) * (cart.get(i).getItem().getPreco()));
	    		unidades += cart.get(i).getQuantidade();
	    		
	    		// Recebe a loja e compara com um list para contar sem repetir a mesma loja.
	    		Loja loja = cart.get(i).getItem().getLoja();
	    		if(!(lojas.contains(loja))) {
	    			lojas.add(loja);
	    		}
	    		// Recebe o produto e compara com um list para contar sem repetir o mesma produto.
	    		Produto produto = cart.get(i).getItem().getProduto();
	    		if (!(produtos.contains(produto))) {
	    			produtos.add(produto);
				}
	    	}
			System.out.println("\nLojas: "+ lojas.size() +", Produtos: "+produtos.size()+", Total de Unidades: "+unidades+", Valor total: R$ "+valorTotal);
		}
    }
    
    public static void listarItens(List<Item> itens, int mode) 
    {
    	/* Obs: mode define se a listagem ser� quando a op��o escolhida de busca escolhida pelo usu�rio foi por loja
    	Caso seja mode == 1, caso contr�rio ser� mode == 0 */
    	
    	int unidades = 0;
    	
    	List<Loja> lojas = new LinkedList<>();
    	List<Produto> produtos = new LinkedList<>();
    	double valorTotal = 0.0;
    	
    	for (int i = 0; i < itens.size(); i++) {
    		System.out.println(String.format("%-3s %-3s", (i+1), "-") + itens.get(i).toString()+ " "+itens.get(i).getProduto().toString());
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
			System.out.println("\nProdutos: "+produtos.size()+", Total de Unidades: "+unidades+", Valor total: R$ "+valorTotal);
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
    	for (int i = 0; i < produtos.size(); i++) {
    		int codProd = produtos.get(i).getCodigo();
    		String nome = produtos.get(i).getNome();
    		String tipo = produtos.get(i).getTipoProd();
    		
    		System.out.println(String.format("%-2d %-1s %-40s %-10s", codProd,"-" , nome, tipo ));
    	}
    	System.out.println("Total de produtos: "+ (produtos.size()));
    }
    
    public static void historicoComprasGeral(List<Carrinho> compras) {
    	// Armazena todas os carrinhos de compras feitas at� o para ordenar segundo os par�metros pedidos
    	List<ItemCarrinho> historico = new ArrayList<>();
    	
    	// Passa todos os itens de compras para um arrayList    	
    	for(Carrinho carrinho : compras) {
    		historico.addAll(carrinho.getItensCarrinho());
    	}
    	
    	Collections.sort(historico, new Comparator<ItemCarrinho>() {
			@Override
			public int compare (ItemCarrinho itemc1 , ItemCarrinho itemc2) {
				// Compara��o por ID da loja
				int comparacao = itemc1.getItem().getLoja().getId().compareTo(itemc2.getItem().getLoja().getId());
				if (comparacao == 0) {
					// Caso lojas iguais compara pelo tipo.
					comparacao = itemc1.getItem().getProduto().getTipoProd().compareTo(itemc2.getItem().getProduto().getTipoProd());
					if (comparacao == 0) {
						// Caso o tipo seja igual compara pelo nome do produto
						comparacao = itemc1.getItem().getProduto().getNome().compareTo(itemc2.getItem().getProduto().getNome());
					}
				}
				return comparacao;
			}
		});
    	
    	listarItensCarrinho(historico);
    }
    
    public static void historicoComprasSequencial(List<Carrinho> compras) {
    	for (Carrinho compra : compras) {
    		System.out.println(compra);
    		Listagem.pularLinha(2);
    	}
    }
}
