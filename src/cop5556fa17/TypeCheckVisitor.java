package cop5556fa17;

import java.awt.Window.Type;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
//import cop5556sp17.AST.Type;
//import cop5556sp17.TypeCheckVisitor.TypeCheckException;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
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
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_Assign;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;
import cop5556fa17.Parser.SyntaxException;

public class TypeCheckVisitor implements ASTVisitor {
	

	
		@SuppressWarnings("serial")
		public static class SemanticException extends Exception {
			Token t;

			public SemanticException(Token t, String message) 
			{
				super("line " + t.line + " pos " + t.pos_in_line + ": "+  message);
				this.t = t;
			}

		}	
		
		HashMap<String,Declaration> symTab = new HashMap<String,Declaration>();

	
	/**
	 * The program name is only used for naming the class.  It does not rule out
	 * variables with the same name.  It is returned for convenience.
	 * 
	 * @throws Exception 
	 */
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for (ASTNode node: program.decsAndStatements) {
			node.visit(this, arg);
		}
		return program.name;
	}

	@Override
	public Object visitDeclaration_Variable(Declaration_Variable declaration_Variable, Object arg)throws Exception
		{	
		
		
			if(declaration_Variable.e != null)
			{
				declaration_Variable.e.visit(this,arg);
			}
			
			if(symTab.get(declaration_Variable.name) != null)
			{
				throw new SemanticException(declaration_Variable.firstToken,"Error in visitDeclaration");
			}	
			
			symTab.put(declaration_Variable.name, declaration_Variable);
			declaration_Variable.attrType= TypeUtils.getType(declaration_Variable.firstToken);
			//REQUIRE if (Expression !=  Îµ) Declaration_Variable.Type == Expression.Type
			if(declaration_Variable.e!=null)
			{
				if(!(declaration_Variable.attrType==declaration_Variable.e.attrType))
					throw new SemanticException(declaration_Variable.firstToken,"Error in visitDeclaration");
				
			}
		
			return null;
		}
		
		

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary,
			Object arg) throws Exception 
	{	try
	{
		if(expression_Binary.e0 != null)
		{
		expression_Binary.e0.visit(this, arg);
		}
		
		if(expression_Binary.e1 != null)
		{
			expression_Binary.e1.visit(this, arg);
		}
		if((expression_Binary.e0.attrType !=expression_Binary.e1.attrType && expression_Binary.attrType ==null))
		{
			throw new SemanticException(expression_Binary.firstToken,"errot in binary");
		}
		
		if(expression_Binary.op.equals(Kind.OP_EQ) ||expression_Binary.op.equals(Kind.OP_NEQ))
		{
			expression_Binary.attrType = TypeUtils.Type.BOOLEAN;
			
		}
		else if((expression_Binary.op.equals(Kind.OP_GE) ||expression_Binary.op.equals(Kind.OP_GT)||expression_Binary.op.equals(Kind.OP_LT) ||expression_Binary.op.equals(Kind.OP_LE)) && expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER))
		{
			expression_Binary.attrType = TypeUtils.Type.BOOLEAN;
		}
		else if((expression_Binary.op.equals(Kind.OP_AND) ||expression_Binary.op.equals(Kind.OP_OR)) && (expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER)||expression_Binary.e0.attrType.equals(TypeUtils.Type.BOOLEAN)))
		{
			expression_Binary.attrType = expression_Binary.e0.attrType;
		}
		else if(((expression_Binary.op.equals(Kind.OP_DIV) ||expression_Binary.op.equals(Kind.OP_MINUS)||expression_Binary.op.equals(Kind.OP_MOD) ||expression_Binary.op.equals(Kind.OP_PLUS)||expression_Binary.op.equals(Kind.OP_POWER)||expression_Binary.op.equals(Kind.OP_TIMES)) && expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER)))
		{
			expression_Binary.attrType= TypeUtils.Type.INTEGER;
		}
		else
		{
			expression_Binary.attrType= null;
		}
	
		return null;
	}
	catch(Exception e)
	{
		throw new SemanticException(expression_Binary.firstToken,"error in binary");
	}
	
		
	}

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary,Object arg) throws Exception 
	{
		if(expression_Unary.e!=null)
		expression_Unary.e.visit(this, null);
		if(expression_Unary.op.equals(Kind.OP_EXCL) && (expression_Unary.e.attrType==TypeUtils.Type.BOOLEAN || expression_Unary.e.attrType==TypeUtils.Type.INTEGER))
		{
			expression_Unary.attrType=expression_Unary.e.attrType;
		}
		else if((expression_Unary.op.equals(Kind.OP_PLUS)|| expression_Unary.op.equals(Kind.OP_MINUS))&& (expression_Unary.e.attrType==TypeUtils.Type.INTEGER ))
		{
			expression_Unary.attrType=TypeUtils.Type.INTEGER;
		}
		else
		{
			expression_Unary.attrType =null ;
		}
		if(expression_Unary.attrType==null)
		{
			throw new SemanticException(expression_Unary.firstToken,"Error in visit expression");
		}
		
		return null;
	}

	@Override
	public Object visitIndex(Index index, Object arg) throws Exception
	{
		if(index.e0!=null)
		index.e0.visit(this, arg);
		
		if(index.e1!=null)
		index.e1.visit(this, arg);
		if(index.e0.attrType==TypeUtils.Type.INTEGER && index.e1.attrType ==TypeUtils.Type.INTEGER)
		{
			index.setCartesian(!(index.e0.firstToken.kind.equals(Kind.KW_r) &&(index.e1.firstToken.kind.equals(Kind.KW_a))));
			return null;
		}
		throw new SemanticException(index.firstToken,"cakmc");
		
		

	}

	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg)	throws Exception 
	{
		if(expression_PixelSelector.index!=null)
		expression_PixelSelector.index.visit(this, null);		

		if(symTab.get(expression_PixelSelector.name).attrType ==TypeUtils.Type.IMAGE)
		{
			expression_PixelSelector.attrType=TypeUtils.Type.INTEGER;
		}
		else if(expression_PixelSelector.index == null)
		{
			expression_PixelSelector.attrType = symTab.get(expression_PixelSelector.name).attrType;
		}
		else
		{
			expression_PixelSelector.attrType= null;
		}
		
		if(expression_PixelSelector.attrType ==null)
		{
			throw new SemanticException(expression_PixelSelector.firstToken,"Error in pixel selector");
		}
		return null;
		
	}
	

	@Override
	public Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg)throws Exception 
	{
		
		expression_Conditional.condition.visit(this, arg);
		expression_Conditional.falseExpression.visit(this, arg);
		expression_Conditional.trueExpression.visit(this, arg);
		if(expression_Conditional.condition!= null &&expression_Conditional.condition.attrType.equals(TypeUtils.Type.BOOLEAN) && expression_Conditional.trueExpression.attrType.equals(expression_Conditional.falseExpression.attrType))
		{
			expression_Conditional.attrType = expression_Conditional.trueExpression.attrType;
			return null;
		}
		throw new SemanticException(expression_Conditional.firstToken,"erroe in condtional");
		
	}

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image,Object arg) throws Exception 
	{
				
		//ASTNode x= declaration_Image.source;
		
			
		
		if(declaration_Image.source != null)
		{
		declaration_Image.source.visit(this, arg);
		}
		
		if(symTab.get(declaration_Image.name) != null)
		{
			throw new SemanticException(declaration_Image.firstToken,"Image already available");
		}
		
		symTab.put(declaration_Image.name, declaration_Image);
		declaration_Image.attrType=TypeUtils.Type.IMAGE;
		
		if(declaration_Image.xSize != null)
		{		
			if(declaration_Image.ySize !=null)
			{
				declaration_Image.xSize.visit(this, arg);
				declaration_Image.ySize.visit(this, arg);
				if(declaration_Image.xSize.attrType != TypeUtils.Type.INTEGER && declaration_Image.ySize.attrType!= TypeUtils.Type.INTEGER)
				{
					throw new SemanticException(declaration_Image.firstToken,"Image already available");
				}
			}
			else
			{
				throw new SemanticException(declaration_Image.firstToken,"Image already available");
			}
		}
		
				return null;
		
	}

	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg)throws Exception {
		//source_StringLiteral.visit(this, arg);
		
		
		if(source_StringLiteral == null)
		{
			throw new SemanticException(source_StringLiteral.firstToken,"null");
		}
		
		Boolean y = isValidURL(source_StringLiteral.fileOrUrl);

		//UrlValidator defaultValidator = new UrlValidator();
		try
		{
		//URL x=new URL(source_StringLiteral.fileOrUrl);
		if(y)
		{
			source_StringLiteral.attrType =TypeUtils.Type.URL;
		}
		else
		{
			source_StringLiteral.attrType =TypeUtils.Type.FILE;
		}
		return null;
		}
		catch(Exception e)
		{
			throw e;//new Exception(source_StringLiteral.firstToken,"cnsnc");
		}
		
	}

	public static boolean isValidURL(String urlString)
	{
	    try
	    {
	        URL url = new URL(urlString);
	        
	        return true;
	    } catch (Exception exception)
	    {
	        return false;
	    }
	}
	@Override
	public Object visitSource_CommandLineParam(
			Source_CommandLineParam source_CommandLineParam, Object arg)
			throws Exception
	{
		
		source_CommandLineParam.paramNum.visit(this, arg);
		/*source_CommandLineParam.attrType = source_CommandLineParam.paramNum.attrType;
		if(source_CommandLineParam.attrType == TypeUtils.Type.INTEGER )
			return null;*/
		
		source_CommandLineParam.attrType = null;
		if(source_CommandLineParam.paramNum.attrType == TypeUtils.Type.INTEGER )
			return null;
		throw new SemanticException(source_CommandLineParam.firstToken,"commandline attribute didn't match");

	}

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg)
			throws Exception {
		
		//Source_Ident.Type <= symbolTable.lookupType(name)
		source_Ident.attrType = symTab.get(source_Ident.name) ==null?null:symTab.get(source_Ident.name).attrType;
		if(!(source_Ident.attrType==TypeUtils.Type.FILE || source_Ident.attrType==TypeUtils.Type.URL))
		{
			throw new SemanticException(source_Ident.firstToken,"vist_source ident mismatch");
		}
		return null;
		
		
	}

	@Override
	public Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg) throws Exception
	{
		
		if(declaration_SourceSink.source!=null)
		{
		declaration_SourceSink.source.visit(this, arg);
		}
	
		if(symTab.get(declaration_SourceSink.name) != null)
		{
			throw new SemanticException(declaration_SourceSink.firstToken,"Source already there");			
		}
		
		
		
		symTab.put(declaration_SourceSink.name,declaration_SourceSink);	
		declaration_SourceSink.attrType= TypeUtils.getType(declaration_SourceSink.firstToken);
		
		if(declaration_SourceSink.source.attrType == declaration_SourceSink.attrType|| (declaration_SourceSink.source.attrType == null))
		{
			return null;
			
		}
		throw new SemanticException(declaration_SourceSink.firstToken,"sourcesink");
		
	}

	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit,
			Object arg) throws Exception {
		expression_IntLit.attrType=TypeUtils.Type.INTEGER;
		return null;
		}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg,Object arg) throws Exception
	{
		
		expression_FunctionAppWithExprArg.arg.visit(this, arg);
		if(expression_FunctionAppWithExprArg.arg.attrType.equals(TypeUtils.Type.INTEGER))
		{
			expression_FunctionAppWithExprArg.attrType= TypeUtils.Type.INTEGER;
			return null;
		}
		
		throw new SemanticException(expression_FunctionAppWithExprArg.firstToken,"Index arg not integer");
		
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg,
			Object arg) throws Exception
	{
		//Expression_FunctionAppWithExprArg.Type <= INTEGER	
		
		expression_FunctionAppWithIndexArg.arg.visit(this, arg);
		expression_FunctionAppWithIndexArg.attrType= TypeUtils.Type.INTEGER;
		return null;
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg) 	throws Exception
	{
		expression_PredefinedName.attrType = TypeUtils.Type.INTEGER;
		return null;
	}

	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg)
			throws Exception {
		if(statement_Out.sink!=null)
		statement_Out.sink.visit(this, arg);
		statement_Out.setDec(symTab.get(statement_Out.name));
		Declaration x =symTab.get(statement_Out.name);
		
		if(x == null)
		{
			throw new SemanticException(statement_Out.firstToken,"Statement_Out semantic error");
		}
		 if((x.attrType == TypeUtils.Type.INTEGER || x.attrType == TypeUtils.Type.BOOLEAN) && statement_Out.sink.attrType== TypeUtils.Type.SCREEN||
				 	 ((symTab.get(statement_Out.name).attrType == TypeUtils.Type.IMAGE &&(statement_Out.sink.attrType== TypeUtils.Type.FILE || statement_Out.sink.attrType== TypeUtils.Type.SCREEN))))
				{
			return null;
		}
		throw new SemanticException(statement_Out.firstToken,"Statement_Out semantic error");
	}

	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg)
			throws Exception {
		if(statement_In.source!=null)
		statement_In.source.visit(this, arg);
		statement_In.setDec(symTab.get(statement_In.name));
		//if(symTab.get(statement_In.name)!=null && symTab.get(statement_In.name).attrType==statement_In.source.attrType)
		//{
			return null;
		//}
		//throw new SemanticException(statement_In.firstToken,"Statement in type mismatch");
	}

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign,Object arg) throws Exception
	{	
		if(statement_Assign.lhs != null)
		{
		statement_Assign.lhs.visit(this, arg);
		}
		
		if(statement_Assign.e != null)
		statement_Assign.e.visit(this, arg);
		
		if((!(statement_Assign.lhs.attrType==TypeUtils.Type.IMAGE && statement_Assign.e.attrType== TypeUtils.Type.INTEGER))&&(statement_Assign.lhs.attrType != statement_Assign.e.attrType) )
		{
			throw new SemanticException(statement_Assign.firstToken,"lhs != expression");
		}
		statement_Assign.setCartesian(statement_Assign.lhs.isCartesian());
		return null;
	}

	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception 
	{
		if(lhs.index!=null)
		{
		lhs.index.visit(this, arg);
		}
		lhs.declara =(Declaration)symTab.get(lhs.name);
		if(lhs.declara !=null)
		lhs.attrType = lhs.declara.attrType;
		if(lhs.index!=null)
		lhs.setCartesian(lhs.index.isCartesian());
		return null;
	}	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg)	throws Exception 
	{
		sink_SCREEN.attrType= TypeUtils.Type.SCREEN;
		return null;
	}

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg)throws Exception 
	{
		sink_Ident.attrType = symTab.get(sink_Ident.name).attrType;
		if(sink_Ident.attrType == TypeUtils.Type.FILE)
			return null;
		throw new SemanticException(sink_Ident.firstToken,"Sink_Ident Error");
	}

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg)throws Exception
	{try
	{
		expression_BooleanLit.attrType= TypeUtils.Type.BOOLEAN;
		return null;
	}
	catch(Exception e)
	{
		throw new SemanticException(expression_BooleanLit.firstToken,"Sink_Ident Error");
	}
		
	}

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident,Object arg) throws Exception 
	{
		//expression_Ident.
		try
		{
		expression_Ident.attrType = symTab.get(expression_Ident.name).attrType;
		}
		catch(Exception e)
		{
			throw new SemanticException(expression_Ident.firstToken,"Sink_Ident Error");
		}
		return null;
		
	}

}
