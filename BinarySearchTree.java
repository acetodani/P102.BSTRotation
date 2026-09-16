public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

    protected BinaryNode<T> root = null;

    public BinarySearchTree() {
    }

    /**
     * Inserts the provided data into this collection.
     *
     * @param data the data to add
     * @throws NullPointerException if data is null
     */
    @Override
    public void add(T data) {
        if (data == null) {
            throw new NullPointerException("Data cannot be null");
        }
        BinaryNode<T> newNode = new BinaryNode<T>(data);

        if (root == null) {
            root = newNode;
        } else {
            addHelper(newNode, root);
        }
    }

    /**
     * Checks whether the collection contains the specified value.
     *
     * @param find the value to search for
     * @return true if the value is contained in this collection, false otherwise
     */
    @Override
    public boolean contains(Comparable<T> find) {
        return containsHelper(find, root);
    }

    /**
     * Recursively searches the provided subtree for the specified value.
     *
     * @param find    the value to search for
     * @param subtree the subtree in which to search
     * @return true if the value is found, false otherwise
     */
    public boolean containsHelper(Comparable<T> find, BinaryNode<T> subtree) {
        if (subtree == null) {
            return false;
        }

        int comparison = find.compareTo(subtree.getEntry());

        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return containsHelper(find, subtree.downLeft());
        } else {
            return containsHelper(find, subtree.downRight());
        }
    }

    /**
     * Returns the number of values in this collection.
     *
     * @return the number of values in this collection
     */
    @Override
    public int size() {
        BinaryNode<T> current = root;

        if (current == null) {
            return 0;
        }

        return sizeHelper(current);
    }

    /**
     * Recursively counts the values in the provided subtree.
     *
     * @param current the subtree to count
     * @return the number of values in the subtree
     */
    protected int sizeHelper(BinaryNode<T> current) {
        if (current == null) {
            return 0;
        }
        return 1 + sizeHelper(current.downLeft()) + sizeHelper(current.downRight());
    }

    /**
     * Checks whether this collection is empty
     *
     * @return true if this collection contains no values, false otherwise
     */
    @Override
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     * Removes all values from this collection.
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized)
     * into the provided tree/subtree but when the provided subtree
     * is null, this method does nothing.
     */
    protected void addHelper(BinaryNode<T> newNode, BinaryNode<T> subtree) {
        if (subtree == null) {
            return;
        }
        // newNode is less than or equal to subtree's value must go left
        if (newNode.entry.compareTo(subtree.getEntry()) <= 0) {
            if (subtree.downLeft() == null) {
                subtree.setLeft(newNode);
                newNode.setUp(subtree);
            } else {
                addHelper(newNode, subtree.downLeft());
            }
            // newNode is strictly greater than subtree's value must go right
        } else if (subtree.downRight() == null) {
            subtree.setRight(newNode);
            newNode.setUp(subtree);
        } else {
            addHelper(newNode, subtree.downRight());
        }
    }

    /**
     * Tests inserting integers in a few different orders.
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test1() {
        // adding the middle values first gives us a balanced tree
        BinarySearchTree<Integer> balanced = new BinarySearchTree<>();
        balanced.add(50);
        balanced.add(30);
        balanced.add(70);
        balanced.add(20);
        balanced.add(40);
        balanced.add(60);
        balanced.add(80);

        // level order shows the shape, in order should come out sorted
        String levels = balanced.root.toLevelOrderString();
        String sorted = balanced.root.toInOrderString();
        if (!levels.equals("[ 50, 30, 70, 20, 40, 60, 80 ]")) {
            return false;
        }
        if (!sorted.equals("[ 20, 30, 40, 50, 60, 70, 80 ]")) {
            return false;
        }

        // the root has no parent everything else goes back up
        if (balanced.root.up() != null) {
            return false;
        }
        if (balanced.root.downLeft().up() != balanced.root) {
            return false;
        }
        if (balanced.root.downRight().downLeft().up() != balanced.root.downRight()) {
            return false;
        }

        // adding in increasing order leans everything to the right
        BinarySearchTree<Integer> increasing = new BinarySearchTree<>();
        increasing.add(10);
        increasing.add(20);
        increasing.add(30);
        if (increasing.root.downLeft() != null) {
            return false;
        }
        if (increasing.root.downRight().downRight().getEntry() != 30) {
            return false;
        }

        // and decreasing order leans everything to the left
        BinarySearchTree<Integer> decreasing = new BinarySearchTree<>();
        decreasing.add(30);
        decreasing.add(20);
        decreasing.add(10);
        if (decreasing.root.downRight() != null) {
            return false;
        }
        if (decreasing.root.downLeft().downLeft().getEntry() != 10) {
            return false;
        }

        return true;
    }

    /**
     * Tests searching a tree of Strings (films) at every kind of positon
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test2() {
        BinarySearchTree<String> movies = new BinarySearchTree<>();
        movies.add("La La Land");
        movies.add("Cleo 9 to 5");
        movies.add("Past Lives");
        movies.add("Arrival");
        movies.add("Interstellar");
        movies.add("Whiplash");

        // La La Land sits at the root Cleo and Past Lives are children nodes
        if (!movies.contains("La La Land")) {
            return false;
        }
        if (!movies.contains("Cleo 9 to 5") || !movies.contains("Past Lives")) {
            return false;
        }
        // Arrival is a left leaf, Interstellar and Whiplash are right leaves
        if (!movies.contains("Arrival") || !movies.contains("Interstellar")
                || !movies.contains("Whiplash")) {
            return false;
        }
        // these were never added, so they shouldn't be found
        if (movies.contains("Dune") || movies.contains("Zodiac")) {
            return false;
        }
        // and nothing is in an empty tree
        BinarySearchTree<String> empty = new BinarySearchTree<>();
        if (empty.contains("La La Land")) {
            return false;
        }

        return true;
    }

    /**
     * Tests size and clear while building up a few different trees.
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test3() {
        // a brand new tree is empty
        BinarySearchTree<Integer> numbers = new BinarySearchTree<>();
        if (!numbers.isEmpty() || numbers.size() != 0) {
            return false;
        }

        // five values spread out on both sides
        numbers.add(5);
        numbers.add(3);
        numbers.add(8);
        numbers.add(1);
        numbers.add(9);
        if (numbers.size() != 5 || numbers.isEmpty()) {
            return false;
        }

        // clearing drops the root and takes us back to empty
        numbers.clear();
        if (!numbers.isEmpty() || numbers.size() != 0 || numbers.root != null) {
            return false;
        }

        // the same tree still works after being cleared, this time as a chain
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        if (numbers.size() != 4) {
            return false;
        }
        numbers.clear();
        if (numbers.size() != 0) {
            return false;
        }

        // size should go up by one with every String we add
        BinarySearchTree<String> words = new BinarySearchTree<>();
        words.add("delta");
        if (words.size() != 1) {
            return false;
        }
        words.add("alpha");
        if (words.size() != 2) {
            return false;
        }
        words.add("echo");
        if (words.size() != 3) {
            return false;
        }
        words.clear();
        if (!words.isEmpty() || words.size() != 0) {
            return false;
        }

        return true;
    }

    /**
     * Tests that duplicate values can be inserted and are all kept.
     *
     * @return true if the test passes, false otherwise
     */
    public boolean test4() {
        // the duplicate 50 is not equal to the root, so it goes left and then
        // down to the right of 30
        BinarySearchTree<Integer> numbers = new BinarySearchTree<>();
        numbers.add(50);
        numbers.add(30);
        numbers.add(70);
        numbers.add(50);
        if (numbers.size() != 4) {
            return false;
        }
        if (!numbers.root.downLeft().downRight().getEntry().equals(50)) {
            return false;
        }
        if (!numbers.root.toLevelOrderString().equals("[ 50, 30, 70, 50 ]")) {
            return false;
        }
        // in order still comes out sorted with the copy next to the original
        if (!numbers.root.toInOrderString().equals("[ 30, 50, 50, 70 ]")) {
            return false;
        }
        // one copy is still there after we search for it
        if (!numbers.contains(50)) {
            return false;
        }

        // adding the same value over and over gives us a chain to the left
        BinarySearchTree<Integer> repeats = new BinarySearchTree<>();
        repeats.add(7);
        repeats.add(7);
        repeats.add(7);
        if (repeats.size() != 3) {
            return false;
        }
        if (repeats.root.downRight() != null) {
            return false;
        }
        if (!repeats.root.downLeft().downLeft().getEntry().equals(7)) {
            return false;
        }
        // the copies are still linked back up to their parents
        if (repeats.root.downLeft().up() != repeats.root) {
            return false;
        }

        // duplicate Strings are kept too
        BinarySearchTree<String> pokemon = new BinarySearchTree<>();
        pokemon.add("Pikachu");
        pokemon.add("Bulbasaur");
        pokemon.add("Pikachu");
        if (pokemon.size() != 3) {
            return false;
        }
        if (!pokemon.root.downLeft().downRight().getEntry().equals("Pikachu")) {
            return false;
        }
        if (!pokemon.contains("Pikachu")) {
            return false;
        }

        return true;
    }

    /**
     * Runs all of the tests and prints whether each one passed.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        BinarySearchTree<Integer> tester = new BinarySearchTree<>();
        System.out.println("Test 1: " + tester.test1());
        System.out.println("Test 2: " + tester.test2());
        System.out.println("Test 3: " + tester.test3());
        System.out.println("Test 4: " + tester.test4());
    }
}
