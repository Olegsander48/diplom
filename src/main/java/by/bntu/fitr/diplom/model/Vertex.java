package by.bntu.fitr.diplom.model;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.io.Serializable;

/**
 * Класс-хранилище для передачи и получения вершин
 *
 * @author Prigodich A.N.
 * @version 1.0.1.2
 * */
public class Vertex<T extends Shape> implements Serializable {
    private int fontSize;
    private double mousePositionX, mousePositionY; //координаты в текущей точке вершины
    transient private T shape;
    transient private Label label;

    private SerializableShape serializableShape;

    private double centerX, centerY; //координаты без масштаба

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(T shape) {
        this.shape = shape;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public double getMousePositionX() {
        return mousePositionX;
    }

    public void setMousePositionX(double mousePositionX) {
        this.mousePositionX = mousePositionX;
    }

    public double getMousePositionY() {
        return mousePositionY;
    }

    public void setMousePositionY(double mousePositionY) {
        this.mousePositionY = mousePositionY;
    }

    public SerializableShape getSerializableShape() {
        return serializableShape;
    }

    public void setSerializableShape(SerializableShape serializableShape) {
        this.serializableShape = serializableShape;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "fontSize=" + fontSize +
                ", mousePositionX=" + mousePositionX +
                ", mousePositionY=" + mousePositionY +
                ", shape=" + shape +
                ", label=" + label +
                ", serializableShape=" + serializableShape +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                '}';
    }
}
