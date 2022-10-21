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
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.pub.SShareData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SUtilsJSON {
    
    public static final int VOUCHER = 1;
    public static final int PREPAYROLL = 2;
    
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
     * @throws java.lang.ClassNotFoundException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     * @throws erp.mod.hrs.link.db.SConfigException
     */
    public static String getData(String lastSyncDate) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException, IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            
            SShareDB sDb = new SShareDB();
            
            SRootJSON objResponse = new SRootJSON();
            
            objResponse.last_sync_date = lastSyncDate;
            objResponse.departments = sDb.getDepartments(lastSyncDate);
            objResponse.positions = sDb.getPositions(lastSyncDate);
            objResponse.employees = sDb.getEmployees(lastSyncDate);
            objResponse.holidays = sDb.getAllHolidays(lastSyncDate);
            objResponse.fdys = sDb.getAllFirstDayOfYear(lastSyncDate);
            objResponse.absences = sDb.getAllAbsences(lastSyncDate);
            objResponse.cuts = sDb.getAllCutsCalendar(lastSyncDate);
            objResponse.vacations = sDb.getEmployeeVacations(lastSyncDate);
            
            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
    }
    
    /**
     * Método que obtiene las fotos de los empleados subordinados del empleado que se recibe.
     * Estas imagenes son codificadas a base64 y transfomadas a texto
     * 
     * @param head entero que representa el id del empleado del que se quieren obtener los subordinados
     * 
     * @return JSON String proveniente del objeto SPhotosResponse
     * 
     * https://mkyong.com/java/jackson-2-convert-java-object-to-from-json/
     * @throws SConfigException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static String getPhotos(int head) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        SPhotosResponse response = new SPhotosResponse();
        SShareDB sDb = new SShareDB();
        
        SEmployeesUtils utils = new SEmployeesUtils();
        
        ArrayList<Integer> ids = utils.getEmployeesOfHead(head);
        response.photos = sDb.getPhotosOfEmployees(ids);
        
        // Java objects to JSON string - pretty-print
        String jsonPhotosString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        
        return jsonPhotosString;
    }
    
    /**
     * Escribe la bitácora de JSON
     * 
     * @param startDate
     * @param endDate
     * @param jsonString
     * @param companyKey nombre de la carpeta donde se guardará el archivo
     * @param option pueden ser VOUCHER o PREPAYROLL
     */
    public static void writeJSON(String startDate, String endDate, String jsonString, String companyKey, int option) {
        try {
            DateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            
            String directory = "";
            switch (option) {
                case VOUCHER:
                    directory = SShareData.PATH_JSON_DESP_DIR;
                    break;
                case PREPAYROLL:
                    directory = SShareData.PATH_JSON_DIR;
                    break;
            }
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory + companyKey + "/jsons/" + startDate + "_" + endDate +
                    "__" + dft.format(new Date()) + ".json"));
            
            writer.write(jsonString);
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Escribir bitácora de CSV
     * 
     * @param startDate
     * @param endDate
     * @param dataLines
     * @param fileHeader
     * @param companyKey
     * @param option VOUCHER o PREPAYROLL
     */
    public static void writeCSV(String startDate, String endDate, List<String> dataLines, String fileHeader, String companyKey, int option) {
        final String NEW_LINE_SEPARATOR = "\n";
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        
        FileWriter fileWriter = null;
        
        try {
            String directory = "";
            switch (option) {
                case VOUCHER:
                    directory = SShareData.PATH_CSV_DESP_DIR;
                    break;
                case PREPAYROLL:
                    directory = SShareData.PATH_CSV_DIR;
                    break;
            }
                    
            fileWriter = new FileWriter(new File(directory + companyKey + "/csvs/" + startDate + "_" + endDate +
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
