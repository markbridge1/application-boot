/*
 * Mark Bridge (markbridge.com), San Francisco CA 94102, j2eewebtier@gmail.com 
 * Copyright (c) 2012 Mark Bridge All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.markbridge.application.boot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.salt.RandomSaltGenerator;

/**
 *
 * @author mbridge
 */
public class Boot {
    
    public static final Logger logger = Logger.getLogger(Boot.class.getName());
    
    public static Boolean BOOT = Boolean.TRUE;
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("bootstrap");
    
    private final static StandardPBEStringEncryptor ENC = new StandardPBEStringEncryptor();
    
    static {
        if(BOOT) {
            BOOT = Boolean.FALSE;
            logger.setLevel(Level.FINEST);
            EnvironmentStringPBEConfig encConfig = new EnvironmentStringPBEConfig();
            encConfig.setAlgorithm(BUNDLE.getString("bootstrap.enc.algorithm"));
            encConfig.setPasswordEnvName(BUNDLE.getString("bootstrap.enc.passwordvar"));
            encConfig.setSaltGenerator(new RandomSaltGenerator());
            ENC.setConfig(encConfig);
            
            init();
        }
    }
    
    private static void init() {
        Config.BUNDLE = getBundle("config", getDelimitedText("bootstrap.config", BUNDLE));
        Res.BUNDLE = getBundle("res", getDelimitedText("bootstrap.res", BUNDLE));
    }
    
    public static String getString(String key, ResourceBundle bundle) {
        String retVal = null;
        try {
            retVal = bundle.getString(key).trim();
            if(retVal.startsWith("ENC(") && retVal.endsWith(")")) {
                retVal = decrypt(retVal.substring(4, retVal.length()-1));
            }
        } catch(Exception ex) {
            logger.log(Level.FINEST, "couldn't get property " + key, ex);
        }
        return retVal;
    }
    
    public static Long getLong(String key, ResourceBundle bundle) {
        Long retVal = null;
        try {
            retVal = Long.parseLong(getString(key, bundle));
        } catch(Exception ex) {
            logger.log(Level.FINEST, "couldn't get property " + key, ex);
        }
        return retVal;
    }
    
    public static Integer getInteger(String key, ResourceBundle bundle) {
        Integer retVal = null;
        try {
            retVal = Integer.parseInt(getString(key, bundle));
        } catch(Exception ex) {
            logger.log(Level.FINEST, "couldn't get property " + key, ex);
        }
        return retVal;
    }
    
    public static ArrayList<String> getDelimitedText(String key, String delimiter, ResourceBundle bundle) {
        ArrayList<String> retVal = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(getString(key, bundle)).useDelimiter(delimiter);
            while(scanner.hasNext()) {
                retVal.add(scanner.next());
            }
        } catch(Exception ex) {
            logger.log(Level.FINEST, "couldn't get property " + key, ex);
        }
        
        return retVal;
    }
    
    public static ArrayList<String> getDelimitedText(String key, ResourceBundle bundle) {
        return getDelimitedText(key, ",", bundle);
    }
    
    public static String decrypt(String ciphertext) {
        String cleartext = null;
        try {
            cleartext = ENC.decrypt(ciphertext);
        } catch(Exception ex) {
            logger.log(Level.INFO, "couldn't decrypt " + ciphertext, ex);
        }
        return cleartext;
    }
    
    public static String encrypt(String cleartext) {
        String ciphertext = null;
        try {
            ciphertext = ENC.encrypt(cleartext);
        } catch(Exception ex) {
            logger.log(Level.INFO, "couldn't encrypt", ex);
        }
        return ciphertext;
    }
    
    private static ResourceBundle getBundle(String base, ArrayList<String> componentL) {
        
        ResourceBundle retVal = null;
        Locale locale = null;
        
        try {
            if(componentL != null) {
                if(componentL.size() == 1) {
                    locale = new Locale(componentL.get(0));
                } else if(componentL.size() == 2) {
                    locale = new Locale(componentL.get(0), componentL.get(1));
                } else if(componentL.size() >= 3) {
                    locale = new Locale(componentL.get(0), componentL.get(1), componentL.get(2));
                }
            }

            if(locale != null) {
                retVal = ResourceBundle.getBundle(base, locale);
            } else {
                retVal = ResourceBundle.getBundle(base);
            }
        } catch(Exception ex) {
            logger.log(Level.FINEST, "couldn't get bundle " + base, ex);
        }
        
        return retVal;
    }
    
    public static void main(String[] args) {
        System.out.println(Boot.encrypt("password"));
        System.out.println(new RandomSaltGenerator().includePlainSaltInEncryptionResults());
        ArrayList<String> serverL = getDelimitedText("bootstrap.config", BUNDLE);
        for(String s : serverL) {
            System.out.println(s);
        }
    }
}
