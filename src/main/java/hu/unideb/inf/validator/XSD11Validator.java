package hu.unideb.inf.validator;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.xerces.jaxp.validation.XMLSchema11Factory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;

@Command(
   name = "XSD11Validator",
   version = {"XSD11Validator 1.0"},
   mixinStandardHelpOptions = true
)
public class XSD11Validator {
   @Option(
      names = {"-x", "--xml"},
      description = {"XML results file"}
   )
   String xmlResults;
   @Option(
      names = {"-d", "--dont-fail-on-error"},
      description = {"Exit without a failure on validation errors"}
   )
   boolean dontFailOnError;
   @Parameters(
      index = "0..1",
      description = {"[schema] instance"}
   )
   String[] files;

   public static void main(String[] args) {
      XSD11Validator validator = new XSD11Validator();
      CommandLine commandLine = new CommandLine(validator);
      commandLine.parseArgs(args);
      if (validator.files.length >= 1 && validator.files.length <= 2) {
         System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/XML/XMLSchema/v1.1", "org.apache.xerces.jaxp.validation.XMLSchema11Factory");

         try {
            new XMLSchema11Factory();
            SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1");
            sf.setErrorHandler(new SimpleErrorHandler(true));
            Schema schema = validator.files.length == 1 ? sf.newSchema() : sf.newSchema(new StreamSource(validator.files[0]));
            StreamSource instance = new StreamSource(validator.files.length == 1 ? validator.files[0] : validator.files[1]);
            XSDErrorHandler errorHandler = null;
            if (validator.xmlResults != null) {
               errorHandler = new XMLErrorHandler(validator.xmlResults, false);
            } else {
               errorHandler = new SimpleErrorHandler(false);
            }

            Validator schemaValidator = schema.newValidator();
            schemaValidator.setErrorHandler((ErrorHandler)errorHandler);
            schemaValidator.validate(instance);
            if (validator.xmlResults != null) {
               ((XSDErrorHandler)errorHandler).close();
            }

            int exitCode = -1;

            if (((XSDErrorHandler)errorHandler).hasErrors()) {
               if (validator.dontFailOnError) {
                  System.out.println("Errors were raised in processing. Exiting without errors due to the dontFailOnError flag.");
                  exitCode = 0;
               } else {
                  System.out.println("Errors were raised in processing. Exiting with failures.");
                  exitCode = 1;
               }
            } else {
               System.out.println("Validation succeeded with no errors.");
               exitCode = 0;
            }

            System.exit(exitCode);
         } catch (SAXParseException var9) {
         } catch (Exception var10) {
            System.err.println(var10.getMessage());
         }

      } else {
         throw new ParameterException(commandLine, "[schema] instance - an instance should be specified and optionally the schema.");
      }
   }
}