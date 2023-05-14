package by.bntu.fitr.diplom.controllers.algorithms.dijkstraAlgorithm;

public class Peak {
    private String label;
    private boolean isInTree;

    public Peak(String label) {
        this.label = label;
        this.isInTree = false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isInTree() {
        return isInTree;
    }

    public void setInTree(boolean inTree) {
        isInTree = inTree;
    }
}
