package flameWars;

import java.util.ArrayList;
import java.util.Random;

public class EffectPile {

	private ArrayList<Integer> effectCards;
	private ArrayList<Integer> stolenCards;

	public EffectPile() {
		effectCards = new ArrayList<Integer>();
		stolenCards = new ArrayList<Integer>();
	}

	public void addEffect(int card, int targetCard) {
		effectCards.add(0, card);

		if (card == Main.JACK && targetCard <= 0) {
			System.err.println("Must supply a target card when adding a Jack");
			return;
		}

		if (card == Main.JACK)
			stolenCards.add(targetCard);
	}

	public void removeEffect(int card, int stolenCard, Deck d) {
		if (effectCards.indexOf(card) == -1) {
			System.err.println("Card: " + card + " not found in effect pile");
			return;
		}

		if (card == Main.JACK) {
			if (stolenCards.indexOf(stolenCard) == -1) {
				System.err.println("Jack target not found in list of targets");
				return;
			}

			effectCards.remove(effectCards.indexOf(card));
			d.addToDiscard(card);
			stolenCards.remove(stolenCards.indexOf(stolenCard));
		} else {
			effectCards.remove(effectCards.indexOf(card));
			d.addToDiscard(card);
		}
	}

	public ArrayList<Integer> removeAllEffects(Deck d) {
		d.addToDiscard(effectCards);
		effectCards = new ArrayList<Integer>();
		ArrayList<Integer> jackTargetsClone = (ArrayList<Integer>) stolenCards.clone();

		stolenCards = new ArrayList<Integer>();

		return jackTargetsClone;
	}

	public int getNumQueens() {
		int counter = 0;
		for (Integer q : effectCards)
			if (q == Main.QUEEN)
				counter++;

		return counter;
	}

	public int getNumJacks() {
		int counter = 0;
		for (Integer j : effectCards)
			if (j == Main.JACK)
				counter++;

		return counter;
	}

	public int getNumKings() {
		int counter = 0;
		for (Integer k : effectCards)
			if (k == Main.KING)
				counter++;

		return counter;
	}

	public ArrayList<Integer> getStolenCards() {
		return stolenCards;
	}

	public int getSize() {
		return effectCards.size();
	}

	public void discardQueen(Deck d) {
		if (effectCards.indexOf(Main.QUEEN) == -1) {
			System.err.println("No queens to discard");
			return;
		}

		effectCards.remove(effectCards.indexOf(Main.QUEEN));
		d.addToDiscard(Main.QUEEN);
	}
	
	public void discardKing(Deck d) {
		if (effectCards.indexOf(Main.KING) == -1) {
			System.err.println("No kings to discard");
			return;
		}

		effectCards.remove(effectCards.indexOf(Main.KING));
		d.addToDiscard(Main.KING);
	}

	public int discardRandomEffect(Deck d) {
		Random r = new Random();
		int card = effectCards.get(r.nextInt(effectCards.size()));

		if (effectCards.indexOf(card) == -1) {
			System.err.println("Card " + card + " not found in effect pile");
			return -1;
		}

		int returnedCard = -1;
		if (card == Main.JACK) {
			if (stolenCards.size() == 0) {
				System.err.println("Discarding a JACK but there are no jackTargets");
				return -1;
			}
			int returnedCardIndex = r.nextInt(stolenCards.size());
			returnedCard = stolenCards.get(returnedCardIndex);
			stolenCards.remove(returnedCardIndex);
		}

		effectCards.remove(effectCards.indexOf(card));
		d.addToDiscard(card);

		return returnedCard;
	}

	public void print(String specialCharacter) {
		if (effectCards.size() > 0) {
			System.out.print(specialCharacter + " Effect pile contains: ");
			for (Integer i : effectCards)
				System.out.print(i + ", ");
			System.out.println();
		}

		if (stolenCards.size() > 0) {
			System.out.print(specialCharacter + " Stolen point cards are: ");
			for (Integer i : stolenCards)
				System.out.print(i + ", ");
			System.out.println();
		}

	}

}
