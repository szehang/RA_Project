import java.util.ArrayList;
import java.util.List;

public class NLPTreeNode<T> {

	T data;
	String type;
	int level;
	int levelID;
	int nodeID;
	int childID;
	int tokenID;
	int startPos, endPos;
	NLPTreeNode<T> parent;
	List<NLPTreeNode<T>> children;
	NLPTreeNode<T> nextToken;
	String compOptions;

	public NLPTreeNode(T data, String type) {
		this.data = data;
		this.type = type;
	}

	public NLPTreeNode(T data, String type, int nodeID) {
		this.children = new ArrayList<NLPTreeNode<T>>();
		this.data = data;
		this.type = type;
		this.nodeID = nodeID;
	}

	public NLPTreeNode(T data, String type, int level, int nodeID) {
		this.data = data;
		this.type = type;
		this.level = level;
		this.nodeID = nodeID;
	}

	public NLPTreeNode(T data, String type, int level, int levelID, int childID, int startPos, int endPos,
			String options) {
		this.data = data;
		this.type = type;
		this.level = level;
		this.levelID = levelID;
		this.childID = childID;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compOptions = options;
		this.children = new ArrayList<NLPTreeNode<T>>();
	}

	NLPTreeNode() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public NLPTreeNode<T> addChild(T child, String type) {
		NLPTreeNode<T> childNode = new NLPTreeNode<T>(child, type);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public NLPTreeNode<T> addChild(T child, String type, int nodeID) {
		NLPTreeNode<T> childNode = new NLPTreeNode<T>(child, type, nodeID);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public NLPTreeNode<T> addChild(T child, String type, int level, int levelID, int ChildID, int startPos, int endPos,
			String options) {
		NLPTreeNode<T> childNode = new NLPTreeNode<T>(child, type, level, levelID, childID, startPos, endPos, options);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public void printData() {
		System.out.println(data);
	}
	// other features ...

	//START of print tree
	//ONLY printWholeTree should be called
	public void printWholeTree() {
		this.assignChildId();
		this.assignTokenId();
		int level = 0;
		System.out.println(this.data + " " + this.tokenID);
		level++;
		if (!this.children.isEmpty()) {
			for (NLPTreeNode<T> child : this.children)
				child.printTree(level);
		}
	}

	public void printTree(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("	");
		}
		System.out.println(this.data + " " + this.tokenID);
		level++;
		if (!this.children.isEmpty()) {
			for (NLPTreeNode<T> child : this.children)
				child.printTree(level);
		}
	}
	//END of print tree

	// list all leaf node
	public List<NLPTreeNode<T>> leafNodeList() {
		List<NLPTreeNode<T>> leafNode = new ArrayList<NLPTreeNode<T>>();
		if (this.isLeaf()) {
			leafNode.add(this);
			return leafNode;
		} else {
			if (!this.children.isEmpty()) {
				for (NLPTreeNode<T> child : this.children) {
					leafNode.addAll(child.leafNodeList());
				}
			}
		}
		return leafNode;
		
	}

	// public

	
	
	//CHECKER//
	public boolean isLeaf() {
		if (this.type.compareTo("data") == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isLastToken() { //check the current token is the last token or not
		if(this.nextToken == null) {
			return true;
		} else {
			return false;
		}
	}
	
	//GETTER//
	public NLPTreeNode<T> getRoot() {
		if(this.parent == null) {
			return this;
		} else {
			return this.parent.getRoot();
		}
	}
	
	public NLPTreeNode<T> getFirstToken() { //get the first token of tree
		return this.getRoot().leafNodeList().get(0);
	}
	
	public NLPTreeNode<T> getLastToken() { //get the last token of tree
		return this.getRoot().leafNodeList().get(this.getRoot().leafNodeList().size() - 1);
	}
	
	//ID ASSIGNMENT//
	
	//should be called by the root ONLY
	//MUST call after the whole tree was builded
	public void assignChildId() { 
		/////////////
		NLPTreeNode<T> root = this;
		if(root.isLeaf()) {
			return;
		} else {
			if(this.children.size() > 0) {
				int idCount = 0;
				for(NLPTreeNode<T> child: this.children) {
					child.childID = idCount++;
					child.assignChildId();
				}
			}
		}
	}
	
	public void assignTokenId() {
		int idCount = 0;
		for(NLPTreeNode<T> token: this.leafNodeList()) {
			token.tokenID = idCount++;
		}
		
	}
	
	public void assignNextToken() {
		List<NLPTreeNode<T>> tokenList = this.getRoot().leafNodeList();
		for(int i = 0 ; i < tokenList.size() - 1; i++) {
			tokenList.get(i).nextToken = tokenList.get(i + 1); 
		}
		tokenList.get(tokenList.size() - 1).nextToken = null;
	}
	
	public void initialAssign() {
		this.assignChildId();
		this.assignTokenId();
		this.assignNextToken();
	}

}