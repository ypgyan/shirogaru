package modelStore;

import java.util.List;
import java.util.Date;

public class Carrinho {
	/* Atributos */
	private List<ItemCarrinho> itensCarrinho;
	private Date dataHora;	
	
	/* Construtor */
	public Carrinho(List<ItemCarrinho> itensCarrinho, Date dataHora) {
		this.itensCarrinho = itensCarrinho;
		this.dataHora = dataHora;
	}
	
	/* Metodos */
	public List<ItemCarrinho> getItensCarrinho() {
		return this.itensCarrinho;
	}
	
	public Date getDataHora() {
		return this.dataHora;
	}
	
	@Override
	public String toString() {
		String itensCompra = "";
		
		for (ItemCarrinho itc : this.itensCarrinho) {
			itensCompra += itc.toString() + '\n';
		}
		
		if (itensCompra != "") {
			itensCompra += "Data e horário da compra: " + this.dataHora; 
		}
		
		return itensCompra;
	}
	
	//Calcula o preço total dos itens no carrinho da compra realizada
	public double precoTotal() {
		// TODO
		double preco = 0;
		
		return preco;
	}
}
