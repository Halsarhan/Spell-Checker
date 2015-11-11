//Hamza Alsarhan
//HW 7 part 2

import java.util.LinkedList;

//constructor
public class HashBucket {
	LinkedList<String> [] myTable = new LinkedList[52];
	
	//initializes each bucket of the hash table to have a linkedList
	public HashBucket(){
		for (int i = 0; i<myTable.length; i++){
			LinkedList Alphabet = new LinkedList();
			myTable[i] = (Alphabet);
		}
	}
	
	
	//returns a string of misspelled words in a sorted order
	public String sorting(){
		String myString = "";
		for(int i =0; i<myTable.length; i++){
			int first = 0;
			int last = myTable[i].size()-1;
			String[] myArray = (String []) myTable[i].toArray(new String[myTable[i].size()]);
			QuickSort(myArray, first, last);
			for(int j =0; j<myArray.length; j++){
				myString += myArray[j] + "\n";
			}
		}
	return myString;
	
	}
	
	
	public void QuickSort(String[] A, int first, int last){
		if(A.length <=3){
			insertionSort(A);
		}
		else if(first < last){ // list size >1
			int pivot = -1;
			if(A[first].compareTo(A[last]) <=0 && A[first].compareTo(A[(first+last)/2]) >=0){
				pivot = first;
			}
			if(A[last].compareTo(A[first]) <=0 && A[last].compareTo(A[(first+last)/2]) >=0){
				pivot = last;
			}else{
				pivot = (last + first)/2;
			}
			
			int splitPoint = partition(A, first, last, pivot);
			QuickSort(A, first, splitPoint-1); //left
			QuickSort(A, splitPoint+1, last); //right
		}
	}
	
	
	
	/*
	 * This method inserts words into the hash table at the certain letter that the word begins with
	 */
	public void hash(String word){
		int value = (int) word.charAt(0); //gets the ascii value of the first letter of the word
		if(value < 94){
			value = value - 65;			
		}else{
			value = value - 71;
		}
		myTable[value].add(word);
	}

	

	public int partition(String[] A, int first, int last, int pivot){
		//swapping A[pivot] with A[last]
		String pivotStr = A[last];
		A[pivot] = A[last];
		A[last] = pivotStr;
		int i = first;
		int j = last - 1;
		boolean loop = true;
		while(loop){
			while(A[i].compareTo(pivotStr) <= 0 && i<last){
			i++;
			}
			while(A[j].compareTo(pivotStr)>= 0 && j>first){
				j--;
			}
			if(i<j){//i and j have not crossed
				//swapping A[i] with A[j]
				String temp = A[i];
				A[i] = A[j];
				A[j] = temp;
			}else{//i and j crossed
				loop = false;
			}
		}
		//swapping A[i] with pivotStr
		String temp2 = A[i];
		A[i] = pivotStr;
		A[last] = temp2;
		
		return i;
	}
	
	
	/*
	 * insertionSort is called when A is of size <= 3
	 */
	 public static void insertionSort(String[] A) {
	        for (int j = 1; j < A.length; j++) {
	            String word = A[j];
	            int i = j-1;
	            while ( (i > -1) && ( A[i].compareTo(word) >= 0) ) {
	                A [i+1] = A [i];
	                i--;
	            }
	            A[i+1] = word;
	    }
	}
	 
}




	