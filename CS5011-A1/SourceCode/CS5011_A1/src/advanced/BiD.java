package advanced;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import help.Node;
import help.SharedMethods;
import help.State;
import startCode.Coord;
import startCode.Map;

public class BiD extends SharedMethods {
	private List<State> exploredStatesFront = new LinkedList<State>(); // states list for the search from start to goal
	private List<State> exploredStatesEnd = new LinkedList<State>(); // states list for the search from goal to start
	private List<Node> exploredNodesFront = new LinkedList<Node>(); // nodes list for the search from start to goal
	private List<Node> exploredNodesEnd = new LinkedList<Node>(); // nodes list for the search from goal to start
	private List<Node> frontier = new LinkedList<Node>(); // list to record nodes to be visited from start to goal
	private List<Node> endier = new LinkedList<Node>(); // list to record nodes to be visited from goal to start
	private int positionFront; // index of intersecting node in the frontier
	private int positionEnd; // index of intersecting node in the endier
	private State initState; // the start state
	private State goalState; // the goal state
	private double costFront; // the path cost for the front search
	private double costEnd; // the path cost for the reverse search direction
	private Map map;

	public BiD(Map map, Coord start, Coord goal) {
		this.map = map;
		if (!checkCorrect(start, goal, map)) {
			System.exit(0);
		}
		this.initState = new State(start.getR(), start.getC());
		this.goalState = new State(goal.getR(), goal.getC());
		Node nS = makeNode(null, initState, goalState);
		frontier.add(nS);
		Node nE = makeNode(null, goalState, initState);
		endier.add(nE);

		while (true) {
			
			treeSearch(start, goal, false); // search from start to goal
			treeSearch(goal, start, true); // search from goal to start
			if (isIntersecting(exploredNodesFront, exploredNodesEnd)) {
				// if there is a intersecting node
				// this means there is a full path
				Node nFront = exploredNodesFront.get(positionFront);
				costFront = nFront.getCostPath();
				Node nEnd = exploredNodesEnd.get(positionEnd);
				costEnd = nEnd.getCostPath();
				
				printResult(nFront,false); // print the front path
				printResult(nEnd,true); // print the second part path
				System.out.println("\n" + (costFront+costEnd)); // print the full cost
				break; 
				
			} 
			else {
				if (frontier.isEmpty() || endier.isEmpty()) {
					System.out.println("fail");
					break;
				}
			}
		}
		System.out.println(exploredStatesFront.size() + exploredStatesEnd.size()); // print the total nodes explored
	}

	private boolean isIntersecting(List<Node> exploredNodeFront, List<Node> exploredNodeEnd) {
		// This method aims at finding a intersecting node
		int lenFront = exploredNodeFront.size();
		int lenEnd = exploredNodeEnd.size();
		for(int i =0;i<lenFront;i++) {
			for(int j =0;j<lenEnd;j++) {
				if(exploredNodeFront.get(i).getState().getX()==exploredNodeEnd.get(j).getState().getX() && exploredNodeFront.get(i).getState().getY()==exploredNodeEnd.get(j).getState().getY()) {
					positionFront=i;
					positionEnd=j;
					return true;
				}
			}
		}
		
		return false;
	}

	private void treeSearch(Coord start, Coord goal, boolean reverse) {
		if (!reverse) { 
			// search in direction from start to goal
			Node node = lowestF(frontier); // find the node with the lowest F 
			exploredNodesFront.add(node); // add into explored node
			exploredStatesFront.add(node.getState()); // add into explored state
			System.out.print("Front");
			printStates(frontier);
			frontier.remove(node);

			List<Node> nextSuccess = expand(node, reverse); // find successor
			for (int i = 0; i < nextSuccess.size(); i++) {
				frontier.add(nextSuccess.get(i));
			}

		} else {
			// search in direction from goal to start
			Node node = lowestF(endier); // find the node with the lowest F 
			exploredNodesEnd.add(node); // add into explored node
			exploredStatesEnd.add(node.getState()); // add into explored state
			System.out.print("End");
			printStates(endier);
			System.out.println();
			endier.remove(node);

			List<Node> nextSuccess = expand(node, reverse); // find successor
			for (int i = 0; i < nextSuccess.size(); i++) {
				endier.add(nextSuccess.get(i));
			}
		}

	}

	private List<Node> expand(Node node, boolean reverse) {
		List<Node> successors = new LinkedList<Node>();
		List<State> nextStates = new LinkedList<State>();
		nextStates = successorFN(map, node);

		for (int i = 0; i < nextStates.size(); i++) {
			State s = nextStates.get(i);
			if (!reverse) {
				if (checkNotExist(s, exploredStatesFront, frontier)) {
					// if not exist in the explored list or the frontier
					Node newNd = makeNode(node, s, goalState);
					successors.add(newNd);
				}
			} else {
				if (checkNotExist(s, exploredStatesEnd, endier)) {
					// if not exist in the explored list or the frontier
					Node newNd = makeNode(node, s, initState);
					successors.add(newNd);
				}
			}

		}
		return successors;
	}

	private Node lowestF(List<Node> queue2) {

		double minF = queue2.get(0).getCostH(); // the lowest F cost
		Node minNd = queue2.get(0); 
		for (int i = 1; i < queue2.size(); i++) {
			if (queue2.get(i).getCostH() < minF) {
				minNd = queue2.get(i);
				minF = queue2.get(i).getCostH();
			}
		}
		return minNd;
	}
	
	protected void printResult(Node node,boolean reverse) {
		// print result
		if(!reverse) {
			Stack<State> finalPath = new Stack<State>();
			while (node.getFrontNode() != null) {
				finalPath.push(node.getState());
				node = node.getFrontNode();
			}
			finalPath.push(node.getState());

			while (!finalPath.isEmpty()) {
				State st = finalPath.pop();
				System.out.print("(" + st.getX() + "," + st.getY() + ")");
			}
		}
		else {
			while(node.getFrontNode()!=null) {
				node = node.getFrontNode();
				System.out.print("(" + node.getState().getX() + "," + node.getState().getY() + ")");
			}
		}

	}
	@Override
	protected void printStates(List<Node> queue) {
		// print states
		System.out.print("[");
		for (int i = 0; i < queue.size(); i++) {
			Node nd = queue.get(i);
			System.out.print("(" + nd.getState().getX() + "," + nd.getState().getY() + "):" + nd.getCostH());
			if (i != queue.size() - 1) {
				System.out.print(",");
			}
		}
		System.out.println("]");
	}
}
