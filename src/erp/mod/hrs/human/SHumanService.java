/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mbps.data.SDataBizPartner;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Cesar Orozco
 */
public class SHumanService {

    private final ObjectMapper moMapper;

    public SHumanService() {
        moMapper = new ObjectMapper();
    }

    /**
     * Sincroniza empleado con Humand.
     */
    public void syncEmployeeToHuman(
            final SHumanConfig config,
            final Statement statement,
            final int employeeId,
            final SHumanAction action
    ) throws Exception {

        ResultSet resultSet = null;
        Statement humanConnection = statement.getConnection().createStatement();
        try {

            String sql = "SELECT "
                    + "e.id_emp AS id_emp, "
                    + "b.firstname, "
                    + "b.lastname, "
                    + "e.num AS emp_num, "
                    + "e.dt_bir AS birthday, "
                    + "e.b_act AS is_active, "
                    + "e.dt_ben AS benefits_since, "
                    + "e.dt_hire AS last_hire, "
                    + "e.dt_dis_n AS last_dismissal, "
                    + "bbc.tel_num_01 AS tel_emp, "
                    + "bbc.tel_num_02 AS tel_com, "
                    + "bbc.tel_ext_02 AS ext_com, "
                    + "bbc.email_01 AS mail_emp, "
                    + "bbc.email_02 AS mail_com, "
                    + "dep.id_dep AS id_dep, "
                    + "dep.name AS dep_name, "
                    + "pos.id_pos, "
                    + "pos.name AS pos_name "
                    + "FROM erp.hrsu_emp AS e "
                    + "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = e.fk_dep "
                    + "INNER JOIN erp.hrsu_pos AS pos ON pos.id_pos = e.fk_pos "
                    + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = e.id_emp "
                    + "INNER JOIN erp.bpsu_bpb AS bb ON bb.fid_bp = b.id_bp "
                    + "AND bb.fid_tp_bpb = 1 "
                    + "LEFT OUTER JOIN erp.bpsu_bpb_con AS bbc "
                    + "ON bbc.id_bpb = bb.id_bpb "
                    + "AND bbc.id_con = 1 "
                    + "WHERE e.id_emp = " + employeeId;

            resultSet = humanConnection.executeQuery(sql);

            if (!resultSet.next()) {
                throw new Exception("Empleado no encontrado: " + employeeId);
            }
            
            int currentEmployeeId = resultSet.getInt("id_emp");
            int currentDepartmentId = resultSet.getInt("id_dep");

            if (config.getSkipEmployees().contains(currentEmployeeId)) {
                SHumanLogger.logSkipped(
                        action,
                        currentEmployeeId,
                        currentDepartmentId,
                        "Empleado excluido por configuración skip-emps"
                );
                return;
            }

            if (config.getSkipDepartments().contains(currentDepartmentId)) {
                SHumanLogger.logSkipped(
                        action,
                        currentEmployeeId,
                        currentDepartmentId,
                        "Departamento excluido por configuración skip-deps"
                );
                return;
            }

            String employeeCode = safe(resultSet.getString("emp_num"));
            String json = buildEmployeeJson(resultSet);
            SHumanResponse response;

            switch (action) {
                case CREATE:
                    response = SHumanHttpClient.sendRequest(
                            config,
                            "POST",
                            SHumanEndpoints.users(config),
                            json
                    );
                    break;
                case UPDATE:
                    response = SHumanHttpClient.sendRequest(
                            config,
                            "PATCH",
                            SHumanEndpoints.user(config,employeeCode),
                            json
                    );
                    break;
                case DELETE:
                    response = SHumanHttpClient.sendRequest(
                            config,
                            "DELETE",
                            SHumanEndpoints.user(config,employeeCode),
                            null
                    );
                    break;
                default:
                    throw new Exception(
                            "Acción no soportada: " + action
                    );
            }

            SHumanLogger.log(action,employeeCode,json,response);
        }
        catch (Exception e) {
            SHumanLogger.logError(e);
            throw e;
        }
        finally {

            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * Construye JSON para Humand.
     */
    private String buildEmployeeJson(
            final ResultSet resultSet
    ) throws Exception {
        
        java.util.Date hiringDate = resultSet.getDate("benefits_since");

        if (hiringDate == null) {
            hiringDate = resultSet.getDate("last_hire");
        }
        
        String json = "{"
                + "\"employeeInternalId\":\"" + safe(resultSet.getString("emp_num")) + "\","
                + "\"password\":\"87654311\","
                + "\"email\":\"" + safe(resultSet.getString("mail_com")) + "\","
                + "\"firstName\":\"" + safe(resultSet.getString("firstname")) + "\","
                + "\"lastName\":\"" + safe(resultSet.getString("lastname")) + "\","
                + "\"phoneNumber\":\"" + safe(resultSet.getString("tel_com")) + "\","
                + "\"hiringDate\":\"" + formatDate(hiringDate) + "\","
                + "\"birthdate\":\"" + formatDate(resultSet.getDate("birthday")) + "\""
                + "}";
        return json;
    }

    /**
     * Formato fecha.
     */
    private String formatDate(final java.util.Date date) {

        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * Evita nulls.
     */
    private String safe(final String value) {
        return value == null ? "" : value;
    }
}