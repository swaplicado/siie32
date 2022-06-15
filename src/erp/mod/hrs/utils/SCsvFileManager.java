/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Edwin Carmona
 */
public class SCsvFileManager {
    
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    
    private static final int UUID = 0;
    private static final int SUBSIDY = 1;
    private static final int TAX = 2;
    
    /**
     * read the csv and transform the lines in the file 
     * received to list of SInputData objects
     * 
     * @param fileName String with the path of file
     * @return list values from file
     */
    public static ArrayList<SInputData> readFile(String fileName) {
        ArrayList<SInputData> lRows = new ArrayList<>();
        BufferedReader fileReader = null;
        
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            
            String line = "";
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                
                if (tokens[UUID].length() > 0 && Double.parseDouble(tokens[SUBSIDY]) > 0) {
                    SInputData row = new SInputData(tokens[UUID], Double.parseDouble(tokens[SUBSIDY]), Double.parseDouble(tokens[TAX]));
                    lRows.add(row);
                }
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SCsvFileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(SCsvFileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SCsvFileManager.class.getName()).log(Level.SEVERE, null, "Hubo un error al leer el archivo");
        }
    
        return lRows;
    }
    
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                    .map(this::escapeSpecialCharacters)
                        .collect(Collectors.joining(COMMA_DELIMITER));
    }
    
    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
    
    public void writeCsvFile(List<String[]> dataLines, String sFile ) throws IOException {
        File csvOutputFile = new File(sFile);
        
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
              .map(this::convertToCSV)
              .forEach(pw::println);
        }
//        assertTrue(csvOutputFile.exists());
    }
}
