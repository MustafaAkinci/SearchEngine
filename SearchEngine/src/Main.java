import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	
	 
	
	public static void main(String args[]) {
		
		List<String> stopwords = new ArrayList<>();
		
	
	   
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("stopwords.txt")));
			String line;
			
			while((line = bufferedReader.readLine()) != null) {
				if(!line.equals("")) {
					stopwords.add(line.trim());
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopwords.add("");
		
		
		SSH sshlp = SSHLP(stopwords,0.75f,100);
		sshlp.printTable();
		SSH sshdh = SSHDH(stopwords,0.75f,100);
		sshdh.printTable();
		PAF paflp = PAFLP(stopwords,0.75f,100);
		paflp.printTable();
		PAF pafdh = PAFDH(stopwords,0.75f,100);
		pafdh.printTable();
		
		ArrayList<Double> sslp = new ArrayList<Double>();
		ArrayList<Double> ssdh = new ArrayList<Double>();
		ArrayList<Double> pflp = new ArrayList<Double>();
		ArrayList<Double> pfdh = new ArrayList<Double>();
		
		
		
		
		
		
		
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("search.txt")));
			String line;
			
			while((line = bufferedReader.readLine()) != null) {
				if(!line.equals("")) {
					line = line.trim();
					double time = System.nanoTime();
					if(sshlp.searchWord(line) != null) {
						sslp.add((System.nanoTime()- time)/1000000000);
					}
					time = System.nanoTime();
					
					if(sshdh.searchWord(line)!=null) {
						ssdh.add((System.nanoTime()- time)/1000000000);
					}
					
					time = System.nanoTime();
					if(paflp.searchWord(line)!=null) {
						pflp.add((System.nanoTime()- time)/1000000000);
					}
					
					
					time = System.nanoTime();
					if(pafdh.searchWord(line)!=null) {
						pfdh.add((System.nanoTime()- time)/1000000000);
					}
					
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//min for 1 query
		//max for all queries*/
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the words you want to search");
		String searchh=sc.next();
		query(searchh,paflp);
		
		
		
		
		/*
		System.out.println("Min search time for SSH LP : " + min(sslp));
		System.out.println("Max search time for SSH LP : " + max(sslp));
		System.out.println("Avg search time for SSH LP : " + sum(sslp)/sslp.size());
		
		System.out.println("Min search time for SSH DH : " + min(ssdh));
		System.out.println("Max search time for SSH DH : " + max(ssdh));
		System.out.println("Avg search time for SSH DH : " + sum(ssdh)/ssdh.size());
		
		System.out.println("Min search time for PAF LP : " + min(pflp));
		System.out.println("Max search time for PAF LP : " + max(pflp));
		System.out.println("Avg search time for PAF LP : " + sum(pflp)/pflp.size());
		
		System.out.println("Min search time for PAF DH : " + min(pfdh));
		System.out.println("Max search time for PAF DH : " + max(pfdh));
		System.out.println("Avg search time for PAF DH : " + sum(pfdh)/pfdh.size());
		//a*/
		
		
		
		
	}
	
	static void query(String qs,PAF paflp) {
		ArrayList<Node> pafnodes = new ArrayList<>();
		Map<String,Integer> result = new HashMap<>();
		
		
		String ll[] = qs.toLowerCase().split(" ");
		for(String s : ll) {
			
			Node n = paflp.searchWord(s.trim());
			if(n!= null) {
				pafnodes.add(paflp.searchWord(s.trim()));
			
			}
			
		}
		
		for(Node n : pafnodes) {
			
			
			if(n != null) {
				for(Map.Entry<String, Integer> entry : n.occurances.entrySet()) {
					if(result.containsKey(entry.getKey())){
						result.replace(entry.getKey(), result.get(entry.getKey())+entry.getValue());
						
					}
					else {
						result.put(entry.getKey(),entry.getValue());
					}
				}
			}
		}
		
		int max = 0;
		String res = "";
		
		for(Map.Entry<String, Integer> entry : result.entrySet()) {
			if(entry.getValue() > max) {
				res = entry.getKey();
				max = entry.getValue();
			}
		}
		
		System.out.println(res);
		
		
	}
	
	static double min(ArrayList<Double> dd) {
		double res = 1;
		for(Double d : dd) {
			if(d < res) {
				res = d;
			}
		}
		return res;
	}
	
	static double max(ArrayList<Double> dd) {
		double res = 0.0000001;
		for(Double d : dd) {
			if(d > res) {
				res = d;
			}
		}
		return res;
	}
	
	static double sum(ArrayList<Double> dd) {
		double res = 0;
		for(Double d : dd) {
			
				res += d;
			
		}
		return res;
	}
	
	public static SSH SSHLP(List<String> stopwords,float load_factor,int num_of_files) {
		double time = System.nanoTime();
		SSH ssh = new SSH(load_factor,0,2500);
		
		for(int i = 1;i < num_of_files+1;i++) {
			String fname = String.format("%03d", i) + ".txt";
			
			
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fname)));
				String line;
				
				while((line = bufferedReader.readLine()) != null) {
					if(!line.equals("")) {
						line = line.replaceAll("[^\\P{P}']+"," ").replaceAll("\\d", " ").replaceAll("\\s+", " ").toLowerCase();
						//.replaceAll("\\b(xm|m|cm|km)\\b", "")
						
						
						List<String> list = new ArrayList<String>(Arrays.asList(line.split(" ")));
						
						list.removeAll(stopwords);
						
						
						for(String s : list) {
							
							ssh.placeTotable(s.replaceAll("('s)", " ").replaceAll("\\p{P}", " ").replaceAll("\\s+", " ").trim(), fname);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Indexin time of SSH LP : " + ((System.nanoTime() - time)/1000000000) + " sec.");
		return ssh;
		
	}

	
	public static SSH SSHDH(List<String> stopwords,float load_factor,int num_of_files) {
		double time = System.nanoTime();
		SSH ssh = new SSH(load_factor,1,2477);
		
		for(int i = 1;i < num_of_files+1;i++) {
			String fname =  String.format("%03d", i) + ".txt";
			
			
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fname)));
				String line;
				
				while((line = bufferedReader.readLine()) != null) {
					if(!line.equals("")) {
						line = line.replaceAll("[^\\P{P}']+"," ").replaceAll("\\d", " ").replaceAll("\\s+", " ").toLowerCase();
						//.replaceAll("\\b(xm|m|cm|km)\\b", "")
						
						
						List<String> list = new ArrayList<String>(Arrays.asList(line.split(" ")));
						
						list.removeAll(stopwords);
						
						
						for(String s : list) {
							
							ssh.placeTotable(s.replaceAll("('s)", " ").replaceAll("\\p{P}", " ").replaceAll("\\s+", " ").trim(), fname);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Indexin time of SSH DH : " + ((System.nanoTime() - time)/1000000000) + " sec.");

		
		return ssh;
		
	}
	
	
	public static PAF PAFLP(List<String> stopwords,float load_factor,int num_of_files) {
		double time = System.nanoTime();
		PAF paf = new PAF(load_factor,0,2500);
		
		for(int i = 1;i < num_of_files+1;i++) {
			String fname = String.format("%03d", i) + ".txt";
			
			
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fname)));
				String line;
				
				while((line = bufferedReader.readLine()) != null) {
					if(!line.equals("")) {
						line = line.replaceAll("[^\\P{P}']+"," ").replaceAll("\\d", " ").replaceAll("\\s+", " ").toLowerCase();
						//.replaceAll("\\b(xm|m|cm|km)\\b", "")
						
						
						List<String> list = new ArrayList<String>(Arrays.asList(line.split(" ")));
						
						list.removeAll(stopwords);
						
						
						for(String s : list) {
							
							paf.placeTotable(s.replaceAll("('s)", " ").replaceAll("\\p{P}", " ").replaceAll("\\s+", " ").trim(), fname);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Indexin time of PAF LP : " + ((System.nanoTime() - time)/1000000000) + " sec.");
		return paf;
		
	}
	
	public static PAF PAFDH(List<String> stopwords,float load_factor,int num_of_files) {
		double time = System.nanoTime();
		PAF paf = new PAF(load_factor,1,2477);
		
		for(int i = 1;i < num_of_files+1;i++) {
			String fname = String.format("%03d", i) + ".txt";
			
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fname)));
				String line;
				
				while((line = bufferedReader.readLine()) != null) {
					if(!line.equals("")) {
						line = line.replaceAll("[^\\P{P}']+"," ").replaceAll("\\d", " ").replaceAll("\\s+", " ").toLowerCase();
						//.replaceAll("\\b(xm|m|cm|km)\\b", "")
						
						
						List<String> list = new ArrayList<String>(Arrays.asList(line.split(" ")));
						
						list.removeAll(stopwords);
						
						
						for(String s : list) {
							
							paf.placeTotable(s.replaceAll("('s)", " ").replaceAll("\\p{P}", " ").replaceAll("\\s+", " ").trim(), fname);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Indexin time of PAF DH : " + ((System.nanoTime() - time)/1000000000) + " sec.");

		
		return paf;
		
	}
}
