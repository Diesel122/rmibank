// MWM, a logger for debugging! 

package rmi.bank;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * A very, very simple logger class for debugging
 */
public class LogHelper {

   ////// Component objects //////

   // A Log to be used in this package...
   private static final Logger LOG = Logger.getLogger("cscie55.project");
   private static boolean initialized;

   protected static void init() {

      assert LOG != null : "Unable to obtain logger!";

      // Set basic init values, could be expanded to read
      // a config file, using standard Java logger functions
      // or this entire class could be replaced by Log4J

      // For now, log to the console
      ConsoleHandler handler = new ConsoleHandler();
      SimpleFormatter format = new SimpleFormatter();
      handler.setFormatter(format);
      // We set the handler's set level to be FINEST, but the controlling
      // level of messages to display is set by LOG.setLevel below.
      handler.setLevel(Level.FINEST);
      LOG.addHandler(handler);
      LOG.setUseParentHandlers(false); // No need to propagate up logged items.

      // By default, the LOG is set to log INFO and above;
      // Uncomment to activate logging to the console
      //LOG.setLevel(Level.FINER);
      initialized = true;
      finer ("LogHelper: Logging turned on!");
   }

   ////// Member functions //////

   // Note: More functions to log other levels to be added if needed.

   /**
   * <code>logInfo</code>
   * Log a message of Level.INFO
   *
   *  @param
   *     msg the string to log
   */
   protected static void info(String msg) {
      if (initialized == false) init();
      LOG.log(Level.INFO, msg + "\n");
   }

   /**
   * <code>warn</code>
   * Log a message of Level.WARN
   *  @param
   *     msg the string to log
   */
   protected static void warn(String msg) {
      if (initialized == false) init();
      LOG.log(Level.WARNING, msg + "\n");
   }


   /**
   * <code>logFine</code>
   * Log a message of Level.FINE
   *
   *  @param
   *     msg the string to log
   */
   protected static void fine(String msg) {
      if (initialized == false) init();
      LOG.log(Level.FINE, msg + "\n");
   }

   /**
   * <code>logFiner</code>
   * Log a message of Level.FINER
   *
   *  @param
   *     msg the string to log
   */
   protected static void finer(String msg) {
      if (initialized == false) init();
      LOG.log(Level.FINER, msg + "\n");
   }
}

