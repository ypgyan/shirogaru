package modelStore;

import java.util.Comparator;

public class ComparadorCodigoPreco implements Comparator<Item>{

	@Override
	public int compare(Item it1, Item it2) {
		ComparadorCodigo cc = new ComparadorCodigo();
		ComparadorPreco cp = new ComparadorPreco();
		
		int compara = cc.compare(it1, it2);
		
		//Similar codes then has to compare by the item price
		if (compara == 0) {
			compara = cp.compare(it1, it2);
		}
		
		return compara;
	}
}
