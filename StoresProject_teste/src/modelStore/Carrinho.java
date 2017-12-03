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

}
