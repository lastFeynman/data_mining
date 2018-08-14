package clustering;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class DBSCAN {
	static final int MINI_POINTS = 4;
	static final double RADIUS = 1.15;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception{
		DBSCAN_DataSet dataSet = new DBSCAN_DataSet();
		
		ArrayList<DBSCAN_Cluster> clusters = new ArrayList<DBSCAN_Cluster>();
		
		Random random = new Random();
		int random_instance;
		ArrayList N = new ArrayList();
		ArrayList areaSet = new ArrayList();
		
		while(dataSet.unvisited_count != 0){
			while(true){
				random_instance = random.nextInt(dataSet.dataSet.data_set.size());
				
				if(!dataSet.isVisited[random_instance]){
					dataSet.isVisited[random_instance] = true;
					break;
				}
			}
			
			if(count_points_in_area(dataSet,random_instance,areaSet) >= MINI_POINTS){
				clusters.add(new DBSCAN_Cluster(random_instance));
				N.clear();
				
				for(int i=0;i<areaSet.size();i++){
					N.add(areaSet.get(i));
				}
				
				for(int i=0;i<N.size();i++){
					if(!dataSet.isVisited[(int)N.get(i)]){
						dataSet.isVisited[(int)N.get(i)] = true;
						
						if(count_points_in_area(dataSet,(int)N.get(i),areaSet) >= MINI_POINTS){
							for(int j=0;j<areaSet.size();j++){
								N.add(areaSet.get(j));
							}
						}
					}
					
					if(!is_in_clusters(clusters,(int)N.get(i))){
						clusters.get(clusters.size()-1).members.add(N.get(i));
					}
				}
			}else{
				dataSet.isNoise[random_instance] = true;
			}
			
			dataSet.count_unvisited();
		}
		File output_file = new File("./data/cluster/DBSCAN_out.txt");
		FileWriter writer = new FileWriter(output_file);
		writer.write("离群点：\n");
		int count_out=0;
		//离群点
		for(int i=0;i<dataSet.dataSet.data_set.size();i++){
			if(!is_in_clusters(clusters,i)){
				count_out++;
				System.out.print(i+" ");
				writer.write(dataSet.dataSet.data_set.get(i).medicineName+" ");
			}
		}
		System.out.println("\n"+count_out+"个离群点");
		writer.write("\n"+count_out+"个离群点"+"\n聚类结果：\n");
		
		//聚类结果
		for(int i=0;i<clusters.size();i++){
			for(int j=0;j<clusters.get(i).members.size();j++){
				System.out.print(clusters.get(i).members.get(j)+" ");
				writer.write(dataSet.dataSet.data_set.get((int)clusters.get(i).members.get(j)).medicineName+" ");
			}
			System.out.println(clusters.get(i).members.size()+"个");
			writer.write(clusters.get(i).members.size()+"个\n");
		}
		writer.flush();
		writer.close();
	}
	
	@SuppressWarnings("unchecked")
	static int count_points_in_area(DBSCAN_DataSet dataSet,int instance,@SuppressWarnings("rawtypes") ArrayList areaSet){
		areaSet.clear();
		int count=0;
		double distance;
		
		for(int i=0;i<dataSet.dataSet.data_set.size();i++){
			distance = 0;
			for(int j=0;j<dataSet.dataSet.xing_attrs.size()+dataSet.dataSet.wei_attrs.size()+dataSet.dataSet.guijing_attrs.size();j++){
				distance = distance + ((int)dataSet.dataSet.data_set.get(i).Attrs.get(j) - (int)dataSet.dataSet.data_set.get(instance).Attrs.get(j))*((int)dataSet.dataSet.data_set.get(i).Attrs.get(j) - (int)dataSet.dataSet.data_set.get(instance).Attrs.get(j));
			}
			distance = Math.sqrt(distance);
			
			if(distance <= RADIUS){
				count++;
				areaSet.add(i);
			}
		}
		return count;
	}
	
	static boolean is_in_clusters(ArrayList<DBSCAN_Cluster> clusters,int instance){
		for(int i=0;i<clusters.size();i++){
			if(clusters.get(i).members.contains(instance))
				return true;
		}
		return false;
	}
}

class DBSCAN_DataSet{
	ClusterDataSet dataSet;
	boolean isVisited[],isNoise[];
	int unvisited_count;
	
	DBSCAN_DataSet() throws Exception{
		dataSet = new ClusterDataSet("./data/cluster/data1.txt","./data/cluster/data2.txt","./data/cluster/data3.txt","./data/cluster/data4.txt");
		unvisited_count = dataSet.data_set.size();
		
		isVisited = new boolean[dataSet.data_set.size()];
		isNoise = new boolean[dataSet.data_set.size()];
		
		for(int i=0;i<dataSet.data_set.size();i++){
			isVisited[i] = false;
			isNoise[i] = false;
		}
	}
	
	void count_unvisited(){
		int count=0;
		for(int i=0;i<dataSet.data_set.size();i++){
			if(!isVisited[i])
				count++;
		}
		unvisited_count = count;
	}
}

class DBSCAN_Cluster{
	@SuppressWarnings("rawtypes")
	ArrayList members = new ArrayList();
	
	@SuppressWarnings("unchecked")
	DBSCAN_Cluster(int instance){
		members.add(instance);
	}
}