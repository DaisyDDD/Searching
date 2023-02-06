package help;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import startCode.Coord;
import startCode.Map;

public class SharedMethods {

	protected boolean checkCorrect(Coord start, Coord goal, Map map) {
		// Checking input validity
		int[][] mapArray = map.getMap();
		int rows = mapArray.length;
		int columns = mapArray[0].length;
		if (start.getR() >= rows || start.getC() >= columns || goal.getR() >= rows || goal.getC() >= columns) {
			// Out of bounds
			System.out.print("Error! Departure or destinations out of bounds.");
			return false;
		}
		if (mapArray[start.getR()][start.getC()] == 1 || mapArray[goal.getR()][goal.getC()] == 1) {
			// Land position
			System.out.print("Error! Departure or destinations is a land.");
			return false;
		}

		return true;
	}

	protected Node makeNode(Node parentNode, State state, State goal) {
		// make a new node
		Node newNode = new Node(parentNode, state, goal);
		return newNode;

	}

	protected List<State> successorFN(Map map, Node node) {
		// This method aims at finding successors nodes
		List<State> nextStates = new LinkedList<State>();
		int x = node.getState().getX();
		int y = node.getState().getY();

		int[][] mapArray = map.getMap();
		int rows = mapArray.length;
		int columns = mapArray[0].length;

		if (y + 1 < columns) { // if column number is not over the bound
			if (mapArray[x][y + 1] == 0) { // right direction
				nextStates.add(new State(x, y + 1));
			}
		}
		if ((x + y) % 2 == 0) { // if it's in a upwards triangle
			if (x + 1 < rows) { // if row number is not over the bound
				if (mapArray[x + 1][y] == 0) { // down direction
					nextStates.add(new State(x + 1, y));
				}
			}
		}
		if (y - 1 >= 0) { // if column number is not over the bound
			if (mapArray[x][y - 1] == 0) { // left direction
				nextStates.add(new State(x, y - 1));
			}
		}

		if ((x + y) % 2 != 0) { // if it's in a downwards triangle
			if (x - 1 >= 0) { // if row number is not over the bound
				if (mapArray[x - 1][y] == 0) { // up direction
					nextStates.add(new State(x - 1, y));
				}
			}
		}
		return nextStates;
	}

	protected boolean checkNotExist(State s, List<State> exploredStates, List<Node> frontier) {
		// This method aims at checking if a node is already explored or in the frontier
		boolean flag = true;
		for (int j = 0; j < exploredStates.size(); j++) {
			if (s.getX() == exploredStates.get(j).getX() && s.getY() == exploredStates.get(j).getY()) {
				flag = false;
			}
		}
		for (int j = 0; j < frontier.size(); j++) {
			if (s.getX() == frontier.get(j).getState().getX() && s.getY() == frontier.get(j).getState().getY()) {
				flag = false;
			}
		}
		return flag;
	}

	protected List<Node> orderedSuccessors(Node node, List<Node> successors, String alg) {
		// make the successor nodes in a right order according to the tie-breaking
		// strategy
		List<Node> orderedSuccessors = new LinkedList<Node>();
		int x = node.getState().getX();
		int y = node.getState().getY();

		if (alg == "DFS") {
			// Because DFS uses stack, the order of successor nodes that will be added into
			// frontier should be anti-clockwise
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getX() < x) {// up
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getY() < y) {// left
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getX() > x) {// down
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getY() > y) {// right
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
		} else {
			// If not DFS algorithm, order the successors into clockwise order
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getY() > y) {// right
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getX() > x) {// down
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getY() < y) {// left
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
			for (int i = 0; i < successors.size(); i++) {
				Node nd = successors.get(i);
				if (nd.getState().getX() < x) {// up
					orderedSuccessors.add(nd);
					successors.remove(nd);
				}
			}
		}

		return orderedSuccessors;

	}

	protected void printStates(List<Node> queue) {
		// print the frontier
		System.out.print("[");
		for (int i = 0; i < queue.size(); i++) {
			Node nd = queue.get(i);
			System.out.print("(" + nd.getState().getX() + "," + nd.getState().getY() + ")");
			if (i != queue.size() - 1) {
				System.out.print(",");
			}
		}
		System.out.println("]");
	}

	protected void printResult(Node node, double cost) {
		// print the result
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
		System.out.println("\n" + cost);

	}

}
