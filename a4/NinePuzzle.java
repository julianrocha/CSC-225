/* NinePuzzle.java
   CSC 225 - Spring 2017
   Assignment 4 - Template for the 9-puzzle
   
   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java NinePuzzle
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. boards.txt), run the program with
    java NinePuzzle boards.txt
	
   The input format for both input methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the 
   empty square. For example, a sample board with the middle square empty is
   
    1 2 3
    4 0 5
    6 7 8
   
   And a solved board is
   
    1 2 3
    4 5 6
    7 8 0
   
   An input file can contain an unlimited number of boards; each will be 
   processed separately.
  
   B. Bird    - 07/11/2014
   M. Simpson - 11/07/2015
*/

import java.util.Scanner;
import java.io.File;
import java.util.*;		//added by Student

public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
	//The max legal moves of a 3 x 3 board is 4, when middle is empty
	public static final int MAX_MOVES = 4; 

	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the 
		value 0),return true if the board is solvable and false otherwise. 
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){
		
		//declare variables and call appropriate methods
		int input_board = getIndexFromBoard(B);	//initial state in int index form
		int goal_state = 0;						//goal state int index
		int[][] G = constructGraph();			//graph of all boards in 3 x 3
		int[] path = pathBetween(G, input_board, goal_state);
		// ^ path from input_board to goal_state, 
		//length 1 containing -1 if no path exists

		//determine if path exists, print if necessary
		if(path[0] == -1) return false;				//no path exists
		for(int i = 0; i < path.length; i++){		//path exists,
			printBoard(getBoardFromIndex(path[i]));	//print boards in order
		}
		return true;
	}//end of SolveNinePuzzle

	/*	pathBetween(G, input_board, goal_state)
		Returns shortest path between 2 vertices in graph G
		Path is from input_board to goal_state
		If path does not exist, array of size 1 containing -1 is returned
		Breadth First Search Algorithm is used
	*/
	public static int[] pathBetween(int[][] G, int input_board, int goal_state){
		//variable declarations
		int[] path;									//path to be returned
		int[] edge_to = new int[NUM_BOARDS];		//incoming edge for each vertex
		int[] dist_to = new int[NUM_BOARDS];		//distance from input_board to vertex
		boolean[] marked = new boolean[NUM_BOARDS];	//true means visited already (used during search)
		Queue<Integer> q = new LinkedList<Integer>();//queue to decided which vertex to explore next

		//variable initializations
		edge_to[input_board] = input_board;			//start edgeTo path with self loop at input_board
		dist_to[input_board] = 0;					//distance from starting location to itself = 0
		marked[input_board] = true;					//mark starting location
		q.add(input_board);							//enqueue starting location

		//begin path search
		while(!q.isEmpty()){
			int current_board = q.remove();
			for(int edge = 0; edge < MAX_MOVES; edge++){
				int adjacent_board = G[current_board][edge];
				if(adjacent_board == -1) break;			//no more edges to explore on current_board
				if(!marked[adjacent_board]){			//adjacent_board is a new edge
					q.add(adjacent_board);
					marked[adjacent_board] = true;
					edge_to[adjacent_board] = current_board;
					dist_to[adjacent_board] = 1 + dist_to[current_board];
				}
			}
			if(marked[goal_state]) break;	//first path found to goal_state is the shortest in BFS
		}//end of path search while loop

		//no path exists
		if(!marked[goal_state]){
			path = new int[1];	//this array will let SolveNinePuzzle know there is no path
			path[0] = -1;
			return path;
		}

		//path exists
		path = new int[dist_to[goal_state] + 1];
		int board = goal_state;
		//edgeTo must be followed in reverse and translated to path array
		for(int path_counter = dist_to[goal_state]; path_counter >= 0; path_counter--){
			path[path_counter] = board;
			board = edge_to[board];
		}
		return path;
	}//end of pathBetween

	/*	constructGraph()
		Return the graph for Nine Puzzles, in the form of an adjacency list (2D int array)
		Vertex represents a board permutation (converted to int index)
		Edge between 2 boards means 1 legal move can be used to get from one to the other and vise versa
	*/
	public static int[][] constructGraph(){
		
		//declare/initialize variables
		int[][] G = new int[NUM_BOARDS][MAX_MOVES];	//adjacency list, stores edge indices
		for(int i = 0; i < NUM_BOARDS; i++){
			Arrays.fill(G[i], -1);				//initialize adjacency list to -1's
		}

		//construct graph
		for(int board_num = 0; board_num < NUM_BOARDS; board_num++){
	
			int edge_num = 0;		//counter for current edge of current vertex from G
			int[][] current_board = getBoardFromIndex(board_num);
	
			//find co-ords of empty square of current_board
			int zero_row = -1;
			int zero_col = -1;
			for(int row = 0; row < 3; row++){
				for(int col = 0; col < 3; col++){
					if(current_board[row][col] == 0){
						zero_row = row;
						zero_col = col;
					}
				}
			}//end of find zero co-ords

			/*
				Cases for legal moves:
				-up square can move down: zero_row > 0
				-down square can move up: zero_row < 2
				-left square can move right: zero_col > 0
				-right square can move left: zero_col < 2
			*/
			int[][] transform_board;	//board which is one legal move away from current_board
			if(zero_row > 0){
				transform_board = getBoardFromIndex(board_num);
				transform_board[zero_row][zero_col] = transform_board[zero_row - 1][zero_col];
				transform_board[zero_row - 1][zero_col] = 0;
				G[board_num][edge_num] = getIndexFromBoard(transform_board);
				edge_num++;
			}
			if(zero_row < 2){
				transform_board = getBoardFromIndex(board_num);
				transform_board[zero_row][zero_col] = transform_board[zero_row + 1][zero_col];
				transform_board[zero_row + 1][zero_col] = 0;
				G[board_num][edge_num] = getIndexFromBoard(transform_board);
				edge_num++;
			}
			if(zero_col > 0){
				transform_board = getBoardFromIndex(board_num);
				transform_board[zero_row][zero_col] = transform_board[zero_row][zero_col - 1];
				transform_board[zero_row][zero_col - 1] = 0;
				G[board_num][edge_num] = getIndexFromBoard(transform_board);
				edge_num++;
			}
			if(zero_col < 2){
				transform_board = getBoardFromIndex(board_num);
				transform_board[zero_row][zero_col] = transform_board[zero_row][zero_col + 1];
				transform_board[zero_row][zero_col + 1] = 0;
				G[board_num][edge_num] = getIndexFromBoard(transform_board);
				edge_num++;
			}
		}//end of graph construction for loop
		return G;
	}//end of constructGraph

	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}//end of printBoard
	
	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284. 
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;
			
			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}//end of getIndexFromBoard
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}//end of getBoardFromIndex
	
	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);
	}//end of main

}//end of class
