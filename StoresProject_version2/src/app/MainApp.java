package app;

import java.util.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import modelStore.*; 

public class MainApp {
	//Atributo static
	private static Scanner scanUser = new Scanner(System.in);
	
    public static void main(String[] args) throws ClassNotFoundException, IOException  
    {
    	
		// Carrega o sistema atraves de um arquivo .dat
		List sistema = carregarSistema();
		if (sistema.isEmpty()) {
			System.out.println("\nFinalizado");
		}else {
			// Inicializa as variaveis do sistema.
			List<Item> itens = (List<Item>) sistema.get(0);
			Map<String, Loja> lojas = (Map<String, Loja>) sistema.get(1);
			List<Carrinho> compras = (List<Carrinho>) sistema.get(2); 
			// Chama o menu da loja
			menu(lojas, itens, compras);
		}
		scanUser.close();
		
    }
    /* Carrega o sistema atraves de um arquivo .dat
     * retorna uma lista generica com os objetivos que estiverem no arquvio.
      */
    public static List carregarSistema() throws IOException, ClassNotFoundException {
    	
    	// cria uma lista generica para armazenar os objetos retornado do arquivo
    	List<Object> aux = new ArrayList<>();
    	
    	// Inicializa as listas do sistema.
    	List<Item> itens = new ArrayList<>();
    	Map<String,Loja> lojas = new HashMap<>();
    	List<Carrinho> compras = new ArrayList<>();
    	
	    try {	
    	// Busca o arquivo
		    FileInputStream fis = new FileInputStream("Database/sistema.dat");
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    
		    // Recupera os arquivos de acordo com a ordem que foram inseridos
		    itens = (List<Item>) ois.readObject();
		    lojas = (Map<String,Loja>) ois.readObject();
		    compras = (List<Carrinho>) ois.readObject();
		    ois.close();
		    
		    // Adiciona os objetos na lista para retorno.
		    aux.add(itens);
		    aux.add(lojas);
		    aux.add(compras);
		    
		    System.out.println("Carregado do .dat");
		    
		    return aux;
		    
    	}catch(FileNotFoundException e) {
    		try {
    			lojas = importarLojas("Database/lojas.txt");
        		
        		// Passando como referencia para pegar os produtos sem ler o arquivo duas vezes.
        		Map<Integer, Produto> produtos = new HashMap<>();
        		itens = extrairItens(lojas, produtos ,"Database/produtos.txt");
        		aux.add(itens);
    		    aux.add(lojas);
    		    aux.add(compras);
    		    
    		    System.out.println("Carregado dos .txt");
    		    return aux;
    		}catch (FileNotFoundException exception) {
				System.out.println("Nenhum arquivo contendo dados do sistema foi encontrado.");
				return aux;
			}
    		
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
    public static List<Item> extrairItens(Map<String, Loja> lojas, Map<Integer, Produto> produtos, String nomeArq) throws FileNotFoundException
    {
    	//String pathArq = ClassLoader.getSystemResource(nomeArq).getPath();
    	Scanner scanProdutos = new Scanner(new File(nomeArq));
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
    
    /* Menu que interage com o usu�rio final do sistema */
    public static void menu (Map<String, Loja> lojas, List<Item> itens, List<Carrinho> compras) throws IOException 
    {
    	//Menu para o usuario que chamar a classe aplicacao
        
    	// Inicialia��o das variaveis
        List<ItemCarrinho> cart = new ArrayList<>();
        List<Item> itensFiltrados = null;
        int opcao = 0;
        /* Pega a quantidade de compras antes de serem feitas modifica��es */
        Integer quantidadeComprasAntes = new Integer(compras.size()); 
        boolean chamadaCompra = false;
        String msgErro = "Entrada de dado inv�lida. Tente novamente:";
        String msgFiltroVazio = ""; // Serve para mostrar mensagem caso a busca n�o contenha nenhum elemento
		
        System.out.println("Bem-vindo a Shirugueri store pesad�o. XD\n\n");
        
        //Faz o menu enquanto o opcao continuar ser menor que zero.
        do{
        	// Variavel que define se lista o valor total ou lojas.
            int mode = 0;
        	// Caso o usuario ja tenho feito uma compra o carrinho ser� mostrado
        	if (cart.size() > 0) {
        		System.out.println("CARRINHO");
        		Listagem.listarItensCarrinho(cart);
        		Listagem.pularLinha(2);
			}
        	// Mostra as op��es disponiveis no sistema.
        	System.out.println("Escolha uma opc�o:");
			System.out.println("1 - Busca produtos por Loja\n2 - Busca de produtos por nome"
					+ "\n3 - Busca de produtos por Tipo\n4 - Busca de produtos por c�digo"
					+ "\n5 - Todos os itens do sistema\n6 - Seu carrinho"
					+ "\n7 - Historico de compras\n0 - Encerrar Sistema");
			
			opcao = controleEntradaDados(scanUser.nextLine(), 0, 100, msgErro, msgErro);
			
        	Listagem.pularLinha(2);
        	switch (opcao) {
        		case 0:
        			salvarSistema(itens, lojas, compras, quantidadeComprasAntes);
        			System.out.println("Agradecemos a prefer�ncia!!\n VOLTE SEMPRE");
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
        			msgFiltroVazio = "N�o h� produtos na loja selecionada";
        			chamadaCompra = true;
        			mode = 1;
        			break;
        		
        		case 2: //busca por nome
        			// Lista todos os produtos do sistema
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			String nomeProd = scanUser.nextLine();
        			itensFiltrados = Busca.nomeProduto(itens, nomeProd);
        			msgFiltroVazio = "N�o foi poss�vel encontrar nenhum produto contendo esse nome";
        			chamadaCompra = true;
        			break;
        		
        		case 3: //busca por tipo
        			System.out.println("Escolha uma opcao do tipo desejado:");
        			System.out.println("1 - Eletronico\n2 - Item de Casa\n3 - Livro");
        			opcao = controleEntradaDados(scanUser.nextLine(), 1, 3, msgErro, msgErro);
        			itensFiltrados = Busca.tipoProduto(itens, opcao);
        			msgFiltroVazio = "N�o h� produtos do tipo selecionado";
        			chamadaCompra = true;
        			break;
        			
        		case 4: //busca pelo codigo do produto
        			System.out.println("Seleciona o codigo do produto desejado:");
        			msgErro = "Tente novamente valor inserido n�o foi achado ou � invalido.";
        			int codigo = controleEntradaDados(scanUser.nextLine(), 0000, 9999, msgErro, msgErro);
        			itensFiltrados = Busca.codigoProduto(itens, codigo);
        			if (itensFiltrados.isEmpty())
        			msgFiltroVazio = "N�o h� produtos com o c�digo inserido";
        			chamadaCompra = true;
        			break;
        			
        		case 5: //busca por todos os produtos
        			System.out.println("Todos os produtos cadastrados do sistema:");
        			itensFiltrados = itens;
        			Collections.sort(itens); // Ordena pelo pre�o
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
        			
        		case 7:
        			if (compras.isEmpty()) {
						System.out.println("N�o h� historico de compras");
					}else {
						Listagem.historicoCompras(compras);
					}
        			chamadaCompra = false;
        			break;
        			
        		default:
        			chamadaCompra = false;
        			System.out.println("Nao existe essa busca");
        	}
        	
        	if ((chamadaCompra) && !itensFiltrados.isEmpty()) {
        		Listagem.listarItens(itensFiltrados,mode);
        		tcgBuy(itensFiltrados, cart);
        	}
        	else if (itensFiltrados != null && itensFiltrados.isEmpty()) {
        		System.out.println(msgFiltroVazio);
        	}
        	
        	System.out.println("############################################################");
        	Listagem.pularLinha(2);
        	
        } while (opcao != 0);
    }
    
    //M�todo que serve para o usu�rio do sistema comprar itens cadastrados no sistema
    private static void tcgBuy(List<Item> itens, List<ItemCarrinho> cart) 
    {
    	Listagem.pularLinha(2);
    	int decision = 1;
    	
    	while (decision == 1) {
    		// Pergunta para o usu�rio se ele realmente deseja comprar um dos pordutos (Adicionar no carrinho)
        	System.out.println("Voc� deseja:\n0 - Voltar para o menu principal\n1 - Comprar algum item");
        	
        	String msgErro = "Entrada de dado inv�lida. Tente novamente:";
        	
        	decision = controleEntradaDados(scanUser.nextLine(), 0, 1, msgErro, msgErro);
        	Listagem.pularLinha(2);
        	
        	if (decision == 0) //O usu�rio deseja voltar para o menu principal
        		return;
        	
        	int opcao = 1;
        	//Deseja comprar	
			while (opcao != 0) {
				System.out.println("Selecione o item que deseja comprar:");
				// Lista o resultado da pesquisa outra
				System.out.println("0 - Voltar");
				Listagem.listarItens(itens, 0);
				Listagem.pularLinha(1);
				
				//Escolher op��o de escolha
				opcao = controleEntradaDados(scanUser.nextLine(), 0, itens.size(), msgErro, msgErro);
				
				// atraves do que foi selecionado pelo usuario � colocado o item em seu carrinho
				if (opcao != 0) {
					Item itemSelecionado = itens.get(opcao-1);
					
				// Verifica se n�o tem mais o item no estoque
					if (itemSelecionado.getQuantidade() > 0) {
						
						String msgErro2 = "Quantidade inserida inv�lida ou indispon�vel no estoque. Insira novamente:";
						
						System.out.println("Informe a quantidade do produto: ");
						int qtd = controleEntradaDados(scanUser.nextLine(), 1, itemSelecionado.getQuantidade(), msgErro, msgErro2);
						Listagem.pularLinha(1);
						itemSelecionado.debitarEstoque(qtd);
						ItemCarrinho itemCart = new ItemCarrinho(itemSelecionado, qtd); 
						
						// Verifica primeiro se j� existe o item no carrinho e caso tenha s� precisa adicionar a quantidade escolhida
						if (cart.contains(itemCart)) {
							int pos = cart.indexOf(itemCart);
							cart.get(pos).adicionaQtdItens(qtd);
						}
						else { cart.add(itemCart); } // Adiciona o item que n�o existe no item carrinho ainda
						
						System.out.println("Produto adicionado ao Carrinho");
						
					}
					else //Caso o estoque do item esteja zerado
						System.out.println("N�o h� disponibilidade do item selecionado no estoque");
					
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
    		// Lista os itens do carrinho.
    		System.out.println("Seu carrinho:");
    		Listagem.listarItensCarrinho(cart);
    		Listagem.pularLinha(1);
    		System.out.println("O que gostaria de fazer:");
    		System.out.println("1 - Finalizar compras\n2 - Adicionar unidades dos produtos"
    							+ "\n3 - Remover unidades dos produto\n4 - Excluir produto"
    							+ "\n5 - Cancelar carrinho\n0 - Voltar ao menu principal.");
    		
    		String msgErro = "N�o existe essa op��o. Tente novamente.";
    		opcao1 = controleEntradaDados(scanUser.nextLine(), 0, 5, msgErro, msgErro);
    		
    		msgErro = "Entrada de dado inv�lida. Tente novamente:"; 
    		
    		switch (opcao1) {
    			case 0 : // Voltar ao menu principal 
    				opcao1 = 0; // Seta a op��o 1 para zero e volta para o menu principal
    				break;
    			
	    		case 1: // Finaliza compra.
	    			System.out.println("Deseja realmente finalizar as compras?\n1 - Sim\n2 - N�o");
	    			opcao2 = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
	    			
	    			//Cliente finaliza a compra e � criado 
	    			if (opcao2 == 1) {
	    				List<ItemCarrinho> itensCarrinho = new ArrayList<>();
	    				itensCarrinho.addAll(cart);
	    				Carrinho compraFechada = new Carrinho(itensCarrinho, new Date());
	    				compras.add(compraFechada); //Adiciona a compra na lista de compras para registro
	    				
	    				cart.clear(); // Limpa o cart que � o "carrinho" que o cliente e estava usando
	    				
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
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usu�rio decidiu n�o cancelar mais
		    			
		    			ItemCarrinho itemCartAdicionar = cart.get(opcao2 - 1); //Pega o itemCarrinho da op��o escolhida
		    			Item itemRetirarEstoque = itemCartAdicionar.getItem(); //Pega o item para debitar as unidades no estoque
		    			
		    			//Tem estoque ent�o pode adicionar mais itens no carrinho
		    			if (itemRetirarEstoque.getQuantidade() > 0) {
		    				System.out.println("Informa��es do item no estoque:\n" + itemRetirarEstoque + "\n");
		    				System.out.println("Escolha a quantidade que deseja adicionar:");
			    			String msgErro2 = "N�o se pode adicionar com a quantidade inserida";
			    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, itemRetirarEstoque.getQuantidade(), msgErro, msgErro2);
			    			
			    			// Basta adicionar no carrinho e remover do estoque de item
			    			itemCartAdicionar.adicionaQtdItens(quantidade);
			    			itemRetirarEstoque.debitarEstoque(quantidade);
		    			}
		    			else 
		    				System.out.println("N�o tem como adicionar mais unidades desse produto, pois est� indispon�vel no estoque");
	    			}
	    			break;
	    		
	    		case 3: // Remove unidades do item escolhido no carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja remover unidades:");
		    			System.out.println("0 - Voltar");
		    			Listagem.listarItensCarrinho(cart);
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usu�rio decidiu n�o cancelar mais
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); //Pega o itemCarrinho da op��o escolhida
		    			Item itemReporEstoque = itemCartRemover.getItem(); //Pega o item para colocar as unidades no estoque novamente
		    			
		    			System.out.println("Escolha a quantidade que deseja remover:");
		    			String msgErro2 = "N�o se pode remover com a quantidade inserida";
		    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, itemCartRemover.getQuantidade(), msgErro, msgErro2);
		    			
		    			// Remover do carrinho e adicionar novamente no estoque do item
		    			itemCartRemover.removeQtdItens(quantidade);
		    			itemReporEstoque.inserirEstoque(quantidade);
		    			
		    			// Deve checar se a unidade do item removido do carrinho est� zero. Logo ele deve sair do carrinho
		    			if (itemCartRemover.getQuantidade() == 0) {
		    				cart.remove(opcao2 - 1); // Remove o item do carrinho atrav�s do index
		    				
		    				// Ap�s remover o item verificar se existe algum item no carrinho do cliente
		    				if (cart.isEmpty()) {
		    					opcao1 = 0; // Para voltar ao menu principal
		    					System.out.println("Carrinho est� vazio. Redirecionando para o menu principal");
		    					break; // Sai da op��o que cont�m while = true
		    				}
		    			}
	    			}
	    			break;
	    			
	    		case 4: // Exclui produto do carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja remover totalmente:");
		    			System.out.println("0 - Voltar");
		    			Listagem.listarItensCarrinho(cart);
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usu�rio decidiu n�o cancelar mais
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); //Pega o itemCarrinho da op��o escolhida
		    			Item itemReporEstoque = itemCartRemover.getItem(); //Pega o item para colocar as unidades no estoque novamente
		    			// Pega a quantidade que a no carrinho.
		    			quantidade = itemCartRemover.getQuantidade();
		    			
		    			// Basta remover do carrinho e adicionar novamente no estoque do item
		    			itemCartRemover.removeQtdItens(quantidade);
		    			itemReporEstoque.inserirEstoque(quantidade);
		    			cart.remove(opcao2 - 1);
		    			System.out.println("Produto removido do carrinho");
		    			if (cart.isEmpty()) {
		    				System.out.println("Carrinho est� vazio. Redirecionando para o menu principal");
		    				opcao1 = 0;
		    				break; // Sai da op��o de exclui o produto do carrinho
		    			}
	    			}
	    			break;
	    			
	    		case 5: // Cancelar o carrinho de compras
	    			System.out.println("Deseja realmente cancelar suas compras?\n1 - Sim\n2 - N�o");
	    			opcao2 = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
	    			
	    			// Caso o usu�rio final deseje realmente cancelar � dado um clear no carrinho 
	    			if (opcao2 == 1) {
	    				// Remove os produtos do carrinho e devolve ao estoque.
	    				for(ItemCarrinho itc : cart) {
	    					itc.getItem().inserirEstoque(itc.getQuantidade());
	    				}
	    				cart.clear();
	    				System.out.println("Carrinho cancelado com sucesso. Redirecionando para o menu principal");
	    				opcao1 = 0; // Volta ao menu principal
	    			}
	    			break;
    		}
    	} while(opcao1 != 0);
    }
    
    //M�todo que serve para controlar entrada de dados realizada pelo usu�rio para que digite o correto
    private static int controleEntradaDados(String opcaoEntrada, int min, int max, String msg1, String msg2)  
    {
    	boolean condicao = true;
    	int opcao = 0;
    	
    	while (condicao) {
    		
    		//Verifica se vai dar o erro de convers�o do parseInt e se dar pede para digitar novamente
    		try 
    		{
    			opcao = Integer.parseInt(opcaoEntrada); //Pode entrar no catch ou n�o
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
    			//Caso n�o d� o erro de convers�o para int verificar se est� dentro das op��es desejadas
        		if (condicao == false) {
        			if (opcao < min || opcao > max) {
        				condicao = true; //Op��o errada ent�o volta a pedir ao usu�rio digitar novamente
        				System.out.println(msg2);
        				opcaoEntrada = scanUser.nextLine();
        			}
        		} 
    		}
    	}
    	
    	return opcao;
    }
    
    /* Serializa o essencial para o sistema, e joga dentro de um arquivo .dat */
    public static boolean salvarSistema(List<Item> itens, Map<String, Loja> lojas, List<Carrinho> compras, Integer qtdComprasPassada) throws IOException {
    	
    	Integer qtdComprasRecente = new Integer(compras.size()); // Pega a quantidade de compras recentes
    	
    	/* Caso a as quantidades dos itens sejam iguais � porque n�o foi realizada nenhuma compra, 
    	logo n�o � necess�rio escrever no arquivo bin�rio de sa�da exatamente o mesmo estado anterior */
    	if (qtdComprasPassada.equals(qtdComprasRecente))
    		return false; // N�o salvo, pois n�o teve nenhuma altera��o
    	
    	FileOutputStream fos = new FileOutputStream("Database/sistema.dat");
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    
	    oos.writeObject(itens);
	    oos.writeObject(lojas);
	    oos.writeObject(compras);
	    oos.close();
	    
	    return true; // Salvo com sucesso
    }
}
	
