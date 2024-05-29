/*	Apl2
 * 
 * Por: Joaquim Rafael Mariano Prieto Pereira  RA: 10408805 
 * Lucas Trebacchetti Eiras RA: 10401973
 * Henrique Árabe Neres de Farias RA: 10410152
 * Antonio Carlos Sciamarelli Neto  RA: 10409160
 * 
 * Estruturas de Dados II Professor Andre Kishimoto Sala 04G12
 * 
 * Consulta em: 
 * https://www.mballem.com/post/manipulando-arquivo-txt-com-java/
 * https://youtu.be/Gt2yBZAhsGM?si=WNOSZxaiCWmrA-sO
 * https://www.geeksforgeeks.org/binary-tree-data-structure/
 * https://www.ime.usp.br/~pf/mac0122-2003/aulas/bin-trees.html
 * https://www.javatpoint.com/binary-search-tree
 * PROGRAMIZ. AVL Tree. Disponível em: https://www.programiz.com/dsa/avl-tree.
 * GALLES, D. AVL Tree Visualization. Disponível em: https://www.cs.usfca.edu/~galles/visualization/AVLtree.html.
 * https://www.devmedia.com.br/java-arquivos-e-fluxos-de-dados/22859
 * 
 *
 *  e materias de sala:
 *  Árvore AVL (André Kishimoto)
 *  Revisão POO com Java (André Kishimoto)
 *  Herança em Java (André Kishimoto)
 *  Árvores - fundamentos (André Kishimoto)
 */

package trees;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class BST extends BinaryTree implements Cloneable {

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
    		return(null);
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
                root.setValue(value);
            }
        }
        return root;
    }

    public void remove(String data, int scopeId, String type) {
        if (search(data, scopeId, type) == null) {
            throw new RuntimeException("Não existe um nó com essa chave: " + data + " " + scopeId + " " + type);
        } else {
            this.root = remove(root, data, scopeId, type);
        }
    }

    private Node remove(Node current, String data, int scopeId, String type) {
        if (current == null) {
            return null;
        }

        Node removeNode = new Node(data, scopeId, type);
        int result = current.compareTo(removeNode);

        if (result > 0) {
            current.setLeft(remove(current.getLeft(), data, scopeId, type));
        } else if (result < 0) {
            current.setRight(remove(current.getRight(), data, scopeId, type));
        } else {
            // Nó encontrado
            if (current.isLeaf()) {
                return null; // Remover o nó folha
            } else if (current.getLeft() == null) {
                Node temp = current.getRight();
                current.setParent(null);
                return temp;
            } else if (current.getRight() == null) {
                Node temp = current.getLeft();
                current.setParent(null);
                return temp;
            } else {
                // Nó com dois filhos
                Node successor = findMin(current.getRight());
                current.setData(successor.getData());
                current.setScopeId(successor.getScopeId());
                current.setType(successor.getType());
                current.setPath(successor.getPath());
                current.setValue(successor.getValue());
                current.setRight(remove(current.getRight(), successor.getData(), successor.getScopeId(), successor.getType()));
            }
        }
        return current;
    }

    private Node findMin(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
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
    
    
    @Override
    public BST clone() {
        BST clone = new BST();
        clone.root = cloneRecursive(this.root);
        return clone;
    }

    public Node cloneRecursive(Node root) {
        if (root == null) {
            return null;
        }

        Node newNode = new Node(root.getData(), root.getScopeId(), root.getParent(), root.getType(), root.getValue(), root.getPath());
        newNode.setLeft(cloneRecursive(root.getLeft()));
        newNode.setRight(cloneRecursive(root.getRight()));

        return newNode;
    }
}
