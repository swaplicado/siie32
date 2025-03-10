/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Sergio Flores
 */
public class SCustomReportsParser {
    
    public static final String CUST_REPS = "reps-cust";
    public static final String CUST_REPS_EXPENSES = CUST_REPS + "\\" + CUST_REPS + "-expenses.json";
    
    public static final String CODE_ACC = "'acc'"; // [acc], placeholder fo code of accounts
    public static final String CODE_CC = "'cc'"; // [cc], placeholder fo code of cost centers
    public static final String CODE_ITEM = "'item'"; // [item], placeholder fo code of items
    
    private static final String REPORTS = "reports";
    private static final String REPORTS_ID = "id";
    private static final String REPORTS_REPORT = "report";
    private static final String CONFIG = "config";
    private static final String CONFIG_FILTER = "filter";
    private static final String CONFIG_MASK = "mask";
    private static final String ACCESSES = "accesses";
    private static final String USERS = "users";
    private static final String USERS_ID = "id";
    private static final String USERS_USER = "user";
    
    protected ArrayList<Report> maCustomReports;
    
    /**
     * Crates new custom reports parser.
     */
    public SCustomReportsParser() {
        maCustomReports = new ArrayList<>();
    }
    
    private Config createConfig(final JSONObject jsonObject) {
        String filter = (String) jsonObject.get(CONFIG_FILTER);
        String mask = (String) jsonObject.get(CONFIG_MASK);

        return new Config(filter, mask);
    }
    
    private ArrayList<User> createUsers(final JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<>();
        
        for (Object object : jsonArray.toArray()) {
            JSONObject jsonUser = (JSONObject) object;
            
            int id = new Long((long) jsonUser.get(USERS_ID)).intValue();
            String user = (String) jsonUser.get(USERS_USER);
            
            users.add(new User(id, user));
        }

        return users;
    }
    
    private ArrayList<Access> createAccesses(final JSONArray jsonArray) {
        ArrayList<Access> accesses = new ArrayList<>();
        
        for (Object object : jsonArray.toArray()) {
            JSONObject jsonAccess = (JSONObject) object;
            
            JSONArray jsonUsers = (JSONArray) jsonAccess.get(USERS);
            JSONObject jsonConfig = (JSONObject) jsonAccess.get(CONFIG);
            
            accesses.add(new Access(createUsers(jsonUsers), createConfig(jsonConfig)));
        }

        return accesses;
    }
    
    /**
     * Read custom reports settings.
     * @param path Path to required custom reports.
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     */
    public void readCustomReports(final String path) throws IOException, ParseException {
        maCustomReports.clear();
        
        JSONObject json = (JSONObject) new JSONParser().parse(new BufferedReader(new FileReader(path)));
        JSONArray jsonArray = (JSONArray) json.get(REPORTS);
        
        for (Object object : jsonArray.toArray()) {
            JSONObject jsonReport = (JSONObject) object;
            
            int id = new Long((long) jsonReport.get(REPORTS_ID)).intValue();
            String report = (String) jsonReport.get(REPORTS_REPORT);
            JSONObject jsonConfig = (JSONObject) jsonReport.get(CONFIG);
            JSONArray jsonAccesses = (JSONArray) jsonReport.get(ACCESSES);
            
            maCustomReports.add(new Report(id, report, createConfig(jsonConfig), createAccesses(jsonAccesses)));
        }
    }
    
    /**
     * Get all custom reports.
     * @return 
     */
    public ArrayList<Report> getCustomReports() {
        return maCustomReports;
    }
    
    /**
     * Get only custom reports that apply to user.
     * @param userId User ID.
     * @return 
     */
    public ArrayList<Report> getUserCustomReports(final int userId) {
        ArrayList<Report> filteredReports = new ArrayList<>();
        
        for (Report report : maCustomReports) {
            if (report.appliesToUser(userId)) {
                filteredReports.add(report);
            }
        }
        
        return filteredReports;
    }
    
    /**
     * Get custom report by ID.
     * @param reportId Report ID.
     * @return 
     */
    public Report getCustomReport(final int reportId) {
        Report requiredReport = null;
        
        for (Report report : maCustomReports) {
            if (reportId == report.Id) {
                requiredReport = report;
            }
        }
        
        return requiredReport;
    }
    
    public class Config {
        
        public String Filter;
        public String Mask;
        
        public Config(final String filter, final String mask) {
            Filter = filter;
            Mask = mask;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + " = {\n" +
                    CONFIG_FILTER + " : \"" + Filter + "\",\n" +
                    CONFIG_MASK + " : \"" + Mask + "\"\n" +
                    "}";
        }
    }
    
    public class User {
        
        public Integer Id;
        public String User;
        
        public User(final Integer userId, final String user) {
            Id = userId;
            User = user;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + " = {\n" +
                    USERS_ID + " : " + Id + ",\n" +
                    USERS_USER + ": \"" + User + "\"\n" +
                    "}";
        }
    }
    
    public class Access {
        
        public ArrayList<User> Users;
        public Config Config;
        
        public Access(final ArrayList<User> users, final Config config) {
            Users = users;
            Config = config;
        }
        
        @Override
        public String toString() {
            String users = "";
            
            for (User user : Users) {
                users += (users.isEmpty() ? "" : ",\n") + user;
            }
            
            return getClass().getSimpleName() + " = {\n" +
                    USERS + " : [" + users + "],\n" +
                    CONFIG + ": " + Config + "\n" +
                    "}";
        }
    }
    
    public class Report {
        
        public Integer Id;
        public String Report;
        public Config Config;
        public ArrayList<Access> Accesses;
        
        public Report(final Integer reportId, final String report, final Config config, final ArrayList<Access> accesses) {
            Id = reportId;
            Report = report;
            Config = config;
            Accesses = accesses;
        }
        
        public boolean appliesToUser(final int userId) {
            boolean applies = false;
            
            accesses:
            for (Access a : Accesses) {
                for (User u : a.Users) {
                    if (userId == u.Id) {
                        applies = true;
                        break accesses;
                    }
                }
            }
            
            return applies;
        }
        
        public User getUser(final int userId) {
            User user = null;
            
            accesses:
            for (Access a : Accesses) {
                for (User u : a.Users) {
                    if (userId == u.Id) {
                        user = u;
                        break accesses;
                    }
                }
            }
            
            return user;
        }
        
        public Access getUserAccess(final int userId) {
            Access access = null;
            
            accesses:
            for (Access a : Accesses) {
                for (User u : a.Users) {
                    if (userId == u.Id) {
                        access = a;
                        break accesses;
                    }
                }
            }
            
            return access;
        }
        
        @Override
        public String toString() {
            String accesses = "";
            
            for (Access a : Accesses) {
                accesses += (accesses.isEmpty() ? "" : ",\n") + a;
            }
            
            return getClass().getSimpleName() + " = {\n" +
                    REPORTS_ID + " : " + Id + ",\n" +
                    REPORTS_REPORT + " : \"" + Report + "\",\n" +
                    CONFIG + " : " + Config + ",\n" +
                    ACCESSES + " : [" + accesses + "]\n" +
                    "}";
        }
    }
    
    public static String humanizeConfigJson(final String configJson) {
        String humanizedConfig = configJson;
        
        humanizedConfig = humanizedConfig.replaceAll("OR ", "o ");
        humanizedConfig = humanizedConfig.replaceAll("AND ", "y ");
        humanizedConfig = humanizedConfig.replaceAll("LIKE ", "parecido a ");
        humanizedConfig = humanizedConfig.replaceAll("IN ", "en ");
        humanizedConfig = humanizedConfig.replaceAll("NOT ", "no ");
        humanizedConfig = humanizedConfig.replaceAll(CONFIG_FILTER, "filtrar");
        humanizedConfig = humanizedConfig.replaceAll(CONFIG_MASK, "enmascarar");
        
        return humanizedConfig;
    }
}
