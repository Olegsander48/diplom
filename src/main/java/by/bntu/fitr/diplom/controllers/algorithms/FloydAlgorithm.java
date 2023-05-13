package by.bntu.fitr.diplom.controllers.algorithms;

import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.Vertex;
import java.util.List;

public class FloydAlgorithm {
    private final List<Vertex> vertexList;
    private final List<Road> roadList;
    private final double[][] arr;

    public FloydAlgorithm(List<Vertex> vertexList, List<Road> roadList) {
        this.vertexList = vertexList;
        this.roadList = roadList;
        arr = new double[vertexList.size()][vertexList.size()];
    }

    private void calculateMatrixOfOptimalConnections(boolean speedLimit) {
        calculateTransportNetworkModel(Double.POSITIVE_INFINITY, speedLimit);

        for (int k = 0; k < vertexList.size(); k++) {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    arr[i][j] = Math.min(arr[i][j], arr[i][k] + arr[k][j]);
                }
            }
        }
    }

    private void calculateTransportNetworkModel(double plug, boolean speedLimit) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (i == j) {
                    arr[i][j] = 0;
                } else {
                    arr[i][j] = checkThatRoadConnectedToString(vertexList.get(i), vertexList.get(j), plug, speedLimit);
                }
            }
        }
    }

    private double checkThatRoadConnectedToString(Vertex startVertex, Vertex endVertex, double plug, boolean speedLimit) {
        double result = plug;
        for (Road road : roadList) {
            if (startVertex.getMousePositionX() == road.getStartPositionX()
                    && startVertex.getMousePositionY() == road.getStartPositionY()
                    && endVertex.getMousePositionX() == road.getEndPositionX()
                    && endVertex.getMousePositionY() == road.getEndPositionY()) {
                double value = Double.parseDouble(road.getDistance());
                result = speedLimit ? value / road.getSpeed() : value;
            }
        }
        return result;
    }

    public double[][] getMatrixOfOptimalConnections(boolean speedLimit) {
        calculateMatrixOfOptimalConnections(speedLimit);
        return arr;
    }

    public double[][] getTransportNetworkModel(boolean speedLimit) {
        calculateTransportNetworkModel(0, speedLimit);
        return arr;
    }
}
