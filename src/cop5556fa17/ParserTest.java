package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.AST.*;

import cop5556fa17.Parser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class ParserTest {

	// set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}

	/**
	 * Simple test case with an empty program. This test expects an exception
	 * because all legal programs must have at least an identifier
	 * 
	 * @throws LexicalException
	 * @throws SyntaxException
	 */
	@Test
	public void testEmpty() throws LexicalException, SyntaxException {
		String input = ""; // The input is the empty string. Parsing should fail
		show(input); // Display the input
		Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
														// initialize it
		show(scanner); // Display the tokens
		Parser parser = new Parser(scanner); //Create a parser
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast = parser.parse();; //Parse the program, which should throw an exception
		} catch (SyntaxException e) {
			show(e);  //catch the exception and show it
			throw e;  //rethrow for Junit
		}
	}
	
	@Test
	public void testNameOnlyExp() throws LexicalException, SyntaxException {
		String input = "statement1 | statement2";  //Legal program with only a name
		show(input);            //display input
		Scanner scanner = new Scanner(input).scan();   //Create scanner and create token list
		show(scanner);    //display the tokens
		Parser parser = new Parser(scanner);   //create parser
		Expression ast = parser.expression();          //parse program and get AST
		show(ast);                             //Display the AST
//		assertEquals(ast.name, "prog");        //Check the name field in the Program object
//		assertTrue(ast.decsAndStatements.isEmpty());   //Check the decsAndStatements list in the Program object.  It should be empty.
	}

	@Test
	public void testNameOnly() throws LexicalException, SyntaxException {
		String input = "prog";  //Legal program with only a name
		show(input);            //display input
		Scanner scanner = new Scanner(input).scan();   //Create scanner and create token list
		show(scanner);    //display the tokens
		Parser parser = new Parser(scanner);   //create parser
		Program ast = parser.parse();          //parse program and get AST
		show(ast);                             //Display the AST
		assertEquals(ast.name, "prog");        //Check the name field in the Program object
		assertTrue(ast.decsAndStatements.isEmpty());   //Check the decsAndStatements list in the Program object.  It should be empty.
	}

	@Test
	public void testDec1() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "prog"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("k", dec.name);
		assertNull(dec.e);
	}
	
	@Test
	public void testVarDec2() throws LexicalException, SyntaxException {
		String input = "prog int k = ( num1 > num2 ) ? num3 : num4;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "prog"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("k", dec.name);
		//assertNull(dec.e);
	}
	
	@Test
	public void testImageDec1() throws LexicalException, SyntaxException {
		String input = "prog image nikita;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testSourceSinkDec1() throws LexicalException, SyntaxException {
		String input = "prog url nikita = \"saxena\";";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}

	@Test
	public void testImageOutStmnt1() throws LexicalException, SyntaxException {
		String input = "prog nikita -> SCREEN;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testImageInStmnt1() throws LexicalException, SyntaxException {
		String input = "prog nikita <- @num1 + num2;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testAssgnStmnt1() throws LexicalException, SyntaxException {
		String input = "prog nikita[[x,y]] = num1 + num2;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testAssgnStmnt2() throws LexicalException, SyntaxException {
		String input = "prog nikita = num1 + num2;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	
	@Test
	public void testExp1() throws LexicalException, SyntaxException {
		String input = "a: b: c";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		ASTNode expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(), "Expression_PredefinedName [name=KW_a]"); 
	}
	
	@Test
	public void testExp2() throws LexicalException, SyntaxException {
		String input = "num1 + num2";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst.firstToken);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Ident [name=num1], op=OP_PLUS, e1=Expression_Ident [name=num2]]");
	}
	
	@Test
	public void testExp3() throws LexicalException, SyntaxException {
		String input = "a = (c > d) ? e : f";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(), "Expression_PredefinedName [name=KW_a]");
	}
	
	@Test
	public void testProRandom1() throws LexicalException, SyntaxException {
		String input = "lo int j = sin [x+y,a+b] ;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=lo, decsAndStatements=[Declaration_Variable [type=[KW_int,int,3,3,1,4], name=j, e=Expression_FunctionAppWithIndexArg [function=KW_sin, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]]]]");
	}
	
	@Test
	public void testExp4() throws LexicalException, SyntaxException {
		String input = "p-+q";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Ident [name=p], op=OP_MINUS, e1=Expression_Unary [op=OP_PLUS, e=Expression_Ident [name=q]]]");
	}
	
	@Test
	public void testProRandom2() throws LexicalException, SyntaxException {
		String input = "hello boolean i = cos [x+y,a+b] ; hal = 5+--++++9 ; aks <- \"aka\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=hello, decsAndStatements=[Declaration_Variable [type=[KW_boolean,boolean,6,7,1,7], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Statement_Assign [lhs=name [name=hal, index=null], e=Expression_Binary [e0=Expression_IntLit [value=5], op=OP_PLUS, e1=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_IntLit [value=9]]]]]]]]], Statement_In [name=aks, source=Source_StringLiteral [fileOrUrl=aka]]]]");
	}
	
	@Test
	public void testProRandom3() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN;_abc <- _abc;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_Ident [name=_abc]]]]");
		}
	
	@Test
	public void testProRandom4() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _abc <- \"_abcnik\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_StringLiteral [fileOrUrl=_abcnik]]]]");
	}
	
	@Test
	public void testProRandom5() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _abc <- @ true;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_CommandLineParam [paramNum=Expression_BooleanLit [value=true]]]]]");
	}
	
	@Test
	public void testProRandom6() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _abc <- @ false;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_CommandLineParam [paramNum=Expression_BooleanLit [value=false]]]]]");
	}
	
	@Test
	public void testProRandom7() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _pqr [[x,y]] = a;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_Assign [lhs=name [name=_pqr, index=Index [e0=Expression_PredefinedName [name=KW_x], e1=Expression_PredefinedName [name=KW_y]]], e=Expression_PredefinedName [name=KW_a]]]]");
	}
	
	@Test
	public void testProRandom8() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _pqr [[x,y]] = a;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_Assign [lhs=name [name=_pqr, index=Index [e0=Expression_PredefinedName [name=KW_x], e1=Expression_PredefinedName [name=KW_y]]], e=Expression_PredefinedName [name=KW_a]]]]");
	}
	
	@Test
	public void testProRandom9() throws LexicalException, SyntaxException {
		String input = "prog k <- \"Nikita Saxena\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=prog, decsAndStatements=[Statement_In [name=k, source=Source_StringLiteral [fileOrUrl=Nikita Saxena]]]]");
	}
	
	@Test
	public void testProRandom10() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]]]]");
	}
	
	@Test
	public void testExp5() throws LexicalException, SyntaxException {
		String input = "a+b < d-c";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]], op=OP_LT, e1=Expression_Binary [e0=Expression_Ident [name=d], op=OP_MINUS, e1=Expression_Ident [name=c]]]");
	}
	
	@Test
	public void testExp6() throws LexicalException, SyntaxException {
		String input = "when?a?b?c:d:e:f";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Conditional [condition=Expression_Ident [name=when], trueExpression=Expression_Conditional [condition=Expression_PredefinedName [name=KW_a], trueExpression=Expression_Conditional [condition=Expression_Ident [name=b], trueExpression=Expression_Ident [name=c], falseExpression=Expression_Ident [name=d]], falseExpression=Expression_Ident [name=e]], falseExpression=Expression_Ident [name=f]]");
	}
	
	@Test
	public void testExp7() throws LexicalException, SyntaxException {
		String input = "cos(a+b)]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_FunctionAppWithExprArg [function=KW_cos, arg=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]");
	}
	
	@Test
	public void testExp8() throws LexicalException, SyntaxException {
		String input = "+-!DEF_X?+-!DEF_Y:nikita [(a*b),c*d]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Conditional [condition=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_EXCL, e=Expression_PredefinedName [name=KW_DEF_X]]]], trueExpression=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_EXCL, e=Expression_PredefinedName [name=KW_DEF_Y]]]], falseExpression=Expression_PixelSelector [name=nikita, index=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_TIMES, e1=Expression_Ident [name=b]], e1=Expression_Binary [e0=Expression_Ident [name=c], op=OP_TIMES, e1=Expression_Ident [name=d]]]]]");
	}
	
	@Test
	public void testExpRan() throws LexicalException, SyntaxException {
		String input = "+x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y & t+s | f+g ? +x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y & t+s | f+g:+x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y & t+s | f+g";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Conditional [condition=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Ident [name=t], op=OP_PLUS, e1=Expression_Ident [name=s]]], op=OP_OR, e1=Expression_Binary [e0=Expression_Ident [name=f], op=OP_PLUS, e1=Expression_Ident [name=g]]], trueExpression=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Ident [name=t], op=OP_PLUS, e1=Expression_Ident [name=s]]], op=OP_OR, e1=Expression_Binary [e0=Expression_Ident [name=f], op=OP_PLUS, e1=Expression_Ident [name=g]]], falseExpression=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Ident [name=t], op=OP_PLUS, e1=Expression_Ident [name=s]]], op=OP_OR, e1=Expression_Binary [e0=Expression_Ident [name=f], op=OP_PLUS, e1=Expression_Ident [name=g]]]]");
	}
	
	@Test
	public void testExp9() throws LexicalException, SyntaxException {
		String input = "++++x";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]]]]]");
	}
	
	@Test
	public void testExp10() throws LexicalException, SyntaxException {
		String input = "sin(a+b)";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_FunctionAppWithExprArg [function=KW_sin, arg=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]");
	}
	
	@Test
	public void testExp11() throws LexicalException, SyntaxException {
		String input = "yu[m,n]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_PixelSelector [name=yu, index=Index [e0=Expression_Ident [name=m], e1=Expression_Ident [name=n]]]");
	}
	
	@Test
	public void testProRandom11() throws LexicalException, SyntaxException {
		String input = "hello boolean i = cos [x+y,a+b] ; hal = 5+--++++9 ; niks <- \"nik\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=hello, decsAndStatements=[Declaration_Variable [type=[KW_boolean,boolean,6,7,1,7], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Statement_Assign [lhs=name [name=hal, index=null], e=Expression_Binary [e0=Expression_IntLit [value=5], op=OP_PLUS, e1=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_IntLit [value=9]]]]]]]]], Statement_In [name=niks, source=Source_StringLiteral [fileOrUrl=nik]]]]");
	}

	@Test
	public void testExpRanAbc() throws LexicalException, SyntaxException {
		String input = "x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y & x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y & x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y & x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]]]");
	}
	
	@Test
	public void testProRandom12() throws LexicalException, SyntaxException {
		String input = "ident1 url ident2 = \"me1333\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=ident1, decsAndStatements=[Declaration_SourceSink [type=KW_url, name=ident2, source=Source_StringLiteral [fileOrUrl=me1333]]]]");
	}
	
	@Test
	public void testProRandom13() throws LexicalException, SyntaxException {
		String input = "idents url abcd = \"this is SSD string literal\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=idents, decsAndStatements=[Declaration_SourceSink [type=KW_url, name=abcd, source=Source_StringLiteral [fileOrUrl=this is SSD string literal]]]]");
	}
	
	@Test
	public void testProRandom14() throws LexicalException, SyntaxException {
		String input = "hello boolean i = cos [x+y,a+b] ; boolean i = cos [x+y,a+b] ; niks <- \"nik\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=hello, decsAndStatements=[Declaration_Variable [type=[KW_boolean,boolean,6,7,1,7], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Declaration_Variable [type=[KW_boolean,boolean,34,7,1,35], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Statement_In [name=niks, source=Source_StringLiteral [fileOrUrl=nik]]]]");
	}
	
	@Test
	public void testProRandom15() throws LexicalException, SyntaxException {
		String input = "lo int j = sin [x+y,a+b] ;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=lo, decsAndStatements=[Declaration_Variable [type=[KW_int,int,3,3,1,4], name=j, e=Expression_FunctionAppWithIndexArg [function=KW_sin, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]]]]");
	}
	
	@Test
	public void testExp12() throws LexicalException, SyntaxException {
		String input = "_habdhdb_$ int ident = 4*3;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Ident [name=_habdhdb_$]");
	}
	
	@Test
	public void testExp13() throws LexicalException, SyntaxException {
		String input = "p-+q";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Ident [name=p], op=OP_MINUS, e1=Expression_Unary [op=OP_PLUS, e=Expression_Ident [name=q]]]");
	}
	
	@Test
	public void testProRandom16() throws LexicalException, SyntaxException {
		String input = "abc int def;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=abc, decsAndStatements=[Declaration_Variable [type=[KW_int,int,4,3,1,5], name=def, e=null]]]");
	}

	@Test
	public void testExp14() throws LexicalException, SyntaxException {
		String input = "+x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]]");
	}
	
	@Test
	public void testExp15() throws LexicalException, SyntaxException {
		String input = "+x*y/t%a+g-u";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]]");
	}
	


}
