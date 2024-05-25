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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;

import trees.*;


public class Archive {
	private List<String> archive;
	private String archiveName = null;
	private Boolean opened;
	
	public Archive(String archiveName) {
		this.archiveName = archiveName + ".txt";
		this.opened = false;
		this.archive = new ArrayList<String>();
		try {
			this.validate();
		}catch(RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Boolean getOpened() {
		return(this.opened);
	}
	
	private void validate(){
		if(!this.archiveName.contains(".ed2") && !this.archiveName.contains(".ED2")) {
			throw new RuntimeException("Formato inválido! O arquivo precisa conter a extensão .ed2 ou .ED2");
		}
	}
	
	public List<String> readArchive() throws IOException{
		BufferedReader buffRead = new BufferedReader(new FileReader(this.archiveName));
		String line = "";
	    while ((line = buffRead.readLine()) != null) {
	        this.archive.add(line);
	    }
		buffRead.close();
		this.opened = true;
		return(this.archive);
	}
	
	public void saveArchive(AvlTree tree, Map<Integer,String> scopesMap) throws IOException {
		String name;
		System.out.println("Digite o nome do arquivo .ed2:");
		name = "out";
		name = name + ".ed2.txt";
		File file = new File(name);
		//Se arquivo existir escrever nele
		if(name.contains(" ")) {
			throw new IOException("Arquivo não pode conter espaços no nome!");
		}
		
		if(file.exists()) {
			//throw new IOException("Arquivo já existente no diretório");
		}
		
		
		AvlTree treeCopy = tree;
		List<String> archive = new ArrayList<String>();
		
		writeScope(treeCopy,scopesMap,0,archive);
		
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(name));
		for(int i=0;i<archive.size();i++) {
			System.out.println(archive.get(i));
			buffWrite.append(archive.get(i) + "\n");
		}
		buffWrite.close();
	}
	
	private void writeScope(AvlTree tree, Map<Integer,String> scopesMap, int currentScopeId, List<String> archive) {
		if(currentScopeId != 0) {
			archive.add(scopesMap.get(currentScopeId)+"(");
		}
		System.out.println(currentScopeId);
		Node result = searchKeys(tree.getRoot(),currentScopeId,tree);
		while(result!=null) {
			archive.add(result.getData()+"="+result.getValue());
			result = searchKeys(tree.getRoot(),currentScopeId,tree);
		}
		
		result = searchScopes(tree.getRoot(),currentScopeId,tree);
		while(result!=null) {
			writeScope(tree, scopesMap, result.getPath().peek(), archive);
			result = searchScopes(tree.getRoot(),currentScopeId,tree);
		}
		if(currentScopeId != 0) {
			archive.add(")");
		}
	}
	
	private Node searchKeys(Node root, int scopeId, AvlTree tree) {
	    if (root == null) {
	        return null;
	    }
	    if (root.getScopeId() == scopeId && root.getType()=="key") {
	        Node aux = new Node(root.getData(), root.getScopeId(), root.getType(), root.getValue(), root.getPath());
	        tree.remove(root.getData(), root.getScopeId(), "key");
	        return aux;
	    }
	    Node result = searchKeys(root.getRight(), scopeId, tree);
	    if (result != null) {
	        return result;
	    }
	    return searchKeys(root.getLeft(), scopeId, tree);
	}
	
	private Node searchScopes(Node root, int scopeId, AvlTree tree) {
	    if (root == null) {
	        return null;
	    }
	    if(root.getType()=="scope") {
	    	Stack<Integer> pathCopy = copyStack(root.path);
	    	int currentScopeId = pathCopy.pop();
	    	if (pathCopy.peek() == scopeId) {
	    		Stack<Integer> newPath = copyStack(root.getPath());
	    		Node aux = new Node(root.getData(), root.getScopeId(), root.getType(), root.getValue(), newPath);
		        tree.remove(root.getData(), currentScopeId, "scope");
		        return aux;
		    }
	    }
	    Node result = searchKeys(root.getRight(), scopeId, tree);
	    if (result != null) {
	        return result;
	    }
	    return searchKeys(root.getLeft(), scopeId, tree);
	}
	
	@SuppressWarnings("unchecked")
	private Stack<Integer> copyStack(Stack<Integer> original) {
	    return (Stack<Integer>) original.clone();
	}
}
