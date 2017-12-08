package app;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import modelStore.*; 

public class MainApp {
	//Atributo static
	private static Scanner scanUser = new Scanner(System.in);
	
    public static void main(String[] args)  
    {
    	try {
    		Map<String, Loja> lojas = importarLojas("Database/lojas.txt");
    		List<Item> itens = extrairItens(lojas, "Database/produtos.txt");
    		
    		menu(lojas, itens);
    	    //Busca.todosItens(itens);
    	    //Busca.codigoProduto(itens, 1003);
    	}
    	catch(FileNotFoundException e) {
    		System.out.println("Arquivo nao encontrado");
    	}
    	finally {
    		scanUser.close();
    		System.out.println("\nFinalizado");
    	}
    }
    
    /* Import stores read from file */
    public static Map<String, Loja> importarLojas(String nomeArq) throws FileNotFoundException  
    {
    	//String pathArq = ClassLoader.getSystemResource(nomeArq).getPath();
    	Scanner scanLojas = new Scanner(new File(nomeArq));
    	
    	//Instance a map structure to save the stores, where the key is String type
    	Map<String, Loja> lojas = new HashMap<>();
        
        while (scanLojas.hasNextLine()) 
        {
        	String[] linha = scanLojas.nextLine().split(";");
        	
            String id = linha[0];
            String nome = linha[1];
            int avaliacao = Integer.parseInt(linha[2]);
            
            //Store instance
            lojas.put(id, new Loja(id, nome, avaliacao)); 
        }
    	
        scanLojas.close();
        
        return lojas;
    }


    /* Import the products from the file and put all of them on respective items */
    public static List<Item> extrairItens(Map<String, Loja> lojas, String nomeArq) throws FileNotFoundException
    {
    	//String pathArq = ClassLoader.getSystemResource(nomeArq).getPath();
    	Scanner scanProdutos = new Scanner(new File(nomeArq));
    	
    	Map<Integer, Produto> produtos = new HashMap<>();
    	List<Item> itens = new ArrayList<>();
    	int countLine = 0;
    	
    	//Loops according the numbers of products registrations
    	while (scanProdutos.hasNextLine()) {
    		countLine++;
    		String[] linha = scanProdutos.nextLine().split(";"); //Read the register/record 
    		int cod = Integer.parseInt(linha[1]); 
    		String nome = linha[3];
    		String tipoProduto = linha[2];
    		String Idloja = linha[0];
    		double preco = Double.parseDouble(linha[5].replace(',', '.'));
    		int quantidade = Integer.parseInt(linha[4]);
    		
    		//Checking the existence of the product in the Map Collection
    		if (!produtos.containsKey(cod)) {
    			Produto pNew = null;
    			
    			//Identify the product to statement the correct object product
    			switch (tipoProduto) {
    				case "Eletronico":
    					pNew = new Eletronico(nome, cod, linha[6], linha[8], Double.parseDouble(linha[7].replace(',', '.')));
    					break;
    				
    				case "ItemCasa":
    					pNew = new ItemCasa(nome, cod, linha[6], linha[7], linha[8]);
    					break;
    				
    				case "Livro":
    					pNew = new Livro(nome, cod, linha[6], Integer.parseInt(linha[7]));
    					break;
    				
    				default : //Doesn't exist this type of product
    					System.out.println("Nao existe esse item. Resolva o problema!");
    					System.out.println("Problema na linha " + (countLine+1));
    					System.exit(1);
    			}
    			
    			//Add the products and new item into your respective collection 
    			produtos.put(cod, pNew);
    			itens.add(new Item(lojas.get(Idloja), pNew, preco, quantidade));
    		}
    		//Else the product already exists then
    		else { 
    			itens.add(new Item(lojas.get(Idloja), produtos.get(cod), preco, quantidade));
    		}
    	}		
    	
    	scanProdutos.close();
    	return itens;
    }
    
    /* Menu que interage com o usuário final do sistema */
    public static void menu (Map<String, Loja> lojas, List<Item> itens) 
    {
    	//Menu para o usuario que chamar a classe aplicacao
        
    	// Inicialiação das variaveis
    	List<Carrinho> compras = new LinkedList<>();
        List<ItemCarrinho> cart = new ArrayList<>();
        List<Item> itensFiltrados = null;
        int opcao = 0;
        boolean chamadaCompra = false;
        String msgErro = "Entrada de dado inválida. Tente novamente:"; 
		
        //Faz o menu enquanto o opcao continuar ser menor que zero
        do{
        	// Caso o usuario ja tenho feito uma compra o carrinho sera mostrado
        	if (cart.size() > 0) {
        		System.out.println("CARRINHO");
        		Listagem.listarItensCarrinho(cart);
        		Listagem.pularLinha(2);
			}
        	// Mostra as opções disponiveis no sistema.
        	System.out.println("Escolha uma opcao da busca desejada dos itens:");
			System.out.println("1 - Por Loja\n2 - Por Nome do produto\n3 - Por Tipo\n4 - Código do produto"
					+ "\n5 - Todos os itens do sistema\n6 - Alterar carrinho (Em construção)\n0 - Encerrar Sistema");
			
			opcao = controleEntradaDados(scanUser.nextLine(), 0, 100, msgErro, msgErro);
			
        	Listagem.pularLinha(2);
        	switch (opcao) {
        		case 0:
        			System.out.println("Agradecemos a preferência!!\n VOLTE SEMPRE");
        			chamadaCompra = false;
        			
        			break;
        			
        		case 1: //busca por loja
        			List<Loja> listLojas = Busca.todasLojas(lojas);
        			System.out.println("0 - Voltar");
        			Listagem.listarLojas(listLojas);
        			System.out.println("\nSelecione a loja desejada: ");
        			int opcao2 = controleEntradaDados(scanUser.nextLine(), 0, listLojas.size(), msgErro, msgErro); // Captura a opcao desejada da loja
        			if (opcao2 == 0) { break; } 
        			itensFiltrados = Busca.porLoja(itens, listLojas.get(opcao2 - 1));
        			chamadaCompra = true;
        			break;
        		
        		case 2: //busca por nome
        			// Lista todos os produtos do sistema
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			String nomeProd = scanUser.nextLine();
        			itensFiltrados = Busca.nomeProduto(itens, nomeProd);
        			chamadaCompra = true;
        			break;
        		
        		case 3: //busca por tipo
        			System.out.println("Escolha uma opcao do tipo desejado:");
        			System.out.println("1 - Eletronico\n2 - Item de Casa\n3 - Livro");
        			opcao = Integer.parseInt(scanUser.nextLine());
        			itensFiltrados = Busca.tipoProduto(itens, opcao);
        			chamadaCompra = true;
        			break;
        			
        		case 4: //busca pelo codigo do produto
        			System.out.println("Seleciona o codigo do produto desejado:");
        			int codigo = Integer.parseInt(scanUser.nextLine());
        			itensFiltrados = Busca.codigoProduto(itens, codigo);
        			chamadaCompra = true;
        			break;
        			
        		case 5: //busca por todos os produtos
        			System.out.println("Todos os produtos cadastrados do sistesma sao:");
        			itensFiltrados = itens;
        			Collections.sort(itens);
        			chamadaCompra = true;
        			break;
        			
        		case 6: // O usario podera encerrar ou remover algo do carrinho.
        			if (cart.isEmpty()) {
						System.out.println("Carrinho Vazio");
					}else {
	        			menuCart(itens, cart, compras);
					}
        			chamadaCompra = false;
        			break;
        			
        		default:
        			chamadaCompra = false;
        			System.out.println("Nao existe essa busca");
        	}
        	
        	if ((chamadaCompra) && !itensFiltrados.isEmpty()) {
        		Listagem.listarItens(itensFiltrados);
        		tcgBuy(itensFiltrados, cart);
        	}
        	System.out.println("############################################################");
        	Listagem.pularLinha(2);
        	
        } while (opcao != 0);
    }
    
    //Método que serve para o usuário do sistema comprar itens cadastrados no sistema
    private static void tcgBuy(List<Item> itens, List<ItemCarrinho> cart) 
    {
    	Listagem.pularLinha(2);
    	int decision = 1;
    	
    	while (decision == 1) {
    		// Pergunta para o usuário se ele realmente deseja comprar um dos pordutos (Adicionar no carrinho)
        	System.out.println("Você deseja: \n1 - Comprar algum item"
    							+ "\n2 - Voltar para o menu principal");
        	
        	String msgErro = "Entrada de dado inválida. Tente novamente:";
        	
        	decision = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
        	Listagem.pularLinha(2);
        	
        	if (decision == 2) //O usuário deseja voltar para o menu principal
        		return;
        	
        	int opcao = 1;
        	//Deseja comprar	
			while (opcao != 0) {
				System.out.println("Selecione o item que deseja comprar:");
				// Lista o resultado da pesquisa outra
				System.out.println("0 - Voltar");
				Listagem.listarItens(itens);
				Listagem.pularLinha(1);
				
				//Escolher opção de escolha
				opcao = controleEntradaDados(scanUser.nextLine(), 0, itens.size(), msgErro, msgErro);
				
				// atraves do que foi selecionado pelo usuario é colocado o item em seu carrinho
				if (opcao != 0) {
					Item itemSelecionado = itens.get(opcao-1);
					
				// Verifica se não tem mais o item no estoque
					if (itemSelecionado.getQuantidade() > 0) {
						
						String msgErro2 = "Quantidade inserida inválida ou indisponível no estoque. Insira novamente:";
						
						System.out.println("Informe a quantidade do produto: ");
						int qtd = controleEntradaDados(scanUser.nextLine(), 1, itemSelecionado.getQuantidade(), msgErro, msgErro2);
						itemSelecionado.debitarEstoque(qtd);
						ItemCarrinho itemCart = new ItemCarrinho(itemSelecionado, qtd); 
						
						// Verifica primeiro se já existe o item no carrinho e caso tenha só precisa adicionar a quantidade escolhida
						if (cart.contains(itemCart)) {
							int pos = cart.indexOf(itemCart);
							cart.get(pos).adicionaQtdItens(qtd);
						}
						else { cart.add(itemCart); } // Adiciona o item que não existe no item carrinho ainda
						
						System.out.println("Produto adicionado ao Carrinho");
						
					}
					else //Caso o estoque do item esteja zerado
						System.out.println("Não há disponibilidade do item selecionado no estoque");
					
					Listagem.pularLinha(1);
				}
			}
		}
    }

    // Menu para interagir com o carrinho.
    private static void menuCart(List<Item> itens, List<ItemCarrinho> cart, List<Carrinho> compras) 
    {
    	int opcao1, opcao2, quantidade;
    	do {	
    		System.out.println("O que gostaria de fazer:");
    		// Lista os itens do carrinho.
    		Listagem.listarItensCarrinho(cart);
    		Listagem.pularLinha(1);
    		System.out.println("1 - Finalizar compras\n2 - Adicionar determinado produto"
    							+ "\n3 - Remover determinado produto\n4 - Cancelar carrinho\n0 - Voltar ao menu principal.");
    		
    		String msgErro = "Não existe essa opção. Tente novamente.";
    		opcao1 = controleEntradaDados(scanUser.nextLine(), 0, 4, msgErro, msgErro);
    		
    		msgErro = "Entrada de dado inválida. Tente novamente:"; 
    		
    		switch (opcao1) {
    			case 0 : // Voltar ao menu principal 
    				opcao1 = 0; // Seta a opção 1 para zero e volta para o menu principal
    				break;
    			
	    		case 1: // Finaliza compra.
	    			System.out.println("Deseja realmente finalizar as compras?\n1 - Sim\n2 - Não");
	    			opcao2 = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
	    			
	    			//Cliente finaliza a compra e é criado 
	    			if (opcao2 == 1) { 
	    				Carrinho compraFechada = new Carrinho(cart, null); //Colocar a data no lugar do null
	    				compras.add(compraFechada); //Adiciona a compra na lista de compras para registro
	    				cart.clear(); // Limpa o cart que é o "carrinho" que o cliente estava usando
	    				System.out.println("Arigatou Gozaimasu pela compra");
	    				opcao1 = 0; //Volta para menu principal
	    			}
	    			break;
	    		
	    		case 2: // Adiciona unidades do item escolhido no carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja adicionar unidades:");
		    			System.out.println("0 - Voltar");
		    			Listagem.listarItensCarrinho(cart);
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usuário decidiu não cancelar mais
		    			
		    			ItemCarrinho itemCartAdicionar = cart.get(opcao2 - 1); //Pega o itemCarrinho da opção escolhida
		    			Item itemRetirarEstoque = itemCartAdicionar.getItem(); //Pega o item para debitar as unidades no estoque
		    			
		    			//Tem estoque então pode adicionar mais itens no carrinho
		    			if (itemRetirarEstoque.getQuantidade() > 0) {
		    				System.out.println("Informações do item no estoque:\n" + itemRetirarEstoque + "\n");
		    				System.out.println("Escolha a quantidade que deseja adicionar:");
			    			String msgErro2 = "Não se pode adicionar com a quantidade inserida";
			    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, itemRetirarEstoque.getQuantidade(), msgErro, msgErro2);
			    			
			    			// Basta adicionar no carrinho e remover do estoque de item
			    			itemCartAdicionar.adicionaQtdItens(quantidade);
			    			itemRetirarEstoque.debitarEstoque(quantidade);
		    			}
		    			else 
		    				System.out.println("Não tem como adicionar mais unidades desse produto, pois está indisponível no estoque");
	    			}
	    			break;
	    		
	    		case 3: // Remove unidades do item escolhido no carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja remover unidades:");
		    			System.out.println("0 - Voltar");
		    			Listagem.listarItensCarrinho(cart);
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usuário decidiu não cancelar mais
		    			
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); //Pega o itemCarrinho da opção escolhida
		    			Item itemReporEstoque = itemCartRemover.getItem(); //Pega o item para colocar as unidades no estoque novamente
		    			
		    			System.out.println("Escolha a quantidade que deseja remover:");
		    			String msgErro2 = "Não se pode remover com a quantidade inserida";
		    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, itemCartRemover.getQuantidade(), msgErro, msgErro2);
		    			
		    			// Basta remover do carrinho e adicionar novamente no estoque do item
		    			itemCartRemover.removeQtdItens(quantidade);
		    			itemReporEstoque.inserirEstoque(quantidade);
		    			
		    			// Deve checar se a unidade do item removido do carrinho está zero. Logo ele deve sair do carrinho
		    			if (itemCartRemover.getQuantidade() == 0)
		    				cart.remove(opcao2 - 1); // Remove a referência do carrinho através do index
	    			}
	    			break;
	    			
	    		case 4: // Cancelar o carrinho de compras
	    			System.out.println("Deseja realmente cancelar suas compras?\n1 - Sim\n2 - Não");
	    			opcao2 = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
	    			
	    			// Caso o usuário final deseje realmente cancelar é dado um clear no carrinho 
	    			if (opcao2 == 1) { 
	    				cart.clear();
	    				System.out.println("Carrinho cancelado com sucesso.");
	    				opcao1 = 0; // Volta ao menu principal
	    			}
	    			break;
    		}
    	} while(opcao1 != 0);
    }
    
    

    
    //Método que serve para controlar entrada de dados realizada pelo usuário para que digite o correto
    private static int controleEntradaDados(String opcaoEntrada, int min, int max, String msg1, String msg2)  
    {
    	boolean condicao = true;
    	int opcao = 0;
    	
    	while (condicao) {
    		
    		//Verifica se vai dar o erro de conversão do parseInt e se dar pede para digitar novamente
    		try 
    		{
    			opcao = Integer.parseInt(opcaoEntrada); //Pode entrar no catch ou não
    			condicao = false;
    		} 
    		catch (NumberFormatException e) 
    		{
    			System.out.println(msg1);
    			condicao = true;
    			opcaoEntrada = scanUser.nextLine();
    		}
    		finally 
    		{
    			//Caso não dê o erro de conversão para int verificar se está dentro das opções desejadas
        		if (condicao == false) {
        			if (opcao < min || opcao > max) {
        				condicao = true; //Opção errada então volta a pedir ao usuário digitar novamente
        				System.out.println(msg2);
        				opcaoEntrada = scanUser.nextLine();
        			}
        		} 
    		}
    	}
    	
    	return opcao;
    }
    
    // Serializar tudo que foi feito
    public static void serializacaoInformacoes() {
    	//TODO - fazer aqui
    }
}
	
