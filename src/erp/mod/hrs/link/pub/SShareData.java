/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.pub;

import com.fasterxml.jackson.core.JsonProcessingException;
import erp.mod.hrs.link.utils.SPrepayroll;
import erp.mod.hrs.link.utils.SUtilsJSON;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.hrs.utils.SCAPResponse;
import java.sql.SQLException;
import javax.swing.JOptionPane;

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
    
    /**
     * 
     * @param sLastSyncDate
     * @return JSON String
     * {
        lastSyncTimeStamp: time stamp (aaaa-MM-dd HH:mm:ss),
        employees : [  
                    {
                        name: string,
                        lastname: string,
                        num_employee: integer,
                        external_id: integer,
                        admission_date: date,
                        leave_date: date,
                        company_id: integer,
                        extra_time: boolean,
                        way_pay: integer,
                        is_active: boolean
                    }
                ],
        firstDayOfYear: [{
                            year: integer,
                            date: date
                        }],
        absences: [
                    {
                        external_id: integer,
                        num_incidents: integer,
                        fecha_ini: date,
                        fecha_fin: date,
                        type_incident: integer,
                        employee_id: integer,
                        efective_day: integer
                    }
                ],
        holidays: [
                {
                    name: string,
                    year: integer,
                    dt_date: date,
                    application_date: date
                }
        ]
    }
     * @throws java.text.ParseException
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     * @throws erp.mod.hrs.link.db.SConfigException

     */
    public String getSiieData(String sLastSyncDate) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        formatterd.parse(sLastSyncDate);

        return SUtilsJSON.getData(sLastSyncDate);
    }
    
    /**
     * Obtiene las fotos de los empleados subordinados del empleado que corresponde al id recibido.
     * 
     * {
        "photos" : [ {
          "photo" : null,
          "idEmployee" : 4785,
          "numEmployee" : 1426
        }, {
          "photo" : null,
          "idEmployee" : 4359,
          "numEmployee" : 1314
        }, {
          "photo" : null,
          "idEmployee" : 5021,
          "numEmployee" : 1512
        }, {
          "photo" : "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\r\nHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\r\nMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCABtAGQDASIA\r\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\r\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\r\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\r\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\r\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\r\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\r\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\r\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD34kAZ\r\nPSgHIz2pH+4fpSL9xcelAD6KQnAzjNV4ZhKPusr7QxRhyue36UAWKWs7UNRs9MtHur64jggTq8jY\r\nH09z7VwOq/GbR7RilhbT3bA/ec+Wv8if0FAm0j0/NJxXmeifGLSb7zBqcJstoyrIxkDe2AMg10ei\r\n+PPD2vXYtLO8xcNkLHKhQv8ATPWgXMjp1YOMjPpyMU+mDO7j7uOlPoKCiiigAooooAa3KH6Uif6t\r\nfoKeelMj/wBWv+6KAHVWvryDT7Ga8uX2QwoXdvQCrNeQfGnxG9tb2+iQtgSjzpyD1APyr+fNAm7H\r\nn3jnxvP4g1M3E7MlumVggVshB/ie5/CuMa8d/uwFQem5+f0qW0s5tW1ERRnnsSK9R0PwFaQRJJc5\r\n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\r\npcU1SSdwc21YKKKK1JP/2Q==",
          "idEmployee" : 3774,
          "numEmployee" : 1117
        }
     * }
     * 
     * @param head entero id del empleado "jefe" (id de siie)
     * @return un string con el json que contiene las fotos de los empleados subordinados al recibido
     * @throws SConfigException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws JsonProcessingException
     * @throws IOException 
     */
    public String getPhotos(int head) throws SConfigException, ClassNotFoundException, SQLException, JsonProcessingException, IOException {
        return SUtilsJSON.getPhotos(head);
    }
    
    /**
     * 
     * @param tStartDate
     * @param tEndDate
     * @param lEmployees
     * @param payType
     * @param dataType Policy for requesting information from external time clock: 1 = all; 2 = official; 3 = non-official
     * @param companyKey
     * @return 
     */
    public SPrepayroll getCAPData(String sURL, Date tStartDate, Date tEndDate, ArrayList<Integer> lEmployees, 
                                    int payType, int dataType, String companyKey) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
            String employees = lEmployees.stream().map(Object::toString)
                                                .collect(Collectors.joining(","));
            
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            String startDate = df.format(tStartDate);
            String endDate = df.format(tEndDate);
            
            String query = String.format("start_date=%s&end_date=%s&employees=%s&pay_type=%s&data_type=%s",
                                    URLEncoder.encode(startDate, charset),
                                    URLEncoder.encode(endDate, charset),
                                    URLEncoder.encode(employees, charset),
                                    URLEncoder.encode(payType + "", charset),
                                    URLEncoder.encode(dataType + "", charset)
                            );
            
            URLConnection connection = new URL(sURL + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            InputStream response = connection.getInputStream();
            
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println("Respuesta desde " + sURL);
//                System.out.println(responseBody);
                
                ObjectMapper mapper = new ObjectMapper();
                SCAPResponse resp = mapper.readValue(responseBody, SCAPResponse.class);
                switch (resp.getCode()) {
                    case SCAPResponse.RESPONSE_OK:
                        SPrepayroll prepayroll = resp.getPrepayrollData();
                        SUtilsJSON.writeJSON(startDate, endDate, responseBody, companyKey, SUtilsJSON.PREPAYROLL);
                        
                        return prepayroll;
                        
                    case SCAPResponse.RESPONSE_NOT_VOBO:
                    case SCAPResponse.RESPONSE_ERROR:
                        JOptionPane.showMessageDialog(null, resp.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(SShareData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
