import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class VocabularyCloser {

	private static String RARE = "_RARE_";
	private static String SPACE = " ";
	private static String NEWLINE = "\n";
	private Set<String> vocabulary;

	public VocabularyCloser(String countsFilename, String trainFilename){
		File countsFile = new File(countsFilename);
		countWords(countsFile);

		File trainFile = new File(trainFilename);
		File closedFile = new File("parse_closed.dat");
		
		//Rewrite tree
		close(trainFile, closedFile);
	}

	private void countWords(File f){
		//first pass - build hashmap
		Scanner scanner;
		Map<String, Integer> freqMap = new HashMap<String, Integer>();
		try {
			scanner = new Scanner(f);
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] tokens = line.split(SPACE);
				if (tokens.length == 4){ //UNARY RULE
					String word = tokens[3];
					int count = Integer.parseInt(tokens[0]);
					freqMap.put(word, freqMap.containsKey(word) ? freqMap.get(word) + count : count);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//second pass - construct vocabulary. x is rare if count(x) < 5
		vocabulary = new HashSet<String>();
		for(String word : freqMap.keySet()){
			if(freqMap.get(word) >= 5)
				vocabulary.add(word);
		}
	}

	private void close(File originalFile, File newFile){
		Scanner scanner;
		try {
			scanner = new Scanner(originalFile);
			FileWriter writer = new FileWriter(newFile);

			JSONParser parser = new JSONParser();
			while(scanner.hasNextLine()){
				String tree = scanner.nextLine();

				Object obj = parser.parse(tree);
				JSONArray array = (JSONArray)obj;
				recurseJSONArray(array);
				writer.write(array.toJSONString());
				writer.write(NEWLINE);
			}

			writer.close();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void recurseJSONArray(JSONArray a){
		if(a.size() == 2){
			//Replace word if rare
			String word = (String) a.get(1);
			if(!vocabulary.contains(word)){
				a.set(1, RARE);
			}
		}

		else{
			recurseJSONArray((JSONArray) a.get(1));
			recurseJSONArray((JSONArray) a.get(2));
		}
	}

	public static void main(String[] args) throws FileNotFoundException, ParseException{
		String trainFilename = "parse_train_vert.dat";
		String countsFilename = "cfg.counts";
		new VocabularyCloser(countsFilename, trainFilename);
	}

}

