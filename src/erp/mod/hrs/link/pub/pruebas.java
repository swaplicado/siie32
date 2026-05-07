/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.pub;

import com.fasterxml.jackson.core.JsonProcessingException;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SShareDB;
import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author Cesar Orozco
 */
public class pruebas {
    public static void main(String[] args) {
        try {

            StringBuilder sb = new StringBuilder();

            sb.append("{")
              .append("\"to_insert\":true,")
              .append("\"application_id\":6434,")
              .append("\"folio\":\"V-01020-23\",")
              .append("\"employee_id\":3505,")
              .append("\"company_id\":2852,")
              .append("\"type_pay_id\":2,")
              .append("\"tp_abs\":1,")
              .append("\"cl_abs\":3,")
              .append("\"date_send\":\"2026-04-09\",")
              .append("\"date_ini\":\"2026-06-13\",")
              .append("\"date_end\":\"2026-06-13\",")
              .append("\"total_days\":1,")
              .append("\"rows\":[{")
                  .append("\"breakdown_id\":10130,")
                  .append("\"folio\":\"V-01020-23-1\",")
                  .append("\"effective_days\":1,")
                  .append("\"year\":2024,")
                  .append("\"anniversary\":8,")
                  .append("\"start_date\":\"2026-06-13\",")
                  .append("\"end_date\":\"2026-06-13\",")
                  .append("\"lDays\":[{")
                      .append("\"date\":\"2026-06-13\",")
                      .append("\"bussinesDay\":true,")
                      .append("\"taked\":true,")
                      .append("\"isOptional\":false")
                  .append("}]")
              .append("}]")
            .append("}");

            String json = sb.toString();

            SShareData clase = new SShareData();
            clase.setJsonConn("{\"dbHost\":\"localhost\",\"dbName\":\"erp\",\"dbPort\":\"3306\",\"dbUser\":\"root\",\"dbPass\":\"msroot\",\"dbMainId\":\"2852\"}");
            String result = clase.insertIncidents(json);

            System.out.println("Resultado: " + result);

        } catch (ParseException | SQLException | ClassNotFoundException | JsonProcessingException | SConfigException e) {
            e.printStackTrace();
        }
    }
}
