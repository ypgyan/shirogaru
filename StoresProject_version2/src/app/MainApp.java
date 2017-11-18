package app;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import modelStore.*; 

public class MainApp {
	
	
    public static void main(String[] args)  
    {
    	try {
    		//Get all stores from the folder
    		Map<String, Loja> lojas = importarLojas();
    		//Get all items from the folder
            List<Item> itens = extrairItens(lojas);
            
            //Busca.listarLojas(new ArrayList<>(lojas.values()));
            //Busca.todosItens(itens);
            //Busca.nomeProduto(itens, "MOTO");
            //Busca.nomeLoja(itens, IdentificadorLoja.RICELETRO);
            //Busca.tipoProduto(itens, TipoProduto.ITEMCASA);
            Busca.codigoProduto(itens, "1001");
            
    	}
    	catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	finally {
    		
    		
			System.out.println("\nFinalizado");
		}
        
    }

    
    /* Import stores read from file */
    public static Map<String, Loja> importarLojas() throws FileNotFoundException  
    {
    	//String pathArquivo = ClassLoader.getSystemResource("lojas.txt").getPath();
    	//File arqInput = new File(pathArquivo);
    	Scanner scanLojas = new Scanner(MainApp.class.getResourceAsStream("lojas.txt"));
    	
    	
    	//Instance a map structure to save the stores, where the key is String type
        Map<String, Loja> lojas = new HashMap<>();
        		
        scanLojas.nextLine(); //Ignore the first line of the file
        
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
    public static List<Item> extrairItens(Map<String, Loja> lojas) throws FileNotFoundException
    {
    	//String pathArquivo = ClassLoader.getSystemResource("produtos.txt").getPath();
    	//File arqInput = new File(pathArquivo);
    	Scanner scanProdutos = new Scanner(MainApp.class.getResourceAsStream("produtos.txt"));
    	
    	int nRegistros = scanProdutos.nextInt();
    	scanProdutos.nextLine(); //Ler o resto da linha que é o \n para tratar erros
    	
    	Map<String, Produto> produtos = new HashMap<>();
    	List<Item> itens = new ArrayList<>();
    	
    	//Loops according the numbers of products registrations
    	for (int i = 0; i < nRegistros; i++) {
    		String[] linha = scanProdutos.nextLine().split(";"); //Read the register/record 
    		String cod = linha[1], nome = linha[3];
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
    					System.out.println("Problema na linha " + (i+1));
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

	
}
	
