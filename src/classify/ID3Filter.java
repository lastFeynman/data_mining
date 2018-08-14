package classify;

import java.util.ArrayList;

public class ID3Filter {
	ClassifyDataSet dataSet = null;
	ID3Filter(ClassifyDataSet dataSet){
		this.dataSet = dataSet;
	}
	
	public int attr_select(){
		ArrayList<Class> classes = new ArrayList<Class>();
		ArrayList<Class> childClasses = new ArrayList<Class>();
		double probability;
		double Info=0,Info_attr[];
		Info_attr = new double[dataSet.data_set.get(0).attributes.size()];
		double Info_partition[] = new double[2];
		int partition_count[] = new int[2];
		double Gain_attr[] = new double[dataSet.data_set.get(0).attributes.size()];
		
		int meaningless;
		for(int i=1;i<dataSet.data_set.size();i++){
			for(meaningless=0;meaningless<classes.size();meaningless++){
				if(dataSet.data_set.get(i).i_class == classes.get(meaningless).ClassID){
					classes.get(meaningless).count++;
					break;
				}
			}
			if(meaningless==classes.size()){
				classes.add(new Class(dataSet.data_set.get(i).i_class,1));
			}
		}
		
		for(int i=0;i<classes.size();i++){
			probability = (double)classes.get(i).count/(dataSet.data_set.size()-1);
			Info = Info+(probability*(Math.log(probability)/Math.log((double)2)));
		}
		Info = -Info;
		
		for(int i=0;i<dataSet.data_set.get(0).attributes.size();i++){
			for(int j=0;j<2;j++){
				childClasses.clear();
				for(int k=1;k<dataSet.data_set.size();k++){
					if((int)dataSet.data_set.get(k).attributes.get(i) != j)
						continue;
					for(meaningless=0;meaningless<childClasses.size();meaningless++){
						if(dataSet.data_set.get(k).i_class == childClasses.get(meaningless).ClassID){
							childClasses.get(meaningless).count++;
							break;
						}
					}
					if(meaningless==childClasses.size()){
						childClasses.add(new Class(dataSet.data_set.get(k).i_class,1));
					}
				}
				
				partition_count[j] = 0;
				for(int k=1;k<dataSet.data_set.size();k++){
					if((int)dataSet.data_set.get(k).attributes.get(i) == j){
						partition_count[j]++;
					}
				}
				Info_partition[j] = 0;
				for(int k=0;k<childClasses.size();k++){
					probability = (double)childClasses.get(k).count/partition_count[j];
					Info_partition[j] = Info_partition[j]+(probability*(Math.log(probability)/Math.log((double)2)));
				}
				Info_partition[j] = -Info_partition[j];
			}
			
			Info_attr[i] = ((double)partition_count[0]/(dataSet.data_set.size()-1))*Info_partition[0]+((double)partition_count[1]/(dataSet.data_set.size()-1))*Info_partition[1];
		}
		
		for(int i=0;i<Info_attr.length;i++){
			Gain_attr[i] = Info - Info_attr[i];
		}
		
		int Max_Gain_index=0;
		double Max_Gain=0;
		for(int i=0;i<Gain_attr.length;i++){
			if(Gain_attr[i]>Max_Gain){
				Max_Gain = Gain_attr[i];
				Max_Gain_index = (int)dataSet.data_set.get(0).attributes.get(i);
			}
		}
		return Max_Gain_index;
	}
}
