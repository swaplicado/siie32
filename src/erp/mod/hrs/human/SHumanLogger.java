/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author César Orozco
 */
public class SHumanLogger {
private static final String LOG_FOLDER = "./HumandLogs";

    public static synchronized void log(
            final SHumanAction action,
            final String employeeCode,
            final String json,
            final SHumanResponse response
    ) {

        try {
            File folder = new File(LOG_FOLDER);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ".log";

            File file = new File(LOG_FOLDER + fileName);
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                writer.println("==================================================");

                writer.println("DATE: " + timestamp);
                writer.println("ACTION: " + action);
                writer.println("EMPLOYEE: " + employeeCode);
                writer.println("STATUS: " + response.getStatusCode());
                writer.println("SUCCESS: " + response.isSuccess());

                writer.println("JSON SENT:");
                writer.println(json);

                writer.println("RESPONSE:");
                writer.println(response.getResponseBody());

                writer.println("ERROR:");
                writer.println(response.getError());

                writer.println("==================================================");
                writer.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logError(
            final Exception exception
    ) {

        try {
            File folder = new File(LOG_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ".log";
            File file = new File(LOG_FOLDER + fileName);
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                writer.println("**************** ERROR ****************");
                writer.println("DATE: " + timestamp);
                exception.printStackTrace(writer);

                writer.println("****************************************");
                writer.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized void logSkipped(
        final SHumanAction action,
        final int employeeId,
        final int departmentId,
        final String reason
    ) {

        try {
            File folder = new File(LOG_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ".log";
            File file = new File(LOG_FOLDER + fileName);
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                writer.println("================ SKIPPED ================");
                writer.println("DATE: " + timestamp);
                writer.println("ACTION: " + action);
                writer.println("EMPLOYEE ID: " + employeeId);
                writer.println("DEPARTMENT ID: " + departmentId);
                writer.println("REASON: " + reason);
                writer.println("=========================================");

                writer.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
