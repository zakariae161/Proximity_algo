package model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private Node parent;
	private ArrayList<Node> children;
	private String id;
	private int pathWeight;
	
	
	
    public Node() {
		children= new ArrayList<>();
	}
	public Node(Node parent, ArrayList<Node> children, String  id,int pathWieght) {
		super();
		this.parent = parent;
		this.children = children ;
		this.id=id;
		this.pathWeight= pathWieght;
	}
	public Node(Node parent, String  id,int pathWieght) {
		super();
		this.parent = parent;
		
		this.id=id;
		this.pathWeight= pathWieght;
	}
	public int addChild(Node child) {
		this.children.add(child);
		return children.size();
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public ArrayList<Node> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPathWieght() {
		return pathWeight;
	}
	public void setPathWieght(int pathWieght) {
		this.pathWeight = pathWieght;
	}

	

}
