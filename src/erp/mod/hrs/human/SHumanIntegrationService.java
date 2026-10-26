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

import sa.lib.gui.SGuiSession;

public class SHumanIntegrationService {
    
     public static void syncEmployee(
            final SGuiSession session,
            final int employeeId,
            final SHumanAction action
    ) {

        try {
            SHumanConfig config = new SHumanConfig(session);
            if (!config.isLinkUp()) {
                return;
            }
            SHumanExecutor.execute(
                    new SHumanIntegrationWorker(
                            session,
                            employeeId,
                            action
                    )
            );
//            new SHumanIntegrationWorker(
//                    session,
//                    employeeId,
//                    action
//            ).run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
