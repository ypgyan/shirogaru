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
	//Atributo static da classe para pegar leitura do usuário
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
    		System.out.println("Carregado com sucesso");
    	}
    	else {
    		System.out.println("Error system");
    	}
    	
    }
    
    /* Abrir o sistema carregando as informações pelo arquivo.dat(arquivo binário) */
    @SuppressWarnings("unchecked") // Suprimir os avisos do casting no read object
	public static boolean carregarSistema (List<Item> itens, Map<String, Loja> lojas, Map<Integer, Produto> produtos, List<Carrinho> compras, List<ItemCarrinho> clienteCart, String nomeArq) {
    	
    	boolean carregadoComSucesso = false;
    	
	    try {	
		    InputStream fis = new FileInputStream(nomeArq);
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    
		    // Recupera os arquivos de acordo com a ordem que foram inseridos no arquivo.dat
	    	itens = (List<Item>) ois.readObject();
		    lojas = (Map<String,Loja>) ois.readObject();
		    produtos = (Map<Integer, Produto>) ois.readObject();
		    compras = (List<Carrinho>) ois.readObject();
		    clienteCart = (List<ItemCarrinho>) ois.readObject();
		    
		    ois.close(); // Fecha o ObjectInputStream
		    carregadoComSucesso = true;
	    }
	    catch (FileNotFoundException e) {
	    	carregadoComSucesso = importarLojas(lojas, "Database/lojas.txt");
	    	carregadoComSucesso = extrairItens(itens, lojas, produtos, "Database/produtos.txt");
	    }
	    catch (ClassCastException | ClassNotFoundException | IOException e) {
	    	System.out.println("Houve problema na leitura do arquivo");
	    }
		    
    	return carregadoComSucesso;
    }
    
    /* Importa as lojas do arquivo.txt */
    public static boolean importarLojas(Map<String, Loja> lojas, String nomeArq)  
    {
    	boolean importadoComSucesso = false;
    	
    	try {
    		Scanner scanLojas = new Scanner(new File(nomeArq));
            
    		// Para segurança faz uma checagem para identificar se o mapa de lojas já foi instanciado
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
    		System.out.println("Erro na abertura do arquivo. Arquivo não encontrado");
    	}
    	
        return importadoComSucesso;
    }

    /* Importa os produtos do arquivo e coloca em seus respectivos itens */
    public static boolean extrairItens(List<Item> itens, Map<String, Loja> lojas, Map<Integer, Produto> produtos, String nomeArq)
    {
    	boolean extraidoComSucesso = false;
    	Scanner scanProdutos = null;
    	
    	try {
    		scanProdutos = new Scanner(new File(nomeArq));
    		extraidoComSucesso = true;
    	}
    	catch (FileNotFoundException e) {
    		System.out.println("Erro na abertura do arquivo. Arquivo não encontrado");
    	}
    	
    	// Erro na abertura, logo volta false porque não abriu
    	if (extraidoComSucesso == false) 
    		return extraidoComSucesso;
    	
    	// Verifica antes por segurança se o list de itens já está instanciado
    	if (itens == null)
    		itens = new ArrayList<>();
    	
    	// Verifica também se os produtos passados já está instanciado
    	if (produtos == null)
    		produtos = new HashMap<>();
    	
    	// Serve para quando acha um produto que não pode ser instanciado como produto, pois não existe uma subclasse para ele
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
    			Item item = new Item(lojas.get(Idloja), produtos.get(cod), preco, quantidade);
    			// Checa se o item já existe dentro da lista para não haver erros de duplicação de itens iguais
    			if (!itens.contains(item))
    				itens.add(item); // Item novo
    			else {
    				System.out.println("Item já existente na lista de registros. Para evitar problemas de redundância "
    									+ "foi pegado a primeira ocorrência do item no arquivo lido");
    			}
    		}
    	}		
    	scanProdutos.close();
    	
    	return extraidoComSucesso;
    }
    
    /* Menu que interage com o usuário final do sistema */
    public static void menu (Map<String, Loja> lojas, Map<Integer, Produto> produtos, List<Item> itens, List<Carrinho> compras, List<ItemCarrinho> cart) 
    {
    	// Inicialiação das variaveis
        List<Item> itensFiltrados = null;
        int opcao = 0, opcao2 = 0;
        // Pega a quantidade de compras antes de serem feitas modificações, no caso possíveis novas compras 
        int quantidadeComprasAntes = compras.size();
        /* Na mesma ideia da variável acima pega o carrinho antigo e depois compara com o novo 
         * para verificar se tem a necessidade de escrever o arquivo binário */
        List<ItemCarrinho> cartOld = new ArrayList<>(cart);
        boolean chamadaCompra = false;
        String msgErro = "Entrada de dado inválida. Tente novamente:";
        String msgFiltroVazio = ""; // Serve para mostrar mensagem caso a busca não contenha nenhum elemento
		
        System.out.println("Bem-vindo a Shirugeru store.\n\n");
        
        // Faz o menu enquanto o opcao continuar ser menor que zero.
        do{
        	// Variavel que define se lista o valor total ou lojas.
            int mode = 0;
        	// Caso o usuario ja tenho feito uma compra o carrinho será mostrado
        	if (cart.size() > 0) {
        		System.out.println("CARRINHO");
        		Listagem.listarItensCarrinho(cart);
        		Listagem.pularLinha(2);
			}
        	// Mostra as opções disponiveis no sistema.
        	System.out.println("Escolha uma opcão:");
			System.out.println("1 - Busca produtos por Loja\n2 - Busca de produtos por nome"
					+ "\n3 - Busca de produtos por Tipo\n4 - Busca de produtos por código"
					+ "\n5 - Todos os itens do sistema\n6 - Seu carrinho"
					+ "\n7 - Historico de compras\n0 - Encerrar Sistema");
			
			opcao = controleEntradaDados(scanUser.nextLine(), 0, 100, msgErro, msgErro);
        	
			Listagem.pularLinha(2);
        	switch (opcao) {
        		case 0: //Finaliza a execução do sistema
        			// Antes verificar se contém algum produto pendente no carrinho
        			if (cart.size() > 0) {
        				System.out.println("Você possui produtos em seu carrinho. Deseja realmente sair do sistema?\n1 - Sim\n2 - Não");
        				opcao = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
        				// Cliente NÃO deseja fechar o sistema sem comprar os itens do carrinho
        				if (opcao == 2) {
        					System.out.println("\nRedirecionado para o seu carrinho.\n");
        					menuCart(itens, cart, compras);// Redireciona para opção 6 do menu principal que é o carrinho
        					break;
        				}
        			}
        			// Caso ele deseje fechar o sistema com itens ou sem itens no carrinho então é salvo os registros juntamente com carrinho do cliente
        			Listagem.pularLinha(3);
        			System.out.println("Agradecemos a preferência!!\n VOLTE SEMPRE");
        			
        			/* Verifica antes se houve alteração nas compras atuais em relação a anterior ou se houve alterações no carrinho novo 
        			 * em relação ao anterior. Caso tenha alterações então faz todo sentido salvar no sistema */
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
        			msgFiltroVazio = "Não há produtos na loja selecionada";
        			chamadaCompra = true;
        			mode = 1;
        			break;
        		
        		case 2: //busca por nome
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			String nomeProd = scanUser.nextLine();
        			itensFiltrados = Busca.nomeProduto(itens, nomeProd);
        			msgFiltroVazio = "Não foi possível encontrar nenhum produto contendo esse nome";
        			chamadaCompra = true;
        			break;
        		
        		case 3: //busca por tipo
        			System.out.println("Escolha uma opcao do tipo desejado:");
        			System.out.println("0 - Voltar\n1 - Eletronico\n2 - Item de Casa\n3 - Livro");
        			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, 3, msgErro, msgErro);
        			if (opcao2 == 0) { break; } 
        			itensFiltrados = Busca.tipoProduto(itens, opcao2);
        			msgFiltroVazio = "Não há produtos do tipo selecionado";
        			chamadaCompra = true;
        			break;
        			
        		case 4: //busca pelo codigo do produto
        			List<Produto> listProdutos = Busca.todosProdutos(produtos);
        			System.out.println("0 - Voltar");
        			Listagem.listarProdutos(listProdutos);
        			System.out.println("\nSelecione a opção do código do produto desejada: ");
        			opcao2 = controleEntradaDados(scanUser.nextLine(), 0, listProdutos.size(), msgErro, msgErro); // Captura a opcao desejada da loja
        			if (opcao2 == 0) { break; } 
        			itensFiltrados = Busca.codigoProduto(itens, listProdutos.get(opcao2 - 1).getCodigo());
        			msgFiltroVazio = "Não foi possível encontrar nenhum produto contendo esse nome";
        			chamadaCompra = true;
        			break;
        			
        		case 5: //busca por todos os produtos
        			System.out.println("Todos os produtos cadastrados do sistema:");
        			itensFiltrados = itens;
        			Collections.sort(itens); // Ordena pelo preço
        			chamadaCompra = true;
        			break;
        			
        		case 6: // Usuário acessa seu carrinho escolhendo as opções de finalizar, cancelar e alterando a compra
        			if (cart.isEmpty()) {
						System.out.println("Carrinho Vazio");
					}else {
	        			menuCart(itens, cart, compras);
					}
        			chamadaCompra = false;
        			break;
        			
        		case 7: // Acessa o histórico de compras do sistema
        			if (compras.isEmpty()) {
						System.out.println("Não há historico de compras");
					}else {
						System.out.println("Histórico de compras");
						Listagem.historicoCompras(compras);
					}
        			chamadaCompra = false;
        			break;
        			
        		default:
        			chamadaCompra = false;
        			System.out.println("Não existe essa busca");
        	}
        	
        	// Caso verdadeiro faz a chamada da compra perguntando ao usuário final se deseja comprar algum item da busca selecionada
        	if (chamadaCompra) {
        		if (itensFiltrados != null && !itensFiltrados.isEmpty()) {
        			Listagem.listarItens(itensFiltrados, mode);
            		tcgBuy(itensFiltrados, cart);
        		}
        		// Imprime a mensagem quando a lista de itensFiltrados da busca retorna nenhum elemento, logo não sendo possível fazer a compra
        		else if (itensFiltrados != null)
        			System.out.println(msgFiltroVazio);
        	}
        	itensFiltrados = null;
        	
        	System.out.println("############################################################");
        	Listagem.pularLinha(2);
        	
        } while (opcao != 0);
    }
    
    /* Serve para quando o cliente deseja comprar algum item da loja */
    /* Método que serve para o usuário do sistema comprar itens cadastrados no sistema */
    private static void tcgBuy(List<Item> itens, List<ItemCarrinho> cart) 
    {
    	Listagem.pularLinha(2);
    	int decision = 1;
    	
    	while (decision == 1) {
    		// Pergunta para o usuário se ele realmente deseja comprar um dos pordutos (Adicionar no carrinho)
        	System.out.println("Você deseja:\n0 - Voltar para o menu principal\n1 - Comprar algum item");
        	
        	String msgErro = "Entrada de dado inválida. Tente novamente:";
        	
        	decision = controleEntradaDados(scanUser.nextLine(), 0, 1, msgErro, msgErro);
        	Listagem.pularLinha(2);
        	
        	if (decision == 0) //O usuário deseja voltar para o menu principal
        		return;
        	
        	int opcao = 1;
        	//Deseja comprar	
			while (opcao != 0) {
				System.out.println("Selecione o item que deseja comprar:");
				// Lista o resultado da pesquisa outra
				System.out.println("0 - Voltar");
				Listagem.listarItens(itens, 0);
				Listagem.pularLinha(1);
				
				//Escolher opção de escolha
				opcao = controleEntradaDados(scanUser.nextLine(), 0, itens.size(), msgErro, msgErro);
				
				// atraves do que foi selecionado pelo usuario é colocado o item em seu carrinho
				if (opcao != 0) {
					Item itemSelecionado = itens.get(opcao-1);
					int qtdItensDisponiveis = itemSelecionado.getQuantidade();
					boolean itemNoCarrinho = false;
					
					// Checa se o item já existe no carrinho de compras
					for (ItemCarrinho itc: cart) {
						if (itc.getItem().equals(itemSelecionado)) {
							qtdItensDisponiveis = itemSelecionado.getQuantidade() - itc.getQuantidade();
							itemNoCarrinho = true;
							break;
						}
					}
					
					// Verifica se não tem mais o item no estoque
					if (qtdItensDisponiveis > 0) {
						
						String msgErro2 = "Quantidade inserida inválida ou indisponível. Insira novamente:";
						
						System.out.println("Informe a quantidade do produto: ");
						
						int qtd = controleEntradaDados(scanUser.nextLine(), 1, qtdItensDisponiveis, msgErro, msgErro2);
						Listagem.pularLinha(1);
						
						ItemCarrinho itemCart = new ItemCarrinho(itemSelecionado, qtd); 
						
						// Verifica primeiro se já existe o item no carrinho e caso tenha só precisa adicionar a quantidade escolhida
						if (itemNoCarrinho) {
							int pos = cart.indexOf(itemCart);
							cart.get(pos).adicionaQtdItens(qtd);
						}
						else { cart.add(itemCart); } // Adiciona o item que não existe no item carrinho ainda
						
						System.out.println("Produto adicionado ao Carrinho");
						Listagem.listarItensCarrinho(cart);
						
					}
					else //Caso o estoque do item esteja zerado
						System.out.println("Quantia seleciona por você já é a mais do que está disponível no estoque");
					
					Listagem.pularLinha(1);
				}
			}
		}
    }
    
    /* Menu do carrinho que permite o usuário finalizar a compra, alterar itens do carrinho ou cancelar o carrinho
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
    		
    		String msgErro = "Não existe essa opção. Tente novamente.";
    		opcao1 = controleEntradaDados(scanUser.nextLine(), 0, 5, msgErro, msgErro);
    		
    		msgErro = "Entrada de dado inválida. Tente novamente:"; 
    		
    		switch (opcao1) {
    			case 0 : // Voltar ao menu principal 
    				opcao1 = 0; // Seta a opção 1 para zero e volta para o menu principal
    				break;
    			
	    		case 1: // Finaliza compra.
	    			System.out.println("Deseja realmente finalizar as compras?\n1 - Sim\n2 - Não");
	    			opcao2 = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
	    			
	    			// Cliente finaliza a compra e é registrado no lista de compras para histórico
	    			if (opcao2 == 1) {
	    				List<ItemCarrinho> carrinhoCliente = new ArrayList<>();
	    				carrinhoCliente.addAll(cart);
	    				Carrinho compraFechada = new Carrinho(carrinhoCliente, new Date());
	    				compras.add(compraFechada); //Adiciona a compra na lista de compras para registro
	    				
	    				/* Antes de dar um flush/limpar o carrinho que o cliente estava usando é necessário debitar a compra realizada 
	    				 * do estoque dos produtos do sistema, pois a compra foi realmente realizada */
	    				for (ItemCarrinho itc : cart) {
	    					int qtdDebitarEstoque = itc.getQuantidade();
	    					itc.getItem().debitarEstoque(qtdDebitarEstoque); // Debitado do estoque
	    				}
	    				cart.clear(); // Limpa o cart que é o "carrinho" que o cliente estava usando
	    				
	    				System.out.println("Obrigado pela compra\nRedirecionando ao menu principal");
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
		    			
		    			if (opcao2 == 0) { break; } // Sai do loop porque o usuário decidiu não cancelar mais
		    			
		    			ItemCarrinho itemCartAdicionar = cart.get(opcao2 - 1); // Pega o itemCarrinho da opção escolhida
		    			Item itemSelecionado = itemCartAdicionar.getItem(); // Pega o item que foi escolhido para adicionar as unidades no carrinho
		    			int qtdDisponivel = itemSelecionado.getQuantidade() - itemCartAdicionar.getQuantidade();
		    			
		    			// Tem disponibilidade então pode adicionar mais itens no carrinho
		    			if (qtdDisponivel > 0) {
		    				System.out.println("Informações do item no estoque:\n" + itemSelecionado + "\n");
		    				System.out.println("Escolha a quantidade que deseja adicionar:");
			    			String msgErro2 = "Não se pode adicionar com a quantidade inserida, pois é mais do que há em estoque";
			    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, qtdDisponivel, msgErro, msgErro2);
			    			
			    			// Basta adicionar no carrinho a quantia válida inserida
			    			itemCartAdicionar.adicionaQtdItens(quantidade);
		    			}
		    			else 
		    				System.out.println("Não pode adicionar mais unidades desse produto, pois você selecionou todos"
		    									+ " itens do estoque\n");
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
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); // Pega o itemCarrinho da opção escolhida
		    			
		    			System.out.println("Escolha a quantidade que deseja remover:");
		    			String msgErro2 = "Não se pode remover com a quantidade inserida";
		    			quantidade = controleEntradaDados(scanUser.nextLine(), 1, itemCartRemover.getQuantidade(), msgErro, msgErro2);
		    			
		    			// Remover do carrinho a quantidade inserida
		    			itemCartRemover.removeQtdItens(quantidade);
		    			
		    			// Deve checar se a unidade do item removido do carrinho está zero. Logo ele deve sair do carrinho
		    			if (itemCartRemover.getQuantidade() == 0) {
		    				cart.remove(opcao2 - 1); // Remove o item do carrinho através do index
		    				
		    				// Após remover o item verificar se existe algum item no carrinho do cliente
		    				if (cart.isEmpty()) {
		    					opcao1 = 0; // Para voltar ao menu principal
		    					System.out.println("Carrinho está vazio. Redirecionando para o menu principal");
		    					break; // Sai da opção que contém while = true
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
		    			
		    			if (opcao2 == 0) { break; } //Sai do loop porque o usuário decidiu não cancelar mais
		    			ItemCarrinho itemCartRemover = cart.get(opcao2 - 1); //Pega o itemCarrinho da opção escolhida
		    			// Pega a quantidade que há no carrinho.
		    			quantidade = itemCartRemover.getQuantidade();
		    			
		    			// Basta remover do carrinho 
		    			itemCartRemover.removeQtdItens(quantidade);
		    			cart.remove(opcao2 - 1);
		    			System.out.println("Produto removido do carrinho");
		    			if (cart.isEmpty()) {
		    				System.out.println("Carrinho está vazio. Redirecionando para o menu principal");
		    				opcao1 = 0;
		    				break; // Sai da opção de exclui o produto do carrinho
		    			}
	    			}
	    			break;
	    			
	    		case 5: // Cancelar o carrinho de compras
	    			System.out.println("Deseja realmente cancelar suas compras?\n1 - Sim\n2 - Não");
	    			opcao2 = controleEntradaDados(scanUser.nextLine(), 1, 2, msgErro, msgErro);
	    			
	    			// Caso o usuário final deseje realmente cancelar é dado um clear no carrinho 
	    			if (opcao2 == 1) {
	    				cart.clear();
	    				System.out.println("Carrinho cancelado com sucesso. Redirecionando para o menu principal");
	    				opcao1 = 0; // Volta ao menu principal
	    			}
	    			break;
    		}
    	} while(opcao1 != 0);
    }
    
    /* Serve para controlar a entrada de dados pelo teclado do usuário, para que não dê erros durante a execução do sistema */
    /* Método que serve para controlar entrada de dados realizada pelo usuário para que digite o correto */
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
    
    /* Método que checa o carrinho antes de usar o sistema e depois de usar o sistema. Ideal para que no momento de 
     * chamar método salvarSistema não escreva o arquivo binário sem necessidade, caso o cliente somente tenha usado o sistema para consultas */
    private static boolean checaCarrinhoAlterado(List<ItemCarrinho> oldCart, List<ItemCarrinho> newCart) {
    	 
    	if (oldCart.isEmpty() && newCart.isEmpty()) // Primeiro checa se o carrinho antes e depois está vazio
    		return false;
    	else if (oldCart.size() != newCart.size()) // Caso o tamanho de itens dos dois carrinhos sejam difentes houve mudança
    		return true;
    	else {
    		Collections.sort(oldCart); Collections.sort(newCart); // Ordena ambos carrinhos
    		// Compara pela posição de cada um, onde caso a quantidade seja a mesma é porque são carrinhos iguais
    		for (int i = 0; i < oldCart.size(); i++) {
    			if (oldCart.get(i).getQuantidade() != newCart.get(i).getQuantidade())
    				return false;
    		}
    	}
    	
    	return true; // Carrinho após uso do sistema foi realmente alterado
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
	    	System.out.println("Não é possível gerar o arquivo de saída. Pode ser o diretório que está sendo submetido");
	    	
	    }
    	catch (IOException e) {
    		System.out.println("Houve problema na escrita do arquivo");
    		
    	}
	    
	    return fechadoComSucesso;
    }
}
	
