package trees;

import java.util.ArrayList;

public class BST extends BinaryTree {

    public BST() { super(); }

    public BST(Node root) { super(root); }

    public Node search(String data, int scopeId) {
        return search(this.root, data, scopeId);
    }

    private Node search(Node root, String data, int scopeId) {
        if (root == null) { 
            return null; 
        } else {
            Node searchNode = new Node(data, scopeId);
            int result = root.compareTo(searchNode);
            if (result == 0) { 
                return root; 
            } else if (result > 0) {
                return search(root.getLeft(), data, scopeId);
            } else {
                return search(root.getRight(), data, scopeId);
            }
        }
    }
    
    public ArrayList<Node> searchList(String data){
    	ArrayList<Node> scopes = new ArrayList<Node>();
    	
    	searchList(this.root, data, scopes);
    	
    	if(scopes.isEmpty()) {
    		throw new RuntimeException("Chave não existe no arquivo!");
    	}
    	
    	return scopes;
    }
    
    private void searchList(Node root, String data, ArrayList<Node> list){
    	if(root != null) {
    		if(root.getData().equals(data)) {
    			list.add(root);
    		}
    		searchList(root.getLeft(), data, list);
    		searchList(root.getRight(), data, list);
    	}
    }

    public void insert(String data, int scopeId) {
        this.root = insert(root, null, data, scopeId);
    }

    private Node insert(Node root, Node parent, String data, int scopeId) {
        if (root == null) { 
            root = new Node(data, scopeId, parent);
            return root;
        } else {
            Node newNode = new Node(data, scopeId);
            int result = root.compareTo(newNode);
            if (result > 0) {
                root.setLeft(insert(root.getLeft(), root, data, scopeId));
            } else if (result < 0) {
                root.setRight(insert(root.getRight(), root, data, scopeId));
            } else {
                throw new RuntimeException("Já existe um nó com essa chave");
            }
        }
        return root;
    }

    public void remove(String data, int scopeId) {
        if (search(data, scopeId) == null) {
            throw new RuntimeException("Não existe um nó com essa chave");
        } else {
            this.root = remove(root.getParent(), root, data, scopeId);
        }
    }

    private Node remove(Node parent, Node current, String data, int scopeId) {
        if (current == null) {
            return null;
        } else {
            Node removeNode = new Node(data, scopeId);
            int result = current.compareTo(removeNode);
            if (result > 0) {
                current.setLeft(remove(current, current.getLeft(), data, scopeId));
            } else if (result < 0) {
                current.setRight(remove(current, current.getRight(), data, scopeId));
            } else {
                if (current.isLeaf()) {
                    current.setParent(null);
                    current = null;
                } else if (current.getLeft() == null) {
                    Node removed = current;
                    current = removed.getRight();
                    current.setParent(removed.getParent());
                    removed.setData(null);
                    removed.setParent(null);
                    removed.setLeft(null);
                    removed.setRight(null);
                } else if (current.getRight() == null) {
                    Node removed = current;
                    current = current.getLeft();
                    current.setParent(removed.getParent());
                    removed.setData(null);
                    removed.setParent(null);
                    removed.setLeft(null);
                    removed.setRight(null);
                } else {
                    Node predecessor = findPredecessor(data, scopeId);
                    current.setData(predecessor.getData());
                    current.setScopeId(predecessor.getScopeId());
                    current.setLeft(remove(current, current.getLeft(), predecessor.getData(), predecessor.getScopeId()));
                }
            }
        }
        return current;
    }

    public Node findPredecessor(String data, int scopeId) {
        Node node = search(data, scopeId);
        if (node == null) {
            return null;
        } else { return findPredecessor(node); }
    }

    private Node findPredecessor(Node node) {
        if (node.getLeft() != null) {
            node = node.getLeft();
            while (node.getRight() != null) {
                node = node.getRight();
            }
            return node;
        } else {
            Node current = node;
            Node parent = node.getParent();
            while (parent != null && current == parent.getLeft()) {
                current = parent;
                parent = current.getParent();
            }
            return parent;
        }
    }

    public Node findSuccessor(String data, int scopeId) {
        Node node = search(data, scopeId);
        if (node == null) {
            return null;
        } else { return findSuccessor(node); }
    }

    private Node findSuccessor(Node node) {
        if (node.getRight() != null) {
            node = node.getRight();
            while (node.getLeft() != null) {
                node = node.getLeft();
            }
            return node;
        } else {
            Node current = node;
            Node parent = node.getParent();
            while (parent != null && current == parent.getRight()) {
                current = parent;
                parent = current.getParent();
            }
            return parent;
        }
    }

    public Node findMin() {
        if (this.isEmpty()) {
            return null;
        } else {
            return findMin(root);
        }
    }

    private Node findMin(Node root) {
        while (root.getLeft() != null) {
            root = root.getLeft();
        }
        return root;
    }

    public Node findMax() {
        if (this.isEmpty()) {
            return null;
        } else {
            return findMax(root);
        }
    }

    private Node findMax(Node root) {
        while (root.getRight() != null) {
            root = root.getRight();
        }
        return root;
    }

    public void clear() {
        this.root = clear(root);
    }

    private Node clear(Node node) {
        if (node != null) {
            node.setLeft(clear(node.getLeft()));
            node.setRight(clear(node.getRight()));
            return null;
        }
        return null;
    }
}
