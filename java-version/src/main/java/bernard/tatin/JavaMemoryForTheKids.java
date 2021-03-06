package bernard.tatin;

import bernard.tatin.common.Counter;
import bernard.tatin.procfs.ProcessCommandLine;
import bernard.tatin.procfs.ProcessID;
import bernard.tatin.procfs.StatM;
import bernard.tatin.threads.AThConsumer;
import bernard.tatin.threads.ThPrinter;
import bernard.tatin.threads.ThPrinterClient;
import bernard.tatin.tools.ThMemoryFiller;

/**
 * Hello world!
 */
class JavaMemoryForTheKids extends ThPrinterClient {
   private static String titleLine = null;
   private final Counter count = new Counter(25);

   public static void stopAll() {
       // stop other threads
       AThConsumer.isRunning.set(false);
       // wait a little
       try {
           Thread.sleep(400);
       } catch (Exception e) {
           // don't use printError, ThPrinter is stopped
           System.err.println("Sleep interrupted...");
       }
   }
    /**
     * @param args command line args
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // System signals handled
            // it works, but I don't like this
            JavaMemoryForTheKids.stopAll();
            // don't use printString, ThPrinter is stopped
            System.out.println("Signal caught, interrupt received, exit...");
         }));

      JavaMemoryForTheKids jm = new JavaMemoryForTheKids();
        // initialize and run threads
      ThPrinter.getMainInstance().initialize();
      ThMemoryFiller.getMainInstance().initialize();

      jm.innerLoop();
   }

   private void showTitle() {
      if (titleLine == null) {
         String aTitle = StatM.getMainInstance().getStatsTitleLine();
         if (aTitle != null) {
            titleLine = aTitle;
         } else {
            titleLine ="titleLine is NULLLLLL";
         }
      }

      printStrings(
                new String[] {"PID          " + String.valueOf(ProcessID.getPID()),
                        "Command line " + ProcessCommandLine.getCommandLine(),
                        titleLine});
   }

   private void innerLoop() {
      while (true) {
         long memorySize = ThMemoryFiller.getMainInstance().getMemorySize();
         String aString = StatM.getMainInstance().getStatsLine(memorySize);

         if (count.getValue() == 0) {
            showTitle();
         }

         if (aString != null) {
            printString(aString);
         } else {
            printError("ERROR reading statm file");
         }
         try {
            Thread.sleep(100L);
         } catch (InterruptedException e) {
            printError("ERROR InterruptedException : " + e.getMessage());
         }
      }
   }
}
