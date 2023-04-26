/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
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
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public class SPayrollUtils {
    
    /**
     * Retorna una lista de empleados con sus respectivas configuraciones y en cada una de ellas
     * la indicación de si ganó el pago consultado.
     * 
     * @param client
     * @param employeesIds lista de empleados a consultar
     * @param payType tipo de pago (1 = semana, 2 = quincena)
     * @param dtDate fecha de corte (fecha final de la nómina actual)
     * @param cutOffDay día de corte configurado para el pago de semana
     * @param weekLag semanas de retraso del cálculo para fechas de corte
     * @param yearPeriod año de la nómina
     * @param payrollNumber número de nómina (para nóminas quincenales)
     * @param bonus lista de bonos a pagar
     * @param companyKey código de la empresa actual (para el guardado del log)
     * @param employeesWithCurBonus empleados que son acreedores al bono actual
     * 
     * @return lista de empleados con sus respectivas configuraciones
     * 
     * @throws SQLException 
     */
    public static HashMap<Integer, ArrayList<SEarnConfiguration>> getBonusPayments(SGuiClient client, 
                                                                                ArrayList<Integer> employeesIds,
                                                                                int payType,
                                                                                Date dtDate,
                                                                                int cutOffDay,
                                                                                int weekLag,
                                                                                int yearPeriod,
                                                                                int payrollNumber,
                                                                                ArrayList<Integer> bonus,
                                                                                String companyKey,
                                                                                ArrayList<Integer> employeesWithCurBonus
                                                                                ) throws SQLException {
        
        // Obtener empleados configurados para bono, despensa en especie y superbono.
        HashMap<Integer, ArrayList<SEarnConfiguration>> configuredEmployees = SPayrollUtils.getConfiguration(client, employeesIds, bonus, dtDate);
        ArrayList<Integer> configuredEmployeesIds = new ArrayList<>();
        configuredEmployeesIds.addAll(configuredEmployees.keySet());
        
        Date[] dates = SPayrollUtils.getCutOffDates(client, payType, dtDate, yearPeriod, payrollNumber, cutOffDay, weekLag);
        if (dates == null) {
            return null;
        }
        
        HashMap<Integer, Integer> empPolicies = SPayrollUtils.getEmployeesCheckerPolicy(client, employeesIds);
        ArrayList<Integer> employeesToRequest = new ArrayList<>();
        ArrayList<Integer> directEmployees = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : empPolicies.entrySet()) {
            if (entry.getValue() != SHrsConsts.CHECKER_POLICY_FREE) {
                employeesToRequest.add(entry.getKey());
            }
            else {
                directEmployees.add(entry.getKey());
            }
        }
        
        if (bonus.contains(SPayrollBonusUtils.BONUS)) {
            HashMap<Integer, SBonusAux> empBonusList = SPayrollUtils.getWhoWinBonus(client, employeesToRequest, payType, dates[0], dates[1], companyKey);
            for (Map.Entry<Integer, ArrayList<SEarnConfiguration>> entry : configuredEmployees.entrySet()) {
                Integer idEmployee = entry.getKey();
                ArrayList<SEarnConfiguration> earnList = entry.getValue();
                SBonusAux oBonusAux = empBonusList.get(idEmployee);
                for (SEarnConfiguration oEarnConfiguration : earnList) {
                    if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.BONUS) {
                        if (directEmployees.contains(idEmployee)) {
                            oEarnConfiguration.setHasWon(1d);
                            oEarnConfiguration.setComments("No checa. ");
                            break;
                        }
                        
                        if (oBonusAux != null) {
                            oEarnConfiguration.setHasWon(oBonusAux.getHasBonus());
                            oEarnConfiguration.setComments(oBonusAux.getComments());
                        }
                        else {
                            oEarnConfiguration.setHasWon(0d);
                            oEarnConfiguration.setComments("Sin información. ");
                        }
                        
                        break;
                    }
                }
            }
        }
        
        if (!bonus.contains(SPayrollBonusUtils.PANTRY) && !bonus.contains(SPayrollBonusUtils.SUPER_BONUS) && !bonus.contains(SPayrollBonusUtils.PANTRY_FOREIGNERS)) {
            return configuredEmployees;
        }
        
        for (Map.Entry<Integer, ArrayList<SEarnConfiguration>> entry : configuredEmployees.entrySet()) {
            ArrayList<SEarnConfiguration> configs = entry.getValue();
            for (SEarnConfiguration oEarnConfiguration : configs) {
                if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.PANTRY || oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.PANTRY_FOREIGNERS) {
                    String comments = "";
                    double hasPantry = 0d;
                    if (directEmployees.contains(entry.getKey())) {
                        hasPantry = 1d;
                        comments += "No checa. ";
                    }
                    else {
                        boolean withCurrentBonus = employeesWithCurBonus.contains(entry.getKey());
                        if (withCurrentBonus) {
                            hasPantry = SPayrollBonusUtils.hasPantry(client, entry.getKey(), dtDate, 
                                        SPrepayrollUtils.isWithPreviousPayment(client.getSession(), oEarnConfiguration.getIdBonus()));
                        }
                    }
                    
                    oEarnConfiguration.setAmount(oEarnConfiguration.getAmount() * hasPantry);
                    oEarnConfiguration.setHasWon(hasPantry);
                    oEarnConfiguration.setComments(hasPantry > 0d ? comments : "No ganó los bonos requeridos");
                }
                if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.SUPER_BONUS) {
                    String comments = "";
                    double hasSuperBonus = 0d;
                    if (directEmployees.contains(entry.getKey())) {
                        hasSuperBonus = 1d;
                        comments += "No checa. ";
                    }
                    else {
                        boolean withCurrentBonus = employeesWithCurBonus.contains(entry.getKey());
                        if (withCurrentBonus) {
                            hasSuperBonus = SPayrollBonusUtils.hasSuperBonus(client, entry.getKey(), dtDate);
                        }
                    }
                    
                    oEarnConfiguration.setHasWon(hasSuperBonus);
                    oEarnConfiguration.setComments(hasSuperBonus > 0d ? comments : "No ganó los bonos requeridos");
                }
//                if (oEarnConfiguration.getIdBonus() == SPayrollBonusUtils.BONUS) {
//                    if (! idsList.contains(entry.getKey())) {
//                        oEarnConfiguration.setComments("Sin información");
//                        oEarnConfiguration.setHasWon(0d);
//                    }
//                }
            }
        }
        
        return configuredEmployees;
    }
    
    /**
     * Obtiene un arreglo con las fechas de corte correspondietes a la semana o quincena
     * solicitadas.
     * 
     * @param client
     * @param payType
     * @param dtDate
     * @param yearPeriod
     * @param payrollNumber
     * @param cutDay
     * @param weekLag
     * @return 
     */
    private static Date[] getCutOffDates(SGuiClient client, int payType, Date dtDate, int yearPeriod, int payrollNumber, int cutDay, int weekLag) {
        Date dates[] = null;
                if (payType == SModSysConsts.HRSS_TP_PAY_WEE) {
                    if (cutDay == 0) {
                        client.showMsgBoxError("No existe configuración para día de corte");
                        return null;
                    }

                    dates = SPrepayrollUtils.getPrepayrollDateRangeByCutDay(cutDay, dtDate, weekLag);
                    DateTime dateTime = new DateTime(dtDate);
                    Date endDatePrevious = dateTime.plusDays(-7).toDate();
                    Date[] dates1 = SPrepayrollUtils.getPrepayrollDateRangeByCutDay(cutDay, endDatePrevious, weekLag);
                    dates[0] = dates1[0];
                }
                else {
                    dates = SPrepayrollUtils.getPrepayrollDateRangeByTable(client, payType, yearPeriod, payrollNumber);
                    if (dates == null || dates[0] == null || dates[1] == null) {
                        client.showMsgBoxError("No se pudieron obtener fechas para prenómina");
                        return null;
                    }
                }
                
        return dates;
    }
    
    /**
     * Determina de los empleados recibidos quiénes ganaron el bono, basándose 
     * únicamente en lo consultado desde el sistema externo
     * 
     * @param client
     * @param employees
     * @param payType
     * @param tStartDate
     * @param tEndDate
     * @param companyKey
     * @return 
     */
    private static HashMap<Integer, SBonusAux> getWhoWinBonus(SGuiClient client, 
                                                            ArrayList<Integer> employees,
                                                            int payType,
                                                            Date tStartDate, 
                                                            Date tEndDate,
                                                            String companyKey) {
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
            return null;
        }
        
        SAbsDelays delaysAndAbsEmployees = SPayrollUtils.requestAbsAndDelays(client, url, employees, tStartDate, tEndDate, payType, companyKey);
        
        if (delaysAndAbsEmployees == null) {
            return null;
        }
        
        HashMap<Integer, SBonusAux> bonusList = new HashMap<>();
        SBonusAux oBonusAux;
        boolean hasBonus;
        String comments;
        for (SDataRow row : delaysAndAbsEmployees.getRows()) {
            oBonusAux = new SBonusAux();
            oBonusAux.setIdEmployee(row.getIdEmployee());
            comments = "";
            hasBonus = false;
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
            oBonusAux.setHasBonus(hasBonus ? 1d : 0d);
            oBonusAux.setComments(comments);
            
            bonusList.put(row.getIdEmployee(), oBonusAux);
        }
        
        return bonusList;
    }
    
    /**
     * Obtiene las configuraciones de los empleados junto a sus percepciones
     * 
     * @param client
     * @param employeesIds
     * @param bonus
     * @param payDay
     * @return
     * @throws SQLException 
     */
    public static HashMap<Integer, ArrayList<SEarnConfiguration>> getConfiguration(SGuiClient client,
                                        ArrayList<Integer> employeesIds, 
                                            ArrayList<Integer> bonus, 
                                                Date payDay) throws SQLException {
        String sqlEarnByBonus = "SELECT DISTINCT fk_ear FROM "
                + SModConsts.TablesMap.get(SModConsts.HRS_COND_EAR) + " "
                + "WHERE NOT b_del AND fk_bonus = ";
        
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
                        "    " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " e " +
                        "  INNER JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " bp ON e.id_emp = bp.id_bp " +
                        "  INNER JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " mem ON e.id_emp = mem.id_emp " +
                        "  INNER JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.HRS_COND_EAR) + " cear ON cear.fk_ref = ";
        
        String sql1 = "INNER JOIN" +
                        " " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " ear ON cear.fk_ear = ear.id_ear " +
                        " INNER JOIN " +
                        " " + SModConsts.TablesMap.get(SModConsts.HRSS_BONUS) + " bonus ON cear.fk_bonus = bonus.id_bonus " +
                        " WHERE " +
                        " cear.fk_ear = ";
        String sql2 =   "  AND cear.fk_scope = ";
        String sql3 =   "  AND cear.fk_bonus = ";
        
        String sql4 =   " AND cear.b_del = FALSE " +
                        " AND cear.dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(payDay) + "' " +
                        " AND LENGTH(e.grocery_srv_acc) > 0 " +
                        " AND mem.b_del = FALSE " +
                        " AND e.b_act = TRUE " +
                        " AND e.b_del = FALSE " +
                        " AND ((cear.for_uni = 1 AND e.b_uni) OR (cear.for_uni = 2 AND NOT e.b_uni) OR (cear.for_uni = 0)) " +
                        " AND ((cear.fk_tp_pay_n = 1 AND e.fk_tp_pay = 1) OR (cear.fk_tp_pay_n = 2 AND e.fk_tp_pay = 2) OR (cear.fk_tp_pay_n IS NULL));";
        
        HashMap<Integer, ArrayList<SEarnConfiguration>> confDept = new HashMap<>();
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
    
    /**
     * Devuelve una lista de los empleados con su respectiva política del checador.
     * Política Estricta = 1
     * Política libre = 2
     * Política eventual 3
     * 
     * @param client
     * @param lEmployees
     * @return 
     */
    private static HashMap<Integer, Integer> getEmployeesCheckerPolicy(SGuiClient client, ArrayList<Integer> lEmployees) {
        HashMap<Integer, Integer> employees = new HashMap<>();
        String query = "SELECT id_emp, checker_policy FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " WHERE id_emp IN (";
        
        String emps = lEmployees.stream().map(Object::toString)
                                    .collect(Collectors.joining(","));
        
        try {
            ResultSet res = client.getSession().getStatement().executeQuery(query + emps + ");");
            
            while (res.next()) {
                employees.put(res.getInt("id_emp"), res.getInt("checker_policy"));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SPayrollUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return employees;
    }
    
    /**
     * Consulta la info del checador del sistema CAP
     * 
     * @param client
     * @param sURL
     * @param lEmployees
     * @param tStartDate
     * @param tEndDate
     * @param payType
     * @param companyKey
     * @return 
     */
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
