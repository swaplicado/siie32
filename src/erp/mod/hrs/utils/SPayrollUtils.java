/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.hrs.form.SFormPayroll;
import erp.mod.hrs.link.db.SAbsDelays;
import erp.mod.hrs.link.db.SDataRow;
import erp.mod.hrs.link.utils.SUtilsJSON;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public class SPayrollUtils {
    
    public static HashMap<Integer, ArrayList<SEarnConfiguration>> process(SGuiClient client,
                                        ArrayList<Integer> employeesIds, 
                                            ArrayList<Integer> bonus, Date tStartDate, Date tEndDate, int payType, String companyKey) throws SQLException {
        
        HashMap<Integer, ArrayList<SEarnConfiguration>> configuredEmployees = SPayrollUtils.getConfiguration(client, employeesIds, bonus, tEndDate);
        
        ArrayList<Integer> configuredEmployeesIds = new ArrayList();
        configuredEmployeesIds.addAll(configuredEmployees.keySet());
        List<Integer> idsList = new ArrayList<>();
        
        if (bonus.contains(SPayrollBonusUtils.BONUS)) {
            String urls = "";
            String url = "";
            try {
                //localhost:8080/CAP/public/api/prepayroll

                urls = SCfgUtils.getParamValue(client.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_CAP);
                String arrayUrls[] = urls.split(";");
                url = arrayUrls[1];
                
            }
            catch (Exception ex) {
                Logger.getLogger(SFormPayroll.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            SAbsDelays delaysAndAbsEmployees = SPayrollUtils.requestAbsAndDelays(client, url, configuredEmployeesIds, tStartDate, tEndDate, payType, companyKey);
            
            if (delaysAndAbsEmployees == null) {
                return null;
            }
            
            for (SDataRow row : delaysAndAbsEmployees.getRows()) {
                for (SEarnConfiguration oEarnConfiguration : configuredEmployees.get(row.getIdEmployee())) {
                    if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.BONUS) {
                        boolean hasBonus = false;
                        String comments = "";
                        if (! row.isLostBonus()) {
                            hasBonus = SPayrollBonusUtils.hasBonus(tStartDate, tEndDate, row);
                            comments += hasBonus ? "" : "Con retardos o faltas.";
                        }
                        else {
                            if (row.getIncidents() != null && row.getIncidents().size() > 0) {
                                for (String incident : row.getIncidents()) {
                                    comments += incident + " ";
                                }
                            }
                        }
                        
                        comments += (row.isHasNoChecks() ? " Omitió checar." : "");
                        oEarnConfiguration.setHasWon(hasBonus ? 1d : 0d);
                        oEarnConfiguration.setComments(comments);
                    }
                }
            }
            
            idsList = delaysAndAbsEmployees.getRows().stream()
                                   .map(SDataRow::getIdEmployee)
                                   .collect(Collectors.toList());
        }
        
        for (Map.Entry<Integer, ArrayList<SEarnConfiguration>> entry : configuredEmployees.entrySet()) {
            ArrayList<SEarnConfiguration> configs = entry.getValue();
            for (SEarnConfiguration oEarnConfiguration : configs) {
                if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.PANTRY) {
                    double hasPantry = SPayrollBonusUtils.hasPantry(client, entry.getKey(), tEndDate);
                    oEarnConfiguration.setAmount(oEarnConfiguration.getAmount() * hasPantry);
                    oEarnConfiguration.setHasWon(hasPantry);
                    oEarnConfiguration.setComments(hasPantry > 0d ? "" : "No ganó los bonos requeridos");
                }
                if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.SUPER_BONUS) {
                    double hasSuperBonus = SPayrollBonusUtils.hasSuperBonus(client, entry.getKey(), tEndDate);
                    oEarnConfiguration.setHasWon(hasSuperBonus);
                    oEarnConfiguration.setComments(hasSuperBonus > 0d ? "" : "No ganó los bonos requeridos");
                }
                if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.BONUS) {
                    if (! idsList.contains(entry.getKey())) {
                        oEarnConfiguration.setComments("Sin información");
                        oEarnConfiguration.setHasWon(0d);
                    }
                }
            }
        }
        
        return configuredEmployees;
    }
    
    public static HashMap<Integer, ArrayList<SEarnConfiguration>> getConfiguration(SGuiClient client,
                                        ArrayList<Integer> employeesIds, 
                                            ArrayList<Integer> bonus, 
                                                Date payDay) throws SQLException {
        String sqlEarnByBonus = "SELECT DISTINCT fk_ear FROM hrs_cond_ear WHERE NOT b_del AND fk_bonus = ";
        
        final int scopeGbl = 1;
        final int scopeDpt = 2;
        final int scopEmp = 3;
        String onGlbl = " 0 ";
        String onDept = " e.fk_dep ";
        String onEmp = " e.id_emp ";
        
        String sql = "SELECT  " +
                        "    e.id_emp, " +
                        "    e.num, " +
                        "    bp.bp, " +
                        "    ear.name AS ear_name, " +
                        "    bonus.name AS bonus_name, " +
                        "    cear.amt, " +
                        "    cear.fk_ear " +
                        " FROM " +
                        "    erp.hrsu_emp e " +
                        "  INNER JOIN " +
                        "    erp.bpsu_bp bp ON e.id_emp = bp.id_bp " +
                        "  INNER JOIN " +
                        "    hrs_cond_ear cear ON cear.fk_ref = ";
        
        String sql1 = "INNER JOIN" +
                        " hrs_ear ear ON cear.fk_ear = ear.id_ear " +
                        " INNER JOIN " +
                        " erp.hrss_bonus bonus ON cear.fk_bonus = bonus.id_bonus " +
                        " WHERE " +
                        " cear.fk_ear = ";
        String sql2 =   "  AND cear.fk_scope = ";
        String sql3 =   "  AND cear.fk_bonus = ";
        
        String sql4 =   "       AND cear.b_del = FALSE " +
                        "        AND cear.dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(payDay) + "' " +
                        "        AND e.b_act = TRUE " +
                        "        AND e.b_del = FALSE;";
        
        HashMap<Integer, ArrayList<SEarnConfiguration>> confDept = new HashMap();
        for (Integer bonu : bonus) {
            ArrayList<Integer> earnings = new ArrayList<>();
            ResultSet resultSetEarByBonus = null;

            resultSetEarByBonus = client.getSession().getStatement().executeQuery(sqlEarnByBonus + bonu);
            while (resultSetEarByBonus.next()) {
                earnings.add(resultSetEarByBonus.getInt("fk_ear"));
            }
            
            for (Integer earn : earnings) {
                String sqlGlbl = sql + onGlbl + sql1 + earn + sql2 + scopeGbl + sql3 + bonu + sql4;
                String sqlDepts = sql  + onDept + sql1 + earn + sql2 + scopeDpt + sql3 + bonu + sql4;
                String sqlEmp = sql  + onEmp + sql1 + earn + sql2 + scopEmp + sql3 + bonu + sql4;

                SEarnConfiguration conf = null;
                int idEmp = 0;
                ResultSet resultSetGlbl = null;

                resultSetGlbl = client.getSession().getStatement().executeQuery(sqlGlbl);
                while (resultSetGlbl.next()) {
                    idEmp = resultSetGlbl.getInt("id_emp");
                    if (! employeesIds.contains(idEmp)) {
                        continue;
                    }

                    conf = new SEarnConfiguration();

                    conf.setAmount(resultSetGlbl.getDouble("amt"));
                    conf.setIdEarning(resultSetGlbl.getInt("fk_ear"));
                    conf.setIdBonus(bonu);
                    conf.setBonus(resultSetGlbl.getString("bonus_name"));
                    conf.setNumEmployee(resultSetGlbl.getString("num"));
                    conf.setEmployee(resultSetGlbl.getString("bp"));
                    conf.setEarning(resultSetGlbl.getString("ear_name"));

                    if (confDept.containsKey(idEmp)) {
                        confDept.get(idEmp).add(conf);
                        continue;
                    }

                    ArrayList<SEarnConfiguration> ar = new ArrayList<>();
                    ar.add(conf);
                    confDept.put(idEmp, ar);
                }

                ResultSet resultSet = null;

                resultSet = client.getSession().getStatement().executeQuery(sqlDepts);
                while (resultSet.next()) {
                    idEmp = resultSet.getInt("id_emp");
                    if (! employeesIds.contains(idEmp)) {
                        continue;
                    }

                    conf = new SEarnConfiguration();

                    conf.setAmount(resultSet.getDouble("amt"));
                    conf.setIdEarning(resultSet.getInt("fk_ear"));
                    conf.setIdBonus(bonu);
                    conf.setBonus(resultSet.getString("bonus_name"));
                    conf.setNumEmployee(resultSet.getString("num"));
                    conf.setEmployee(resultSet.getString("bp"));
                    conf.setEarning(resultSet.getString("ear_name"));

                    if (confDept.containsKey(idEmp)) {
                        boolean exists = false;
                        ArrayList<SEarnConfiguration> ar = confDept.get(idEmp);
                        for (SEarnConfiguration earnConfiguration : ar) {
                            if (earnConfiguration.getIdEarning() == earn) {
                                earnConfiguration.setAmount(conf.getAmount());
                                exists = true;
                                break;
                            }
                        }

                        if (! exists) {
                            confDept.get(idEmp).add(conf);
                        }

                        continue;
                    }

                    ArrayList<SEarnConfiguration> ar = new ArrayList<>();
                    ar.add(conf);
                    confDept.put(idEmp, ar);
                }

                ResultSet resultSet1 = null;

                resultSet1 = client.getSession().getStatement().executeQuery(sqlEmp);
                while (resultSet1.next()) {
                    idEmp = resultSet1.getInt("id_emp");
                    if (! employeesIds.contains(idEmp)) {
                        continue;
                    }

                    conf = new SEarnConfiguration();

                    conf.setAmount(resultSet1.getDouble("amt"));
                    conf.setIdEarning(resultSet1.getInt("fk_ear"));
                    conf.setIdBonus(bonu);
                    conf.setBonus(resultSet1.getString("bonus_name"));
                    conf.setNumEmployee(resultSet1.getString("num"));
                    conf.setEmployee(resultSet1.getString("bp"));
                    conf.setEarning(resultSet1.getString("ear_name"));

                    if (confDept.containsKey(idEmp)) {
                        boolean exists = false;
                        ArrayList<SEarnConfiguration> ar = confDept.get(idEmp);
                        for (SEarnConfiguration earnConfiguration : ar) {
                            if (earnConfiguration.getIdEarning() == earn) {
                                earnConfiguration.setAmount(conf.getAmount());
                                exists = true;
                                break;
                            }
                        }

                        if (! exists) {
                            confDept.get(idEmp).add(conf);
                        }

                        continue;
                    }

                    ArrayList<SEarnConfiguration> arr = new ArrayList<>();
                    arr.add(conf);
                    confDept.put(idEmp, arr);
                }
            }
        }
        
        
        
        return confDept;
    }
    
    private static SAbsDelays requestAbsAndDelays(SGuiClient client, String sURL, ArrayList<Integer> lEmployees, Date tStartDate, Date tEndDate, int payType, String companyKey) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String employees = lEmployees.stream().map(Object::toString)
                    .collect(Collectors.joining(","));

            String url = sURL;

            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            String startDate = df.format(tStartDate);
            String endDate = df.format(tEndDate);

            String query = String.format("start_date=%s&end_date=%s&employees=%s&pay_type=%s",
                    URLEncoder.encode(startDate, charset),
                    URLEncoder.encode(endDate, charset),
                    URLEncoder.encode(employees, charset),
                    URLEncoder.encode(payType + "", charset));

            URLConnection connection = new URL(url + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            InputStream response = connection.getInputStream();

            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
                
                ObjectMapper mapper = new ObjectMapper();
                SCAPResponse resp = mapper.readValue(responseBody, SCAPResponse.class);
                switch (resp.getCode()) {
                    case SCAPResponse.RESPONSE_OK:
                        SAbsDelays absDelays = resp.getAbsData();
                        SUtilsJSON.writeJSON(startDate, endDate, responseBody, companyKey, SUtilsJSON.VOUCHER);
                        return absDelays;
                        
                    case SCAPResponse.RESPONSE_NOT_VOBO:
                    case SCAPResponse.RESPONSE_ERROR:
                        client.showMsgBoxError(resp.getMessage());
                        break;
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SPayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SPayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SPayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
