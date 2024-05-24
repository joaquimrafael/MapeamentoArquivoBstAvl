package trees;

import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BST extends BinaryTree {

    public BST() { super(); }

    public BST(Node root) { super(root); }

    public Node search(String data, int scopeId, String type) {
        return search(this.root, data, scopeId, type);
    }

    private Node search(Node root, String data, int scopeId, String type) {
        if (root == null) { 
            return null; 
        } else {
            Node searchNode = new Node(data, scopeId, type);
            int result = root.compareTo(searchNode);
            if (result == 0) { 
                return root; 
            } else if (result > 0) {
                return search(root.getLeft(), data, scopeId, type);
            } else {
                return search(root.getRight(), data, scopeId, type);
            }
        }
    }
    
    public Map<Node, Integer> searchList(String data){
    	Map<Node, Integer> nodesCount = new HashMap<>();
    	
    	searchList(this.root, data, nodesCount, 0);
    	if(nodesCount.isEmpty()) {
    		throw new RuntimeException("Chave não existe no arquivo!");
    	}
    	
    	return nodesCount;
    }
    
    private void searchList(Node root, String data, Map<Node, Integer> nodesCount, int comparisons){
    	if(root != null) {
    		comparisons++;
    		if(root.getData().equals(data)) {
    			nodesCount.put(root, comparisons);
    		}
    		searchList(root.getLeft(), data, nodesCount, comparisons);
    		searchList(root.getRight(), data, nodesCount, comparisons);
    	}
    }

    public void insert(String data, int scopeId, String type, String value, Stack<Integer> path) {
        this.root = insert(root, null, data, scopeId, type, value, path);
    }

    private Node insert(Node root, Node parent, String data, int scopeId, String type, String value, Stack<Integer> path) {
        if (root == null) { 
            root = new Node(data, scopeId, parent, type, value, path);
            return root;
        } else {
            Node newNode = new Node(data, scopeId, type, value, path);
            int result = root.compareTo(newNode);
            if (result > 0) {
                root.setLeft(insert(root.getLeft(), root, data, scopeId, type, value, path));
            } else if (result < 0) {
                root.setRight(insert(root.getRight(), root, data, scopeId, type, value, path));
            } else {
                throw new RuntimeException("Já existe um nó com essa chave");
            }
        }
        return root;
    }

    public void remove(String data, int scopeId, String type) {
        if (search(data, scopeId, type) == null) {
            throw new RuntimeException("Não existe um nó com essa chave");
        } else {
            this.root = remove(root.getParent(), root, data, scopeId, type);
        }
    }

    private Node remove(Node parent, Node current, String data, int scopeId, String type) {
        if (current == null) {
            return null;
        } else {
            Node removeNode = new Node(data, scopeId, type);
            int result = current.compareTo(removeNode);
            if (result > 0) {
                current.setLeft(remove(current, current.getLeft(), data, scopeId, type));
            } else if (result < 0) {
                current.setRight(remove(current, current.getRight(), data, scopeId, type));
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
                    Node predecessor = findPredecessor(data, scopeId, type);
                    current.setData(predecessor.getData());
                    current.setScopeId(predecessor.getScopeId());
                    current.setLeft(remove(current, current.getLeft(), predecessor.getData(), predecessor.getScopeId(), type));
                }
            }
        }
        return current;
    }

    public Node findPredecessor(String data, int scopeId, String type) {
        Node node = search(data, scopeId, type);
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

    public Node findSuccessor(String data, int scopeId, String type) {
        Node node = search(data, scopeId, type);
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
