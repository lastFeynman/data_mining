package classify;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class DecisionTreeTest {

	public static void main(String[] args) throws Exception {
		DecisionTree DTree = new DecisionTree("./data/data_matrix.txt");
		ArrayList<String> attrs = new ArrayList<String>();
		
		File attr_file = new File("./data/output.rst");
		Reader reader = new InputStreamReader(new FileInputStream(attr_file));
		int tmpChar;
		String tmpAttr;
		while(true){
			tmpAttr = "";
			tmpChar = reader.read();
			while((char)tmpChar != '\n' && tmpChar != -1){
				tmpAttr = tmpAttr+(char)tmpChar;
				tmpChar = reader.read();
			}
			attrs.add(tmpAttr);
			
			if(tmpChar == -1)
				break;
		}
		
		for(int i=0;i<attrs.size();i++)
			System.out.println(attrs.get(i));
		
		for(int i=0;i<DTree.tree.size();i++){
			if(!DTree.tree.get(i).isLeaf)
				System.out.print("false "+DTree.tree.get(i).AttrID+" ");
			else
				System.out.print("true "+DTree.tree.get(i).classID+" ");
			
			System.out.println(DTree.tree.get(i).fatherNode+" "+DTree.tree.get(i).fatherAttrValue);
		}
		
		ClassifyDataSet dataSet = new ClassifyDataSet("./data/data_matrix.txt");
		int testSet[][] = new int[dataSet.data_set.size()-1][dataSet.data_set.get(0).attributes.size()];
		
		for(int i=1;i<dataSet.data_set.size();i++){
			for(int j=0;j<dataSet.data_set.get(0).attributes.size();j++){
				testSet[i-1][j] = (int)dataSet.data_set.get(i).attributes.get(j);
			}
		}
//		for(int i=1;i<dataSet.data_set.size();i++){
//			for(int j=0;j<dataSet.data_set.get(0).attributes.size();j++){
//				System.out.print(testSet[i-1][j]);
//			}
//			System.out.println();
//		}
		int test_class[] = new int[dataSet.data_set.size()-1];
		for(int i=1;i<dataSet.data_set.size();i++){
			test_class[i-1] = DTree.searchTree(testSet[i-1]);
		}
		
		int err_count=0;
		for(int i=1;i<dataSet.data_set.size();i++){
			if(test_class[i-1] != dataSet.data_set.get(i).i_class)
				err_count++;
		}
		
		System.out.println(err_count);
		reader.close();
	}

}
