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


public class BST extends BinaryTree {
	
	public BST() { super(); }
	
	public BST(Node root) { super(root); }
	
	public Node search(String key) {
		return search(this.root,key);
	}
	
	private Node search(Node root, String data) {
		if(root == null) { 
			return null; 
		}else {
			int result = data.compareTo(root.getData());
			if(result == 0) { 
				return root; 
			}else if (result < 0) {
				return search(root.getLeft(),data);
			}else {
				return search(root.getRight(),data);
			}
		}
	}
	
	
	public void insert(String data) {
		this.root = insert(root, null, data);
	}
	
	private Node insert(Node root, Node parent, String data) {
		
		if(root == null) { 
			root = new Node(data,parent);
			return root;
		}else {
			int result = data.compareTo(root.getData());
			if (result < 0) {
				 root.setLeft(insert(root.getLeft(),root,data));
			}else if (result > 0) {
				root.setRight(insert(root.getRight(),root,data));
			}else {
				throw new RuntimeException("Já existe um nó com essa chave");
			}
		}
		return root;
		
	}
	
	public void remove(String data) {
		if(search(data) == null) {
			throw new RuntimeException("Não existe um nó com essa chave");
		}else {
		this.root = remove(root.getParent(),root, data);
		}
	}
	
	private Node remove(Node parent, Node current, String data) {
	    if (current == null) {
	        return null;
	    } else {
	        int result = data.compareTo(current.getData());
	        if (result < 0) {
	            current.setLeft(remove(current, current.getLeft(), data));
	        } else if (result > 0) {
	            current.setRight(remove(current, current.getRight(), data));
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
	                // dois filhos
	                Node predecessor = findPredecessor(current.getData());
	                current.setData(predecessor.getData());
	                current.setLeft(remove(current, current.getLeft(), predecessor.getData()));
	            }
	        }
	    }

	    return current;
	}
	
	
	public Node findPredecessor(String data) {
		Node node = search(data);
		if(node == null) {
			return null;
		}else { return findPredecessor(node); }
	}
	
	private Node findPredecessor(Node node) {
		
		if(node.getLeft() != null) {
			node = node.getLeft();
			while(node.getRight() != null) {
				node = node.getRight();
			}
			return node;
		}else {
			Node current = node;
			Node parent = node.getParent();

			while (parent != null && current == parent.getLeft()) {
				current = parent;
				parent = current.getParent();
			}
			
			return parent;
		}
	}
	
	public Node findSucessor(String data) {
		Node node = search(data);
		if(node == null) {
			return null;
		}else { return findSucessor(node); }
	}
	
	private Node findSucessor(Node node) {
		if(node.getRight() != null) {
			node = node.getRight();
			while(node.getLeft() != null) {
				node = node.getLeft();
			}
			return node;
		}else {
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
		if(this.isEmpty()) {
			return null;
		}else {
			return findMin(root);
		}
	}
	
	private Node findMin(Node root) {
		while(root.getLeft() != null) {
			root = root.getLeft();
		}
		
		return root;
	}
	
	public Node findMax() {
		if(this.isEmpty()) {
			return null;
		}else {
			return findMax(root);
		}
	}
	
	private Node findMax(Node root) {
		while(root.getRight() != null) {
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
