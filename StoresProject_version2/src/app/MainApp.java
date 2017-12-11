package app;

import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import modelStore.*; 

public class MainApp {
	//Atributo static da classe para pegar leitura do usu�rio
	private static Scanner scanUser = new Scanner(System.in);
	
    public static void main(String[] args)   
    {
    	List<Item> itens = new ArrayList<>();
    	Map<String, Loja> lojas = new HashMap<>();
    	Map<Integer, Produto> produtos = new HashMap<>();
    	List<Carrinho> compras = new ArrayList<>();
    	List<ItemCarrinho> clienteCart = new ArrayList<>();
    	String nomeArq = "Database/sistema.dat";
    	
    	if (carregarSistema(itens, lojas, produtos, compras, clienteCart, nomeArq)) {
    		menu(lojas,produtos,itens,compras,clienteCart);
    	}
    	else {
    		System.out.println("Error system");
    	}
    	
    }
    
    /* Abrir o sistema carregando as informa��es pelo arquivo.dat(arquivo bin�rio) */
    @SuppressWarnings("unchecked") // Suprimir os avisos do casting no read object
	public static boolean carregarSistema (List<Item> itens, Map<String, Loja> lojas, Map<Integer, Produto> produtos, List<Carrinho> compras, List<ItemCarrinho> clienteCart, String nomeArq) {
    	
    	boolean carregadoComSucesso = false;
    	
	    try {	
		    InputStream fis = new FileInputStream(nomeArq);
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    
		    // Recupera os arquivos de acordo com a ordem que foram inseridos no arquivo.dat
	    	itens.addAll((List<Item>) ois.readObject());
		    lojas.putAll((Map<String,Loja>) ois.readObject());
		    produtos.putAll((Map<Integer, Produto>) ois.readObject());
		    compras.addAll((List<Carrinho>) ois.readObject());
		    clienteCart.addAll((List<ItemCarrinho>) ois.readObject());
		    
		    ois.close(); // Fecha o ObjectInputStream
		    carregadoComSucesso = true;
	    }
	    catch (FileNotFoundException e) {
	    	carregadoComSucesso = importarLojas(lojas, "Database/lojas.txt", "UTF-8");
	    	carregadoComSucesso = extrairItens(itens, lojas, produtos, "Database/produtos2.txt", "UTF-8");
	    }
	    catch (ClassCastException | ClassNotFoundException | IOException e) {
	    	System.out.println("Houve problema na leitura do arquivo");
	    }
		    
    	return carregadoComSucesso;
    }
    
    /* Importa as lojas do arquivo.txt */
    public static boolean importarLojas(Map<String, Loja> lojas, String nomeArq, String encoding)  
    {
    	boolean importadoComSucesso = false;
    	
    	try {
    		Scanner scanLojas = new Scanner(new File(nomeArq), encoding);
            
    		// Para seguran�a faz uma checagem para identificar se o mapa de lojas j� foi instanciado
    		if (lojas == null)
    			lojas = new HashMap<>();
    		
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
            importadoComSucesso = true; // Foi importando com sucesso
    	} 
    	catch (FileNotFoundException e) {
    		System.out.println("Erro na abertura do arquivo. Arquivo n�o encontrado");
    	}
    	catch (IllegalArgumentException e) {
    		System.out.println("Erro de codifica��o do arquivo. N�o foi aceito o encondig passado");
    	}
    	
        return importadoComSucesso;
    }

    /* Importa os produtos do arquivo e coloca em seus respectivos itens */
    public static boolean extrairItens(List<Item> itens, Map<String, Loja> lojas, Map<Integer, Produto> produtos, String nomeArq, String encoding)
    {
    	boolean extraidoComSucesso = false;
    	Scanner scanProdutos = null;
    	
    	try {
    		scanProdutos = new Scanner(new File(nomeArq), "UTF-8");
    		extraidoComSucesso = true;
    	}
    	catch (FileNotFoundException e) {
    		System.out.println("Erro na abertura do arquivo. Arquivo n�o encontrado");
    	}
    	catch (IllegalArgumentException e) {
    		System.out.println("Erro de codifica��o do arquivo. N�o foi aceito o encondig passado");
    	}
    	
    	// Erro na abertura, logo volta false porque n�o abriu
    	if (extraidoComSucesso == false) 
    		return extraidoComSucesso;
    	
    	// Verifica antes por seguran�a se o list de itens j� est� instanciado
    	if (itens == null)
    		itens = new ArrayList<>();
    	
    	// Verifica tamb�m se os produtos passados j� est� instanciado
    	if (produtos == null)
    		produtos = new HashMap<>();
    	
    	// Serve para quando acha um produto que n�o pode ser instanciado como produto, pois n�o existe uma subclasse para ele
    	int countLine = 0; 
    	
    	//Loops according the numbers of products registrations
    	while (scanProdutos.hasNextLine()) {
    		countLine++;
    		String[] linha = scanProdutos.nextLine().split(";"); //Read the register/record 
    		int cod = Integer.parseInt(linha[1]); 
    		String nome = linha[3];
    		String tipoProduto = linha[2];
    		String Idloja = linha[0];
    		double preco = conversaoPreco(linha[5]);
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
    			Item item = new Item(lojas.get(Idloja), produtos.get(cod), preco, quantidade);
    			// Checa se o item j� existe dentro da lista para n�o haver erros de duplica��o de itens iguais
    			if (!itens.contains(item))
    				itens.add(item); // Item novo
    			else {
    				System.out.println("Item j� existente na lista de registros. Para evitar problemas de redund�ncia "
    									+ "foi pegado a primeira ocorr�ncia do item no arquivo lido");
    			}
    		}
    	}
    	scanProdutos.close();
    	
    	return extraidoComSucesso;
    }
    
    /* M�todo que serve para converter a string de forma que esteja no padr�o para convers�o para double */
    private static double conversaoPreco(String preco) {
		double precoConvertido = 0;
		preco = preco.replace(",", ".");
		
		int contPontos = 0;
		// Pega a quantidade de pontos que aparece no string pre�o
		for (int i = 0; i < preco.length(); i++) {
			if (preco.charAt(i) == '.') {
				contPontos++; // Adicionado a posi��o do ponto
			}
		}
		
		/* Checa a quantidade de pontos que aparece e retorna e caso o pre�o string tenha mais de dois pontos 
		 * far� a convers�o correta para pegar o valor no padr�o brasileiro */
		if (contPontos > 1) {
			String precoCorreto = "";
			for (int i = 0; i < preco.length(); i++) {
				if (preco.charAt(i) != '.')
					precoCorreto += preco.charAt(i);
				else if (contPontos-- == 1) 
					break; // D� o break, porque esse �ltimo ponto � o que separa a parte decimal da parte inteira
				}
			precoConvertido = Double.parseDouble(precoCorreto);
		}
		else 
			precoConvertido = Double.parseDouble(preco);
		
		return precoConvertido;
    }
    
    /* Menu que interage com o usu�rio final do sistema */
    public static void menu (Map<String, Loja> lojas, Map<Integer, Produto> produtos, List<Item> itens, List<Carrinho> compras, List<ItemCarrinho> cart) 
    {
    	// Inicialia��o das variaveis
        List<Item> itensFiltrados = null;
        int opcao = 0, opcao2 = 0;
        // Pega a quantidade de compras antes de serem feitas modifica��es, no caso poss�veis novas compras 
        int quantidadeComprasAntes = compras.size();
        /* Na mesma ideia da vari�vel acima pega o carrinho antigo e depois compara com o novo 
         * para verificar se tem a necessidade de escrever o arquivo bin�rio */
        List<ItemCarrinho> cartOld = new ArrayList<>(cart);
        boolean chamadaCompra = false;
        String msgErro = "Entrada de dado inv�lida. Tente novamente:";
        String msgFiltroVazio = ""; // Serve para mostrar mensagem caso a busca n�o contenha nenhum elemento
		
        System.out.println("Bem-vindo a Shirugeru store.\n\n");
        
        // Faz o menu enquanto o opcao continuar ser menor que zero.
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
        		case 0: //Finaliza a execu��o do sistema
        			// Antes verificar se cont�m algum produto pendente no carrinho
        			if (cart.size() > 0) {
        				System.out.println("Voc� possui produtos em seu carrinho. Deseja realmente sair do sistema?\n1 - Sim\n2 - N�o");
        				opcao = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
        				// Cliente N�O deseja fechar o sistema sem comprar os itens do carrinho
        				if (opcao == 2) {
        					System.out.println("\nRedirecionado para o seu carrinho.\n");
        					menuCart(itens, cart, compras);// Redireciona para op��o 6 do menu principal que � o carrinho
        					break;
        				}
        			}
        			// Caso ele deseje fechar o sistema com itens ou sem itens no carrinho ent�o � salvo os registros juntamente com carrinho do cliente
        			Listagem.pularLinha(3);
        			System.out.println("Agradecemos a prefer�ncia!!\n VOLTE SEMPRE");
        			
        			/* Verifica antes se houve altera��o nas compras atuais em rela��o a anterior ou se houve altera��es no carrinho novo 
        			 * em rela��o ao anterior. Caso tenha altera��es ent�o faz todo sentido salvar no sistema */
        			if (quantidadeComprasAntes != compras.size() || checaCarrinhoAlterado(cartOld, cart)) {
        				String nomeArq = "Database/sistema.dat";
        				salvarSistema(itens, lojas, produtos, compras, cart, nomeArq);
        			}
        			
        			opcao = 0;
        			chamadaCompra = false;
        			break;
        			
        		case 1: //busca por loja
        			List<Loja> listLojas = Busca.todasLojas(lojas);
        			System.out.println("0 - Voltar");
        			Listagem.listarLojas(listLojas);
        			System.out.println("\nSelecione a loja desejada: ");
        			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, listLojas.size(), msgErro, msgErro); // Captura a opcao desejada da loja
        			if (opcao2 == 0) { break; } 
        			itensFiltrados = Busca.porLoja(itens, listLojas.get(opcao2 - 1));
        			msgFiltroVazio = "N�o h� produtos na loja selecionada";
        			chamadaCompra = true;
        			mode = 1;
        			break;
        		
        		case 2: //busca por nome
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			String nomeProd = scanUser.nextLine();
        			itensFiltrados = Busca.nomeProduto(itens, nomeProd);
        			msgFiltroVazio = "N�o foi poss�vel encontrar nenhum produto contendo esse nome";
        			chamadaCompra = true;
        			break;
        		
        		case 3: //busca por tipo
        			System.out.println("Escolha uma opcao do tipo desejado:");
        			System.out.println("1 - Eletronico\n2 - Item de Casa\n3 - Livro\n0 - Voltar");
        			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, 3, msgErro, msgErro);
        			if (opcao2 == 0) { break; } 
        			itensFiltrados = Busca.tipoProduto(itens, opcao2);
        			msgFiltroVazio = "N�o h� produtos do tipo selecionado";
        			chamadaCompra = true;
        			break;
        			
        		case 4: //busca pelo codigo do produto
        			List<Produto> listProdutos = Busca.todosProdutos(produtos);
        			Listagem.listarProdutos(listProdutos);
        			Listagem.pularLinha(1);
        			System.out.println("0 - Voltar");
        			System.out.println("\nSelecione a op��o do c�digo do produto desejada: ");
        			int codFinal = listProdutos.get(listProdutos.size()-1).getCodigo(); // Define codigo maximo
        			int codigo = controleEntradaDados(scanUser.nextLine(), 0, codFinal, msgErro, msgErro); // Captura a opcao desejada da loja
        			if (codigo == 0) { break; } 
        			itensFiltrados = Busca.codigoProduto(itens, codigo);
        			msgFiltroVazio = "N�o foi poss�vel encontrar nenhum produto contendo esse nome";
        			chamadaCompra = true;
        			break;
        			
        		case 5: //busca por todos os produtos
        			System.out.println("Todos os produtos cadastrados do sistema:");
        			itensFiltrados = itens;
        			Collections.sort(itens); // Ordena pelo pre�o
        			chamadaCompra = true;
        			break;
        			
        		case 6: // Usu�rio acessa seu carrinho escolhendo as op��es de finalizar, cancelar e alterando a compra
        			if (cart.isEmpty()) {
						System.out.println("Carrinho Vazio");
					}else {
	        			menuCart(itens, cart, compras);
					}
        			chamadaCompra = false;
        			break;
        			
        		case 7: // Acessa o hist�rico de compras do sistema. 
        			if (compras.isEmpty()) {
						System.out.println("N�o h� historico de compras");
					}
        			else {
        				System.out.println("Deseja acessar qual hist�rico de compras?");
        				System.out.println("0 - Voltar\n1 - Hist�rico geral de compras\n2 - Hist�rico sequencial de compras\n\n");
        				opcao2 = controleEntradaDados(scanUser.nextLine(), 0, 2, msgErro, msgErro);
        				if (opcao2 == 0) { break; }
						if (opcao2 == 1)
							Listagem.historicoComprasGeral(compras);
						else
							Listagem.historicoComprasSequencial(compras);
					}
        			chamadaCompra = false;
        			break;
        		
        		default:
        			chamadaCompra = false;
        			System.out.println("N�o existe essa busca");
        	}
        	
        	// Caso verdadeiro faz a chamada da compra perguntando ao usu�rio final se deseja comprar algum item da busca selecionada
        	if (chamadaCompra) {
        		if (itensFiltrados != null && !itensFiltrados.isEmpty()) {
        			Listagem.listarItens(itensFiltrados, mode);
            		tcgBuy(itensFiltrados, cart);
        		}
        		// Imprime a mensagem quando a lista de itensFiltrados da busca retorna nenhum elemento, logo n�o sendo poss�vel fazer a compra
        		else if (itensFiltrados != null)
        			System.out.println(msgFiltroVazio);
        	}
        	itensFiltrados = null;
        	
        	System.out.println("############################################################");
        	Listagem.pularLinha(2);
        	
        } while (opcao != 0);
    }
    
    /* Serve para quando o cliente deseja comprar algum item da loja */
    /* M�todo que serve para o usu�rio do sistema comprar itens cadastrados no sistema */
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
				Listagem.pularLinha(1);
				Listagem.listarItens(itens, 0);
				Listagem.pularLinha(1);
				
				//Escolher op��o de escolha
				opcao = controleEntradaDados(scanUser.nextLine(), 0, itens.size(), msgErro, msgErro);
				
				// atraves do que foi selecionado pelo usuario � colocado o item em seu carrinho
				if (opcao != 0) {
					Item itemSelecionado = itens.get(opcao-1);
					int qtdItensDisponiveis = itemSelecionado.getQuantidade();
					boolean itemNoCarrinho = false;
					
					// Checa se o item j� existe no carrinho de compras
					for (ItemCarrinho itc: cart) {
						if (itc.getItem().equals(itemSelecionado)) {
							qtdItensDisponiveis = itemSelecionado.getQuantidade() - itc.getQuantidade();
							itemNoCarrinho = true;
							break;
						}
					}
					
					// Verifica se n�o tem mais o item no estoque
					if (qtdItensDisponiveis > 0) {
						
						String msgErro2 = "Quantidade inserida inv�lida ou indispon�vel. Insira novamente:";
						
						System.out.println("Informe a quantidade do produto: ");
						
						int qtd = controleEntradaDados(scanUser.nextLine(), 1, qtdItensDisponiveis, msgErro, msgErro2);
						Listagem.pularLinha(1);
						
						ItemCarrinho itemCart = new ItemCarrinho(itemSelecionado, qtd); 
						
						// Verifica primeiro se j� existe o item no carrinho e caso tenha s� precisa adicionar a quantidade escolhida
						if (itemNoCarrinho) {
							int pos = cart.indexOf(itemCart);
							cart.get(pos).adicionaQtdItens(qtd);
						}
						else { cart.add(itemCart); } // Adiciona o item que n�o existe no item carrinho ainda
						
						System.out.println("Produto adicionado ao Carrinho");
						Listagem.listarItensCarrinho(cart);
						
					}
					else //Caso o estoque do item esteja zerado
						System.out.println("Quantia seleciona por voc� j� � a mais do que est� dispon�vel no estoque");
					
					Listagem.pularLinha(1);
				}
			}
		}
    }
    
    /* Menu do carrinho que permite o usu�rio finalizar a compra, alterar itens do carrinho ou cancelar o carrinho
     * ou seja, Menu para interagir com o carrinho */
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
	    			
	    			// Cliente finaliza a compra e � registrado no lista de compras para hist�rico
	    			if (opcao2 == 1) {
	    				List<ItemCarrinho> carrinhoCliente = new ArrayList<>();
	    				carrinhoCliente.addAll(cart);
	    				Carrinho compraFechada = new Carrinho(carrinhoCliente, new Date());
	    				compras.add(compraFechada); //Adiciona a compra na lista de compras para registro
	    				
	    				/* Antes de dar um flush/limpar o carrinho que o cliente estava usando � necess�rio debitar a compra realizada 
	    				 * do estoque dos produtos do sistema, pois a compra foi realmente realizada */
	    				for (ItemCarrinho itc : cart) {
	    					int qtdDebitarEstoque = itc.getQuantidade();
	    					itc.getItem().debitarEstoque(qtdDebitarEstoque); // Debitado do estoque
	    				}
	    				cart.clear(); // Limpa o cart que � o "carrinho" que o cliente estava usando
	    				
	    				System.out.println("Obrigado pela compra\nRedirecionando ao menu principal");
	    				opcao1 = 0; //Volta para menu principal
	    			}
	    			break;
	    		
	    		case 2: // Adiciona unidades do item escolhido no carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja adicionar unidades:");		    			
		    			Listagem.listarItensCarrinho(cart);
		    			Listagem.pularLinha(1);
		    			System.out.println("0 - Voltar");
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } // Sai do loop porque o usu�rio decidiu n�o cancelar mais
		    			
		    			ItemCarrinho itemCartAdicionar = cart.get(opcao2 - 1); // Pega o itemCarrinho da op��o escolhida
		    			Item itemSelecionado = itemCartAdicionar.getItem(); // Pega o item que foi escolhido para adicionar as unidades no carrinho
		    			int qtdDisponivel = itemSelecionado.getQuantidade() - itemCartAdicionar.getQuantidade();
		    			
		    			// Tem disponibilidade ent�o pode adicionar mais itens no carrinho
		    			if (qtdDisponivel > 0) {
		    				System.out.println("Informa��es do item no estoque:\n" + itemSelecionado + "\n");
		    				System.out.println("Escolha a quantidade que deseja adicionar:");
			    			String msgErro2 = "N�o se pode adicionar com a quantidade inserida, pois � mais do que h� em estoque";
			    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, qtdDisponivel, msgErro, msgErro2);
			    			
			    			// Basta adicionar no carrinho a quantia v�lida inserida
			    			itemCartAdicionar.adicionaQtdItens(quantidade);
		    			}
		    			else 
		    				System.out.println("N�o pode adicionar mais unidades desse produto, pois voc� selecionou todos"
		    									+ " itens do estoque\n");
	    			}
	    			break;
	    		
	    		case 3: // Remove unidades do item escolhido no carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja remover unidades:");
		    			Listagem.listarItensCarrinho(cart);
		    			System.out.println("0 - Voltar");
		    			Listagem.pularLinha(1);
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usu�rio decidiu n�o cancelar mais
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); // Pega o itemCarrinho da op��o escolhida
		    			
		    			System.out.println("Escolha a quantidade que deseja remover:");
		    			String msgErro2 = "N�o se pode remover com a quantidade inserida";
		    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, itemCartRemover.getQuantidade(), msgErro, msgErro2);
		    			
		    			// Remover do carrinho a quantidade inserida
		    			itemCartRemover.removeQtdItens(quantidade);
		    			
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
	    			
	    		case 4: // Exclui produto selecionado do carrinho.
	    			while (true) {
	    				System.out.println("Escolha o produto que deseja remover totalmente:");
		    			System.out.println("0 - Voltar");
		    			Listagem.listarItensCarrinho(cart);
		    			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, cart.size(), msgErro, msgErro);
		    			Listagem.pularLinha(1);
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usu�rio decidiu n�o cancelar mais
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); //Pega o itemCarrinho da op��o escolhida
		    			// Pega a quantidade que h� no carrinho.
		    			quantidade = itemCartRemover.getQuantidade();
		    			
		    			// Basta remover do carrinho 
		    			itemCartRemover.removeQtdItens(quantidade);
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
	    				cart.clear();
	    				System.out.println("Carrinho cancelado com sucesso. Redirecionando para o menu principal");
	    				opcao1 = 0; // Volta ao menu principal
	    			}
	    			break;
    		}
    	} while(opcao1 != 0);
    }
    
    /* Serve para controlar a entrada de dados pelo teclado do usu�rio, para que n�o d� erros durante a execu��o do sistema */
    /* M�todo que serve para controlar entrada de dados realizada pelo usu�rio para que digite o correto */
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
    
    /* M�todo que checa o carrinho antes de usar o sistema e depois de usar o sistema. Ideal para que no momento de 
     * chamar m�todo salvarSistema n�o escreva o arquivo bin�rio sem necessidade, caso o cliente somente tenha usado o sistema para consultas */
    private static boolean checaCarrinhoAlterado(List<ItemCarrinho> oldCart, List<ItemCarrinho> newCart) {
    	 
    	if (oldCart.isEmpty() && newCart.isEmpty()) // Primeiro checa se o carrinho antes e depois est� vazio
    		return false;
    	else if (oldCart.size() != newCart.size()) // Caso o tamanho de itens dos dois carrinhos sejam difentes houve mudan�a
    		return true;
    	else {
    		Collections.sort(oldCart); Collections.sort(newCart); // Ordena ambos carrinhos
    		// Compara pela posi��o de cada um, onde caso a quantidade seja a mesma � porque s�o carrinhos iguais
    		for (int i = 0; i < oldCart.size(); i++) {
    			if (oldCart.get(i).getQuantidade() != newCart.get(i).getQuantidade())
    				return false;
    		}
    	}
    	
    	return true; // Carrinho ap�s uso do sistema foi realmente alterado
    }
    
    /* Serializa o essencial para o sistema, e joga dentro de um arquivo .dat, salvando o sistema */
    public static boolean salvarSistema(List<Item> itens, Map<String, Loja> lojas, Map<Integer, Produto> produtos, List<Carrinho> compras, List<ItemCarrinho> clienteCart, String nomeArq)
    {
    	boolean fechadoComSucesso = false;
    	
    	try {
    		OutputStream fos = new FileOutputStream(nomeArq);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    
		    oos.writeObject(itens);
		    oos.writeObject(lojas);
		    oos.writeObject(produtos);
		    oos.writeObject(compras);
		    oos.writeObject(clienteCart);
		    oos.close();
		    
		    fechadoComSucesso = true; // Salvo o arquivo com sucesso
    	}
	    catch (FileNotFoundException e) {
	    	System.out.println("N�o � poss�vel gerar o arquivo de sa�da. Pode ser o diret�rio que est� sendo submetido");
	    	
	    }
    	catch (IOException e) {
    		System.out.println("Houve problema na escrita do arquivo");
    		
    	}
	    
	    return fechadoComSucesso;
    }
}
	
