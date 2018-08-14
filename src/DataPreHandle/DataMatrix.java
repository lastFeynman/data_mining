package DataPreHandle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import associations.DataSet;

public class DataMatrix {
	
	static class m_class{
		String name = null;
		int i_class=0;
		m_class(String name,int i_class){
			this.name = name;
			this.i_class = i_class;
		}
	}
	
	static DataSet dataSet;
	static ArrayList<String> medcine_set = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		File matName = new File("./data/data_matrix.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(matName));
		
		dataSet = new DataSet("./data/out.txt");
		get_medcine_set();
		
		ArrayList<m_class> classes = new ArrayList<m_class>();
		File class_file = new File("./data/class.txt");
		Reader reader = new InputStreamReader(new FileInputStream(class_file));
		
		int tmpChar = 0,classes_i=0,num;
		String class_name;
		
		while(true){
			class_name = "";
			tmpChar = reader.read();
			while((char)tmpChar != '\r' && (char)tmpChar != '\n' && tmpChar != -1){
				class_name = class_name+(char)tmpChar;
				tmpChar = reader.read();
			}
			
			
			for(num=0;num<classes.size();num++){
				if(classes.get(num).name.equals(class_name))
					break;
			}
			if(!class_name.equals("") && num == classes.size())
				classes_i++;
				
			if(!class_name.equals(""))
				classes.add(new m_class(class_name,classes_i));
			
			if(tmpChar == -1){
				break;
			}
			
		}
		reader.close();
		for(int i=0;i<classes.size();i++)
			System.out.println(classes.get(i).name+classes.get(i).i_class);
		System.out.println(classes.size());
		
		int matrix[][] = new int[dataSet.data_set.size()][medcine_set.size()+1];
		
		for(int i=0;i<dataSet.data_set.size();i++){
			for(int j=0;j<dataSet.data_set.get(i).row.size();j++){
				matrix[i][medcine_set.indexOf(dataSet.data_set.get(i).row.get(j))] = 1;
			}
		}
		
		for(int i=0;i<dataSet.data_set.size();i++){
			matrix[i][medcine_set.size()] = classes.get(i).i_class;
		}
		
		for(int i=0;i<dataSet.data_set.size();i++){
			for(int j=0;j<medcine_set.size();j++)
				writer.write(matrix[i][j]+" ");
			writer.write(matrix[i][medcine_set.size()]+"\n");
		}
		writer.flush();
		writer.close();
	}
	
	static void get_medcine_set(){
		for(int i=0;i<dataSet.data_set.size();i++){
			for(int j=0;j<dataSet.data_set.get(i).row.size();j++){
				if(medcine_set.indexOf(dataSet.data_set.get(i).row.get(j)) == -1)
					medcine_set.add(dataSet.data_set.get(i).row.get(j));
			}
		}
	}
}
