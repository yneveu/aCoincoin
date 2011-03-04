package fr.gabuzomeu.aCoincoin;

import java.util.Comparator;

public abstract class MessageTimeComparator implements Comparator{
	
		public  int compare( CoinCoinMessage a, CoinCoinMessage b){
		if (a.getTime() > b.getTime() )
			return 1;
		else if (a.getTime() < b.getTime())
			return -1;
		else
			return 0;
	
		}

	

}
