package flameWars;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

	final public static int CLONE = 5;
	final public static int LAMB = 2;
	final public static int KING = 10;
	final public static int ACE = 1;
	final public static int BLACKSHEEP = 3;
	final public static int GOAT = 4;
	final public static int RAM = 6;
	final public static int WOOLY = 7;
	final public static int QUEEN = 9;
	final public static int JACK = 8;
	final public static boolean DEBUG = false;
	final public static int numberOfGames = 10000;

	public static void main(String[] args) throws FileNotFoundException {

		PrintWriter pw = new PrintWriter(new File("output.csv"));
		StringBuilder sb = new StringBuilder();
		Logger logger = new Logger(sb);

		
		for (int game = 0; game < numberOfGames; game++) {

			Player playerA = new Player("Player A", "*");
			Player playerB = new Player("Player B", ">");
			Player dealer;
			Player notDealer;
			Deck d = new Deck();

			// Randomly decide dealer
			Random r = new Random();
			if (r.nextInt(2) % 2 == 0) {
				dealer = playerA;
				notDealer = playerB;
			} else {
				dealer = playerB;
				notDealer = playerA;
			}

			// Deal
			for (int j = 0; j < 5; j++) {
				notDealer.drawStartingHand(d);
				dealer.drawStartingHand(d);
			}
			dealer.drawStartingHand(d); // Dealer starts with 6

			// Play starts with notDealer and continues until there is a winner
			Player currentPlayer = notDealer;
			Player idlePlayer = dealer;
			int turn = 1;
			while (playerA.getPointPileTotal() < playerA.getWinThreshold()
					&& playerB.getPointPileTotal() < playerB.getWinThreshold() && d.getPlayPileSize() > 0) {
				if (DEBUG)
					System.out.println(currentPlayer.specialCharacter + currentPlayer.specialCharacter
							+ currentPlayer.specialCharacter + " " + currentPlayer.name + " turn....");
				
				logger.startAddRow(game, currentPlayer, idlePlayer, d, turn, 0);
				currentPlayer.play(d, idlePlayer);
				logger.endAddRow(currentPlayer, idlePlayer);
				
				if (DEBUG)
					currentPlayer.print();
				if (DEBUG)
					if (d.getDiscardPileSize() > 0)
						d.print();

				if (currentPlayer.equals(dealer)) {
					currentPlayer = notDealer;
					idlePlayer = dealer;
				} else {
					currentPlayer = dealer;
					idlePlayer = notDealer;
				}

				turn++;
			}

			if (d.getPlayPileSize() <= 0)
			{
				System.out.println("Out of cards!");
				if(playerA.getPointPileTotal() == playerB.getPointPileTotal())System.out.println("We have a draw.");
				logger.startAddRow(game, currentPlayer, idlePlayer, d, turn-1, 2);
				logger.endAddRow(currentPlayer, idlePlayer);
			}
			

			if (playerA.getPointPileTotal() > playerA.getWinThreshold()) {
				System.out.println("Player A wins with this many points: " + playerA.getPointPileTotal());
				logger.startAddRow(game, playerA, playerB, d, turn - 1, 1);
				logger.endAddRow(playerA,  playerB);
			} else if (playerB.getPointPileTotal() > playerB.getWinThreshold()) {
				System.out.println("Player B wins with this many points: " + playerB.getPointPileTotal());
				logger.startAddRow(game, playerB, playerA, d, turn - 1, 1);
				logger.endAddRow(playerB,  playerA);
			} else if (playerA.getPointPileTotal() > playerB.getPointPileTotal()) {
				System.out.println("Player A wins with this many points: " + playerA.getPointPileTotal());
				logger.startAddRow(game, playerA, playerB, d, turn - 1, 1);
				logger.endAddRow(playerA,  playerB);
			} else if (playerB.getPointPileTotal() > playerA.getPointPileTotal()) {
				System.out.println("Player B wins with this many points: " + playerB.getPointPileTotal());
				logger.startAddRow(game, playerB, playerA, d, turn - 1, 1);
				logger.endAddRow(playerB,  playerA);
			}

		}

		pw.write(sb.toString());
		pw.close();

	}

}
