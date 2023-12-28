package hu.unideb.inf.validator;

import org.xml.sax.ErrorHandler;

public abstract class XSDErrorHandler implements ErrorHandler {
   protected boolean failOnError;
   protected boolean hasErrors;

   public XSDErrorHandler() {
      this(false);
   }

   public XSDErrorHandler(boolean failOnError) {
      this.failOnError = false;
      this.hasErrors = false;
      this.failOnError = failOnError;
   }

   public boolean hasErrors() {
      return this.hasErrors;
   }

   public void close() {
   }
}