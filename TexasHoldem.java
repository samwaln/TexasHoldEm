import java.util.Arrays;

public class TexasHoldem {

	private int round = 1;
	private Deck deck = new Deck(false);
	private Card[] playerOneHand = new Card[2];
	private Card[] playerTwoHand = new Card[2];
	private Card[] deal = new Card[5];
	private Chips playerOneChips = new Chips();
	private Chips playerTwoChips = new Chips();
	private int pot;
	private int endRound;
	private int currentBet;
	private int currentCompBet;
	private boolean winner;
	private boolean fold;
	private boolean compFold;
	private boolean check;
	private boolean compCheck;
	private boolean raise;
	private boolean compRaise;
	private boolean call;
	private boolean compCall;
	private Score player1Score = new Score();
	private Score player2Score = new Score();
	private int betRound = 0;
	
	public static void main(String[] args){
		TexasHoldem game = new TexasHoldem();
		game.deck.shuffle(false);
		game.playerOneChips.addChips(500);
		game.playerTwoChips.addChips(500);
		System.out.println("Let's play Texas Hold'em.");
		System.out.println("Rules: \nEach person receives two cards. Then three cards are laid in the middle.\nThis is followed by a fourth. "
				+ "Then, a fifth card is dealt. After this the person with the best hand (five of the seven cards) wins the round.\nYou will"
				+ " have the chance to bet after each set of cards is dealt. \nCheck: If you choose not to bet, enter 'check.' This "
				+ "can only be done if the computer has also checked. \nCall: You bet the same amount as the computer bet. \nRaise: If you choose"
				+ " to bet more than the previous person, you will be asked for the total amount you would like to raise. \nFold: If you"
				+ " do not want to continue playing with your hand, you can choose to fold. The round will be over if either player folds.");
		game.play(game, game.deck);
	}
	
	public void play(TexasHoldem game, Deck deck){
		while (playerOneChips.getChipCount()>0 && playerTwoChips.getChipCount()>0){
			deck = new Deck(false);
			deck.shuffle(false);
			playerOneHand = new Card[2];
			playerTwoHand = new Card[2];
			deal = new Card[5];
			System.out.println();
			System.out.println();
			System.out.println("Round: " + round);
			fold=false;
			compFold=false;
			winner=false;
			pot = 0;
			while (!winner){
				game.dealHand();
				System.out.println("Your hand: " + Arrays.toString(playerOneHand));
				System.out.println("You have " + playerOneChips.getChipCount() + " chips. The computer has " + playerTwoChips.getChipCount() + " chips. "
						+ "The pot has " + pot + " chips.");
				bet();
				if (winner){
					break;
				}
				System.out.println();
				System.out.println("Your hand: " + Arrays.toString(playerOneHand));
				game.dealFlop();
				System.out.println("You have " + playerOneChips.getChipCount() + " chips. The computer has " + playerTwoChips.getChipCount() + " chips. "
						+ "The pot has " + pot + " chips.");
				bet();
				if (winner){
					break;
				}
				System.out.println();
				System.out.println("Your hand: " + Arrays.toString(playerOneHand));
				game.dealTurn();
				System.out.println("You have " + playerOneChips.getChipCount() + " chips. The computer has " + playerTwoChips.getChipCount() + " chips. "
						+ "The pot has " + pot + " chips.");
				bet();
				if (winner){
					break;
				}
				System.out.println();
				System.out.println("Your hand: " + Arrays.toString(playerOneHand));
				game.dealRiver();
				System.out.println("You have " + playerOneChips.getChipCount() + " chips. The computer has " + playerTwoChips.getChipCount() + " chips. "
						+ "The pot has " + pot + " chips.");
				bet();
				winner = true;
			}
			endRound = determineWinner();
			System.out.println("Your hand: " + Arrays.toString(playerOneHand));
			System.out.println("The computer's hand: " + Arrays.toString(playerTwoHand));
			if (endRound==1){
				System.out.println("You won round " + round + ".");
				playerOneChips.addChips(pot);
			}
			else if (endRound==2){
				System.out.println("The computer won round " + round + ".");
				playerTwoChips.addChips(pot);
			}
			else {
				System.out.println("You tied. You split the pot.");
				playerOneChips.addChips(pot/2);
				playerTwoChips.addChips(pot/2);
			}
			round++;
		}
		if (playerOneChips.getChipCount()<=0){
			System.out.println("You lost. Game over.");
		}
		else {
			System.out.println("Congratulations! You won. Game over.");
		}
	}
	
	public void bet(){
		String bet = "";
		raise = false;
		compRaise = false;
		call = false;
		compCall = false;
		check = false;
		compCheck = false;
		int raiseBet = 0;
		betRound = 0;
		while (fold==false && check==false && call==false && compCall==false && playerOneChips.getChipCount()>0 && playerTwoChips.getChipCount()>0){
			if (raise){
				currentCompBet = currentBet;
				compCall = true;
			}
			else{
				currentCompBet=compBet();
			}
			if (betRound == 0){
				int firstBet = compBet();
				while (firstBet==1){
					firstBet = compBet();
				}
				currentCompBet = firstBet;	
				betRound++;
			}
			if (currentCompBet==0){
				compCheck = true;
				System.out.println("The computer checks.");
				currentBet = 0;
				compRaise = false;
			}
			else if (currentCompBet==1){
				if (playerTwoChips.getChipCount()<currentBet && playerTwoChips.getChipCount() > 0){
					System.out.println("The computer goes all in.");
					currentBet = playerTwoChips.getChipCount();
				}
				else {
				System.out.println("The computer calls.");
				playerTwoChips.removeChips(currentBet);
				compRaise = false;
				compCall = true;
				}
			}
			else if (currentCompBet==7){
				if (playerTwoChips.getChipCount()<0){
					break;
				}
				System.out.println("The computer bets " + currentCompBet + ". (all in)");
				playerTwoChips.removeChips(currentCompBet);
				compRaise = true;
			}
			else {
				if (playerTwoChips.getChipCount()<0){
					break;
				}
				if (!raise){
					System.out.println("The computer bets " + currentCompBet + ".");
				}
				else{
					System.out.println("The computer calls.");
				}
				playerTwoChips.removeChips(currentCompBet);
				compRaise = true;
			}
			pot += currentBet;
			if (!compCall){
				System.out.println("Would you like to raise, call, check, or fold?");
				bet = TextIO.getlnString();
				bet = bet.toLowerCase();
				if (currentBet>0){
					while (bet.equals("check")){
						System.out.println("You cannot check at this time. Enter another choice:");
						bet = TextIO.getlnString();
						bet = bet.toLowerCase();
					}
				}
				while (!bet.equals("raise") && !bet.equals("call") && !bet.equals("check") && !bet.equals("fold")){
					System.out.println("Invalid selection.");
					bet = TextIO.getlnString();
					bet = bet.toLowerCase();
				}
				switch(bet){
					case "raise": 
						System.out.println("How much would you like to bet? (This is the total amount bet, not just what you want to raise)");
						raiseBet = TextIO.getlnInt();
						if (playerOneChips.getChipCount() == currentBet){
							System.out.println("You cannot raise because you don't have more than the previous bet. You call.");
							raiseBet = currentBet;
						}
						else if (raiseBet == playerOneChips.getChipCount()){
							System.out.println("You go all in.");
							raise(raiseBet);
							if (playerTwoChips.getChipCount()>playerOneChips.getChipCount()){
								System.out.println("The computer calls.");
								playerTwoChips.removeChips(currentBet);
								pot += currentBet;
								compCall = true;
								break;
							}
							else {
								System.out.println("The computer also goes all in.");
								playerTwoChips.removeChips(playerTwoChips.getChipCount());
								pot += playerTwoChips.getChipCount();
								compCall = true;
								break;
							}				
						}
						else{
							while (raiseBet > playerOneChips.getChipCount()){
								System.out.println("You don't have that much money. You have " + playerOneChips.getChipCount() + ".");
								System.out.println("Enter a number less than or equal to your chip count.");
								raiseBet = TextIO.getlnInt();
							}
							while (raiseBet <= currentBet){
								System.out.println("Enter a number larger than the previous bet.");
								raiseBet = TextIO.getlnInt();
							}
						}
						raise(raiseBet);
						compCheck = false;
						check = false;
						break;
					case "call": 
						if (compCheck){
							System.out.println("You cannot call because the computer checked. You also check.");
							check = true;
						}
						else{
							call = true; 
							call();
						}
						break;
					case "check": 
						check = true; 
						break;
					case "fold": 
						fold = true;
						fold();
						break;
					default: 
						break;
				}
			}
		}
		return;
	}
	
	public int compBet(){
		int num = (int)(Math.random()*9);
		int bet = 0;
		switch(num){
			case 0: 
				break;
			case 1: 
				bet = 10; 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = bet;
				break;
			case 2: 
				bet = 20; 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = bet;
				break;
			case 3: 
				bet = 30; 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = bet; 
				break;
			case 4: 
				bet = 40; 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = bet; 
				break;
			case 5: 
				bet = 50; 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = bet; 
				break;
			case 6: 
				bet = 60; 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = bet;
				break;
			case 7: 
				bet = playerTwoChips.getChipCount(); 
				if (bet > playerTwoChips.getChipCount()){
					bet = playerTwoChips.getChipCount();
				}
				currentBet = playerTwoChips.getChipCount();
				break;
			default: 
				bet = 1;
		}
		return bet;
	}
	
	public void dealHand(){
		playerOneHand[0] = deck.dealCard();
		playerTwoHand[0] = deck.dealCard();
		playerOneHand[1] = deck.dealCard();
		playerTwoHand[1] = deck.dealCard();
	}
	
	public void dealFlop(){
		deal[0] = deck.dealCard();
		deal[1] = deck.dealCard();
		deal[2] = deck.dealCard();
		System.out.println("Flop: " + Arrays.toString(deal));
	}
	
	public void dealTurn(){
		deal[3] = deck.dealCard();
		System.out.println("Turn: " + Arrays.toString(deal));
	}
	
	public void dealRiver(){
		deal[4] = deck.dealCard();
		System.out.println("River: " + Arrays.toString(deal));
	}
	
	public void call(){
		if (currentBet > playerOneChips.getChipCount()){
			System.out.println("You go all in.");
			playerOneChips.removeChips(playerOneChips.getChipCount());
		}
		else{
			playerOneChips.removeChips(currentBet);
			pot += currentBet;
		}
	}
	
	public void raise(int betAmount){
		playerOneChips.removeChips(betAmount);
		pot+=betAmount;
		currentBet = betAmount - currentBet;
		raise = true;
	}
	
	public void fold(){
		winner = true;
	}
	
	public int determineWinner(){
		int winning = 0;
		if (fold==true){
			winning = 2;
		}
		else if (compFold==true){
			winning = 1;
		}
		else if (player1Score.points(playerOneHand, deal) > player2Score.points(playerTwoHand, deal)){
			winning = 1;
		}
		else if (player2Score.points(playerTwoHand, deal) > player1Score.points(playerOneHand, deal)){
			winning = 2;
		}
		else {
			winning = 3;
		}

		return winning;
	}
}
