package associations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FP_GrowthMining {
	
	static DataSet dataSet;
	static final int SUPPORT = 5;
	static ArrayList<_1FrequentSet> _1_frequent_set = new ArrayList<_1FrequentSet>();
	static ArrayList<FP_TreeNode> FP_Tree = new ArrayList<FP_TreeNode>();
	static SortedDataSet sorted_set;

	public static void main(String[] args) throws IOException {
		Date date1 = new Date();
		long time1,time2;
		time1 = date1.getTime();
		
		dataSet = new DataSet("./data/out.txt");
		gen_1_frequent_item_set(dataSet,_1_frequent_set);
		_1FS_Sorter _1FS_S = new _1FS_Sorter(_1_frequent_set);
		_1FS_S._1FS_Sort();
		
		sorted_set = new SortedDataSet(dataSet,_1_frequent_set);

		gen_tree(FP_Tree,sorted_set);

		link_node(FP_Tree,_1_frequent_set);

		ArrayList<Frequent_Pattern> mid_result;
		ArrayList<Frequent_Pattern> result = new ArrayList<Frequent_Pattern>();

		for(int i=_1_frequent_set.size()-1;i>=0;i--){
			mid_result = FP_Growth(FP_Tree,i,_1_frequent_set);
			if(mid_result.size() == 0)
				continue;
			for(int j=0;j<mid_result.size();j++){
				result.add(mid_result.get(j));
				result.get(result.size()-1).frequent_set.add(_1_frequent_set.get(i).name);
			}
		}

		ArrayList<Frequent_Pattern> sorted_frequent_set = new ArrayList<Frequent_Pattern>();
		int item_num = 2,item_num_count=0;
		FP_Sorter FP_S;
		
		File frequent_set_filename = new File("./data/frequent_file_FP.txt");
		FileWriter writer = new FileWriter(frequent_set_filename);
		
		for(int i=0;i<result.size();i++)
			if(result.get(i).frequent_set.size() == item_num)
				item_num_count++;
		while(item_num_count != 0){
			sorted_frequent_set.clear();
			for(int i=0;i<result.size();i++){
				if(result.get(i).frequent_set.size() == item_num){
					sorted_frequent_set.add(result.get(i));
				}
			}
			FP_S = new FP_Sorter(sorted_frequent_set);
			FP_S.FP_Sort();
			for(int i=0;i<sorted_frequent_set.size();i++){
				if(i>=10)
					break;
				for(int j=0;j<sorted_frequent_set.get(i).frequent_set.size();j++){
					writer.write(sorted_frequent_set.get(i).frequent_set.get(j)+" ");
					System.out.print(sorted_frequent_set.get(i).frequent_set.get(j)+" ");
				}
				System.out.println(sorted_frequent_set.get(i).count);
				writer.write(sorted_frequent_set.get(i).count+"\n");
			}
			item_num++;
			item_num_count=0;
			for(int i=0;i<result.size();i++)
				if(result.get(i).frequent_set.size() == item_num)
					item_num_count++;
		}
		writer.close();
		Date date2 = new Date();
		time2 = date2.getTime();
		System.out.println(time2-time1);
	}
	
	static void gen_1_frequent_item_set(DataSet dataSet,ArrayList<_1FrequentSet> _1set){
		int k = 0;
		//Get All Medicine And Count
		for(int i=0;i<dataSet.data_set.size();i++){
			for(int j=0;j<dataSet.data_set.get(i).row.size();j++){
				for(k=0;k<_1set.size();k++)
					if(dataSet.data_set.get(i).row.get(j).equals(_1set.get(k).name)){
						_1set.get(k).count++;
						break;
					}
				if(k==_1set.size())
					_1set.add(new _1FrequentSet(dataSet.data_set.get(i).row.get(j),1));
			}
		}
		
		//Remove Unsatisfying Medicine
		for(int i=0;i<_1set.size();i++){
			if(_1set.get(i).count < SUPPORT){
				_1set.remove(i);
				i--;
			}
		}
	}
	
	static void gen_tree(ArrayList<FP_TreeNode> tree,SortedDataSet dataSet){
		tree.add(new FP_TreeNode(null,-1));
		int father;
		int k;
		for(int i=0;i<dataSet.sorted_data_set.size();i++){
			father = 0;
			for(int j=0;j<dataSet.sorted_data_set.get(i).col.size();j++){
				for(k=1;k<tree.size();k++){
					if(tree.get(k).name.equals(dataSet.sorted_data_set.get(i).col.get(j)) && (tree.get(k).father_node == father)){
						father = k;
						tree.get(k).node_weight++;
						break;
					}
				}
				if(k==tree.size()){
					tree.add(new FP_TreeNode(dataSet.sorted_data_set.get(i).col.get(j),father));
					father = tree.size()-1;
				}
			}
		}
	}
	
	static void link_node(ArrayList<FP_TreeNode> tree,ArrayList<_1FrequentSet> _1set){
		int last_node;
		for(int i=0;i<_1set.size();i++){
			last_node = 0;
			for(int j=1;j<tree.size();j++){
				if(tree.get(j).name.equals(_1set.get(i).name)){
					if(last_node == 0){
						_1set.get(i).first_node = j;
						last_node = j;
					}
					else{
						tree.get(last_node).next_node = j;
						last_node = j;
					}
				}
			}
		}
	}
	
	static ArrayList<Frequent_Pattern> FP_Growth(ArrayList<FP_TreeNode> tree,int suffix,ArrayList<_1FrequentSet> _1set){
		int nextNode = _1set.get(suffix).first_node;
		int father;
		int rowNo = 1,flag = 0;
		DataSet childDataSet = null;
		ArrayList<_1FrequentSet> child_1set = new ArrayList<_1FrequentSet>();
		SortedDataSet child_sorted_set;
		ArrayList<FP_TreeNode> child_tree = new ArrayList<FP_TreeNode>();
		
		ArrayList<Frequent_Pattern> result = new ArrayList<Frequent_Pattern>();
		ArrayList<Frequent_Pattern> mid_result;
		//Get the child DataSet
		while(nextNode != 0){
			if((tree.get(nextNode).father_node != 0) && (flag == 0)){
				childDataSet = new DataSet();
				flag = 1;
			}
			for(int i=0;i<tree.get(nextNode).node_weight;i++){
				if(tree.get(nextNode).father_node != 0){
					childDataSet.data_set.add(new O_Row(rowNo));
					rowNo++;
				}
				father = tree.get(nextNode).father_node;
				while(father != 0){
					childDataSet.data_set.get(rowNo-2).row.add(tree.get(father).name);
					father = tree.get(father).father_node;
				}
			}
			nextNode = tree.get(nextNode).next_node;
		}
		
//		for(int i=0;i<childDataSet.data_set.size();i++){
//			for(int j=0;j<childDataSet.data_set.get(i).row.size();j++)
//				System.out.println(childDataSet.data_set.get(i).row.get(j));
//		}
		//Get child tree
		if(childDataSet == null){
			return result;
		}
		gen_1_frequent_item_set(childDataSet,child_1set);
		_1FS_Sorter _1FS_S = new _1FS_Sorter(child_1set);
		_1FS_S._1FS_Sort();
		child_sorted_set = new SortedDataSet(childDataSet,child_1set);
		gen_tree(child_tree,child_sorted_set);
		link_node(child_tree,child_1set);
		
//		System.out.println();
//		for(int i=0;i<child_1set.size();i++)
//			System.out.println(i+child_1set.get(i).name+child_1set.get(i).count+"  "+child_1set.get(i).first_node);
//		for(int i=1;i<child_tree.size();i++){
//			System.out.println(i+child_tree.get(i).name+child_tree.get(i).next_node+" "+child_tree.get(i).father_node);
//		}
		
		boolean haveBranch=false;
		for(int i=1;i<child_tree.size();i++){
			if(child_tree.get(i).father_node != i-1){
				haveBranch = true;
				break;
			}
		}
			
		
		if(!haveBranch){
			result = combine(child_1set);
		}else{
			for(int i=0;i<child_1set.size();i++){
				result.add(new Frequent_Pattern(child_1set.get(i).count,child_1set.get(i).name));
			}
			for(int i=child_1set.size()-1;i>=0;i--){
				mid_result = FP_Growth(child_tree,i,child_1set);
				for(int j=0;j<mid_result.size();j++){
					result.add(mid_result.get(j));
					result.get(result.size()-1).frequent_set.add(child_1set.get(i).name);
				}
			}
		}
		
		return result;
	}
	
	static ArrayList<Frequent_Pattern> combine(ArrayList<_1FrequentSet> _1set){
		ArrayList<Frequent_Pattern> frequent_pattern = new ArrayList<Frequent_Pattern>();
		ArrayList<Frequent_Pattern> suffix_F_P;
		
		ArrayList<_1FrequentSet> child_1set = new ArrayList<_1FrequentSet>();
		
		if(_1set.size() == 0){
			return frequent_pattern;
		}else if(_1set.size() == 1){
			frequent_pattern.add(new Frequent_Pattern(_1set.get(0).count,_1set.get(0).name));
			return frequent_pattern;
		}else{
			frequent_pattern.add(new Frequent_Pattern(_1set.get(0).count,_1set.get(0).name));
			for(int i=1;i<_1set.size();i++){
				child_1set.add(new _1FrequentSet(_1set.get(i).name,_1set.get(i).count));
			}
			suffix_F_P = combine(child_1set);
			for(int i=0;i<suffix_F_P.size();i++){
				frequent_pattern.add(new Frequent_Pattern(suffix_F_P.get(i).count,_1set.get(0).name));
				for(int j=0;j<suffix_F_P.get(i).frequent_set.size();j++){
					frequent_pattern.get(i+1).frequent_set.add(suffix_F_P.get(i).frequent_set.get(j));
				}
			}
			for(int i=0;i<suffix_F_P.size();i++){
				frequent_pattern.add(suffix_F_P.get(i));
			}
			return frequent_pattern;
		}
	}
}

class SortedDataSet{
	@SuppressWarnings("rawtypes")
	class Col implements Comparable{
		String name;
		int place;
		
		Col(String name,int place){
			this.name = name;
			this.place = place;
		}
		@Override
		public int compareTo(Object col) {
			if(this.place>((Col)col).place)
				return 1;
			else if(this.place==((Col)col).place)
				return 0;
			else
				return -1;
		}
	}
	class Col_Sorter{
		ArrayList<Col> row = new ArrayList<Col>();
		
		Col_Sorter(ArrayList<Col> row){
			this.row = row;
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Col> Col_Sort(){
			Collections.sort(row);
			return row;
		}
	}
	class SortedRow{
		ArrayList<String> col = null;
		SortedRow(O_Row row,ArrayList<_1FrequentSet> _1_set){
			ArrayList<Col> row_list = new ArrayList<Col>();
			int j;
			for(int i=0;i<row.row.size();i++){
				for(j=0;j<_1_set.size();j++)
					if(_1_set.get(j).name.equals(row.row.get(i)))
						break;
				
				if(j<_1_set.size())
					row_list.add(new Col(row.row.get(i),j));
			}
			if(row_list.size() == 0)
				return;
			
			Col_Sorter _1FS_S = new Col_Sorter(row_list);
			row_list = _1FS_S.Col_Sort();
			col = new ArrayList<String>();
			
			for(int i=0;i<row_list.size();i++){
				col.add(row_list.get(i).name);
			}
		}
	}
	ArrayList<SortedRow> sorted_data_set = new ArrayList<SortedRow>();
	
	SortedDataSet(DataSet dataset,ArrayList<_1FrequentSet> _1_set){
		SortedRow sr = null;
		for(int i=0;i<dataset.data_set.size();i++){
			sr = new SortedRow(dataset.data_set.get(i),_1_set);
			if(sr.col != null)
				sorted_data_set.add(sr);
		}
	}
}

@SuppressWarnings("rawtypes")
class _1FrequentSet implements Comparable{
	
	String name;
	int count = 0;
	int first_node = 0;
	
	_1FrequentSet(String name){
		this.name = name;
	}
	
	_1FrequentSet(String name,int n){
		this.name = name;
		count = n;
	}
	
	public int compareTo(Object _1FS){
		if(this.count>((_1FrequentSet)_1FS).count)
			return -1;
		else if(this.count==((_1FrequentSet)_1FS).count)
			return 0;
		else
			return 1;
	}
}

class _1FS_Sorter{
	ArrayList<_1FrequentSet> _1FS;
	
	_1FS_Sorter(ArrayList<_1FrequentSet> _1fs){
		_1FS = _1fs;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<_1FrequentSet> _1FS_Sort(){
		Collections.sort(_1FS);
		return _1FS;
	}
}

class FP_TreeNode{
	String name;
	int node_weight = 1;
	int next_node = 0;
	int father_node;
	
	FP_TreeNode(String name,int father_node){
		this.name = name;
		this.father_node = father_node;
	}
}

@SuppressWarnings("rawtypes")
class Frequent_Pattern implements Comparable{
	ArrayList<String> frequent_set = new ArrayList<String>();;
	int count;
	Frequent_Pattern(int count,String name){
		this.count = count;
		frequent_set.add(name);
	}
	
	@Override
	public int compareTo(Object FP) {
		if(this.count>((Frequent_Pattern)FP).count)
			return -1;
		else if(this.count==((Frequent_Pattern)FP).count)
			return 0;
		else
			return 1;
	}
}

class FP_Sorter{
	ArrayList<Frequent_Pattern> FP;
	
	FP_Sorter(ArrayList<Frequent_Pattern> fp){
		FP = fp;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Frequent_Pattern> FP_Sort(){
		Collections.sort(FP);
		return FP;
	}
}