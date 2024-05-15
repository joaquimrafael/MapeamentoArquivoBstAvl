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

package parser;

import java.util.List;

//================================================================================
// GRAMÁTICA
// Observe que parte da gramática é processada/avaliada na classe Tokenizer e
// parte é processada/avaliada na classe Parser (<code>, <print> e <sum>).
//================================================================================
// <code>         ::= ((<print> | <sum>)* <blank_line>)*
// <print>        ::= ">" <whitespace>* <string>
// <sum>          ::= "+" <whitespace>* <uint> (<whitespace>+ <uint>)*
// <string>       ::= <char>+
// <char>         ::= <basic_latin> | <latin_1_supp> | <whitespace>
// <basic_latin>  ::= [\u0020-\u007F]  ; Unicode Basic Latin
// <latin_1_supp> ::= [\u00A0-\u00FF]  ; Unicode Latin-1 Supplement
// <uint>         ::= <digit>+
// <digit>        ::= [0-9]
// <blank_line>   ::= <whitespace>* <newline>
// <whitespace>   ::= " " | "\t"
// <newline>      ::= "\n" | "\r" | "\r\n"

public class Parser {

	private List<Token> tokens;
	private Token currToken;
	private int index;

	public Parser() {
		tokens = null;
		currToken = null;
		index = -1;
	}
	
	public void run(List<String> contents) {
		Tokenizer tokenizer = new Tokenizer();
		tokens = tokenizer.tokenize(contents);
		currToken = null;
		index = -1;

		// Descomente o código abaixo para ver a lista de tokens gerada pelo Tokenizer.
//		System.out.println("==================== TOKENS ====================");
		for (var token : tokens) {
			System.out.println(token);
		}
//		System.out.println("==================== TOKENS ====================");
		
		parse();
	}
	
	// O parser sempre começa avançando para o primeiro token da lista e, na sequência,
	// avalia a regra <code> (a regra mais geral da gramática).
	// Após processar <code>, o último token da lista deve ser o EOF, indicando que
	// todo o conteúdo foi processado corretamente.
	private void parse() {
		advance();
		code();		
		if (currToken.getType() != TokenType.EOF) {
			throw new RuntimeException("Parser.parse(): Esperado fim do conteúdo (EOF), mas encontrou " + currToken);
		}
	}
	
	// <code> ::= ((<print> | <sum>)* <blank_line>)*
	private void code() {
		TokenType type = currToken.getType();

		// Consome 0+ regras do tipo <print> e/ou <sum> seguida por <blank_line>.
		while (type == TokenType.PRINT || type == TokenType.SUM || type == TokenType.NEWLINE) {
			if (type == TokenType.PRINT) {
				print();
			} else if (type == TokenType.SUM) {
				sum();
			}
			
			// Após processar <print> ou <sum>, consome um <blank_line>.
			consume(TokenType.NEWLINE);
			
			// Neste exemplo, processamos a regra <blank_line> com uma quebra de linha na saída em tela.
			System.out.println();			
			
			type = currToken.getType();
		}
	}
	
	// <print> ::= ">" <whitespace>* <string>
	private void print() {
		// Consome ">".
		consume(TokenType.PRINT);
		
		// Consome 0+ espaços em branco (<whitespace>*).
		while (currToken.getType() == TokenType.WHITESPACE) {
			consume(TokenType.WHITESPACE);
		}
		
		// Neste ponto, o esperado é ter um token STRING.
		// Salvamos o valor do token para exibir em tela (caso o conteúdo esteja bem formado). 
		String str = currToken.getValue();
		
		// Consome <string>.
		consume(TokenType.STRING);
		
		// Se chegamos aqui, significa que o conteúdo seguiu a gramática e podemos processar a regra <print>
		// (que significa exibir a string em tela).
		System.out.print(str);
	}
	
	// <sum> ::= "+" <whitespace>* <uint> (<whitespace>+ <uint>)*
	private void sum() {
		// Consome "+".
		consume(TokenType.SUM);
		
		// Consome 0+ espaços em branco (<whitespace>*).
		while (currToken.getType() == TokenType.WHITESPACE) {
			consume(TokenType.WHITESPACE);
		}
		
		// Neste ponto, o esperado é ter um token UINT.
		// Salvamos o valor do token para realizar a somatória (caso o conteúdo esteja bem formado).
		int sum = getIntFromToken();		
		
		// Consome <uint>.
		consume(TokenType.UINT);

		// Opcionalmente, podemos ter 0+ números separados por um espaço em branco ((<whitespace> <uint>)*),
		// até chegar ao final da linha atual.
		while (currToken.getType() != TokenType.NEWLINE) {
			// Consome o <whitespace> obrigatório (<whitespace>+).
			consume(TokenType.WHITESPACE);

			// Consome espaços em branco extras, se tiver.
			while (currToken.getType() == TokenType.WHITESPACE) {
				consume(TokenType.WHITESPACE);
			}

			// Realizamos a somatória dos números.
			sum += getIntFromToken();
			
			// Consome <uint>.
			consume(TokenType.UINT);
		}
		
		// Se chegamos aqui, significa que o conteúdo seguiu a gramática, temos a somatória dos números e podemos
		// processar a regra <sum> (neste exemplo, exibimos o resultado da somatória em tela).
		System.out.print(sum);
	}
	
	
	
	private int getIntFromToken() {
		try {
			return Integer.parseInt(currToken.getValue());
		} catch (Exception e) {
			throw new RuntimeException("Parser.getIntFromToken(): Não foi possível converter o valor do token ('" + currToken.getValue() + "') em um número inteiro.");
		}
	}

	// Avança para o próximo token da lista (atualiza currToken).
	private void advance() {
		++index;
		if (index >= tokens.size()) {
			throw new RuntimeException("Fim de conteúdo inesperado.");
		}
		currToken = tokens.get(index);
	}
	
	// Consome um token esperado.
	// "Consumir um token" significa que o token atual é do tipo esperado por uma regra da gramática.
	// Caso seja o token esperado, avança para o próximo token da lista (atualiza currToken).
	// Caso contrário, lança uma exceção de token incorreto.
	private void consume(TokenType expected) {
		if (currToken.getType() == expected) {
			advance();
		} else {
			throw new RuntimeException("Parser.consume(): Token incorreto. Esperado: " + expected + ". Obtido: " + currToken);
		}
	}

}
