package associations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class DataSet {
	public ArrayList<O_Row> data_set = new ArrayList<O_Row>();
	
	public DataSet(String src_path) throws IOException{
		File src_file = new File(src_path);
		Reader reader = new InputStreamReader(new FileInputStream(src_file));
		
		String tmpMedcineName;
		int tmpChar = 0,rowIndex = 1;
		
		while(true){
			tmpMedcineName = "";
			//Get Data
			if(tmpChar == 0)
				tmpChar = reader.read();
			while(tmpChar != ' '){
				tmpMedcineName = tmpMedcineName + (char)tmpChar;
				tmpChar = reader.read();
			}
			
			//Data Into List
			if(data_set.size()==0){
				data_set.add(new O_Row(rowIndex,tmpMedcineName));
			}else{
				if(data_set.get(data_set.size()-1).rowNum == rowIndex)
					data_set.get(data_set.size()-1).row.add(tmpMedcineName);
				else
					data_set.add(new O_Row(rowIndex,tmpMedcineName));
			}
			
			
			//Skip Other Char
			tmpChar = reader.read();
			if((char)tmpChar == '\r')
			{
				tmpChar = reader.read();
				rowIndex++;
			}
			else if(tmpChar == -1)
				break;
			else
				continue;
		}
		reader.close();
	}
	
	public DataSet(){}
}