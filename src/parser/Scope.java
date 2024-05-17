package parser;
import java.util.Stack;

public class Scope {
	public String data;
	public int scopeId;
	public Stack<Integer> path;
	
	public Scope(String data, int scopeId) {
		this.data = data;
		this.scopeId = scopeId;
		this.path = new Stack<Integer>();
	}
}
