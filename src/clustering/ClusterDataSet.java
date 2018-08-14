package clustering;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class ClusterDataSet {
	public class Medicine{
		String medicineName;
		@SuppressWarnings("rawtypes")
		ArrayList Attrs = new ArrayList();
			
		Medicine(String medicineName){
			this.medicineName = medicineName;
		}
	}
	
	ArrayList<Medicine> data_set = new ArrayList<Medicine>();
	ArrayList<String> xing_attrs = new ArrayList<String>();
	ArrayList<String> wei_attrs = new ArrayList<String>();
	ArrayList<String> guijing_attrs = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	ClusterDataSet(String medicine_path,String xing_path,String wei_path,String guijing_path) throws Exception{
		File medicine_file = new File(medicine_path);
		File xing_file = new File(xing_path);
		File wei_file = new File(wei_path);
		File guijing_file = new File(guijing_path);
		
		Reader medicine_reader = new InputStreamReader(new FileInputStream(medicine_file));
		Reader xing_reader = new InputStreamReader(new FileInputStream(xing_file));
		Reader wei_reader = new InputStreamReader(new FileInputStream(wei_file));
		Reader guijing_reader = new InputStreamReader(new FileInputStream(guijing_file));
		
		int tmpChar = 0;
		String tmpString;
		
		while(true){
			tmpString = "";
			
			tmpChar = medicine_reader.read();
			while((char)tmpChar != '\n' && (char)tmpChar != '\r' && tmpChar != -1){
				tmpString = tmpString+(char)tmpChar;
				tmpChar = medicine_reader.read();
			}
			
			
			if(!tmpString.equals(""))
				data_set.add(new Medicine(tmpString));
			
			if(tmpChar == -1)
				break;
		}
		medicine_reader.close();
		
		int rowCount = 0;
		tmpChar = 0;
		while(true){
			tmpString = "";
			tmpChar = xing_reader.read();
			
			while((char)tmpChar != '£¬' && (char)tmpChar != '\r' && (char)tmpChar != '\n' && tmpChar != -1){
				tmpString = tmpString+(char)tmpChar;
				tmpChar = xing_reader.read();
			}
			if(tmpString != ""){
				if(xing_attrs.indexOf(tmpString) == -1){
					xing_attrs.add(tmpString);
					for(int i=0;i<data_set.size();i++){
						if(i==rowCount)
							data_set.get(i).Attrs.add(1);
						else
							data_set.get(i).Attrs.add(0);
					}
				}else{
					data_set.get(rowCount).Attrs.set(xing_attrs.indexOf(tmpString),1);
				}
			}
			
			if((char)tmpChar == '\r'){
				rowCount++;
			}else if(tmpChar == -1){
				break;
			}else
				continue;
		}
		xing_reader.close();
		
		rowCount = 0;
		tmpChar = 0;
		while(true){
			tmpString = "";
			tmpChar = wei_reader.read();
			
			while((char)tmpChar != '£¬' && (char)tmpChar != '¡¢' && (char)tmpChar != '\r' && (char)tmpChar != '\n' && tmpChar != -1){
				tmpString = tmpString+(char)tmpChar;
				tmpChar = wei_reader.read();
			}
			if(tmpString != ""){
				if(wei_attrs.indexOf(tmpString) == -1){
					wei_attrs.add(tmpString);
					for(int i=0;i<data_set.size();i++){
						if(i==rowCount)
							data_set.get(i).Attrs.add(1);
						else
							data_set.get(i).Attrs.add(0);
					}
				}else{
					data_set.get(rowCount).Attrs.set(wei_attrs.indexOf(tmpString)+xing_attrs.size(),1);
				}
			}
			
			if((char)tmpChar == '\r'){
				rowCount++;
			}else if(tmpChar == -1){
				break;
			}else
				continue;
		}
		wei_reader.close();
		
		rowCount = 0;
		tmpChar = 0;
		while(true){
			tmpString = "";
			tmpChar = guijing_reader.read();
			
			while((char)tmpChar != '£¬' && (char)tmpChar != '¡¢' && (char)tmpChar != '\r' && (char)tmpChar != '\n' && tmpChar != -1){
				tmpString = tmpString+(char)tmpChar;
				tmpChar = guijing_reader.read();
			}
			if(tmpString != ""){
				if(guijing_attrs.indexOf(tmpString) == -1){
					guijing_attrs.add(tmpString);
					for(int i=0;i<data_set.size();i++){
						if(i==rowCount)
							data_set.get(i).Attrs.add(1);
						else
							data_set.get(i).Attrs.add(0);
					}
				}else{
					data_set.get(rowCount).Attrs.set(guijing_attrs.indexOf(tmpString)+xing_attrs.size()+wei_attrs.size(),1);
				}
			}
			
			if((char)tmpChar == '\r'){
				rowCount++;
			}else if(tmpChar == -1){
				break;
			}else
				continue;
		}
		guijing_reader.close();
	}
}
