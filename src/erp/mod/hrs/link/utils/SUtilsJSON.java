package erp.mod.hrs.link.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mod.hrs.link.db.SBreachInfoResponse;
import erp.mod.hrs.link.db.SCancelResponse;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SDataEmployee;
import erp.mod.hrs.link.db.SEarningResponse;
import erp.mod.hrs.link.db.SIncidentResponse;
import erp.mod.hrs.link.db.SPersonalInfoResponse;
import erp.mod.hrs.link.db.SShareDB;
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

public class SUtilsJSON {
  
    public static final int VOUCHER = 1;
  
    public static final int PREPAYROLL = 2;
  
    public static String getData(String lastSyncDate) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
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
        String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
        
        return jsonInString2;
    }
  
    public static String getEmployeesSiieData() throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SShareDB sDb = new SShareDB();
        SEmployeeJSON objResponse = new SEmployeeJSON();
        objResponse.employees = sDb.getEmployeesSiie();
        String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
        
        return jsonInString2;
    }
  
    public static String getDataPGH(String sJson) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SShareDB sDb = new SShareDB();
        SRootJSON objResponse = new SRootJSON();
        objResponse.vacations = sDb.getEmployeeVacations(sJson);
        String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
        
        return jsonInString2;
    }
  
    public static String getPhotos(int head) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SPhotosResponse response = new SPhotosResponse();
        SShareDB sDb = new SShareDB();
        SEmployeesUtils utils = new SEmployeesUtils();
        ArrayList<Integer> ids = utils.getEmployeesOfHead(head);
        response.photos = sDb.getPhotosOfEmployees(ids);
        String jsonPhotosString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        
        return jsonPhotosString;
    }
  
  public static void writeJSON(String startDate, String endDate, String jsonString, String companyKey, int option) {
    try {
      DateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
      String directory = "";
      switch (option) {
        case 1:
          directory = SShareData.PATH_JSON_DESP_DIR;
          break;
        case 2:
          directory = SShareData.PATH_JSON_DIR;
          break;
      } 
      BufferedWriter writer = new BufferedWriter(new FileWriter(directory + companyKey + "/jsons/" + startDate + "_" + endDate + "__" + dft.format(new Date()) + ".json"));
      writer.write(jsonString);
      writer.close();
    } catch (IOException ex) {
      Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
  
    public static void writeCSV(String startDate, String endDate, List<String> dataLines, String fileHeader, String companyKey, int option) {
        String NEW_LINE_SEPARATOR = "\n";
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        FileWriter fileWriter = null;
        
        try {
            String directory = "";
            
            switch (option) {
                case 1:
                    directory = SShareData.PATH_CSV_DESP_DIR;
                    break;
              case 2:
                    directory = SShareData.PATH_CSV_DIR;
                    break;
            } 
            
            fileWriter = new FileWriter(new File(directory + companyKey + "/csvs/" + startDate + "_" + endDate + "__" + dft.format(new Date()) + ".csv"));
            fileWriter.append(fileHeader);
            fileWriter.append("\n");
            
            for (String dataLine : dataLines) {
                fileWriter.append(dataLine);
                fileWriter.append("\n");
            }
            
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
  
    public static String insertData(String sJsonInc) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        ObjectMapper mapper = new ObjectMapper();
        boolean setinIncidents = false;
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SIncidentsJSON objResponse = new SIncidentsJSON();
        SIncidentResponse AvailableResponse = new SIncidentResponse();
        SShareDB sDb = new SShareDB();
        JSONParser parser = new JSONParser();
        boolean toInsert = false;
        
        try {
            JSONObject root = (JSONObject)parser.parse(sJsonInc);
            toInsert = Boolean.parseBoolean(root.get("to_insert").toString());
            AvailableResponse = sDb.cheakIncidents(sJsonInc);
            
            if (toInsert == true && AvailableResponse.getCode() == 200)
                AvailableResponse = sDb.setinIncidents(sJsonInc); 
            
            objResponse.response = AvailableResponse;
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } 
    }
  
    public static String cancelData(String sJsonInc) throws SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SCancelJSON objResponse = new SCancelJSON();
        SCancelResponse CancelResponse = new SCancelResponse();
        SShareDB sDb = new SShareDB();
        JSONParser parser = new JSONParser();
        
        try {
            JSONObject root = (JSONObject)parser.parse(sJsonInc);
            CancelResponse = sDb.checkCancel(sJsonInc);
            objResponse.response = CancelResponse;
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            
            return jsonInString2;
            
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } 
    }
  
    public static String earningData(String sJsonInc) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SEarningJSON objResponse = new SEarningJSON();
        SEarningResponse EarningResponse = new SEarningResponse();
        SShareDB sDb = new SShareDB();
        
        try {
            EarningResponse = sDb.getEarnings(sJsonInc);
            objResponse.response = EarningResponse;
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            
            return jsonInString2;
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
            return jsonInString2;
        } 
    }
  
    public static String missingPhotos(String employees) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SPhotosResponse response = new SPhotosResponse();
        SShareDB sDb = new SShareDB();
        ArrayList<Integer> ids = new ArrayList<>();
        JSONParser parser = new JSONParser();
        
        try {
            JSONArray root = (JSONArray)parser.parse(employees);
            for (int i = 0; root.size() > i; i++)
                ids.add(Integer.valueOf(Integer.parseInt(root.get(i).toString()))); 
        } catch (ParseException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            return "";
        } 
        
        response.photos = sDb.getPhotosOfEmployees(ids);
        String jsonPhotosString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return jsonPhotosString;
    }
  
    public static String personalData(String idEmp) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SDataEmployee dataEmployee = new SDataEmployee();
        SShareDB sDb = new SShareDB();
        dataEmployee = sDb.getDataEmployee(idEmp);
        String jsonInStringDataEmploye = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataEmployee);
        return jsonInStringDataEmploye;
    }
  
    public static String personalInfo(String idEmp) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SPersonalInfoResponse personalInfoResponse = new SPersonalInfoResponse();
        SShareDB sDb = new SShareDB();
        personalInfoResponse = sDb.getPersonalInfo(idEmp);
        String jsonInStringDataEmploye = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(personalInfoResponse);
        return jsonInStringDataEmploye;
    }
  
    public static String insertPersonalInfo(String sJsonInc) {
        ObjectMapper mapper = new ObjectMapper();
        boolean setInfo = false;
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SShareDB sDb = new SShareDB();
        
        try {
            setInfo = sDb.insertPersonalInfo(sJsonInc);
        } catch (SQLException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (SConfigException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
        } 
        
        String jsonInString2 = "";
        
        try {
            jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Boolean.valueOf(setInfo));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
        } 
        
        return jsonInString2;
    }
  
    public static String docAdmRecInfo(String sJsonInc) throws ParseException {
        ObjectMapper mapper = new ObjectMapper();
        SBreachInfoResponse objResponse = new SBreachInfoResponse();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SShareDB sDb = new SShareDB();
        
        try {
            objResponse = sDb.getAdmRecInfo(sJsonInc);
        } catch (SQLException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (SConfigException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
        } 
        String jsonInString2 = "";
        
        try {
            jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
        }
        
        return jsonInString2;
    }
  
    public static String breachInfo(String sJsonInc) throws ParseException {
        ObjectMapper mapper = new ObjectMapper();
        SBreachInfoResponse objResponse = new SBreachInfoResponse();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SShareDB sDb = new SShareDB();
        
        try {
            objResponse = sDb.getBreachInfo(sJsonInc);
        } catch (SQLException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (SConfigException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
        } 
        String jsonInString2 = "";
        
        try {
            jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SUtilsJSON.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
        } 
        return jsonInString2;
    }
}
