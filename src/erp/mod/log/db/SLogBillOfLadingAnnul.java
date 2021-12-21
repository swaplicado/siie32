/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.client.SClientInterface;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Juan Barajas
 */
public class SLogBillOfLadingAnnul {

    protected SClientInterface miClient;
    protected SDataCfd moCfd;
    protected ArrayList<SDataPayrollReceiptIssue> maReceiptIssues;
    protected int mnCfdiVersion;
    protected Date mtDateAnnul;
    protected boolean mbAnnulSat;
    protected int mnTpDpsAnn;
    
    public SLogBillOfLadingAnnul(SClientInterface client, SDataCfd cfd, Date dateAnnul, boolean annulSat, int tpDpsAnn, int cfdType) {
        miClient = client;
        moCfd = cfd;
        mtDateAnnul = dateAnnul;
        mbAnnulSat = annulSat;
        mnTpDpsAnn = tpDpsAnn;
        mnCfdiVersion = cfdType;
    }

    
    public boolean annulBillOfLading() throws Exception {
        boolean cancel = false;
        
        if (mbAnnulSat) {
            if (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                cancel = SCfdUtils.cancelAndSendCfdi(miClient, moCfd, mnCfdiVersion, mtDateAnnul, mbAnnulSat, true, mnTpDpsAnn);
            }
            else {
                cancel = SCfdUtils.cancelCfdi(miClient, moCfd, mnCfdiVersion, mtDateAnnul, mbAnnulSat, true, mnTpDpsAnn);
            }
        }
        else {
            SDbBillOfLading bol = new SDbBillOfLading();
            bol.read(miClient.getSession(), new int[] { moCfd.getFkBillOfLadingId_n() });
            bol.disable(miClient.getSession().getDatabase().getConnection());
            cancel = true;
        }
        
        return cancel;
    }
    
}
