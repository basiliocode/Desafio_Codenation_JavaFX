package com.thiago.desafio.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {
    private final String USER_AGENT = "mozilla/5.0";
    private String token;
    private String url;
    private URL urlObj;
    private HttpURLConnection con;

    public HttpConnection(String url, String token){
        this.url = url;
        this.token = token;
    }

    public String get() throws Exception {
        urlObj = new URL(url+"/generate-data?token="+token);
        con = (HttpURLConnection) urlObj.openConnection();
        //optional default is GET
        con.setRequestMethod("GET");
        // add Request header
        con.addRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Sending 'GET' to url: " + url);
        System.out.println("ResponseCode: " + responseCode);

        if(responseCode == 200){
            BufferedReader br = new BufferedReader( new InputStreamReader(con.getInputStream()));
            String response = lerArquivo(br);
            br.close();
            return response;
        } else
            return null;
    }

    public String post(String attachmentName, String fileName, byte [] objAsBytes) throws Exception {
        urlObj = new URL(url+"/submit-solution?token="+token);

        String crlf = "\r\n",twoHyphens = "--";
        String boundary =  "----WebKitFormBoundary7MA4YWxkTrZu0gW";

        con = (HttpURLConnection) urlObj.openConnection();
        //add Request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(
                con.getOutputStream());
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                attachmentName + "\";filename=\"" + fileName + "\"" + crlf);
        request.writeBytes(crlf);

        //Escrever o byte convertido do arquivo
        request.write(objAsBytes);
        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary +
                twoHyphens + crlf);

        int responseCode = con.getResponseCode();
        System.out.println("Sending 'POST' to url: " + url);
        System.out.println("ResponseCode: " + responseCode);
        request.flush();
        request.close();

        if(responseCode == 429){
            return "Tente mais tarde (aprox. 1 minuto)";
        }

        if(responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = lerArquivo(br);
            br.close();
            return response;
        } else
            return null;
    }

    private String lerArquivo(BufferedReader br) throws IOException {
        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = br.readLine()) != null){
            response.append(inputLine);
        }
        return response.toString();
    }
}
