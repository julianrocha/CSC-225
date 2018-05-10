import java.util.Scanner;
public class median{
	static minHeap min;
	static maxHeap max;
	
	public median(){
		min = new minHeap();
		max = new maxHeap();
	}
	//Contains two heaps: 
	//-max is a maxHeap that contains the lower half of integers
	//-min is a minHeap that contains the upper half of integers
	public static int calculateMedian(int x){
		if(x > max.peek()){					//if x is in upper half
			min.insert(x);					//add to upper half heap (min)
		}
		else{								//else x is in lower half
			max.insert(x);					//add to lower half heap (max)
		}
		if(max.size() < min.size()){		//if max contains less elements than min
			max.insert(min.removeMin());	//min gives an element to max
		}
		if(max.size() > min.size() + 1){	//if max contains more than 1 + size of min
			min.insert(max.removeMax());	//max gives an element to min
		}
		return max.peek();			//max contains median for odd number of integers
	}
	
	public static void main(String[] args){
		median m = new median();
		
		System.out.println("Enter a list of non negative integers. To end enter a negative integers.");
		Scanner s = new Scanner(System.in);
		int current = s.nextInt();

		while(current >=0){
			System.out.println("current median:" + m.calculateMedian(current));
			current = s.nextInt();
			if(current<0)break;
			m.calculateMedian(current);
			current = s.nextInt();
		}	
	}
}

class minHeap{
	private int[] heap;
	private int size;
	
	public minHeap(){
		heap=new int[10000];
		size=0;
	}
	
	public boolean isEmpty(){
		return (size==0);
	}
	
	public int size(){
		return size;
	}
	
	public void insert(int x){
		heap[size] = x;					//insert in open leftmost spot(complete tree)
		size++;
		bubbleup(size - 1);
	}
	
	public void bubbleup(int k){
		if(k <= 0) return;					//stop bubbling up if top of heap is reached
		int kParent = ((k + 1) / 2) - 1;			//parent index of k index
		int kValue = heap[k];				//value of index k
		int kParentValue = heap[kParent];	//value of parent index of k
		if(kValue < kParentValue){			//swap if out of order
			heap[k] = kParentValue;
			heap[kParent] = kValue;
			bubbleup(kParent);
		}
	}
	public void exchange(int i,int j){
		int x = heap[i];
		heap[i] = heap[j];
		heap[j] = x;
	}
	public void bubbledown(int k){
		if(k >= size / 2) return;			//stop bubbling up if bottom level of tree is reached
		int kValue = heap[k];
		int kLeftChild = (k * 2) + 1 ;
		int kRightChild = (k * 2) + 2;
		boolean rightChildExists = kRightChild < size;
		if(rightChildExists && (heap[kRightChild] < heap[kLeftChild]) && (heap[kRightChild] < kValue)){
			heap[k] = heap[kRightChild];
			heap[kRightChild] = kValue;
			bubbledown(kRightChild);
		}
		else if(heap[kLeftChild] < kValue){
			heap[k] = heap[kLeftChild];
			heap[kLeftChild] = kValue;
			bubbledown(kLeftChild);
		}
	}
	public int peek(){
		if(size == 0) return -1;		//return -1 for peeking empty heap
		return heap[0];
	}
	
	public int removeMin(){
		if(size == 0) return -1;		//return -1 for removing from empty heap
		int x = heap[0];				//save min value to be returned
		heap[0] = heap[size - 1];		//replace min value with last element(complete tree)
		heap[size - 1] = -1;			//replace last element with -1 (it was a duplicate)
		size--;
		bubbledown(0);
		return x;
	}
}

class maxHeap{
	private int[] heap;
	private int size;
	
	public maxHeap(){
		heap=new int[10000];
		size=0;
	}
	
	public boolean isEmpty(){
		return (size==0);
	}
	
	public int size(){
		return size;
	}
	
	public void insert(int x){
		heap[size] = x;					//insert in open leftmost spot(complete tree)
		size++;
		bubbleup(size - 1);
	}
	
	public void bubbleup(int k){
		if(k == 0) return;					//stop bubbling up if top of heap is reached
		int kParent = ((k + 1) / 2) - 1;			//parent of k index
		int kValue = heap[k];				//value of index k
		int kParentValue = heap[kParent];	//value of parent of index k
		if(kValue > kParentValue){			//swap if out of order
			heap[k] = kParentValue;
			heap[kParent] = kValue;
			bubbleup(kParent);
		}
	}
	public void exchange(int i,int j){
		int x = heap[i];
		heap[i] = heap[j];
		heap[j] = x;
	}
	public void bubbledown(int k){
		if(k >= size / 2) return;			//stop bubbling up if bottom level of tree is reached
		int kValue = heap[k];
		int kLeftChild = (k * 2) + 1 ;
		int kRightChild = (k * 2) + 2;
		boolean rightChildExists = kRightChild < size;
		if(rightChildExists && (heap[kRightChild] > heap[kLeftChild]) && heap[kRightChild] > kValue){
			heap[k] = heap[kRightChild];
			heap[kRightChild] = kValue;
			bubbledown(kRightChild);
		}
		else if(heap[kLeftChild] > kValue){
			heap[k] = heap[kLeftChild];
			heap[kLeftChild] = kValue;
			bubbledown(kLeftChild);
		}
	}
	public int peek(){
		if(size == 0) return -1;		//return -1 for peeking empty heap
		return heap[0];
	}
	
	public int removeMax(){
		if(size == 0) return -1;		//return -1 for removing from empty heap
		int x = heap[0];				//save max value to be returned
		heap[0] = heap[size - 1];		//replace max value with last element(complete tree)
		heap[size - 1] = -1;			//replace last element with -1 (it was a duplicate)
		size--;
		bubbledown(0);
		return x;
	}
}