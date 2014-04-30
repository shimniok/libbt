/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.botthoughts;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Shimniok
 */
public class UpdateChecker {

    private String version="";
    private URL url;
    private String currentVersion="";
    
    private int[] parseVersion(String v) {
        int[] results;
        
        String[] n = v.split("-");
        String[] ver = n[0].split("[.]");
        
        results = new int[ver.length];
        for (int i=0; i < ver.length; i++) {
            results[i] = Integer.parseInt(ver[i]);
        }
        
        return results;
    }
    
    private boolean newAvailable(String current) {
        boolean result = false;
        int[] currentVersion = parseVersion(current);
        int[] myVersion = parseVersion(this.version);

        for (int i=0; i < Math.min(currentVersion.length, myVersion.length); i++) {
            if (myVersion[i] > currentVersion[i]) {
                break;
            } else if (myVersion[i] < currentVersion[i]) {
                result = true;
                break;
            }
        }

        return result;
    }
    
    public void setURL(String url) throws MalformedURLException {
        this.url = new URL(url);
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public boolean checkUpdate() {
        boolean result = false;
        
        try {
            InputStream is;
            String line;

            if (url != null) {
                is = url.openStream();  // throws an IOException
                BufferedReader reader = new BufferedReader( new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    currentVersion = line;
                }
                is.close();
                //System.out.println(this.version);
                if (currentVersion != null) {
                    result = newAvailable(currentVersion);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UpdateChecker uc = new UpdateChecker();
        try {
            uc.setURL("http://wheel-encoder-generator.googlecode.com/svn/trunk/CurrentVersion.txt");
            uc.setVersion("0.1.3-alpha");
            uc.checkUpdate();
        } catch (MalformedURLException ex) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
