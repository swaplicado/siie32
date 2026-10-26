/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

import sa.lib.gui.SGuiSession;

/**
 *
 * @author César Orozco
 */
public class SHumanIntegrationWorker implements Runnable {

    private final SGuiSession session;
    private final int employeeId;
    private final SHumanAction action;

    public SHumanIntegrationWorker(
            final SGuiSession session,
            final int employeeId,
            final SHumanAction action
    ) {
        this.session = session;
        this.employeeId = employeeId;
        this.action = action;
    }

    @Override
    public void run() {

        try {
            SHumanConfig config = new SHumanConfig(session);
            SHumanService service = new SHumanService();
            service.syncEmployeeToHuman(config, session.getStatement(), employeeId, action);
        }
        catch (Exception e) {
            SHumanLogger.logError(e);
            e.printStackTrace();
        }
    }
}
