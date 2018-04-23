package flameWars;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Deck {
	
	private ArrayList<Integer> playPile;
	private ArrayList<Integer> discardPile;
	
	public Deck()
	{
		playPile = new ArrayList<Integer>();
		//Add cards to the pile. 1=Ace, 10=King
		for(int i =1;i<=7;i++)
			for(int j=0;j<3;j++)
				playPile.add(i);
		for(int i =8;i<=10;i++)
			for(int j=0;j<4;j++)
				playPile.add(i);
		
		//Shuffle playPile
		Collections.shuffle(playPile);
		
		discardPile = new ArrayList<Integer>();
	}
	
	public int draw()
	{
		int topCard = playPile.get(0);
		playPile.remove(0);
		return topCard;
	}
	
	public void addToDiscard(int card)
	{
		discardPile.add(0, card);
	}
	
	public void addToDiscard(ArrayList<Integer> cards)
	{
		discardPile.addAll(cards);
	}
	
	public void addToPlayPile(int card)
	{
		playPile.add(0, card);
	}
	
	public int getPlayPileSize()
	{
		return playPile.size();
	}
	
	public int getDiscardPileSize()
	{
		return discardPile.size();
	}
	
	public int pullRandomDiscard()
	{
		Random r = new Random();
		int randomIndex = r.nextInt(discardPile.size());
		int discardCard = discardPile.get(randomIndex);
		discardPile.remove(randomIndex);
		return discardCard;
	}
	
	public void print()
	{
		System.out.print("Discard pile contains: ");
		for(Integer i : this.discardPile)
			System.out.print(i + ", ");
		System.out.println("");
	}

}
