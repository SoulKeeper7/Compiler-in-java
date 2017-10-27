package cop5556fa17;
 


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression;
import cop5556fa17.AST.Expression_Binary;
import cop5556fa17.AST.Expression_BooleanLit;
import cop5556fa17.AST.Expression_Conditional;
import cop5556fa17.AST.Expression_FunctionApp;
import cop5556fa17.AST.Expression_FunctionAppWithExprArg;
import cop5556fa17.AST.Expression_FunctionAppWithIndexArg;
import cop5556fa17.AST.Expression_Ident;
import cop5556fa17.AST.Expression_IntLit;
import cop5556fa17.AST.Expression_PixelSelector;
import cop5556fa17.AST.Expression_PredefinedName;
import cop5556fa17.AST.Expression_Unary;
import cop5556fa17.AST.Index;
import cop5556fa17.AST.LHS;
import cop5556fa17.AST.Program;
import cop5556fa17.AST.Sink;
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_Assign;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;
import cop5556fa17.Parser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class Parser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}
	class FindFirstPredict
	{
		
			HashSet<Kind> Sink = new HashSet<Kind>();			
			HashSet<Kind> Expression = new HashSet<Kind>();
			HashSet<Kind> OrExpression = new HashSet<Kind>();		
			HashSet<Kind> AndExpression = new HashSet<Kind>();
			HashSet<Kind> EqExpression = new HashSet<Kind>();		
			HashSet<Kind> RelExpression = new HashSet<Kind>();
			HashSet<Kind> AddExpression = new HashSet<Kind>();		
			HashSet<Kind> MulExpression =  new HashSet<Kind>();		
			HashSet<Kind> Primary = new HashSet<Kind>();			
			HashSet<Kind> IdentOrPix =  new HashSet<Kind>();
			HashSet<Kind> RaSelector =  new HashSet<Kind>();
			HashSet<Kind> UnaryExpression =  new HashSet<Kind>();
			HashSet<Kind> UnaryNotPlusMinus = new HashSet<Kind>();
			HashSet<Kind> Program = new HashSet<Kind>();
			HashSet<Kind> Selector = new HashSet<Kind>();		
			HashSet<Kind> Declaration = new HashSet<Kind>();			
			HashSet<Kind> SourceSinkType = new HashSet<Kind>();	
			HashSet<Kind> ImageDec = new HashSet<Kind>();
			HashSet<Kind> Lhs = new HashSet<Kind>();				
			HashSet<Kind> FunctionApplication = new HashSet<Kind>();
			HashSet<Kind> FunctionName =  new HashSet<Kind>();		
			HashSet<Kind> LhsSelector = new HashSet<Kind>();
			HashSet<Kind> Statement = new HashSet<Kind>();			
			HashSet<Kind> Assignment = new HashSet<Kind>();
			HashSet<Kind> ImageIn = new HashSet<Kind>();		
			HashSet<Kind> ImageOut = new HashSet<Kind>();
			HashSet<Kind> VariableDec =  new HashSet<Kind>();		
			HashSet<Kind> VariableType = new HashSet<Kind>();
			HashSet<Kind> SourceSinkDec = new HashSet<Kind>();		
			HashSet<Kind> Source = new HashSet<Kind>();
			HashSet<Kind> XySelector = new HashSet<Kind>();		

			
			FindFirstPredict()
			{
				SetFunction();
				SetSelect();
				SetOperators();
				SetExpre();
				SetAssign();
				SetDec();
						
			}
			private void SetFunction()
			{
				SetFunctionName();		
				SetFunctionApplication();
			}
			
			private void SetSelect()
			{
				SetRaSelector();
				SetXySelector();
				SetLhsSelector();
			}
			private void SetOperators()
			{
				SetLhs();	
				SetIdentOrPix();
				SetPrimary();
				SetUnaryNotPlusMinus();
				SetUnary();
				
			}
			
			private void SetExpre()
			{
				SetMultExp();
				SetAddExp();			
				SetRelExp();
				SetEqExp();
				SetAndExp();
				SetOrExp();
				SetExpression();
				SetSelector();
			}
			private void SetAssign()
			{
				SetAssignment();	
				SetImageIn();	
				SetSink();
				SetImageOut();
				SetStatement();
				SetImageDec();
				SetSourceSinkType();
			}
			
			private void SetDec()
			{
				SetSource();		
				SetSourceSinkDec();		
				SetVariableType();		
				SetVariableDec();	
				SetDeclaration();		
				SetProgram();
			}
			private void SetUnaryNotPlusMinus()
			{
				this.UnaryNotPlusMinus.add(Kind.OP_EXCL);			
				this.UnaryNotPlusMinus.addAll(IdentOrPix);			
				this.UnaryNotPlusMinus.add(Kind.KW_y);
				this.UnaryNotPlusMinus.add(Kind.KW_X);
				this.UnaryNotPlusMinus.add(Kind.KW_Y);		
				this.UnaryNotPlusMinus.add(Kind.KW_r);
				this.UnaryNotPlusMinus.add(Kind.IDENTIFIER);
				this.UnaryNotPlusMinus.add(Kind.KW_x);			
				this.UnaryNotPlusMinus.add(Kind.KW_A);
				this.UnaryNotPlusMinus.add(Kind.KW_DEF_X);
				this.UnaryNotPlusMinus.add(Kind.KW_DEF_Y);
				this.UnaryNotPlusMinus.add(Kind.KW_Z);
				this.UnaryNotPlusMinus.add(Kind.KW_R);
				this.UnaryNotPlusMinus.addAll(this.Primary);
				this.UnaryNotPlusMinus.add(Kind.KW_a);
			}
			
			
			
			private void SetFunctionApplication()
			{
				this.FunctionApplication.addAll(this.FunctionName);
			}			
			
			private void SetXySelector()
			{
				this.XySelector.add(Kind.KW_x);
			}

			private void SetLhsSelector()
			{
				this.LhsSelector.add(Kind.LSQUARE);
			}
			
			private void SetRaSelector()
			{
				this.RaSelector.add(Kind.KW_r);
			}
						
			private void SetIdentOrPix()
			{
				this.IdentOrPix.add(Kind.IDENTIFIER);
			}
			
			private void SetPrimary()
			{
				this.Primary.addAll(this.FunctionApplication);
				this.Primary.add(Kind.LPAREN);
				this.Primary.add(Kind.INTEGER_LITERAL);
				this.Primary.add(Kind.BOOLEAN_LITERAL);
				
				
			}
			
			private void SetLhs()
			{
				this.Lhs.add(Kind.IDENTIFIER);
			}

			private void SetFunctionName()
			{
				this.FunctionName.add(Kind.KW_sin);
				this.FunctionName.add(Kind.KW_atan);
				this.FunctionName.add(Kind.KW_cos);
				this.FunctionName.add(Kind.KW_abs);
				this.FunctionName.add(Kind.KW_cart_x);
				this.FunctionName.add(Kind.KW_cart_y);
				this.FunctionName.add(Kind.KW_polar_a);
				this.FunctionName.add(Kind.KW_polar_r);
			}
			
			private void SetUnary()
			{
				this.UnaryExpression.add(Kind.OP_PLUS);
				this.UnaryExpression.add(Kind.OP_MINUS);
				this.UnaryExpression.addAll(this.UnaryNotPlusMinus);
			}
			
			private void SetMultExp()
			{
				this.MulExpression.addAll(this.UnaryExpression);
			}			
			
			private void SetExpression()
			{
				this.Expression.addAll(this.OrExpression);
			}
			
			private void SetSelector()
			{
				this.Selector.addAll(this.Expression);				
			}
			
			private void SetAssignment()
			{
				this.Assignment.addAll(this.Lhs);
			}

			private void SetImageIn()
			{
				this.ImageIn.add(Kind.IDENTIFIER);
			}
			
			private void SetSink()
			{			
				this.Sink.add(Kind.IDENTIFIER); 
				this.Sink.add(Kind.KW_SCREEN);
			}
			
			private void SetImageOut()
			{
				this.ImageOut.add(Kind.IDENTIFIER);
			}
			
			private void SetStatement()
			{
				this.Statement.addAll(this.Assignment);
				this.Statement.addAll(this.ImageOut);
				this.Statement.addAll(this.ImageIn);
			}
			
			private void SetImageDec()
			{
				this.ImageDec.add(Kind.KW_image);
			}
			
			private void SetSourceSinkType()
			{
				this.SourceSinkType.add(Kind.KW_url);
				this.SourceSinkType.add(Kind.KW_file);
			}

			private void SetSource()
			{
				this.Source.add(Kind.STRING_LITERAL);
				this.Source.add(Kind.OP_AT);
				this.Source.add(Kind.IDENTIFIER);
			}
			
			private void SetSourceSinkDec()
			{
				this.SourceSinkDec.addAll(this.SourceSinkType);
			}
			private void SetAddExp()
			{
				this.AddExpression.addAll(this.MulExpression);
			}

			private void SetRelExp()
			{
				this.RelExpression.addAll(this.AddExpression);
			}

			private void SetEqExp()
			{
				this.EqExpression.addAll(this.RelExpression);
			}

			private void SetAndExp()
			{
				this.AndExpression.addAll(this.EqExpression);
			}

			private void SetOrExp()
			{
				this.OrExpression.addAll(this.AndExpression);
			}
			private void SetVariableType()
			{
				this.VariableType.add(Kind.KW_int);
				this.VariableType.add(Kind.KW_boolean);
			}
			
			private void SetVariableDec()
			{
				this.VariableDec.add(Kind.KW_int);
				this.VariableDec.add(Kind.KW_boolean);
			}
			
			private void SetDeclaration()
			{
				this.Declaration.addAll(this.VariableDec);
				this.Declaration.addAll(this.SourceSinkDec);
				this.Declaration.addAll(this.ImageDec);
			}

			private void SetProgram()
			{
				this.Program.add(Kind.IDENTIFIER);
			}

	}
	
	
	FindFirstPredict checkFirst= new FindFirstPredict();
	Scanner scanner;
	Token t;
	

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF  
	 * 
	 * @throws SyntaxException
	 */
	public Program parse() throws SyntaxException 
	{
		Program x =program();
		matchEOF();
		return x;		
	}
	

	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	
	public Program program() throws SyntaxException
	{
		//TODO  implement this
		//throw new UnsupportedOperationException();
		// IDENTIFIER   ( Declaration SEMI | Statement SEMI )*  
	
		ArrayList<ASTNode> decsAndStateme = new ArrayList<ASTNode>();
		Program p = new Program(t,t,decsAndStateme);
		
		if(t.kind.equals(IDENTIFIER))
		{
			consume();	
			//boolean tf= true;
			ASTNode x= null;
			
			while(checkFirst.Declaration.contains(t.kind)||checkFirst.Statement.contains(t.kind))
			{
				//consume();
				if(checkFirst.Declaration.contains(t.kind))
				{
					 x=declaration();					
					 p.decsAndStatements.add(x);
				}
				else if(checkFirst.Statement.contains(t.kind))
				{
					
					x= statement();
					p.decsAndStatements.add(x);
				}
				
				
				consume();
				if(!t.kind.equals(SEMI))
				{
					throw new SyntaxException(t,"Not ending with a semicolon");
				}
				else
				{
					consume();					
				}
			}
			
		}
		else
		{
			throw new SyntaxException(t,"Not valid starting point for program");
		}
		
		return p;
	}

	

	public Declaration declaration() throws SyntaxException
	{
		//Declaration :: = VariableDeclaration | ImageDeclaration |
			//	SourceSinkDeclaration
		Declaration x =null;
		if(checkFirst.VariableDec.contains(t.kind))
		{
			//consume();
			 x=variableDeclaration();
			 
		}
		else if(checkFirst.ImageDec.contains(t.kind))
		{
			//consume();
			x= imageDeclaration();
		}
		else if(checkFirst.SourceSinkDec.contains(t.kind))
		{
			//consume();
			x=sourceSinkDeclaration();
			
		}
		return x;		
	}

	public Declaration_SourceSink sourceSinkDeclaration() throws SyntaxException{
		//SourceSinkDeclaration ::= SinkType IDENTIFIER  OP_ASSIGN  Source
		returnObject x= null;
		Token name= null;
		Token tmp=t;
		//Declaration_SourceSink y= new Declaration_SourceSink(t,t,t,null);
		if(t.kind.equals(KW_url)||t.kind.equals(KW_file))
		{
		 consume();
		if(t.kind.equals((IDENTIFIER)))
		{
			name= t;
			consume();
			if(t.kind.equals(OP_ASSIGN))
			{
				consume();
				if(checkFirst.Source.contains(t.kind))
				{
					ASTNode z= source();
					Declaration_SourceSink y= new Declaration_SourceSink(tmp,tmp,name,(Source)z);				
					return y;
				}
			}
			}
		}
			
		throw new SyntaxException(t,"Not a valid sourceSink");			
}
	

	

	/*public boolean SourceSinkType() {
		// TODO Auto-generated method stub
		//SourceSinkType := KW_url | KW_file
		if(t.kind.equals(KW_url) ||t.kind.equals((KW_file)))
				{
					return true;
				}
			
		return false;
	}*/

	public Source source() throws SyntaxException {
		// TODO Auto-generated method stub
		//Source ::= STRING_LITERAL  
		//Source ::= OP_AT Expression 
		//Source ::= IDENTIFIER
		
		//returnObject x= null;
		if(t.kind.equals(STRING_LITERAL))
		{
			Source_StringLiteral y = new Source_StringLiteral(t,t.getText());
			return y;
		}
		else if(t.kind.equals(IDENTIFIER))
		{
			Source_Ident y= new Source_Ident(t,t);
			return y;
		}
		else if(t.kind.equals(OP_AT))
			{
				consume();
				//// art needs to be checked.
				if(checkFirst.Expression.contains(t.kind))///check for Expression e
				{
					Expression e=newExpression();
					Source_CommandLineParam y = new Source_CommandLineParam(t,e);
					return y;
				}
				else
					throw new SyntaxException(t,"Not valid Source for program");
			}
		throw new SyntaxException(t,"Not valid Source for program");
	
		}

	

	public Declaration_Image imageDeclaration() throws SyntaxException
	{
		// TODO Auto-generated method stub
		//ImageDeclaration ::=  KW_image  (LSQUARE Expression COMMA Expression RSQUARE | ε)
        //IDENTIFIER ( OP_LARROW Source | ε )  
		// need to work on these
		
		///Declaration_Image(Token firstToken, Expression xSize, Expression ySize, Token name,
		//Source source) 
		//returnObject x= null;
		//Token firsttokennull =t;
		Token name = t;
		Token secondname=null;
		if(t.kind.equals(KW_image))
		{
			consume();
			if(t.kind.equals(LSQUARE))
			{
				consume();
				if(checkFirst.Expression.contains(t.kind))
				{
					Expression  xsize = newExpression();
					consume();
					if( t.kind.equals(COMMA))
					{
						consume();
						if(checkFirst.Expression.contains(t.kind))
						{
							Expression  ysize= newExpression() ;
							consume();
							if(t.kind.equals(RSQUARE))
							{
								consume();
								if(t.kind.equals(IDENTIFIER))
								{
									secondname=t;
									consume();
									if(t.kind.equals(OP_LARROW))
									{
										consume();
										if(t.kind.equals(STRING_LITERAL)||t.kind.equals(OP_AT)||t.kind.equals(IDENTIFIER))
										{
											Source s= source();
											 return  new Declaration_Image(name,xsize,ysize,secondname,s);
											
										}
									}
									else
									{
										deconsume();
										 return new  Declaration_Image(name,xsize,ysize,secondname,null);
										
									}
									throw new SyntaxException(t,"Not valid starting point for program");
									
								}
								
							}
						}
					}
				}
				throw new SyntaxException(t,"Not valid starting point for program");
			}
			else if(t.kind.equals(IDENTIFIER))
			{
				secondname=t;
				consume();
				if(t.kind.equals(OP_LARROW))
				{
					consume();
					if(t.kind.equals(STRING_LITERAL)||t.kind.equals(OP_AT)||t.kind.equals(IDENTIFIER))						
					{
						Source s= source();
						return new  Declaration_Image(name,null,null,secondname,(Source)s);
						
						
					}
				}
				else
				{
					deconsume();
					return  new  Declaration_Image(name,null,null,secondname,null);
					
				}				
				throw new SyntaxException(t,"Not valid starting point for program");
				
			}
			throw new SyntaxException(t,"Not valid starting point for program");			
		}	

		throw new SyntaxException(t,"Not valid starting point for program");	
	}

	
	public Declaration_Variable variableDeclaration() throws SyntaxException {
		// TODO Auto-generated method stub
		//VarType IDENTIFIER  (  OP_ASSIGN  Expression  | ε )
		
		//Declaration_Variable(Token firstToken,  Token type, Token name, Expression e)
		//returnObject x= new returnObject();
		
		Token tmp= t;
		Token name=null;
		if(t.kind.equals(KW_int)||t.kind.equals(KW_boolean))
		{
			consume();
			if(t.kind.equals(IDENTIFIER))
			{
				name= t;
				consume();
				 if(t.kind.equals(OP_ASSIGN))
				 {
			 		consume();
			 		if(checkFirst.Expression.contains(t.kind))
			 		{
			 			Expression e = newExpression();
			 			Declaration_Variable y= new Declaration_Variable(tmp,tmp,name,e);
			 			return y;
			 		}
				 }
				 else
				 {
					 
					 deconsume();
					 Declaration_Variable x= new Declaration_Variable(tmp,tmp,name,null);
					 return x;
					 ///e case need to be handled
				 }
			}
			
			throw new SyntaxException(t,"variableDeclaration");
			
		}
		throw new SyntaxException(t,"variableDeclaration");
	}

	public boolean varType() {
		// TODO Auto-generated method stub
		//VarType ::= KW_int | KW_boolean;
		if(t.kind.equals(KW_int)||t.kind.equals(KW_boolean))
		{
			return true;
		}
		return false;
	}

	public ASTNode statement() throws SyntaxException
	{
		// TODO Auto-generated method stub
		//Statement  ::= AssignmentStatement 
	  	//| ImageOutStatement    
	  	//| ImageIn  
		//if(t.kind.equals(IDENTIFIER))
		//{
			Token toadd= t;
			//consume();
			if(checkFirst.ImageIn.contains(t.kind) || checkFirst.ImageOut.contains(t.kind))
			{
				consume();
				if(t.kind.equals(OP_LARROW))
				{
					Statement_In x= imageInStatement(toadd);
					return x;
				}
				else if(t.kind.equals(OP_RARROW))
				{
					Statement_Out  x = imageOutStatement(toadd);
					return x;
				}
			}
					
			deconsume();
			
			if(checkFirst.Assignment.contains(t.kind) &&t.kind.equals(IDENTIFIER))
			{
				//consume();
				Statement_Assign x= assignmentStatement(toadd);
				return x;
			}			
			
						
		throw new SyntaxException(t,"Exceptionn at imageInStatement");
	}

	
	public Statement_In imageInStatement(Token tmp) throws SyntaxException {
		// TODO Auto-generated method stub
		//IDENTIFIER OP_LARROW Source
		//returnObject x= new returnObject();
		//Statement_In y= new Statement_In(t,t,null);
		
		
			if( t.kind.equals(OP_LARROW))
			{
				consume();
				if(t.kind.equals(STRING_LITERAL)||t.kind.equals(OP_AT)||t.kind.equals(IDENTIFIER))
				{
					
					ASTNode s= source();
					Statement_In  x= new Statement_In(tmp,tmp,(Source)s);
					return x;
					
				
				}
			}
			 throw new SyntaxException(t,"Exceptionn at imageInStatement");
					
			}	
			
		
	

	public Statement_Out imageOutStatement(Token tmp) throws SyntaxException {
		// TODO Auto-generated method stub
		//IDENTIFIER OP_RARROW Sink 

			if( t.kind.equals(OP_RARROW))
			{
				
				consume();
				if(t.kind.equals(IDENTIFIER) ||t.kind.equals(KW_SCREEN))
					{
						ASTNode y=sink(tmp);
						return new Statement_Out(tmp,tmp,(Sink)y) ;
					}
				
			}
					throw new SyntaxException(t,"Exceptionn at imageoutStatement");		
						
		}
			
		
	

	public Sink sink(Token tmp) {
		// TODO Auto-generated method stub
		//Sink ::= IDENTIFIER | KW_SCREEN  //ident must be file
		if(t.kind.equals(IDENTIFIER))
		{
			Sink_Ident x= new Sink_Ident(t,t);			
			return x;
			
		}
		else if(t.kind.equals(KW_SCREEN))
		{
			Sink_SCREEN x= new Sink_SCREEN(t);			
			return x;
		}
		
		return null;
		
	}

	public Statement_Assign assignmentStatement(Token tmp) throws SyntaxException {
		// TODO Auto-generated method stub
		//Lhs OP_ASSIGN Expression
		if(t.kind.equals(IDENTIFIER))
		{
			consume();
			LHS x= lhs(tmp);
			//consume();
			if(t.kind.equals(OP_ASSIGN))
			{
				consume();
				if(checkFirst.Expression.contains(t.kind))
				{
					Expression em= newExpression();
					return new Statement_Assign(tmp,x,em);
				}
			}
			throw new SyntaxException(t,"Exception assignmentstatement");
		}
		throw new SyntaxException(t,"Exception assignmentstatement");

		//retFfunctionnurn false;
	}

	public LHS lhs(Token tmp) throws SyntaxException {
		// TODO Auto-generated method stub
		//IDENTIFIER ( LSQUARE LhsSelector RSQUARE   | ε )
		LHS x= new LHS (tmp,tmp,null);
			if(t.kind.equals(LSQUARE))
			{
				consume();
				if(t.kind.equals(LSQUARE))
				{
					Index y = lhsSelector();
					//consume();
					if(t.kind.equals(RSQUARE))
					{
						return new LHS(tmp,tmp,y);
					}
				}
			}
			else
			{
				//deconsume();
				return new LHS(tmp,tmp,null);
			}
			throw new SyntaxException(t,"Excetion lhs");
		}
		//return false;
	

	public Index lhsSelector() throws SyntaxException {
		// TODO Auto-generated method stub
		//LhsSelector ::= LSQUARE  ( XySelector  | RaSelector  )   RSQUARE
		Token tmp=t;
		Index x=null;
		if(t.kind.equals(LSQUARE))
		{
			consume();
			if(t.kind.equals(KW_x))
			{
			 x= xySelector();
			}
			else if(t.kind.equals(KW_r))
			{
				x= RaSelector();
			}
			else
			{
				throw new SyntaxException(t,"Exception lhsSelector");
			}
				
			consume();	
			
				
				if(t.kind.equals(RSQUARE))
					return x;
			}
			
			throw new SyntaxException(t,"Exception lhsSelector");
	}

		//return false;
	

	public Index xySelector() throws SyntaxException {
		// TODO Auto-generated method stub
		//XySelector ::= KW_x COMMA KW_y
		Token tmp =t;
		if(t.kind.equals(KW_x))
		{
			Expression kwX = new Expression_PredefinedName(t,t.kind);
			consume();
			if(t.kind.equals(COMMA))
			{
				consume();
				if(t.kind.equals(KW_y))
				{
					Expression kwy = new Expression_PredefinedName(t,t.kind);
					return new Index(tmp,kwX,kwy);
				}
			}
			throw new SyntaxException(t,"Exception RaSeletor");
		}
		//return false;
		throw new SyntaxException(t,"Exception RaSeletor");
	}

	public Index RaSelector() throws SyntaxException {
		// TODO Auto-generated method stub
		//RaSelector ::= KW_r COMMA KW_A
		Token tmp=t;
		if(t.kind.equals(KW_r))
		{
			Expression kwr = new Expression_PredefinedName(t,t.kind);
			consume();
			if(t.kind.equals(COMMA))
			{
				consume();
				if(t.kind.equals(KW_A))
				{
					Expression kwa = new Expression_PredefinedName(t,t.kind);
					return new Index(tmp,kwr,kwa);
				}
			}
			throw new SyntaxException(t,"Not valid starting point for program");
		}

		//return false;
		throw new SyntaxException(t,"Not valid starting point for program");
	}

	/**
	 * Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression    | OrExpression
	 * 
	 * Our test cases may invoke this routine directly to support incremental development.
	 * 
	 * @throws SyntaxException
	 */
	Expression expression() throws SyntaxException
	{
		//TODO implement this.
			
		return newExpression();
		
	}

	public Expression newExpression() throws SyntaxException 
	{
		//Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression  |  OrExpression
		
		// TODO Auto-generated method stub
		Expression e2=null,e1=null;
		Token tmp = t;
		Expression e0=null;
		if(checkFirst.OrExpression.contains(t.kind))
		{
			 e0=OrExpression();
			consume();
			if(t.kind.equals(OP_Q))
			{ 
				consume();
				if(checkFirst.Expression.contains(t.kind))
				{
					 e1= newExpression();
					consume();
					if(t.kind.equals(OP_COLON))
					{
						consume();
						if(checkFirst.OrExpression.contains(t.kind))
						{
							 e2= newExpression();
							 Expression_Conditional  x= new Expression_Conditional(tmp,e0,e1,e2);
							return x;
						}
					}
				}
			}
			else 
			{
				if(t.kind!=EOF)
					deconsume();
				return e0;
			}
			throw new SyntaxException(t,"not a valid expression");
			
		}
		
	return null;
	}



	public Expression OrExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		//OrExpression ::= AndExpression   (  OP_OR  AndExpression)*
		Expression e0=null,e1=null;
		Token tmp =t;
		Token operator = null;
		if(checkFirst.AndExpression.contains(t.kind))
		{
			e0= AndExpression();
			consume();
			if(t.kind.equals(OP_OR))
			{
				while(t.kind.equals(OP_OR))
				{
					operator=t;
					consume();
					if(!checkFirst.AddExpression.contains(t.kind))
					{
						throw new SyntaxException(t,"not a vlid input");
					}
					else
					{
						e1 = AndExpression();
						e0 = new Expression_Binary(tmp,e0,operator,e1);
						consume();
					}
				}
				deconsume();
				return e0;

			}
			else
			{
				deconsume();
				//Expression_Binary  z = new Expression_Binary(tmp,e0,operator,e1);
				return e0;

			}
			
				//throw new SyntaxException(t,"not a vlid input");
				}
		throw new SyntaxException(t,"not a vlid input");
		//return false;
	}

	public Expression AndExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		//AndExpression ::= EqExpression ( OP_AND  EqExpression )*
		//Token tem=t;
		Expression e0=null,e1=null;
		Token Operator = null;
		Token tmp =t;
		if(checkFirst.EqExpression.contains(t.kind))
		{
			e0=EqExpression();
			consume();
			if(t.kind.equals(OP_AND))
			{
				while(t.kind.equals(OP_AND))
				{
					Operator = t;
					consume();
					if(!checkFirst.EqExpression.contains(t.kind))
					{
						throw new SyntaxException(t,"not a valid input ");
					}
					e1=EqExpression();
					e0 = new Expression_Binary(tmp,e0,Operator,e1);
					consume();
				}
				deconsume();
				return e0;
					
			}
			else
			{
				deconsume();
				//Expression_Binary  z = new Expression_Binary(tmp,e0,Operator,null);
				return e0;
			}
			//throw new SyntaxException(t,"not a vlid input");
		}
		throw new SyntaxException(t,"not a vlid input");

		//return false;
	}

	public Expression EqExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		//EqExpression ::= RelExpression  (  (OP_EQ | OP_NEQ )  RelExpression )*
		Expression e0=null,e1=null;
		Token operator = null; 
		Token tmp =t;
		if(checkFirst.RelExpression.contains(t.kind))
		{
			e0 = RelExpression();
			consume();
			if(t.kind == OP_EQ || t.kind == OP_NEQ)
			{
				while(t.kind == OP_EQ || t.kind == OP_NEQ)
				{
					operator = t;
					consume();
					if(!checkFirst.RelExpression.contains(t.kind))
					{
						throw new SyntaxException(t,"not a valid input");
					}
					e1=  RelExpression();
					e0 = new Expression_Binary(tmp,e0,operator,e1);
					consume();
				}
				deconsume();
				return e0;
					
			}
			else
			{
				deconsume();
				//Expression_Binary  z = new Expression_Binary(tmp,e0,operator,null);
				return e0;
			}
			//throw new SyntaxException(t,"not a vlid input");
		}
		throw new SyntaxException(t,"not a vlid input");
		//return false;

		
	}

	public Expression RelExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		//RelExpression ::= AddExpression (  ( OP_LT  | OP_GT |  OP_LE  | OP_GE )   AddExpression)*
		Expression e0=null,e1=null;
		Token operator = null;
		Token tmp=t;
		if(checkFirst.AddExpression.contains(t.kind))
		{
			e0=AddExpression();
			consume();
			if(t.kind.equals(OP_LT)|| t.kind.equals(OP_GT)||t.kind.equals(OP_LE)|| t.kind.equals(OP_GE))
			{
				while(t.kind.equals(OP_LT)|| t.kind.equals(OP_GT)||t.kind.equals(OP_LE)|| t.kind.equals(OP_GE))
				{
					operator = t;
					consume();
					if(!checkFirst.AddExpression.contains(t.kind))
					{
						throw new SyntaxException(t,"not a valid input ");
					}
					e1=AddExpression();
					e0 = new Expression_Binary(tmp,e0,operator,e1);
					consume();
				}
				deconsume();
				return e0;
					
			}
			else
			{
				deconsume();
				//Expression_Binary  z = new Expression_Binary(tmp,e0,operator,null);
				return e0;
			}
			//throw new SyntaxException(t,"not a vlid input");
		}
		throw new SyntaxException(t,"not a vlid input");
		//return false;
	}

	public Expression AddExpression() throws SyntaxException {
		// TODO Auto-generated method stub
		//AddExpression ::= MultExpression   (  (OP_PLUS | OP_MINUS ) MultExpression )*

		Token tmp =t;
		Expression e0= null;
		Token Operator= null;
		Expression e1= null;
		if(checkFirst.MulExpression.contains(t.kind))
		{
			 e0= MultExpression();
			consume();			
			if(t.kind.equals(OP_PLUS)|| t.kind.equals(OP_MINUS))
			{
				while(t.kind.equals(OP_PLUS)|| t.kind.equals(OP_MINUS))
				{
					Operator = t;
					consume();
					if(!(checkFirst.MulExpression.contains(t.kind)))
					{
						throw new SyntaxException(t,"not a valid input ");
					}
					 e1= MultExpression();
					 e0 = new Expression_Binary(tmp,e0,Operator,e1);
					consume();
				}
				deconsume();
				//Expression_Binary  z = new Expression_Binary(tmp,e0,tmp,e1);
				return e0;
					
			}
			else
			{
				deconsume();
				//Expression_Binary  z = new Expression_Binary(tmp,e0,Operator,null);
				return e0;
			}
			//throw new SyntaxException(t,"not a vlid input");
		}
		throw new SyntaxException(t,"not a vlid input");
		
		//return false;
	}

	public Expression MultExpression() throws SyntaxException 
	{
		// TODO Auto-generated method stub
		//MultExpression := UnaryExpression ( ( OP_TIMES | OP_DIV  | OP_MOD ) UnaryExpression )*
		Token tmp =t;
		Token operator=null;
		Expression e0=null;
		if(checkFirst.UnaryExpression.contains(t.kind))
		{
			 e0= UnaryExpression();
			
			consume();
			if(t.kind.equals(OP_TIMES)|| t.kind.equals(OP_DIV)|| t.kind.equals(OP_MOD))
			{
				Expression e1=null;
				while(t.kind.equals(OP_TIMES)|| t.kind.equals(OP_DIV)|| t.kind.equals(OP_MOD))
				{
					operator= t;
					consume();
					if(!checkFirst.UnaryExpression.contains(t.kind))
					{
						throw new SyntaxException(t,"not a valid input ");
					}
					 e1= UnaryExpression();					 
					 e0 = new Expression_Binary(tmp,e0,operator,e1);
					consume();
				}
				
				deconsume();				
				return e0;
				//return true;
					
			}
			else
			{
				deconsume();/////look to check in future				
				//Expression_Binary  z = new Expression_Binary(tmp,e0,operator,null);
				return e0;
			}
			
		}

		throw new SyntaxException(t,"not a valid input ");
	}

	public Expression UnaryExpression() throws SyntaxException
	{
		// TODO Auto-generated method stub
		//UnaryExpression ::= OP_PLUS UnaryExpression 
        //| OP_MINUS UnaryExpression 
        //| UnaryExpressionNotPlusMinus
		
		//consume();
		Token tmp = t;
		Token operator= null;
			if(t.kind.equals(OP_PLUS))
			{
				operator= t;
					consume();
					if(!(checkFirst.UnaryExpression.contains(t.kind)))
					{
						
						throw new SyntaxException(t,"UnaryExpression");
					}
					Expression x = UnaryExpression();
					return new Expression_Unary(tmp,operator,x);
				
			}
			else if(t.kind.equals(OP_MINUS))
			{
				operator =t;
				consume();
				if(!checkFirst.UnaryExpression.contains(t.kind))
				{
					throw new SyntaxException(t,"UnaryExpression ");
				}
				Expression x = UnaryExpression();
				return new Expression_Unary(tmp,operator,x);
			}
			else if(checkFirst.UnaryNotPlusMinus.contains(t.kind))
			{
				Expression x = UnaryExpressionNotPlusMinus();
				return x;
				
				
			}
			throw new SyntaxException(t,"UnaryExpression");
				
				
			}

	public Expression UnaryExpressionNotPlusMinus() throws SyntaxException {
		// TODO Auto-generated method stub
		//UnaryExpressionNotPlusMinus ::=  OP_EXCL  UnaryExpression  | Primary 
		//| IdentOrPixelSelectorExpression | KW_x | KW_y | KW_r | KW_a | KW_X | KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
		Token tmp=t;
		Token Operator=t; 
		//Expression_Unary x= new Expression_Unary(t,t,null)
		//Token tmp=t;
		if(t.kind.equals(OP_EXCL))
		{
			Operator = t; 
			consume();
			if(checkFirst.UnaryExpression.contains(t.kind)) /// check for the data
			{
				Expression x = UnaryExpression();
				Expression_Unary y = new Expression_Unary(tmp,Operator,x);
				return y;
			}
			else
			{
				throw new SyntaxException(t,"UnaryExpression");
			}
			//return true;
			
		}
		else if(checkFirst.Primary.contains(t.kind))		
		{
			Expression e= Primary();
			return e;  
		}
		else if(checkFirst.IdentOrPix.contains(t.kind))
		{
			Expression em= IdentOrPixelSelectorExpression();
			return em;
		}
		else if(t.kind.equals(KW_x)||t.kind.equals(KW_y)|| t.kind.equals(KW_r)||t.kind.equals(KW_a)||t.kind.equals(KW_X)||t.kind.equals(KW_Y)||t.kind.equals(KW_Z)||t.kind.equals(KW_A)||t.kind.equals(KW_R)||t.kind.equals(KW_DEF_X)||t.kind.equals(KW_DEF_Y))
		{
			//return true;
			return new Expression_PredefinedName(t,t.kind);
		}
		
		throw new SyntaxException(t,"UnaryExpression ");
	}

	public Expression IdentOrPixelSelectorExpression() throws SyntaxException 
	{
		// TODO Auto-generated method stub
		//IdentOrPixelSelectorExpression::=  IDENTIFIER LSQUARE Selector RSQUARE   | IDENTIFIER
		Token tmp=t;
		if( t.kind.equals(IDENTIFIER))
		{
			consume();
			if(t.kind.equals(LSQUARE))
			{
				consume();
				if(checkFirst.Expression.contains(t.kind))
				{
					Index x= selector();
					consume();
					if(t.kind.equals(RSQUARE))
					{
						Expression_PixelSelector y =new Expression_PixelSelector(tmp,tmp,x);
						return y;
					}
				}
				throw new SyntaxException(t,"IdentOrPixelSelectorExpression");
			}
			else
			{
				deconsume();
				//return true;
				Expression_Ident z= new Expression_Ident(tmp,tmp);
				return z;
			}
			
		}
		
		throw new SyntaxException(t,"IdentOrPixelSelectorExpression");
		
		//return false;
	}

	public Index selector() throws SyntaxException {
		// TODO Auto-generated method stub
		//Selector ::=  Expression COMMA Expression 
		Token tmp=t;
		if(checkFirst.Expression.contains(t.kind))
		{
			Expression e0= newExpression();
			try
			{
		  consume();
		  if(t.kind.equals(COMMA))
		  {
			  consume();
			  if(checkFirst.Expression.contains(t.kind))
			  {
				  Expression e1= newExpression();
				  return new Index(t,e0,e1);
			  }
		  }
		  throw new SyntaxException(t,"selector error");
			}
		
		catch(Exception e)
		{
			throw new SyntaxException(t,"selector error");
		}
		}
		throw new SyntaxException(t,"selector error");
		//return false;
	}

	public Expression_FunctionApp FunctionApplication() throws SyntaxException 
	{
		// TODO Auto-generated method stub
		//FunctionName LPAREN Expression RPAREN | FunctionName  LSQUARE Selector RSQUARE 
		Token tmp=t;
		if(t.kind.equals(KW_sin)||t.kind.equals(KW_cos)||t.kind.equals(KW_atan)||t.kind.equals(KW_abs)||t.kind.equals(KW_cart_x)||t.kind.equals(KW_cart_y)||t.kind.equals(KW_polar_a)||t.kind.equals(KW_polar_r))
		{
			FunctionName();
			consume();
			if(t.kind.equals(LPAREN))
			{
				consume();
				if(checkFirst.Expression.contains(t.kind))
				{
					Expression e0= newExpression();
					consume();
					if(t.kind.equals(RPAREN))
					{
						return new Expression_FunctionAppWithExprArg(tmp,tmp.kind,e0);
					}
				}
				throw new SyntaxException(t, "Lets see");
			}
			else if(t.kind.equals(LSQUARE))
			{
				consume();
				if(checkFirst.Expression.contains(t.kind))
				{
					Index i= selector();
					consume();
					if(t.kind.equals(RSQUARE))
					{
						return new Expression_FunctionAppWithIndexArg(tmp,tmp.kind,i);
					}
				}
				throw new SyntaxException(t, "Lets see");
			}
		}
		throw new SyntaxException(t, "Lets see");
		//return false;
	}

	public boolean FunctionName() {
		// TODO Auto-generated method stub
		//FunctionName ::= KW_sin | KW_cos | KW_atan | KW_abs		| KW_cart_x | KW_cart_y | KW_polar_a | KW_polar_r
		if(t.kind.equals(KW_sin)||t.kind.equals(KW_cos)||t.kind.equals(KW_atan)||t.kind.equals(KW_abs)||t.kind.equals(KW_cart_x)||t.kind.equals(KW_cart_y)||t.kind.equals(KW_polar_a)||t.kind.equals(KW_polar_r))
		{
			return true;
		}
		

		return false;
	}

	public Expression Primary() throws SyntaxException {
		// TODO Auto-generated method stub
		//Primary ::= INTEGER_LITERAL | LPAREN Expression RPAREN | FunctionApplication 

				if(t.kind.equals(INTEGER_LITERAL))
				{
					return new Expression_IntLit(t,t.intVal());
				}
				else if(t.kind.equals(LPAREN))
				{
					consume();
					if(!(checkFirst.Expression.contains(t.kind)))
					{
						
						//return true;
						throw new SyntaxException(t,"Why?");
					}
					else
					{
						Expression e= newExpression();
						
						consume();
						if(t.kind.equals(RPAREN))
						{
							return e;
						}
						throw new SyntaxException(t,"Why?");
					}
				}
				else if(t.kind.equals(KW_sin)||t.kind.equals(KW_cos)||t.kind.equals(KW_atan)||t.kind.equals(KW_abs)||t.kind.equals(KW_cart_x)||t.kind.equals(KW_cart_y)||t.kind.equals(KW_polar_a)||t.kind.equals(KW_polar_r))
				{
					Expression_FunctionApp x= FunctionApplication();
					return x;
					//throw new SyntaxException(t,"Why?");
				}
				else if(t.kind.equals(BOOLEAN_LITERAL))
				{
					return new Expression_BooleanLit(t,t.getText().equals("true"));
					
				}
				throw new SyntaxException(t, "Lets see");
		//return false;
	}

	/**
	 * Only for check at end of program. Does not "consume" EOF so no attempt to get
	 * nonexistent next Token.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	public Token matchEOF() throws SyntaxException {
		if (t.kind == EOF)
		{
			return t;
		}
		String message =  "Expected EOL at " + t.line + ":" + t.pos_in_line;
		throw new SyntaxException(t, message);
	}
	
	public Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}
	
	public Token deconsume() throws SyntaxException
	{
		
		Token tmp = t;
		t = scanner.previousToken();
		return tmp;
	
	}
	
	public class returnObject
	{
		boolean returntype= false;
		ASTNode nod= null;
	}

}
