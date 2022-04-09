import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class MaxFlow {
//Use Linked List
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int instances = scan.nextInt();
		while (instances-- > 0) {
			Graph g = new Graph(scan.nextInt());
			int numEdge = scan.nextInt();
			while (numEdge-- > 0) {
				g.addEdge(scan.nextInt()-1, scan.nextInt()-1, scan.nextInt());
			}
			System.out.println(g.Maxflow());// TODO: The max flow
		}
		scan.close();
	}
}

class Edge {
	int destination;
	int capacity;

	Edge(int destination, int capacity) {
		this.capacity = capacity;
		this.destination = destination;
	}
}

class Graph {
	private int V; // No. of vertices
	private LinkedList<Edge> adj[]; // Adjacency Lists

	// Constructor
	Graph(int v) {
		V = v;
		adj = new LinkedList[v];
		for (int i = 0; i < v; ++i)
			adj[i] = new LinkedList<Edge>();
	}

	// Function to add an edge into the graph
	void addEdge(int v, int destination, int capacity) {
		adj[v].add(new Edge(destination, capacity));
	}

	int[] BFS(int s) {
		// Mark all the vertices as not visited(By default
		// set as false)
		boolean visited[] = new boolean[V];
		// Create a queue for BFS
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// Mark the current node as visited and enqueue it
		visited[s] = true;
		queue.add(s);
		int parent[] = new int[V];
		parent[s] = -1;
		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			s = queue.poll();
			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Iterator<Edge> i = adj[s].listIterator();
			while (i.hasNext()) {
				Edge n = i.next();
				if ((!visited[n.destination])&&(n.capacity!=0)) {
					 parent[n.destination] = s;
					visited[n.destination] = true;
					queue.add(n.destination);
					if(n.destination==V) {
						return parent;
					}
				}
			}
			
		}
		return parent;
	}

	int Maxflow() {
		int[] parent = BFS(0);
		//recover the path
		LinkedList<Integer> path = new LinkedList<Integer>();
		int min = Integer.MAX_VALUE;
		path.add(V-1);
        for (int v = V-1; v != 0; v = parent[v]) {
            path.add(0, parent[v]);
            for(int i = 0; i<adj[parent[v]].size();++i) {
            	if(adj[parent[v]].get(i).destination==v&&adj[parent[v]].get(i).capacity<min) {
            		min = adj[parent[v]].get(i).capacity;
            		continue;
            	}
            }
        }
        if(min == Integer.MAX_VALUE) {
        	min = 0;
        }
		int max = min;
		/// we need to while loop to make sure there is no BFS,
		// ie. BFS(1).size = 0
		while (min != 0) {
			for (int i = 0; i < path.size()-1; ++i) {
				for (int j = 0; j < adj[path.get(i)].size(); ++j) {
					if (adj[path.get(i)].get(j).destination == path.get(i + 1)&&adj[path.get(i)].get(j).capacity >= min) {
						adj[path.get(i)].get(j).capacity -= min;
						boolean found = false;
						for (int k = 0; k < adj[path.get(i + 1)].size(); ++k) {
							if (adj[path.get(i + 1)].get(k).destination == path.get(i)) {
								found = true;
								adj[path.get(i + 1)].get(k).capacity += min;
								continue;
							}
						}
						if (!found) {
							adj[path.get(i + 1)].add(new Edge(path.get(i), min));
						}
						break;
					}
				}
			}
			parent = BFS(0);
			path = new LinkedList<Integer>();
			min = Integer.MAX_VALUE;
			path.add(V-1);
	        for (int v = V-1; v != 0; v = parent[v]) {
	            path.add(0, parent[v]);
	            int max_in = 0;
	            for(int i = 0; i<adj[parent[v]].size();++i) {
	            	if(adj[parent[v]].get(i).destination==v&&adj[parent[v]].get(i).capacity>max_in) {
	            		max_in = adj[parent[v]].get(i).capacity;
	            	}
	            }
            	if(max_in<min) {
            		min = max_in;
            	}
	        }
			max = max + min;
		}
		return max;
	}

}