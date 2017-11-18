package app;

import java.util.Scanner;
import java.io.IOException;
import modelStore.*;

public class MainApp {
	
	
    public static void main(String[] args) throws IOException  
    {
        Loja[] lojas = importarLojas(); //Pega todas as lojas do arquivo
        extrairProdutosLojas(lojas); //Pega os itens das correspondentes lojas
        
        
        //Menu para o usuário que chamar a classe aplicacao
        Scanner scanUser = new Scanner(System.in);
        opcoesTipoBusca();
        int opcao = scanUser.nextInt();
        int opcao2;
        
        //Faz o menu enquanto o opcao continuar ser menor que zero
        while (opcao > 0) {
        	Listagem.saltarLinhas(2);
        	switch (opcao) {
        		case 1: //busca por loja
        			opcoesLojas(lojas);
        			opcao2 = scanUser.nextInt() - 1; //Subtrai 1 para pegar a posicao correta
        			Listagem.saltarLinhas(2);
        			if (opcao2 < lojas.length && opcao2 >= 0)
        				Listagem.listarItensLoja(lojas[opcao2].getNome(), lojas[opcao2].getItens());
        			else {System.out.println("Opcao errada!"); }
        			break;
        		
        		case 2: //busca por nome
        			System.out.println("Insira o nome do produto que deseja buscar:");
        			scanUser.nextLine(); //Resolver o problema do \n
        			String nomeProd = scanUser.nextLine();
        			Listagem.saltarLinhas(2);
        			buscaItensNome(nomeProd, lojas);
        			break;
        		
        		case 3: //busca por tipo
        			opcoesTiposProd();
        			opcao2 = scanUser.nextInt();
        			Listagem.saltarLinhas(2);
        			buscaItensTipo(opcao2, lojas); 
        			break;
        			
        		default:
        			System.out.println("Nao existe essa busca");
        	}
        	
        	opcoesTipoBusca();
            opcao = scanUser.nextInt();
        }
        
        System.out.println("Finalizado!");
        scanUser.close();
    }


	/* Importa as lojas lidas do arquivo */
    public static Loja[] importarLojas() throws IOException  
    {
    	//Pega exatamente o diretório está o arquivo lojas.txt
    	Scanner scanLojas = new Scanner(MainApp.class.getResourceAsStream("/app/lojas.txt"));
    	
        //Lendo somente a primeira linha para saber o número de lojas
        final int nLojas = Integer.parseInt(scanLojas.nextLine());
        
        //Cria um vetor do número de lojas correspondentes
        Loja[] lojas = new Loja[nLojas];
        		
        int pos = 0;
        while (scanLojas.hasNextLine()) 
        {
        	String[] linha = scanLojas.nextLine().split(";");
        	
            String id = linha[0];
            String nome = linha[1];
            int avaliacao = Integer.parseInt(linha[2]);
            int qntItens = quantidadeItensLoja(id);
            
            //Instanciando as lojas
            lojas[pos++] = new Loja(id, nome, avaliacao, qntItens); 
        }
        scanLojas.close();
        
        return lojas;
    }

    /* Verifica a quantidade de itens fixos para cada loja, pois não é usado lista dinâmica e sim uma estática fixa */
    private static int quantidadeItensLoja(String idLoja) 
    {
    	Scanner scanProdutos = new Scanner(MainApp.class.getResourceAsStream("/app/produtos.txt"));
    	int qntIdLoja = 0; //Conta a quantidade de itens que a loja possui
    	
    	//Desconsiderar a primeira linha porque não entra nesse escopo
    	scanProdutos.nextLine();
    	
    	while (scanProdutos.hasNextLine()) {
    		String linha = scanProdutos.nextLine();
    		boolean idIgual = true;
    		
    		//Checa se os ids são iguais da loja fazendo isso por caracter. Caso ache um caracter diferente então são diferentes
    		for (int pos = 0; pos < idLoja.length(); pos++) {
    			if (linha.charAt(pos) != idLoja.charAt(pos)) {
    				idIgual = false;
    				break;
    			}
    			
    		}   		
    		//Caso idIgual seja verdadeiro então é um item a mais na loja 
    		if (idIgual)
    			qntIdLoja++;
    	}
    	scanProdutos.close();
    	
    	return qntIdLoja;
    }
    
    /* Importar os produtos do arquivo e colocá-los dentro das lojas correspondentes sem repetição de itens já criados */
    public static Produto[] extrairProdutosLojas(Loja[] lojas) throws IOException 
    {
    	Scanner scanProdutos = new Scanner(MainApp.class.getResourceAsStream("/app/produtos.txt"));
    	int nRegistros = scanProdutos.nextInt();
    	int qntProdutos = scanProdutos.nextInt();
    	scanProdutos.nextLine(); //Ler o resto da linha que é o \n para tratar erros
    	Produto[] produtos = new Produto[qntProdutos];
    	int posAtualProd = 0;
    	
    	//Faz o loop para o número de registros existentes e registra no vetor de produtos e nos itens da loja
    	for (int i = 0; i < nRegistros; i++) {
    		String[] linha = scanProdutos.nextLine().split(";"); //Ler o registro
    		
    		//Primeiro verificar se o produto já é existente e retorna a posição dele no vetor
    		int posExistencia = verificaProduto(linha[1], produtos);
    		
    		//Nao existe ainda
    		if (posExistencia == -1) {
    			//Identifica o produto pelo primeiro caractere
    			switch(linha[2].charAt(0)) {
    				case 'E': //Eletronico
    					produtos[posAtualProd] = new Eletronico(linha[3], linha[1], linha[5], linha[7], Double.parseDouble(linha[6]));
    					break;
    				
    				case 'L': //Livro
    					produtos[posAtualProd] = new Livro(linha[3], linha[1], linha[5], Integer.parseInt(linha[6]));
    					break;
    				
    				case 'I': //Item de casa
    					produtos[posAtualProd] = new ItemCasa(linha[3], linha[1], linha[5], linha[6], linha[7]);
    					break;
    				
    				default : //Nao existe
    					System.out.println("Nao existe esse item. Resolva o problema!");
    					System.out.println("Problema na linha " + (i+1));
    					System.exit(1);
    			}
    			posExistencia = posAtualProd; //Passa a existir na posição atual do vetor de produtos
    			posAtualProd++;	
    		}
			
    		
    		//Segundo colocar o produto junto com seu preco especifico na loja
    		for (int posLoja = 0; i < lojas.length; posLoja++) {
    			//Identificando a loja pelo id da loja
    			if (linha[0].equals(lojas[posLoja].getId())) {
    				double preco = Double.parseDouble(linha[4].replace(',', '.'));
    				lojas[posLoja].registraItem(produtos[posExistencia], preco);
    				break;
    			}	
    		}
    	}
    	scanProdutos.close();
    	
    	return produtos;
    }
 
    //Identifica em qual posicao se encontra o produto no vetor estático de produtos
	private static int verificaProduto(String codProd, Produto[] produtos) {
		for (int i = 0; produtos[i] != null && i < produtos.length; i++) {
			if (codProd.equals(produtos[i].getCodigo()))
				return i;
		}
		return -1;
	}
	
	private static void opcoesTipoBusca() {
		System.out.println("Escolha uma opcao da busca desejada dos itens:");
		System.out.println("1 - Por Loja\n2 - Por Nome do produto\n3 - Por Tipo");
	}
	
	private static void opcoesLojas(Loja[] lojas) {
		System.out.println("Escolha a loja:");
		for (int i = 0; i < lojas.length; i++) {
			System.out.println(i+1 + " - " + lojas[i].getNome());
		}
	}
	
	private static void buscaItensNome(String nomeProd, Loja[] lojas) {
		boolean cond = false;
		for (Loja l : lojas) {
			int pos = l.buscaNome(nomeProd); //retorna a posicao que se encontra o produto no vetor de itens
			
			if (pos == -1) //Caso nao ache o produto continua a busca nas outras lojas
				continue;
			
			cond = true;
			System.out.println(l.getNome());
			System.out.println(l.getItemPos(pos).toString());
		}
		
		if (!cond) //Caso nao ache o nome do produto
			System.out.println("Nao existe esse produto em nenhuma loja!");
	}
	
	private static void opcoesTiposProd() {
		System.out.println("Escolha uma opcao do tipo desejado:");
		System.out.println("1 - Eletronico\n2 - Livro\n3 - Item de Casa");
		}
	
	//Busca o item pelo tipo dele
	private static void buscaItensTipo(int opcaoTipoProd, Loja[] lojas) {
		if (opcaoTipoProd <= 0 || opcaoTipoProd > 3) {
			System.out.println("Opcao errada!");
			return;
		}
		
		for (Loja l : lojas) {
			for (Item i : l.getItens()) {
				
				//Verificando se a instancia do produto dentro do item eh igual da opcao escolhida pelo usuario no principal
				if (verificaInstanciaProd(i.getProduto()) == opcaoTipoProd) {
					System.out.println(l.getNome() + "\n" + i.toString());
				}
			}
		}
	}
	
	//Retorna um inteiro que indica se a instancia do determinado produto
	private static int verificaInstanciaProd(Produto p) {
		
		int identifica = (p instanceof Eletronico) ? 1 :
						 (p instanceof Livro) ? 2 : 3;
		
		//3 é instanceof ItemCasa
		
		return identifica;
	}
}

