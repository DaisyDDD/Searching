package uninformed;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import help.Node;
import help.State;
import startCode.Coord;
import startCode.Map;
import help.SharedMethods;

public class DFS extends SharedMethods {

	private List<State> exploredStates = new LinkedList<State>();
	private List<Node> frontier = new Stack<Node>();
	private State initState;
	private State goalState;
	private double cost = 0;
	private Map map;

	public DFS(Map map, Coord start, Coord goal) {
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

		Node n = makeNode(null, initState, null);// initial node
		frontier.add(n);

		while (true) {
			if (frontier.isEmpty()) {
				return false;
			}
			printStates(frontier);
			Node node = frontier.remove(frontier.size() - 1);
			exploredStates.add(node.getState());

			if (node.getState().getX() == goalState.getX() && node.getState().getY() == goalState.getY()) {
				// find the goal
				cost = node.getCostPath();
				printResult(node, cost);
				return true;
			} else {
				List<Node> nextSuccess = expand(node); // find successor nodes
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
				Node newNd = makeNode(node, s, null);
				successors.add(newNd);
				successors = orderedSuccessors(node, successors, "DFS");
			}
		}
		return successors;
	}

}
