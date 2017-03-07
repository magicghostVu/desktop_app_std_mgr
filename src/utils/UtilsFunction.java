/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.gson.Gson;
import entity.Pojo;
import entity.Student;
import err.define.ErrDefine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author magic_000
 */
public class UtilsFunction {

    private static HttpClient client = HttpClientBuilder.create().build();

    private static String getStringFromResponse(HttpResponse response) throws IOException {
        String result = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            } else {
                builder.append(line);
            }
        }
        bufferedReader.close();
        result = builder.toString();
        return result;
    }

    public static boolean updateStudent(Student student){
        if (student.getId() == null) {
            throw new IllegalArgumentException("Student's id must be not null");
        }
        if(student.getName()==null){
            throw new IllegalArgumentException("Student's name must be not null");
        }
        int id= student.getId();
        try {
            Student stdNeedToUpdate= getAStudentById(id);
            if(stdNeedToUpdate==null){
                return false;
            }else{
                String url= "http://localhost:8080/student/update";
                String jsonData= new Gson().toJson(student);
                String rawResponse=postJsonToServer(url, jsonData);
                JSONObject response= new JSONObject(rawResponse);
                if(response.has("content")){
                    if(!response.getString("content").equals(ErrDefine.SUCCESS)){
                        return false;
                    }
                    return true;
                }else{
                    return false;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        //return false;
    }

    public static void addARowToAModel(DefaultTableModel model, Student s){
        Object[] rowData= {s.getId(), s.getName(), s.getAddress(), s.getAge(),
            s.getGender(), s.getEmail(), s.getPhoneNumber(), s.getBirthday(), s.getStudent_class()};
        model.addRow(rowData);
    }
    
    public static boolean addStudent(Student s) {
        //check name
        if (s.getName() == null) {
            throw new IllegalArgumentException("Student's name must not be null");
        }
        // check id
        else if (s.getId() != null) {
            throw new IllegalArgumentException("Student's id had initialized already");
        }
        else {
            String url = "http://localhost:8080/student/add";
            String jsonData = new Gson().toJson(s);
            try {
                String responseFromServer = postJsonToServer(url, jsonData);
                JSONObject parseJson = new JSONObject(responseFromServer);
                if (parseJson.has("content")) {
                    String content = parseJson.getString("content");
                    if (!content.equals(ErrDefine.SUCCESS)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    private static String postJsonToServer(String url, String jsonData) throws UnsupportedEncodingException, IOException {
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
        StringEntity postData = new StringEntity(jsonData, Charset.forName("UTF-8"));
        //Header charsetHeader= new BasicHeader("charset", "UTF-8");
        postData.setContentEncoding("UTF-8");
        //Header h=postData.getContentEncoding();
        postRequest.setEntity(postData);
        //System.out.println(h.getValue());
        HttpResponse res = client.execute(postRequest);
        String dataResponse = getStringFromResponse(res);
        
        return dataResponse;
    }

    public static String testPostJsonToServer() throws UnsupportedEncodingException, IOException {
        HttpPost postRequest = new HttpPost("http://localhost:8080/post/json");
        postRequest.setHeader("Content-type", "application/json");
        String jsonData = new Gson().toJson(new Pojo());
        StringEntity postData = new StringEntity(jsonData);
        postRequest.setEntity(postData);
        HttpResponse res = client.execute(postRequest);
        String dataResponse = getStringFromResponse(res);
        return dataResponse;
    }

    public static List<Student> getAllStudentFromServer() throws IOException {
        String url = "http://localhost:8080/students/all";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        //int cod
        String jsonResult = getStringFromResponse(response);
        JSONArray arr = new JSONArray(jsonResult);
        List<Student> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); ++i) {
            Student tmpStd = parseAstudent(arr.getJSONObject(i));
            result.add(tmpStd);
        }
        return result;
    }

    public static Student getAStudentById(int id) throws IOException {
        String url = "http://localhost:8080/student/?id=" + id;
        HttpPost post = new HttpPost(url);
        HttpResponse response = client.execute(post);
        String stringResponse = getStringFromResponse(response);
        JSONObject jsonResponse = new JSONObject(stringResponse);
        if (jsonResponse.has("content")) {
            return null;
        } else {
            Student result = parseAstudent(jsonResponse);
            return result;
        }
    }

    private static Student parseAstudent(JSONObject jsonStudent) {
        Student result = new Student();
        if (!jsonStudent.has("id") || !jsonStudent.has("name")) {
            throw new IllegalArgumentException("JSon lỗi cmnr");
        }
        int id = jsonStudent.getInt("id");
        result.setId(id);
        result.setName(jsonStudent.getString("name"));

        if (jsonStudent.has("address")) {
            result.setAddress(jsonStudent.getString("address"));
        }
        if (jsonStudent.has("age")) {
            result.setAge(jsonStudent.getInt("age"));
        }

        if (jsonStudent.has("student_class")) {
            result.setStudent_class(jsonStudent.getString("student_class"));
        }

        if (jsonStudent.has("gender")) {
            result.setGender(jsonStudent.getString("gender"));
        }
        if (jsonStudent.has("email")) {
            result.setEmail(jsonStudent.getString("email"));
        }
        if (jsonStudent.has("phoneNumber")) {
            result.setPhoneNumber(jsonStudent.getString("phoneNumber"));
        }
        if (jsonStudent.has("birthday")) {
            result.setBirthday(jsonStudent.getString("birthday"));
        }
        return result;

    }
    
    public static boolean deleteAStudent(int id){
        String urlPost= "http://localhost:8080/student/delete?id="+id;
        HttpPost postDelete= new HttpPost(urlPost);
        try{
            String responseFromServer= getStringFromResponse(client.execute(postDelete));
            JSONObject jsonResponse= new JSONObject(responseFromServer);
            if(jsonResponse.has("content")){
                if(jsonResponse.getString("content").equals(ErrDefine.SUCCESS)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        //return true;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        String response = testPostJsonToServer();
        System.out.println(response);
    }

}
