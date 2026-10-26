/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

/**
 *
 * @author César Orozco
 */
public class SHumanEndpoints {

    public static final String USERS = "/users";

    public static String users(final SHumanConfig config) {
        return config.getBaseUrl() + USERS;
    }

    public static String user(
            final SHumanConfig config,
            final String employeeId
    ) {
        return config.getBaseUrl() + USERS + "/" + employeeId;
    }
}
