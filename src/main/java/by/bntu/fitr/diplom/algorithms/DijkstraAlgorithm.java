package by.bntu.fitr.diplom.algorithms;

import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.Vertex;

import java.util.*;

public class DijkstraAlgorithm {
    private static final int NO_PARENT = -1;
    private final List<Vertex> vertexList;
    private final List<Road> roadList;
    private final double[][] adjacencyMatrix;

    public DijkstraAlgorithm(List<Vertex> vertexList, List<Road> roadList) {
        this.vertexList = vertexList;
        this.roadList = roadList;
        adjacencyMatrix = new double[vertexList.size()][vertexList.size()];
    }

    public static Map<String, Double> shortestPath(double[][] adjacencyMatrix, Map<String, Integer> vertexNames,
                                                    String startVertexName, String endVertexName) {
        int startVertex = vertexNames.get(startVertexName);
        int endVertex = vertexNames.get(endVertexName);

        int nVertices = adjacencyMatrix[0].length;
        double[] shortestDistances = new double[nVertices];
        boolean[] visited = new boolean[nVertices];
        int[] parents = new int[nVertices];

        for (int i = 0; i < nVertices; i++) {
            shortestDistances[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        shortestDistances[startVertex] = 0;
        parents[startVertex] = NO_PARENT;

        for (int i = 1; i < nVertices; i++) {
            int nearestVertex = -1;
            double shortestDistance = Double.MAX_VALUE;
            for (int vertex = 0; vertex < nVertices; vertex++) {
                if (!visited[vertex] && shortestDistances[vertex] < shortestDistance) {
                    nearestVertex = vertex;
                    shortestDistance = shortestDistances[vertex];
                }
            }

            visited[nearestVertex] = true;
            for (int vertex = 0; vertex < nVertices; vertex++) {
                double edgeDistance = adjacencyMatrix[nearestVertex][vertex];
                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertex])) {
                    parents[vertex] = nearestVertex;
                    shortestDistances[vertex] = shortestDistance + edgeDistance;
                }
            }
        }

        Map<String, Double> path = getPath(endVertex, parents, vertexNames, shortestDistances);
        return path;
    }

    private static Map<String, Double> getPath(int endVertex, int[] parents, Map<String, Integer> vertexNames,
                                                double[] shortestDistances) {
        Map<String, Double> path = new LinkedHashMap<>();
        for (int vertex = endVertex; vertex != NO_PARENT; vertex = parents[vertex]) {
            path.put(getVertexName(vertex, vertexNames), shortestDistances[vertex]);
        }
        Map<String, Double> reversedPath = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>(path.keySet());
        Collections.reverse(keys);
        for (String key : keys) {
            reversedPath.put(key, path.get(key));
        }
        return reversedPath;
    }

    private static String getVertexName(int vertex, Map<String, Integer> vertexNames) {
        for (Map.Entry<String, Integer> entry : vertexNames.entrySet()) {
            if (entry.getValue() == vertex) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Map<String, Double> getMapOfDistances(String startVertexName, String endVertexName) {
        return getOptimalRoute(startVertexName, endVertexName, false);
    }

    public Map<String, Double> getMapOfTimes(String startVertexName, String endVertexName) {
        return getOptimalRoute(startVertexName, endVertexName, true);
    }

    public Map<String, Double> getOptimalRoute(String startVertexName, String endVertexName, boolean speedLimit) {
        double[][] adjacencyMatrix = new FloydAlgorithm(vertexList, roadList).getTransportNetworkModel(speedLimit);

        Map<String, Integer> vertexNames = new HashMap<>();
        int count = 0;
        for (Vertex vertex : vertexList) {
            vertexNames.put(vertex.getLabel().getText(), count++);
        }

        Map<String, Double> path = shortestPath(adjacencyMatrix, vertexNames, startVertexName, endVertexName);
        return path;
    }
}
