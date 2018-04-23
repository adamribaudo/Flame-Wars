package flameWars;

public class Logger {

	StringBuilder sb;
	
	public Logger(StringBuilder sb)
	{
		this.sb = sb;
		//Create columns
		sb.append("game");
        sb.append(',');
        sb.append("player");
        sb.append(',');
        sb.append("turn");
        sb.append(',');
        sb.append("pre_self_points");
        sb.append(',');
        sb.append("pre_self_threshold");
        sb.append(',');
        sb.append("pre_opp_points");
        sb.append(',');
        sb.append("pre_opp_threshold");
        sb.append(',');
        sb.append("cards_remaining");
        sb.append(',');
        sb.append("winning_move");
        sb.append(',');
        sb.append("self_hand_1");
        sb.append(',');
        sb.append("self_hand_2");
        sb.append(',');
        sb.append("self_hand_3");
        sb.append(',');
        sb.append("self_hand_4");
        sb.append(',');
        sb.append("self_hand_5");
        sb.append(',');
        sb.append("self_hand_6");
        sb.append(',');
        sb.append("self_hand_7");
        sb.append(',');
        sb.append("self_hand_8");
        sb.append(',');
        sb.append("self_hand_9");
        sb.append(',');
        sb.append("self_hand_10");
        sb.append(',');
        sb.append("self_effect_JACK");
        sb.append(',');
        sb.append("self_effect_QUEEN");
        sb.append(',');
        sb.append("self_effect_KING");
        sb.append(',');
        sb.append("opp_effect_JACK");
        sb.append(',');
        sb.append("opp_effect_QUEEN");
        sb.append(',');
        sb.append("opp_effect_KING");
        sb.append(',');        
        sb.append("self_point_6");
        sb.append(',');
        sb.append("self_point_7");
        sb.append(',');        
        sb.append("opp_point_6");
        sb.append(',');
        sb.append("opp_point_7");
        sb.append(',');
        sb.append("action");
        sb.append(',');
        sb.append("post_self_points");
        sb.append(',');
        sb.append("post_self_threshold");
        sb.append(',');
        sb.append("post_opp_points");
        sb.append(',');
        sb.append("post_opp_threshold");
        
        //TODO: top card known
        //TODO: jack stealing jack
        //TODO: valuable discard card
        //TODO: is dealer
       
        sb.append('\n');
	}
	
	public void startAddRow(int game, Player player, Player opp, Deck d, int turn, int winningMove)
	{
		sb.append(game); //Game
        sb.append(',');
        sb.append(player.name); //Player
        sb.append(',');
        sb.append(turn); //Turn
        sb.append(',');
        sb.append(player.getPointPileTotal()); //Points
        sb.append(',');
        sb.append(player.getWinThreshold()); //Threshold
        sb.append(',');
        sb.append(opp.getPointPileTotal()); //Points
        sb.append(',');
        sb.append(opp.getWinThreshold()); //Threshold
        sb.append(',');
        sb.append(d.getPlayPileSize()); //Cards remaining
        sb.append(',');
        sb.append(winningMove); //winning move
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.ACE)); //self 1
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.LAMB)); //self 2
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.BLACKSHEEP)); //self 3
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.GOAT)); //self 4
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.CLONE)); //self 5
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.RAM)); //self 6
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.WOOLY)); //self 7
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.JACK)); //self 8
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.QUEEN)); //self 9
        sb.append(',');
        sb.append(player.getCountOfCardInHand(Main.KING)); //self 10
        sb.append(',');
        sb.append(player.getCountOfCardInEffectPile(Main.JACK)); //self effect jack
        sb.append(',');
        sb.append(player.getCountOfCardInEffectPile(Main.QUEEN)); //self effect queen
        sb.append(',');
        sb.append(player.getCountOfCardInEffectPile(Main.KING)); //self effect king
        sb.append(',');
        sb.append(opp.getCountOfCardInEffectPile(Main.JACK)); //opp effect jack
        sb.append(',');
        sb.append(opp.getCountOfCardInEffectPile(Main.QUEEN)); //opp effect queen
        sb.append(',');
        sb.append(opp.getCountOfCardInEffectPile(Main.KING)); //opp effect king
        sb.append(',');        
        sb.append(player.getCountOfCardInPointPile(Main.RAM)); //self point 6
        sb.append(',');
        sb.append(player.getCountOfCardInPointPile(Main.WOOLY)); //self point 7
        sb.append(',');        
        sb.append(opp.getCountOfCardInPointPile(Main.RAM)); //opp point 6
        sb.append(',');
        sb.append(opp.getCountOfCardInPointPile(Main.WOOLY)); //opp point 7
	}
	
	public void endAddRow(Player player, Player opp)
	{
		sb.append(',');
		sb.append(player.getLastMove());
        sb.append(',');
        sb.append(player.getPointPileTotal());
        sb.append(',');
        sb.append(player.getWinThreshold());
        sb.append(',');
        sb.append(opp.getPointPileTotal());
        sb.append(',');
        sb.append(opp.getWinThreshold());
        
        sb.append('\n');
	}
}
