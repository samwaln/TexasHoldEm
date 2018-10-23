
public class Chips {

	private int chips;
	
	public void addChips(int amount){
		chips += amount;
	}
	
	public void removeChips(int amount){
		chips -= amount;
	}
	
	public int getChipCount(){
		return chips;
	}
}
