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
import erp.mod.hrs.link.db.SCancelResponse;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SDataEmployee;
import erp.mod.hrs.link.db.SEarningResponse;
import erp.mod.hrs.link.db.SIncidentResponse;
import static erp.mod.hrs.link.db.SIncidentResponse.RESPONSE_ERROR;
import static erp.mod.hrs.link.db.SIncidentResponse.RESPONSE_OK_AVA;
import static erp.mod.hrs.link.db.SIncidentResponse.RESPONSE_OK_INS;
import static erp.mod.hrs.link.db.SIncidentResponse.RESPONSE_OTHER_INC;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    public static String getData(String lastSyncDate) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
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
            //objResponse.vacations = sDb.getEmployeeVacations(lastSyncDate);
            
            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
    }
    
    public static String getDataPGH(String sJson) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException, ParseException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            
            SShareDB sDb = new SShareDB();
            
            SRootJSON objResponse = new SRootJSON();
            objResponse.vacations = sDb.getEmployeeVacations(sJson);
            
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
    
    /**
     * Revisar si se pueden meter las incidencias y de poderse insertarlas
     * 
     * @param sJsonInc
     */
    
    public static String insertData(String sJsonInc) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
            ObjectMapper mapper = new ObjectMapper();
            boolean setinIncidents = false;
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            
            SIncidentsJSON objResponse = new SIncidentsJSON();
            SIncidentResponse AvailableResponse = new SIncidentResponse();
            SShareDB sDb = new SShareDB();
            
            // objeto para leer el JSON
            JSONParser parser = new JSONParser();
            JSONObject root;
            boolean toInsert = false;
        try {
            root = (JSONObject) parser.parse(sJsonInc);
            toInsert = Boolean.parseBoolean(root.get("to_insert").toString());
             // revisar si hay incidencias para esas fechas
            AvailableResponse = sDb.cheakIncidents(sJsonInc);
            
            if (toInsert == true && AvailableResponse.getCode() == 200 ) {
                AvailableResponse = sDb.setinIncidents(sJsonInc);
            }
            
            objResponse.response = AvailableResponse;
            
            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, null, ex);
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        }       
    }
    
    public static String cancelData(String sJsonInc) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        SCancelJSON objResponse = new SCancelJSON();
        SCancelResponse CancelResponse = new SCancelResponse();
        SShareDB sDb = new SShareDB();
        
        // objeto para leer el JSON
        JSONParser parser = new JSONParser();
        JSONObject root;
        
        try {
            root = (JSONObject) parser.parse(sJsonInc);
             // revisar si hay incidencias para esas fechas
            CancelResponse = sDb.checkCancel(sJsonInc);
           
            
            objResponse.response = CancelResponse;
            
            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, null, ex);
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        }   
    }
    
    public static String earningData(String sJsonInc) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        SEarningJSON objResponse = new SEarningJSON();
        SEarningResponse EarningResponse = new SEarningResponse();
        SShareDB sDb = new SShareDB();
        
        try {
             // revisar si hay incidencias para esas fechas
            EarningResponse = sDb.getEarnings(sJsonInc);
           
            
            objResponse.response = EarningResponse;
            
            // Java objects to JSON string - pretty-print
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, null, ex);
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } 
    }
    
    /**
     * Obtener las fotos faltantes para aplicación vacaciones
     * 
     * @param employees
     * @throws erp.mod.hrs.link.db.SConfigException
     */
    
    public static String missingPhotos(String employees) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        SPhotosResponse response = new SPhotosResponse();
        SShareDB sDb = new SShareDB();
        ArrayList<Integer> ids = new ArrayList();
        JSONParser parser = new JSONParser();
        try {
            //JSONObject root = (JSONObject) parser.parse(employees);
            JSONArray root  = (JSONArray) parser.parse(employees);
            for( int i = 0 ; root.size() > i ; i++ ){
                ids.add(Integer.parseInt((root.get(i).toString())));
            }
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
        response.photos = sDb.getPhotosOfEmployees(ids);
        
        // Java objects to JSON string - pretty-print
        String jsonPhotosString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        
        return jsonPhotosString;
    }
    
    public static String personalData(String idEmp) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        
        SDataEmployee dataEmployee = new SDataEmployee();
        SShareDB sDb = new SShareDB();
        
        dataEmployee = sDb.getDataEmployee(idEmp);
        String jsonInStringDataEmploye = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataEmployee);
        return jsonInStringDataEmploye; 
      
    }
}
