package gcrf_tool.generators;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

	public static double[][] generateDirectedGraph(int noOfNodes) {
		double[][] graph = new double[noOfNodes][noOfNodes];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (i != j) {
					graph[i][j] = Math.random();
				}
			}
		}
		return graph;
	}

	public static double[][] generateDirectedGraphWithEdgeProbability(
			int noOfNodes, double probability) {
		double p = 1 - probability;
		double[][] graph = new double[noOfNodes][noOfNodes];
		double tempP = 0;
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (i != j) {
					tempP = Math.random();
					if (tempP >= p) {
						graph[i][j] = Math.random();
					}
				}
			}
		}
		return graph;
	}

	public static double[][] converteGraphToUndirected(double[][] graph) {
		double[][] graphUndirected = new double[graph.length][graph.length];
		double first = 0;
		double second = 0;
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (graphUndirected[i][j] == 0) {
					if (i != j) {
						first = graph[i][j];
						second = graph[j][i];
						graphUndirected[i][j] = (first + second) / 2;
						graphUndirected[j][i] = (first + second) / 2;
					}
				}
			}
		}
		return graphUndirected;
	}

	public static double[][] generateDirectedAcyclicGraph(int noOfNodes) {
		int m = (noOfNodes * (noOfNodes - 1)) / 2;
		long seed = 1;
		boolean simple = true, directed = true, acyclic = true, weighted = true;
		int minweight = 0;
		int maxweight = 1;
		int nodei[] = new int[m + 1];
		int nodej[] = new int[m + 1];
		double weight[] = null;
		weight = new double[m + 1];

		int k = GraphGenerator.randomGraph(noOfNodes, m, seed, simple,
				directed, acyclic, weighted, minweight, maxweight, nodei,
				nodej, weight);
		if (k != 0)
			System.out.println("Invalid input data, error code = " + k);
		else {
			double[][] matrix = new double[noOfNodes][noOfNodes];
			for (int i = 1; i <= m; i++) {
				int indexI = nodei[i] - 1;
				int indexJ = nodej[i] - 1;
				matrix[indexI][indexJ] = weight[i];
			}

			return matrix;
		}
		return null;

	}

	public static double[][] generateGraphNoFeedback(int noOfNodes) {
		double[][] graph = generateDirectedGraph(noOfNodes);
		double[][] finalGraph = new double[noOfNodes][noOfNodes];
		for (int i = 0; i < finalGraph.length; i++) {
			for (int j = i + 1; j < finalGraph.length; j++) {
				if (graph[i][j] > graph[j][i]) {
					finalGraph[i][j] = Math.random();
					finalGraph[j][i] = 0;
				} else {
					finalGraph[i][j] = 0;
					finalGraph[j][i] = Math.random();
				}
			}
		}
		return finalGraph;
	}

	public static double[][] generateChain(int noOfNodes) {
		double[][] graph = new double[noOfNodes][noOfNodes];

		List<Integer> choosen = new ArrayList<>();
		Random r = new Random();
		int prev = r.nextInt(noOfNodes);
		int next = 0;
		choosen.add(prev);
		while (choosen.size() < noOfNodes) {
			next = r.nextInt(noOfNodes);
			while (choosen.contains(next)) {
				next = r.nextInt(noOfNodes);
			}
			graph[prev][next] = Math.random();
			;
			prev = next;
			choosen.add(prev);
		}

		return graph;
	}

	public static double[][] generateBinaryTree(int noOfNodes) {
		double[][] matrix = new double[noOfNodes][noOfNodes];
		Random r = new Random();
		int root = r.nextInt(noOfNodes);
		List<Integer> nodes = new ArrayList<>();
		nodes.add(root);
		// System.out.println(root + 1);
		generateNode(root, nodes, noOfNodes, matrix);
		return matrix;
	}

	public static void generateNode(int root, List<Integer> nodes, int total,
			double[][] matrix) {
		Random r = new Random();
		int left = r.nextInt(total);
		int rigth = r.nextInt(total);
		if (nodes.size() < total) {
			while (nodes.contains(left)) {
				left = r.nextInt(total);
			}
			matrix[root][left] = Math.random();
			nodes.add(left);
			if (nodes.size() == total) {
				return;
			}
			while (nodes.contains(rigth)) {
				rigth = r.nextInt(total);
			}
			matrix[root][rigth] = Math.random();
			nodes.add(rigth);
			if (nodes.size() == total) {
				return;
			}
			generateNode(left, nodes, total, matrix);
			generateNode(rigth, nodes, total, matrix);
		}
	}

	public static double[][] generateGraphByType(int noOfNodes, String type,
			double probability) {
		double[][] graph = null;
		switch (type) {
		case "DirectedGraph":
			graph = GraphGenerator.generateDirectedGraph(noOfNodes);
			break;
		case "DirectedAcyclicGraph":
			graph = GraphGenerator.generateDirectedAcyclicGraph(noOfNodes);
			break;
		case "DirectedGraphWithoutDirectFeedback":
			graph = GraphGenerator.generateGraphNoFeedback(noOfNodes);
			break;
		case "DirectedGraphWithEdgeProbability":
			graph = GraphGenerator.generateDirectedGraphWithEdgeProbability(
					noOfNodes, probability);
			break;
		case "Chain":
			graph = GraphGenerator.generateChain(noOfNodes);
			break;
		case "BinaryTree":
			graph = GraphGenerator.generateBinaryTree(noOfNodes);
			break;
		default:
			graph = null;
			break;
		}
		return graph;
	}

	public static int randomGraph(int n, int m, long seed, boolean simple,
			boolean directed, boolean acyclic, boolean weighted, int minweight,
			int maxweight, int nodei[], int nodej[], double weight[]) {

		int maxedges, nodea, nodeb, numedges, temp;
		int dagpermute[] = new int[n + 1];
		boolean adj[][] = new boolean[n + 1][n + 1];
		Random ran = new Random(seed);
		// initialize the adjacency matrix
		for (nodea = 1; nodea <= n; nodea++)
			for (nodeb = 1; nodeb <= n; nodeb++)
				adj[nodea][nodeb] = false;
		numedges = 0;
		// check for valid input data
		if (simple) {
			maxedges = n * (n - 1);
			if (!directed)
				maxedges /= 2;
			if (m > maxedges)
				return 1;
		}
		if (acyclic) {
			maxedges = (n * (n - 1)) / 2;
			if (m > maxedges)
				return 1;
			randomPermutation(n, ran, dagpermute);
		}
		while (numedges < m) {

			nodea = ran.nextInt(n) + 1;
			nodeb = ran.nextInt(n) + 1;
			if (simple || acyclic)
				if (nodea == nodeb)
					continue;
			if ((simple && (!directed)) || acyclic)
				if (nodea > nodeb) {
					temp = nodea;
					nodea = nodeb;
					nodeb = temp;
				}
			if (acyclic) {
				nodea = dagpermute[nodea];
				nodeb = dagpermute[nodeb];
			}
			if ((!simple) || (simple && (!adj[nodea][nodeb]))) {
				numedges++;
				nodei[numedges] = nodea;
				nodej[numedges] = nodeb;
				adj[nodea][nodeb] = true;
				if (weighted)
					weight[numedges] = Math.random();
			}
		}
		return 0;
	}

	private static void randomPermutation(int n, Random ran, int perm[]) {
		int i, j, k;
		for (i = 1; i <= n; i++)
			perm[i] = i;
		for (i = 1; i <= n; i++) {
			j = (int) (i + ran.nextDouble() * (n + 1 - i));
			k = perm[i];
			perm[i] = perm[j];
			perm[j] = k;
		}
	}

	public static void showMatrix(double[][] s) {
		DecimalFormat df = new DecimalFormat("#.##");
		for (int i = 0; i < s.length; i++) {
			double[] s1 = s[i];
			System.out.print((i + 1) + "\t");
			for (int j = 0; j < s1.length; j++) {
				System.out.print(df.format(s[i][j]) + "\t");
			}
			System.out.println();
		}
	}

}
