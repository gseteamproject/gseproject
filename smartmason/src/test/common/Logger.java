/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A.

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation,
version 2.1 of the License.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package test.common;

import java.io.*;

import test.common.xml.FunctionalityDescriptor;
import test.common.HTMLManager;


/**
 * @author Giovanni Caire - TILAB
 * @author Alessandro Negri - AOTLab UniPR
 * @author Matteo Bisi - UniPR
 * @author Yuri Ferrari - UniPR
 * @author Rossano Vitulli - UniPR
 * @version $Date: December 2003
 */

/**
 * This class contains methods and fields for handling the log process
 */
public class Logger {
    // Create a single instance of this class
    static Logger theLogger = new Logger();

    // Default log file name
    private static File logFile;

    // Default PrintStream is standard output
    private static PrintStream out = System.out;
    // Progressive value to set log file name
    private static int nLog = 0;

    public static final int CONSOLE_LOGGER=0;
    public static final int TXT_LOGGER=1;
    public static final int HTML_LOGGER=2;
    // Deafult system logger is the Console file
    private static int currentType=CONSOLE_LOGGER;
    //Available types of loggers
    public static final String[] typeStringArray = {
        "Console",
        "Txt File",
        "HTML File"
    };
    // Current test in execution
    public static String loggedCurrentTest;


        /**
         * Simple print a string: if current logger system is set to "txt file" or "console",
         * the string is print on out, otherwise (HTML) it's append in Body section
         * @param s The string to be printed
         */
        public void simpleLog(String s) {
            switch (currentType) {
                case TXT_LOGGER :
                case CONSOLE_LOGGER : out.println(s); break;
                case HTML_LOGGER : HTMLManager.addBody(s + "<BR/>\n"); break;
            }
        }

        /**
         * Simple print a string with a CR
         * @param s The string to be printed
         */
        public void log(String s) {
            simpleLog(s);
        }

        /**
         * Print the log.
         * @param obj The object to be printed: there must be an obj.ToString() method defined.
         */
        public void log(Object obj) {
            String message = obj.toString();
            log(message);
        }

        /**
         * Simply print a line without CR.
         * @param s The line to be printed.
         */
        public void logString(String s) {
            switch (currentType) {
                case TXT_LOGGER :
                case CONSOLE_LOGGER : out.print(s); break;
                case HTML_LOGGER : HTMLManager.addBody(s); break;
            }
        }

        /**
         * Print the stack trace of an exception.
         * @param t The throwable object representing the exception.
         */
        public void logStackTrace(Throwable t) {

            switch (currentType) {
                case TXT_LOGGER :
                case CONSOLE_LOGGER : t.printStackTrace(out); break;
                case HTML_LOGGER :
                    HTMLManager.addBody(t.toString()+"\n");
                    break;
            }

            StackTraceElement ste[] = t.getStackTrace();

            if (currentType==HTML_LOGGER)
                    HTMLManager.addBody("<BLOCKQUOTE>\n");
            for(int i=0;i<ste.length;i++) {
                if (currentType==HTML_LOGGER)
                    HTMLManager.addBody("\t"+ste[i].toString() + "<BR/>\n");
            }
            if (currentType==HTML_LOGGER)
                    HTMLManager.addBody("</BLOCKQUOTE>\n");

        }

        /**
         * Get the class single instance.
         */
	public static Logger getLogger() {
		return theLogger;
	}

    /**
     * Set the Logger to print on a file.
     * @param fileType The type of the new file
     */
	public static void setFileLogger(int fileType) {
            currentType=fileType;
            switch(currentType) {
                case TXT_LOGGER: {
                    logFile = new File("log000"+ ".txt");
                    nLog=0;
                    try {
                        while (logFile.exists()) {
                        nLog++;
                        logFile = new File("log00" + nLog + ".txt");
                    }
                    out = new PrintStream(new FileOutputStream(logFile));
                    }
                    catch (Exception e){
                        System.out.println("Error writing to TXT log file");
                    } }
                    break;
                case HTML_LOGGER: {
                    logFile = new File("log000"+ ".html");
                    nLog=0;
                    try {
                        while (logFile.exists()) {
                        nLog++;
                        logFile = new File("log00" + nLog + ".html");
                    }
                    out = new PrintStream(new FileOutputStream(logFile));
                    }
                    catch (Exception e){
                        System.out.println("Error writing to HTML log file");
                    } }
                    HTMLManager.init(logFile.toString());
                    break;
            }    
	}

        /**
         * Set the Logger for printing on standard output.
         */
        public static void setTextLogger() {
            currentType=CONSOLE_LOGGER;
            out = System.out;
        }

        /** 
         * Close the current opened logger
         */
        public void closeLogger() {
            if(currentType==HTML_LOGGER) HTMLManager.buildFile(out);
            out.close();
        }

        /** 
         * Return the index of current logger system
	 */
        public static int getLoggerType() {
            return currentType;
        }

       /** 
        * Return current logger system
        */
        public static String getLoggerStringType() {
            return typeStringArray[currentType];
        }

        /**
         * Return a counter of the blocks in body section: each block identifies a Run
         * or RunAll execution
         */
        public static int getBlockCounter() {
            return HTMLManager.getBlockCounter();
        }
        
        /** Increments the counter of the blocks in body section */
        public static void incBlockCounter() {
            HTMLManager.incBlockCounter();
        }

        /**
         * Append string title and testerName to the index section of html file
         * with the needed tags
         */
        public static void addIndex(String title, String testerName) {
            if(!title.equals("")) HTMLManager.addIndex(
                "  <TR><TD BGCOLOR=\"#AEAEFF\" COLSPAN=\"3\" HEIGHT=\"2\"></TD></TR>\n");
            HTMLManager.addIndex(
                "  <TR>\n"+
                "    <TD BGCOLOR=\"#EEEEFF\" WIDTH=\"200\">"+title+"</TD>\n"+
                "    <TD BGCOLOR=\"#FFFFFF\"><a href=\"#a"+HTMLManager.getBlockCounter()+"\">"+testerName+"</a></TD>\n");
        }
        
        /**
         * Append string title and testerName to the index section of html file
         * with the needed tags
         */
        public static void addIndexSummary(String summary) {
            HTMLManager.addIndex(
                "    <TD BGCOLOR=\"#F7F7FF\" WIDTH=\"200\">"+summary+"</TD>\n"+
                "  </TR>\n" );
        }
        
        /**
	 * Append string s to body section of html file
	 */
        public static void addBody(String s) {
            HTMLManager.addBody(s);
        }

        /**
	 * Append the heading of a run test group to body section of html file
         * @param testerName The tester agent name
	 */
        public static void addBodyHeading(String testerName) {
            HTMLManager.addBody(
            "\n<P>\n"+
            "<TABLE BORDER=\"0\" CELLPADDING=\"10\" CELLSPACING=\"0\">\n"+
            "  <TR>\n"+
            "    <TD BGCOLOR=#D0D0FF><FONT SIZE=\"-1\"><A HREF=\"#\">BACK TO TOP<A></FONT></TD>\n"+
            "    <TD BGCOLOR=#A0A0FF><FONT SIZE=\"+2\"><B><A name=\"a"+HTMLManager.getBlockCounter()+"\">"+testerName+"</a></B></FONT></TD>\n"+
            "  </TR>\n"+
            "</TABLE>\n"+
            "<P>\n");
            
        }
        
        /**
	 * Append the "back to top" string to body section of html file
	 */
        public static void addBodyCloser() {
            HTMLManager.addBody(
                "<P>\n"+
                "<TABLE BORDER=\"0\" CELLPADDING=\"10\" CELLSPACING=\"0\">\n"+
                "  <TR>\n"+
		"    <TD BGCOLOR=#D0D0FF><FONT SIZE=\"-1\"><A HREF=\"#\">BACK TO TOP<A></FONT></TD>\n"+
                "  </TR>\n"+
                "</TABLE>\n"+
                "<HR>\n"+
                "<P>\n\n");
        }
        
        /** 
         * Delete the current logger file
         */
        public static void deleteLogger() {
            switch (currentType) {
                case TXT_LOGGER : out.close(); 
                                  logFile.delete(); break;
                case CONSOLE_LOGGER : break;
                case HTML_LOGGER : break;
            }
        }
}
