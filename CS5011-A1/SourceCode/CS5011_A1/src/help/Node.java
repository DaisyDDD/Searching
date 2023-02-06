package help;

public class Node {

	private State state;
	private double costPath = 0; // path cost
	private double cost = 0; // single step
	private double costH = 0; // H-cost
	private double costF = 0; // F-cost
	private Node frontNode; // parent node

	public Node(Node frontNode, State state, State goal) {
		this.setState(state);
		this.setCost(1);
		this.setFrontNode(frontNode);
		if (frontNode == null) { // root start node
			this.setCostPath(0);
		} else {
			this.setCostPath(this.frontNode.getCostPath() + this.getCost());
		}
		if (goal != null) {
			int costH = estimateCost(state, goal);
			this.setCostH(costH);
		}
		this.setCostF(this.getCostH() + this.getCostPath()); // in A*

	}

	public int estimateCost(State state, State goal) { // estimate Manhattan distance
		return Math.abs(state.getX() - goal.getX()) + Math.abs(state.getY() - goal.getY());
	}
	public int estimateCostTri(State state, State goal) {// estimate triangle Manhattan distance
		int aS,bS,cS,aG,bG,cG;
		int xS = state.getX();
		int yS = state.getY();
		int xG = goal.getX();
		int yG = goal.getY();
		int dirS, dirG;
		if ((xS + yS) % 2 == 0) {
			dirS = 0;
		}else {
			dirS = 1;
		}
		if((xG + yG) % 2 == 0) {
			dirG = 0;
		}else {
			dirG = 1;
		}
		aS = 0 - xS;
		bS = (xS + yS - dirS)/2 ;
		cS = (xS + yS - dirS)/2 - xS + dirS;
		aG = 0 - xG;
		bG = (xG + yG - dirG)/2 ;
		cG = (xG + yG - dirG)/2 - xG + dirG;
		return Math.abs(aG-aS)+ Math.abs(bG-bS)+ Math.abs(cG-cS);
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Node getFrontNode() {
		return frontNode;
	}

	public void setFrontNode(Node frontNode) {
		this.frontNode = frontNode;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public double getCostH() {
		return costH;
	}

	public void setCostH(double costToGoal) {
		this.costH = costToGoal;
	}

	public double getCostF() {
		return costF;
	}

	public void setCostF(double costF) {
		this.costF = costF;
	}

	public double getCostPath() {
		return costPath;
	}

	public void setCostPath(double costPath) {
		this.costPath = costPath;
	}
}
