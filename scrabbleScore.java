import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
/**
 * 
 * @author Jacob Mackowiak
 * 
 * This program is made to keep score when playing the game "Scrabble". 
 * There is a menu option to add player data including points scored and words used,
 * add player data, printing out how many of each letter are left, you can substitute a word 
 * that was replaced with a blank tile, and you can print the score board.
 *
 */
public class scrabbleScore {

	public static void main(String[] args)  {

		ArrayList<playerData> roster = new ArrayList<playerData>(); // creates array list to store players
		TreeMap<Character, Integer> letterBank = new TreeMap<Character, Integer>(); // creates hash map to store letter bank
		Scanner input = new Scanner(System.in); // Scanner input

		letterBank(letterBank); // create the letter bank
		getPlayers(input, roster); //get players first
		System.out.println();

		boolean exit = false;

		while(!exit) {

			int menuOption = menuOption(input); // prints menu and gets choice from user
			runMenuOption(input, menuOption, roster, letterBank); // runs choice from user

			if(menuOption == 6) {
				exit = true; // exits the game
			}
		}

		input.close();
	}

	public static void addPoints(Scanner input, ArrayList<playerData> roster) {

		boolean exit = false;

		while(!exit) {
			System.out.println("Enter name: ");
			String playerName = input.next();

			// check if name entered is an object in roster
			boolean playerExists = playerExists(playerName, roster);
			if(!playerExists) {
				System.out.println("Name is not in roster");
				continue;
			}
			else { 

				// get points from user
				System.out.println("How many points would you like to add?");
				int points = input.nextInt();
				playerData player = findSpecificPlayer(playerName, roster); // finds the correct player to add points
				player.addPoints(points);

				// prints roster data
				for(playerData e: roster) {

					System.out.println(e);		
				}

				exit = true; //exit loop
			}
		}
	}

	/**
	 * Prints menu and gets the option choice from the user
	 * @param input
	 * @return the number choice
	 */

	public static int menuOption(Scanner input) {

		String menu = "";
		menu += String.format("%20s", "Menu Option\n");
		menu += "----------------------------\n";
		menu += "1: Add Player Data\n";
		menu += "2: Add a New Player\n";
		menu += "3: Print the Letter Bank\n";
		menu += "4: Substitute a blank used\n";
		menu += "5: Print the score board\n";
		menu += "6: End the Game\n";
		System.out.println(menu);
		System.out.println("Enter choice:");
		int choice = input.nextInt();
		return choice;
	}

	/**
	 * Does the corresponding task entered by the user
	 * @param input
	 * @param menuOption
	 * @param roster
	 * @param letterBank
	 */
	public static void runMenuOption(Scanner input, int menuOption, ArrayList<playerData> roster, TreeMap<Character, Integer> letterBank) {

		switch(menuOption){

		case(1):
			addPlayerData(input, roster, letterBank);
		break;
		case(2):
			addNewPlayer(input, roster);
		break;
		case(3):
			printletterBank(letterBank);
		break;
		case(4):
			substituteBlank(input, letterBank, roster);
		break;
		case(5):
			printScoreBoard(roster);
		break;
		case(6):
			endGame(input, roster, letterBank);
		break;
		default:
			System.out.println("Not an option");
			//runMenuOption(input, menuOption, roster, letterBank);
			break;	
		}


	}


	public static void endGame(Scanner input, ArrayList<playerData> roster, TreeMap<Character, Integer> letterBank) {

		findWinner(roster);
		System.out.println("-----------------------");
		System.out.println("Printing final scoreboard.....");
		printScoreBoard(roster);
		System.out.println("-----------------------");
		System.out.println("Printing the final letter bank......");
		printletterBank(letterBank);
		System.out.println("-----------------------");
		System.out.println("Exiting......");

	}

	public static void printScoreBoard(ArrayList<playerData> roster) {

		for(playerData e: roster) {

			System.out.println(e);		
		}
	}


	/**
	 * Substitutes the word with a blank used for word with the replaced letter
	 * @param input
	 * @param letterBank
	 * @param roster
	 */
	public static void substituteBlank(Scanner input, TreeMap<Character, Integer> letterBank, ArrayList<playerData> roster) {

		System.out.println("Enter word where the blank was used:");
		String word = input.next();
		System.out.println("Which letter is replacing the blank?");
		char letter = input.next().charAt(0);
		System.out.println("Which player used the word?");
		String player = input.next();

		// update letter bank
		letterBank.put('*', letterBank.get('*') + 1); // adds the blank tile back to the letter bank
		letterBank.put(letter, letterBank.get(letter) - 1); // subtracts new letter from the letter bank
		// change the word entered to the word with blank
		String wordWithBlank = word.replace(letter, '*');

		// substitute word with blank with word without blank in the player's used words
		playerData playerObject = findSpecificPlayer(player, roster); // get the player object
		playerObject.replaceWord(wordWithBlank, word);

	}

	/**
	 * Creates a new player
	 * @param input
	 * @param roster
	 */
	public static void addNewPlayer(Scanner input, ArrayList<playerData> roster ) {

		System.out.println("Enter name: ");
		String playerName = input.next();
		playerData player = new playerData(playerName);
		roster.add(player);

	}

	/**
	 * Adds points and words used for a player and prints the scoreboard
	 * @param input
	 * @param roster
	 * @param letterBank
	 */

	public static void addPlayerData(Scanner input, ArrayList<playerData> roster, TreeMap<Character, Integer> letterBank ) {

		boolean exit = false;

		while(!exit) {
			System.out.println("Enter name: ");
			String playerName = input.next();

			// check if name entered is an object in roster
			boolean playerExists = playerExists(playerName, roster);
			if(!playerExists) {
				System.out.println("Name is not in roster");
				continue;
			}
			else { 

				// get points from user
				System.out.println("How many points would you like to add?");
				int points = input.nextInt();
				playerData player = findSpecificPlayer(playerName, roster); // finds the correct player to add points
				player.addPoints(points);

				// get the word used from user
				System.out.println("What word was made?");
				String word = input.next();
				player.addWordUsed(word);

				subtractLetters(word, letterBank); // subtract letters from word bank

				// prints roster data
				for(playerData e: roster) {

					System.out.println(e);		
				}

				exit = true; //exit loop
			}
		}
	}

	/**
	 * Gets the number of players that are playing from user, the player names, and creates a new object for each player
	 * @param input - Scanner input
	 * @param roster - array list to add the players (objects)
	 */

	public static void getPlayers(Scanner input, ArrayList <playerData> roster) {

		System.out.println("Enter number of players that are playing: ");
		int numPlayers = input.nextInt();
		for(int i = 0; i<numPlayers; i++) {
			System.out.println("Enter name: ");
			String playerName = input.next();
			playerData player = new playerData(playerName);
			roster.add(player);
		}
	}

	/**
	 * Creates a word bank of the initial quantity of the letters
	 * @param letterBank
	 */

	public static void letterBank(TreeMap<Character, Integer> letterBank) {

		letterBank.put('a', 9);
		letterBank.put('b', 2);
		letterBank.put('c', 2);
		letterBank.put('d', 4);
		letterBank.put('e', 12);
		letterBank.put('f', 2);
		letterBank.put('g', 3);
		letterBank.put('h', 2);
		letterBank.put('i', 9);
		letterBank.put('j', 1);
		letterBank.put('k', 1);
		letterBank.put('l', 4);
		letterBank.put('m', 2);
		letterBank.put('n', 6);
		letterBank.put('o', 8);
		letterBank.put('p', 2);
		letterBank.put('q', 1);
		letterBank.put('r', 6);
		letterBank.put('s', 4);
		letterBank.put('t', 6);
		letterBank.put('u', 4);
		letterBank.put('v', 2);
		letterBank.put('w', 2);
		letterBank.put('x', 1);
		letterBank.put('y', 2);
		letterBank.put('z', 1);
		letterBank.put('*', 2);
	}


	/**
	 * Subtracts letters from the letter bank when a word is used
	 * @param word - the word used
	 * @param letterBank
	 */

	public static void subtractLetters(String word, TreeMap<Character, Integer> letterBank) {

		// if bank contains a letter from word, subtract 1 from letter value (letters left)
		Set<Map.Entry<Character, Integer>> entrySet = letterBank.entrySet();

		// outer for loop check each letter in word
		for(int i = 0; i< word.length(); i++) {
			// inner for loop sees if letter equals key, if it does subtract 1 from the value
			for (Entry<Character, Integer> entry : entrySet) {
				if(word.charAt(i) == (entry.getKey())) {
					entry.setValue((entry.getValue() - 1));
				}	
			}
		}
	}

	/**
	 * Prints the list of all the letters, and how many of each are left to use
	 * @param letterBank
	 */

	public static void printletterBank(TreeMap<Character, Integer> letterBank) {

		Set<Map.Entry<Character, Integer>> entrySet = letterBank.entrySet();
		for (Entry<Character, Integer> entry : entrySet) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}


	}

	/**
	 * Finds a specific player in the roster to manipulate data such as the score or words used
	 * @param playerName
	 * @param roster
	 * @return the specific player
	 */

	public static playerData findSpecificPlayer(String playerName, ArrayList<playerData> roster) {

		playerData result = null;

		for(playerData object : roster) {
			if(playerName.equals(object.getName())) {
				result = object;
				break;
			}
		}

		return result;

	}

	/**
	 * Checks to see if the player specified is indeed in the roster
	 * @param playerName 
	 * @param roster 
	 * @return true if the player is in the roster, false if not in roster
	 */

	public static boolean playerExists(String playerName, ArrayList<playerData> roster) {

		boolean result = false;

		for(playerData object : roster) {
			if(playerName.equals(object.getName())) {
				result = true;
				break;
			}
		}

		return result;

	}
	/**
	 * This method find the winner of the game based on the greatest score
	 * @param roster
	 */

	public static void findWinner(ArrayList<playerData> roster) {

		int maxVal = 0;
		int counter = 0;// keeps track of what player will have the max score
		for(playerData object : roster) {
			if(object.getScore() > maxVal) {
				maxVal = object.getScore();
				break;
			}
			counter++;
		}

		System.out.println(roster.get(counter).getName() + "wins!!");
	}


}
