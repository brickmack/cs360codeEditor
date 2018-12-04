/*
 * LanguageBuilder:
 * 
 * LanguageBuilder is not part of the editor itself, it is a tool for creating Language configuration
 * files which can be separately distributed. It produces a .lng file which is a serialized Language object, along
 * with all the objects Language contains (InsertableCode, HighlightRule). Language objects can be programmatically
 * created here and then imported by the editor itself. Currently, this code is configured to output .lng files for
 * Java and C
 * 
 * NOTE: LANGUAGEBUILDER MUST BE EXECUTED AFTER ANY CHANGE TO THE LANGUAGE CLASS
 */

package cs360ProjectImplementation;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LanguageBuilder {
	private static Color darkGreen = new Color(18, 119, 2);
	private static Color purple = new Color(90, 2, 119);
	
	private static String path = "languages/"; //folder to put the language file in. MAKE SURE THIS IS APPROPRIATELY SET
	
	public static void main(String[] args) {
		Language java = generateJava();
		
		//serialization
        serialize(java);
        
        Language c = generateC();
        serialize(c);
        
        //test the deserialization. Not necessary, purely for debugging
		//deserializeTest(filename);
	}
	
	public static Language generateC() {
		String cKeysString = "\\bauto\\b|\\bbreak\\b|\\bcase\\b|\\bchar\\b|\\bconst\\b|\\bcontinue\\b|\\bdefault\\b|\\bdo\\b|\\bdouble\\b|\\belse\\b|\\benum\\b|\\bextern\\b|\\bfloat\\b|\\bfor\\b|\\bgoto\\b|\\bif\\b|\\bint\\b|\\blong\\b|\\bregister\\b|\\breturn\\b|\\bshort\\b|\\bsigned\\b|\\bsizeof\\b|\\bstatic\\b|\\bstruct\\b|\\bswitch\\b|\\btypedef\\b|\\bunion\\b|\\bunsigned\\b|\\bvoid\\b|\\bvolatile\\b|\\bwhile\\b";
		HighlightRule cKeywords = new HighlightRule("keywords", new String[] {cKeysString}, purple, false);
		
		InsertableCode cIfBlock = new InsertableCode("If", "if ( ) {", "}");
		InsertableCode cWhileBlock = new InsertableCode("While", "while ( ) {", "}");
		InsertableCode cIfElseBlock = new InsertableCode("If Else", "if ( ) {\n}\nelse {", "}");
		InsertableCode cDoWhileBlock = new InsertableCode("Do While", "do{ ", "}while ( )");
		
		return new Language("C", new HighlightRule[] {cKeywords}, "c", new InsertableCode[] {cIfBlock, cWhileBlock,cIfElseBlock,cDoWhileBlock});
	}
	
	public static Language generateJava() {
		String javaKeysString = "\\babstract\\b|\\bassert\\b|\\bboolean\\b|\\bbreak\\b|\\bbyte\\b|\\bcase\\b|\\bcatch\\b|\\bchar\\b|\\bclass\\b|\\bconst\\b|\\bcontinue\\b|\\bdefault\\b|\\bdo\\b|\\bdouble\\b|\\belse\\b|\\bextends\\b|\\bfalse\\b|\\bfinal\\b|\\bfinally\\b|\\bfloat\\b|\\bfor\\b|\\bgoto\\b|\\bif\\b|\\bimplements\\b|\\bimport\\b|\\binstanceof\\b|\\bint\\b|\\binterface\\b|\\blong\\b|\\bnative\\b|\\bnew\\b|\\bnull\\b|\\bpackage\\b|\\bprivate\\b|\\bprotected\\b|\\bpublic\\b|\\breturn\\b|\\bshort\\b|\\bstatic\\b|\\bstrictfp\\b|\\bsuper\\b|\\bswitch\\b|\\bsynchronized\\b|\\bthis\\b|\\bthrow\\b|\\bthrows\\b|\\btransient\\b|\\btrue\\b|\\btry\\b|\\bvoid\\b|\\bvolatile\\b|\\bwhile\\b";
		HighlightRule javaKeywords = new HighlightRule("keywords", new String[] {javaKeysString}, purple, false);
		HighlightRule javaStringDef = new HighlightRule("string", new String[] {"(\"([^\"]*)\")"}, Color.blue, false);
		HighlightRule javaCommentDef = new HighlightRule("comment", new String[] {"((?m)//(.*)$)|((?s)/\\*(.*?)\\*/)"}, darkGreen, true);
		
		InsertableCode javaIfBlock = new InsertableCode("If", "if ( ) {", "}");
		InsertableCode javaIfElseBlock = new InsertableCode("If Else", "if ( ) {\n}\nelse {", "}");
		
		InsertableCode javaWhileBlock = new InsertableCode("While", "while ( ) {", "}");
		InsertableCode javaDoWhileBlock = new InsertableCode("Do While", "do{ ", "}while ( )");
		
		return new Language("Java", new HighlightRule[] {javaKeywords, javaCommentDef, javaStringDef}, "java", new InsertableCode[] {javaIfBlock, javaIfElseBlock, javaWhileBlock, javaDoWhileBlock});
	}
	
	public static void serialize(Language lang) {
		String fileName = path + lang.getName() + ".lng";
		
		try {
			//saving object into file
			FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            
            //Method for serialization of object
            out.writeObject(lang);
            
            out.close();
            file.close();
            
            System.out.println("success serialized");
		}
		catch (IOException ex) {
            System.out.println(ex.toString());
		}
	}
	
	public static void deserializeTest(String fileName) {
		//this is used only to validate that the file was successfully generated
		
		Language demo = null;
		try {
			//read object from file
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);
			
			//method for deserialization of an object
			demo = (Language) in.readObject();
			
			in.close();
			file.close();
			
			System.out.println("Successfully deserialized");
			System.out.println(demo.getName());
			System.out.println(demo.getRules()[0].getDefinition()[0]);
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
		catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());
		}
	}
}