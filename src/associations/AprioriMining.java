package associations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AprioriMining{
	
	static final int SUPPORT = 5;
	static ArrayList<K_items_set> frequent_set = new ArrayList<K_items_set>();
	static ArrayList<K_items_set> gen_set = new ArrayList<K_items_set>();
	static DataSet dataSet;
	
	public static void main(String[] args) throws IOException {
		Date date1 = new Date();
		long time1,time2;
		time1 = date1.getTime();
		
		//Get Whole Data Set
		dataSet = new DataSet("./data/out.txt");
		ArrayList<K_items_set> all_frequent_set = new ArrayList<K_items_set>();

		KIS_Sorter KIS_S = new KIS_Sorter(all_frequent_set);
		
		//Output File
		File frequent_set_filename = new File("./data/frequent_file_Apri.txt");
		FileWriter writer = new FileWriter(frequent_set_filename);
		
		int k;
		//Generate 1 item frequent set
		gen_1_frequent_item_set();
		
		while(frequent_set.size() != 0)
		{
			//Generate Candidate Set
			apriori_gen();
			
			//Count The Candidate Set
			for(int i=0;i<dataSet.data_set.size();i++){
				second:for(int j=0;j<gen_set.size();j++)
				{
					for(k=0;k<gen_set.get(j).set.size();k++){
						if(dataSet.data_set.get(i).row.indexOf(gen_set.get(j).set.get(k)) == -1)
							continue second;
					}
					if(k == gen_set.get(j).set.size())
						gen_set.get(j).count++;
				}
			}
			//Write Into Final Frequent Set
			all_frequent_set.clear();
			for(int i=0;i<frequent_set.size();i++)
				all_frequent_set.add(frequent_set.get(i));
			
			//Sort Final Frequent Set And Output
			all_frequent_set = KIS_S.KIS_Sort();
			for(int i=0;i<all_frequent_set.size();i++){
				if(i>=10)
					break;
				for(int j=0;j<all_frequent_set.get(i).set.size();j++){
					System.out.print(all_frequent_set.get(i).set.get(j)+" ");
					writer.write(all_frequent_set.get(i).set.get(j)+"\t");
				}
				System.out.println(all_frequent_set.get(i).count);
				writer.write(all_frequent_set.get(i).count+"\n");
			}
			
			//Choose Candidate Set Into Frequent Set 
			frequent_set.clear();
			for(int i=0;i<gen_set.size();i++)
				if(gen_set.get(i).count>=SUPPORT)
					frequent_set.add(gen_set.get(i));
		}
		
		//Sort Final Frequent Set And Output
//		KIS_Sorter KIS_S = new KIS_Sorter(all_frequent_set);
//		all_frequent_set = KIS_S.KIS_Sort();
//		for(int i=0;i<all_frequent_set.size();i++){
//			for(int j=0;j<all_frequent_set.get(i).set.size();j++)
//				System.out.print(all_frequent_set.get(i).set.get(j)+" ");
//			System.out.println(all_frequent_set.get(i).count);
//		}
		writer.close();
		Date date2 = new Date();
		time2 = date2.getTime();
		
		System.out.println(time2-time1);
		
	}
	
	static void gen_1_frequent_item_set(){
		int k = 0;
		//Get All Medicine And Count
		for(int i=0;i<dataSet.data_set.size();i++){
			for(int j=0;j<dataSet.data_set.get(i).row.size();j++){
				for(k=0;k<frequent_set.size();k++)
					if(dataSet.data_set.get(i).row.get(j).equals(frequent_set.get(k).set.get(0))){
						frequent_set.get(k).count++;
						break;
					}
				if(k==frequent_set.size())
					frequent_set.add(new K_items_set(dataSet.data_set.get(i).row.get(j),1));
			}
		}
		
		//Remove Unsatisfying Medicine
		for(int i=0;i<frequent_set.size();i++){
			if(frequent_set.get(i).count < SUPPORT){
				frequent_set.remove(i);
				i--;
			}
		}
	}
	
	static void apriori_gen(){
		int k;
		gen_set.clear();
		//Merge
		for(int i=0;i<frequent_set.size()-1;i++){
			for(int j=i+1;j<frequent_set.size();j++){
				for(k=0;k<frequent_set.get(i).set.size()-1;k++){
					if(frequent_set.get(i).set.get(k).equals(frequent_set.get(j).set.get(k)) == false)
						break;
				}
					
				if(k == frequent_set.get(i).set.size()-1){
					gen_set.add(new K_items_set(frequent_set.get(i).set));
					gen_set.get(gen_set.size()-1).set.add(frequent_set.get(j).set.get(frequent_set.get(j).set.size()-1));
				}
			}
		}
		
		//Cut
		for(int i=0;i<gen_set.size();i++){
			if(has_infrequent_subset(i)){
				gen_set.remove(i);
				i--;
			}
		}
	}
	
	static boolean has_infrequent_subset(int setIndex){
		int k;
		outer:for(int i=0;i<gen_set.get(setIndex).set.size()-2;i++){
			for(int j=0;j<frequent_set.size();j++){
				for(k=0;k<gen_set.get(setIndex).set.size();k++){
					if(k == i)
						continue;
					if(frequent_set.get(j).set.indexOf(gen_set.get(setIndex).set.get(k)) == -1)
						break;
				}
				if(k == gen_set.get(setIndex).set.size())
					continue outer;
			}
			return true;
		}
		return false;
	}

}

@SuppressWarnings("rawtypes")
class K_items_set implements Comparable{
	
	ArrayList<String> set = new ArrayList<String>();
	int count = 0;
	
	K_items_set(ArrayList<String> set){
		for(int i=0;i<set.size();i++)
			this.set.add(set.get(i));
	}
	
	K_items_set(String MedcineName,int n){
		set.add(MedcineName);
		count = n;
	}
	
	public int compareTo(Object KIS){
		if(this.count>((K_items_set)KIS).count)
			return -1;
		else if(this.count==((K_items_set)KIS).count)
			return 0;
		else
			return 1;
	}
}

class KIS_Sorter{
	ArrayList<K_items_set> KIS;
	
	KIS_Sorter(ArrayList<K_items_set> kis){
		KIS = kis;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<K_items_set> KIS_Sort(){
		Collections.sort(KIS);
		return KIS;
	}
}
