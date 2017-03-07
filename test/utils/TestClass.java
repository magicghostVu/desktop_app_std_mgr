/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entity.Student;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author magic_000
 */
public class TestClass extends TestCase{
    
    @Test
    public void testJUnit() throws IOException{
        assertEquals(3 , UtilsFunction.getAllStudentFromServer().size());
    }
    
    @Test
    public void testGetStudentById() throws IOException{
        assertEquals(new Integer(23),UtilsFunction.getAStudentById(1).getAge());
        assertEquals(null,UtilsFunction.getAStudentById(5));
        System.out.println(UtilsFunction.testPostJsonToServer());
    }
    @Test
    public void testAddStudent(){
        Student newStudent= new Student();
        newStudent.setName("Lưu Thắng");
        newStudent.setAddress("Bắc Giang");
        newStudent.setBirthday("27/11/1995");
        boolean res=UtilsFunction.addStudent(newStudent);
        assertTrue(res);
    }
    @Test
    public void testUpdateStudent(){
        Student s= new Student();
        s.setId(2);
        s.setName("Vũ Hồng Phú");
        boolean result= UtilsFunction.updateStudent(s);
        assertTrue(result);
    }
  
    @Test
    public void testDeleteStudent(){
        assertTrue(UtilsFunction.deleteAStudent(11));
    }
}
