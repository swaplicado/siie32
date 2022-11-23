/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolLocation extends SDbRegistryUser implements SGridRow, Serializable {

    protected final SDbBillOfLading moBillOfLading;
    
    protected int mnPkBillOfLadingId;
    protected int mnPkLocationId;
    protected double mdDistance;
    protected Date mtDateDeparture_n;
    protected Date mtDateArrival_n;
    protected int mnLocationType;
    protected int mnFkOriginBizPartner_n;
    protected int mnFkOriginBizPartnerAddress_n;
    protected int mnFkOriginAddressAddress_n;
    protected int mnFkOriginNeighborhoodZipCode_n;
    protected int mnFkDestinationBizPartner_n;
    protected int mnFkDestinationBizPartnerAddress_n;
    protected int mnFkDestinationAddressAddress_n;
    protected int mnFkDestinationNeighborhoodZipCode_n;
    
    protected SDbBizPartnerBranchAddressNeighborhood moCompBizPartnerBranchNeighborhood;
    
    protected SDataBizPartner moDataBizParter;
    protected SDataBizPartnerBranch moDataBizParterBranch;
    protected SDataBizPartnerBranchAddress moDataBizPartnerBranchAddress;
    
    protected String msXtaLocationType;
    protected boolean mbXtaIsOrigin;
    protected boolean mbXtaIsDestination;
    
    protected ArrayList<SDbBolMerchandiseQuantity> maBolMerchandiseQuantityCharges;
    protected ArrayList<SDbBolMerchandiseQuantity> maBolMerchandiseQuantityDischarges;
    
    protected ArrayList<SRowBillOfLading> maRowBolPrecharges;
    protected ArrayList<SRowBillOfLading> maRowBolCurrentCharges;
    
    public SDbBolLocation(SDbBillOfLading bol) {
        super(SModConsts.LOG_BOL_LOCATION);
        moBillOfLading = bol;
    }
    
    public void inicializeRowsCurrentCharge(ArrayList<SRowBillOfLading> rows) {
        maRowBolCurrentCharges.clear();
        for (SRowBillOfLading row : rows) {
            SRowBillOfLading bol = new SRowBillOfLading();
            bol.setItemId(row.getItemId());
            bol.setItem(row.getItem());
            bol.setQuantity(row.getQuantity());
            bol.setUnit(row.getUnit());
            maRowBolCurrentCharges.add(bol);
        }
    }
    
    public void updateRowsPrecharged(SDbBolLocation l) {
        for (SDbBolMerchandiseQuantity qty : l.maBolMerchandiseQuantityCharges) {
            boolean added= false;
            for (SRowBillOfLading rowPrecharged : maRowBolPrecharges) {
                if (rowPrecharged.getItemId() == qty.getParentMerchandise().getFkItemId()) {
                    rowPrecharged.updateRowCharge(qty);
                    added = true;
                }
            }
            if (!added) {
                SRowBillOfLading row = new SRowBillOfLading();
                row.setItemId(qty.getParentMerchandise().getFkItemId());
                row.setItem(qty.getXtaItemName());
                row.setQuantity(qty.getQuantity());
                row.setUnit(qty.getXtaUnitName());
                maRowBolPrecharges.add(row);
            }
        }
        
        for (SDbBolMerchandiseQuantity qty : l.maBolMerchandiseQuantityDischarges) {
            boolean added = false;
            for (SRowBillOfLading rowPrecharged : maRowBolPrecharges) {
                if (rowPrecharged.getItemId() == qty.getParentMerchandise().getFkItemId()) {
                    rowPrecharged.updateRowDischarge(qty);
                    added = true;
                }
            }
            if (!added) {
                SRowBillOfLading row = new SRowBillOfLading();
                row.setItemId(qty.getParentMerchandise().getFkItemId());
                row.setItem(qty.getXtaItemName());
                row.setQuantity(qty.getQuantity());
                row.setUnit(qty.getXtaUnitName());
                maRowBolPrecharges.add(row);
            }
        }
    }
    
    public void updateRowsCurrentCharge(SDbBolMerchandiseQuantity qty) {
        boolean added = false;
        
        for (SRowBillOfLading row : maRowBolCurrentCharges) {
            if (row.getItemId() == qty.getParentMerchandise().getFkItemId()) {
                if (qty.getFkOriginAddressAddress_n() != 0 && qty.getFkOriginBizPartnerAddress_n() != 0) {
                    row.setQuantity(row.getQuantity() + qty.getQuantity());
                    added = true;
                } 
                else {
                    row.setQuantity(row.getQuantity() - qty.getQuantity());
                    added = true;
                }
            }
        }
        
        if (!added) {
            SRowBillOfLading row = new SRowBillOfLading();
            row.setItemId(qty.getParentMerchandise().getFkItemId());
            row.setItem(qty.getXtaItemName());
            row.setQuantity(qty.getQuantity());
            row.setUnit(qty.getXtaUnitName());
            maRowBolCurrentCharges.add(row);
        }
    }

    public void setPkBillOfLadingId(int n) { mnPkBillOfLadingId = n; }
    public void setPkLocationId(int n) { mnPkLocationId = n; }
    public void setDistance(double d) { mdDistance = d; }
    public void setDateDeparture_n(Date t) { mtDateDeparture_n = t; }
    public void setDateArrival_n(Date t) { mtDateArrival_n = t; }
    public void setLocationType(int n) { mnLocationType = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkOriginBizPartner_n(int n) { mnFkOriginBizPartner_n = n; }
    public void setFkOriginBizPartnerAddress_n(int n) { mnFkOriginBizPartnerAddress_n = n; }
    public void setFkOriginAddressAddress_n(int n) { mnFkOriginAddressAddress_n = n; }
    public void setFkOriginNeighborhoodZipCode_n(int n) { mnFkOriginNeighborhoodZipCode_n = n; }
    public void setFkDestinationBizPartner_n(int n) { mnFkDestinationBizPartner_n = n; }
    public void setFkDestinationBizPartnerAddress_n(int n) { mnFkDestinationBizPartnerAddress_n = n; }
    public void setFkDestinationAddressAddress_n(int n) { mnFkDestinationAddressAddress_n = n; }
    public void setFkDestinationNeighborhoodZipCode_n(int n) { mnFkDestinationNeighborhoodZipCode_n = n; }

    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
    public int getPkLocationId() { return mnPkLocationId; }
    public double getDistance() { return mdDistance; }
    public Date getDateDeparture_n() { return mtDateDeparture_n; }
    public Date getDateArrival_n() { return mtDateArrival_n; }
    public int getLocationType() { return mnLocationType; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkOriginBizPartner_n() { return mnFkOriginBizPartner_n; }
    public int getFkOriginBizPartnerAddress_n() { return mnFkOriginBizPartnerAddress_n; }
    public int getFkOriginAddressAddress_n() { return mnFkOriginAddressAddress_n; }
    public int getFkOriginNeighborhoodZipCode_n() { return mnFkOriginNeighborhoodZipCode_n; }
    public int getFkDestinationBizPartner_n() { return mnFkDestinationBizPartner_n; }
    public int getFkDestinationBizPartnerAddress_n() { return mnFkDestinationBizPartnerAddress_n; }
    public int getFkDestinationAddressAddress_n() { return mnFkDestinationAddressAddress_n; }
    public int getFkDestinationNeighborhoodZipCode_n() { return mnFkDestinationNeighborhoodZipCode_n; }
    
    public void setDbmsBizPartnerBranchNeighborhood(SDbBizPartnerBranchAddressNeighborhood o) { moCompBizPartnerBranchNeighborhood = o; }
    
    public SDbBizPartnerBranchAddressNeighborhood getDbmsBizPartnerBranchNeighborhood() { return moCompBizPartnerBranchNeighborhood; }
    
    public void setDataBizPartner(SDataBizPartner o) { moDataBizParter = o; } 
    public void setDataBizPartnerBranch(SDataBizPartnerBranch o) { moDataBizParterBranch = o; } 
    public void setDataBizPartnerBranchAddress(SDataBizPartnerBranchAddress o) { moDataBizPartnerBranchAddress = o; } 
    
    public SDataBizPartner getDataBizPartner() { return moDataBizParter; }
    public SDataBizPartnerBranch getDataBizPartnerBranch() { return moDataBizParterBranch; }
    public SDataBizPartnerBranchAddress getDataBizPartnerBranchAddress() { return moDataBizPartnerBranchAddress; }
    
    public void setXtaLocationType(String s) { msXtaLocationType = s; };
    public void setXtaIsOrigin(boolean b) { mbXtaIsOrigin = b; };
    public void setXtaIsDestination(boolean b) { mbXtaIsDestination = b; };
    
    public String getXtaLocationType() { return msXtaLocationType; }
    public boolean getXtaIsOrigin() { return mbXtaIsOrigin; }
    public boolean getXtaIsDestination() { return mbXtaIsDestination; }
    
    public ArrayList<SDbBolMerchandiseQuantity> getBolMerchandiseQuantityCharges() { return maBolMerchandiseQuantityCharges; }
    public ArrayList<SDbBolMerchandiseQuantity> getBolMerchandiseQuantityDischarges() { return maBolMerchandiseQuantityDischarges; }
    
    public ArrayList<SRowBillOfLading> getRowBolPrecharges() { return maRowBolPrecharges; }
    public ArrayList<SRowBillOfLading> getRowBolCurrentCharges() { return maRowBolCurrentCharges; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBillOfLadingId = pk[0];
        mnPkLocationId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBillOfLadingId, mnPkLocationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBillOfLadingId = 0;
        mnPkLocationId = 0;
        mdDistance = 0;
        mtDateDeparture_n = null;
        mtDateArrival_n = null;
        mnLocationType = 0;
        mbDeleted = false;
        mnFkOriginBizPartner_n = 0;
        mnFkOriginBizPartnerAddress_n = 0;
        mnFkOriginAddressAddress_n = 0;
        mnFkOriginNeighborhoodZipCode_n = 0;
        mnFkDestinationBizPartner_n = 0;
        mnFkDestinationBizPartnerAddress_n = 0;
        mnFkDestinationAddressAddress_n = 0;
        mnFkDestinationNeighborhoodZipCode_n = 0;
        
        moCompBizPartnerBranchNeighborhood = null;
        
        moDataBizParter = null;
        moDataBizParterBranch = null;
        moDataBizPartnerBranchAddress = null;
        
        msXtaLocationType = "";
        mbXtaIsOrigin = false;
        mbXtaIsDestination = false;
        
        maBolMerchandiseQuantityCharges = new ArrayList<>();
        maBolMerchandiseQuantityDischarges = new ArrayList<>();
        
        maRowBolPrecharges = new ArrayList<>();
        maRowBolCurrentCharges = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_location = " + mnPkLocationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " AND id_location = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkLocationId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_location), 0) + 1 FROM " + getSqlTable() + " WHERE id_bol = " + mnPkBillOfLadingId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLocationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement = session.getDatabase().getConnection().createStatement();
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT "
                + " l.*, IF(location_tp = 1, 'Inicial', IF(location_tp = 2, 'Intermedio', IF(location_tp = 3, 'Final', 'No definido'))) AS _location_tp, "
                + "IF(fk_orig_bp_n IS NULL, 0, 1) AS is_origin, IF(fk_dest_bp_n IS NULL, 0, 1) AS is_destination "
                + "FROM " + getSqlTable() + " AS l "
                + "WHERE id_bol = " + pk[0] 
                + " AND id_location = " + pk[1];
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            mnPkLocationId = resultSet.getInt("id_location");
            mdDistance = resultSet.getDouble("distance");
            mtDateDeparture_n = resultSet.getTimestamp("dt_departure_n");
            mtDateArrival_n = resultSet.getTimestamp("dt_arrival_n");
            mnLocationType = resultSet.getInt("location_tp");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkOriginBizPartner_n = resultSet.getInt("fk_orig_bp_n");
            mnFkOriginBizPartnerAddress_n = resultSet.getInt("fk_orig_bpb_add_n");
            mnFkOriginAddressAddress_n = resultSet.getInt("fk_orig_add_add_n");
            mnFkOriginNeighborhoodZipCode_n = resultSet.getInt("fk_orig_nei_zip_code_n");
            mnFkDestinationBizPartner_n = resultSet.getInt("fk_dest_bp_n");
            mnFkDestinationBizPartnerAddress_n = resultSet.getInt("fk_dest_bpb_add_n");
            mnFkDestinationAddressAddress_n = resultSet.getInt("fk_dest_add_add_n");
            mnFkDestinationNeighborhoodZipCode_n = resultSet.getInt("fk_dest_nei_zip_code_n");
            
            msXtaLocationType = resultSet.getString("_location_tp");
            mbXtaIsOrigin = resultSet.getBoolean("is_origin");
            mbXtaIsDestination = resultSet.getBoolean("is_destination");
            
            // Read merchandise charge
            
            maBolMerchandiseQuantityCharges.clear();
            msSql = "SELECT id_merch, id_merch_qty " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH_QTY) + " " + 
                    "WHERE id_bol = " + mnPkBillOfLadingId + " " + 
                    "AND fk_orig_bpb_add_n " + (mnFkOriginBizPartnerAddress_n == 0 ? "IS NULL " : "= " + mnFkOriginBizPartnerAddress_n) + " " +
                    "AND fk_orig_add_add_n " + (mnFkOriginAddressAddress_n == 0 ? "IS NULL " : "= " + mnFkOriginAddressAddress_n) + " " +
                    "AND fk_dest_bpb_add_n IS NULL " +
                    "AND fk_dest_add_add_n IS NULL " +
                    "ORDER BY id_merch, id_merch_qty ";
            ResultSet resultSetMerchandise = statement.executeQuery(msSql);
            while (resultSetMerchandise.next()) {
                SDbBolMerchandiseQuantity merchQty = new SDbBolMerchandiseQuantity();
                merchQty.read(session, new int[] { mnPkBillOfLadingId, 
                    resultSetMerchandise.getInt("id_merch"), 
                    resultSetMerchandise.getInt("id_merch_qty") });
                maBolMerchandiseQuantityCharges.add(merchQty);
            }
            
            // Read merchandise discharge
            
            maBolMerchandiseQuantityDischarges.clear();
            msSql = "SELECT id_merch, id_merch_qty " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH_QTY) + " " + 
                    "WHERE id_bol = " + mnPkBillOfLadingId + " " + 
                    "AND fk_orig_bpb_add_n IS NULL " +
                    "AND fk_orig_add_add_n IS NULL " +
                    "AND fk_dest_bpb_add_n " + (mnFkDestinationBizPartnerAddress_n == 0 ? "IS NULL " : "= " + mnFkDestinationBizPartnerAddress_n) + " " +
                    "AND fk_dest_add_add_n " + (mnFkDestinationAddressAddress_n == 0 ? "IS NULL " : "= " + mnFkDestinationAddressAddress_n) + " " +
                    "ORDER BY id_merch, id_merch_qty";
            resultSetMerchandise = statement.executeQuery(msSql);
            while (resultSetMerchandise.next()) {
                SDbBolMerchandiseQuantity merchQty = new SDbBolMerchandiseQuantity();
                merchQty.read(session, new int[] { mnPkBillOfLadingId, 
                    resultSetMerchandise.getInt("id_merch"), 
                    resultSetMerchandise.getInt("id_merch_qty") });
                maBolMerchandiseQuantityDischarges.add(merchQty);
            }
            
            // Read BizPartner
            
            moDataBizParter = new SDataBizPartner();
            moDataBizParter.read(new int[] { mnFkOriginBizPartner_n == 0 ? mnFkDestinationBizPartner_n : mnFkOriginBizPartner_n }, statement);
            
            // Read BizPartnerBranch
            
            moDataBizParterBranch = new SDataBizPartnerBranch();
            moDataBizParterBranch.read(new int[] { mnFkOriginBizPartnerAddress_n == 0 ? mnFkDestinationBizPartnerAddress_n : mnFkOriginBizPartnerAddress_n } , statement);
            
            // Read BizPartnerBranchAddress
            
            moDataBizPartnerBranchAddress = new SDataBizPartnerBranchAddress();
            
            if (mnFkOriginBizPartnerAddress_n != 0) {
                moDataBizPartnerBranchAddress.read(new int[] { mnFkOriginBizPartnerAddress_n, mnFkOriginAddressAddress_n } , statement);
            }
            else {
                moDataBizPartnerBranchAddress.read(new int[] { mnFkDestinationBizPartnerAddress_n, mnFkDestinationAddressAddress_n } , statement);
            }
            
            // Read neighborhood zip code
            
            if (mnFkDestinationNeighborhoodZipCode_n != 0 || mnFkOriginNeighborhoodZipCode_n != 0) {
                moCompBizPartnerBranchNeighborhood = new SDbBizPartnerBranchAddressNeighborhood();
                if (mnFkOriginBizPartnerAddress_n != 0) {
                    moCompBizPartnerBranchNeighborhood.read(session, new int[] { mnFkOriginBizPartnerAddress_n, mnFkOriginAddressAddress_n });
                }
                else {
                    moCompBizPartnerBranchNeighborhood.read(session, new int[] { mnFkDestinationBizPartnerAddress_n, mnFkDestinationAddressAddress_n });
                }
            }
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkBillOfLadingId + ", " + 
                mnPkLocationId + ", " + 
                mdDistance + ", " + 
                (mtDateDeparture_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateDeparture_n) + "', ") + 
                (mtDateArrival_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateArrival_n) + "', ") + 
                mnLocationType + ", " + 
                (mbDeleted ? 1 : 0) + ", " + 
                (mnFkOriginBizPartner_n == 0 ? "NULL, " : mnFkOriginBizPartner_n + ", ") + 
                (mnFkOriginBizPartnerAddress_n == 0 ? "NULL, " : mnFkOriginBizPartnerAddress_n + ", ") + 
                (mnFkOriginAddressAddress_n == 0 ? "NULL, " : mnFkOriginAddressAddress_n + ", ") + 
                (mnFkOriginNeighborhoodZipCode_n == 0 ? "NULL, " : mnFkOriginNeighborhoodZipCode_n + ", ") + 
                (mnFkDestinationBizPartner_n == 0 ? "NULL, " : mnFkDestinationBizPartner_n + ", ") + 
                (mnFkDestinationBizPartnerAddress_n == 0 ? "NULL, " : mnFkDestinationBizPartnerAddress_n + ", ") + 
                (mnFkDestinationAddressAddress_n == 0 ? "NULL, " : mnFkDestinationAddressAddress_n + ", ") +
                (mnFkDestinationNeighborhoodZipCode_n == 0 ? "NULL " : mnFkDestinationNeighborhoodZipCode_n + " ") + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                //"id_bol = " + mnPkBillOfLadingId + ", " +
                //"id_location = " + mnPkLocationId + ", " +
                "distance = " + mdDistance + ", " +
                "dt_departure_n = " + (mtDateDeparture_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateDeparture_n) + "', ") +
                "dt_arrival_n = " + (mtDateArrival_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateArrival_n) + "', ") +
                "location_tp = " + mnLocationType + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_orig_bp_n = " + (mnFkOriginBizPartner_n == 0 ? "NULL, " : mnFkOriginBizPartner_n + ", ") +
                "fk_orig_bpb_add_n = " + (mnFkOriginBizPartnerAddress_n == 0 ? "NULL, " : mnFkOriginBizPartnerAddress_n + ", ") +
                "fk_orig_add_add_n = " + (mnFkOriginAddressAddress_n == 0 ? "NULL, " : mnFkOriginAddressAddress_n + ", ") +
                "fk_orig_nei_zip_code_n = " + (mnFkOriginNeighborhoodZipCode_n == 0 ? "NULL, " : mnFkOriginNeighborhoodZipCode_n + ", ") +
                "fk_dest_bp_n = " + (mnFkDestinationBizPartner_n == 0 ? "NULL, " : mnFkDestinationBizPartner_n + ", ") +
                "fk_dest_bpb_add_n = " + (mnFkDestinationBizPartnerAddress_n == 0 ? "NULL, " : mnFkDestinationBizPartnerAddress_n + ", ") +
                "fk_dest_add_add_n = " + (mnFkDestinationAddressAddress_n == 0 ? "NULL, " : mnFkDestinationAddressAddress_n + ", ") +
                "fk_dest_nei_zip_code_n = " + (mnFkDestinationNeighborhoodZipCode_n == 0 ? "NULL " : mnFkDestinationNeighborhoodZipCode_n + " ") +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Guardar la colonia conforme al domicilio
        
        moCompBizPartnerBranchNeighborhood = new SDbBizPartnerBranchAddressNeighborhood();
        
        if (mnFkOriginBizPartnerAddress_n != 0 && mnFkOriginAddressAddress_n != 0 && mnFkOriginNeighborhoodZipCode_n != 0) {
            moCompBizPartnerBranchNeighborhood.setPkBizPartnerBranchAddressId(mnFkOriginBizPartnerAddress_n);
            moCompBizPartnerBranchNeighborhood.setPkAddressAddressId(mnFkOriginAddressAddress_n);
            moCompBizPartnerBranchNeighborhood.setFkNeighborhoodZipCode(mnFkOriginNeighborhoodZipCode_n);
        }
        else if (mnFkDestinationBizPartnerAddress_n != 0 && mnFkDestinationAddressAddress_n != 0 && mnFkDestinationNeighborhoodZipCode_n != 0) {
            moCompBizPartnerBranchNeighborhood.setPkBizPartnerBranchAddressId(mnFkDestinationBizPartnerAddress_n);
            moCompBizPartnerBranchNeighborhood.setPkAddressAddressId(mnFkDestinationAddressAddress_n);
            moCompBizPartnerBranchNeighborhood.setFkNeighborhoodZipCode(mnFkDestinationNeighborhoodZipCode_n);
        }
        
        moCompBizPartnerBranchNeighborhood.save(session);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBolLocation clone() throws CloneNotSupportedException {
        SDbBolLocation registry = new SDbBolLocation(moBillOfLading);
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
        registry.setPkLocationId(this.getPkLocationId());
        registry.setDistance(this.getDistance());
        registry.setDateDeparture_n(this.getDateDeparture_n());
        registry.setDateArrival_n(this.getDateArrival_n());
        registry.setLocationType(this.getLocationType());
        registry.setDeleted(this.isDeleted());
        registry.setFkOriginBizPartner_n(this.getFkOriginBizPartner_n());
        registry.setFkOriginBizPartnerAddress_n(this.getFkOriginBizPartnerAddress_n());
        registry.setFkOriginAddressAddress_n(this.getFkOriginAddressAddress_n());
        registry.setFkOriginNeighborhoodZipCode_n(this.getFkOriginNeighborhoodZipCode_n());
        registry.setFkDestinationBizPartner_n(this.getFkDestinationBizPartner_n());
        registry.setFkDestinationBizPartnerAddress_n(this.getFkDestinationBizPartnerAddress_n());
        registry.setFkDestinationAddressAddress_n(this.getFkDestinationAddressAddress_n());
        registry.setFkDestinationNeighborhoodZipCode_n(this.getFkDestinationNeighborhoodZipCode_n());
        
        registry.setDataBizPartner(this.getDataBizPartner()); // el clon comparte este registro que es de solo lectura
        registry.setDataBizPartnerBranch(this.getDataBizPartnerBranch()); // el clon comparte este registro que es de solo lectura
        registry.setDataBizPartnerBranchAddress(this.getDataBizPartnerBranchAddress()); // el clon comparte este registro que es de solo lectura
        
        registry.setXtaLocationType(this.getXtaLocationType()); 
        registry.setXtaIsOrigin(this.getXtaIsOrigin());
        registry.setXtaIsDestination(this.getXtaIsDestination());
        
        for (SDbBolMerchandiseQuantity mc : this.getBolMerchandiseQuantityCharges()) {
            registry.getBolMerchandiseQuantityCharges().add(mc.clone());
        }
        for (SDbBolMerchandiseQuantity mc : this.getBolMerchandiseQuantityDischarges()) {
            registry.getBolMerchandiseQuantityDischarges().add(mc.clone());
        }
        for (SRowBillOfLading row : this.getRowBolPrecharges()) {
            registry.getRowBolPrecharges().add(row); // se copiarán sólo referencias a los mismos objetos
        }
        for (SRowBillOfLading row : this.getRowBolCurrentCharges()) {
            registry.getRowBolCurrentCharges().add(row); // se copiarán sólo referencias a los mismos objetos
        }
        
        if (moCompBizPartnerBranchNeighborhood != null) {
            registry.setDbmsBizPartnerBranchNeighborhood(this.getDbmsBizPartnerBranchNeighborhood().clone());
        }
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch(row) {
            case 0: 
                value = msXtaLocationType;
                break;
            case 1:
                value = mdDistance;
                break;
            case 2:
                value = mbXtaIsOrigin;
                break;
            case 3: 
                value = mbXtaIsDestination;
                break;
            case 4: 
                value = moDataBizParter.getBizPartner();
                break;
            case 5:
                value = moDataBizParterBranch.getBizPartnerBranch();
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            default:
        }
    }
}
