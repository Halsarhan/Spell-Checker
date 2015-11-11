//Hamza Alsarhan
//3 extra credit methods: swap, replace, and insert

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class SpellChecker {
	
	public static String dictName;
	public static QuadraticProbingHashTable<String> Dictionary;
	public static QuadraticProbingHashTable <String> Misspelled;
	public static QuadraticProbingHashTable <String> Repeated;
	public static LinkedList<String> Suggestions;
	static BufferedWriter out;
	static BufferedWriter misspelledFile;
	
	public static void main(String[] args) throws IOException{
		
		dictName =  args[0];
		readDict();
		readFile();
	}
	
	
	
	/*
	 * reads dictionary file and inserts words into the Dictionary hashTable
	 */
	public static void readDict() throws IOException{
		System.out.println("Reading in Dictionary...");
		Dictionary = new QuadraticProbingHashTable<String>();
		//Reads file line by line and word by word
		File openFile = new File(dictName);
		Scanner dictreader = new Scanner(openFile);
		int i =0;
		while(dictreader.hasNextLine()){
			Dictionary.insert(dictreader.nextLine()); //adds words to Dictionary
			i++;
		}
		System.out.println("Dictionary read.");
	}
	
	
	/*
	 * prompts user for filename and reads file and does all of the storing of the misspelled
	 * words in a hashtable, and calls on other functions that correct the misspelled words.
	 * Also, write to output files.
	 */
	public static void readFile() throws IOException{
		
		Misspelled = new QuadraticProbingHashTable <String>(); //initializing hashTable
		Repeated = new QuadraticProbingHashTable <String>(); //initializing hashTable
		System.out.println("Please enter a file to spell check>> ");
		Scanner scan = new Scanner(System.in);
		String filename = scan.nextLine();
		//Reads file line by line and word by word
		File openFile = new File(filename);
		Scanner filereader = new Scanner(openFile);
		int lineNumber =1; ////initializing lineNumber of misspelled word
		System.out.println("print words(p), enter new file(f), or quit(q)?");
		Scanner scanChoice = new Scanner (System.in);
		char choice = scanChoice.nextLine().charAt(0);
		String outputMisspelled = "";
		String word= "";
		String outputStr ="";
		while (choice != 'q'){
			if (choice != 'f'){
					while(filereader.hasNextLine()){
						String[] line = filereader.nextLine().split(" "); //creating a string array of words in a line
						
						for (int i = 0; i < line.length; i++){
							word = line[i];
							
							if(!Repeated.contains(word)){
								if (!Dictionary.contains(word)){
									System.out.println("--"+ word + " " + lineNumber);
									System.out.println("ignore all(i), suggest(s), next(n)?");
									Scanner scanChoice2 = new Scanner (System.in);
									char choice2 = scanChoice2.nextLine().charAt(0);
									if(choice2 == 'i'){
										Repeated.insert(word);
										Misspelled.insert(word);
										outputStr += word + " "; //string that will be printed to output.txt
										outputMisspelled += word + " " + lineNumber + "\n"; //string that will be printed to misspelled.txt

									}else if(choice2=='s'){
										Suggestions = new LinkedList(); //initializes a linkedList of suggestions
										delete(word);
										insert(word);
										swap(word);
										replace(word);
										split(word);
										if(Suggestions.size() > 1){
											outputMisspelled += word + " " + lineNumber + "\n";
											outputStr += fixWord(word) +" ";
											
										
										}else{ //if we don't find any suggestions to a misspelled word
										System.out.println("Sorry, no suggestions exist!");
										outputStr += word + " ";
										}
									}else if(choice2 =='n'){ //if user chooses to skip over 1 occurrence of a misspelled word
										outputStr += word + " ";
										Misspelled.insert(word); //storing misspelled words
										outputMisspelled += word + " " + lineNumber + "\n";
									}
									
								}else{ //if (Dictionary.contains(word))
									outputStr += word + " ";
								}
							}else{ //if (Repeated.contains(word))
							outputStr += word + " ";
							outputMisspelled += word + " " + lineNumber + "\n";
						}
					}    
						outputStr += "\n"; 
						lineNumber ++; //incrementing lineNumber when done working with the line.

					}
					//now start writing to files.
					out=new BufferedWriter(new FileWriter(new File("output.txt")));
					out.write(outputStr);
					out.close();
					misspelledFile = new BufferedWriter(new FileWriter(new File("misspelled.txt")));
					misspelledFile.write(outputMisspelled);
					misspelledFile.close();
					System.out.println("Spell-check complte.");

			}else{
				System.out.println("Please enter a file to spell check>> ");
				filename = scan.nextLine();
				openFile = new File(filename);
				filereader = new Scanner(openFile);	
		}
			
			System.out.println("Print words(p), enter new file(f), or quit(q)?");
			Scanner scan2 = new Scanner(System.in);
			choice = scan2.nextLine().charAt(0);	
		}
		
		System.out.println("Goodbye!"); //if user inputs q for his/her choice
	}
	
	
	
	/*
	 * method deletes each character from word and checks if it exists in Dictionary
	 * to add to Suggestions.
	 */
	public static void delete(String word){
		for (int i = 0; i<word.length(); i++){
			String first = word.substring(0, i);
			String second = word.substring(i+1);
			
			String newWord = first + second;
			if(Dictionary.contains(newWord)){ //check if in Dictionary
				if(!Suggestions.contains(newWord)){ //check if it's not repeated
				Suggestions.add(newWord);
				}
			}
		}
	}
	
	/*
	 * insert in between each adjacent pair of characters in the word each letter from a through z
	 * and checks if it exists in Dictionary to add to Suggestions
	 */
	public static void insert(String word){

		for (int i=1;i<=word.length()+1; i++){
			for(int j=0; j<26; j++){
				
				String first = word.substring(0, i-1);
				String second = word.substring(i-1);
				
				String newWord = first + (char) (97 + j) + second;
			
				if(Dictionary.contains(newWord)){ //check if in Dictionary
					if(!Suggestions.contains(newWord)){ //check if it's not repeated
						Suggestions.add(newWord);}

				}
				String newWordCapital = first + (char) (65 + j) + second;
				
				if(Dictionary.contains(newWordCapital)){ //check if in Dictionary
					if(!Suggestions.contains(newWordCapital)){ //check if it's not repeated
					Suggestions.add(newWordCapital);}		

			
				}
				
			}
			
		}
	}
	
	
	
	
	/*
	 * swaps each adjacent pair of characters in the word and checks if it exists
	 * in Dictionary to add to Suggestions
	 */
	public static void swap(String word){
		for(int i = 0; i+1 < word.length(); i++){
			char first = word.charAt(i);
			char second = word.charAt(i+1);
			String straight="";
			String swapped = "";
			
			straight += first + second;
			swapped += second + first;
			
			String newWord = word.replace(straight, swapped);
			if(Dictionary.contains(newWord)){ //check if in Dictionary
				if(!Suggestions.contains(newWord)){ //check if it's not repeated
					Suggestions.add(newWord);
				}
			}
		}
	}
	
	

	/*
	 * replaces each character in the word with each letter from a through z and checks if it
	 * exists in Dictionary to add to Suggestions
	 */
	public static void replace(String word){
		for (int i=0; i< word.length(); i++){
			for(int j=0; j< 26; j++){
				String newWord = word.replace(word.charAt(i), (char)(j+97));
				String newWordCapital = word.replace(word.charAt(i), (char)(j+65));
				
				if (Dictionary.contains(newWord)){ //check if in Dictionary
					if(!Suggestions.contains(newWord)){ //check if it's not repeated
						Suggestions.add(newWord);
					}
				}
				if (Dictionary.contains(newWordCapital)){ //check if in Dictionary
					if(!Suggestions.contains(newWordCapital)){ //check if it's not repeated
						Suggestions.add(newWordCapital);
					}
				}
			}
		}
	}
	
	/*
	 * splits the word into a pair of words by adding a space in between each adjacent pair
	 * of characters in the word, and checks if both words in the pair are found in the Dictionary
	 * to add to Suggestions
	 */
	
	
	public static void split(String word){
		for (int i = 0; i < word.length(); i++){
			String first = word.substring(0, i);
			String second = word.substring(i);
			String newWord = first + " " + second;
			if(Dictionary.contains(first) && Dictionary.contains(second)){ //check if in Dictionary
				if(!Suggestions.contains(newWord)){ //check if it's not repeated
					Suggestions.add(newWord);
				}
			}
		}
	}
	
	
	/*
	 * asks the user for which suggestion to replace the misspelled word with from the suggestions
	 * list ad replaces the misspelled word with that.
	 */
	public static String fixWord(String word){
		int i;
		String suggestions = "Replace with ";
		for (i = 0; i < Suggestions.size(); i++){
			suggestions += ("(" + (i+1) + ")" + Suggestions.get(i) + " ");
		}
		System.out.println(suggestions + ", or next(n)");
		Scanner scan = new Scanner (System.in);
		int choice = scan.nextInt();
		word = Suggestions.get(choice-1);
		return word;
	}
	
	
	
	
	
	
	
}





	