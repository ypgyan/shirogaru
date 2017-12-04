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
    		
    		//
    	    //
    	    //Busca.todosItens(itens);
    	    //Busca.nomeLoja(itens, "submarino.com");
    	    //
    	    //Busca.codigoProduto(itens, 1003);
    	}
    	catch(FileNotFoundException e) {
    		System.out.println("Arquivo nao encontrado");
    	}
	     
		System.out.println("\nFinalizado");
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
        	
        	
        	System.out.println("Escolha uma opcao da busca desejada dos itens:");
			System.out.println("1 - Por Loja\n2 - Por Nome do produto\n3 - Por Tipo");

			opcao = Integer.parseInt(scanUser.nextLine());
			
        	pularLinha();
        	switch (opcao) {
        		case 1: //busca por loja
        			Busca.listarLojas(new ArrayList<>(lojas.values()));
        			System.out.println("Informe o nome da loja desejada: ");
        			opcao2 = scanUser.nextLine(); // Captura o nome desejada da loja
        			System.out.println();
        			Busca.nomeLoja(itens, opcao2);
        			pularLinha();
        			
        			break;
        		
        		case 2: //busca por nome
        			// Lista todos os produtos do sistema
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			String nomeProd = scanUser.nextLine();
        			Busca.nomeProduto(itens, nomeProd);
        			pularLinha();
        			tcgBuy(itens, cart);
        			System.out.println("olooooko");
        			
        			break;
        		
        		case 3: //busca por tipo
        			System.out.println("Escolha uma opcao do tipo desejado:");
        			System.out.println("1 - Eletronico\n2 - Item de Casa\n3 - Livro");
        			
        			opcao = Integer.parseInt(scanUser.nextLine());
        			pularLinha();
        			Busca.tipoProduto(itens, opcao);
        			pularLinha();
        			
        			break; 
        			
        			
        		default:
        			System.out.println("Nao existe essa busca");
        			
        	}
        	
        	
        } while (opcao > 0);
        
        scanUser.close();
        System.out.println("Finalizado!");
        
    }
    
    private static boolean tcgBuy(List<Item> itens, List<ItemCarrinho> cart) {	
    	Item itemSelecionado;
    	int decision = 0;
    	int cod = 0;
    	String loja = "";
    	
    	while(decision < 1 || decision > 2) {
    		System.out.println("Deseja informar um item para compra ou iniciar outra busca? \n 1 - yes \n 2 - no");
        	decision = scanUser.nextInt();
    	}
    	
		if (decision == 1) {
			
			System.out.println("Informe o nome da loja e o codigo do produto: ");
			System.out.println("Loja: ");
			scanUser.nextLine();
			loja = scanUser.nextLine();
			System.out.println("COD produto: ");
			cod = scanUser.nextInt();
			
			// Sera redirecionado para comprar o item.
			System.out.println("Processando compra");
			itemSelecionado = Busca.selecionaItem(itens, loja, cod);
			
			if (itemSelecionado != null) {
				System.out.println("Informe a quantidade do produto: ");
				int qtd = scanUser.nextInt();
				scanUser.nextLine();
				cart.add(new ItemCarrinho(itemSelecionado,qtd));
			}else{
				System.out.println("Ocorreu algum erro tente de novo.");
			};
			
			//scanUser.close();
			return true;
		}
		
		//scanUser.close();
		return false;
		
		
    }
    
    private static void pularLinha() {
    	for(int i = 0; i<3; i++) {
    		System.out.println();
    	}
    }
}
	
