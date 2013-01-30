package fr.cavalier.netcheck.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>Acces XML generique et gestion des exceptions</p>
 * @author malika 
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao XmlParser.java 
 */
public class XmlParser {

	protected Document doc;
	protected String input;
	protected String output;
	protected DefaultHandler gestionnaire;
	
	/**
	 * <p> Methode en charge de l'ouverture et extraction du fichier xml </p>
	 */
	public void initializeFromFile (){
		DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			File f = new File("src/main/resources/"+input+".xml");
			DocumentBuilder builder =
					factory.newDocumentBuilder();
			if (!f.exists()) {
				document = builder.newDocument();
			} else {
				document = builder.parse(f);
			}
		} 
		catch (SAXParseException spe) {
			// Error generated by the parser
			System.out.println("\n** Parsing error"
					+ ", line " + spe.getLineNumber()
					+ ", uri " + spe.getSystemId());
			System.out.println("  " + spe.getMessage() );

			// Use the contained exception, if any
			Exception x = spe;
			if (spe.getException() != null)
				x = spe.getException();
			x.printStackTrace();
		}
		catch (SAXException sxe) {
			// Error generated by this application
			// (or a parser-initialization error)
			Exception x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();
		}
		catch (ParserConfigurationException pce) {
			// Parser with specified options 
			// cannot be built
			pce.printStackTrace();
		}
		catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}
		doc=document;
	}

	/**
	 * <p>Enregistre les modifications de fichier xml</p>
	 */
	public void transform(){
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			FileOutputStream oFile= new FileOutputStream("src/main/resources/"+output+".xml");
			StreamResult result = new StreamResult(oFile);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Initialise le parseur sax avec le gestionnaire adequat</p>
	 */
	public void parse(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parseur =factory.newSAXParser();
			File fichier =new  File("src/main/resources/"+input+".xml");
			if (fichier.exists()) {
				parseur.parse(fichier, gestionnaire);
			} else {
				System.out.println("Le fichier source n'existe pas, le document est généré vide");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public DefaultHandler getGestionnaire() {
		return gestionnaire;
	}

	public void setGestionnaire(DefaultHandler gestionnaire) {
		this.gestionnaire = gestionnaire;
	}
	
	public void cleanDocument() {
		if (this.doc != null) {
			this.doc.removeChild(this.doc.getFirstChild());
		}
	}
	
	
}
