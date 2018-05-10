//Check for AVL property of a BST
public class AVL_BST{
	//Calls recursiveCheckAVL
	//Checks for AVL property of a BST
	public static boolean checkAVL(BST b){
		if(b == null) return true;	//empty BST is balanced
		return recursiveCheckAVL(b.getRoot()); //get root node of b, call recursive method
	}

	//Recursive method
	//Checks if individual nodes are balanced
	private static boolean recursiveCheckAVL(Node n){
		boolean leftChildExist = n.leftChild != null;
		boolean rightChildExist = n.rightChild != null;

		if(leftChildExist && rightChildExist){			//n has 2 children
			int x = n.leftChild.height - n.rightChild.height;
			if(x > 1 || x < -1) return false;
			return (recursiveCheckAVL(n.leftChild) && recursiveCheckAVL(n.rightChild));
		}
		else if(leftChildExist){						//n has only left child
			if(n.leftChild.height > 1) return false;
			return recursiveCheckAVL(n.leftChild);
		}
		else if(rightChildExist){						//n has only right child
			if(n.rightChild.height > 1) return false;
			return recursiveCheckAVL(n.rightChild);
		}
		return true; //this statement is reached when n is a leaf node
	}
	
	public static BST createBST(int[] a){
		if(a.length == 0) return null;	//if a contains no item, return nothing
		BST tree = new BST(a[0]);		//initialize the BST with first item
		for(int i = 1; i < a.length; ++i){//iteratively insert all items
			tree.insertItem(a[i]);
		}
		return tree;
	}
	public static void main(String[] args){
		int[] A = {};
		BST b = createBST(A);
		System.out.println(checkAVL(b));
	}
}//end of AVL_BST

//BST of Nodes, starting at root
class BST{
	Node root;
	//BST must be initially constructed with an item for root
	public BST(int x){
		root = new Node(x, null);
	}

	//Return the root node
	public Node getRoot(){
		return root;
	}

	//Calls findParent to determine which node x should be appended to
	//Calls adjustHeight to go back up the tree, updating each nodes height
	public void insertItem(int x){
		Node n = new Node(x, findParent(x, root));
		if(x > n.parent.item) n.parent.rightChild = n;
		else n.parent.leftChild = n;
		adjustHeight(n);
	}
	
	//Recursive method
	//Returns the parent Node of the item x
	//left child: less than OR equal
	//right child: greater than
	private Node findParent(int x, Node subtree){
		if(x > subtree.item){
			if(subtree.rightChild == null) return subtree;
			return findParent(x, subtree.rightChild);
		}
		if(subtree.leftChild == null) return subtree;
		return findParent(x, subtree.leftChild);
	}

	//Recursive method
	//Increments the height of the parent node of subtree
	private void adjustHeight(Node subtree){
		if(subtree.parent == null) return;			//base case when root is reached
		else if(subtree.parent.leftChild == subtree){
			if(subtree.parent.rightChild != null){
				int hR = subtree.parent.rightChild.height;
				if(hR >= subtree.height) return;
			}
			subtree.parent.height++;
			adjustHeight(subtree.parent);
		}
		else if(subtree.parent.rightChild == subtree){
			if(subtree.parent.leftChild != null){	
				int hL = subtree.parent.leftChild.height;
				if(hL >= subtree.height) return;
			}
			subtree.parent.height++;
			adjustHeight(subtree.parent);
		}
	}
}//end of BST class

//Nodes of a BST
class Node{
	Node leftChild;
	Node rightChild;
	Node parent;
	int item;
	int height;	//current height of node in BST

	//Construct a leaf Node
	Node(int item, Node parent){
		this.item = item;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = parent;
		height = 1;
	}
}//end of Node class