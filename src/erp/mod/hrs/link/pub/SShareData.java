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
import java.sql.SQLException;

/**
 *
 * @author Edwin Carmona
 */
public class SShareData {
    
    public static String PATH_JSON_DIR = "prenomina/";
    public static String PATH_CSV_DIR = "prenomina/";
    
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

     */
    public String getSiieData(String sLastSyncDate) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date tLastSyncDate = formatterd.parse(sLastSyncDate);

        return SUtilsJSON.getData(sLastSyncDate);
    }
    
    
    public SPrepayroll getCAPData(Date tStartDate, Date tEndDate, ArrayList<Integer> lEmployees, 
                                    int payType, int dataType, String companyKey) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
            String employees = lEmployees.stream().map(Object::toString)
                                                .collect(Collectors.joining(","));
            
            String url = "http://localhost:9090/cap/public/api/prepayroll";
            
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
            
            URLConnection connection = new URL(url + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            InputStream response = connection.getInputStream();
            
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
                
                ObjectMapper mapper = new ObjectMapper();
                SPrepayroll prepayroll = mapper.readValue(responseBody, SPrepayroll.class);
                
                SUtilsJSON.writeJSON(startDate, endDate, responseBody, companyKey);
                
                return prepayroll;
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
