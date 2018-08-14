package associations;

import java.util.ArrayList;

public class O_Row{
	public int rowNum;
	public ArrayList<String> row= null;
		
	public O_Row(int rowNum,String name){
		this.rowNum = rowNum;
		row = new ArrayList<String>();
		row.add(name);
	}
	
	public O_Row(int rowNum){
		this.rowNum = rowNum;
		row = new ArrayList<String>();
	}
}

