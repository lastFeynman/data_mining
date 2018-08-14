package DataPreHandle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

public class Counter {
	
	static ArrayList<CounterEntity> countList = new ArrayList<CounterEntity>();
	
	public static void main(String args[]) throws Exception{
		
		String dest_file_name = "./data/output.rst";
		File output_file = new File(dest_file_name);
		
		
		String out_file_name = "./data/out.txt";
		File out_file = new File(out_file_name);
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(out_file));
		
		File src_file = new File("./data/dest_data_final.txt");
		Reader reader = new InputStreamReader(new FileInputStream(src_file));
		
		String tmpMedcineName;
		int tmpChar,existIndex;
		
		outer:while(true){
			
			tmpMedcineName = "";
			//Get Data
			while((char)(tmpChar = reader.read()) != '£º'){
				if((char)tmpChar != '\n' && (char)tmpChar != '\r')
				{
					if(tmpChar == -1)
						break outer;
						
					tmpMedcineName = tmpMedcineName + (char)tmpChar;
				}
				else{
					if((char)tmpChar == '\n'){
						writer1.write("\r");
					}
				}
			}
		
			writer1.write(tmpMedcineName+" ");
			//Data Into List
			if(countList.size()==0){
				countList.add(new CounterEntity(tmpMedcineName));
			}else{
				existIndex = 0;
				for(existIndex = 0;existIndex<countList.size();existIndex++){
					if(tmpMedcineName.equals(countList.get(existIndex).MedcineName)){
						countList.get(existIndex).MedcineCount++;
						break;
					}
				}
				if(existIndex == countList.size()){
					countList.add(new CounterEntity(tmpMedcineName));
				}
					
			}
			
			//Skip Other Char
			tmpChar = reader.read();
			while(tmpChar != -1 && (char)tmpChar != '£»'){
				tmpChar = reader.read();
			}
				
			if(tmpChar == -1)
				break;
		}
		
		if(!output_file.exists()){
			output_file.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));
		CESorter CE_S = new CESorter(countList);
		CE_S.CESort();
		for(int i = 0;i<countList.size();i++){
			writer.write(countList.get(i).MedcineName+" "+countList.get(i).MedcineCount+"\n");
		}
		writer.flush();
		writer1.flush();
		writer.close();
		writer1.close();
	}
}

@SuppressWarnings("rawtypes")
class CounterEntity implements Comparable{
	String MedcineName;
	int MedcineCount = 1;
	
	CounterEntity(String medName){
		MedcineName = new String(medName);
	}
	
	public int compareTo(Object CE){
		if(this.MedcineCount>((CounterEntity)CE).MedcineCount)
			return -1;
		else if(this.MedcineCount==((CounterEntity)CE).MedcineCount)
			return 0;
		else
			return 1;
	}
}

class CESorter{
	ArrayList<CounterEntity> CE;
	
	CESorter(ArrayList<CounterEntity> ce){
		CE = ce;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CounterEntity> CESort(){
		Collections.sort(CE);
		return CE;
	}
}