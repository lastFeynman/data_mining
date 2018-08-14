package clustering;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class K_Means {
	static final int K_CLUSTERS = 6;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		ClusterDataSet dataSet = new ClusterDataSet("./data/cluster/data1.txt","./data/cluster/data2.txt","./data/cluster/data3.txt","./data/cluster/data4.txt");
		
//		for(int i=0;i<dataSet.data_set.size();i++){
//			if(i==228){
//				System.out.print(dataSet.data_set.get(i).medicineName);
//				for(int j=0;j<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();j++){
//					System.out.print(dataSet.data_set.get(i).Attrs.get(j)+" ");
//				}
//				System.out.println();
//			}
//		}
//		System.out.println(dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size());
//		for(int i=0;i<dataSet.xing_attrs.size();i++){
//			System.out.print(dataSet.xing_attrs.get(i)+" ");
//		}
//		for(int i=0;i<dataSet.wei_attrs.size();i++){
//			System.out.print(dataSet.wei_attrs.get(i)+" ");
//		}
//		for(int i=0;i<dataSet.guijing_attrs.size();i++){
//			System.out.print(dataSet.guijing_attrs.get(i)+" ");
//		}
		
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		for(int i=0;i<K_CLUSTERS;i++){
			clusters.add(new Cluster());
		}
		
		ArrayList<Cluster> last_clusters = new ArrayList<Cluster>();
		for(int i=0;i<K_CLUSTERS;i++){
			last_clusters.add(new Cluster());
		}
		
		int center[] = new int[K_CLUSTERS];
		for(int i=0;i<K_CLUSTERS;i++)
			center[i] = -1;
		
		Random random = new Random();
		for(int i=0;i<K_CLUSTERS;i++){
			middle:while(true){
				center[i] = random.nextInt(dataSet.data_set.size());
				for(int j=0;j<K_CLUSTERS;j++){
					if(j != i && center[j] == center[i])
						continue middle;
				}
				break;
			}
		}
//		for(int i=0;i<K_CLUSTERS;i++){
//			System.out.print(dataSet.data_set.get(center[i]).medicineName);
//			for(int j=0;j<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();j++){
//				System.out.print(dataSet.data_set.get(center[i]).Attrs.get(j)+" ");
//			}
//			System.out.println();
//		}
//		for(int i=0;i<K_CLUSTERS;i++)
//			System.out.println(center[i]);
		
		double center_coordinate[][] = new double[K_CLUSTERS][dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size()];
		for(int i=0;i<K_CLUSTERS;i++){
			for(int j=0;j<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();j++){
				center_coordinate[i][j] = (int)dataSet.data_set.get(center[i]).Attrs.get(j);
			}
		}
//		for(int i=0;i<K_CLUSTERS;i++){
//			for(int j=0;j<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();j++){
//				System.out.print(center_coordinate[i][j]+" ");
//			}
//			System.out.println();
//		}
		
		double distance[] = new double[K_CLUSTERS];
		int min_index;
		double min_distance;
		boolean equal_flag;
		
		while(true){
			//clear clusters
			for(int i=0;i<K_CLUSTERS;i++){
				clusters.get(i).members.clear();
			}
			//redistribute the data
			for(int i=0;i<dataSet.data_set.size();i++){
				for(int j=0;j<K_CLUSTERS;j++){
					distance[j] = 0;
					for(int k=0;k<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();k++){
						distance[j] = distance[j] + ((int)dataSet.data_set.get(i).Attrs.get(k)-center_coordinate[j][k])*((int)dataSet.data_set.get(i).Attrs.get(k)-center_coordinate[j][k]);
					}
					distance[j] = Math.sqrt(distance[j]);
				}
				
				min_index = -1;
				min_distance = 9999;
				for(int j=0;j<K_CLUSTERS;j++){
					if(distance[j] < min_distance){
						min_index = j;
						min_distance = distance[j];
					}
				}
				
				clusters.get(min_index).members.add(i);
			}
			//cluster is equal to last_clusters
			equal_flag = true;
			for(int i=0;i<K_CLUSTERS;i++){
				if(!clusters.get(i).equals(last_clusters.get(i))){
					equal_flag = false;
					break;
				}
			}
			
//			for(int i=0;i<K_CLUSTERS;i++){
//				for(int j=0;j<clusters.get(i).members.size();j++){
//					System.out.print((int)clusters.get(i).members.get(j)+" ");
//				}
//				System.out.println();
//				for(int j=0;j<last_clusters.get(i).members.size();j++){
//					System.out.print((int)last_clusters.get(i).members.get(j)+" ");
//				}
//				System.out.println();
//			}
			//not equal
			if(!equal_flag){
				for(int i=0;i<K_CLUSTERS;i++){
					for(int j=0;j<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();j++){
						center_coordinate[i][j] = 0;
						for(int k=0;k<clusters.get(i).members.size();k++){
							center_coordinate[i][j] = center_coordinate[i][j] + (int)dataSet.data_set.get((int)clusters.get(i).members.get(k)).Attrs.get(j);
						}
						center_coordinate[i][j] = center_coordinate[i][j]/clusters.get(i).members.size();
					}
				}
				
				for(int i=0;i<K_CLUSTERS;i++){
					last_clusters.get(i).members.clear();
					
					for(int j=0;j<clusters.get(i).members.size();j++){
						last_clusters.get(i).members.add(clusters.get(i).members.get(j));
					}
				}
			}else{
				break;
			}
//			for(int i=0;i<K_CLUSTERS;i++){
//				for(int j=0;j<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();j++){
//					System.out.print(center_coordinate[i][j]+" ");
//				}
//				System.out.println();
//			}
		}
		
		File output_file = new File("./data/cluster/k_means_out.txt");
		FileWriter writer = new FileWriter(output_file);
		for(int i=0;i<K_CLUSTERS;i++){
			for(int j=0;j<clusters.get(i).members.size();j++){
				System.out.print(dataSet.data_set.get((int)clusters.get(i).members.get(j)).medicineName+" ");
				writer.write(dataSet.data_set.get((int)clusters.get(i).members.get(j)).medicineName+" ");
			}
			System.out.println();
			writer.write("\n");
		}
		writer.flush();
		writer.close();
		
		double e = 0;
		for(int i=0;i<K_CLUSTERS;i++){
			for(int j=0;j<clusters.get(i).members.size();j++){
				for(int k=0;k<dataSet.xing_attrs.size()+dataSet.wei_attrs.size()+dataSet.guijing_attrs.size();k++){
					e = e+ ((int)dataSet.data_set.get((int)clusters.get(i).members.get(j)).Attrs.get(k) - center_coordinate[i][k]) * ((int)dataSet.data_set.get((int)clusters.get(i).members.get(j)).Attrs.get(k) - center_coordinate[i][k]);
				}
			}
		}
		System.out.println(e);
	}
}

class Cluster{
	@SuppressWarnings("rawtypes")
	ArrayList members = new ArrayList();
	
	boolean equals(Cluster cluster){
		if(this.members.size() != cluster.members.size()){
			return false;
		}else{
			for(int i=0;i<this.members.size();i++){
				if((int)this.members.get(i) != (int)cluster.members.get(i))
					return false;
			}
		}
		return true;
	}
}
