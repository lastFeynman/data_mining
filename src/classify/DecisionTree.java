package classify;

import java.util.ArrayList;

public class DecisionTree {
	ArrayList<DecisionTreeNode> tree = new ArrayList<DecisionTreeNode>();
	
	DecisionTree(String path) throws Exception{
		ClassifyDataSet dataSet = new ClassifyDataSet(path);
		GenerateDecisionTree(dataSet,-1,-1);
	}

	void GenerateDecisionTree(ClassifyDataSet dataSet,int father,int fatherValue) {
		ArrayList<Class> classes = new ArrayList<Class>();
		int j;
		boolean haveClasses = false;
		for(int i=1;i<dataSet.data_set.size();i++){
			for(j=0;j<classes.size();j++){
				if(dataSet.data_set.get(i).i_class == classes.get(j).ClassID){
					classes.get(j).count++;
					break;
				}
			}
			if(j==classes.size()){
				classes.add(new Class(dataSet.data_set.get(i).i_class,1));
			}
		}
		
		if(classes.size() > 1)
			haveClasses = true;
		
		int MaxClassID=0;
		int MaxClass=0;
		
		if(!haveClasses){
			tree.add(new DecisionTreeNode(true,classes.get(0).ClassID,father,fatherValue));
		}else if(dataSet.data_set.get(0).attributes.size() == 0){
			for(int i=0;i<classes.size();i++){
				if(classes.get(i).count > MaxClass){
					MaxClass = classes.get(i).count;
					MaxClassID = i;
				}
			}
			tree.add(new DecisionTreeNode(true,MaxClassID,father,fatherValue));
		}else{
			int divideAttr;
			ID3Filter filter = new ID3Filter(dataSet);
			divideAttr = filter.attr_select();
			ClassifyDataSet childDataSet0 = new ClassifyDataSet(divideAttr,0,dataSet);
			ClassifyDataSet childDataSet1 = new ClassifyDataSet(divideAttr,1,dataSet);
			
//			for(int i=0;i<dataSet.data_set.size();i++){
//				System.out.print(dataSet.data_set.get(i).attributes.size()+" ");
//				for(int k=0;k<dataSet.data_set.get(i).attributes.size();k++){
//					System.out.print(dataSet.data_set.get(i).attributes.get(k)+" ");
//				}
//				System.out.println(dataSet.data_set.get(i).i_class);
//			}
//			
//			for(int i=0;i<childDataSet0.data_set.size();i++){
//				System.out.print(childDataSet0.data_set.get(i).attributes.size()+" ");
//				for(int k=0;k<childDataSet0.data_set.get(i).attributes.size();k++){
//					System.out.print(childDataSet0.data_set.get(i).attributes.get(k)+" ");
//				}
//				System.out.println(childDataSet0.data_set.get(i).i_class);
//			}
//			
//			for(int i=0;i<childDataSet1.data_set.size();i++){
//				System.out.print(childDataSet1.data_set.get(i).attributes.size()+" ");
//				for(int k=0;k<childDataSet1.data_set.get(i).attributes.size();k++){
//					System.out.print(childDataSet1.data_set.get(i).attributes.get(k)+" ");
//				}
//				System.out.println(childDataSet1.data_set.get(i).i_class);
//			}
			
			tree.add(new DecisionTreeNode(false,divideAttr,father,fatherValue));
			int deeperFather = tree.size()-1;
			
			GenerateDecisionTree(childDataSet0,deeperFather,0);
			GenerateDecisionTree(childDataSet1,deeperFather,1);
		}
	}
	
	public int searchTree(int[] instance){
		int i=0,j;
		while(true){
//			System.out.println(i);
			if(tree.get(i).isLeaf == true)
				break;
			
			for(j=i;j<tree.size();j++){
				if(tree.get(j).fatherNode == i && tree.get(j).fatherAttrValue == instance[tree.get(i).AttrID])
					break;
			}
			i=j;
		}
		return tree.get(i).classID;
	}
	
}

class DecisionTreeNode{
	int AttrID;
	int fatherAttrValue;
	int fatherNode;
	int classID;
	boolean isLeaf;
	
	DecisionTreeNode(boolean isLeaf,int ID,int fatherNode,int fatherAttrValue){
		if(isLeaf){
			this.isLeaf = true;
			classID = ID;
		}else{
			this.isLeaf = false;
			AttrID = ID;
		}
		this.fatherNode = fatherNode;
		this.fatherAttrValue = fatherAttrValue;
	}
}

class Class{
	int ClassID;
	int count;
	Class(int ClassID,int count){
		this.ClassID = ClassID;
		this.count = count;
	}
}