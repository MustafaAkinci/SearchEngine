import java.util.HashMap;
import java.util.Map;

public class Node {
	String value;
	Map<String,Integer> occurances = new HashMap<String,Integer>();
	
	public Node() {
		
	}
	
	public void printNode() {
		System.out.println(this.value);
		for(Map.Entry<String, Integer> entry : this.occurances.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

}
