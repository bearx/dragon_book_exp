package lexer;

import java.io.EOFException;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {

	public static int line = 1;
	char peek = ' ';
	Hashtable words = new Hashtable();

	void reserve(Word w) {
		words.put(w.lexeme, w);
	}

	public Lexer() {
		reserve(new Word("if", Tag.IF));
		reserve(new Word("else", Tag.ELSE));
		reserve(new Word("while", Tag.WHILE));
		reserve(new Word("do", Tag.DO));
		reserve(new Word("break", Tag.BREAK));
		reserve(new Word("void",Tag.VOID));
		reserve(new Word("int",Tag.INT));
		reserve(new Word("double",Tag.DOUBLE));
		reserve(new Word("bool",Tag.BOOL));
		reserve(new Word("string",Tag.STRING));
		reserve(new Word("class",Tag.CLASS));
		reserve(new Word("null",Tag.NULL));
		reserve(new Word("this",Tag.THIS));
		reserve(new Word("extends",Tag.EXTENDS));
		reserve(new Word("for",Tag.FOR));
		reserve(new Word("return",Tag.RETURN));
		reserve(new Word("NewArray",Tag.NEWARRAY));
		reserve(new Word("Print",Tag.PRINT));
		reserve(new Word("ReadInteger",Tag.READINTEGER));
		reserve(new Word("ReadLine",Tag.READLINE));
		reserve(new Word("static",Tag.STATIC));
		reserve(Word.True);
		reserve(Word.False);
	}

	public void readch() throws IOException {
		peek = (char) System.in.read();
		
	}

	boolean readch(char c) throws IOException {
		readch();
		if (peek != c) {
			return false;
		}
		peek = ' ';
		return true;
	}

	public Token scan() throws IOException {
		for (;; readch()) {
			if (peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n') {
				line += 1;
			} else {
				break;
			}
		}
		if (peek=='\uFFFF') throw new EOFException();
		switch (peek) {
		case '&':
			if (readch('&'))
				return Word.and;
			else
				return new Token('&');
		case '|':
			if (readch('|'))
				return Word.or;
			else
				return new Token('|');
		case '=':
			if (readch('='))
				return Word.eq;
			else
				return new Token('=');
		case '!':
			if (readch('='))
				return Word.ne;
			else
				return new Token('!');
		case '<':
			if (readch('='))
				return Word.le;
			else
				return new Token('<');
		case '>':
			if (readch('='))
				return Word.ge;
			else
				return new Token('>');
		}
		if (peek=='@'||peek=='#'||peek=='$')
		{
			char tmp=peek;
			peek=' ';
			throw new IOException(tmp+"is invalid");
		}
		if (Character.isDigit(peek)) {
			int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));
			if (peek != '.')
				return new Num(v);
			float x = v;
			float d = 10;
			for (;;) {
				readch();
				if (!Character.isDigit(peek))
					break;
				x = x + Character.digit(peek, 10) / d;
				d = d * 10;
			}
			return new Real(x);
		}
		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();
			Word w = (Word) words.get(s);
			if (w != null)
				return w;
			w = new Word(s, Tag.ID);
			words.put(s, w);
			return w;
		}
		Token tok = new Token(peek);
		peek = ' ';
		return tok;
	}
	
	public void out() {
		System.out.println(words.size());
		
	}

	public char getPeek() {
		return peek;
	}

	public void setPeek(char peek) {
		this.peek = peek;
	}

}
