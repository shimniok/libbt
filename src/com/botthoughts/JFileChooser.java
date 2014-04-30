/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.botthoughts;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** Custom JFileChooser implementation adding a few features
 *
 * @author mes
 */
public class JFileChooser extends javax.swing.JFileChooser {

    /** Mac FileDialog */
    private FileDialog macFileDialog;
    /** the file filter to use */
    private JFileFilter ff = null;
    /** the list of file filters to use */
    //TODO: remove ffList
    private ArrayList<JFileFilter> ffList = new ArrayList<JFileFilter>();
    /** the directory of the selected file (deprecated) */
    //private String directory = null;
    /** the selected filename (deprecated) */
    //private String file = null;
    /** the selected file */
    private File mySelFile = null;

    /** extract the basename of a file string
     *
     * @param filename the name of a file
     * @return the basename (filename with extension removed)
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
     * @param filename the name of a file
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

    /** called when a file selection is made, adds additional features.
     * This version automatically adds file extension if unspecified using
     * the specified file filter (JFileFilter). If the file exists, user is
     * prompted to overwrite. Additionally, this version preserves look and
     * feel across Windows and Mac OSX
     */
    @Override
    public void approveSelection() {
        // This probably will only be called on non-OSX since we're not
        // calling super.showDialog() etc on that platform
        if (this.getDialogType() == SAVE_DIALOG) {
            // Do we have the wrong file extension? If so, fix right now
            File sf = super.getSelectedFile();
            System.out.println("approveSelection(): " + sf.getName());
            if (ff != null && ff.accept(sf)) {
                mySelFile = sf;
            } else {
                String ext = getExtension(sf.getName());
                if (ext.equals("")) {
                    // is the extension missing? fix it
                    String fn = getBasename(sf.getName());
                    sf.getParent();
                    ff.getExtension();
                    mySelFile = new File(sf.getParent(), fn + ff.getExtension());
                    System.out.println("approveSelection(): Fixed ext: "+mySelFile.getName());
                } else {
                    // unrecognized file type
                    int response = JOptionPane.showConfirmDialog(this.getRootPane(),
                        "File " + sf.getName() + " is not a recognized file type. Ok to proceed?", "Unrecognized file type",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE );
                    if (response == JOptionPane.NO_OPTION) {
                        mySelFile = null;
                    } else {
                        mySelFile = sf;
                        System.out.println("approveSelection(): Unrecognized ok: "+mySelFile.getName());
                    }
                }
            }
            
            if (mySelFile.exists()) {
                int response = JOptionPane.showConfirmDialog(this.getRootPane(),
                        "Replace existing file " + mySelFile.getName() + "?", "Replace?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    super.approveSelection();
                } else if (response == JOptionPane.NO_OPTION) {
                    mySelFile = null;
                    //super.cancelSelection();
                }
            } else {
                super.approveSelection();
                this.mySelFile = super.getSelectedFile();
            }
        } else {
            super.approveSelection();
            this.mySelFile = super.getSelectedFile();
        }
    }

    /** adds a user-choosable file filter to the list
     * 
     * @param ff the file filter to add
     */
    public void addChoosableFileFilter(JFileFilter ff) {
        if (!ffList.add(ff)) {
            //TODO check fflist.add() failure
        } else {
            super.addChoosableFileFilter(ff);
        }
    }

    /** sets the filefilter for this file chooser
     *
     * @param ff the file filter to add
     */
    public void setFileFilter(JFileFilter ff) {
        this.ff = ff;
        System.out.println("JFileChooser.setFileFilter() -- got JFileFilter");
        if (!PlatformUtilities.isOSX()) {
            super.setFileFilter(ff);
        }
    }

    private void setDialogSelectedFile() {
        if (PlatformUtilities.isOSX()) {
            if (macFileDialog != null && mySelFile != null) {
                if (mySelFile.isDirectory()) {
                    macFileDialog.setDirectory(mySelFile.getPath());
                    macFileDialog.setFile("");
                } else {
                    macFileDialog.setDirectory(mySelFile.getParent());
                    macFileDialog.setFile(mySelFile.getName());
                }
            }
        } else {
            super.setSelectedFile(mySelFile);
        }
    }

    /** set the selected file to display when the dialog opens
     *
     * @param f the file that will be displayed as selected in the dialog
     */
    @Override
    public void setSelectedFile(File f) {
        if (f != null) {
            System.out.println("setSelectedFile(): "+f.getName());
            this.mySelFile = f;
            setDialogSelectedFile();
        }
    }

    /** return the file selected after the user clicks approve
     *
     * @return file selected after user clicks approve button in the dialog
     */
    @Override
    public File getSelectedFile() {
        return mySelFile;
    }


    private int showMacDialog(Component frame, String buttonText, int mode) {
        int result = CANCEL_OPTION;
        System.out.println(">>>>> "+frame.getClass().toString());
        if (frame instanceof JFrame) {
            JFrame myFrame = (JFrame) frame;
            macFileDialog = new FileDialog(myFrame, buttonText, mode);
        } else if (frame instanceof JDialog) {
            JDialog myDlg = (JDialog) frame;
            macFileDialog = new FileDialog(myDlg, buttonText, mode);
        }
        setDialogSelectedFile();
        macFileDialog.setFilenameFilter(this.ff);
        macFileDialog.setVisible(true);
        if (macFileDialog.getFile() != null) {
            result = APPROVE_OPTION;
            mySelFile = new File( macFileDialog.getDirectory(), macFileDialog.getFile());
        } else {
            result = CANCEL_OPTION;
        }
        return result;
    }

    public int showMacSaveDialog(Component frame) {
        return showMacDialog(frame, "Save", FileDialog.SAVE);
    }
    
    public int showMacOpenDialog(Component frame) {
        return showMacDialog(frame, "open", FileDialog.LOAD);        
    }

    /** displays this as a modal dialog
     *
     * @param frame the parent frame setting location of dialog and modal dependencies
     * @param buttonText is the text to display for the approve button (e.g., "Save")
     * @return user choice, APPROVE_OPTION or CANCEL_OPTION
     */
    @Override
    public int showDialog(Component frame, String buttonText) {
        int result;

        if (PlatformUtilities.isOSX()) {
            // TODO figure out correct default behavior if mode not specified
            result = showMacDialog(frame, buttonText, FileDialog.SAVE);
        } else {
            result = super.showDialog(frame, buttonText);
        }
        return result;
    }

    /** displays this as a modal open dialog
     *
     * @param frame the parent frame setting location of dialog and modal dependencies
     * @return user choice, APPROVE_OPTION or CANCEL_OPTION
     */
    @Override
    public int showOpenDialog(Component frame) {
        int result;

        if (PlatformUtilities.isOSX()) {
            result = showMacOpenDialog(frame);
        } else {
            result = super.showOpenDialog(frame);
        }

        return result;
    }


    /** displays this as a modal save dialog
     *
     * @param frame the parent frame setting location of dialog and modal dependencies
     * @return user choice, APPROVE_OPTION or CANCEL_OPTION
     */
    @Override
    public int showSaveDialog(Component frame) {
        int result;

        if (PlatformUtilities.isOSX()) {
            result = showMacSaveDialog(frame);
        } else {
            result = super.showSaveDialog(frame);
        }

        return result;
    }

    /** return the extension of a file (e.g., ".txt")
     *
     * @param filename the name of a file as a String
     * @return the extension of the file
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}
