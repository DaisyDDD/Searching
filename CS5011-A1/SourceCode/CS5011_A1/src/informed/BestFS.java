package informed;

import java.util.LinkedList;
import java.util.List;

import help.Node;
import help.State;
import startCode.Coord;
import startCode.Map;
import help.SharedMethods;

public class BestFS extends SharedMethods {
	private List<State> exploredStates = new LinkedList<State>();
	private List<Node> frontier = new LinkedList<Node>();
	private State initState;
	private State goalState;
	private double cost = 0;
	private Map map;

	public BestFS(Map map, Coord start, Coord goal) {
		this.map = map;
		if(!checkCorrect(start,goal,map)) {
			System.exit(0);
		}
		this.initState = new State(start.getR(), start.getC());
		this.goalState = new State(goal.getR(), goal.getC());
		if (!treeSearch(start, goal)) {
			System.out.println("fail");
		}

		System.out.println(exploredStates.size());

	}

	private boolean treeSearch(Coord start, Coord goal) {
		Node n = makeNode(null, initState, goalState);// initial node
		frontier.add(n);

		while (true) {
			if (frontier.isEmpty()) {
				return false;
			}
			Node node = lowestF(frontier);

			exploredStates.add(node.getState());
			printStates(frontier);

			frontier.remove(node);
			if (node.getState().getX() == goalState.getX() && node.getState().getY() == goalState.getY()) {
				// find the goal
				cost = node.getCostPath();
				printResult(node, cost);
				return true;
			} else {
				List<Node> nextSuccess = expand(node);// find successor nodes
				for (int i = 0; i < nextSuccess.size(); i++) {
					frontier.add(nextSuccess.get(i));
				}
			}
		}

	}

	private List<Node> expand(Node node) {
		List<Node> successors = new LinkedList<Node>();

		List<State> nextStates = new LinkedList<State>();
		nextStates = successorFN(map, node);

		for (int i = 0; i < nextStates.size(); i++) {
			State s = nextStates.get(i);
			if (checkNotExist(s, exploredStates, frontier)) {
				// if not exist in the explored list or the frontier
				Node newNd = makeNode(node, s, goalState);
				successors.add(newNd);
			}
		}
		return successors;
	}

	private static Node lowestF(List<Node> queue2) {

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
