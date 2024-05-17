package trees;

public class AvlTree extends BST {
    public AvlTree() {
        super();
    }

    public AvlTree(Node root) {
        super(root);
    }

    @Override
    public void insert(String data, int scopeId) {
        super.insert(data, scopeId);
        Node aux = search(data, scopeId);

        while (aux != null) { // verifica o balanceamento de cada nó ancestral do adicionado
            if (aux.getBalanceFactor() >= -1 && aux.getBalanceFactor() <= 1) {
                aux = aux.getParent();
                continue;
            } else if (aux.getBalanceFactor() > 1) {
                if (aux.getRight() != null && aux.getRight().getBalanceFactor() < 0) {
                    aux = rotateRightLeft(aux); // arvore LR
                } else {
                    aux = rotateLeft(aux); // arvore RR
                }
            } else {
                if (aux.getLeft() != null && aux.getLeft().getBalanceFactor() > 0) {
                    aux = rotateLeftRight(aux); // arvore RL
                } else {
                    aux = rotateRight(aux); // arvore LL
                }
            }

            if (aux.getParent() == null) { // substitui as referencias da arvore
                root = aux;
            } else if (aux == aux.getParent().getLeft()) {
                aux.getParent().setLeft(aux);
            } else {
                aux.getParent().setRight(aux);
            }
        }
    }

    @Override
    public void remove(String data, int scopeId) {
        Node aux = search(data, scopeId);
        aux = aux.getParent();

        super.remove(data, scopeId);

        if (aux == null) {
            aux = root; // caso tenha removido a raiz, testa o balanceamento da nova raiz
        }
        while (aux != null) {
            if (aux.getBalanceFactor() >= -1 && aux.getBalanceFactor() <= 1) {
                aux = aux.getParent();
                continue;
            } else if (aux.getBalanceFactor() > 1) {
                if (aux.getRight() != null && aux.getRight().getBalanceFactor() < 0) {
                    aux = rotateRightLeft(aux);
                } else {
                    aux = rotateLeft(aux);
                }
            } else {
                if (aux.getLeft() != null && aux.getLeft().getBalanceFactor() > 0) {
                    aux = rotateLeftRight(aux);
                } else {
                    aux = rotateRight(aux);
                }
            }

            if (aux.getParent() == null) {
                root = aux;
            } else if (aux == aux.getParent().getLeft()) {
                aux.getParent().setLeft(aux);
            } else {
                aux.getParent().setRight(aux);
            }
        }
    }

    private Node rotateLeft(Node root) {
        if (root == null || root.getRight() == null) {
            return root;
        }
        Node newRoot = root.getRight();
        root.setRight(newRoot.getLeft());
        newRoot.setParent(root.getParent());
        newRoot.setLeft(root);

        if (newRoot.getParent() != null) {
            if (root == newRoot.getParent().getLeft()) {
                newRoot.getParent().setLeft(newRoot);
            } else if (root == newRoot.getParent().getRight()) {
                newRoot.getParent().setRight(newRoot);
            }
        }

        return newRoot;
    }

    private Node rotateRight(Node root) {
        if (root == null || root.getLeft() == null) {
            return root;
        }
        Node newRoot = root.getLeft();
        root.setLeft(newRoot.getRight());
        newRoot.setParent(root.getParent());
        newRoot.setRight(root);

        if (newRoot.getParent() != null) {
            if (root == newRoot.getParent().getLeft()) {
                newRoot.getParent().setLeft(newRoot);
            } else if (root == newRoot.getParent().getRight()) {
                newRoot.getParent().setRight(newRoot);
            }
        }

        return newRoot;
    }

    private Node rotateLeftRight(Node root) {
        if (root == null || root.getLeft() == null) {
            return root;
        }
        root.setLeft(rotateLeft(root.getLeft()));
        return rotateRight(root);
    }

    private Node rotateRightLeft(Node root) {
        if (root == null || root.getRight() == null) {
            return root;
        }
        root.setRight(rotateRight(root.getRight()));
        return rotateLeft(root);
    }
}
