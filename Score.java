import java.lang.reflect.Array;
import java.util.Arrays;

public class Score {
	
	private Card[] seven = new Card[7];
	private Card[] five = new Card[5];
	private int[] fiveValue = new int[5];
	private int score = 0;
	private int currentScore = 0;
	
	public int points(Card[] playerHand, Card[] deal){
		score = 0;
		return five(playerHand, deal);	
	}
	
	public int five(Card[] playerHand, Card[] deal){
		for (int i=0; i<5; i++){
			seven[i]=deal[i];
		}
		for (int i=0; i<2; i++){
			seven[i+5]=playerHand[i];
		}
		int l=0;
		for (int i=0; i<7; i++){
			for (int j=i+1; j<7; j++){
				l=0;
				for (int k=0; k<7; k++){
					if (k!=i && k!=j){
						five[l]=seven[k];
						l++;
					}
				}
				currentScore = handScore();
				if (currentScore > score){
					score = currentScore;
				}
			}
		}
		return score;
	}
	
	public int handScore(){
		for (int i=0; i<5; i++){
			fiveValue[i]=five[i].getValue();
		}
		for (int i=0; i<5; i++){
			if (fiveValue[i]==1){
				fiveValue[i]=14;
			}
		}
		Arrays.sort(fiveValue);
		int count = 0;
		int newCount = 0;
		int highCount = fiveValue[4];
		highCount = highCount*15 + fiveValue[3];
		highCount = highCount*15 + fiveValue[2]*15;
		highCount = highCount*15 + fiveValue[1]*15;
		highCount = highCount*15 + fiveValue[0]*15;
		int[] counts = new int[15];
		for (int i=2; i<15; i++){
			count = 0;
			for (int j=0; j<5; j++){
				if (five[j].getValue()==i)
					count++;
				counts[i]=count;
			}
		}
		for (int i=2; i<15; i++){
			//four of a kind
			if (counts[i]==4){
				newCount = 8000000 + i*10000;
				for (int k=4; k>=0; k--){
					if (fiveValue[k]!=i){
						newCount += k;
					}
				}
				if (newCount>highCount){
					highCount=newCount;
				}
			}
			//three of a kind
			else if (counts[i]==3){
				int highCards = 0;
				newCount = 4000000 + i*10000;
				for (int k=4; k>=0; k--){
					if (fiveValue[k]!=i){
						highCards = highCards*15 + fiveValue[k];
						break;
					}
				}
				newCount += highCards;
				if (newCount>highCount){
					highCount=newCount;
				}
			}
			//pair
			else if (counts[i]==2){
				int highCards = 0;
				newCount = 2000000 + i*10000;
				for (int k=4; k>=0; k--){
					if (fiveValue[k]!=i){
						highCards = highCards*15 + fiveValue[k];
					}
				}
				newCount += highCards;
				if (newCount>highCount){
					highCount=newCount;
				}
			}
			else{
				continue;
			}
		}
		//two pair
		for (int i=2; i<15; i++){
			for (int j=i+1; j<15; j++){
				if (counts[i]==2 && counts[j]==2){
					highCount = 3000000;
					highCount += j*10000;
					highCount += i*100;
					for (int k=4; k>=0; k--){
						if (fiveValue[k]!=i && fiveValue[k]!=j){
							highCount += fiveValue[k];
						}
					}
				}
				else{
					continue;
				}
			}
		}
		//straight
		if (fiveValue[4]-fiveValue[3]==1 && fiveValue[3]-fiveValue[2]==1 && fiveValue[2]-fiveValue[1]==1 
				&& fiveValue[1]-fiveValue[0]==1){
			highCount=5000000;
			highCount += fiveValue[4]*10000;
		}
		//straight ace low
		for (int i=0; i<5; i++){
			if (fiveValue[3]==5 && fiveValue[2]==4 && fiveValue[1]==3 && fiveValue[0]== 2 && fiveValue[4]==14){
				highCount=5000000;
				highCount += fiveValue[3]*10000;
			}
		}
		//flush
		if (five[0].getSuit() == five[1].getSuit() && five[1].getSuit() == five[2].getSuit() && five[2].getSuit() == five[3].getSuit()
				&& five[3].getSuit() == five[4].getSuit()){
			highCount=6000000;
			highCount += fiveValue[4]*10000;
		}
		//full house
		for (int i=2; i<15; i++){
			for (int j=i+1; j<15; j++){
				if (counts[i]==3 && counts[j]==2){
					highCount = 7000000;
					highCount += j*10000;
				}
				else if (counts[i]==2 && counts[j]==3){
					highCount = 7000000;
					highCount += j*10000;
				}
				else{
					continue;
				}
			}
		}
		//straight flush
		for (int i=0; i<5; i++){
			if (fiveValue[4]-fiveValue[3]==1 && fiveValue[3]-fiveValue[2]==1 && fiveValue[2]-fiveValue[1]==1 
					&& fiveValue[1]-fiveValue[0]==1){
				if (five[0].getSuit() == five[1].getSuit() && five[1].getSuit() == five[2].getSuit() && five[2].getSuit() == five[3].getSuit()
				&& five[3].getSuit() == five[4].getSuit()){
					highCount = 9000000;
					highCount += fiveValue[4];
				}
			}
		}
		//straight flush ace low
		for (int i=0; i<5; i++){
			if (fiveValue[3]==5 && fiveValue[2]==4 && fiveValue[1]==3 && fiveValue[0]== 2 && fiveValue[4]==14){
				if (five[0].getSuit() == five[1].getSuit() && five[1].getSuit() == five[2].getSuit() && five[2].getSuit() == five[3].getSuit()
				&& five[3].getSuit() == five[4].getSuit()){
					highCount = 9000000;
					highCount += fiveValue[4];
				}
			}
		}
		//royal flush
		for (int i=0; i<5; i++){
			Arrays.sort(fiveValue);
			if (five[4].getValue()==14 && five[3].getValue()==13 && five[2].getValue()==12 
					&& five[1].getValue()==11 && five[0].getValue()==10){
				if (five[0].getSuit() == 0 && five[1].getSuit() == 0 && five[2].getSuit() == 0 && five[3].getSuit() == 0 
						&& five[4].getSuit() == 0){
					highCount = 10000000;
				}
			}
		}
		return highCount;
	}
	
}
