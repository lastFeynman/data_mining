package classify;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import associations.DataSet;

public class ClassifyDataSet {
	public class Instance{
		@SuppressWarnings("rawtypes")
		ArrayList attributes = new ArrayList();
		int i_class=0;
	}
	
	ArrayList<Instance> data_set = new ArrayList<Instance>();
	
	@SuppressWarnings("unchecked")
	ClassifyDataSet(String src_path) throws Exception{
		File data_file = new File(src_path);
		Reader divider_reader = new InputStreamReader(new FileInputStream(data_file));
		Reader reader = new InputStreamReader(new FileInputStream(data_file));
		DataSet dataSet = new DataSet("./data/out.txt");
		
		String class_str;
		int dividerCount = 0;
		int tmpChar = divider_reader.read();
		while(tmpChar != '\n' && tmpChar != '\r'){
			if(tmpChar == ' ')
				dividerCount++;
			tmpChar = divider_reader.read();
		}
		divider_reader.close();
		
		data_set.add(new Instance());
		for(int i=0;i<dividerCount;i++){
			data_set.get(0).attributes.add(i);
		}
		data_set.get(0).i_class = 0;
		
		for(int i=0;i<dataSet.data_set.size();i++){
			data_set.add(new Instance());
			for(int j=0;j<dividerCount;j++){
				tmpChar = reader.read();
				data_set.get(i+1).attributes.add(tmpChar-48);
				tmpChar = reader.read();
			}
			
			class_str = "";
			tmpChar = reader.read();
			while((char)tmpChar != '\n'){
				class_str = class_str+(char)tmpChar;
				tmpChar = reader.read();
			}
			
			data_set.get(i+1).i_class = Integer.parseInt(class_str);
		}
		reader.close();
	}
	
	@SuppressWarnings("unchecked")
	ClassifyDataSet(int attrID,int attrValue,ClassifyDataSet dataSet){
		this.data_set.add(new Instance());
		for(int i=0;i<dataSet.data_set.get(0).attributes.size();i++){
			if((int)dataSet.data_set.get(0).attributes.get(i) != attrID)
				this.data_set.get(0).attributes.add((int)dataSet.data_set.get(0).attributes.get(i));
		}
		for(int i=1;i<dataSet.data_set.size();i++){
			if((int)dataSet.data_set.get(i).attributes.get(dataSet.data_set.get(0).attributes.indexOf(attrID)) != attrValue){
				continue;
			}
			this.data_set.add(new Instance());
			for(int j=0;j<dataSet.data_set.get(0).attributes.size();j++){
				if((int)dataSet.data_set.get(0).attributes.get(j) != attrID){
					this.data_set.get(this.data_set.size()-1).attributes.add(dataSet.data_set.get(i).attributes.get(j));
				}
			}
			this.data_set.get(this.data_set.size()-1).i_class = dataSet.data_set.get(i).i_class;
		}
	}
}
