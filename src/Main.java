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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.HashMap;

import trees.AvlTree;
import trees.BST;
import trees.Node;
import parser.Parser;

public class Main {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		String option;
		boolean carregado = false;
		AvlTree avl = new AvlTree();
		BST bst = new BST();
		Map<Integer,String> scopesMap = null;
		Map<Node,Integer> nodesCount = null;
		Parser parser = null;
		
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
					System.out.println("Informe o nome do arquivo a ser carregado: ");
					String archiveName = input.nextLine();
					Archive archive = new Archive(archiveName);
					List<String> contents;
					try {
						contents = archive.readArchive();
					} catch (IOException e) {
						input.close();
						throw new RuntimeException(e.getMessage());
					}
					
					parser = new Parser();
					//Carregando dados do parser
					parser.run(contents);
					avl = parser.getAVL();
					bst = parser.getBST();
					scopesMap = parser.getScopesMap();
					
					System.out.println("Arquivo carregado em memória com sucesso!");
					carregado = true;
					break;
				case "2":
					if(carregado) {
						System.out.println("Digite o identificador (String) a ser buscado no arquivo:");
						String search = input.nextLine();
						nodesCount = avl.searchList(search);
						
						if(nodesCount.size()>0) {
							for (Map.Entry<Node, Integer> entry : nodesCount.entrySet()) {
								Node node = entry.getKey();
				            	Integer comparisons = entry.getValue();
				            	System.out.println("Node: " + node + " | Comparisons: " + comparisons +" | Parent Scope: " + scopesMap.get(node.getScopeId()));
							}
						}else {
							System.out.println("Chave/escopo não existe no arquivo!");
						}
					}else {
						System.out.println("Carregue dados primeiro!");
					}
					break;
				case "3":
					if(carregado) {
						System.out.println("Digite 1 para adicionar um escopo e 2 para adiionar uma chave:");
						String choice = input.nextLine();
						if(choice.equals("1")) {
							System.out.println("Escopos disponíveis para inserção do escopo:");
							
							Map<Node,Integer> scopes = new HashMap<Node,Integer>();
							
							for (Map.Entry<Integer, String> entry : scopesMap.entrySet()) {
								if(entry.getValue()!="global") {
									scopes.putAll(avl.searchList(entry.getValue()));
								}
							}
							System.out.println("Nome: global | scopeId: 0 | Parent Scope: null");
							for (Map.Entry<Node, Integer> entry : scopes.entrySet()) {
								if(entry.getKey().getType()=="scope") {
									System.out.println("Nome: "+entry.getKey().getData()+" | scopeId: "+entry.getKey().getScopeId()+" | Parent Scope: " + scopesMap.get(entry.getKey().getScopeId()));
								}
							}
							System.out.println("Digite o nome do escopo a ser inserido:");
							String newScopeName = input.nextLine();
							System.out.println("Digite o scopeId onde o novo escopo será inserido:");
							String scopeIdString = input.nextLine();
							int scopeId = Integer.parseInt(scopeIdString);
							if(!scopesMap.containsKey(scopeId)) {
								System.out.println("O scopeId não existe atualmente, impossível inserir um novo escopo!");
								continue;
							}
							Stack<Integer> newPath;
							if(scopeId!=0) {
								Node parent = avl.search(scopesMap.get(scopeId), scopeId, "scope");
								newPath = parent.getPath();
							}else {
								newPath = new Stack<Integer>();
								newPath.add(0);
							}
							if(avl.search(newScopeName, scopeId, "scope")==null) {
								parser.countScopeId++;
								scopesMap.put(parser.countScopeId, newScopeName);
								newPath.add(parser.countScopeId);
								avl.insert(newScopeName, scopeId, "scope", "", newPath);
								bst.insert(newScopeName, scopeId, "scope", "", newPath);
								avl.inOrder();
								System.out.println("Inserção realizada com sucesso!");
							}else {
								System.out.println("Chave já existente!");
							}
						}else if(choice.equals("2")) {
							System.out.println("Escopos disponíveis para inserção da chave:");
							
							Map<Node,Integer> scopes =  new HashMap<Node,Integer>();
							
							for (Map.Entry<Integer, String> entry : scopesMap.entrySet()) {
								if(entry.getValue()!="global") {
									scopes.putAll(avl.searchList(entry.getValue()));
								}
							}
							
							System.out.println("Nome: global | scopeId: 0 | Parent Scope: null");
							for (Map.Entry<Node, Integer> entry : scopes.entrySet()) {
								if(entry.getKey().getType()=="scope") {
									System.out.println("Nome: "+entry.getKey().getData()+" | scopeId: "+entry.getKey().getScopeId()+" | Parent Scope: " + scopesMap.get(entry.getKey().getScopeId()));
								}
							}
							
							System.out.println("Digite o nome da chave a ser inserida:");
							String newScopeName = input.nextLine();
							System.out.println("Digite o valor da chave a ser inserida:");
							String value = input.nextLine();

							System.out.println("Digite o scopeId onde o novo escopo será inserido:");
							String scopeIdString = input.nextLine();
							int scopeId = Integer.parseInt(scopeIdString);
							if(!scopesMap.containsKey(scopeId)) {
								System.out.println("O scopeId não existe atualmente, impossível inserir um nova chave!");
								continue;
							}
							Stack<Integer> newPath;
							if(scopeId!=0) {
								Node parent = avl.search(scopesMap.get(scopeId), scopeId, "scope");
								newPath = parent.getPath();
							}else {
								newPath = new Stack<Integer>();
								newPath.add(0);
							}
							if(avl.search(newScopeName, scopeId, "scope")==null) {
								avl.insert(newScopeName, scopeId, "key", value, newPath);
								bst.insert(newScopeName, scopeId, "key", value, newPath);
								System.out.println("Inserção realizada com sucesso!");
							}else {
								System.out.println("Chave já existente!");
							}
						}else {
							System.out.println("Opção inválida!");
						}
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

	}

}
