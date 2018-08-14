package DataPreHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class CountDivider {
	
	public static void main(String args[]) throws Exception{
		File file = new File("./data/dest_data_final.txt");
		@SuppressWarnings("resource")
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
		
		int rowcount = 1,colcount=0,count=0;
		int tempchar;
		char divider,preDivider = '£»';
		
		while((tempchar = reader.read()) != -1){
			if(tempchar == '\n'){
				rowcount++;
				colcount = 0;
			}
			if((char)tempchar == '£º'){
				count++;
				divider = (char)tempchar;
				if((divider == '£º' && preDivider != '£»') || (divider == '£¬' && preDivider != '£º') || (divider == '£»' && preDivider != '£¬')){
					System.out.println(rowcount+"qqqqqqq"+colcount);
					return;
				}
				preDivider = divider;
			}

			
		}
		reader.close();
		System.out.println(count);
	}
	
	
}
