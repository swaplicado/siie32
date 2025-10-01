/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytests.java;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public class TestSimpleDateFormat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("EEE dd/MMM/yyyy");
        System.out.println("Today is " + format.format(new Date()));
    }
    
}
