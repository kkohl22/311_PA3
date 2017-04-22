import java.util.ArrayList;

public class CostPair {
	private ArrayList<Integer> path;
	private int cost = 0;
	
	public CostPair(ArrayList<Integer> path, int cost) {
		this.path = path;
		this.cost = cost;
	}
	
	public CostPair() {
		this.path = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getPath() {
		return this.path;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public void addToPath(int number) {
		this.path.add(number);
	}
	
	public void addToCost(int number) {
		this.cost += number;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
}
