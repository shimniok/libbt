/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.botthoughts;

/** A simple framework for println debugging
 *
 * @author Michael Shimniok
 */
public class Debug extends Throwable {
    private static boolean on=true;
    private static int level = 0;
    public static Debug debug = new Debug();

    /** turns debugging on/off
     *
     * @param isOn enables debugging if true, disables otherwise
     */
    public static void setEnable(boolean isOn) {
        Debug.on = isOn;
    }

    /** set debugging level
     *
     * @param newLevel is the new debugging level
     */
    public static void setLevel(int newLevel) {
        Debug.level = newLevel;
    }

    /** display debug message
     *
     * @param message is the message to display
     */
    public static void println(String message) {
        if (on) {
            StackTraceElement st = new Throwable().fillInStackTrace().getStackTrace()[1];
            String theMethod = st.getMethodName();
            String theClass = st.getClassName();
            int theLine = st.getLineNumber();
            String theFile = st.getFileName();

            System.out.println(theFile + ":"+ theLine + " " + theClass + "." + theMethod + "(): " + message);
        }
    }
}
