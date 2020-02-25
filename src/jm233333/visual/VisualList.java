package jm233333.visual;

import java.util.ArrayList;

import jm233333.Director;

/**
 * The {@code VisualList} class defines the graphic components of a list that is displayed on the monitor.
 * Used in subclasses of {@code VisualizedDataStructure}.
 */
public class VisualList extends Visual {

    public static final int CACHED_NODE = -1;

    private ArrayList<VisualListNode> arrayNode = new ArrayList<>();
    private VisualListNode cachedNode = null;

    public VisualList(String name) {
        // super
        super(name);
        // initialize
        this.getStyleClass().add("visual-list");
    }

    private VisualListNode createNode(int index, int value) {
        VisualListNode node = new VisualListNode(value);
        node.setLayoutX(32 + index * (32 + node.getWidth()));
        node.setLayoutY(96);
        this.getChildren().add(node);
        return node;
    }
    private void removeNode(int index) {
        VisualListNode node = arrayNode.remove(index);
        Director.getInstance().createAnimation(1.0, node.scaleXProperty(), 0);
        Director.getInstance().updateAnimation(1.0, node.scaleYProperty(), 0);
        Director.getInstance().getLastTimeline().setOnFinished((event) -> {
            this.getChildren().remove(node);
        });
    }

    public void cacheNode(int index, int value) {
        if (cachedNode == null) {
            cachedNode = createNode(index, value);
        }
    }
    public void insertCachedNode(int index) {
        if (cachedNode != null) {
            arrayNode.add(index, cachedNode);
            Director.getInstance().createAnimation(1.0, cachedNode.layoutYProperty(), 0);
            cachedNode = null;
        }
    }

    public void setPointerNext(int indexFrom, int indexTo) {
        VisualListNode nodeFrom = (indexFrom == CACHED_NODE ? cachedNode : arrayNode.get(indexFrom));
        VisualListNode nodeTo = (indexTo == CACHED_NODE ? cachedNode : arrayNode.get(indexTo));
        nodeFrom.setNext(nodeTo);
    }
    public void setPointerNextToNull(int index) {
        VisualListNode node = (index == CACHED_NODE ? cachedNode : arrayNode.get(index));
        node.setNext(null);
    }

    public void moveRestNodes(int begin, int distance) {
        if (begin < arrayNode.size()) {
            VisualListNode lastNode = arrayNode.get(begin);
            double unitLength = lastNode.getWidth() + VisualNode.BOX_SIZE / 2;
            Director.getInstance().createAnimation(1.0, lastNode.layoutXProperty(), lastNode.getLayoutX() + unitLength * distance);
            for (int i = begin + 1; i < arrayNode.size(); i++) {
                VisualListNode node = arrayNode.get(i);
                Director.getInstance().updateAnimation(1.0, node.layoutXProperty(), node.getLayoutX() + unitLength * distance);
            }
        }
    }

    public void pushFrontNode(int value) {
        insertNode(0, value);
    }
    public void insertNode(int index, int value) {
        // insert new node
        VisualListNode newNode = createNode(index, value);
        arrayNode.add(index, newNode);
        // set pointer next
        if (index > 0) {
            setPointerNext(index - 1, index);
        }
        if (index + 1 < arrayNode.size()) {
            setPointerNext(index, index + 1);
        }
        // move rest nodes
        moveRestNodes(index + 1, 1);
        // play animation
        Director.getInstance().createAnimation(1.0, newNode.layoutYProperty(), 0);
        Director.getInstance().playAnimation();
    }

    public void updateNode(int index, int value) {
        VisualListNode node = arrayNode.get(index);
        node.setValue(value);
    }

    public void popFrontNode() {
        eraseNode(0);
    }
    public void eraseNode(int index) {
        // get erased node
        VisualListNode erasedNode = arrayNode.get(index);
        removeNode(index);
        // set pointer
        if (index > 0) {
            if (index < arrayNode.size()) {
                setPointerNext(index - 1, index);
            } else {
                setPointerNextToNull(index - 1);
            }
        }
        // move rest nodes
        moveRestNodes(index, -1);
        // play animation
        Director.getInstance().createAnimation(1.0, erasedNode.layoutYProperty(), 96);
        Director.getInstance().playAnimation();
    }
}
