import java.util.ArrayList;
/**
 * 
 * @author Jacob Mackowiak
 * This class keeps data for the player including name, score and words used.
 *
 */

public class playerData {


	private String name;
	private int score;
	private ArrayList <String> wordsUsed = new ArrayList<String>();

	public playerData() {
		this("No Name");
		score = 0;
	}

	public playerData(String name) {
		this.name = name;
		score = 0;
	}


	public String toString() {
		String msg = "";
		msg += name + "\n";
		msg += "--------\n";
		msg += "Score: " + score + "\n";
		msg += "Words Used: " + wordsUsed + "\n";
		msg += "--------\n";
		return msg;
	}

	public String getName() {
		return name;
	}

	
	public int getScore() {
		return score;
	}

	public ArrayList<String> getWordsUsed(){
		return wordsUsed;
	}
	/**
	 * This method replaces a word that contains a blank tile with a new word 
	 * that has the missing letter.
	 * @param wordWithBlank
	 * @param newWord
	 */

	public void replaceWord(String wordWithBlank, String newWord) {
		int i = 0;

		for(String currentWord: this.wordsUsed) {
			if(currentWord.equals(wordWithBlank)) {
				this.wordsUsed.set(i, newWord);
			}
			i++;
		}
	}

	public void addPoints(int points) {

		//add to points to corresponding value
		int value = score;
		int newPoints = value + points;
		score = newPoints;
		System.out.println();


	}
	
	public void addWordUsed(String word) {
		
		this.wordsUsed.add(word);
	}
}


