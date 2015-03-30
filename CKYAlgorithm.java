import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;


public class CKYAlgorithm {
	
	Map<String, double[][]> pi;
	Map<String, Objective[][]> bp;
	String[] sentence;
	static PcfgModel g;
	
	//Train on parse_train.dat and test on parse_dev.dat, which has labels in parse_dev.key
	public CKYAlgorithm(String sentence){
		this.sentence = sentence.split(" ");
		pi = new HashMap<String, double[][]>();
		bp = new HashMap<String, Objective[][]>();
		initpi();
	}
	
	private void initpi(){
		int n = sentence.length;
		for(String X : g.getN()){
			double[][] values = new double[n][n];
			for(int i = 0; i < n; i++){
				UnaryRule rule = new UnaryRule(X,sentence[i]);
				values[i][i] = g.hasRule(rule) ? g.q(rule) : 0;
			}
			pi.put(X, values);
			bp.put(X, new Objective[n][n]);
		}
	}
	
	public String execute(){
		int n = sentence.length;
		for (int l = 1; l <= n-1; l++){ 	//offset (exactly same as pseudocode)
			for (int i = 0; i < n-l; i++){	//Shift by 1 for java
				int j = i+l;
				for(String X : g.getN()){
					Objective opt = max(X,i,j);
					pi.get(X)[i][j] = opt != null ? opt.probability : 0.0;
					bp.get(X)[i][j] = opt;
				}
			}
		}
//		System.out.println(pi.get("S")[0][n-1]);
//		System.out.println(bp.get("S")[0][n-1]);
//		System.out.println(bp.get("NP+PRON")[0][1]);
//		System.out.println(bp.get("S")[1][n-1]);
//		System.out.println(bp.get("VP")[1][2]);
//		System.out.println(bp.get(".")[3][n-1]);
		
		JSONArray arr = new JSONArray();

//		JSONArray pred = recover("S",0,n-1, arr);
//		return pred != null ? pred.toJSONString() : "";
		if(pi.get("S")[0][n-1] != 0)
			return recover("S",0,n-1, arr).toJSONString();
		else{
			double max = Double.MIN_VALUE;
			String argmax = null;
			for(String X : g.getN()){
				if(pi.get(X)[0][n-1] > max){
					max = pi.get(X)[0][n-1];
					argmax = X;
				}
			}
			return recover(argmax,0,n-1,arr).toJSONString();
		}
	}
	/* Used for testing
	private void recover(String X, int i, int j){
		if(i == j){
			System.out.println(X + " at " + i);
		}
		else{
			Objective opt = bp.get(X)[i][j];
			BinaryRule rule = opt.rule;
			int s = opt.split;
			recover(rule.y1,i,s);
			recover(rule.y2,s+1,j);
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	private JSONArray recover(String X, int i, int j, JSONArray arr){
		try{
		if(i == j){
			arr.add(X);
			arr.add(sentence[i]);
			return arr;
		}
		else{
			Objective opt = bp.get(X)[i][j];
			BinaryRule rule = opt.rule;
			int s = opt.split;
			arr.add(0,X);
			arr.add(recover(rule.y1,i,s, new JSONArray()));
			arr.add(recover(rule.y2,s+1,j, new JSONArray()));
		}
		return arr;
		}
		catch(NullPointerException e){
			return null;
		}
	}
	
	private Objective max(String X, int i, int j){
		List<BinaryRule> rules = g.getBinaryRules(X);
		double max = -Double.MIN_VALUE;
		BinaryRule bestRule = null;
		int split = -1;
		if(rules == null) return null;
		for(BinaryRule rule : rules){
			for(int s = i; s <= j-1; s++){
				double q = g.q(rule) * pi.get(rule.y1)[i][s] * pi.get(rule.y2)[s+1][j];
				if(q>max){
					max = q;
					bestRule = rule;
					split = s;
				}
			}
		}
		return new Objective(max,bestRule,split);
	}
	
	public static void main(String[] args) throws IOException{
		g = new PcfgModel("cfg_closed.counts");
		String NEWLINE = "\n";
		Scanner scanner = new Scanner(new File("parse_dev.dat"));
		FileWriter writer = new FileWriter(new File("prediction_file"));
		while(scanner.hasNextLine()){
			String sentence = close(scanner.nextLine());
			CKYAlgorithm cky = new CKYAlgorithm(sentence);
			writer.write(cky.execute());
			writer.write(NEWLINE);
		}
		writer.close();
		scanner.close();
		
//		String sentence = close("Odds and Ends");
//		System.out.println(sentence);
//		CKYAlgorithm cky = new CKYAlgorithm(sentence);
//		System.out.println(cky.execute());
	}
	
	private static String close(String s){
		String[] sentence = s.split(" ");
		String RARE = "_RARE_";
		String SPACE = " ";
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < sentence.length; i++){
			if(g.isRare(sentence[i]))
				buffer.append(RARE);
			else
				buffer.append(sentence[i]);
			buffer.append(SPACE);
			
		}
		return buffer.toString();
	}
	
}

class Objective{
	double probability;
	BinaryRule rule;
	int split;
	public Objective(double probability, BinaryRule rule, int split) {
		super();
		this.probability = probability;
		this.rule = rule;
		this.split = split;
	}
	@Override
	public String toString() {
		return "Objective [probability=" + probability + ", rule=" + rule
				+ ", split=" + split + "]";
	}
	
	
	
	
}
