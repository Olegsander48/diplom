package by.bntu.fitr.diplom.model;

import javafx.scene.control.Label;

import java.io.Serializable;

public class Road implements Serializable {
    private double distance;
    private double speed;
    private int roadClass;

    private double startPositionX;
    private double startPositionY;
    private double endPositionX;
    private double endPositionY;
    private int strokeWidth;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private int startXOffset;
    private int startYOffset;
    private int endXOffset;
    private int endYOffset;

    private int labelOffset;

    public Road(double distance, double speed, int roadClass, double startPositionX, double startPositionY, double endPositionX, double endPositionY, int strokeWidth, double scaleStep, int startXOffset, int startYOffset, int endXOffset, int endYOffset, int labelOffset) {
        this.distance = distance;
        this.speed = speed;
        this.roadClass = roadClass;
        this.startPositionX = startPositionX;
        this.startPositionY = startPositionY;
        this.endPositionX = endPositionX;
        this.endPositionY = endPositionY;
        this.strokeWidth = strokeWidth;
        this.startX = startPositionX / scaleStep;
        this.startY = startPositionY / scaleStep;
        this.endX = endPositionX / scaleStep;
        this.endY = endPositionY / scaleStep;
        this.startXOffset = startXOffset;
        this.startYOffset = startYOffset;
        this.endXOffset = endXOffset;
        this.endYOffset = endYOffset;
        this.labelOffset = labelOffset;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getStartPositionX() {
        return startPositionX;
    }

    public void setStartPositionX(double startPositionX) {
        this.startPositionX = startPositionX;
    }

    public double getStartPositionY() {
        return startPositionY;
    }

    public void setStartPositionY(double startPositionY) {
        this.startPositionY = startPositionY;
    }

    public double getEndPositionX() {
        return endPositionX;
    }

    public void setEndPositionX(double endPositionX) {
        this.endPositionX = endPositionX;
    }

    public double getEndPositionY() {
        return endPositionY;
    }

    public void setEndPositionY(double endPositionY) {
        this.endPositionY = endPositionY;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public int getStartXOffset() {
        return startXOffset;
    }

    public void setStartXOffset(int startXOffset) {
        this.startXOffset = startXOffset;
    }

    public int getStartYOffset() {
        return startYOffset;
    }

    public void setStartYOffset(int startYOffset) {
        this.startYOffset = startYOffset;
    }

    public int getEndXOffset() {
        return endXOffset;
    }

    public void setEndXOffset(int endXOffset) {
        this.endXOffset = endXOffset;
    }

    public int getEndYOffset() {
        return endYOffset;
    }

    public void setEndYOffset(int endYOffset) {
        this.endYOffset = endYOffset;
    }

    public int getLabelOffset() {
        return labelOffset;
    }

    public void setLabelOffset(int labelOffset) {
        this.labelOffset = labelOffset;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getRoadClass() {
        return roadClass;
    }

    public void setRoadClass(int roadClass) {
        this.roadClass = roadClass;
    }

    @Override
    public String toString() {
        return "Road{" +
                "distance=" + distance +
                ", speed=" + speed +
                ", roadClass=" + roadClass +
                ", startPositionX=" + startPositionX +
                ", startPositionY=" + startPositionY +
                ", endPositionX=" + endPositionX +
                ", endPositionY=" + endPositionY +
                ", strokeWidth=" + strokeWidth +
                ", startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", startXOffset=" + startXOffset +
                ", startYOffset=" + startYOffset +
                ", endXOffset=" + endXOffset +
                ", endYOffset=" + endYOffset +
                ", labelOffset=" + labelOffset +
                '}';
    }
}
