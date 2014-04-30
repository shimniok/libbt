/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.botthoughts;

/**
 *
 * @author Michael Shimniok
 */
public class PlatformUtilities {

    /** set to true if platform is Windows */
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    /** set to true if platform is OSX */
    private static boolean isOSX = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
    /** set to true if platform is Linux */
    private static boolean isLinux = System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;

    public static String getOS() {
        String os = "";
        if (isWindows()) {
            os = "Windows";
        } else if (isOSX()) {
            os = "OSX";
        } else if (isLinux()) {
            os = "Linux";
        }

        return os;
    }

    public static boolean isWindows() {
        return isWindows;
    }

    public static boolean isOSX() {
        return isOSX;
    }

    public static boolean isLinux() {
        return isLinux;
    }

    /** return the basename of a file (extension removed)
     *
     * @param filename is the name of a file as a String
     * @return the basename of the file with extension removed
     */
    public static String getBasename(String filename) {
        String result="";

        if (filename != null && !filename.equals("")) {
            int lastDot = filename.lastIndexOf(".");
            if (lastDot >= 0)
                result = filename.substring(0, lastDot);
            else
                result = filename;
        }

        return result;
    }

    /** extract the extension of a file string
     *
     * @param filename is the name of a file
     * @return the extension (e.g., ".txt") or "" if no extension present
     */
    public static String getExtension(String filename) {
        String result="";

        if (filename != null && !filename.equals("")) {
            int lastDot = filename.lastIndexOf(".");
            if (lastDot >= 0)
                result = filename.substring(lastDot, filename.length());
        }

        return result;
    }


}