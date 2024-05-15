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

//
// Exemplo de tokenizer (lexer) e parser.
// Copyright (C) 2024 André Kishimoto
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
//

import java.util.ArrayList;
import java.util.List;

import parser.Parser;

public class Program {
	
	public static void testParser(final String[] testData) {
		List<String> contents = new ArrayList<String>();
		for (var s : testData) {
			contents.add(s);
		}

		Parser parser = new Parser();
		try {
			parser.run(contents);
		} catch(RuntimeException e) {
			System.out.println();
			System.out.println("*** ERRO! O conteúdo inserido não está bem formado (não está de acordo com as regras gramaticais):");
			System.out.println("  > " + e.getMessage());
			System.out.println();
		}
	}

	public static void main(String[] args) {
		final String[] grammar = {
			"> <code>         ::= ((<print> | <sum>)* <blank_line>)*",
			"> <print>        ::= \">\" <whitespace>* <string>",
			"> <sum>          ::= \"+\" <whitespace>* <uint> (<whitespace> <uint>)*",
			"> <string>       ::= <char>+",
			"> <char>         ::= <basic_latin> | <latin_1_supp> | <whitespace>",
			"> <basic_latin>  ::= [\\u0020-\\u007F]  ; Unicode Basic Latin",
			"> <latin_1_supp> ::= [\\u00A0-\\u00FF]  ; Unicode Latin-1 Supplement",
			"> <uint>         ::= <digit>+",
			"> <digit>        ::= [0-9]",
			"> <blank_line>   ::= <whitespace>* <newline>",
			"> <whitespace>   ::= \" \" | \"\\t\"",
			"> <newline>      ::= \"\\n\" | \"\\r\" | \"\\r\\n\"",
		};

		final String[] testOk = {
			">>>>>>>>>>>>>>>>>",
			"> Hello, World!",
			">++++++++++++++++",
			"> A linha a seguir exibe o resultado da somatória (+1 2 3 4 5):",
			"+1 2 3 4 5",
			">                    E outra somatória abaixo (+ 99 88 77 66 55 44 33 22 11):",
			"+ 99 88 77 66 55 44 33 22 11",
			"",
			"> A próxima linha contém apenas espaços em branco, mas consideramos como linha vazia.",
			"                ",
			">Legal, não? :)",
		};
		
		final String[][] testErrors = {
			{
				"> Exemplo de conteúdo com problemas: a próxima linha não condiz com nenhuma regra da gramática (não começa com '>' nem '+').",
				">Hello, World!",
				"Hello, World!",
			},
			{
				"> Outro exemplo de conteúdo com problemas (string no lugar de número):",
				">+ teste",
				"+ teste",
			},
			{
				"> Outro exemplo de conteúdo com problemas (gramática só permite números inteiros):",
				">+ 1 2.2",
				"+ 1 2.2",
			},
			{
				"> Outro exemplo de conteúdo com problemas ('+' sem nenhum número inteiro em seguida):",
				">+",
				"+",
			},
			{
				"> Outro exemplo de conteúdo com problemas ('>' seguido de espaços em branco, apenas):",
				">>                ",
				">                ",
			},
			{
				"> Outro exemplo de conteúdo com problemas ('>' somente):",
				">>",
				">",
			}
		};
				

		System.out.println("============================[ GRAMÁTICA ]=============================");
		testParser(grammar);
		
		System.out.println();
		System.out.println("====================[ PARSER COM CONTEÚDO VÁLIDO ]====================");
		testParser(testOk);

		System.out.println();
		System.out.println("====================[ PARSER COM CONTEÚDO VÁLIDO ]====================");
		testParser(new String[] {  }); // A gramática aceita um conteúdo vazio.

		System.out.println();
		System.out.println("====================[ PARSER COM CONTEÚDO VÁLIDO ]====================");
		testParser(new String[] { "" }); // A gramática aceita um conteúco com uma única linha vazia.

		for (var testError : testErrors) {
			System.out.println();
			System.out.println("====================[ PARSER COM CONTEÚDO INVÁLIDO ]====================");
			testParser(testError);
		}
	}
	
}

