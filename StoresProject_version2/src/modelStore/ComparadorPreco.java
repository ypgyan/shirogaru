package modelStore;

import java.util.Comparator;

public class ComparadorPreco implements Comparator<Item> {

	@Override
	public int compare(Item it1, Item it2) {
		return (int) (it1.getPreco() - it2.getPreco());
	}

}
