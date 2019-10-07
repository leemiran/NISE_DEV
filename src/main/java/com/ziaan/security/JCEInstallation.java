package com.ziaan.security;

import javax.crypto.*;
public class JCEInstallation {
   public static void main(String[] args) {
       Cipher cipher = null;
       try {
               cipher = Cipher.getInstance("DES");
               System.out.println("JCE Installed!");
       } catch(Exception e) {
               System.out.println("JCE Installation Failure!");
       }
   }
}