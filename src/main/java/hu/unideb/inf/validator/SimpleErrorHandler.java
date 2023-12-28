package hu.unideb.inf.validator;

import java.io.PrintWriter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler extends XSDErrorHandler {
   private PrintWriter out;

   public SimpleErrorHandler(boolean failOnError, PrintWriter out) {
      super(failOnError);
      this.out = out;
   }

   public SimpleErrorHandler(boolean failOnError) {
      this(failOnError, new PrintWriter(System.err, true));
   }

   public SimpleErrorHandler() {
      this(false);
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
      this.printException("Fatal error", e);
      this.hasErrors = true;
      throw e;
   }

   private void printException(String prefix, SAXParseException e) {
      this.out.format("[%s] %s:%d:%d: %s%n", prefix, e.getSystemId(), e.getLineNumber(), e.getColumnNumber(), e.getMessage());
   }
}