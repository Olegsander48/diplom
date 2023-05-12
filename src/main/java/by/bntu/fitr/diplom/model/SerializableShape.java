package by.bntu.fitr.diplom.model;

import java.io.Serializable;

public class SerializableShape implements Serializable {
    private String shapeType;
    private String shapeFill;
    private String shapeStroke;
    private double shapeSideLength;
    private double shapeStrokeWidth;

    private String labelText;
    private String labelFontName;
    private double labelFontSize;
    private String labelColor;

    public SerializableShape(Vertex vertex) {
        this.shapeType = vertex.getShape().getTypeSelector();
        this.shapeFill = vertex.getShape().getFill().toString();
        this.shapeStroke = vertex.getShape().getStroke().toString();
        this.shapeSideLength = vertex.getFontSize() / 2;
        this.shapeStrokeWidth = vertex.getFontSize() / 5;
        this.labelText = vertex.getLabel().getText();
        this.labelFontName = vertex.getLabel().getFont().getName();
        this.labelFontSize = vertex.getLabel().getFont().getSize();
        this.labelColor = vertex.getLabel().getTextFill().toString();
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getShapeFill() {
        return shapeFill;
    }

    public void setShapeFill(String shapeFill) {
        this.shapeFill = shapeFill;
    }

    public String getShapeStroke() {
        return shapeStroke;
    }

    public void setShapeStroke(String shapeStroke) {
        this.shapeStroke = shapeStroke;
    }

    public double getShapeSideLength() {
        return shapeSideLength;
    }

    public void setShapeSideLength(double shapeSideLength) {
        this.shapeSideLength = shapeSideLength;
    }

    public double getShapeStrokeWidth() {
        return shapeStrokeWidth;
    }

    public void setShapeStrokeWidth(double shapeStrokeWidth) {
        this.shapeStrokeWidth = shapeStrokeWidth;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getLabelFontName() {
        return labelFontName;
    }

    public void setLabelFontName(String labelFontName) {
        this.labelFontName = labelFontName;
    }

    public double getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(double labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }
}
