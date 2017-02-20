/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.mod.SModConsts;
import erp.mtrn.data.SCfdPacket;
import erp.mtrn.data.SDataCfd;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas
 */
public class SDbFormerPayrollImport extends SDataRegistry implements Serializable {

    protected int mnPayrollId;
    protected boolean mbRegenerateOnlyNonStampedCfdi;
    protected ArrayList<SCfdPacket> moCfdPackets;

    public SDbFormerPayrollImport() {
        super(SModConsts.HRS_SIE_PAY);
        reset();
    }

    public void setPayrollId(int n) { mnPayrollId = n; }
    public void setRegenerateOnlyNonStampedCfdi(boolean b) { mbRegenerateOnlyNonStampedCfdi = b; }
    public void setCfdPackets(ArrayList<SCfdPacket> o) { moCfdPackets = o; }

    public int getPayrollId() { return mnPayrollId; }
    public boolean isRegenerateOnlyNonStampedCfdi() { return mbRegenerateOnlyNonStampedCfdi; }
    public ArrayList<SCfdPacket> getCfdPackets() { return moCfdPackets; }

    @Override
    public int[] getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPrimaryKey(Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPayrollId = 0;
        mbRegenerateOnlyNonStampedCfdi = false;
        moCfdPackets = new ArrayList<SCfdPacket>();
    }

    @Override
    public int read(Object pk, Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        String sSql = "";
        Statement stUpd = null;
        SDataCfd cfd = null;
        String xmlFile = "";
        String xmlFileName = "";
        Date dateCfd = null;
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            stUpd = connection.createStatement();

            sSql = "UPDATE trn_cfd SET b_con = 0 " +
                    "WHERE fid_pay_pay_n = " + mnPayrollId + " AND fid_st_xml <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " ";
            stUpd.execute(sSql);

            for (SCfdPacket packet: moCfdPackets) {
                cfd = new SDataCfd();
                if (packet.getCfdId() != SLibConstants.UNDEFINED && packet.getIsConsistent()) {
                    cfd.saveField(connection, new int[] { packet.getCfdId() }, SDataCfd.FIELD_B_CON, packet.getIsConsistent());
                }
                else {
                    xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
                    xmlFile += ((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getElementForXml();

                    xmlFileName = "";
                    xmlFileName += ((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getEltEmisor().getAttRfc().getString() + "_";
                    xmlFileName += ((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getAttTipoDeComprobante().getOption().substring(0, 1).toUpperCase() + "_";
                    xmlFileName += (((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getAttSerie().getString().length() == 0 ? "" : ((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getAttSerie().getString() + "_");
                    xmlFileName += decimalFormat.format(SLibUtils.parseLong(((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getAttFolio().getString())) + ".xml";
                    dateCfd = ((cfd.ver32.DElementComprobante) packet.getCfdRootElement()).getAttFecha().getDatetime();

                    cfd.setPkCfdId(packet.getCfdId());
                    cfd.setTimestamp(dateCfd);
                    cfd.setCertNumber(packet.getCertNumber());
                    cfd.setStringSigned(packet.getStringSigned());
                    cfd.setSignature(packet.getSignature());
                    cfd.setDocXml(xmlFile);
                    cfd.setDocXmlName(xmlFileName);
                    cfd.setAcknowledgmentCancellationXml(packet.getAcknowledgmentCancellation());
                    cfd.setUuid(packet.getUuid());
                    cfd.setIsConsistent(true);
                    cfd.setFkCfdTypeId(packet.getFkCfdTypeId());
                    cfd.setFkXmlTypeId(packet.getFkXmlTypeId());
                    cfd.setFkXmlStatusId(packet.getFkXmlStatusId());
                    cfd.setFkXmlDeliveryTypeId(packet.getFkXmlDeliveryTypeId());
                    cfd.setFkXmlDeliveryStatusId(packet.getFkXmlDeliveryStatusId());
                    cfd.setFkUserProcessingId(packet.getFkUserDeliveryId());
                    cfd.setFkUserDeliveryId(packet.getFkUserDeliveryId());
                    cfd.setFkDpsYearId_n(packet.getDpsYearId());
                    cfd.setFkDpsDocId_n(packet.getDpsDocId());
                    cfd.setFkPayrollPayrollId_n(packet.getPayrollPayrollId());
                    cfd.setFkPayrollEmployeeId_n(packet.getPayrollEmployeeId());
                    cfd.setFkPayrollBizPartnerId_n(packet.getPayrollBizPartnerId());
                    cfd.setFkPayrollReceiptPayrollId_n(packet.getPayrollReceiptPayrollId());
                    cfd.setFkPayrollReceiptEmployeeId_n(packet.getPayrollReceiptEmployeeId());
                    cfd.setFkPayrollReceiptIssueId_n(packet.getPayrollReceiptIssueId());

                    cfd.setAuxRfcEmisor(packet.getRfcEmisor());
                    cfd.setAuxRfcReceptor(packet.getRfcReceptor());
                    cfd.setAuxTotalCy(packet.getTotalCy());

                    if (cfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
                
            }
            if (mbRegenerateOnlyNonStampedCfdi) {
                sSql = "UPDATE trn_cfd AS c " +
                        "INNER JOIN hrs_sie_pay_emp AS pe ON c.fid_pay_pay_n = pe.id_pay AND c.fid_pay_emp_n = pe.id_emp AND c.fid_pay_bpr_n = pe.fid_bpr_n AND pe.b_del = 0 SET c.b_con = 1 " +
                        "WHERE c.fid_pay_pay_n = " + mnPayrollId + " AND c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " ";
                stUpd.execute(sSql);
            }

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
