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

public class Node {
	
	private int data;
	private Node parent;
	private Node left;
	private Node right;
	private int balanceFactor;
	
	public Node() {
		this.data = 0;
		this.parent = null;
		this.left = null;
		this.right = null;
		this.balanceFactor = 0;
	}
	
	public Node(int data) {
		this.data = data;
		this.parent = null;
		this.left = null;
		this.right = null;
		this.balanceFactor = 0;
	}
	
	public Node(int data, Node parent) {
		this.data = data;
		this.parent = parent;
		this.left = null;
		this.right = null;
		this.balanceFactor = 0;
	
	}
	
	public Node(int data, Node left, Node right, Node parent) {
		this.data = data;
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.balanceFactor = 0;
	}

	public int getData() { return data; }

	public void setData(int data) { this.data = data; }

	public Node getParent() { return parent; }

	public void setParent(Node parent) { this.parent = parent; }

	public Node getLeft() { return left; }

	public void setLeft(Node left) { 
		this.left = left; 
		if (this.left != null) {// atualiza a referencia do no filho automaticamente
			this.left.setParent(this);
		}
		this.updateBalanceFactor();
	
	}

	public Node getRight() { return right; }

	public void setRight(Node right) { 
		this.right = right;
		if (this.right != null) {// atualiza a referencia do no filho automaticamente
			this.right.setParent(this);
		}
		this.updateBalanceFactor();
		}
	
	public int getBalanceFactor() {
		updateBalanceFactor(); 
		return balanceFactor; // retorna o fb atualizado
		}

	private void updateBalanceFactor() { // calculo do fb a partir das alturas das sub-árvores
	    int leftHeight = (this.left != null) ? this.left.getHeight() : -1;
	    int rightHeight = (this.right != null) ? this.right.getHeight() : -1;
	    this.balanceFactor = rightHeight - leftHeight; 
	}

	public boolean isRoot() {//se não tiver parente, é uma raiz
		return(this.parent == null);
	}
	
	public boolean isLeaf() {//se não possuir filhos, então é uma folha
		return(this.left == null && this.right == null);
	}
	
	public int getDegree() {// calcula o numero de filhos do nó
		int count = 0;
		
		if(this.left != null) {
			count++;
		}
		if(this.right != null) {
			count++;
		}
		
		return count;
	}
	
	public int getLevel() { return getLevel(this); }
	
	private int getLevel(Node node) {//percorre recursivamente do nó ate a raiz da arvore para saber o nivel
		if(node.parent == null) {
			return 0;
		}else {
		
			return 1 + getLevel(node.parent);
		}
	}
	
	public int getHeight() { return getHeight(this); }
	
	private int getHeight(Node node) { //percorre recursivamente ate encontrar o nó folha do maior caminho e então soma 1
		if(node == null) {
			return -1;
		}else {
			
			int leftHeight = getHeight(node.left);
			int rightHeight = getHeight(node.right);
			
			if(leftHeight > rightHeight) { return 1 + leftHeight;}
			else { return 1 + rightHeight; }
		}
		
	}
	@Override
	public String toString() {
		return "node: " + data
				+ ", parent: " + (parent != null ? parent.getData() : "null")
				+ ", left: " + (left != null ? left.getData() : "null")
				+ ", right: " + (right != null ? right.getData() : "null")
				+ ", Balance Factor: " + balanceFactor;
	}
	
}
