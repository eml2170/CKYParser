import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * This class builds the MLE estimates for the rule probabilities
 * Naturally, it also contains the PCFG
 * @author edwardliu
 *
 */
public class PcfgModel {

	Map<BinaryRule, Integer> binaryRuleCount; 	//Key set is R_binary
	Map<UnaryRule, Integer> unaryRuleCount; 	//Key set is R_unary
	Map<String, Integer> nonTerminalCount; 		//Key set is N
	Set<String> vocabulary;						//Sigma
	private static final String SPACE = " ";
	Map<String, List<BinaryRule>> xRules; 		//Map to look up a nonterminal's binary rules (needed for CKY)

	public PcfgModel(String countsFilename){
		File file = new File(countsFilename);
		binaryRuleCount = new HashMap<BinaryRule, Integer>();
		unaryRuleCount = new HashMap<UnaryRule, Integer>();
		nonTerminalCount = new HashMap<String, Integer>();
		vocabulary = new HashSet<String>();
		xRules = new HashMap<String, List<BinaryRule>>();
		count(file);
	}

	//Count(X->Y1 Y2)/Count(X)
	public double q(BinaryRule rule){
		double ruleCount = (double) binaryRuleCount.get(rule);
		double ntCount = (double) nonTerminalCount.get(rule.x);
		return ruleCount/ntCount;
	}

	//Count(X -> w)/Count(X)
	public double q(UnaryRule rule){
		double ruleCount = (double) unaryRuleCount.get(rule);
		double ntCount = (double) nonTerminalCount.get(rule.x);
		return ruleCount/ntCount;
	}

	private void count(File file){
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] tokens = line.split(SPACE);
				switch (tokens.length) {
				case 3: //non terminal
					String nt = tokens[2];
					int count = Integer.parseInt(tokens[0]);
					nonTerminalCount.put(nt, count);
					break;
				case 4: //unary rule
					String x = tokens[2];
					String w = tokens[3];
					UnaryRule unaryRule = new UnaryRule(x,w);
					int unaryCount = Integer.parseInt(tokens[0]);
					unaryRuleCount.put(unaryRule, unaryCount);
					vocabulary.add(w);
					break;
				case 5: //binary rule
					x = tokens[2];
					String y1 = tokens[3];
					String y2 = tokens[4];
					BinaryRule binaryRule = new BinaryRule(x,y1,y2);
					int binaryCount = Integer.parseInt(tokens[0]);
					binaryRuleCount.put(binaryRule, binaryCount);
					
					//add rule to xRules
					if(xRules.containsKey(x)){
						List<BinaryRule> rules = xRules.get(x);
						rules.add(binaryRule);
					}
					else{
						List<BinaryRule> newRules = new ArrayList<BinaryRule>();
						newRules.add(binaryRule);
						xRules.put(x, newRules);
					}
						
					break;
				}
			}


			scanner.close();
		} 


		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Set<String> getN(){
		return nonTerminalCount.keySet();
	}
	
	public boolean hasRule(UnaryRule rule){
		return unaryRuleCount.containsKey(rule);
	}
	
	public List<BinaryRule> getBinaryRules(String X){
		return xRules.get(X);
	}
	
	public boolean isRare(String x){
		return !vocabulary.contains(x);
	}



	public static void main(String[] args){
		new PcfgModel("cfg_closed.counts");
	}
}
