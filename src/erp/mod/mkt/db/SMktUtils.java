/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.mkt.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Néstor Ávalos
 */
public abstract class SMktUtils {

    /**
     * Print report: Commissions payment
     * @param client Interface Client
     * @param Key comms. payment id
     * @throws java.lang.Exception
     */
    public static void printReportCommissionsPayment(final SGuiClient client, final int[] Key) throws java.lang.Exception {
        HashMap<String, Object> oMap = null;

        oMap = client.createReportParams();

        // XXX oMap.put("sCompanyBranch", moFieldCompanyBranch.getKeyAsIntArray()[0] == 0 ? "(TODAS)" : jcbCompanyBranch.getSelectedItem().toString());
        // XXX oMap.put("sBizPartner", moFieldBizPartner.getKeyAsIntArray()[0] == 0 ? "(TODOS)" : jcbBizPartner.getSelectedItem().toString());
        oMap.put("nPkComPayId", Key[0]);

        // XXX oMap.put("nPkCobId", moFieldCompanyBranch.getKeyAsIntArray()[0]);
        oMap.put("nFkCtSysMovDpsCus", SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]);
        oMap.put("nFkTpSysMovDpsCus", SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]);
        oMap.put("nFkCtDpsId", SDataConstantsSys.TRNS_CT_DPS_SAL);
        oMap.put("nFkClDpsDocId", SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
        oMap.put("nFkClDpsAdjId", SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
        oMap.put("nFkStDpsId", SDataConstantsSys.TRNS_ST_DPS_EMITED);
        oMap.put("nFkValDpsId", SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
        oMap.put("nFkCtBpId", SDataConstantsSys.BPSS_CT_BP_CUS);
        // XXX oMap.put("sSqlCob", jcbCompanyBranch.getSelectedIndex() > 0 ? "AND d.fid_cob = " + moFieldCompanyBranch.getKeyAsIntArray()[0] : "");
        // XXX oMap.put("sSqlSalAgt", jcbBizPartner.getSelectedIndex() > 0 ? "AND d.fid_sal_agt_n = " + moFieldBizPartner.getKeyAsIntArray()[0] : "");

        client.getSession().printReport(SModConsts.MKTR_COMMS_PAY, SLibConsts.UNDEFINED, null, oMap);
    }
}
