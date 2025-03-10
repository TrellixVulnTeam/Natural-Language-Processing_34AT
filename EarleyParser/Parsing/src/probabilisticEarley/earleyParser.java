package probabilisticEarley;
////////////////////////////////////////////////////////////////////////////////
//
//Juanyan Wang
//A20411039
//
//	This implements the final assignment for cs585.
//
//		The task is to implement a language parser; I have chosen to
//	implement the Earley method with probabalistic likelihoods; basically
//	instead of keeping all productions we keep only the K most likely.  this
//	increases efficiency while hopefully having a minimal impact upon the
//	performance of the algorithm.  For the grammar, I am using several by
//	Jason Eisner.
////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

/**This class implements an Earley parser for CFG's
 * @author Paul Chase: chaspau@iit.edu
 * @version .68
 * This code is adapted hastily from a previous assignment; it's one of the
 * first things I wrote in java and as such is less than polished.
 */
public class earleyParser
{
	//The grammar!
//	static Grammar grammar;

	/**the main function, it reads in a grammar and a file to tag, or gets
	* sentences from the command line.
	*
	* There are two modes, interactive and batch.
	* Interactive: java earleyParser grammar
	* Batch: java earleyParser grammar sentence_file
	* 
	* @param argv the standard command line argument array
	*/
	public static void main(String argv[]) throws Exception
	{
		Grammar grammar;
		if((argv.length != 1) && (argv.length != 2))
		{
			System.out.println("USAGE: java earleyParser <grammar>[ <sentences file>]");
		}else{
			//load the grammar
			grammar = new Grammar(argv[0]);
			//grammar.print();
			
			//process sentences from the command line
			if(argv.length == 1)
			{
				System.out.println("Grammar:"+argv[0]);
				System.out.println("Type sentences below.  Note that there is no tokenization\nyou should input each token separated by spaces.  Input \"quit\" to exit.");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String temp;
				String arr[];
				while(true)
				{
					System.out.print("?");
					temp = br.readLine();
					arr = temp.split(" ");
					Vector result = null;
					//see if they want to exit
					if((arr.length == 1)&&(arr[0].compareTo("quit")==0))
					{
						return;
					}
					//parse the array of words
					if(grammar.canParse(arr))
					{
						result = grammar.parse(arr);
						if(result != null && result.size() != 0){
							System.out.println("Parsing was successful.");
							
							String maxParse = "";
							String parse = "";
							float maxProb = -Float.MAX_VALUE;
							float prob = 0;
							
							for(int i=0;i<result.size();i++) {
								parse = ((Production) result.get(i)).recursivePrint();
								parse = parse.substring(4);
								prob = ((Production) result.get(i)).recursiveProb();
								
								System.out.println("Parsing tree"+(i+1)+":");
								System.out.println(parse);
								System.out.println("Prob:"+prob+"\n");
								
								if(prob > maxProb) {
									maxProb = prob;
									maxParse = parse;
								}
							}
							System.out.println("The most likely parsing is:\n"+maxParse+"\nProb:"+maxProb);
						}else{
							System.out.println("Parsing failed!");
						}
					}else{
						System.out.println("That sentence is not in the grammar.");
					}
				}
			}else{
				//they entered a file
				BufferedReader br = new BufferedReader(new FileReader(new File(argv[1])));
				String line;
				String arr[];
				Vector result = null;
				while((line = br.readLine()) != null)
				{
					arr = line.split(" ");
					if(grammar.canParse(arr))
					{
						System.out.println("=====================================\n"+line);
						result = grammar.parse(arr);
						if(result != null && result.size() != 0){
							System.out.println("Parsing was successful.");
							
							String maxParse = "";
							String parse = "";
							float maxProb = -Float.MAX_VALUE;
							float prob = 0;
							
							for(int i=0;i<result.size();i++) {
								parse = ((Production) result.get(i)).recursivePrint();
								parse = parse.substring(4);
								prob = ((Production) result.get(i)).recursiveProb();
								
								System.out.println("Parsing tree"+(i+1)+":");
								System.out.println(parse);
								System.out.println("Prob:"+prob+"\n");
								
								if(prob > maxProb) {
									maxProb = prob;
									maxParse = parse;
								}
							}
							System.out.println("The most likely parsing is:\n"+maxParse+"\nProb:"+maxProb);
						}else{
							System.out.println("no matching productions found");
						}
					}else{
						System.out.println("That sentence is not in the grammar.");
					}
				}
			}
		}
	}
}
