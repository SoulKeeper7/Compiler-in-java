package cop5556fa17;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556fa17.TypeUtils.Type;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression;
import cop5556fa17.AST.Expression_Binary;
import cop5556fa17.AST.Expression_BooleanLit;
import cop5556fa17.AST.Expression_Conditional;
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
import cop5556fa17.AST.Source;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;
import cop5556fa17.Scanner.Kind;
import cop5556fa17.AST.Statement_Assign;
//import cop5556fa17.image.ImageSupport;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * All methods and variable static.
	 */


	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;
	

	MethodVisitor mv; // visitor of method currently under construction
	FieldVisitor fv;
	
	
	

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;
	


	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.name;  
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		cw.visitSource(sourceFileName, null);
		// create main method
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		// initialize
		mv.visitCode();		
		//add label before first instruction
		Label mainStart = new Label();
		mv.visitLabel(mainStart);		
		createPredefinedStaticvariables();
		
		

		//CodeGenUtils.genLog(GRADE, mv, "entering main");

		// visit decs and statements to add field to class
		//  and instructions to main method, respectivley
		ArrayList<ASTNode> decsAndStatements = program.decsAndStatements;
		for (ASTNode node : decsAndStatements) {
			node.visit(this, arg);
		}

		//generates code to add string to log
		//CodeGenUtils.genLog(GRADE, mv, "leaving main");
		
		//adds the required (by the JVM) return statement to main
		mv.visitInsn(RETURN);
		
		//adds label at end of code
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		
		//handles parameters and local variables of main. Right now, only args
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);

		//Sets max stack size and number of local vars.
		//Because we use ClassWriter.COMPUTE_FRAMES as a parameter in the constructor,
		//asm will calculate this itself and the parameters are ignored.
		//If you have trouble with failures in this routine, it may be useful
		//to temporarily set the parameter in the ClassWriter constructor to 0.
		//The generated classfile will not be correct, but you will at least be
		//able to see what is in it.
		mv.visitMaxs(0, 0);
		
		//terminate construction of main method
		mv.visitEnd();
		
		//terminate class construction
		cw.visitEnd();

		//generate classfile as byte array and return
		return cw.toByteArray();
	}

	///define all the static variables
	
	private void createPredefinedStaticvariables() 
	{
		fv=cw.visitField(ACC_STATIC,"x", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		fv.visitEnd();
		// if GRADE, generates code to add string to log
		fv=cw.visitField(ACC_STATIC,"y", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		fv.visitEnd();
		
		fv=cw.visitField(ACC_STATIC,"Const0", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		fv.visitEnd();
		
		fv=cw.visitField(ACC_STATIC,"valueToduplicate", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		fv.visitEnd();
		
		fv=cw.visitField(ACC_STATIC,"DEF_X", TypeUtils.getStringType(Type.INTEGER), null, 256);
		fv.visitEnd();
		fv=cw.visitField(ACC_STATIC,"DEF_Y", TypeUtils.getStringType(Type.INTEGER), null, 256);
		fv.visitEnd();
		
		fv=cw.visitField(ACC_STATIC,"pixel_Z", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0xFFFFFF));
		fv.visitEnd();
		fv=cw.visitField(ACC_STATIC,"X", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		fv.visitEnd();
		// if GRADE, generates code to add string to log
		fv=cw.visitField(ACC_STATIC,"Y", TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		fv.visitEnd();
	}

	@Override
	public Object visitDeclaration_Variable(Declaration_Variable declaration_Variable, Object arg) throws Exception
	{
		// TODO 
		//throw new UnsupportedOperationException();
		addStaticfieldandname(declaration_Variable);		
		fv.visitEnd();		
		visitexpressionandgeneratecode(declaration_Variable, arg);
		return null;
	}

	private void visitexpressionandgeneratecode(Declaration_Variable declaration_Variable, Object arg)
			throws Exception {
		if(declaration_Variable.e!=null)
		{
			declaration_Variable.e.visit(this, arg);

			if(declaration_Variable.e.attrType.equals(TypeUtils.Type.BOOLEAN))
			{
				mv.visitFieldInsn(PUTSTATIC,className,declaration_Variable.name, TypeUtils.getStringType(Type.BOOLEAN));
			}
			else /*if(declaration_Variable.e.attrType.equals(TypeUtils.Type.INTEGER))*/
			{
				mv.visitFieldInsn(PUTSTATIC,className,declaration_Variable.name, TypeUtils.getStringType(Type.INTEGER));
			}
			/*else if(declaration_Variable.e.attrType.equals(TypeUtils.Type.IMAGE))
			{
				mv.visitFieldInsn(PUTSTATIC,className,declaration_Variable.name, TypeUtils.getStringType(Type.IMAGE));
			}*/
			
		}
	}

	private void addStaticfieldandname(Declaration_Variable declaration_Variable) {
		if(declaration_Variable.attrType.equals(TypeUtils.Type.BOOLEAN))
		{
			fv=cw.visitField(ACC_STATIC,declaration_Variable.name, TypeUtils.getStringType(Type.BOOLEAN), null, new Boolean(false));
		}
		else
		{
			fv=cw.visitField(ACC_STATIC,declaration_Variable.name, TypeUtils.getStringType(Type.INTEGER), null, new Integer(0));
		}
	}

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary, Object arg) throws Exception {
		// TODO 
		//throw new UnsupportedOperationException();
		
		//if()
		expression_Binary.e0.visit(this, arg);
		expression_Binary.e1.visit(this, arg);
		
		// start lable
		Label jumpstart = new Label();
		Label Jumpend = new Label();
		
		if(expression_Binary.op.equals(Kind.OP_EQ) ||expression_Binary.op.equals(Kind.OP_NEQ))
		{
			Kind x=expression_Binary.op;	
			if(expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER)||expression_Binary.e0.attrType.equals(TypeUtils.Type.BOOLEAN))
			{
				
				switch(x)
				{
					case OP_EQ:mv.visitJumpInsn(IF_ICMPEQ, jumpstart);
								mv.visitLdcInsn(false);		
								break;
					case OP_NEQ:mv.visitJumpInsn(IF_ICMPNE, jumpstart);
								mv.visitLdcInsn(false);
								break;
				}		
			}
			else
			{
				
				switch(x)
				{
					case OP_EQ:mv.visitJumpInsn(IF_ACMPEQ, jumpstart);
								mv.visitLdcInsn(false);	
								break;
					case OP_NEQ:mv.visitJumpInsn(IF_ACMPNE, jumpstart);
								mv.visitLdcInsn(false);
								break;
				}	
				
			}
					
		}
		else if((expression_Binary.op.equals(Kind.OP_GE) ||expression_Binary.op.equals(Kind.OP_GT)||expression_Binary.op.equals(Kind.OP_LT) ||expression_Binary.op.equals(Kind.OP_LE)) && expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER))
		{
			
			Kind x=expression_Binary.op;
			switch(x)
			{
				case OP_GE:mv.visitJumpInsn(IF_ICMPGE,jumpstart );
							mv.visitLdcInsn(false);
							break;
				case OP_GT:mv.visitJumpInsn(IF_ICMPGT,jumpstart );
							mv.visitLdcInsn(false);
							break;
				case OP_LT:mv.visitJumpInsn(IF_ICMPLT,jumpstart );
							mv.visitLdcInsn(false);
							break;
				case OP_LE:mv.visitJumpInsn(IF_ICMPLE,jumpstart );
							mv.visitLdcInsn(false);
							break;
			}
			
		}
		else if((expression_Binary.op.equals(Kind.OP_AND) ||expression_Binary.op.equals(Kind.OP_OR)) && (expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER)||expression_Binary.e0.attrType.equals(TypeUtils.Type.BOOLEAN)))
		{
			//expression_Binary.attrType = expression_Binary.e0.attrType;
			Kind x=expression_Binary.op;
				switch(x)
				{
					case OP_AND:mv.visitInsn(IAND);
							break;
					case OP_OR:mv.visitInsn(IOR);
							break;
				}
		}
		else if(((expression_Binary.op.equals(Kind.OP_DIV) ||expression_Binary.op.equals(Kind.OP_MINUS)||expression_Binary.op.equals(Kind.OP_MOD) ||expression_Binary.op.equals(Kind.OP_PLUS)||expression_Binary.op.equals(Kind.OP_POWER)||expression_Binary.op.equals(Kind.OP_TIMES)) && expression_Binary.e0.attrType.equals(TypeUtils.Type.INTEGER)))
		{
			Kind x=expression_Binary.op;
			switch(x)
			{
				case OP_DIV:mv.visitInsn(IDIV);
				break;
				case OP_MINUS:mv.visitInsn(ISUB);
				break;
				case OP_MOD:mv.visitInsn(IREM);
				break;
				case OP_PLUS:mv.visitInsn(IADD);
				break;
				//case OP_POWER:mv.visitInsn(IPOWER);
				case OP_TIMES:mv.visitInsn(IMUL);
				break;
			
			}
			//expression_Binary.attrType= TypeUtils.Type.INTEGER;
		}
		
		mv.visitJumpInsn(GOTO,Jumpend);
		mv.visitLabel(jumpstart);
		mv.visitLdcInsn(true);
		mv.visitLabel(Jumpend);
		//CodeGenUtils.genLogTOS(GRADE, mv, expression_Binary.attrType);
		return null;
	}

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary, Object arg) throws Exception {
		// TODO 
		//throw new UnsupportedOperationException();
		expression_Unary.e.visit(this, null);
		if(expression_Unary.op.equals(Kind.OP_EXCL) && (expression_Unary.e.attrType==TypeUtils.Type.BOOLEAN || expression_Unary.e.attrType==TypeUtils.Type.INTEGER))
		{			
			if(expression_Unary.e.attrType==TypeUtils.Type.BOOLEAN)
			{
			
				Label conditionend = new Label();
				Label conditionfalse = new Label();
				
				////todo pop operation
				mv.visitJumpInsn(IFEQ, conditionfalse);				
				mv.visitLdcInsn(false);
				mv.visitJumpInsn(GOTO, conditionend);				
				mv.visitLabel(conditionfalse);
				mv.visitLdcInsn(true);				
				mv.visitLabel(conditionend);
			}
			else
			{
				
				mv.visitLdcInsn(Integer.MAX_VALUE);
				mv.visitInsn(IXOR);
			}
				
		}
		else if((expression_Unary.op.equals(Kind.OP_PLUS)|| expression_Unary.op.equals(Kind.OP_MINUS))&& (expression_Unary.e.attrType==TypeUtils.Type.INTEGER ))
		{
			if(expression_Unary.op.equals(Kind.OP_MINUS))
			{
				mv.visitInsn(INEG);				
			}			
		}
		

		//CodeGenUtils.genLogTOS(GRADE, mv, expression_Unary.attrType);
		return null;
	}

	// generate code to leave the two values on the stack
	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// TODO HW6
		//throw new UnsupportedOperationException();
		index.e0.visit(this, arg);
		index.e1.visit(this, arg);
		if(!index.isCartesian())
		{
			mv.visitInsn(DUP2);
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"cart_x", RuntimeFunctions.cart_xSig,false);
			mv.visitInsn(DUP_X2);
			mv.visitInsn(POP);
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"cart_y", RuntimeFunctions.cart_ySig,false);		
			
		}		
		return null;
	}

	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		// TODO HW6
		//throw new UnsupportedOperationException();
		mv.visitFieldInsn(GETSTATIC, className, expression_PixelSelector.name, TypeUtils.getStringType(Type.IMAGE));//check if type should be image
		expression_PixelSelector.index.visit(this, arg);
		mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"getPixel", ImageSupport.getPixelSig,false);
		return null;
	}

	@Override
	public Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg)
			throws Exception {
		// TODO 
		//throw new UnsupportedOperationException();
		expression_Conditional.condition.visit(this, arg);
		//boolean x= expression_Conditional.condition.firstToken.equals(true);
		Label conditionend = new Label();
		Label conditionfalse = new Label();
		
		///todo pop operation check
		mv.visitJumpInsn(IFEQ, conditionfalse);				
		expression_Conditional.trueExpression.visit(this, arg);
		mv.visitJumpInsn(GOTO, conditionend);
		
		mv.visitLabel(conditionfalse);
		expression_Conditional.falseExpression.visit(this, arg);
		mv.visitLabel(conditionend);
		//CodeGenUtils.genLogTOS(GRADE, mv, expression_Conditional.trueExpression.attrType);
		return null;
	}


	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception
	{
		// TODO HW6
		//throw new UnsupportedOperationException();
		if(declaration_Image.attrType.equals(TypeUtils.Type.IMAGE))
		{
			fv=cw.visitField(ACC_STATIC,declaration_Image.name, ImageSupport.ImageDesc, null, null);
			fv.visitEnd();
			if(declaration_Image.source != null)
			{
				declaration_Image.source.visit(this, arg);
			
			
				if(declaration_Image.xSize != null && declaration_Image.ySize != null)
				{
					declaration_Image.xSize.visit(this, arg);
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
					declaration_Image.ySize.visit(this, arg);	
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			
				}
				else
				{
					mv.visitInsn(ACONST_NULL);
					mv.visitInsn(ACONST_NULL);
				}
			
				mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"readImage", ImageSupport.readImageSig,false);
			}
			else
			{
				if(declaration_Image.xSize != null && declaration_Image.ySize != null)
				{
					declaration_Image.xSize.visit(this, arg);
					declaration_Image.ySize.visit(this, arg);					
			
				}
				else
				{
					mv.visitFieldInsn(GETSTATIC,className,"DEF_X",TypeUtils.getStringType(Type.INTEGER));;
					mv.visitFieldInsn(GETSTATIC,className,"DEF_Y",TypeUtils.getStringType(Type.INTEGER));;
				}
				mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"makeImage", ImageSupport.makeImageSig,false);
			}
		}
		mv.visitFieldInsn(PUTSTATIC,className,declaration_Image.name, TypeUtils.getStringType(Type.IMAGE));
		return null;
	}
	
  
	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception 
	{
		// TODO HW6
		//throw new UnsupportedOperationException();
		String val = source_StringLiteral.fileOrUrl;
		mv.visitLdcInsn(val);	
		return null;
	}

	

	@Override
	public Object visitSource_CommandLineParam(Source_CommandLineParam source_CommandLineParam, Object arg)
			throws Exception {
		// TODO 	
		
		mv.visitVarInsn(ALOAD, 0); //load argument 0 on the top of the stack;
		source_CommandLineParam.paramNum.visit(this, arg); /// visits the source line command
		mv.visitInsn(AALOAD);// retreive the entry from the stack
	
		return null;
		
	}

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		// TODO HW6
		//throw new UnsupportedOperationException();
		///need to check the type of image
		mv.visitFieldInsn(GETSTATIC, className, source_Ident.name, TypeUtils.getStringType(Type.FILE));
		//string descroption
		return null;
	}


	@Override
	public Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg)
			throws Exception {
		// TODO HW6
		//throw new UnsupportedOperationException();
		fv=cw.visitField(ACC_STATIC,declaration_SourceSink.name, TypeUtils.getStringType(Type.FILE), null, null);
		fv.visitEnd();
		if(declaration_SourceSink.source!=null)
		{
			declaration_SourceSink.source.visit(this, arg);
			mv.visitFieldInsn(PUTSTATIC,className,declaration_SourceSink.name, TypeUtils.getStringType(Type.FILE));
		}
		
		return null;
	}
	


	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		// TODO 
		//throw new UnsupportedOperationException();
		int x = expression_IntLit.value;
		mv.visitLdcInsn(x); // pushes the int value on the top of the stack
		//CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);
		return null;
	}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg, Object arg) throws Exception {
		// TODO HW6
		//throw new UnsupportedOperationException();
		expression_FunctionAppWithExprArg.arg.visit(this, arg);
		switch (expression_FunctionAppWithExprArg.function)
		{
		case KW_abs:mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"abs", RuntimeFunctions.absSig,false);
		break;
		case KW_log:mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"log", RuntimeFunctions.logSig,false);
		break;
		}
		return null;
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg, Object arg) throws Exception {
		// TODO HW6
		//throw new UnsupportedOperationException();
		expression_FunctionAppWithIndexArg.arg.e0.visit(this, arg);
		expression_FunctionAppWithIndexArg.arg.e1.visit(this, arg);		
		switch(expression_FunctionAppWithIndexArg.function)
		{
		case KW_cart_x:mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"cart_x", RuntimeFunctions.cart_xSig,false);
		break;
		case KW_cart_y:mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"cart_y", RuntimeFunctions.cart_ySig,false);
		break;
		case KW_polar_r:mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"polar_r", RuntimeFunctions.polar_rSig,false);
		break;
		case KW_polar_a:mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"polar_a", RuntimeFunctions.polar_aSig,false);
		break;
		}
		return null;
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg)
			throws Exception {
		// TODO HW6
		
		//throw new UnsupportedOperationException();
		switch(expression_PredefinedName.kind)
		{
		
		case KW_a: 
			get_x_yOnTopOfStack();
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"polar_a", RuntimeFunctions.polar_aSig,false);
			break;
		case KW_r:
			get_x_yOnTopOfStack();
			mv.visitMethodInsn(INVOKESTATIC, RuntimeFunctions.className,"polar_r", RuntimeFunctions.polar_rSig,false);
			break;
		case KW_x:
			mv.visitFieldInsn(GETSTATIC,className,"x",TypeUtils.getStringType(Type.INTEGER));
			break;
		case KW_y:
			mv.visitFieldInsn(GETSTATIC,className,"y",TypeUtils.getStringType(Type.INTEGER));
			break;
		case KW_X:
			
			mv.visitFieldInsn(GETSTATIC,className,"X",TypeUtils.getStringType(Type.INTEGER));
			break;
		case KW_Y:
		
			mv.visitFieldInsn(GETSTATIC,className,"Y",TypeUtils.getStringType(Type.INTEGER));
			break;
		case KW_Z:
			
			mv.visitFieldInsn(GETSTATIC,className,"pixel_Z",TypeUtils.getStringType(Type.INTEGER));
			break;
		case KW_DEF_X:
			mv.visitFieldInsn(GETSTATIC,className,"DEF_X",TypeUtils.getStringType(Type.INTEGER));
		
			break;
		case KW_DEF_Y:
			mv.visitFieldInsn(GETSTATIC,className,"DEF_Y",TypeUtils.getStringType(Type.INTEGER));
			
		break;
		
			
		}
		
		return null;
	}

	///places the value of x and y on the top stack
	private void get_x_yOnTopOfStack() {
		mv.visitFieldInsn(GETSTATIC,className,"x",TypeUtils.getStringType(Type.INTEGER));
		mv.visitFieldInsn(GETSTATIC,className,"y",TypeUtils.getStringType(Type.INTEGER));
	}

	/** For Integers and booleans, the only "sink"is the screen, so generate code to print to console.
	 * For Images, load the Image onto the stack and visit the Sink which will generate the code to handle the image.
	 */
	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg) throws Exception {
		// TODO in HW5:  only INTEGER and BOOLEAN
		
		//statement_Out.sink.visit(this, arg);
		if(statement_Out.getDec().attrType==Type.BOOLEAN )		{
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");// Gets  the print object.
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, TypeUtils.getStringType(Type.BOOLEAN));//getting the static object
			CodeGenUtils.genLogTOS(GRADE, mv, statement_Out.getDec().attrType);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V",false);//invokes the virtual print
		}
		else if(statement_Out.getDec().attrType==Type.INTEGER) 		{
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");// Gets  the print object.
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, TypeUtils.getStringType(Type.INTEGER));// getting the static object
			CodeGenUtils.genLogTOS(GRADE, mv, statement_Out.getDec().attrType);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V",false);// invokes and prints
		}
		else if(statement_Out.getDec().attrType==Type.IMAGE)
		{
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, TypeUtils.getStringType(Type.IMAGE));// getting the static object
			CodeGenUtils.genLogTOS(GRADE, mv, statement_Out.getDec().attrType);			
			statement_Out.sink.visit(this, arg);

		}

		
		
		return null;
		// TODO HW6 remaining cases
		//throw new UnsupportedOperationException();
	}

	/**
	 * Visit source to load rhs, which will be a String, onto the stack
	 * 
	 *  In HW5, you only need to handle INTEGER and BOOLEAN
	 *  Use java.lang.Integer.parseInt or java.lang.Boolean.parseBoolean 
	 *  to convert String to actual type. 
	 *  
	 *  TODO HW6 remaining types
	 */
	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg) throws Exception 
	{		
		statement_In.source.visit(this, arg);
		if(statement_In.getDec().attrType==Type.INTEGER)
		{
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
			mv.visitFieldInsn(PUTSTATIC,className,statement_In.name, TypeUtils.getStringType(Type.INTEGER));
		}
		else if(statement_In.getDec().attrType==Type.BOOLEAN)
		{
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
			mv.visitFieldInsn(PUTSTATIC,className,statement_In.name, TypeUtils.getStringType(Type.BOOLEAN));
		}	
		else if(statement_In.getDec().attrType==Type.IMAGE)
		{
			Declaration_Image declaration_Image = (Declaration_Image) statement_In.getDec();
			if(declaration_Image.xSize != null && declaration_Image.ySize != null)
			{
				mv.visitFieldInsn(GETSTATIC,className,statement_In.name, TypeUtils.getStringType(Type.IMAGE));
				mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"getX", ImageSupport.getXSig,false);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
				mv.visitFieldInsn(GETSTATIC,className,statement_In.name, TypeUtils.getStringType(Type.IMAGE));
				mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"getY", ImageSupport.getYSig,false);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			}
			else
			{
				mv.visitInsn(ACONST_NULL);
				mv.visitInsn(ACONST_NULL);
			}
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"readImage", ImageSupport.readImageSig,false);
			mv.visitFieldInsn(PUTSTATIC,className,statement_In.name, TypeUtils.getStringType(Type.IMAGE));
			
		}
	
		return null;
	}

	
	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */


	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		//TODO  (see comment)
		
		///If LHS.Type  is INTEGER or BOOLEAN, generate code to 
       // store the value on top of the stack in variable name.
		
		if(lhs.attrType==Type.BOOLEAN || (lhs.attrType==Type.INTEGER))
		{
			if(lhs.attrType.equals(TypeUtils.Type.BOOLEAN))
			{
				mv.visitFieldInsn(PUTSTATIC,className,lhs.name, TypeUtils.getStringType(Type.BOOLEAN));
			}
			else
			{
				mv.visitFieldInsn(PUTSTATIC,className,lhs.name, TypeUtils.getStringType(Type.INTEGER));
			}

		}
		else  if(lhs.attrType==Type.IMAGE)
		{
			mv.visitFieldInsn(GETSTATIC,className,lhs.name,TypeUtils.getStringType(Type.IMAGE));
			get_x_yOnTopOfStack();		
			mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"setPixel", ImageSupport.setPixelSig,false);//invokes the virtual print			
		
		}
		
		return null;
	
	}
	

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception {
		//TODO HW6
		//throw new UnsupportedOperationException();
		mv.visitMethodInsn(INVOKESTATIC, ImageFrame.className,"makeFrame", ImageSupport.makeFrameSig,false);//invokes the virtual print
		//mv.visitLdcInsn(ImageSupport.JFrameDesc);
		mv.visitInsn(POP);
		return null;

	}

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception {
		//TODO HW6
		//throw new UnsupportedOperationException();
		mv.visitFieldInsn(GETSTATIC, className, sink_Ident.name, TypeUtils.getStringType(Type.FILE));
		mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"write",ImageSupport.writeSig,false);//invokes the virtual print	
		///check thetype
		return null;
		
	}

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception
	{
		boolean bool = expression_BooleanLit.value;
		mv.visitLdcInsn(bool);		
		//CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);
		return null;
		
	}

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident,
			Object arg) throws Exception {
		
		String name= expression_Ident.name;
		switch(expression_Ident.attrType)
		{
			case BOOLEAN: mv.visitFieldInsn(GETSTATIC, className, name, TypeUtils.getStringType(Type.BOOLEAN));
						break;
			case INTEGER:mv.visitFieldInsn(GETSTATIC, className, name, TypeUtils.getStringType(Type.INTEGER));
						break;
			case IMAGE:throw new UnsupportedOperationException();
						
			case URL:throw new UnsupportedOperationException();
						
			case FILE:throw new UnsupportedOperationException();
			
			case SCREEN:throw new UnsupportedOperationException();		
		}	
	
		//CodeGenUtils.genLogTOS(GRADE, mv, expression_Ident.attrType);
		return null;
	}

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign, Object arg) throws Exception
	{	
		if(statement_Assign.lhs.attrType.equals(TypeUtils.Type.INTEGER)||statement_Assign.lhs.attrType.equals(TypeUtils.Type.BOOLEAN))
		{
			statement_Assign.e.visit(this, arg);
			statement_Assign.lhs.visit(this, arg);
		}
	else if(statement_Assign.lhs.attrType.equals(TypeUtils.Type.IMAGE))
	{
		
		
		Label  innerStart = new Label();
		Label  innerEnd = new Label();
		Label  OuterStart = new Label();
		Label  OuterEnd = new Label();
		
		
		
		mv.visitFieldInsn(GETSTATIC,className,statement_Assign.lhs.name, TypeUtils.getStringType(Type.IMAGE));
		mv.visitFieldInsn(GETSTATIC,className,statement_Assign.lhs.name, TypeUtils.getStringType(Type.IMAGE));
	
		mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"getX", ImageSupport.getXSig,false);
		mv.visitFieldInsn(PUTSTATIC,className,"X", TypeUtils.getStringType(Type.INTEGER));
		mv.visitMethodInsn(INVOKESTATIC, ImageSupport.className,"getY", ImageSupport.getYSig,false);
		mv.visitFieldInsn(PUTSTATIC,className,"Y", TypeUtils.getStringType(Type.INTEGER));
		
		
		
		mv.visitFieldInsn(GETSTATIC,className,"Const0", TypeUtils.getStringType(Type.INTEGER));
		mv.visitLabel(OuterStart);
		
		Duplicatethetopvalue3times();
		mv.visitFieldInsn(PUTSTATIC,className,"x", TypeUtils.getStringType(Type.INTEGER));
		mv.visitFieldInsn(GETSTATIC,className,"X", TypeUtils.getStringType(Type.INTEGER));		
		mv.visitJumpInsn(IF_ICMPEQ, OuterEnd);
		mv.visitFieldInsn(GETSTATIC,className,"Const0", TypeUtils.getStringType(Type.INTEGER));
		mv.visitLabel(innerStart);
		Duplicatethetopvalue3times();
		
		mv.visitFieldInsn(PUTSTATIC,className,"y", TypeUtils.getStringType(Type.INTEGER));
		mv.visitFieldInsn(GETSTATIC,className,"Y", TypeUtils.getStringType(Type.INTEGER));		
		mv.visitJumpInsn(IF_ICMPEQ, innerEnd);		
		statement_Assign.e.visit(this, arg);
		statement_Assign.lhs.visit(this, arg);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);	
		mv.visitJumpInsn(GOTO, innerStart);
		
		mv.visitLabel(innerEnd);
		mv.visitInsn(POP);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitJumpInsn(GOTO, OuterStart);
		mv.visitLabel(OuterEnd);
		mv.visitInsn(POP);
		
		}
		return null;
	}
	
	/// value to duplicate for while loop
	private void Duplicatethetopvalue3times()
	{
		mv.visitFieldInsn(PUTSTATIC,className,"valueToduplicate", TypeUtils.getStringType(Type.INTEGER));
		mv.visitFieldInsn(GETSTATIC,className,"valueToduplicate", TypeUtils.getStringType(Type.INTEGER));
		mv.visitFieldInsn(GETSTATIC,className,"valueToduplicate", TypeUtils.getStringType(Type.INTEGER));
		mv.visitFieldInsn(GETSTATIC,className,"valueToduplicate", TypeUtils.getStringType(Type.INTEGER));
	}

}
