package by.bntu.fitr.diplom.controllers.algorithms.dijkstraAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Path { // объект данного класса содержащий расстояние и предыдущие и пройденные вершины
    private int distance; // текущая дистанция от начальной вершины
    private double transitTime; //время прохождения текущей дистанции
    private double speedLimit; //ограничение скорости на определенном участке
    private List<Integer> parentVertices; // текущий родитель вершины

    public Path(int distance, double speedLimit) {
        this.distance = distance;
        this.parentVertices = new ArrayList<>();
        this.speedLimit = speedLimit;
        this.transitTime = distance / speedLimit;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Integer> getParentVertices() {
        return parentVertices;
    }

    public void setParentVertices(List<Integer> parentVertices) {
        this.parentVertices = parentVertices;
    }

    public double getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(double distance) {
        this.transitTime = distance / speedLimit;
        System.out.println("Speed limit = " + speedLimit);
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }
}
