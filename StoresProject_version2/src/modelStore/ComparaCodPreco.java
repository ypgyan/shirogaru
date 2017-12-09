package modelStore;

import java.util.Comparator;

public class ComparaCodPreco implements Comparator<Item> {

	@Override
	public int compare(Item item1, Item item2) {		
		int comp = item1.getProduto().getCodigo() - (item2.getProduto().getCodigo());
		
		if (comp == 0) {
			comp = (int) (item1.getPreco() - item2.getPreco());
		}
		return comp;
	}
	
	
}
