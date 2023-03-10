
import informed.AStar;
import informed.BestFS;
import startCode.Conf;
import startCode.Coord;
import startCode.Map;
import uninformed.BFS;
import advanced.BiD;
import uninformed.DFS;
/********************Starter Code
 * 
 * This class contains some examples on how to handle the required inputs and outputs 
 * and other debugging options
 * 
 * @author at258
 * 
 * run with 
 * java A1main <Algo> <ConfID>
 * 
 */


public class A1main {

	public static void main(String[] args) {
		//Example: java A1main BFS JCONF03

		/* 
		 * 
		 * Retrieve input and configuration
		 * and run search algorithm
		 *
		 */


		Conf conf = Conf.valueOf(args[1]);

		
		 //Uncomment here for debugging only 

//		
//		System.out.println("Configuration:"+args[1]);
//		System.out.println("Map:");
//		printMap(conf.getMap(), conf.getS(), conf.getG());
//		System.out.println("Departure port: Start (r_s,c_s): "+conf.getS());
//		System.out.println("Destination port: Goal (r_g,c_g): "+conf.getG());
//		System.out.println("Search algorithm: "+args[0]);
//		System.out.println();
		

		//run your search algorithm 
		runSearch(args[0],conf.getMap(),conf.getS(),conf.getG());

		/*
		 * The system must print the following information from your search methods
		 * All code below is for demonstration purposes, and can be removed
		 */


		/*
		 * 1) Print the Frontier at each step before polling 
		 */

//		boolean uninformed=true;
//		String frontier_string="";
//
//		if(uninformed) {
//
//			//starting point (1,1), 
//			//insert node in the frontier, then print the frontier:
//			frontier_string="[(0,0)]";
//
//
//			System.out.println(frontier_string);
//
//			//extract (0,0)
//			//insert successors in the frontier (0,1),(1,0) , then print the frontier,  and repeat for all steps until a path is found or not 
//			frontier_string="[(0,1),(1,0)]\n"
//					+ "[(1,0),(0,2)]\n"
//					+ "[(0,2),(1,1)]\n"
//					+ "[(1,1),(1,2)]\n"
//					+ "[(1,2),(2,1)]\n"
//					+ "[(2,1)]\n"
//					+ "[(2,2),(2,0)]";
//			System.out.println(frontier_string);
//
//
//		}else {
//			//for informed searches the nodes in the frontier must also include the f-cost 
//			//for example 
//			frontier_string="[(0,0):4.0]\n"
//					+ "[(0,1):3.0,(1,0):3.0]\n"
//					+ "...";
//			System.out.println(frontier_string);
//
//		}
//
//		/*
//		 * 2) The final three lines must be the path, path cost, and number of nodes visited/explored, in this order
//		 */
//
//		boolean path_found=true;
//		String path_string="(0,0)(1,0)(1,1)(2,1)(2,2)";
//		double path_cost=4;
//		int n_explored=8;
//
//		if(path_found) {
//			System.out.println(path_string);
//			System.out.println(path_cost);
//		}else {
//			System.out.println("fail");
//		}
//
//		System.out.println(n_explored);

	}

	private static void runSearch(String algo, Map map, Coord start, Coord goal) {
		switch(algo) {
		case "BFS": //run BFS
			new BFS(map, start, goal);
			break;
		case "DFS": //run DFS
			new DFS(map, start, goal);
			break;  
		case "BestF": //run Best-first search
			new BestFS(map, start, goal);
			break;
		case "AStar": //run A*
			new AStar(map, start, goal);
			break;
		case "BiD": //run bidirectional search
			new BiD(map, start, goal);
			break;
		}

	}


	private static void printMap(Map m, Coord init, Coord goal) {

		int[][] map=m.getMap();

		System.out.println();
		int rows=map.length;
		int columns=map[0].length;

		//top row
		System.out.print("  ");
		for(int c=0;c<columns;c++) {
			System.out.print(" "+c);
		}
		System.out.println();
		System.out.print("  ");
		for(int c=0;c<columns;c++) {
			System.out.print(" -");
		}
		System.out.println();

		//print rows 
		for(int r=0;r<rows;r++) {
			boolean right;
			System.out.print(r+"|");
			if(r%2==0) { //even row, starts right [=starts left & flip right]
				right=false;
			}else { //odd row, starts left [=starts right & flip left]
				right=true;
			}
			for(int c=0;c<columns;c++) {
				System.out.print(flip(right));
				if(isCoord(init,r,c)) {
					System.out.print("S");
				}else {
					if(isCoord(goal,r,c)) {
						System.out.print("G");
					}else {
						if(map[r][c]==0){
							System.out.print(".");
						}else{
							System.out.print(map[r][c]);
						}
					}
				}
				right=!right;
			}
			System.out.println(flip(right));
		}
		System.out.println();


	}

	private static boolean isCoord(Coord coord, int r, int c) {
		//check if coordinates are the same as current (r,c)
		if(coord.getR()==r && coord.getC()==c) {
			return true;
		}
		return false;
	}



	public static String flip(boolean right) {
        //prints triangle edges
		if(right) {
			return "\\"; //right return left
		}else {
			return "/"; //left return right
		}

	}

}
