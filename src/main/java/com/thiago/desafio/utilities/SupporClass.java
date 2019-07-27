package com.thiago.desafio.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thiago.desafio.model.MessageBean;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SupporClass {

    final static String FILENAME = "answer.json";

    public static String get(String url, String token) throws Exception {
        HttpConnection http = new HttpConnection(url,token);
        return http.get();
    }

    public static void salvarArquivo(MessageBean mb) throws IOException {
        var writer = new ObjectMapper();
        writer.enable(SerializationFeature.INDENT_OUTPUT);
        writer.writeValue(new File(FILENAME), mb);
    }

    public static MessageBean lerArquivo() throws IOException {
       File file = new File(FILENAME);

       if(!file.exists())
           return null;

       var reader = new ObjectMapper();
       return reader.readValue(file,MessageBean.class);
    }

    public static String decifrar(String message, int n){
        message = message.toLowerCase();

        byte[] barr = message.getBytes();

        for (int i = 0; i < barr.length; i++) {
            byte b = (barr[i]);
            if(b > 96 && b < 123)
                barr[i] = (byte) (b - n);
        }
        return new String(barr);
    }

    public static String encriptarSHA1(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String sha1 = "";
        if(!message.equals("")) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(message.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        }
        return sha1;
    }

    public static String post(String url, String token, String attachamentName, MessageBean mb) throws Exception {
        HttpConnection http = new HttpConnection(url,token);
        var writer = new ObjectMapper();
        writer.enable(SerializationFeature.INDENT_OUTPUT);
        return http.post(attachamentName,FILENAME,writer.writeValueAsBytes(mb));
    }
}
