public class AvlTree<E extends Comparable<E>> {

    private static final int ALLOWED_IMBALANCE = 1;
    public AvlNode<E> root = null;
    public int size = 0;

    public static class AvlNode<E> {

        E element; // Data in the node
        AvlNode<E> left; // Left subtree of the node
        AvlNode<E> right; // Right subtree of the node
        int height; // Height of the node

        int totalTrucks;

        public AvlNode(E element) {
            this(element, null, null, 0);
        }

        public AvlNode(E element, AvlNode<E> left, AvlNode<E> right, int totalTrucks) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.totalTrucks = totalTrucks;
        }

        private static <T> void inOrderTraversal(AvlNode<T> node) {
            if (node == null)
                return ;
            inOrderTraversal(node.left);
            System.out.print(node.element + " ");
            inOrderTraversal(node.right);
        }

        public int getTotalOfLeftSubtree() {
            if (left != null) {
                return left.totalTrucks;
            } else {
                return 0;
            }
        }

        public int getTotalOfRightSubTree() {
            if (right != null) {
                return right.totalTrucks;
            } else {
                return 0;
            }
        }
    }

    public AvlTree() {}
    public AvlTree(E element) {
        this.insert(element);
    }

    public AvlTree(E[] elementList) {
        for (E element : elementList)
            this.insert(element);
    }

    private static <T> int height(AvlNode<T> node) {
        if (node == null)
            return -1;
        else
            return node.height;
    }

    private static <T> int getHeightDifference(AvlNode<T> node) {
        return Math.abs(height(node.right) - height(node.left));
    }


    // We again print height differences in order to compare with values
    public static <T> void printingHeightDifferencesRecursively(AvlNode<T> node) {
        if (node == null)
            return ;

        printingHeightDifferencesRecursively(node.left);
        System.out.print(getHeightDifference(node) + " ");
        printingHeightDifferencesRecursively(node.right);
    }

    public static <T> void printInOrder(AvlNode<T> startingNode) {
        AvlNode.inOrderTraversal(startingNode);
    }

    // Driver method to add an element easily to the tree
    public void insert(E item) {
        root = insert(item, root);
    }

    // Rost means root of the subtree
    private AvlNode<E> insert(E item, AvlNode<E> rost) {

        if (rost == null) {
            size += 1;
            return new AvlNode<>(item, null, null, 0);
        }

        int compareResult = item.compareTo(rost.element);

        // Item to be inserted is smaller than the current tree element so go left
        if (compareResult < 0) {
            rost.left = insert(item, rost.left);

        } else if (compareResult > 0) {
            rost.right = insert(item, rost.right);

        } else { // else do nothing because it is a duplicate just return the same subtree
            return rost;
        }
        // totalTrucks is correct for the subtree whose root is rost before balancing
        return balance(rost);
    }

    // In order to maintain the type parameter E in the function, avoid declaring it
    // using <E> in the method signature otherwise the general E will be shadowed
    private AvlNode<E> balance(AvlNode<E> rost) {
        if (rost == null)
            return rost;

        if (height(rost.left) - height(rost.right) > ALLOWED_IMBALANCE)
            // Single rotation
            if (height(rost.left.left) >= height(rost.left.right)) {
                rost = rotateWithLeftChild(rost);
            } else { // Double Rotation
                rost = doubleWithLeftChild(rost);
            }
        else if (height(rost.right) - height(rost.left) > ALLOWED_IMBALANCE)
            // Single rotation
            if (height(rost.right.right) >= height(rost.right.left)) {
                rost = rotateWithRightChild(rost);
            } else { // Double Rotation
                rost = doubleWithRightChild(rost);
            }

        rost.height = Math.max(height(rost.left), height(rost.right)) + 1;
        return rost;
    }

    // Single rotation from the left, or in clockwise direction
    // k1 is the new root and all heights of the subtree, in which k1 is the new root is, is updated
    private AvlNode<E> rotateWithLeftChild(AvlNode<E> k2) {
        AvlNode<E> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;

        // Rearranging the totalTrucks
        k2.totalTrucks -= (k1.totalTrucks); // independent of null
        k1.totalTrucks += (k2.totalTrucks); // independent of null

        if (k2.left != null) {
            k2.totalTrucks += k2.left.totalTrucks; // k1.right.left might be null
        }
        return k1;
    }

    // Single rotation from the right, or in counter-clockwise direction
    // k1 is the new root and all heights of the subtree, in which k1 is the new root is, is updated
    private AvlNode<E> rotateWithRightChild(AvlNode<E> k2) {
        AvlNode<E> k1 = k2.right;
        k2.right = k1.left;
        k1.left = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(k2.height, height(k1.right)) + 1;

        // Rearranging the totalTrucks
        k2.totalTrucks -= (k1.totalTrucks); // independent of null
        k1.totalTrucks += (k2.totalTrucks); // independent of null

        if (k2.right != null) {
            k2.totalTrucks += k2.right.totalTrucks; // k1.right.left might be null
        }
        return k1;
    }

    // Double rotation to the left if there is an imbalance at rost.left.right
    // This is literally double rotation
    private AvlNode<E> doubleWithLeftChild(AvlNode<E> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    // Double rotation to the left if there is an imbalance at rost.right.left
    private AvlNode<E> doubleWithRightChild(AvlNode<E> k3) {
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }

    // Driver method to remove an element from the tree
    public void remove(E item) {
        root = remove(item, root);
        size -= 1;
    }

    // Until found this func searches for the item and while searching it does recursive calls in each step to balance
    // every level in the path after deleting the item, it uses balance(rost) at every step to do that
    private AvlNode<E> remove(E item, AvlNode<E> rost) {
        if (rost == null) {
            size += 1;
            return rost;
        }

        int compareResult = item.compareTo(rost.element);

        if (compareResult > 0)
            rost.right = remove(item, rost.right);
        else if (compareResult < 0)
            rost.left = remove(item, rost.left);
        else if (rost.left != null && rost.right != null) {
            rost.element = findMin(rost.right).element;
            // It is for sure that this second call of remove will not enter this else if statement to call
            // another remove because it is the smallest item in that subtree, which means it has no left child there
            // The comment above is for the code line below
            rost.right = remove(rost.element, rost.right);
        }
        else {
            if (rost.left != null)
                rost = rost.left;
            else
                rost = rost.right;
        }

        return balance(rost);
    }

    private AvlNode<E> findMin(AvlNode<E> rost) {
        if (rost.left == null) {
            return rost;
        }

        return findMin(rost.left);
    }


}
