package com.thiago.desafio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thiago.desafio.model.MessageBean;
import com.thiago.desafio.utilities.SupporClass;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Controller {

    String token = "ec380e5983554ce07cbafbbe0c5ffdbdc9dd6463";
    String url = "https://api.codenation.dev/v1/challenge/dev-ps";
    private MessageBean mb = null;

    @FXML
    private TextField fieldNumCasasId;
    @FXML
    private TextField fieldTokenId;
    @FXML
    private TextField fieldCifradoId;
    @FXML
    private TextField fieldDecifradoId;
    @FXML
    private TextField fieldResCriptogId;
    @FXML
    private TextArea txtAreaId;


    public void initialize(){
        var writer = new ObjectMapper();
        writer.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mb = writer.readValue(SupporClass.get(url,token), MessageBean.class);
            if(mb != null) {
                fieldNumCasasId.setText(Integer.toString(mb.getNumero_casas()));
                fieldTokenId.setText(mb.getToken());
                fieldCifradoId.setText(mb.getCifrado());
                txtAreaId.setText(writer.writeValueAsString(mb));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSave(){
        try {
            mb.setNumero_casas(Integer.parseInt(fieldNumCasasId.getText()));
            mb.setToken(fieldTokenId.getText());
            mb.setCifrado(fieldCifradoId.getText());
            mb.setDecifrado(fieldDecifradoId.getText());
            mb.setResumo_criptografico(fieldResCriptogId.getText());
            SupporClass.salvarArquivo(mb);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRead(){
        var writer = new ObjectMapper();
        writer.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            MessageBean mb = SupporClass.lerArquivo();
            if(mb != null){
                this.mb = mb;
                fieldNumCasasId.setText(Integer.toString(mb.getNumero_casas()));
                fieldTokenId.setText(mb.getToken());
                fieldCifradoId.setText(mb.getCifrado());
                fieldDecifradoId.setText(mb.getDecifrado());
                fieldResCriptogId.setText(mb.getResumo_criptografico());
                txtAreaId.setText(writer.writeValueAsString(mb));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDecrypt(){
        if(mb != null){
            String dec = SupporClass.decifrar(mb.getCifrado(),mb.getNumero_casas());
            fieldDecifradoId.setText(dec);
        }
    }

    public void onEncrypt(){
        if(!fieldDecifradoId.getText().isEmpty()){
            try {
                String encSHA1 = SupporClass.encriptarSHA1(fieldDecifradoId.getText());
                fieldResCriptogId.setText(encSHA1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSend(){
        try {
            String response = SupporClass.post(url,token,"answer",this.mb);
            if(response != null)
                txtAreaId.setText(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
