package modelStore;

import java.util.Comparator;

public class ComparadorCodigo implements Comparator<Item> {

	@Override
	public int compare(Item it1, Item it2) {
		return it1.getProduto().getCodigo().compareTo(it2.getProduto().getCodigo());
	}

}
