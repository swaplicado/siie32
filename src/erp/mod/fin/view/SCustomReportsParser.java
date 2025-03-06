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
    
    public static final String JSON_ACC = "acc"; // JSON element for accounts
    public static final String JSON_CC = "cc"; // JSON element for cost centers
    public static final String JSON_ITEM = "item"; // JSON element for items
    
    private static final String REPORTS = "reports";
    private static final String REPORT_ID = "id";
    private static final String REPORT_REPORT = "report";
    private static final String CONFIG = "config";
    
    private static final String CONFIG_ACC = "acc"; // accounts SQL filter
    private static final String CONFIG_CC = "cc"; // cost centers SQL filter
    private static final String CONFIG_ITEM = "item"; // items SQL filter
    private static final String CONFIG_ITEM_MASK = "itemMask"; // items masked SQL filter
    
    private static final String USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_USER = "user";
    
    protected ArrayList<Report> maCustomReports;
    
    /**
     * Crates new custom reports parser.
     */
    public SCustomReportsParser() {
        maCustomReports = new ArrayList<>();
    }
    
    private Config createConfig(final JSONObject jsonObject) {
        String accounts = (String) jsonObject.get(CONFIG_ACC);
        String costCenters = (String) jsonObject.get(CONFIG_CC);
        String items = (String) jsonObject.get(CONFIG_ITEM);
        String itemsSumm = (String) jsonObject.get(CONFIG_ITEM_MASK);

        return new Config(accounts, costCenters, items, itemsSumm);
    }
    
    private ArrayList<User> createUsers(final JSONArray jsonUsers) {
        ArrayList<User> users = new ArrayList<>();
        
        for (Object object : jsonUsers.toArray()) {
            JSONObject jsonUser = (JSONObject) object;
            
            int id = new Long((long) jsonUser.get(USER_ID)).intValue();
            String user = (String) jsonUser.get(USER_USER);
            
            users.add(new User(id, user, createConfig((JSONObject) jsonUser.get(CONFIG))));
        }

        return users;
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
            
            int id = new Long((long) jsonReport.get(REPORT_ID)).intValue();
            String report = (String) jsonReport.get(REPORT_REPORT);
            
            maCustomReports.add(new Report(id, report, createConfig((JSONObject) jsonReport.get(CONFIG)), createUsers((JSONArray) jsonReport.get(USERS))));
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
    public ArrayList<Report> getCustomReports(final int userId) {
        ArrayList<Report> filteredReports = new ArrayList<>();
        
        for (Report report : maCustomReports) {
            if (report.appliesToUser(userId)) {
                filteredReports.add(report);
            }
        }
        
        return filteredReports;
    }
    
    /**
     * Get read custom report by ID.
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
        
        public String Accounts;
        public String CostCenters;
        public String Items;
        public String ItemsMask;
        
        public Config(final String accounts, final String costCenters, final String items, final String itemsMask) {
            Accounts = accounts;
            CostCenters = costCenters;
            Items = items;
            ItemsMask = itemsMask;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + " = {\n" +
                    CONFIG_ACC + " : \"" + Accounts + "\",\n" +
                    CONFIG_CC + " : \"" + CostCenters + "\",\n" +
                    CONFIG_ITEM + " : \"" + Items + "\",\n" +
                    CONFIG_ITEM_MASK + " : \"" + ItemsMask + "\" }";
        }
    }
    
    public class User {
        
        public Integer Id;
        public String User;
        public Config Config;
        
        public User(final Integer id, final String user, final Config config) {
            Id = id;
            User = user;
            Config = config;
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + " = {\n" +
                    USER_ID + " : " + Id + ",\n" +
                    USER_USER + ": \"" + USER_USER + "\",\n" +
                    CONFIG + " : " + Config + " }";
        }
    }
    
    public class Report {
        
        public Integer Id;
        public String Report;
        public Config Config;
        public ArrayList<User> Users;
        
        public Report(final Integer id, final String report, final Config config, final ArrayList<User> users) {
            Id = id;
            Report = report;
            Config = config;
            Users = users;
        }
        
        public boolean appliesToUser(final int userId) {
            boolean applies = false;
            
            for (User user : Users) {
                if (userId == user.Id) {
                    applies = true;
                    break;
                }
            }
            
            return applies;
        }
        
        public User getUser(final int userId) {
            User requiredUser = null;
            
            for (User user : Users) {
                if (userId == user.Id) {
                    requiredUser = user;
                    break;
                }
            }
            
            return requiredUser;
        }
        
        @Override
        public String toString() {
            String users = "";
            
            for (User user : Users) {
                users += (users.isEmpty() ? "" : ",\n") + user;
            }
            
            return getClass().getSimpleName() + " = {\n" +
                    REPORT_REPORT + " : \"" + Report + "\",\n" +
                    CONFIG + " : " + Config + ",\n" +
                    USERS + " : [" + users + "] }";
        }
    }
    
    public static String humanizeConfigJson(final String configJson) {
        String humanizedConfig = configJson;
        
        humanizedConfig = humanizedConfig.replaceAll("OR ", "o ");
        humanizedConfig = humanizedConfig.replaceAll("AND ", "y ");
        humanizedConfig = humanizedConfig.replaceAll("LIKE ", "parecido a ");
        humanizedConfig = humanizedConfig.replaceAll("IN ", "en ");
        humanizedConfig = humanizedConfig.replaceAll("NOT ", "no ");
        
        return humanizedConfig;
    }
}
