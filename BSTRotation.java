public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     *
     * @param child  is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws IllegalArgumentException if child or parent is null, or if child
     *                                  is not a direct child of parent
     */
    protected void rotate(BinaryNode<T> child, BinaryNode<T> parent) {
        // Reject null nodes, then make sure child really is one of parent's two
        // children before rearranging any references.
        if (child == null || parent == null
                || (parent.downLeft() != child && parent.downRight() != child)) {
            throw new IllegalArgumentException("Child has to be a direct child of parent");
        }

        BinaryNode<T> grandparent = parent.up();
        boolean parentIsRight = parent.isRightChild();

        // the subtree between child and parent, which changes parents in the rotation
        BinaryNode<T> moving;

        if (child.isRightChild()) {
            // rotate left: child's left subtree becomes parent's right subtree
            moving = child.downLeft();
            parent.setRight(moving);
            // parent goes under child, on child's left
            child.setLeft(parent);
        } else {
            // rotate right: child's right subtree becomes parent's left subtree
            moving = child.downRight();
            parent.setLeft(moving);
            // parent goes under child, on child's right
            child.setRight(parent);
        }

        // the moved subtree hangs off parent now, so it needs parent as its up
        if (moving != null) {
            moving.setUp(parent);
        }

        // child now sits where parent used to, so its up is the old grandparent
        child.setUp(grandparent);
        // parent is now below child
        parent.setUp(child);

        if (grandparent == null) {
            // parent was the root, so child is the new root
            root = child;
        } else if (parentIsRight) {
            // put child in parent's old spot on the right
            grandparent.setRight(child);
        } else {
            // put child in parent's old spot on the left
            grandparent.setLeft(child);
        }
    }

    /**
     * Makes child the left child of parent, linking both directions.
     *
     * @param parent the node to attach to
     * @param child  the node to attach as parent's left child
     */
    static void linkLeft(BinaryNode<Integer> parent, BinaryNode<Integer> child) {
        parent.setLeft(child);
        child.setUp(parent);
    }

    /**
     * Makes child the right child of parent, linking both directions.
     *
     * @param parent the node to attach to
     * @param child  the node to attach as parent's right child
     */
    static void linkRight(BinaryNode<Integer> parent, BinaryNode<Integer> child) {
        parent.setRight(child);
        child.setUp(parent);
    }

    /**
     * Tests right rotations below the root (0 shared children) and at the
     * root (1 shared child).
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test1() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        BinaryNode<Integer> n30 = new BinaryNode<>(30);
        BinaryNode<Integer> n20 = new BinaryNode<>(20);
        BinaryNode<Integer> n10 = new BinaryNode<>(10);
        linkLeft(n30, n20);
        linkLeft(n20, n10);
        tree.root = n30;

        // not the root, 0 shared children
        tree.rotate(n10, n20);
        if (!tree.root.toLevelOrderString().equals("[ 30, 10, 20 ]")) {
            return false;
        }

        // the root, 1 shared child
        tree.rotate(n10, n30);
        if (!tree.root.toLevelOrderString().equals("[ 10, 30, 20 ]")) {
            return false;
        }

        // n20 moved from under n10 to under n30, and the new root has no parent
        if (n20.up() != n30 || n10.up() != null) {
            return false;
        }

        return tree.root == n10;
    }

    /**
     * Tests a right rotation and then a left rotation at the root,
     * both with 2 shared children.
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test2() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        BinaryNode<Integer> n30 = new BinaryNode<>(30);
        BinaryNode<Integer> n20 = new BinaryNode<>(20);
        BinaryNode<Integer> n40 = new BinaryNode<>(40);
        BinaryNode<Integer> n10 = new BinaryNode<>(10);
        linkLeft(n30, n20);
        linkRight(n30, n40);
        linkLeft(n20, n10);
        tree.root = n30;

        // right rotation, 2 shared children
        tree.rotate(n20, n30);
        if (!tree.root.toLevelOrderString().equals("[ 20, 10, 30, 40 ]")) {
            return false;
        }

        // left rotation, 2 shared children
        tree.rotate(n30, n20);
        return tree.root.toLevelOrderString().equals("[ 30, 20, 40, 10 ]");
    }

    /**
     * Tests a left rotation at the root with 3 shared children.
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test3() {
        BSTRotation<Integer> tree = new BSTRotation<>();
        BinaryNode<Integer> n20 = new BinaryNode<>(20);
        BinaryNode<Integer> n10 = new BinaryNode<>(10);
        BinaryNode<Integer> n40 = new BinaryNode<>(40);
        BinaryNode<Integer> n30 = new BinaryNode<>(30);
        BinaryNode<Integer> n50 = new BinaryNode<>(50);
        linkLeft(n20, n10);
        linkRight(n20, n40);
        linkLeft(n40, n30);
        linkRight(n40, n50);
        tree.root = n20;

        // left rotation, 3 shared children
        tree.rotate(n40, n20);
        if (!tree.root.toLevelOrderString().equals("[ 40, 20, 50, 10, 30 ]")) {
            return false;
        }

        // n30 moved from under n40 to under n20, and the new root has no parent
        return n30.up() == n20 && n20.up() == n40 && n40.up() == null;
    }

    /**
     * Runs all of the tests and prints whether each one passed.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        BSTRotation<Integer> tester = new BSTRotation<>();
        System.out.println("Test 1: " + tester.test1());
        System.out.println("Test 2: " + tester.test2());
        System.out.println("Test 3: " + tester.test3());
    }

}
