/**
 * 
 * אביטל חיימן
 * avitalhaiman
 * 
 * 
 * אור פיקהולץ
 * orpickholz 
 */

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	
	private IAVLNode root ; //the root of the AVLTree
	private IAVLNode minimum ; // keeping a pointer to minimal node
	private IAVLNode maximum ; // keeping a pointer to maximal node
	
	/**
	 * public AVLTree()
	 * sets the trees root as a new AVLNode with parent "null".
     */
	public AVLTree() 
	{
		this.root = new AVLNode(null);
	}
	
	/**
	 * public boolean empty()
	 * @return true for an empty tree.
     */
	public boolean empty() 
	{
		if(this.root.isRealNode()==false)
			return true;
		return false; 
	}
	
	/**
	 * public IAVLNode searchForParent(int k, int diff)
	 * @pre node with key k is in the tree.
	 * @return pointer to the parent of node with key k.
	 */
	 public IAVLNode searchForParent(int k, int diff) {
		  IAVLNode curr = root;
		  while (curr != null && curr.getHeight() != -1 ) 
		  {
			  if(curr.getKey() < k) 
			  {
				if(curr.getRight().getHeight()== -1)
					return curr;
				curr = curr.getRight();	 
			  }
			  else 
			  {
				  if(curr.getLeft().getHeight()== -1)
					  return curr;
				  curr = curr.getLeft();	
			  }
		  }
		return null;  
	  }
	  
	/**
	 * public IAVLNode searchFor(IAVLNode curr, int k)
	 * @return a pointer to node with key k.
	 * if there is no node with key k in the tree, return an external node, a virtual node 
	 */
	 public IAVLNode searchFor(IAVLNode curr, int k) {
		  while (curr!= null && curr.isRealNode()) {
			  if(curr.getKey() == k)
				  return curr;
			  if(curr.getKey() > k)
				  curr = curr.getLeft();
			  else 
				  curr = curr.getRight();	  
		  }
		return curr;  
	  }
	  
	/**
	 * public String search(int k)
	 * @return the value of node with key k.
	 * if there is no node with key k in the tree, returns null.
	 */
	  public String search(int k)
	  {
		IAVLNode curr = searchFor(this.root, k);
		if(curr.isRealNode()==false)
			return null;
		return curr.getValue();
	  }
	   
		
	/**
	 * public int BFCalc(IAVLNode node)
	 * @return a node's Balance Factor
	 */
	public int BFCalc(IAVLNode node)
	  {
		if(node.isRealNode())
			  return node.getLeft().getHeight()-node.getRight().getHeight();
		return 0; //External node's balance factor
	  }
	
	/**
	 * private IAVLNode sizeCalc(IAVLNode node)
	 * @return the size of a node, the number of sons it has + 1 for itself.
	 */
	private int sizeCalc(IAVLNode node) {
		if (node.isRealNode()==true)
		{
			int result = 1; //1 for node itself
			//adding the number of nodes in node's right subtree
			if (node.getRight().isRealNode()) {
				result += node.getRight().getSize();
			}
			//adding the number of nodes in node's left subtree
			if (node.getLeft().isRealNode()) {
				result += node.getLeft().getSize();
			}
			return result;	
		} 
		// if node is a virtual node, then it's size is 0  
			return 0;
	}
	
	/**
	 * private void SizesUpdate(IAVLNode parent)
	 * updates the size of a node(parent of a new node)
	 */
	private void SizesUpdate(IAVLNode parent) {  
			IAVLNode curr = parent;
			
			//down check
			if(curr != null && curr.getRight().isRealNode())
				curr.getRight().setSize(curr.getRight().getRight().getSize() + curr.getRight().getLeft().getSize() + 1);
			if(curr != null && curr.getLeft().isRealNode())
				curr.getLeft().setSize(curr.getLeft().getRight().getSize() + curr.getLeft().getLeft().getSize() + 1);
			
			while(curr != null)
			   {
				   int newSize = curr.getRight().getSize() + curr.getLeft().getSize() + 1;
				   if(curr.getSize() != newSize)
				     	 curr.setSize(newSize);
					curr = curr.getParent();   
			   }
		}
	
	/**
	 * private int HeightCalc(IAVLNode node)
	 * @return a node's height.
	 * if node is an external node, a virtual node, returns -1.
	 */
	private int HeightCalc(IAVLNode node) {
		if (node.isRealNode()) 
			return Math.max(node.getLeft().getHeight(), node.getRight().getHeight())+1;
		return -1; //external's height is -1 
	}	
	
	/**
	 * public int FixAVLtree(IAVLNode node)
	 * decides which correction is needed in order to maintain the AVL shape of the tree.
	 * calls the methods that rotate the tree.
	 * @return 1 for LL/RR rotations, or 2 for RL/LR rotations.
	 */
	private int FixAVLtree(IAVLNode node)
	{
		if (node.isRealNode()) 
		{
			if(BFCalc(node) == -2) 
			{
				//in insert we need to fix if BFCalc(node.getLeft())==-1
				//in delete we need to fix if BFCalc(node.getLeft())==0 or -1
				int rightBF=BFCalc(node.getRight());
				if(rightBF==0||rightBF==-1) {
					RRRotation(node);
					return 1;
				} 
				else 
				{ 
					RLRotation(node);
					return 2;
				}
			}
			else //if BFCalc(node) == 2 
			{
				//in insert we need to fix if BFCalc(node.getLeft())==1
				//in delete we need to fix if BFCalc(node.getLeft())==0 or 1
				int leftBF=BFCalc(node.getLeft());
				if(leftBF==1 ||leftBF==0)  
				{
					LLRotation(node);
					return 1;
				}
				else  
				{ 
					LRRotation(node);
					return 2;
				}					
			} 
		}  
			return 0;
	}

	/**
	 * private void LLRotation(IAVLNode node)
	 * rotates the tree with a LL rotation.
	 */
	private void LLRotation(IAVLNode node)
	{
		IAVLNode parent = node.getParent(); 
		IAVLNode leftChild = node.getLeft(); 
		IAVLNode rightGrandChild = node.getLeft().getRight(); 
										  
		if(node != root) 
		{
			if(node == parent.getLeft())   
				parent.setLeft(leftChild);
			else 
				parent.setRight(leftChild);
		}
		node.setLeft(rightGrandChild);   // rightGrandChild is now node's left child
		rightGrandChild.setParent(node); // node becomes rightGrandChild's parent
		leftChild.setRight(node); // node is now leftChild's right child
		node.setParent(leftChild); // node's parent is now leftChild
		leftChild.setParent(parent); // leftChild's parent is now node's old parent
		
		//updating sizes and heights
		node.setSize(sizeCalc(node)); // updating node's size 
		leftChild.setSize(sizeCalc(leftChild)); //updating leftChild's size
		leftChild.setHeight(HeightCalc(leftChild)); // updating leftChild's height
		node.setHeight(HeightCalc(node)); // updating node's height

		if (node == root)
			this.root = leftChild;
	}

	/**
	 * private void RRRotation(IAVLNode node)
	 * rotates the tree with a RR rotation.
	 */
	private void RRRotation(IAVLNode node)
	{
		IAVLNode parent = node.getParent(); 
		IAVLNode rightChild = node.getRight() ; 
		IAVLNode leftGrandChild = node.getRight().getLeft(); 
		
		if(node != root) 
		{
			if(node == parent.getLeft())
				parent.setLeft(rightChild);
			else 
				parent.setRight(rightChild);
		}
		
		node.setRight(leftGrandChild);   //leftGrandChild is now node's right child
		leftGrandChild.setParent(node); //node is now leftGrandChild's parent
		rightChild.setLeft(node); // node is now rightChild's left child
		node.setParent(rightChild); // node's parent is now leftChild
		rightChild.setParent(parent); // leftChild's parent is now node's old parent
		
		//updating sizes and heights
		node.setSize(sizeCalc(node)); // updating node's size
		rightChild.setSize(sizeCalc(rightChild)); //updating rightChild's size
		rightChild.setHeight(HeightCalc(rightChild)); // updating rightChild's height
		node.setHeight(HeightCalc(node)); // updating node's height
		
		if (node == root) 
			this.root = rightChild;
	}
	
	/**
	 * public void RLRotation(IAVLNode node)
	 * makes a RL rotation.
	 * calls LLRotation method, and afterwards calls RRRotation method.
	 */
	private void RLRotation(IAVLNode node)
	{
		LLRotation(node.getRight());
		RRRotation(node);
	}
	/**
	 * public void LRRotation(IAVLNode node)
	 * makes a LR rotation.
	 * calls RRRotation method, and afterwards calls LLRotation method.
	 */
	private void LRRotation(IAVLNode node)
	{

		RRRotation(node.getLeft());
		LLRotation(node);
	}
	
	/**
	 * public int insert(int k, String i)
	 * inserts a node with key k, value i to the tree, 
	 * and calls a method that fixes the tree after the insertion.
	 * if the node we wish to insert to the tree is already in the tree, then returns -1.
	 * @return the number of rebalancing operations.
	 */
	public int insert(int k, String i) {
		int countOp = 0 ;   // counter for the amount of rotations
		IAVLNode newNode = new AVLNode(k, i); // create a new leaf node with the key and value 

		if (this.empty()) 
		{ //if the tree is empty we need to create the root
			root = newNode; 
			newNode.setParent(null);
			maximum = newNode;
			minimum = newNode; 
			return countOp;
		}
		//checking if node with key k is already in the tree
		IAVLNode node = searchFor(root, k);  
		if (node.getKey() == k)   
			return -1;
		
		else //inserting newNode to the tree
		 {
			//updating minimum or maximum of the tree if needed
			if(k>maximum.getKey())
				maximum=newNode;
			else
				if(k < minimum.getKey()) 
					minimum = newNode;

			IAVLNode parent = searchForParent(k,1);
			newNode.setParent(parent);
			
			if (node.getParent().getKey() < k)
				parent.setRight(newNode);			
			else 
				parent.setLeft(newNode);
			
			countOp = HieghtsUpdating(newNode);
		}
		return countOp;	
	}

	/**
	 * private int HieghtsUpdating(IAVLNode node)
	 * updates the size and height of each node we go through while fixing the tree.  
	 * calls a method that fixes the tree.
	 * @return the number of the rebalancing operations.
	 */
	private int HieghtsUpdating(IAVLNode node) 
	{
		int moneRot = 0;
		while (node != null) //as long as we didn't reached root's parent
		{
			if ((BFCalc(node) < -1) || (BFCalc(node) > 1)) //if AVL isn't balanced 
				moneRot+= FixAVLtree(node);  
 
			node.setSize(sizeCalc(node));
			if(HeightCalc(node) != node.getHeight()) {
				moneRot += Math.abs(HeightCalc(node)-node.getHeight());
				node.setHeight(HeightCalc(node));
			}
			node = node.getParent();
		}
		return moneRot;
	}

	/**
	 * public int delete(int k)
	 * deletes a node from the tree, and calls a method that 
	 * fixes the tree after the deletion if needed.
	 * @return -1 if the node we wish to delete is not in the tree.
	 * else, returns the number of rebalancing operations.
	 */
	public int delete(int k)
	{
		IAVLNode node = searchFor(this.root, k);
		IAVLNode parent;
		if(node==root)
			parent=new AVLNode(null);
		else
			parent=node.getParent();
		
		IAVLNode startHieghtUpdate = parent; // this will be the node from which we'll start updating the nodes' hieghts
		
		if (!node.isRealNode())
			return -1;
		
		//updating the maximum and minimum fields
		if (node == this.maximum)
			this.maximum = findPredecessor(node);
		else
			if (node ==  this.minimum)
				this.minimum = findSuccessor(node); 
		
		//if the node we wish to delete is a leaf
		if (!node.getLeft().isRealNode()&&!node.getRight().isRealNode())
		{
			if (node == parent.getLeft())
				parent.setLeft(node.getRight());
			else
				parent.setRight(node.getRight());
			node.getRight().setParent(parent);
		}
		else
		{
		    //if the node we wish to delete is unary:
			if (node.getLeft().isRealNode()&&!node.getRight().isRealNode())  //only left child
			{
				if (node == parent.getLeft())
					parent.setLeft(node.getLeft());
				else
					parent.setRight(node.getLeft());
				node.getLeft().setParent(parent);
			}
			else
				if (node.getRight().isRealNode()&&!node.getLeft().isRealNode())  //only right child
					{
					if (node == parent.getLeft())
						parent.setLeft(node.getRight());
					else
						parent.setRight(node.getRight());
					node.getRight().setParent(parent);
					}
				//if the node we wish to delete is a father of two
				else
				{
						IAVLNode suc = findSuccessor(node);
						IAVLNode sucsParent = suc.getParent();
						//deleting the successor of the node we wish to delete
						if (suc == sucsParent.getLeft())
							sucsParent.setLeft(suc.getRight());
						else
							sucsParent.setRight(suc.getRight());
						suc.getRight().setParent(sucsParent);
						
						//inserting the successor in it's new place (the previous place of the node we deleted)
						if (node == parent.getLeft())
							parent.setLeft(suc);
						else
							parent.setRight(suc);
						suc.setParent(parent);
						
						// node's children are now his scuccessor's children
						suc.setRight(node.getRight());
						suc.setLeft(node.getLeft());
						suc.getRight().setParent(suc);
						suc.getLeft().setParent(suc);
						//now we check if the successor of the node we wanted to delete was his son
						if (node != sucsParent) // the successor's parent was the deleted node (which is no longer part of the tree)
							startHieghtUpdate = sucsParent;
						else
							startHieghtUpdate = suc;
				}
		}
		
		//if we wish to delete the tree's root, then we need to update the tree's root to a new node
		if (node == this.root) 
		{
			this.root = parent.getRight();
			this.root.setParent(null);
		}
		
		int moneRot = HieghtsUpdating(startHieghtUpdate);
		return moneRot;
	}
	
	/**
	 * public IAVLNode findSuccessor(IAVLNode node)
	 * @return a pointer to node's successor. 
	 * for the maximum node - returns null.
	 */
	   private IAVLNode findSuccessor(IAVLNode node) 
	   {
		   if(node.getRight().getHeight() != -1) 
			   return minPointer(node.getRight());
		   IAVLNode parent = node.getParent();
		   while(parent.getHeight() != -1 && node == parent.getRight())
		   {
			   node = parent;
			   parent = node.getParent();
		   }
		   return parent;	   
		}
		/**
		 * public IAVLNode minPointer(IAVLNode node)
		 * @return a pointer to the minimal node in the tree from "node" and down.
		 */
	   private IAVLNode minPointer(IAVLNode node) 
	   {
			while (node.isRealNode())
				node = node.getLeft();
			return node.getParent();	
	   }
	   
	 /**
	 * public IAVLNode findPredecessor(IAVLNode node)
	 * @return a pointer to the node's predecessor.
	 * for the minimum node - returns null.  
	 */
	private IAVLNode findPredecessor(IAVLNode node)
	{
		//minimum node has no predecessor
		if (node == minimum) 
			return null; 
		
		if (node.getLeft().isRealNode()) 
			return maxPointer(node.getLeft()); 
		else 
		{
			IAVLNode parent = node.getParent();
			while ((node == parent.getLeft())&&(parent != null)) 
			{   
				node = parent;                             
 				parent = parent.getParent() ;               
			}
			return parent;	
		}
	}
	/**
	 * public IAVLNode maxPointer(IAVLNode node)
	 * @return a pointer to the maximal node in the tree from "node" and down.
	 */
	private IAVLNode maxPointer(IAVLNode node)
	{
		while (node.isRealNode())
			node = node.getRight();
		return node.getParent();	
	}
	
	/**
	 * public String min()
	 * @return null for an empty tree.
	 * else, returns the value of minimal node (node with minimal key) in the tree.
	 */
	public String min(){
		if (empty()==false) 
			return minimum.getValue();
		return null;
	}

	/**
	 * public String max()
	 * @return null for an empty tree.
	 * else, returns the value of maximal node (node with maximal key) in the tree.
	 */
	public String max()
	{
		if (empty()==false) 
			return maximum.getValue();
		return null;
	}

	  /**
	   * public int[] keysToArray()
	   * @return a sorted array which contains all keys in the tree,
	   * or an empty array if the tree is empty.
	   */
     public int[] keysToArray() {
    	    if(this.empty())
    	    	return null;
			IAVLNode[] nodes = new IAVLNode[size()];
			nodesToArrayRec(this.root, nodes, 0);
			int[] arr = new int[size()]; 
			for (int i = 0; i < arr.length; i++)
				arr[i] = nodes[i].getKey();
		        return arr;
			}
	  	
      /**
       * private int nodesToArrayRec(IAVLNode node, IAVLNode[] arr, int index) 
       * Recursive function.
       * @return Array of nodes in - order of a given tree
       */

	 private int nodesToArrayRec(IAVLNode node, IAVLNode[] arr, int index) {
			if (node.isRealNode() == false)
				return index;
			index = nodesToArrayRec(node.getLeft(), arr, index);
			arr[index++] = node;
			index = nodesToArrayRec(node.getRight(), arr, index);
			return index;
		}

		/**
		   * public String[] infoToArray()
		   * @return an array which contains all info in the tree,
		   * sorted by their respective keys,
		   * or an empty array if the tree is empty.
		   */

	 public String[] infoToArray(){
				if(this.empty())
	    	    	return null;
				IAVLNode[] nodes = new IAVLNode[size()]; //creating list of nodes - with recursion
				nodesToArrayRec(this.root, nodes, 0);
				String[] arr = new String[size()]; 
				for (int i = 0; i < arr.length; i++)
					arr[i] = nodes[i].getValue();
		        return arr;
			}
	 
	 
	 /**
	* public AVLTree[] split(int x)
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	* @pre: search(x) != null
    */   
	 public AVLTree[] split(int x)
	   {
		   AVLTree[] splitedTrees = new AVLTree[2];
		   splitedTrees[0] = new AVLTree();
		   splitedTrees[1] = new AVLTree();
		   if(!this.root.isRealNode()) 
			   return splitedTrees;
		   
		   IAVLNode nodeX = searchFor(this.root, x);
		   splitedTrees = splitLoop(nodeX,splitedTrees);
		   return splitedTrees;
		   
		   }
	 
	 /**private AVLTree[] splitLoop(IAVLNode nodeX, AVLTree [] arr)
	  * This function returns an array of two trees - 
	  * the split function itself.
	  */
		private AVLTree[] splitLoop(IAVLNode nodeX, AVLTree [] arr){
			AVLTree tmp1 = new AVLTree();
			AVLTree tmp2 = new AVLTree();
			AVLTree tmp3 = new AVLTree();
			AVLTree tmp4 = new AVLTree();
			IAVLNode preOfX, newNodeForJoin; 
			int newHeight, newSize;
			
			IAVLNode curr = nodeX;
			tmp1.setRoot(curr.getLeft());
			tmp1.getRoot().setParent(null);
			tmp3.setRoot(curr.getRight());
			tmp3.getRoot().setParent(null);
			while (curr != null) {
				if(curr.getParent() == null)
					break;
				if(curr.getParent().getRight() == curr) // im his left son
					{preOfX = curr.getParent();
					tmp2.setRoot(preOfX.getLeft());
					tmp2.getRoot().setParent(null);
					newNodeForJoin= new AVLNode(preOfX.getKey(), preOfX.getValue());
					newNodeForJoin.setHeight(0);
					tmp1.join(newNodeForJoin, tmp2);
					newHeight = tmp1.HeightCalc(tmp1.getRoot());
					newSize = tmp1.sizeCalc(tmp1.getRoot());
					tmp1.getRoot().setHeight(newHeight);
					tmp1.getRoot().setSize(newSize);
					tmp2.setRoot(null);
					curr = preOfX;
					}
				else {
					preOfX = curr.getParent();
					tmp4.setRoot(preOfX.getRight());
					tmp4.getRoot().setParent(null);
					newNodeForJoin= new AVLNode(preOfX.getKey(), preOfX.getValue());
					newNodeForJoin.setHeight(0);
					tmp3.join(newNodeForJoin,  tmp4);
					newHeight = tmp3.HeightCalc(tmp3.getRoot());
					newSize = tmp3.sizeCalc(tmp3.getRoot());
					tmp3.getRoot().setHeight(newHeight);
					tmp3.getRoot().setSize(newSize);
					tmp4.setRoot(null);
					curr = preOfX;
				}
				newNodeForJoin = new AVLNode(null);
				
				}
				arr[0] = tmp1;
				arr[1]= tmp3;
				arr[0].minimum = arr[0].minPointer(arr[0].getRoot());
				arr[1].minimum = arr[1].minPointer(arr[1].getRoot());
				arr[0].maximum = arr[0].maxPointer(arr[0].getRoot());
				arr[1].maximum = arr[1].maxPointer(arr[1].getRoot());
	
				return arr;
				
			}

		/**
		    * public join(IAVLNode x, AVLTree t)
		    * joins t and x with the tree. 	
		    * Returns the complexity of the operation (rank difference between the tree and t + 1)
			* @pre: keys(x,t) < keys() or keys(x,t) > keys()
		    */   
	public int join(IAVLNode x, AVLTree t)
	{
	
		int heighDiff = 0;
		if(this.empty()) {
			this.root.setParent(x);
			this.minimum = x;
			this.maximum = x;
			if(t.empty()) {// both trees are empty
				this.setRoot(x);
				return 1;
			}
			   }	
		if(t.empty()) {
			t.root.setParent(x);
			t.minimum = x;
			t.maximum = x;
		}
		 //both tree same height or 1
		heighDiff = Math.abs(this.getRoot().getHeight() - t.getRoot().getHeight());
		 if(heighDiff <= 1) {
				 joinWithRoot(this,x,t);
				   return heighDiff + 1;
			   }
			   
		//case 1: keys(x,t) > keys() 
		 if(this.getRoot().getKey() <= x.getKey() && t.getRoot().getKey() >= x.getKey()) {
		 // 1.1 : this tree is lower then t INPUT
			   if(this.getRoot().getHeight() < t.getRoot().getHeight()) 
				   return 1 + joinFirstCase(this,x,t);
		   		// 1.2: this tree is higher then t INPUT
			   else
				   return 1 + joinSecnodCase(this,x,t);
		   
			   }
			   //case 2: keys(x,t) < keys()
		 else {
			 if(this.getRoot().getHeight() < t.getRoot().getHeight()) 
				   return 1 + joinSecnodCase(t,x,this);
		   		// 1.2: this tree is higher then t INPUT
			   else
				   return 1 + joinFirstCase(t,x,this);
		 }
		   }
		   
	/**private int joinFirstCase(AVLTree t1, IAVLNode x, AVLTree t2) 
	 * CASE 1:  of join of 2 tree
	 * keys(x,t) > keys() 
	 *  this tree is lower then t INPUT
	 *  @return the rank differences
	 */
	private int joinFirstCase(AVLTree t1, IAVLNode x, AVLTree t2) {
			   int returnValue = 0;
			   IAVLNode curr = t2.getRoot();
			   
			   while(true) {
				   if(curr.getHeight() <= t1.getRoot().getHeight())
					   break;
				   if(curr.getLeft().getHeight() == -1)
					   break;
				   curr = curr.getLeft();
			   }
			   
			   if(t2.getRoot() == curr) {
				   curr.setLeft(x);
				   x.setParent(curr);
				   curr.setHeight(curr.getHeight() + 1 );
				   return 0;
						   
			   }
			   returnValue = t2.getRoot().getHeight() - curr.getHeight();
		
			   //replacing pointers
			   x.setRight(curr);
			   x.setParent(curr.getParent());
			   curr.getParent().setLeft(x);
			   curr.setParent(x);
			   x.setLeft(t1.getRoot());
			   t1.getRoot().setParent(x);
			   t1.setRoot(t2.getRoot());
			   x.setHeight(Math.max(x.getRight().getHeight(),x.getLeft().getHeight()) + 1);
			   
			   t2.minimum = t1.minimum;
			  
			 //FIXING NEW TREE CREATED
			  HieghtsUpdating(x);
			  SizesUpdate(x);
	
			  return returnValue;
			   
		}      
	
	/**
	 *  CASE 2:  of join of 2 tree
	 *  keys(x,t) < keys() 
	 *  this tree is higher then t INPUT
	 *  @return the rank differences    
	 */
	private int joinSecnodCase(AVLTree t1, IAVLNode x, AVLTree t2) {
				   int returnValue = 0;
				   IAVLNode curr = t1.getRoot(), parent;
				   
				   while(true) {
					   if(curr.getHeight() <= t2.getRoot().getHeight())
						   break;
					   if(curr.getRight().getHeight() == -1)
						   break;
					   curr = curr.getRight();
				   }
				  
				   parent = curr.getParent();
				   
				   if(t1.getRoot() == curr) {
					   curr.setRight(x);
					   x.setParent(curr);
					   curr.setHeight(curr.getHeight() + 1 );
					   return 0;
				   }
				   
				   returnValue = Math.abs(t2.getRoot().getHeight() - curr.getHeight());
				   
				   //replacing pointers
				   
				   x.setLeft(curr);
				   x.setParent(parent);
				   parent.setRight(x);
				   curr.setParent(x);
				   x.setRight(t2.getRoot());
				   t2.getRoot().setParent(x);
				   t2.setRoot(null);
				   t2.setRoot(t1.getRoot());
				   x.setHeight(Math.max(x.getRight().getHeight(), x.getLeft().getHeight()) + 1);
				   //FIXING NEW TREE CREATED
				   
				   //t2.minimum = t1.minimum;
				   t2.maximum = t1.maximum;
				   
				 //FIXING NEW TREE CREATED
				   HieghtsUpdating(x);
				   SizesUpdate(x);
		
				 return returnValue;
				}   
			   
	/**
	 *  CASE 0:  of join of 2 tree, when x it the root 
	 *  happens one abs(t1.height - t2.height) <= 1
	 *  keys(x,t) < keys() or keys(x,t) > keys() 
	 *  @return the rank differences    
	 */
    private static void joinWithRoot(AVLTree t1, IAVLNode x, AVLTree t2) 
    {		
			   if (t1.getRoot().getKey() < x.getKey() && t2.getRoot().getKey() > x.getKey()) {
				   x.setLeft(t1.getRoot());
				   t1.getRoot().setParent(x);
				   
				   x.setRight(t2.getRoot());
				   t2.getRoot().setParent(x);
				   
				   t1.setRoot(x);
				   int newSize = t1.getRoot().getLeft().getSize() + t1.getRoot().getRight().getSize() + 1;
				   int newHeight = Math.max(t1.getRoot().getLeft().getHeight(),t1.getRoot().getRight().getHeight())+1;
				   t1.getRoot().setSize(newSize);
				   t1.getRoot().setHeight(newHeight);
				   t1.maximum = t2.maximum;  
			   }
			   //case 2
			   else {
				   x.setRight(t1.getRoot());
				   t1.getRoot().setParent(x);
				   
				   x.setLeft(t2.getRoot());
				   t2.getRoot().setParent(x);
				   
				   t1.setRoot(x);
				   int newSize = t1.getRoot().getLeft().getSize() + t1.getRoot().getRight().getSize() + 1;
				   int newHeight = Math.max(t1.getRoot().getLeft().getHeight(),t1.getRoot().getRight().getHeight())+1;
				   t1.getRoot().setSize(newSize);
				   t1.getRoot().setHeight(newHeight);
				   t1.minimum = t2.minimum;  
			   }
		   }
    
    /** private void setRoot(IAVLNode x)
     * sets new root into the tree
     */
    private void setRoot(IAVLNode x) {
			this.root = x;
		}		  

	/**
	 * public int size()
	 * @return the number of real nodes in the tree.
	 */
	public int size()
	{
		return root.getSize();
	}
	 
	/**
    * public IAVLNode getRoot()
    * @return null for an empty tree. else, return the tree's root (an AVL node).
    */
	public IAVLNode getRoot()
	{
		return this.root;
	}

	/**
	* public interface IAVLNode
	* ! Do not delete or modify this - otherwise all tests will fail !
	*/
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setValue(String value); // sets the value of the node
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	public void setKey(int key); // Sets the key of the node
    	public void setSize(int size); // sets the number of real nodes in this node's subtree
    	public int getSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
	}
	  
	/**
	 * public class AVLNode
	 * Creates an AVL node
	 */
	public class AVLNode implements IAVLNode{
		private int key; 
		private String value; 
		private IAVLNode left; 
		private IAVLNode right; 
		private IAVLNode parent; 
	  	private int size ;   				 
	  	private int height; 
	  	
	  	//building an external node, a virtual node
	  	public AVLNode(IAVLNode parent) {
			this.key = -1;
			this.value = null;
			this.height = -1;
			this.parent = parent;
			this.size = 0;
			this.left = null;
			this.right = null;
		}
	  	
	  	//building a real node
	  	public AVLNode(int i, String val) {
			this.key = i;
			this.value = val;
			this.height = 0;
			this.size = 1;
			this.left = new AVLNode(this); // external node
			this.right = new AVLNode(this); // external node
			this.parent = null;
		}		
		
		public int getKey()
		{
			return this.key; // returns the node's key
		}
		
		public String getValue()
		{
			return this.value; // returns the node's value
		}
		
		public void setLeft(IAVLNode node)
		{
			 this.left = node; // sets the node's left child
		}
		
		public IAVLNode getLeft()
		{
			return this.left; // returns the node's left child
		}
		
		public void setRight(IAVLNode node)
		{
			this.right = node; // sets the node's right child
		}
		
		public IAVLNode getRight()
		{
			return this.right; // returns the node's right child
		}
		
		public void setParent(IAVLNode node)
		{
			 this.parent = node; // sets the node's parent
		}
		
		public IAVLNode getParent()
		{
			return this.parent; // returns the node's parent
		}
		
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			if (this.key == -1)
				return false;
			return true; 
		}
		
		public void setSize(int size)
		{
			this.size = size; // sets the node's size 
		}
		
		public int getSize()
		{
			return this.size; // returns the node's size
		}
		
		public void setHeight(int height)
		{
			this.height = height ; // sets the node's height
		}
		
		public int getHeight()
		{
			return this.height; // returns the node's height 
		}
		public void setKey(int key) {
			this.key = key;
		}

		@Override
		public void setValue(String value) {
			this.value = value;
		}	
  }
}