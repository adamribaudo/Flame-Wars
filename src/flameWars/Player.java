package flameWars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {

	public String name;
	private ArrayList<Integer> hand;
	private ArrayList<Integer> pointPile;
	public EffectPile effectPile;
	private final int POINTS = 0;
	private final int ACTION = 1;
	private int intention;
	private Action lastAction;
	Random r;
	public String specialCharacter;

	public Player(String name, String specialCharacter) {
		this.name = name;
		hand = new ArrayList<Integer>();
		pointPile = new ArrayList<Integer>();
		effectPile = new EffectPile();
		r = new Random();
		intention = r.nextInt(2);
		this.specialCharacter = specialCharacter;
	}

	public void drawStartingHand(Deck d) {
		hand.add(d.draw());
	}

	public void play(Deck d, Player opponent) {
		//Randomly decide to draw but only if hand size is < 5
		if (hand.size() < 5 && (r.nextBoolean() || hand.size() == 0)) {
			if (Main.DEBUG)
				System.out.println(specialCharacter + " " + this.name + " draws");
			if(d.getPlayPileSize() > 0)hand.add(d.draw());
			lastAction = Action.DRAW;
		} else {
			playCard(hand.get(r.nextInt(hand.size())), d, opponent);
		}
	}

	private void playPointCard(int card) {
		if (Main.DEBUG)
			System.out.println(specialCharacter + " Playing " + card + " for points");
		hand.remove(hand.indexOf(card));
		pointPile.add(0, card);
		switch (card) {
		case 1:
			lastAction = Action.POINT_1;
			break;
		case 2:
			lastAction = Action.POINT_2;
			break;
		case 3:
			lastAction = Action.POINT_3;
			break;
		case 4:
			lastAction = Action.POINT_4;
			break;
		case 5:
			lastAction = Action.POINT_5;
			break;
		case 6:
			lastAction = Action.POINT_6;
			break;
		case 7:
			lastAction = Action.POINT_7;
			break;
		default:
			System.err.println("invalid point card being played");
		}
	}

	private void playCard(int card, Deck d, Player opponent) {
		ArrayList<Integer> tempHand;
		// Set the intention to either points or action every time you play a
		// card
		intention = r.nextInt(2);
 
		switch (card) {
		case Main.ACE:
			if (intention == this.ACTION && opponent.getPointPileTotal() > 0) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing ACE as action");
				discardFromHand(d, card);
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					this.play(d, opponent);
				} else {
					this.discardPointPile(d);
					opponent.discardPointPile(d);
					lastAction = Action.ACTION_1;
				}
			} else
				playPointCard(Main.ACE);
			break;
		case Main.CLONE:
			if (intention == this.ACTION && d.getPlayPileSize() > 0) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing CLONE as action");
				discardFromHand(d, card);
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					this.play(d, opponent);
				} else {
					tempHand = new ArrayList<Integer>();
					for (int i = 0; i < Math.min(3, d.getPlayPileSize()); i++)
						tempHand.add(d.draw());
					Collections.shuffle(tempHand);

					for (int i = 0; i < tempHand.size() - 1; i++)
						this.hand.add(tempHand.get(i));

					if (tempHand.size() == 3)
						d.addToPlayPile(tempHand.get(2));
					lastAction = Action.ACTION_5;
				}
			} else
				playPointCard(Main.CLONE);
			break;
		case Main.KING:
			if (Main.DEBUG)
				System.out.println(specialCharacter + " playing KING as effect");
			if (opponent.counterAction(d, card, this)) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Opponent countered. Play again.");
				discardFromHand(d, card);
				this.play(d, opponent);
			} else {
				hand.remove(hand.indexOf(Main.KING));
				effectPile.addEffect(Main.KING, -1);
				lastAction = Action.EFFECT_KING;
			}
			break;
		case Main.QUEEN:
			if (r.nextBoolean() && opponent.hasKingInEffectPile()) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing QUEEN as scuttle");
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					discardFromHand(d, card);
					this.play(d, opponent);
				} else {
					this.discardFromHand(d, card);
					opponent.scuttleKing(d);
					lastAction = Action.SCUTTLE_QUEEN;
				}
			} else {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing QUEEN as effect");
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					discardFromHand(d, card);
					this.play(d, opponent);
				} else {
					hand.remove(hand.indexOf(card));
					effectPile.addEffect(card, -1);
					lastAction = Action.EFFECT_QUEEN;
				}
			}
			break;
		case Main.LAMB:
			if (intention == this.ACTION && (d.getPlayPileSize() > 0 || d.getDiscardPileSize() > 0)) {
				discardFromHand(d, card);
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					this.play(d, opponent);
				} else {
					if (d.getDiscardPileSize() == 1) {
						// Discard pile only contains your LAMB, play top card
						int topCard = d.draw();
						hand.add(topCard);
						if (Main.DEBUG)
							System.out.println(
									specialCharacter + " Playing LAMB and playing top card which is " + topCard);
						playCard(topCard, d, opponent);
						lastAction = Action.ACTION_2_PLAY_TOP;
					} else if (d.getPlayPileSize() == 0) {
						if (Main.DEBUG)
							System.out.println(
									specialCharacter + " Playing LAMB and adding a random discard card to hand");
						// Pull from discard
						hand.add(d.pullRandomDiscard());
						lastAction = Action.ACTION_2_PULL_DISCARD;
					} else
					// choose randomly
					if (r.nextBoolean()) {
						if (Main.DEBUG)
							System.out.println(
									specialCharacter + " Playing LAMB and adding a random discard card to hand");
						// Pull from discard
						hand.add(d.pullRandomDiscard());
						lastAction = Action.ACTION_2_PULL_DISCARD;
					} else {
						// Play top card
						int topCard = d.draw();
						hand.add(topCard);
						if (Main.DEBUG)
							System.out.println(
									specialCharacter + " Playing LAMB and playing top card which is " + topCard);
						playCard(topCard, d, opponent);
						lastAction = Action.ACTION_2_PLAY_TOP;
					}
				}
			} else
				playPointCard(Main.LAMB);
			break;
		case Main.BLACKSHEEP:
			if (intention == this.ACTION && opponent.getEffectPileSize() > 0) {
				discardFromHand(d, card);
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing BLACKSHEEP as action");
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					this.play(d, opponent);
				} else {
					this.discardEffectPile(d);
					// As the opponent discards all effects, return any cards
					// targeted by Jacks
					pointPile.addAll(opponent.discardEffectPile(d));
					lastAction = Action.ACTION_3;
				}
			} else
				playPointCard(card);
			break;
		case Main.GOAT:
			if (intention == this.ACTION && opponent.getHandSize() > 0) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing GOAT as action");
				discardFromHand(d, card);
				if (opponent.counterAction(d, card, this)) {
					// Opponent countered, so play again
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					this.play(d, opponent);
				} else {
					opponent.discardRandomCards(d, 2);
					lastAction = Action.ACTION_4;
				}
			} else
				playPointCard(card);
			break;
		case Main.RAM:
			if (intention == this.ACTION && opponent.getEffectPileSize() > 0 && opponent.numQueensInEffectPile() < 2) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing RAM as action");
				discardFromHand(d, card);
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					this.play(d, opponent);
				} else {
					int stolenCard = opponent.discardRandomFromEffectPileCheckQueen(d);
					if (stolenCard > 0) {
						if (Main.DEBUG)
							System.out.println(
									specialCharacter + " returning stolen card " + stolenCard + " to this player");
						pointPile.add(stolenCard);
					}
					lastAction = Action.ACTION_6;
				}
			} else
				playPointCard(card);
			break;
		case Main.JACK:
			if (r.nextBoolean() && (opponent.hasQueenInEffectPile() || opponent.hasKingInEffectPile())) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing JACK as scuttle");
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					discardFromHand(d, card);
					this.play(d, opponent);
				} else {
					discardFromHand(d, card);
					opponent.randomlyScuttleQueenOrKing(d);
					lastAction = Action.SCUTTLE_JACK;
				}
			} else if (opponent.getPointPileTotal() > 0 && !opponent.hasQueenInEffectPile()) {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Playing JACK as effect");
				if (opponent.counterAction(d, card, this)) {
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Opponent countered. Play again.");
					discardFromHand(d, card);
					this.play(d, opponent);
				} else {
					hand.remove(hand.indexOf(card));
					int stolenCard = opponent.giveHighestPointCard();
					effectPile.addEffect(card, stolenCard);
					pointPile.add(stolenCard);
					if (Main.DEBUG)
						System.out.println(specialCharacter + " Stole " + stolenCard);
					lastAction = Action.EFFECT_JACK;
				}
			} else {
				if (Main.DEBUG)
					System.out.println(specialCharacter + " Cannot play Jack. Playing again");
				this.play(d, opponent);
			}
			break;
		case 7:
			playPointCard(card);
			break;
		default:
			System.err.println("Attempting to play a card without a case: " + card);
			break;
		}
	}

	public void randomlyScuttleQueenOrKing(Deck d) {
		if (this.hasKingInEffectPile() && this.hasQueenInEffectPile()) {
			// Randommly discard 1 or the other
			if (r.nextBoolean())
				effectPile.discardQueen(d);
			else
				effectPile.discardKing(d);
		} else if (this.hasKingInEffectPile()) {
			effectPile.discardKing(d);
		} else if (this.hasQueenInEffectPile()) {
			effectPile.discardQueen(d);
		} else
			System.err.println("Could not find a King or Queen to scuttle");
	}

	public void scuttleKing(Deck d) {
		if (this.hasKingInEffectPile())
			effectPile.discardKing(d);
		else
			System.err.println("Could not find a King to scuttle");
	}

	public boolean hasQueenInEffectPile() {
		return effectPile.getNumQueens() > 0;
	}

	public boolean hasKingInEffectPile() {
		return effectPile.getNumKings() > 0;
	}

	public boolean hasJackInEffectPile() {
		return effectPile.getNumJacks() > 0;
	}

	public int giveHighestPointCard() {
		int card;
		if (pointPile.size() == 0) {
			System.err.println("No point cards to hand over");
			return -1;
		} else {
			Collections.sort(pointPile);
			card = pointPile.get(pointPile.size() - 1);
			pointPile.remove(pointPile.size() - 1);
			return card;
		}
	}

	public int getWinThreshold() {
		return 15 - (5 * effectPile.getNumKings());
	}

	public boolean counterAction(Deck d, int actionCard, Player opponent) {
		if (hand.contains(Main.RAM)) {
			if (r.nextBoolean()) {
				this.discardFromHand(d, Main.RAM);
				// Opponent can counter the counter
				return !opponent.counterAction(d, Main.RAM, this);
			}
			return false;
		} else
			return false;
	}

	public int getHandSize() {
		return hand.size();
	}

	public int getPointPileTotal() {
		int total = 0;
		for (Integer i : pointPile)
			total += i;
		return total;
	}

	public int getEffectPileSize() {
		return effectPile.getSize();
	}

	public int numQueensInEffectPile() {
		return effectPile.getNumQueens();
	}

	public int discardRandomFromEffectPileCheckQueen(Deck d) {
		int stolenCard = -1;
		if (effectPile.getNumQueens() == 1) {
			// If there is only 1 queen, target that queen
			effectPile.discardQueen(d);
		} else if (effectPile.getNumQueens() == 0) {
			// Discard random effect which could include a Jack
			stolenCard = effectPile.discardRandomEffect(d);
			// If we discarded a Jack, remove the returned card from player's
			// point pile and return it
			if (stolenCard > 0)
			{
				int stolenCardIndex = pointPile.indexOf(stolenCard); //Do we still have this stolen card? It may have been stolen back
				if(stolenCardIndex > 0)
					pointPile.remove(stolenCardIndex);
			}
		} else {
			System.err.println("Cannot target an effect to discard with more than 1 Queen");
		}

		return stolenCard;
	}

	public void discardRandomCards(Deck d, int numCards) {
		int randomIndex;
		for (int i = 0; i < numCards; i++) {
			if (hand.size() > 0) {
				randomIndex = r.nextInt(hand.size());
				d.addToDiscard(hand.get(randomIndex));
				hand.remove(randomIndex);
			}
		}

	}

	public void discardPointPile(Deck d) {
		d.addToDiscard(pointPile);
		pointPile = new ArrayList<Integer>();
	}

	public ArrayList<Integer> discardEffectPile(Deck d) {
		ArrayList<Integer> stolenCards = effectPile.removeAllEffects(d);
		// Remove any stolen cards from this player's point pile
		for (Integer i : stolenCards)
			pointPile.remove(i);
		effectPile = new EffectPile();
		return stolenCards;
	}

	public void print() {
		if (pointPile.size() > 0) {
			if (Main.DEBUG)
				System.out.println(specialCharacter + " " + this.name + " has " + this.getPointPileTotal() + " points");
			if (Main.DEBUG)
				System.out.print(specialCharacter + " " + this.name + " point Pile contains: ");
			for (Integer i : this.pointPile) {
				if (Main.DEBUG)
					System.out.print(i + ", ");
			}
			if (Main.DEBUG)
				System.out.println();
		}

		if (Main.DEBUG)
			System.out.print(specialCharacter + " " + this.name + " hand contains: ");
		for (Integer i : this.hand) {
			if (Main.DEBUG)
				System.out.print(i + ", ");
		}
		if (Main.DEBUG)
			System.out.println();

		effectPile.print(specialCharacter);
	}

	private void discardFromHand(Deck d, int card) {
		hand.remove(hand.indexOf(card));
		d.addToDiscard(card);
	}

	public Action getLastMove() {
		return lastAction;
	}

	public int getCountOfCardInHand(int card) {
		int counter = 0;
		for (Integer c : hand)
			if (c == card)
				counter++;

		return counter;
	}
	
	public int getCountOfCardInPointPile(int card) {
		int counter = 0;
		for (Integer c : pointPile)
			if (c == card)
				counter++;

		return counter;
	}

	public int getCountOfCardInEffectPile(int card) {
		switch (card) {
		case Main.JACK:
			return effectPile.getNumJacks();
		case Main.QUEEN:
			return effectPile.getNumQueens();
		case Main.KING:
			return effectPile.getNumKings();
		default:
			System.err.println("Invalid card specified for counting effect cards " + card);
			return -1;

		}
	}

}
