package paint;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import paint.Log.LogSelection;
import paint.Log.OpenLog;
import paint.Log.SaveLog;

public class Log {
    private static final Logger logger = Logger.getLogger(PainT.class.getName());

    public void LoggerFunc() throws IOException{
        // Construct a default FileHandler.
      // "%t" denotes the system temp directory, kept in environment variable "tmp"
      Handler fh = new FileHandler("./logger.log", true);  // append is true
//    fh.setFormatter(new SimpleFormatter());  // Set the log format
      // Add the FileHandler to the logger.
      logger.addHandler(fh);
      // Set the logger level to produce logs at this level and above.
      logger.setLevel(Level.FINE);
       
    }
    
    /**
    *
    * @param selectedTool = Tool that's selected
    */
   public static void tLogS(String selectedTool, CheckSelected cS){
       Thread logSelection = new Thread(new Log().new LogSelection(selectedTool, cS));
       logSelection.setDaemon(true);
       logSelection.start();
   }
   
   public static void oThread(String fileName){
       Thread openLog = new Thread(new Log().new OpenLog(fileName));
       openLog.start();
   }

   public static void slThread(File outputFile){
       Thread saveLog = new Thread(new Log().new SaveLog(outputFile.getName()));
               saveLog.start();
   }
    
    public class OpenLog implements Runnable{
        String fileName;

        /**
         *
         * @param fileName
         */
        public OpenLog(String fileName){
            this.fileName = fileName;
        }

        @Override
        public void run() {
           
            while(fileName == null){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
           
            logger.info(fileName + " opened.");
        }   
    }
	/**
	 *
	 */
	public class SaveLog implements Runnable{
	    String fileName;

	    /**
	     *
	     * @param fileName = Name of the file
	     */
	    public SaveLog(String fileName){
	        this.fileName = fileName;
	    }

	    @Override
	    public void run() {
	       
	        while(fileName == null){
	            try{
	                Thread.sleep(1000);
	            } catch (InterruptedException ex) {
	                logger.log(Level.SEVERE, null, ex);
	            }
	        }
	       
	        logger.info(fileName + " saved.");
	    }
	    
	    
	}
	
	public class LogSelection implements Runnable{
	    String toolLabel;
	    CheckSelected cS;
	    public LogSelection(String toolLabel, CheckSelected cS){
	        this.toolLabel = toolLabel;
	        this.cS = cS;
	    }

	    @Override
	    public void run() {
	        long startTimer = System.currentTimeMillis();
	        while(cS.selectedTool.equals(toolLabel)){
	            try{
	                Thread.sleep(100);
	            } catch (InterruptedException ex) {
	                logger.log(Level.SEVERE, null, ex);
	            }
	        }
	        long stopTimer = System.currentTimeMillis() - startTimer;
	        logger.info(toolLabel + " for " + stopTimer + " ms.");
	    }
	}

}


