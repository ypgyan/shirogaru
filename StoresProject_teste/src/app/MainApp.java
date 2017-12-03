package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    		Map<String, Loja> lojas = importarLojas("Database/lojas.txt");
    		
    		//Get all items from the folder
            List<Item> itens = extrairItens(lojas, "Database/produtos.txt");
            
            Busca.listarLojas(new ArrayList<>(lojas.values()));
            Busca.todosItens(itens);
            //Busca.nomeProduto(itens, "x");
            //Busca.nomeLoja(itens, "submarino.com");
            //Busca.tipoProduto(itens, TipoProduto.ITEMCASA);
            //Busca.codigoProduto(itens, 1003);

    	}
    	catch(IOException e) {
    		System.out.println("Arquivo nao encontrado");
    	}
		
		    		
		System.out.println("\nFinalizado");
    }

    
    /* Import stores read from file */
    public static Map<String, Loja> importarLojas(String nomeArq) throws IOException
    {
    	BufferedReader scanLojas = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArq), "UTF8")); 

    	//Instance a map structure to save the stores, where the key is String type
        Map<String, Loja> lojas = new HashMap<>();
        
        String linha = scanLojas.readLine();
        while (linha != null) 
        {
        	String[] dados = linha.split(";");
        	
            String id = dados[0];
            String nome = dados[1];
            int avaliacao = Integer.parseInt(dados[2]);
            
            //Store instance
            lojas.put(id, new Loja(id, nome, avaliacao));
            
            linha = scanLojas.readLine();
        }
        
        scanLojas.close();
        
        return lojas;
    }


    /* Import the products from the file and put all of them on respective items */
    public static List<Item> extrairItens(Map<String, Loja> lojas, String nomeArq) throws IOException
    {
    	BufferedReader scanProdutos = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArq), "UTF8"));
    	
    	Map<Integer, Produto> produtos = new HashMap<>();
    	List<Item> itens = new ArrayList<>();
    	int countLine = 0;
    	
    	String linha = scanProdutos.readLine(); 
    	
    	//Loops according the numbers of products registrations
    	while (linha != null) {
    		countLine++;
    		String[] dados = linha.split(";"); //Read the register/record 
    		int cod = Integer.parseInt(dados[1]); 
    		String nome = dados[3];
    		String tipoProduto = dados[2];
    		String Idloja = dados[0];
    		double preco = Double.parseDouble(dados[5].replace(',', '.'));
    		int quantidade = Integer.parseInt(dados[4]);
    		
    		//Checking the existence of the product in the Map Collection
    		if (!produtos.containsKey(cod)) {
    			Produto pNew = null;
    			
    			//Identify the product to statement the correct object product
    			switch (tipoProduto) {
    				case "Eletronico":
    					pNew = new Eletronico(nome, cod, dados[6], dados[8], Double.parseDouble(dados[7].replace(',', '.')));
    					break;
    				
    				case "ItemCasa":
    					pNew = new ItemCasa(nome, cod, dados[6], dados[7], dados[8]);
    					break;
    				
    				case "Livro":
    					pNew = new Livro(nome, cod, dados[6], Integer.parseInt(dados[7]));
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
    		
    		linha = scanProdutos.readLine();
    	}		
    	
    	scanProdutos.close();
    	return itens;
    }

	
}
	
