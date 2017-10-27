/* *
 * Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2017.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2017 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2017
  */

package cop5556fa17;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cop5556fa17.Scanner.Token;

public class Scanner {
	
	public static int position=0;
	public static int Position_in_line=1;
	public static  int tokenlength=0;
	public static int starting_posiition=0;
	public static int linenumber=1;
	public static HashMap<String, Scanner.Kind> KW_Words= new HashMap();
	public static HashMap<String, Scanner.Kind> KW_operators= new HashMap();
	public static HashMap<Character,Scanner.Kind> KW_Separators= new HashMap();
	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {
		
		int pos;

		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}
		
		public int getPos() { return pos; }

	}

	public static enum Kind {
		IDENTIFIER, INTEGER_LITERAL, BOOLEAN_LITERAL, STRING_LITERAL, 
		KW_x/* x */, KW_X/* X */, KW_y/* y */, KW_Y/* Y */, KW_r/* r */, KW_R/* R */, KW_a/* a */, 
		KW_A/* A */, KW_Z/* Z */, KW_DEF_X/* DEF_X */, KW_DEF_Y/* DEF_Y */, KW_SCREEN/* SCREEN */, 
		KW_cart_x/* cart_x */, KW_cart_y/* cart_y */, KW_polar_a/* polar_a */, KW_polar_r/* polar_r */, 
		KW_abs/* abs */, KW_sin/* sin */, KW_cos/* cos */, KW_atan/* atan */, KW_log/* log */, 
		KW_image/* image */,  KW_int/* int */, 
		KW_boolean/* boolean */, KW_url/* url */, KW_file/* file */, OP_ASSIGN/* = */, OP_GT/* > */, OP_LT/* < */, 
		OP_EXCL/* ! */, OP_Q/* ? */, OP_COLON/* : */, OP_EQ/* == */, OP_NEQ/* != */, OP_GE/* >= */, OP_LE/* <= */, 
		OP_AND/* & */, OP_OR/* | */, OP_PLUS/* + */, OP_MINUS/* - */, OP_TIMES/* * */, OP_DIV/* / */, OP_MOD/* % */, 
		OP_POWER/* ** */, OP_AT/* @ */, OP_RARROW/* -> */, OP_LARROW/* <- */, LPAREN/* ( */, RPAREN/* ) */, 
		LSQUARE/* [ */, RSQUARE/* ] */, SEMI/* ; */, COMMA/* , */, EOF;
	}

	/** Class to represent Tokens. 
	 * 
	 * This is defined as a (non-static) inner class
	 * which means that each Token instance is associated with a specific 
	 * Scanner instance.  We use this when some token methods access the
	 * chars array in the associated Scanner.
	 * 
	 * 
	 * @author Beverly Sanders
	 *
	 */
	public class Token {
		public final Kind kind;
		public final int pos;
		public final int length;
		public final int line;
		public final int pos_in_line;

		public Token(Kind kind, int pos, int length, int line, int pos_in_line) {
			super();
			this.kind = kind;
			this.pos = pos;
			this.length = length;
			this.line = line;
			this.pos_in_line = pos_in_line;
		}

		public String getText() {
			if (kind == Kind.STRING_LITERAL) {
				return chars2String(chars, pos, length);
			}
			else return String.copyValueOf(chars, pos, length);
		}

		/**
		 * To get the text of a StringLiteral, we need to remove the
		 * enclosing " characters and convert escaped characters to
		 * the represented character.  For example the two characters \ t
		 * in the char array should be converted to a single tab character in
		 * the returned String
		 * 
		 * @param chars
		 * @param pos
		 * @param length
		 * @return
		 */
		private String chars2String(char[] chars, int pos, int length) {
			StringBuilder sb = new StringBuilder();
			for (int i = pos + 1; i < pos + length - 1; ++i) {// omit initial and final "
				char ch = chars[i];
				if (ch == '\\') { // handle escape
					i++;
					ch = chars[i];
					switch (ch) {
					case 'b':
						sb.append('\b');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r'); //for completeness, line termination chars not allowed in String literals
						break;
					case 'n':
						sb.append('\n'); //for completeness, line termination chars not allowed in String literals
						break;
					case '\"':
						sb.append('\"');
						break;
					case '\'':
						sb.append('\'');
						break;
					case '\\':
						sb.append('\\');
						break;
					default:
						assert false;
						break;
					}
				} else {
					sb.append(ch);
				}
			}
			return sb.toString();
		}

		/**
		 * precondition:  This Token is an INTEGER_LITERAL
		 * 
		 * @returns the integer value represented by the token
		 */
		public int intVal() {
			assert kind == Kind.INTEGER_LITERAL;
			return Integer.valueOf(String.copyValueOf(chars, pos, length));
		}

		public String toString() {
			return "[" + kind + "," + String.copyValueOf(chars, pos, length)  + "," + pos + "," + length + "," + line + ","
					+ pos_in_line + "]";
		}

		/** 
		 * Since we overrode equals, we need to override hashCode.
		 * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
		 * 
		 * Both the equals and hashCode method were generated by eclipse
		 * 
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((kind == null) ? 0 : kind.hashCode());
			result = prime * result + length;
			result = prime * result + line;
			result = prime * result + pos;
			result = prime * result + pos_in_line;
			return result;
		}

		/**
		 * Override equals method to return true if other object
		 * is the same class and all fields are equal.
		 * 
		 * Overriding this creates an obligation to override hashCode.
		 * 
		 * Both hashCode and equals were generated by eclipse.
		 * 
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Token other = (Token) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (kind != other.kind)
				return false;
			if (length != other.length)
				return false;
			if (line != other.line)
				return false;
			if (pos != other.pos)
				return false;
			if (pos_in_line != other.pos_in_line)
				return false;
			return true;
		}

		/**
		 * used in equals to get the Scanner object this Token is 
		 * associated with.
		 * @return
		 */
		private Scanner getOuterType() {
			return Scanner.this;
		}

	}

	/** 
	 * Extra character added to the end of the input characters to simplify the
	 * Scanner.  
	 */
	static final char EOFchar = 0;
	
	/**
	 * The list of tokens created by the scan method.
	 */
	final ArrayList<Token> tokens;
	
	/**
	 * An array of characters representing the input.  These are the characters
	 * from the input string plus and additional EOFchar at the end.
	 */
	final char[] chars;  



	
	/**
	 * position of the next token to be returned by a call to nextToken
	 */
	private int nextTokenPos = 0;

	Scanner(String inputString) {
		int numChars = inputString.length();
		this.chars = Arrays.copyOf(inputString.toCharArray(), numChars + 1); // input string terminated with null char
		chars[numChars] = EOFchar;
		tokens = new ArrayList<Token>();
	}


	/**
	 * Method to scan the input and create a list of Tokens.
	 * 
	 * If an error is encountered during scanning, throw a LexicalException.
	 * 
	 * @return
	 * @throws LexicalException
	 */
	public Scanner scan() throws LexicalException {
		position=0;
		Position_in_line=1;		
		starting_posiition=0;
		linenumber=1;
		addKeyWords();
		addOperators();
		regexCheck(); 
		return this;
	}


	/**
	 * @throws LexicalException
	 * This method checks for appropriate regex expression and calls assign the state to the character inputs.
	 */
	private void regexCheck() throws LexicalException {
		String state;
		while (position <= chars.length)
	    {
	    	
			tokenlength=0;
			state = " ";
			char ch = chars[position];
			
			if(String.valueOf(ch).matches("[A-Z|a-z|$|_]") && ch !='|')
			{
				state="IndentifierOrKeyword";
			}
			else if(String.valueOf(ch).matches("[\\n|\\r]")&& ch !='|')	
			{
				state="LineTerminator";
			}
			else if(Character.isWhitespace(ch)|| ch=='\t'||ch=='\f')
			{
				position++;
				Position_in_line++;
				continue;
			}			
			else if(String.valueOf(ch).matches("[|=|>|<|!|?|:|&|+|*|/|%|@|]")||ch=='-')
			{
				if(ch=='/' &&this.chars[position+1] =='/')
				{
					state="comment";
				}
				else
				state="operator";
			}
			else if(String.valueOf(ch).matches("[0|1-9]")&& ch !='|')	        			
			{
				state="In_Literal";
			}
			else if(String.valueOf(ch).matches("[( | ) | ; | ,]")|| ch=='['||ch==']')
			{
				state="separator";
			}
			else if(ch=='/')
			{
					state= "comment";
			}
			else if(String.valueOf(ch).matches("[\"]"))
			{
				state="StringLiteral";
			}
			else if(this.chars[position]==EOFchar)
			{
				tokens.add(new Token(Kind.EOF, position, 1, linenumber, Position_in_line));
				break;
			}
			else
			{
				throw new LexicalException("not a valid input",position);
			}
			
	        switchCases(state);   
	    }
	}


	/**
	 * @param state
	 * @throws LexicalException
	 * This function check calls the appropriate grammer  method to craete the token.
	 */
	private void switchCases(String state) throws LexicalException {
		switch (state) 
		{
		    case "IndentifierOrKeyword":checkTheFIrstCharacter();
		    							break;
		    case "LineTerminator":checkForLineTerminator();
		    					break;
		    case "In_Literal":checkForDigits();
							break;
		    case "operator":OperatorsToken();
		    				break;
		    case "separator":SeparatorsToken();
		    				break;
		    case "comment":CommentToken();
		    				break;
		    case "StringLiteral":StringToken();
		    				break;	            
		}
	}

	/**
	 * @throws LexicalException
	 * This method creates the string token.
	 */
	private void StringToken() throws LexicalException
	{
		position++;
		Position_in_line++;
		tokenlength++;
			while((this.chars[position] != '"' ))		
			{	
				if( this.chars[position]=='\\' && (this.chars[position+1]=='n'||this.chars[position+1]=='r'||(this.chars[position+1]=='f'|| this.chars[position+1]=='t' || this.chars[position+1]=='b'||this.chars[position+1]=='\''|this.chars[position+1]=='\"'||this.chars[position+1]=='\\')))
				{
					position=position+2;
					Position_in_line+=2;
					tokenlength+=2;
				}
				else if(this.chars[position]=='\\' && (!(this.chars[position+1]=='n'||this.chars[position+1]=='r'||(this.chars[position+1]=='f'|| this.chars[position+1]=='t' || this.chars[position+1]=='b'||this.chars[position+1]=='\''|this.chars[position+1]=='\"'||this.chars[position+1]=='\\'))))
				{					
				
					LexicalException  x= new  LexicalException("No closing braces",position+1); 
					throw x;
				}
				else if((this.chars[position]=='\n'||this.chars[position]=='\r'||(this.chars[position]=='\r'&& this.chars[position+1]=='\n')))
				{
				
					LexicalException  x= new  LexicalException("No closing braces",position); //// check this part
					throw x;
				}
				else if((this.chars[position] != EOFchar))
				{				
					position++;
					Position_in_line++;
					tokenlength++;				
				}
				else
				{
					LexicalException  x= new  LexicalException("No closing braces",position); 
					throw x;
				}
			}
				position++;
				Position_in_line++;
				tokenlength++;			
				tokens.add(new Token(Kind.STRING_LITERAL, position-tokenlength, tokenlength, linenumber, Position_in_line-tokenlength));
				
			}
		
			
		
	
		
	

	/**
	 * @throws LexicalException
	 * This method checks for the comment 
	 */
		private void CommentToken()
		{
			StringBuilder com= new StringBuilder();
			if(this.chars[position+1]=='/')
			{
				while(!(this.chars[position] =='\n'| this.chars[position]=='\r'| this.chars[position]== EOFchar))
				{
					com.append(String.valueOf(this.chars[position]));
					position++;
					Position_in_line++;
					tokenlength++;
				}
			}
		}

		/**
		 * @throws LexicalException
		 * This method creates the hashmap for separators and creates the separators tokens
		 */
		private void SeparatorsToken() 
		{
			KW_Separators.put('[', Kind.LSQUARE);
			KW_Separators.put(']', Kind.RSQUARE);
			KW_Separators.put('(', Kind.LPAREN);
			KW_Separators.put(')', Kind.RPAREN);
			KW_Separators.put(';', Kind.SEMI);
			KW_Separators.put(',', Kind.COMMA);
			
			if(KW_Separators.containsKey(this.chars[position]))
			{
				tokens.add(new Token(KW_Separators.get(this.chars[position]), position, 1, linenumber, Position_in_line));
				
			}
			position++;
			Position_in_line++;
		}


		/**
		 
		 * This method creates the hashmap for the operators
		 */
		private void addOperators() {
		// TODO Auto-generated method stub
			
			KW_operators.put("=", Kind.OP_ASSIGN) ;
			KW_operators.put(">", Kind.OP_GT) ;
			KW_operators.put("<", Kind.OP_LT) ;
			KW_operators.put("!", Kind.OP_EXCL) ;
			KW_operators.put("?", Kind.OP_Q) ;
			KW_operators.put(":", Kind.OP_COLON) ;
			KW_operators.put("==", Kind.OP_EQ) ;
			KW_operators.put("!=", Kind.OP_NEQ) ;			
			KW_operators.put("<=", Kind.OP_LE) ;
			KW_operators.put(">=", Kind.OP_GE) ;
			KW_operators.put("&", Kind.OP_AND) ;
			KW_operators.put("|", Kind.OP_OR) ;
			KW_operators.put("+", Kind.OP_PLUS) ;
			KW_operators.put("-", Kind.OP_MINUS) ;
			KW_operators.put("*", Kind.OP_TIMES) ;
			KW_operators.put("/", Kind.OP_DIV);
			KW_operators.put("%", Kind.OP_MOD) ;
			KW_operators.put("**", Kind.OP_POWER) ;
			KW_operators.put("->", Kind.OP_RARROW) ;
			KW_operators.put("<-", Kind.OP_LARROW) ;
			KW_operators.put("@", Kind.OP_AT) ;
			
					
	}


		/**
		 * @throws LexicalException
		 * This method creates the operators tokens
		 */
		private void OperatorsToken() 
		{
			StringBuilder op= new StringBuilder();
				op.append(this.chars[position]);				
				if( String.valueOf(this.chars[position]).matches("[=|>|<|!|*|]")||this.chars[position]=='-')
				{					
					op.append(this.chars[position+1]);
					if(KW_operators.containsKey(op.toString()))
					{
				
					
					//op.append(this.chars[position+1]);
					tokens.add(new Token(KW_operators.get(op.toString()), position, 2, linenumber, Position_in_line));
					position=position+2;
					Position_in_line=Position_in_line+2;
					
					}
					else
					{
						op.deleteCharAt(op.length()-1) ;
						if(KW_operators.containsKey(op.toString()))
						{
							//op.append(this.chars[position]);
							tokens.add(new Token(KW_operators.get(op.toString()), position, 1, linenumber, Position_in_line));
							position++;
							Position_in_line++;
						}
						
					}
				}				
				else if(KW_operators.containsKey(op.toString()))
				{
					//op.append(this.chars[position]);
					tokens.add(new Token(KW_operators.get(op.toString()), position, 1, linenumber, Position_in_line));
					position++;
					Position_in_line++;
				}
		}


		/**
		 * @throws LexicalException
		 * This method creates the digits tokens
		 */
		private void checkForDigits() throws LexicalException
		{
			if(this.chars[position]=='0')
			{
				tokens.add(new Token(Kind.INTEGER_LITERAL, position, 1, linenumber, Position_in_line));
				Position_in_line++;
				position++;
				tokenlength=0;
			}
			else
			{
				StringBuilder digittoken= new StringBuilder();
			
				while(String.valueOf(this.chars[position]).matches("[0-9]"))
				{
					digittoken.append(this.chars[position]);
					Position_in_line++;
					position++;
					tokenlength++;
				}
				try
				{
					Integer.parseInt(digittoken.toString());
					tokens.add(new Token(Kind.INTEGER_LITERAL, position-tokenlength, tokenlength, linenumber, Position_in_line-tokenlength));
					
				}
				catch(Exception e)
				{
				throw new LexicalException("Too large integer",position-tokenlength);
					
				}
			}
		
	   }

		/**
	
		 * This method creates the hashmap for the keywords
		 */
		private void addKeyWords() 
		{
		// TODO Auto-generated method stub
			KW_Words.put("x", Kind.KW_x);
			KW_Words.put("X", Kind.KW_X);
			KW_Words.put("Y", Kind.KW_Y);
			KW_Words.put("Z", Kind.KW_Z);
			KW_Words.put("y", Kind.KW_y);
			KW_Words.put("R", Kind.KW_R);
			KW_Words.put("r", Kind.KW_r);
			KW_Words.put("a", Kind.KW_a);
			KW_Words.put("A", Kind.KW_A);			
			KW_Words.put("cos", Kind.KW_cos);
			KW_Words.put("sin", Kind.KW_sin);
			KW_Words.put("log", Kind.KW_log);
			KW_Words.put("abs", Kind.KW_abs);
			KW_Words.put("log", Kind.KW_log);
			KW_Words.put("int", Kind.KW_int);
			KW_Words.put("url", Kind.KW_url);
			KW_Words.put("DEF_X", Kind.KW_DEF_X);
			KW_Words.put("DEF_Y", Kind.KW_DEF_Y);
			KW_Words.put("cart_x", Kind.KW_cart_x);
			KW_Words.put("cart_y", Kind.KW_cart_y);
			KW_Words.put("image", Kind.KW_image);
			KW_Words.put("atan", Kind.KW_atan);
			KW_Words.put("file", Kind.KW_file);
			KW_Words.put("polar_a", Kind.KW_polar_a);
			KW_Words.put("polar_r", Kind.KW_polar_r);			
			KW_Words.put("boolean", Kind.KW_boolean);
			KW_Words.put("SCREEN", Kind.KW_SCREEN);
		}


		/**
		 * @throws LexicalException
		 * This method checks line terminators and then increments the line position.
		 */
		private void checkForLineTerminator() 
		{
		// TODO Auto-generated method stub
			
			if(this.chars[position]=='\r' && this.chars[position+1]=='\n')
			{
				position++;
			}
			else
			{
				Position_in_line=1;
				linenumber++;
				position++;
			}
		
		}
		
		/**
		 * @throws LexicalException
		 * This method checks if it is identifier or not and calls the method to create identifier keywords and boolean literal
		 */
	private void checkTheFIrstCharacter() 
	{
		// TODO Auto-generated method stub
		if(String.valueOf(this.chars[position]).matches("[A-Z|a-z|$|_]"))
		{
			checkIndentifierOrKeyword();
		}
	}

	/**
	 * @throws LexicalException
	 * This method creates identifier  or boolean literals or kewords tokens
	 */
	private void checkIndentifierOrKeyword()
	{			
		StringBuilder readingtoken= new StringBuilder();
		
		while(String.valueOf(this.chars[position]).matches("[A-Z|a-z|$|_|0|1-9]"))
		{
			readingtoken.append(this.chars[position]);
			tokenlength++;
			position++;	
			Position_in_line++;
		}
		
		if(KW_Words.containsKey(readingtoken.toString()))
		{
			tokens.add(new Token(KW_Words.get(readingtoken.toString()), position-tokenlength, tokenlength, linenumber, Position_in_line-tokenlength));
		}
		else if(readingtoken.toString().equals("true")||readingtoken.toString().equals("false"))
		{
			tokens.add(new Token(Kind.BOOLEAN_LITERAL, position-tokenlength, tokenlength, linenumber, Position_in_line-tokenlength));
			
		}
		else
		{
			tokens.add(new Token(Kind.IDENTIFIER, position-tokenlength, tokenlength, linenumber, Position_in_line-tokenlength));
		}
	}
	
	/**
	 * Returns true if the internal interator has more Tokens
	 * 
	 * @return
	 */
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}

	/**
	 * Returns the next Token and updates the internal iterator so that
	 * the next call to nextToken will return the next token in the list.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * @return
	 */
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}
	
	/**
	 * Returns the next Token, but does not update the internal iterator.
	 * This means that the next call to nextToken or peek will return the
	 * same Token as returned by this methods.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * 
	 * @return next Token.
	 */
	public Token peek() {
		return tokens.get(nextTokenPos);
	}
	
	
	/**
	 * Resets the internal iterator so that the next call to peek or nextToken
	 * will return the first Token.
	 */
	public void reset() {
		nextTokenPos = 0;
	}

	/**
	 * Returns a String representation of the list of Tokens 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tokens:\n");
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append('\n');
		}
		return sb.toString();
	}


	public Token previousToken() {
		// TODO Auto-generated method stub
		Token t= tokens.get(nextTokenPos-2);
		nextTokenPos--;
		return t;
	}

}
