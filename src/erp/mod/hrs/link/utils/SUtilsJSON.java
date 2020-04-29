/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import erp.mod.hrs.link.db.SShareDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mod.hrs.link.pub.SShareData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SUtilsJSON {
    
    /**
     * Consulta los elementos en la base de datos y los transforma a un 
     * objeto JSON
     * 
     * @param lastSyncDate
     * 
     * @return JSON String proveniente del objeto SRootJSON
     * 
     * https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
     * @throws java.sql.SQLException
     */
    public static String getData(String lastSyncDate) throws SQLException, ClassNotFoundException, JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            
            SShareDB sDb = new SShareDB();
            
            SRootJSON objResponse = new SRootJSON();
            
            objResponse.last_sync_date = lastSyncDate;
            objResponse.employees = sDb.getEmployees(lastSyncDate);
            objResponse.holidays = sDb.getAllHolidays(lastSyncDate);
            objResponse.fdys = sDb.getAllFirstDayOfYear(lastSyncDate);
            objResponse.absences = sDb.getAllAbsences(lastSyncDate);
            
            // Java objects to JSON file
            // mapper.writeValue(new File("c:\\test\\staff.json"), staff);
            
            // Java objects to JSON string - compact-print
//            String jsonString = mapper.writeValueAsString(staff);
//            System.out.println(jsonString);
            
            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
//            System.out.println(jsonInString2);
            return jsonInString2;
    }
    
    public static void writeJSON(String startDate, String endDate, String jsonString) {
        try {
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(SShareData.PATH_JSON_DIR + startDate + "_" + endDate +
                    "__" + dft.format(new Date()) + ".json"));
            
            writer.write(jsonString);
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeCSV(String startDate, String endDate, List<String> dataLines, String fileHeader) {
        final String NEW_LINE_SEPARATOR = "\n";
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        
        FileWriter fileWriter = null;
        
        try {
            fileWriter = new FileWriter(new File(SShareData.PATH_CSV_DIR + startDate + "_" + endDate +
                                        "__" + dft.format(new Date()) + ".csv"));

            //Write the CSV file header
            fileWriter.append(fileHeader);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new Ingredient object list to the CSV file
            
            for (String dataLine : dataLines) {
                fileWriter.append(dataLine);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
