package erp.mod.hrs.link.pub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.hrs.link.utils.SPrepayroll;
import erp.mod.hrs.link.utils.SUtilsJSON;
import erp.mod.hrs.utils.SCAPResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SShareData {
    
    public static String PATH_JSON_DIR = "prenomina/";

    public static String PATH_CSV_DIR = "prenomina/";

    public static String PATH_JSON_DESP_DIR = "vales/";

    public static String PATH_CSV_DESP_DIR = "vales/";

    public void setJsonConn(String sjon) {
        SMySqlClass.setJsonConn(sjon);
    }
  
    public String getSiieData(String sLastSyncDate) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatterd.parse(sLastSyncDate);

        return SUtilsJSON.getData(sLastSyncDate);
    }

    public String getEmployeesSiieData() throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        return SUtilsJSON.getEmployeesSiieData();
    }

    public String getPGHData(String sJSon) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException, org.json.simple.parser.ParseException {
        try {
            return SUtilsJSON.getDataPGH(sJSon);
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
          return ex.getMessage();
        } 
      }

    public String getPhotos(int head) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException, IOException {
        return SUtilsJSON.getPhotos(head);
    }

    public SPrepayroll getCAPData(String sURL, Date tStartDate, Date tEndDate, ArrayList<Integer> lEmployees, int payType, int dataType, String companyKey) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String employees = lEmployees.stream().map(Object::toString).collect(Collectors.joining(","));
            String charset = StandardCharsets.UTF_8.name();
            String startDate = df.format(tStartDate);
            String endDate = df.format(tEndDate);
            String query = String.format("start_date=%s&end_date=%s&employees=%s&pay_type=%s&data_type=%s", new Object[] { URLEncoder.encode(startDate, charset), 
                URLEncoder.encode(endDate, charset), 
                URLEncoder.encode(employees, charset), 
                URLEncoder.encode(payType + "", charset), 
                URLEncoder.encode(dataType + "", charset) });

            URLConnection connection = (new URL(sURL + "?" + query)).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            InputStream response = connection.getInputStream();

            try (Scanner scanner = new Scanner(response)) {
                SPrepayroll prepayroll;
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
                ObjectMapper mapper = new ObjectMapper();
                SCAPResponse resp = (SCAPResponse)mapper.readValue(responseBody, SCAPResponse.class);
                switch (resp.getCode()) {
                    case 200:
                        prepayroll = resp.getPrepayrollData();
                        SUtilsJSON.writeJSON(startDate, endDate, responseBody, companyKey, 2);
                        return prepayroll;
                    case 500:
                    case 550:
                        JOptionPane.showMessageDialog(null, resp.getMessage(), "ERROR", 0);
                        break;
                } 
            } 
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
        } 

        return null;
    }

    public String insertIncidents(String sJsonInc) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        return SUtilsJSON.insertData(sJsonInc);
    }

    public String cancelIncidents(String sJsonInc) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        return SUtilsJSON.cancelData(sJsonInc);
    }

    public String getMissingPhotos(String employees) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        try {
            return SUtilsJSON.missingPhotos(employees);
        } catch (IOException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        } 
    }

    public String getEarnings(String sJsonInc) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        try {
            return SUtilsJSON.earningData(sJsonInc);
        } catch (IOException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        } 
    }

    public String getDataPersonal(String idEmp) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        try {
            return SUtilsJSON.personalData(idEmp);
        } catch (IOException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }

    public String getPersonalInfo(String idEmp) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        try {
            return SUtilsJSON.personalInfo(idEmp);
        } catch (IOException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        } 
    }

    public String insertPersonalInfo(String sJsonInc) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        return SUtilsJSON.insertPersonalInfo(sJsonInc);
    }

    public String getBreachInfo(String sJsonInc) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        try {
            return SUtilsJSON.breachInfo(sJsonInc);
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            return "fallo";
        } 
    }

    public String getAdmRecInfo(String sJsonInc) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        try {
            return SUtilsJSON.docAdmRecInfo(sJsonInc);
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            return "fallo";
        } 
    }
}
