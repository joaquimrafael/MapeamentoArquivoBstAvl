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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import trees.AvlTree;
import trees.BST;
import parser.Token;
import parser.Tokenizer;
import parser.Parser;

public class Main {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		String option;
		boolean carregado = false;
		AvlTree avl = new AvlTree();
		BST bst = new BST();
		
		bst.insert("a",2);
		bst.insert("a",3);
		bst.insert("a",4);
		bst.insert("a",1);
		
		//bst.inOrder();
		
		avl.insert("a",2);
		avl.insert("a",3);
		avl.insert("a",4);
		avl.insert("a",1);
		
		Archive archive = new Archive("teste.ed2");
		List<String> contents;
		try {
			contents = archive.readArchive();
		} catch (IOException e) {
			input.close();
			throw new RuntimeException(e.getMessage());
		}
		
		Parser parser = new Parser();
		parser.run(contents);
		/*
		while(true) {
			System.out.println("\nMapeamento Arquivos Arvores Bst-Avl\r\n"
					+ "1. Carregar dados de um arquivo ED2\r\n"
					+ "2. Buscar uma chave/escopo na arvore\r\n"
					+ "3. Inserir uma chave/escopo na arvore\r\n"
					+ "4. Alterar uma chave da arvore\r\n"
					+ "5. Remover uma chave da arvore\r\n"
					+ "6. Salvar dados para um arquivo\r\n"
					+ "7. Exibir o conteudo e as propriedades da arvore BST\r\n"
					+ "8. Exibir o conteudo e as propriedades da arvore AVL\r\n"
					+ "9. Encerrar o programa");
			System.out.println("Digite sua escolha");
			option = input.nextLine();
			
			switch (option) {
				case "1":
					carregado = true;
					break;
				case "2":
					if(carregado) {
						
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "3":
					if(carregado) {
						
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "4":
					if(carregado) {
						
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "5":
					if(carregado) {
						
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "6":
					if(carregado) {
						
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "7":
					if(carregado) {
						bst.treeInformation();
						bst.preOrder();
						bst.inOrder();
						bst.postOrder();
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "8":
					if(carregado) {
						avl.treeInformation();
						avl.preOrder();
						avl.inOrder();
						avl.postOrder();
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "9":
					System.out.println("Saindo...");
					input.close();
					return;
				default:
					System.out.println("Digite uma opção válida");
					break;
			}
				
		}
		*/

	}

}
