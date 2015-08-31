/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataPriceList extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPriceListId;
    protected java.lang.String msPriceList;
    protected int mnPriceListNumber;
    protected double mdPercentage;
    protected double mdQuantityMinimum;
    protected boolean mbIsDeleted;
    protected int mnFkPriceListTypeId;
    protected int mnFkPriceUpdateTypeId;
    protected int mnFkPriceListGroupId_n;
    protected int mnFkDpsCategoryId;
    protected int mnFkCurrencyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mmkt.data.SDataPriceListLink> mvDbmsPriceListLink;
    protected java.util.Vector<erp.mmkt.data.SDataPriceListItemPrice> mvDbmsPriceListItemPrice;

    protected int mnDbmsAuxSortingItem;

    public SDataPriceList() {
        super(SDataConstants.MKT_PLIST);

        mvDbmsPriceListLink = new Vector<SDataPriceListLink>();
        mvDbmsPriceListItemPrice = new Vector<SDataPriceListItemPrice>();
        reset();
    }

    public void setPkPriceListId(int n) { mnPkPriceListId = n; }
    public void setPriceList(java.lang.String s) { msPriceList = s; }
    public void setPriceListNumber(int n) { mnPriceListNumber = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setQuantityMinimum(double d) { mdQuantityMinimum = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkPriceListTypeId(int n) { mnFkPriceListTypeId = n; }
    public void setFkPriceUpdateTypeId(int n) { mnFkPriceUpdateTypeId = n; }
    public void setFkPriceListGroupId_n(int n) { mnFkPriceListGroupId_n = n; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkPriceListId() { return mnPkPriceListId; }
    public java.lang.String getPriceList() { return msPriceList; }
    public int getPriceListNumber() { return mnPriceListNumber; }
    public double getPercentage() { return mdPercentage; }
    public double getQuantityMinimum() { return mdQuantityMinimum; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkPriceListTypeId() { return mnFkPriceListTypeId; }
    public int getFkPriceUpdateTypeId() { return mnFkPriceUpdateTypeId; }
    public int getFkPriceListGroupId_n() { return mnFkPriceListGroupId_n; }
    public int getFkCategoryDpsId() { return mnFkDpsCategoryId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }

    public int getAuxSortingItem() { return mnDbmsAuxSortingItem; }

    public java.util.Vector<SDataPriceListLink> getDbmsPListLink() { return mvDbmsPriceListLink; }
    public java.util.Vector<SDataPriceListItemPrice> getDbmsPListItemPrice() { return mvDbmsPriceListItemPrice; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPriceListId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPriceListId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPriceListId = 0;
        msPriceList = "";
        mnPriceListNumber = 0;
        mdPercentage = 0;
        mdQuantityMinimum = 0;
        mbIsDeleted = false;
        mnFkPriceListTypeId = 1;
        mnFkPriceUpdateTypeId = 1;
        mnFkPriceListGroupId_n = 0;
        mnFkDpsCategoryId = 0;
        mnFkCurrencyId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        //mnDbmsAuxSortingItem = SDataConstants.UNDEFINED;

        mvDbmsPriceListLink.clear();
        mvDbmsPriceListItemPrice.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        boolean b_add = false;
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;
        java.sql.Statement statementAux = null;
        SDataPriceListLink pListLink = null;
        SDataPriceListItemPrice pListItemPrice = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM mkt_plist WHERE id_plist = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPriceListId = resultSet.getInt("id_plist");
                msPriceList = resultSet.getString("plist");
                mnPriceListNumber = resultSet.getInt("plist_num");
                mdPercentage = resultSet.getDouble("per");
                mdQuantityMinimum = resultSet.getDouble("qty_min");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkPriceListTypeId = resultSet.getInt("fid_tp_plist");
                mnFkPriceUpdateTypeId = resultSet.getInt("fid_tp_price_upd");
                mnFkPriceListGroupId_n = resultSet.getInt("fid_plist_grp_n");
                mnFkDpsCategoryId = resultSet.getInt("fid_ct_dps");
                mnFkCurrencyId = resultSet.getInt("fid_cur");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read price list item:

                statementAux = statement.getConnection().createStatement();
                mvDbmsPriceListLink.removeAllElements();
                sql = "SELECT * " +
                        "FROM mkt_plist_item " +
                        "WHERE id_plist = " + key[0] + " " +
                        "ORDER BY id_tp_link ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    pListLink = new SDataPriceListLink();
                    pListLink.setDbmsAuxSortingItem(mnDbmsAuxSortingItem);
                    if (pListLink.read(new int[] { resultSet.getInt("id_plist"), resultSet.getInt("id_tp_link"), resultSet.getInt("id_ref") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsPriceListLink.add(pListLink);
                    }
                }

                // Read item price by link:

                statementAux = statement.getConnection().createStatement();
                mvDbmsPriceListItemPrice.removeAllElements();
                for (int i=0; i<mvDbmsPriceListLink.size(); i++) {

                    pListLink = mvDbmsPriceListLink.get(i);
                    switch(pListLink.getPkLinkTypeId()) {

                        case SDataConstantsSys.TRNS_TP_LINK_ITEM:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM mkt_plist_price AS pr " +
                            "INNER JOIN erp.itmu_item AS i ON pr.id_item = i.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND i.id_item = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_MFR:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itmu_mfr AS mfr " +
                            "INNER JOIN erp.itmu_item AS i ON mfr.id_mfr = i.fid_mfr " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND i.fid_mfr = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_BRD:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itmu_brd AS brd " +
                            "INNER JOIN erp.itmu_item AS i ON brd.id_brd = i.fid_brd " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND i.fid_brd = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_LINE:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itmu_line AS line " +
                            "INNER JOIN erp.itmu_item AS i ON line.id_line = i.fid_line_n " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND i.fid_line_n = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_IGEN:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itmu_igen AS gen " +
                            "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND i.fid_igen = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_IGRP:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itmu_igrp AS grp " +
                            "INNER JOIN erp.itmu_igen AS gen ON grp.id_igrp = gen.fid_igrp " +
                            "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND gen.fid_igrp = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_IFAM:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itmu_ifam AS fam " +
                            "INNER JOIN erp.itmu_igrp AS grp ON fam.id_ifam = grp.fid_ifam " +
                            "INNER JOIN erp.itmu_igen AS gen ON gen.fid_igrp = grp.id_igrp " +
                            "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND grp.fid_ifam = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itms_tp_item AS tp " +
                            "INNER JOIN erp.itmu_igen AS gen ON gen.fid_tp_item = tp.id_tp_item " +
                            "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND gen.fid_tp_item = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itms_cl_item AS cl " +
                            "INNER JOIN erp.itmu_igen AS gen ON gen.fid_cl_item = cl.id_cl_item " +
                            "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND gen.fid_cl_item = " + pListLink.getPkReferenceId() + " ";
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:

                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM erp.itms_ct_item AS ct " +
                            "INNER JOIN erp.itmu_igen AS gen ON gen.fid_ct_item = ct.id_ct_item " +
                            "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen " +
                            "INNER JOIN mkt_plist_price AS pr ON i.id_item = pr.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " AND i.b_del = FALSE AND gen.fid_ct_item = " + pListLink.getPkReferenceId() + " " ;
                            break;
                        case SDataConstantsSys.TRNS_TP_LINK_ALL:
                            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                            "FROM mkt_plist_price AS pr " +
                            "INNER JOIN erp.itmu_item AS i ON pr.id_item = i.id_item " +
                            "WHERE pr.id_plist = " + key[0] + " ";
                            break;
                    }

                    resultSet = statement.executeQuery(sql + "ORDER BY " + (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item " : "i.item, i.item_key "));
                    while (resultSet.next()) {
                        pListItemPrice = new SDataPriceListItemPrice();
                        if (pListItemPrice.read(new int[] { resultSet.getInt("pr.id_plist"), resultSet.getInt("pr.id_item") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            // Validate if not exist item:

                            b_add = true;
                            for (SDataPriceListItemPrice itemPrice : mvDbmsPriceListItemPrice) {
                                if (itemPrice.getPkItemId() == pListItemPrice.getPkItemId() &&
                                    itemPrice.getPkPriceListId() == pListItemPrice.getPkPriceListId()) {

                                    b_add = false;
                                    break;
                                }
                            }

                            if (b_add) {
                                mvDbmsPriceListItemPrice.add(pListItemPrice);
                            }
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        int i = 0;
        CallableStatement callableStatement = null;
        SDataPriceListLink pListLink = null;
        SDataPriceListItemPrice pListItemPrice = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mkt_plist_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPriceListId);
            callableStatement.setString(nParam++, msPriceList);
            callableStatement.setInt(nParam++, mnPriceListNumber);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setDouble(nParam++, mdQuantityMinimum);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkPriceListTypeId);
            callableStatement.setInt(nParam++, mnFkPriceUpdateTypeId);
            if (mnFkPriceListGroupId_n > 0) callableStatement.setInt(nParam++, mnFkPriceListGroupId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkDpsCategoryId);
            callableStatement.setInt(nParam++, mnFkCurrencyId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkPriceListId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                // Delete link:

                pListLink = new SDataPriceListLink();
                pListLink.setPkPriceListId(mnPkPriceListId);
                pListLink.del(connection);

                // Save link:

                for (i = 0; i < mvDbmsPriceListLink.size(); i++) {
                    pListLink = (SDataPriceListLink) mvDbmsPriceListLink.get(i);
                    if (pListLink != null) {
                        pListLink.setPkPriceListId(mnPkPriceListId);
                        if (pListLink.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
                // Delete item price:

                pListItemPrice = new SDataPriceListItemPrice();
                pListItemPrice.setPkPriceListId(mnPkPriceListId);
                pListItemPrice.del(connection);

                // Save item price:

                for (i = 0; i < mvDbmsPriceListItemPrice.size(); i++) {
                    pListItemPrice = (SDataPriceListItemPrice) mvDbmsPriceListItemPrice.get(i);
                    if (pListItemPrice != null) {
                        pListItemPrice.setPkPriceListId(mnPkPriceListId);
                        if (pListItemPrice.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
