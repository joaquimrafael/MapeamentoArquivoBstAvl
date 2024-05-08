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

import java.util.ArrayList;
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

public class Tokenizer {
	
	private int pos;
	private String line = null;

	public Tokenizer() {
		pos = 0;
	}
	
	// A partir da lista de strings de entrada (contents), constrói e retorna uma lista de Tokens
	// que será processada pelo parser.
	public List<Token> tokenize(List<String> contents) {
		List<Token> tokens = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int lineIndex = 0;
		char currChar = '\0';
		boolean isString = false;
		
		while (true) {
			// Pode avançar para a próxima linha?
			if (line == null || pos >= line.length()) {
				// Se o StringBuilder sb não está vazio neste ponto, significa que estávamos
				// montando uma string quando o cursor pos atingiu o final da linha.
				// Neste caso, adicionamos a string montada até o momento como um novo token.
				if (sb.length() > 0) {
					tokens.add(new Token(TokenType.STRING, sb.toString()));
					sb.setLength(0);
				}
				
				// Adiciona um token NEWLINE após a primeira linha, indicando quebra de linha.
				// (line == null neste momento significa que ainda vamos ler a primeira linha).
				if (line != null) {
					tokens.add(new Token(TokenType.NEWLINE, "\n"));
				}
				
				// Acabaram as linhas?
				if (lineIndex >= contents.size()) {
					// Adiciona um token EOF, indicando fim do conteúdo, e encerra o loop.
					tokens.add(new Token(TokenType.EOF, "\0"));
					break;
				}
				
				// Lê a próxima linha.
				line = contents.get(lineIndex++);
				
				// Reinicia variáveis a cada nova linha.
				pos = 0;
				currChar = '\0';
				isString = false;
				
				// Linha vazia ou somente espaços em branco?
				// Muda posição do cursor para o final da linha e encerra a iteração antecipadamente.
				// É importante mudar pos, para que na próxima iteração do loop o código entre nesta
				// condicional e avance para a próxima linha.
				if (line.isBlank()) {
					pos = line.length();
					continue;
				}				
			}
			
			if (!isString) {
				currChar = getNextChar();
				
				if (Character.isWhitespace(currChar)) { // Reconhece um token WHITESPACE.
					// Considera uma sequência de espaços em branco como um único espaço em branco.
					while (Character.isWhitespace(currChar)) {
						currChar = getNextChar();
					}
					tokens.add(new Token(TokenType.WHITESPACE, " "));

					// Se passamos por uma sequência de espaços em branco, voltamos uma posição do cursor
					// somente se o último caractere não for um espaço em branco, para que a instrução
					// currChar = getNextChar(); na próxima iteração do loop obtenha o caractere correto.
					// Isso deve ser feito porque no loop de sequência de espaços em branco acima,
					// sempre avançamos para o próximo caractere.
					if (pos <= line.length() && !Character.isWhitespace(line.charAt(pos - 1))) {
						--pos;
					}
				
				} else if (Character.isDigit(currChar)) { // Reconhece um token UINT.
					sb.setLength(0);
					while (Character.isDigit(currChar)) {
						sb.append(currChar);
						currChar = getNextChar();
					}
					// Armazena o valor do UINT como string (será convertido para número pelo parser).
					tokens.add(new Token(TokenType.UINT, sb.toString()));
					sb.setLength(0);

					// Se passamos por uma sequência de dígitos que compõem um número e não atingimos o
					// final da linha, voltamos uma posição do cursor, para que a instrução
					// currChar = getNextChar(); na próxima iteração do loop obtenha o caractere correto.
					// Isso deve ser feito porque no loop de sequência de dígitos acima, sempre avançamos
					// para o próximo caractere.
					if (pos < line.length()) {
						--pos;
					}

				} else if (currChar == '>') { // Reconhece um token PRINT.
					// Se o token anterior é um PRINT, então começa uma string (permite que uma string
					// comece com o caractere '>').
					if (tokens.size() > 0 && tokens.get(tokens.size() - 1).getType() == TokenType.PRINT) {
						isString = true;
						startStringWith(sb, currChar);
					} else {
						tokens.add(new Token(TokenType.PRINT, ">"));
					}
					
				} else if (currChar == '+') { // Reconhece um token SUM.
					// Se o token anterior é um PRINT, então começa uma string (permite que uma string
					// comece com o caractere '+').
					if (tokens.size() > 0 && tokens.get(tokens.size() - 1).getType() == TokenType.PRINT) {
						isString = true;
						startStringWith(sb, currChar);
					} else {
						tokens.add(new Token(TokenType.SUM, "+"));
					}
				
				} else if (currChar != '\0') { // Provavelmente encontramos uma string.
					// Adiciona o caractere atual como primeiro da string e ativa flag isString para que,
					// a partir da próxima iteração, os próximos caracteres sejam adicionados à string.
					isString = true;
					startStringWith(sb, currChar);
				}

			} else { // Reconhece um token STRING.
				// Neste exemplo, a única condição para indicar que chegamos ao final de uma string é
				// ler todo o conteúdo da linha atual até o final da linha.
				while (pos < line.length()) {
					currChar = getNextChar();
					sb.append(currChar);
				}
				tokens.add(new Token(TokenType.STRING, sb.toString()));
				sb.setLength(0);
				isString = false;
			}
		}
	
		return tokens;
	}
	
	// Avança para o próximo caractere da linha e retorna seu valor.
	// Ou retorna o caractere nulo '\0' se chegou no final da linha.
	private char getNextChar() {
		if (pos >= line.length()) {
			return '\0';
		}
		return line.charAt(pos++);
	}
	
	private void startStringWith(StringBuilder sb, char ch) {
		sb.setLength(0);
		sb.append(ch);
	}

}