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
		this.archiveName = archiveName;
		this.opened = false;
		this.archive = new ArrayList<String>();
		try {
			this.archiveName = this.validate(this.archiveName);
		}catch(RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Boolean getOpened() {
		return(this.opened);
	}
	
	private String validate(String archiveName){
		if(!archiveName.contains(".ed2") && !archiveName.contains(".ED2")){
			return(archiveName+".ed2.txt");
		}else {
			return(archiveName+".txt");
		}
	}
	
	public List<String> readArchive() throws IOException{
        File file = new File(this.archiveName);

        if (!file.exists()) {
            throw new IOException("! Arquivo .ed2 não encontrado!");
        }
        
		BufferedReader buffRead = new BufferedReader(new FileReader(this.archiveName));
		String line = "";
	    while ((line = buffRead.readLine()) != null) {
	        this.archive.add(line);
	    }
		buffRead.close();
		this.opened = true;
		return(this.archive);
	}
	
	public void saveArchive(BST tree, Map<Integer,String> scopesMap, String archiveName) throws IOException {
		String name = this.validate(archiveName);
		File file = new File(name);
		if(name.contains(" ")) {
			throw new IOException("! Arquivo não pode conter espaços no nome!");
		}
		
		if(file.exists()) {
			System.out.println(">Você deseja sobreescrever o arquivo já existente no diretório? Digite 1 para Sim e 0 para Não");
			@SuppressWarnings("resource")
			Scanner entry = new Scanner(System.in);
			int chose = entry.nextInt();
			if(chose !=1) {
				System.out.println("! Operação cancelada!");
				entry.close();
				return;
			}
		}
		
		
		BST treeCopy = tree;
		List<String> archive = new ArrayList<String>();
		
		writeScope(treeCopy,scopesMap,0,archive);
		
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(name));
		for(int i=0;i<archive.size();i++) {
			buffWrite.append(archive.get(i) + "\n");
		}
		buffWrite.close();
	}
	
	private void writeScope(BST tree, Map<Integer, String> scopesMap, int currentScopeId, List<String> archive) {
        if (currentScopeId != 0) {
            archive.add(scopesMap.get(currentScopeId) + "(");
        }
        Node result;
        while ((result = searchKeys(tree.getRoot(), currentScopeId, tree)) != null) {
            archive.add(result.getData() + "=" + result.getValue());
        }

        while ((result = searchScopes(tree.getRoot(), currentScopeId, tree)) != null) {
            writeScope(tree, scopesMap, result.getScopeId(), archive);
        }

        if (currentScopeId != 0) {
            archive.add(")");
        }
    }

    private Node searchKeys(Node root, int scopeId, BST tree) {
        if (root == null) {
            return null;
        }
        if (root.getScopeId() == scopeId && root.getType().equals("key")) {
            Node aux = new Node(root.getData(), root.getScopeId(), root.getParent(), root.getType(), root.getValue(), root.getPath());
            tree.remove(root.getData(), root.getScopeId(), "key");
            return aux;
        }
        Node result = searchKeys(root.getRight(), scopeId, tree);
        if (result != null) {
            return result;
        }
        return searchKeys(root.getLeft(), scopeId, tree);
    }

    private Node searchScopes(Node root, int scopeId, BST tree) {
        if (root == null) {
            return null;
        }
        if (root.getType().equals("scope")) {
            Stack<Integer> pathCopy = copyStack(root.path);
            int currentScopeId = pathCopy.pop();
            if (pathCopy.peek() == scopeId) {
                Stack<Integer> newPath = copyStack(root.getPath());
                Node aux = new Node(root.getData(), root.getScopeId(), root.getParent(), root.getType(), root.getValue(), newPath);
                tree.remove(root.getData(), currentScopeId, "scope");
                return aux;
            }
        }
        Node result = searchScopes(root.getRight(), scopeId, tree);
        if (result != null) {
            return result;
        }
        return searchScopes(root.getLeft(), scopeId, tree);
    }
	
	@SuppressWarnings("unchecked")
	public static Stack<Integer> copyStack(Stack<Integer> original) {
	    return (Stack<Integer>) original.clone();
	}
	
	@SuppressWarnings("unused")
	private BST copyTree(BST tree) {
		return (BST) tree.clone();
	}
}
