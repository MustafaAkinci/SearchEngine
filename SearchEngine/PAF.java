import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PAF {
	float load_factor;
	int collision_algorithm;
	int table_size;
	int collisions;
	Map<Integer,Node> table;
	
	public PAF(float load_factor,int col_alg,int table_size) {
		this.load_factor = load_factor;
		this.collision_algorithm = col_alg;
		this.table_size = table_size;
		this.collisions = 0;
		this.table = new HashMap<Integer,Node>(this.table_size,this.load_factor);
		
		
	}
	
	public void printTable() {
		System.out.println("Number of collisions : " + this.collisions);
		
		/*for(Entry<Integer, Node> entry : this.table.entrySet()) {
			System.out.println(entry.getValue().value);
			System.out.println(entry.getValue().occurances);
			
		}
		
		//a*/

	}
	
	public Node searchWord(String value) {
		Node node = null;
		double time =System.nanoTime();
		if(this.collision_algorithm == 0) {
			//linear
			int index = (int)(this.Hash(value));
			
			while(table.containsKey(index) && (!table.get(index).value.equals(value))) {
				index++;
				
			}
			if(table.containsKey(index)) {
				if(table.get(index).value.equals(value)) {
					//found the word
					node = table.get(index);
				}
			}
			
			//System.out.println("Searching time of PAF LP : " + ((System.nanoTime() - time)/1000000000) + " sec.");

		}
		else {
			//double hash
			double hash = this.Hash(value);
		
			int index = (int)(hash);
			int j = 0;
			int q = 7;
			while(table.containsKey(index) && (!table.get(index).value.equals(value))) {
				
				index = nextProb(index,(int)(q - hash%q));
			
				j++;
			}
			
			if(table.containsKey(index)) {
				if(table.get(index).value.equals(value)) {
					//found the word
					node = table.get(index);
				}
			}
			//System.out.println("Searching time of PAF DH : " + ((System.nanoTime() - time)/1000000000) + " sec.");

		}
		
		return node;
	}
	
	private int getPrime(double hash) {
		int res = 2;
		boolean flag = true;
		
		while(flag) {
			for(int i = 2 ; i < hash/2;i++) {
				if(hash % i == 0) {
					flag = true;
					break;
				}
				else {
					res = i;
					flag = false;
					break;
				}
			}
		}
		
		return res;
	}
	
	public double Hash(String value) {
		double res = 0;
		for(int i = 0; i < value.length();i++) {
			int c = (int)value.charAt(i);
			res += c* Math.pow(13, value.length() - i -1);
		}
		
		return res;

	}
	
	public int nextProb(int hash,int dk) {
		return (int)((hash + dk));
	}
	
	public void placeTotable(String value,String filename) {
		if(this.collision_algorithm == 0) {
			//linear prob
			int index = (int)(this.Hash(value));
			
			int oldindex = index;
			while(table.containsKey(index) && (!table.get(index).value.equals(value))) {
				index++;
				
			}
			if(!table.containsKey(index)) {
				//first time putting it
				Node node = new Node();
				node.value = value;
				node.occurances.put(filename, 1);
				this.collisions += index - oldindex;
				this.table.put(index, node);
				if(table.size() > this.table_size*this.load_factor) {
					this.table_size *= 2;
				}
				
			}
			else if(table.get(index).value.equals(value)){
				//we already putted it
				Node node = table.get(index);
				if(node.occurances.containsKey(filename)) {
					node.occurances.replace(filename, node.occurances.get(filename) +1);
				}
				else {
					node.occurances.put(filename, 1);
				}
				
			}
		}
		else {
			//double hash
			double hash = this.Hash(value);
			int index = (int)(hash);

			int j = 0;
			int q = 7;
			while(table.containsKey(index) && (!table.get(index).value.equals(value))) {
				
		
				index = nextProb(index,(int)(q - hash%q));
				
				
				
				j++;
			}
			
			if(!table.containsKey(index)) {
				//first time putting it
				Node node = new Node();
				node.value = value;
				node.occurances.put(filename, 1);
				this.collisions += j;
				this.table.put(index, node);
				if(table.size() > this.table_size*this.load_factor) {
					//calculate a prime number for new size
					this.table_size *= 2;
					boolean flag = true;
					while(flag) {
						flag = false;
						for(int x = 2; x <= this.table_size/2;x++) {
							if(this.table_size % x == 0) {
								flag = true;
								this.table_size+=1;
								break;
							}
							
						}
						
						
					}
				}
				
			}
			else if(table.get(index).value.equals(value)){
				//we already putted it
				Node node = table.get(index);
				if(node.occurances.containsKey(filename)) {
					node.occurances.replace(filename, node.occurances.get(filename) +1);
				}
				else {
					node.occurances.put(filename, 1);
				}
				
			}
			
			
		}
		
	}
	
	
}
