package jm233333.visualized;

/**
 * The {@code VisualizedBinarySearchTree} class defines the data structure {@code BinarySearchTree} for visualizing.
 * Extended from abstract class {@code VisualizedDataStructure}.
 */
public class VisualizedBinarySearchTree extends VisualizedDataStructure {

    static class Node {
        int value;
        Node left, right;
        int depth;
        Node(int value) {
            this.value = value;
            left = null;
            right = null;
            depth = 0;
        }
    }

    private Node root = null;

    public VisualizedBinarySearchTree() {
        super();
    }

    @Override
    void createVisual() {
        createVisualBST(getName());
    }

    public Node find(int value) {
        return null;
    }

    public void insert(int value) {
        if (root == null) {
            root = new Node(value);
            root.depth = 0;
            getVisualBST(getName()).insertNode(value);
            return;
        }
        insert(root, value);
    }
    private void insert(Node p, int value) {
        // assert
        assert (p != null);
        if (p.depth == 5) {
            System.out.println("Sorry, depth cannot be higher than 5.");
            return;
        }
        // deal with duplication
        if (value == p.value) {
            return;
        }
        // discussion
        if (value < p.value) {
            if (p.left == null) {
                p.left = new Node(value);
                p.left.depth = p.depth + 1;
                getVisualBST(getName()).insertNode(value);
            } else {
                insert(p.left, value);
            }
        } else {
            if (p.right == null) {
                p.right = new Node(value);
                p.right.depth = p.depth + 1;
                getVisualBST(getName()).insertNode(value);
            } else {
                insert(p.right, value);
            }
        }
    }
}
