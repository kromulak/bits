import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
/**
 * ========================================================================
 * 	NUIM CS211 - Lab 5 - 2014
 * ------------------------------------------------------------------------
 * An anagram is a type of word play, the result of rearranging the letters of
 * a word or phrase to produce a new word or phrase, using all the original
 * letters exactly once. Write a program that finds the longest English
 * anagram where the letters are completely scrambled (no letter is
 * followed by the same letter in both words). For example Mary and army
 * are anagrams, but the a is followed by r in both words, so it isn’t a
 * scrambled anagram. Pot and top are scrambled anagrams.
 * Try using last week’s smaller dictionary first. This has words up to 15
 * letters long. If you get that working, you could try using the
 * bigdictionary.txt or even biggestdictionary.txt. This has all kinds of
 * words. It has entries that are not proper words. You will need to modify
 * your algorithm to avoid noise. For example, turn all letters into lower
 * case. Also, avoid any word that has punctuation in it.
 * Also, no valid English word has the same letter repeated more than twice in a row.
 * You might need to enlist some help from the user to accept or reject potential
 * anagrams.
 * 
 * Use any data structures you want (see the Java library for ArrayList,TreeMap,
 * HashMap, Arrays.sort() or Collections.sort()). Try to optimize your algorithm
 * for speed (without overfitting it).
 * =======================================
 * @author Ollie Noonan
 *
 */
public class Lab5v3 {
	static String currentFile = "";
	public static void main(String[] args) {
				
		
		//String filename = "dictionary.txt";	 
		//String filename = "bigdictionary.txt";
		//String filename = "biggestdictionary.txt";
		
		//findBiggestPair(filename);

		Scanner sc1 = new Scanner(System.in);
		System.out.println("Enter file name: ");
		String fName = sc1.nextLine();
		
		findBiggestPair(fName);
		
		sc1.close();
	}
	
	/**
	 * Finds the longest English anagram where the letters are completely scrambled
	 * (no letter is followed by the same letter in both words).<br>
	 * For example Mary and army are anagrams, but the a is followed by r in both words,
	 *  so it isn’t a scrambled anagram. Pot and top are scrambled anagrams
	 *  
	 * @param filename The name of the file to search through
	 * @return a string of the biggest pair found
	 */
	public static String findBiggestPair(String filename) {
		
		System.out.println("Loading file: " + filename);
		long startBuildTime = System.currentTimeMillis();
		
		loadFile(filename);
		
		long endBuildTime   = System.currentTimeMillis();
		long totalBuildTime = endBuildTime - startBuildTime;
		System.out.println(filename + " loaded in "+totalBuildTime+ "ms");
		
		System.out.println("Searching...");
		
		long startSearchTime = System.currentTimeMillis();
		
		String s = LongestAnagramComparator.longest;
		
		long endSearchTime = System.currentTimeMillis();
		long totalSearchTime = endSearchTime - startSearchTime;

		System.out.println("Search complete in "+totalSearchTime + "ms");
		System.out.println("Total runtime = "+(totalBuildTime + totalSearchTime)+"ms");

		Scanner sc = new Scanner(System.in);
		String input;
		do {
			String answer = LongestAnagramComparator.sols.poll();
			System.out.println(answer + " ["+(answer.length()-1)+"]");
			System.out.println(alphabetize(answer.split(" ")[0]) + " " + alphabetize(answer.split(" ")[1]));
			System.out.print("\t\tHit y then enter to view more (Q then enter to end)");
			input = sc.next();
		} while((!input.trim().equalsIgnoreCase("q"))&&(!LongestAnagramComparator.sols.isEmpty()));

		sc.close();
		
		return s;
	}

	
	
	public static char[] uniqueify(char[] s) {

		for(int i = 0; i < s.length-3; i++) {
			int j = 0;
			
			if(!Character.isLetter(s[i]))
				return null;
			
			if(s[i] == 'q') { // no english word has qr -> http://www.scrabblefinder.com/contains/qr/
				if(s[i+1]!='u')
					return null;
			}
			
			while((i+j < s.length)&&(s[i+j] == s[i])) {
				j++;
				if(j > 2)// same letter repeated 3 or more
					return null;
			}
			
			i+=(j-1);
		}

		Arrays.sort(s);

		return s;
	}

	
	public static void saveToFile(String filename, PriorityQueue<PriorityQueue<String>> mainQueue) {
       File aFile = new File(filename);
       Writer output = null;
       try {
          output = new BufferedWriter( new FileWriter(aFile) );
        	  for(int i = 0 ; i < mainQueue.size(); i++) {
      			PriorityQueue<String> topQueue = mainQueue.poll();
      			String w1 = topQueue.poll();
      			String w2 = topQueue.poll();
      			
      			if((w1 != null)&&(w2 != null)) {
      				String result = w1 + " " + w2 + " [total: " + (w1.length()+w2.length()) + "]";
      				//System.out.println(result);
      				output.write(result);
      	            output.write(System.getProperty("line.separator"));
      			}
        	  }
        } catch (Exception e) {

			e.printStackTrace();
		}
        finally {
          if (output != null)
			try {
				output.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        }
    }
	
	static LongestAnagramComparator sComp = new LongestAnagramComparator();
		
	public static HashMap<String, PriorityQueue<String>> loadFile(String file) {
	    File aFile = new File(file);     
	    BufferedReader input = null;
	    
	    HashMap<String, PriorityQueue<String>> map = new HashMap<>();
	    
	    try {
	      input = new BufferedReader( new FileReader(aFile) );
	      String word = null; 
	      
	      //static StringLenComparator sComp = new StringLenComparator();

	      while (( word = input.readLine()) != null){
	    	  char[] s = uniqueify(word.trim().toLowerCase().toCharArray());

//	    	  String tempKey = alphabetize(word);
	    	  
	    	  if( (s != null)&&(LongestAnagramComparator.longest.length()/2 < s.length)) {
		    	  String tempKey = new String(s);
		    	  //System.out.println(tempKey);
		    	  PriorityQueue<String> res = map.get(tempKey);//new String(tempKey));
					

		    	  if(res == null)
		    		  res = new PriorityQueue<String>(1, sComp);/* {

						/* (non-Javadoc)
						 * @see java.util.PriorityQueue#offer(java.lang.Object)
						 *
						@Override
						public boolean offer(String arg0) {
							
							//if(sComp.longest.length()/2 < arg0.length())//AnagramUtils.isScrambledAnagram(longest, arg0))
								return super.offer(arg0);
							
							//return false;
						}
		    		  
		    	  };*/
						
					
		    	  res.offer(word.trim());
					
		    	  map.put(tempKey, res);//tempKey, res);
	    	  }
	      }
	    }
	    catch (FileNotFoundException ex) {
	      System.out.println("Can't find the file - are you sure the file is in this location: "+file);
	      ex.printStackTrace();
	    }
	    catch (IOException ex){
	      System.out.println("Input output exception while processing file");
	      ex.printStackTrace();
	    }
	    finally {
	      try {
	        if (input!= null) {
	          input.close();
	        }
	      }
	      catch (IOException ex) {
	        System.out.println("Input output exception while processing file");
	        ex.printStackTrace();
	      }
	    }
	    
	    return map;
	  }
	
    private static String alphabetize(String s) {
    	char[] a = s.toCharArray();
    	Arrays.sort(a);
    	return new String(a);
    }
}


/**
 * Messy!
 * Keeps tract of the longest anagram compared during its life
 * 
 * @author Ollie Noonan
 *
 */
class LongestAnagramComparator implements Comparator<String>
{
	public static String longest = "";
	public static PriorityQueue<String> sols = new PriorityQueue<String>(1, new StringLengthComparator());

    @Override
    public int compare(String x, String y)
    {

        if(AnagramUtils.isScrambledAnagram(x, y)) {//ScrambledAnagram(x, y)) {
        	if(x.length() + y.length() + 1 > longest.length()) {
        		longest = x + " " + y;
        		sols.offer(longest+"");


        	}
        	
        	return x.compareTo(y);
        }
        
        return 1;        
    }
    
    public static boolean isScrambledAnagram(String sA, String sB) {
    	if((sA == null)||(sB == null))
    		return false;

    	String s1 = sA.toLowerCase();
    	String s2 = sB.toLowerCase();
    	
    	String sTemp;
    	for(int i = 0; i < s1.length()-1; i++) {
    		sTemp = s1.substring(i, i+2);
    		if(s2.contains(sTemp))
    			return false;
    	}

    	return true;
	}
}

/**
 * Compare two strings based on length.
 * 
 * @author Ollie Noonan
 *
 */
class StringLengthComparator implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		if(s1.length() < s2.length())
			return 1;
		
		if(s1.length() > s2.length())
			return -1;
		
		return 0;
	}
}