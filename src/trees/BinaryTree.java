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

import java.util.Queue;
import java.util.LinkedList;

public class BinaryTree {
	
	protected Node root;
	
	public BinaryTree() {
		this(null);
	}
	
	public BinaryTree(Node root) {
		this.root = root;
	}

	public Node getRoot() { return root; }

	public void setRoot(Node root) { this.root = root; }
	
	public boolean isEmpty() { return(this.root == null); } // raiz nula -> arvore vazia
	
	public int getDegree() { return getDegree(root); }
	
	public int getDegree(Node root) { // compara recursivamente cada nó para descobrir o de maior grau
			
		if(root == null) {
			return 0;
		}else {
			
			int currentCount = root.getDegree();
			
			int leftDegree = getDegree(root.getLeft());
			int rightDegree = getDegree(root.getRight());
			
			return Math.max(currentCount, Math.max(leftDegree, rightDegree));
			
		}
		
	}
	
	public int getHeight() { return this.root.getHeight(); } // devolve a altura da raiz (altura da arvore)
	
	public void inOrder() { inOrder(root); }
	
	private void inOrder(Node root) {// esquerda, raiz e direita
		if(root != null) {
			inOrder(root.getLeft());
			System.out.println(root);
			inOrder(root.getRight());
		}
	}
	
	public void preOrder() { preOrder(root); }
	
	private void preOrder(Node root) {// raiz, esquerda e direita
		if(root != null) {
			System.out.println(root);
			preOrder(root.getLeft());
			preOrder(root.getRight());
		}

	}
	
	public void postOrder() { postOrder(root); }
	
	private void postOrder(Node root) {//esquerda, direita e raiz
		if(root != null) {
			postOrder(root.getLeft());
			postOrder(root.getRight());
			System.out.println(root);
		}
	}
	
	public void levelOrder() { levelOrder(root); }
	
	private void levelOrder(Node root) {//utiliza uma aplicação da pesquisa em largura para imprimir os nós da arvore em nível
		//basicamente cria uma fila que define a prioridade que os nós devem ser impressos a medida que são adicionados
		if(root != null) {
		
			Queue<Node> fila = new LinkedList<>();
			fila.add(root);
			
			while(!fila.isEmpty()) {
				Node current = fila.poll();
				System.out.println(current);
				
				if(current.getLeft() != null) {
					fila.add(current.getLeft());
				}
				if(current.getRight() != null) {
					fila.add(current.getRight());
				}
			}
		}
	}

}
