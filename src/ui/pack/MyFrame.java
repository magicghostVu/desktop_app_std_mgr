/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.pack;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

/**
 *
 * @author magic_000
 */
public class MyFrame extends JFrame{

    public MyFrame() {
        this.setSize(200, 200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public static void main(String... args) throws IOException {
        //MyFrame mf= new MyFrame();
        HttpClient client= HttpClientBuilder.create().build();
        String url= "http://localhost:8080/students/all";
        HttpGet get= new HttpGet(url);
        HttpResponse response = client.execute(get);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader( response.getEntity().getContent()));
        StringBuilder builder= new StringBuilder();
        while(true){
            String line= bufferedReader.readLine();
            if(line==null){
                break;
            }else{
                builder.append(line);
            }
        }
        bufferedReader.close();
        String result= builder.toString();
        System.out.println(result);
        JSONArray arr= new JSONArray(result);
        System.out.println(arr.length());
        
        //client.
    }
}
