package classify;

import java.util.ArrayList;
import java.util.Collections;

public class K_Nearest {
	static final int MIN_K = 3;
	
	public static void main(String args[]) throws Exception{
		ClassifyDataSet dataSet = new ClassifyDataSet("./data/data_matrix.txt");
int testSet[][] = new int[dataSet.data_set.size()-1][dataSet.data_set.get(0).attributes.size()];
		
		for(int i=1;i<dataSet.data_set.size();i++){
			for(int j=0;j<dataSet.data_set.get(0).attributes.size();j++){
				testSet[i-1][j] = (int)dataSet.data_set.get(i).attributes.get(j);
			}
		}

		int test_class[] = new int[dataSet.data_set.size()-1];
		for(int i=1;i<dataSet.data_set.size();i++){
			test_class[i-1] = classify(testSet[i-1],dataSet);
		}
		
		for(int i=0;i<test_class.length;i++)
			System.out.println(test_class[i]);
		
		int err_count=0;
		for(int i=1;i<dataSet.data_set.size();i++){
			if(test_class[i-1] != dataSet.data_set.get(i).i_class)
				err_count++;
		}
		
		System.out.println((double)(dataSet.data_set.size()-err_count)/dataSet.data_set.size());
	}
	
	static int classify(int[] instance,ClassifyDataSet dataSet){
		ArrayList<Distance> distances = new ArrayList<Distance>();
		for(int i=1;i<dataSet.data_set.size();i++){
			distances.add(new Distance(dataSet.data_set.get(i).i_class,0));
			for(int j=0;j<dataSet.data_set.get(0).attributes.size();j++){
				distances.get(i-1).distance = distances.get(i-1).distance + ((int)dataSet.data_set.get(i).attributes.get(j)-instance[j])*((int)dataSet.data_set.get(i).attributes.get(j)-instance[j]);
			}
			distances.get(i-1).distance = Math.sqrt(distances.get(i-1).distance);
		}
		
		Distance_Sorter D_S = new Distance_Sorter(distances);
		distances = D_S.DSort();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		int j;
		for(int i=0;i<MIN_K;i++){
			for(j=0;j<classes.size();j++){
				if(distances.get(i).index == classes.get(j).ClassID){
					classes.get(j).count++;
					break;
				}
			}
			if(j==classes.size()){
				classes.add(new Class(distances.get(i).index,1));
			}
		}
		
		int MaxClass = 0;
		int MaxCount = 0;
		for(int i=0;i<classes.size();i++){
			if(classes.get(i).count>MaxCount){
				MaxCount = classes.get(i).count;
				MaxClass = classes.get(i).ClassID;
			}
		}
		
		return MaxClass;
	}
}

@SuppressWarnings("rawtypes")
class Distance implements Comparable{
	int index;
	double distance;
	Distance(int index,double distance){
		this.index = index;
		this.distance = distance;
	}
	
	@Override
	public int compareTo(Object D) {
		if(this.distance>((Distance)D).distance)
			return 1;
		else if(this.distance==((Distance)D).distance)
			return 0;
		else
			return -1;
	}
}

class Distance_Sorter{
	ArrayList<Distance> D;
	
	Distance_Sorter(ArrayList<Distance> d){
		D = d;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Distance> DSort(){
		Collections.sort(D);
		return D;
	}
}