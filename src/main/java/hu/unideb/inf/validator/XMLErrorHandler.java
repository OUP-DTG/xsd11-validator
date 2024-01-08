package hu.unideb.inf.validator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLErrorHandler extends XSDErrorHandler {
   private PrintWriter out;

   public XMLErrorHandler(String xmlResultFile, boolean failOnError) throws IOException {
      super(failOnError);
      File targetFile = new File(xmlResultFile);
      FileWriter fileWriter = new FileWriter(targetFile);
      this.out = new PrintWriter(fileWriter, true);
      this.out.println("<results name=\"LeXML2 schema validation results\">");
   }

   public void warning(SAXParseException e) throws SAXException {
      this.printException("Warning", e);
   }

   public void error(SAXParseException e) throws SAXException {
      this.printException("Error", e);
      this.hasErrors = true;
      if (this.failOnError) {
         throw e;
      }
   }

   public void fatalError(SAXParseException e) throws SAXException {
      this.printException("Fatal Error", e);
      this.hasErrors = true;
      throw e;
   }

   private void printException(String prefix, SAXParseException e) {
      this.out.println("\t<result name=\"" + prefix + "\">");
      this.out.format("\t\t%d:%d|%s%n", e.getLineNumber(), e.getColumnNumber(), e.getMessage());
      this.out.println("\t</result>");
   }

   public void close() {
      this.out.println("</results>");
   }
}