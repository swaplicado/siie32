/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Vector;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public abstract class SDataReadRegistries {

    private static java.lang.Object[] createQuery(int registryType, java.lang.Object filterKey) throws java.lang.Exception {
        String sql = "";
        Object[] packet = null;

        /* Index:
         * 0: number of primary key fields
         * 1: SQL query
         * 2: data registry class
         * 3: is primary key made up only by integer fields?
         */

        switch (registryType) {
            case SDataConstants.CFGU_CERT:
                sql = "SELECT id_cert AS id_1 FROM cfgu_cert WHERE b_del = 0 ORDER BY dt, num, id_cert ";
                packet = new Object[] { 1, sql, erp.mcfg.data.SDataCertificate.class, true };
                break;

            case SDataConstants.CFGU_CO:
                sql = "SELECT id_co AS id_1 FROM erp.cfgu_co WHERE b_del = 0 ORDER BY co, id_co ";
                packet = new Object[] { 1, sql, erp.mcfg.data.SDataCompany.class, true };
                break;

            case SDataConstants.CFGU_COB_ENT:
                sql = "SELECT id_cob AS id_1, id_ent AS id_2 FROM erp.cfgu_cob_ent " +
                        "WHERE b_del = 0 AND id_cob = " + ((int[]) filterKey)[0] + " " +
                        "ORDER BY fid_ct_ent, fid_tp_ent, ent, id_ent ";
                packet = new Object[] { 2, sql, erp.mcfg.data.SDataCompanyBranchEntity.class, true };
                break;

            case SDataConstants.USRS_ROL:
                sql = "SELECT id_rol AS id_1 FROM erp.usrs_rol WHERE b_del = 0 ORDER BY id_rol ";
                packet = new Object[] { 1, sql, erp.musr.data.SDataRole.class, true };
                break;

            case SDataConstants.BPSU_BPB:
                sql = "SELECT id_bpb AS id_1 FROM erp.bpsu_bpb " +
                        "WHERE b_del = 0 AND fid_bp = " + ((int[]) filterKey)[0] + " " +
                        "ORDER BY bpb, fid_tp_bpb, bpb, id_bpb ";
                packet = new Object[] { 1, sql, erp.mbps.data.SDataBizPartnerBranch.class, true };
                break;

            case SDataConstants.BPSX_BANK_ACC_CHECK:
                sql = "SELECT bk.id_bpb AS id_1, bk.id_bank_acc AS id_2 " +
                        "FROM fin_rec_ety AS re " +
                        "INNER JOIN fin_check AS c ON " +
                        "re.fid_check_wal_n = c.id_check_wal AND re.fid_check_n = c.id_check " +
                        "INNER JOIN erp.bpsu_bank_acc AS bk ON " +
                        "c.fid_bpb_n = bk.id_bpb AND c.fid_bank_acc_n = bk.id_bank_acc " +
                        "WHERE re.fid_check_wal_n = " + ((int[]) filterKey)[0] + " AND re.fid_check_n = " + ((int[]) filterKey)[1]  + " ";
                packet = new Object[] { 2, sql, erp.mbps.data.SDataBizPartnerBranchBankAccount.class, true };
                break;

            case SDataConstants.ITMU_UNIT:
                sql = "SELECT id_unit AS id_1 FROM erp.itmu_unit WHERE b_del = 0 ";
                packet = new Object[] { 1, sql, erp.mitm.data.SDataUnit.class, true };
                break;

            case SDataConstants.FINU_TAX:
                sql = "SELECT id_tax_bas AS id_1, id_tax AS id_2 FROM erp.finu_tax WHERE b_del = 0 ";
                packet = new Object[] { 2, sql, erp.mfin.data.SDataTax.class, true };
                break;

            case SDataConstants.FIN_ACC_CASH:
                sql = "SELECT id_cob AS id_1, id_acc_cash AS id_2 FROM fin_acc_cash WHERE b_del = 0 ";
                packet = new Object[] { 2, sql, erp.mfin.data.SDataAccountCash.class, true };
                break;

            case SDataConstants.FIN_TAX_GRP:
                sql = "SELECT id_tax_grp AS id_1 FROM fin_tax_grp WHERE b_del = 0 ";
                packet = new Object[] { 1, sql, erp.mfin.data.SDataTaxGroup.class, true };
                break;

            case SDataConstants.FIN_TAX_GRP_ITEM:
                sql = "SELECT id_item AS id_1, id_tax_reg AS id_2, id_dt_start AS id_3 FROM fin_tax_grp_item WHERE b_del = 0 ";
                packet = new Object[] { 3, sql, erp.mfin.data.SDataTaxGroupItem.class, false };
                break;

            case SDataConstants.FIN_TAX_GRP_IGEN:
                sql = "SELECT id_igen AS id_1, id_tax_reg AS id_2, id_dt_start AS id_3 FROM fin_tax_grp_igen WHERE b_del = 0 ";
                packet = new Object[] { 3, sql, erp.mfin.data.SDataTaxGroupItemGeneric.class, false };
                break;

            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return packet;
    }

    /**
     * Pretended to be call by ERP Server.
     * @param statement
     * @param registryType
     * @param filterKey
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.lib.data.SDataRegistry> readRegistries(java.sql.Statement statement, int registryType, java.lang.Object filterKey) {
        int i = 0;
        Object pk = null;
        ResultSet resultSet = null;
        Statement statementAux = null;
        Object[] packet = null;
        Vector<SDataRegistry> registries = new Vector<>();

        try {
            statementAux = statement.getConnection().createStatement();
            packet = createQuery(registryType, filterKey);

            resultSet = statement.executeQuery((String) packet[1]);
            while (resultSet.next()) {
                SDataRegistry registry = (SDataRegistry) ((Class) packet[2]).getConstructor(new Class[] {}).newInstance(new Object[] {});

                if ((Boolean) packet[3]) {
                    // Primary key build up only by integers:

                    pk = new int[(Integer) packet[0]];
                    for (i = 0; i < ((int[]) pk).length; i++) {
                        ((int[]) pk)[i] = resultSet.getInt("id_" + (i + 1));
                    }
                }
                else {
                    // Primary key build up by diferent types of data:

                    pk = new Object[(Integer) packet[0]];
                    for (i = 0; i < ((Object[]) pk).length; i++) {
                        ((Object[]) pk)[i] = resultSet.getObject("id_" + (i + 1));
                    }
                }

                if (registry.read(pk, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                }
                else {
                    registries.add(registry);
                }
            }
        }
        catch (SQLException e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }
        catch (NoSuchMethodException e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }
        catch (InstantiationException e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }
        catch (IllegalAccessException e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }
        catch (InvocationTargetException e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }
        catch (ParseException e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }
        catch (Exception e) {
            SLibUtilities.printOutException(SDataReadComponentItems.class.getName(), e);
        }

        return registries;
    }

    /**
     * Pretended to be call by ERP Client.
     * @param client
     * @param registryType
     * @param filterKey
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.lib.data.SDataRegistry> readRegistries(erp.client.SClientInterface client, int registryType, java.lang.Object filterKey) {
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SDataRegistry> registries = new Vector<>();

        try {
            request = new SServerRequest(SServerConstants.REQ_REGS);
            request.setRegistryType(registryType);
            request.setPrimaryKey(filterKey);

            response = client.getSessionXXX().request(request);
            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                registries = (Vector<SDataRegistry>) response.getPacket();
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(SDataReadRegistries.class.getName(), e);
        }

        return registries;
    }
}
