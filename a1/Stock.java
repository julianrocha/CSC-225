/*
*	Rahnuma Islam Nishat - January 17, 2018
*/
import java.util.Scanner;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

public class Stock{
	
	public static int[] CalculateSpan(int[] p){
		int maxSoFar = 0; //stores the highest price found so far
		int[] span = new int[p.length]; //array of stock span (integers) to be returned
		LinkedList<Integer> stack = new LinkedList<>(); //stack to store days i already visited
		if(p.length > 0){ //initialize the variables for first day
			stack.push(0);
			span[0] = 1;
			maxSoFar = p[0];
		}
		for(int i = 1; i < p.length; i++){//iterate through p from second element to last
			if(p[i - 1] > p[i]){//if todays price is less than yesterdays
				span[i] = 1; 
				stack.push(i); 
				continue;
			}
			else if(p[i] >= maxSoFar){//if todays price is the highest seen so far
				span[i] = i + 1; 
				maxSoFar = p[i];
				stack.push(i); 
				continue; 
			}
			else{//if todays price is larger than yesterdays but not the biggest seen so far
				while(p[stack.peek()] <= p[i]){//pop days off stack until higher price is found
					stack.pop();			   //OR maxSoFar is reached
				}
				span[i] = i - stack.peek();//span is # of days between today and last higher price
				stack.push(i);
				continue;
			}
		}
		return span;
	}



	public static int[] readInput(Scanner s){
		Queue<Integer> q = new LinkedList<Integer>();
		int n=0;
		if(!s.hasNextInt()){
			return null;
		}
		int temp = s.nextInt();
		while(temp>=0){
			q.offer(temp);
			temp = s.nextInt();
			n++;
		}
		int[] inp = new int[q.size()];
		for(int i=0;i<n;i++){
			inp[i]= q.poll();
		}
		return inp;
	}
	public static void main(String[] args){
		Scanner s;
        Stock m = new Stock();
        if (args.length > 0){
        	try{
        		s = new Scanner(new File(args[0]));
        	} catch(java.io.FileNotFoundException e){
        		System.out.printf("Unable to open %s\n",args[0]);
        		return;
        	}
        	System.out.printf("Reading input values from %s.\n", args[0]);
        }else{
        	s = new Scanner(System.in);
        	System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
        }
            
        int[] price = m.readInput(s);
        System.out.println("The stock prices are:");
        for(int i=0;i<price.length;i++){
        	System.out.print(price[i]+ (((i+1)==price.length)? ".": ", "));
        }

        if(price!=null){
        	int[] span = m.CalculateSpan(price);
        	if(span!=null){
        		System.out.println("The spans are:");
        		for(int i=0;i<span.length;i++){
        			System.out.print(span[i]+ (((i+1)==span.length)? ".": ", "));
        		}
        	}
        }
    }
}