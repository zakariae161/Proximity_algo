package treeProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Bucket;
import model.Node;
import utilities.Parameter;

public class hierarchy {

	private static  Node child1;
	
	private static Map<String, String> diseaseId ;
	static{
 // nodes of the tree
		 child1 = new Node();
		Node child2 = new Node();
		Node child3 = new Node();
		Node child4 = new Node();
		Node child5 = new Node();
		Node child6 = new Node();
		Node child7 = new Node();
		Node child8 = new Node();
		Node child9 = new Node();
		Node child10 = new Node();
		Node child11 = new Node();
		Node child12 = new Node();
		Node child13 = new Node();
		Node child14 = new Node();
		Node child15 = new Node();
		Node child16 = new Node();
		Node child17 = new Node();

	

	
		// this map holds for each disease  a disease ID to simplify the processing of the tree 
		diseaseId = new HashMap<>();
		diseaseId.put("respiratory&digestive system diseases", "d1");
		diseaseId.put("vascular lung diseases", "d2");
		diseaseId.put("digestive system diseases", "d3");
		diseaseId.put("respiratory infection", "d4");
		diseaseId.put("vascular lung diseases2", "d5");
		diseaseId.put("stomach  diseases ", "d6");
		diseaseId.put("colon diseases", "d7");
		diseaseId.put("flu", "d8");
		diseaseId.put("pneumonia", "d9");
		diseaseId.put("bronchitis", "d10");
		diseaseId.put("pulmonary edema", "d11");
		diseaseId.put("pulmonary embolism", "d12");
		diseaseId.put("gastric ulcer", "d13");
		diseaseId.put("stomach cancer", "d14");
		diseaseId.put("gastritis", "d15");
		diseaseId.put("colitis", "d16");
		diseaseId.put("colon cancer", "d17");
		// end

		// constructing the tree
		
		// chiled 1

		child1.setId("d1");
		child1.setParent(null);
		child1.setPathWieght(1);

		// chiled 2
		child2.setId("d2");
		child2.setPathWieght(1);
		child2.setParent(child1);

		// child3
		child3.setId("d3");
		child3.setPathWieght(2);
		child3.setParent(child1);

		// child4
		child4.setId("d4");
		child4.setPathWieght(1);
		child4.setParent(child2);

		// child5
		child5.setId("d5");
		child5.setPathWieght(2);
		child5.setParent(child2);
		// child6
		child6.setId("d6");
		child6.setPathWieght(1);
		child6.setParent(child3);

		// child7
		child7.setId("d7");
		child7.setPathWieght(2);
		child7.setParent(child3);
		// child8
		child8.setId("d8");
		child8.setPathWieght(1);
		child8.setParent(child4);

		// child9
		child9.setId("d9");
		child9.setPathWieght(2);
		child9.setParent(child4);
		// child10
		child10.setId("d10");
		child10.setPathWieght(3);
		child10.setParent(child4);

		// child11
		child11.setId("d11");
		child11.setPathWieght(1);
		child11.setParent(child5);

		// child12
		child12.setId("d12");
		child12.setPathWieght(2);
		child12.setParent(child5);
		// child13
		child13.setId("d13");
		child13.setPathWieght(1);
		child13.setParent(child6);
		// child14
		child14.setId("d14");
		child14.setPathWieght(2);
		child14.setParent(child6);
		// child15
		child15.setId("d15");
		child15.setPathWieght(3);
		child15.setParent(child6);

		// child16
		child16.setId("d16");
		child16.setPathWieght(1);
		child16.setParent(child7);
		// child15
		child17.setId("d17");
		child17.setPathWieght(2);
		child17.setParent(child7);

		/*--------------------------*/
//		child1.addChild(child3);
//		child1.addChild(child4);
//		tree.addChild(child1);
//		tree.addChild(child2);
//		child3.addChild(child5);
		
		child1.addChild(child2);child1.addChild(child3);
		
		child2.addChild(child4);
		child2.addChild(child5);
		
		child3.addChild(child6);
		child3.addChild(child7);

		child4.addChild(child8);
		child4.addChild(child9);
		child4.addChild(child10);
		
		child5.addChild(child11);
		child5.addChild(child12);
		
		child6.addChild(child13);
		child6.addChild(child14);
		child6.addChild(child15);
		
		child7.addChild(child16);
		child7.addChild(child17);

	}
	
	// getter for the tree
	public static  Node getTree() {
		return child1;
	}
	
	
	
	
	
	// attribute to each record a DiseaseId 
	public static  void setDiseaseId(List<Bucket> bucketList) {
		
		// for each bucket of the list 
		for(Bucket b :bucketList) {
			// for each record of the bucket  , get the disease name  and find the corresponding  DiseaseID
			
			for(Map<String, String> ele:b.getRecords()) {
				String  keString=ele.get(Parameter.diseaseColumn).toLowerCase();
				System.out.println(keString);
				
				// add the disease ID to the record  
				ele.put("diseaseid", diseaseId.get(keString));
			//	System.out.println(ele.get("diseaseid"));
			}
		}
		
	}
	
	
	
	
}
