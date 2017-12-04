package app;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
    
    public static void menu (Map<String, Loja> lojas, List<Item> itens ) {
    	//Menu para o usuario que chamar a classe aplicacao
        
    	// Inicialiação das variaveis
        List<ItemCarrinho> cart = new ArrayList<>();
        int opcao = 0;
		String opcao2 = "";
		
        
        //Faz o menu enquanto o opcao continuar ser menor que zero
        do{
        	// Caso o usuario ja tenho feito uma compra o carrinho sera mostrado
        	if (cart.size() > 0) {
        		printCart(cart);
			}
        	// Mostra as opções disponiveis no sistema.
        	System.out.println("Escolha uma opcao da busca desejada dos itens:");
			System.out.println("1 - Por Loja\n2 - Por Nome do produto\n3 - Por Tipo\n4 - todos os produtos(Em construção)"
					+ "\n5 - Todas as lojas (Em construção)\n0 - Encerrar Sistema");

			opcao = Integer.parseInt(scanUser.nextLine());
			
        	pularLinha(2);
        	switch (opcao) {
        		case 0:
        			System.out.println("Agradecemos a preferência!!\n VOLTE SEMPRE");
        			break;
        			
        		case 1: //busca por loja
        			Busca.listarLojas(new ArrayList<>(lojas.values()));
        			System.out.println("Informe o nome da loja desejada: ");
        			opcao2 = scanUser.nextLine(); // Captura o nome desejada da loja
        			System.out.println();
        			Busca.nomeLoja(itens, opcao2);
        			tcgBuy(itens,cart);
        			
        			break;
        		
        		case 2: //busca por nome
        			// Lista todos os produtos do sistema
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			String nomeProd = scanUser.nextLine();
        			Busca.nomeProduto(itens, nomeProd);
        			// Chama a função que preencher o carrinho.
        			tcgBuy(itens, cart);
        			break;
        		
        		case 3: //busca por tipo
        			System.out.println("Escolha uma opcao do tipo desejado:");
        			System.out.println("1 - Eletronico\n2 - Item de Casa\n3 - Livro");
        			opcao = Integer.parseInt(scanUser.nextLine());
        			Busca.tipoProduto(itens, opcao);
        			tcgBuy(itens, cart);
        			break; 
        			
        		default:
        			System.out.println("Nao existe essa busca");
        			
        	}
        	pularLinha(5);
        } while (opcao > 0);
        
    }
    
    private static boolean tcgBuy(List<Item> itens, List<ItemCarrinho> cart) {
    	pularLinha(2);
    	// Inicialização das variaveis
    	Item itemSelecionado;
    	int decision = 0;
    	int cod = 0;
    	String loja = "";
    	
    	// Pergunta para o usuário se ele realmente deseja comprar um dos pordutos (Adicionar no carrinho)
    	// E verifica se ele está inserindo o valor correto.
    	while(decision < 1 || decision > 2) {
    		System.out.println("Deseja informar um item para compra ou iniciar outra busca? \n 1 - yes \n 2 - no");
        	decision = scanUser.nextInt();
    	}
    	
		if (decision == 1) {
			
			System.out.println("Informe o nome da loja e o codigo do produto: ");
			System.out.println("Loja: ");
			scanUser.nextLine(); 
			loja = scanUser.nextLine(); // captura o nome da loja
			System.out.println("COD produto: ");
			cod = scanUser.nextInt(); // captura o codigo do produto
			
			// Atraves do COD e do nome da LOJA retornara o produto desejado.
			itemSelecionado = Busca.selecionaItem(itens, loja, cod);
			
			// Verifica se foi encontrado algum produto
			if (itemSelecionado != null) {
				System.out.println("Informe a quantidade do produto: ");
				int qtd = scanUser.nextInt();
				scanUser.nextLine();
				// Insere o produto no carrinho
				cart.add(new ItemCarrinho(itemSelecionado,qtd));
				System.out.println("Produto adicionado ao Carrinho");
			}
			else {
				System.out.println("Ocorreu algum erro tente de novo.");
			}
			return true;
		}
		scanUser.nextLine();
		return false;
    }
    
    private static void printCart(List<ItemCarrinho> cart) {
    	System.out.println("CARRINHO");
		for (ItemCarrinho it : cart) {
    		System.out.println(it.toString());
    	}
    }
    
    // Função com o objtivo de pular linha
    private static void pularLinha(int x) {
    	for(int i = 0; i < x; i++) {
    		System.out.println();
    	}
    }
}
	
