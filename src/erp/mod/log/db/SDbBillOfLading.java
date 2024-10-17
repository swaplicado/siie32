/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import cfd.DCfdConsts;
import cfd.DElement;
import cfd.ver40.DCfdi40Catalogs;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mloc.data.SDataBolZipCode;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.STrnCfdRelatedDocs;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBillOfLading extends SDbRegistryUser implements erp.cfd.SCfdXmlCfdi33, erp.cfd.SCfdXmlCfdi40, Serializable {
    
    protected int mnPkBillOfLadingId;
    protected String msBillOfLadingUuid;
    protected String msBillOfLadingType;
    protected String msSeries;
    protected String msNumber;
    protected Date mtDate;
    protected String msFiscalIdReceptor;
    protected boolean mbInternationalBol;
    protected String msInputOutputBol;
    protected String msInputOutputWay;
    protected double mdTotalDistance;
    protected double mdGrossWeight;
    protected int mnMerchandiseNodes;
    protected String msEnvironmentalInsurerPolicy;
    protected String msMerchandiseInsurerPolicy;
    protected String msPremium;
    protected boolean mbReverseLogistics;
    //protected boolean mbDeleted;
    protected int mnFkBillOfLadingStatusId;
    protected int mnFkCompanyBranchId;
    protected int mnFkInputOutputCountry_n;
    protected int mnFkGrossWeightUnit;
    protected int mnFkEnviromentalInsurer_n;
    protected int mnFkMerchandiseInsurer_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbBolTransportationMode moBolTransportationMode;
    protected ArrayList<SDbBolLocation> maBolLocations;
    protected ArrayList<SDbBolMerchandise> maBolMerchandises;
    
    protected SDataCfd moDataCfd;
    
    protected String msXtaCtyCode;
    protected String msXtaGrossWeightUnitCode;
    protected String msXtaTaxRegime;
    protected SDbInsurer moDataEnvironmentalInsurer;
    protected SDbInsurer moDataMerchandiseInsurer;
    
    protected int mnAuxCfdId;
    protected String msAuxCfdExportation;
    protected String msAuxCfdCfdiRelacionado33TipoRelacion;
    protected String msAuxCfdCfdiRelacionado33Uuid; // available when CFDI is not stored in SIIE, e.g., third-party
    protected STrnCfdRelatedDocs moAuxCfdRelatedDocs;
    protected SDataBizPartner moAuxDbmsDataEmisor;
    protected SDataBizPartnerBranch moAuxDbmsDataEmisorSucursal;
    
    protected int mnLastDbActionResult;
    protected int mnDbmsErrorId;
    protected java.lang.String msDbmsError;
    
    public SDbBillOfLading() {
        super(SModConsts.LOG_BOL);
    }
    
    public void readBizPartner(SGuiSession session, int pk) {
        moAuxDbmsDataEmisorSucursal = new SDataBizPartnerBranch();
        moAuxDbmsDataEmisorSucursal.read(new int[] { pk }, session.getStatement());
                
        moAuxDbmsDataEmisor = new SDataBizPartner();
        moAuxDbmsDataEmisor.read(new int[] { moAuxDbmsDataEmisorSucursal.getFkBizPartnerId() }, session.getStatement());
    }
    
    public void addMerchandise(SDbBolMerchandise o) {
        boolean found = false;
        for (SDbBolMerchandise merch : maBolMerchandises) {
            if (merch.getFkItemId() == o.getFkItemId()) {
                found = true;
                merch.addMerchandiseQuantity(o.getChildBolMerchandiseQuantities().get(0));
                merch.updateTotalItemQuantity();
            }
        }
        if (!found) {
            maBolMerchandises.add(o);
        }
    }
    
    public void removeMerchandise(SDbBolMerchandise o) {
        for (SDbBolMerchandise merch : maBolMerchandises) {
            if (merch.getFkItemId() == o.getFkItemId()) {
                merch.removeMerchandiseQuantity(o.getChildBolMerchandiseQuantities().get(0));
                if (merch.maChildBolMerchandiseQuantities.isEmpty()) {
                    maBolMerchandises.remove(merch);
                    break;
                }
                else {
                    merch.updateTotalItemQuantity();
                }
            }
        }
    }
    
    public void updateSatCtyCode (SGuiSession session) {
        try {
            String sql = "SELECT cty_code FROM erp.locu_cty where id_cty = " + mnFkInputOutputCountry_n + ";";
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                msXtaCtyCode = resultSet.getString(1);
            }
        }
        catch(Exception e) {}
    }
    
    public void updateGrossWeightUnitCode(SGuiSession session) {
        try {
            String sql = "SELECT cfd.code FROM erp.itms_cfd_unit AS cfd "
                    + "INNER JOIN erp.itmu_unit AS u ON u.fid_cfd_unit = cfd.id_cfd_unit "
                    + "WHERE u.id_unit = " + mnFkGrossWeightUnit;
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                msXtaGrossWeightUnitCode = resultSet.getString(1);
            }
        }
        catch (Exception e) {}
    }
    
    public int canDisable(Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testDisable(connection, "No se pudo anular ", SDbConsts.ACTION_ANNUL)) {
                mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_YES;
            }
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL + "\n" + exception.toString();
            }
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }
    
    public String getDisableError() {
        return msDbmsError;
    }
    
    public void setPkBillOfLadingId(int n) { mnPkBillOfLadingId = n; }
    public void setBillOfLadingUuid(String s) { msBillOfLadingUuid = s; }
    public void setBillOfLadingType(String s) { msBillOfLadingType = s; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setFiscalIdReceptor(String s) { msFiscalIdReceptor = s; }
    public void setInternationalBol(boolean b) { mbInternationalBol = b; }
    public void setInputOutputBol(String s) { msInputOutputBol = s; }
    public void setInputOutputWay(String s) { msInputOutputWay = s; }
    public void setTotalDistance(double d) { mdTotalDistance = d; }
    public void setGrossWeight(double d) { mdGrossWeight = d; }
    public void setMerchandiseNodes(int n) { mnMerchandiseNodes = n; }
    public void setEnviromentalInsurerPolicy(String s) { msEnvironmentalInsurerPolicy = s; }
    public void setMerchandiseInsurerPolicy(String s) { msMerchandiseInsurerPolicy = s; }
    public void setPremium(String s) { msPremium = s; }
    public void setReverseLogistics(boolean b) { mbReverseLogistics = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBillOfLadingStatusId(int n) { mnFkBillOfLadingStatusId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkInputOutputCountry_n(int n) { mnFkInputOutputCountry_n = n; }
    public void setFkGrossWeightUnit(int n) { mnFkGrossWeightUnit = n; }
    public void setFkEnviromentalInsurer_n(int n) { mnFkEnviromentalInsurer_n = n; }
    public void setFkMerchandiseInsurer_n(int n) { mnFkMerchandiseInsurer_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
    public String getBillOfLadingUuid() { return msBillOfLadingUuid; }
    public String getBillOfLadingType() { return msBillOfLadingType; }
    public String getSeries() { return msSeries; }
    public String getNumber() { return msNumber; }
    public Date getDate() { return mtDate; }
    public String getFiscalIdReceptor() { return msFiscalIdReceptor; }
    public boolean isInternationalBol() { return mbInternationalBol; }
    public String getInputOutputBol() { return msInputOutputBol; }
    public String getInputOutputWay() { return msInputOutputWay; }
    public double getTotalDistance() { return mdTotalDistance; }
    public double getGrossWeight() { return mdGrossWeight; }
    public int getMerchandiseNodes() { return mnMerchandiseNodes; }
    public String getEnviromentalInsurerPolicy() { return msEnvironmentalInsurerPolicy; }
    public String getMerchandiseInsurerPolicy() { return msMerchandiseInsurerPolicy; }
    public String getPremium() { return msPremium; }
    public boolean isReverseLogistics() { return mbReverseLogistics; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBillOfLadingStatusId() { return mnFkBillOfLadingStatusId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkInputOutputCountry_n() { return mnFkInputOutputCountry_n; }
    public int getFkGrossWeightUnit() { return mnFkGrossWeightUnit; }
    public int getFkEnviromentalInsurer_n() { return mnFkEnviromentalInsurer_n; }
    public int getFkMerchandiseInsurer_n() { return mnFkMerchandiseInsurer_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setBolTransportationMode(SDbBolTransportationMode o) { moBolTransportationMode = o; }
    
    public SDbBolTransportationMode getBolTransportationMode() { return moBolTransportationMode; }
    public ArrayList<SDbBolLocation> getBolLocations() { return maBolLocations; }
    public ArrayList<SDbBolMerchandise> getBolMerchandises() { return maBolMerchandises; }
    
    public void setDataCfd (SDataCfd o) { moDataCfd = o;}
    
    public SDataCfd getDataCfd() { return moDataCfd; }
    
    public void setXtaCtyCode(String s) { msXtaCtyCode = s; }
    public void setXtaGrossWeightUnitCode(String s) { msXtaGrossWeightUnitCode = s; }
    public void setXtaTaxRegime(String s) { msXtaTaxRegime = s; }
    public void setDataEnvironmentalInsurer(SDbInsurer o) { moDataEnvironmentalInsurer = o; } 
    public void setDataMerchandiseInsurer(SDbInsurer o) { moDataMerchandiseInsurer = o; } 
    
    public String getXtaCtyCode() { return msXtaCtyCode; }
    public String getXtaGrossWeightUnitCode() { return msXtaGrossWeightUnitCode; }
    public String getXtaTaxRegime() { return msXtaTaxRegime; }
    public SDbInsurer getDataEnvironmentalInsurer() { return moDataEnvironmentalInsurer; }
    public SDbInsurer getDataMerchandiseInsurer() { return moDataMerchandiseInsurer; }
    
    public void setAuxCfdId(int i) { mnAuxCfdId = i; }
    public void setAuxCfdExportation(String s) { msAuxCfdExportation = s; }
    public void setAuxCfdCfdiRelacionado33TipoRelacion(String s) { msAuxCfdCfdiRelacionado33TipoRelacion = s; }
    public void setAuxCfdCfdiRelacionado33Uuid(String s) { msAuxCfdCfdiRelacionado33Uuid = s; }
    public void setAuxCfdRelatedDocs(STrnCfdRelatedDocs o) { moAuxCfdRelatedDocs = o; }
    public void setAuxDbmsDataEmisor(SDataBizPartner o) { moAuxDbmsDataEmisor = o; }
    public void setAuxDbmsDataEmisorSucursal(SDataBizPartnerBranch o) { moAuxDbmsDataEmisorSucursal = o; }
    
    public int getAuxCfdId() { return mnAuxCfdId; }
    public String getAuxCfdExportation() { return msAuxCfdExportation; }
    public String getAuxCfdCfdiRelacionado33TipoRelacion() { return msAuxCfdCfdiRelacionado33TipoRelacion; }
    public String getAuxCfdCfdiRelacionado33Uuid() { return msAuxCfdCfdiRelacionado33Uuid; }
    //public STrnCfdRelated getCfdiRelacionados() { return moAuxCfdiRelacionados; } // se implementa en las interfaces
    public SDataBizPartner getAuxDbmsDataEmisor() { return moAuxDbmsDataEmisor; }
    public SDataBizPartnerBranch getAuxDbmsDataEmisorSucursal() { return moAuxDbmsDataEmisorSucursal; }
    
    public void computeNumber(SGuiSession session, int type) throws SQLException, Exception {
        ResultSet resultSet;
        
        msNumber = "";
        
        msSql = "SELECT COALESCE(MAX(CONVERT(num, UNSIGNED INTEGER)), 0) + 1 FROM " + getSqlTable() + " WHERE ser = '" + msSeries + "' "
                + "AND bol_tp = " + (SDataConstantsSys.TRNS_TP_CFD_INV == type ? "'I'" : "'T'");
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            msNumber = resultSet.getString(1);
        }
    }
        
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBillOfLadingId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBillOfLadingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBillOfLadingId = 0;
        msBillOfLadingUuid = "";
        msBillOfLadingType = "";
        msSeries = "";
        msNumber = "";
        mtDate = null;
        msFiscalIdReceptor = "";
        mbInternationalBol = false;
        msInputOutputBol = "";
        msInputOutputWay = "";
        mdTotalDistance = 0;
        mdGrossWeight = 0;
        mnMerchandiseNodes = 0;
        msEnvironmentalInsurerPolicy = "";
        msMerchandiseInsurerPolicy = "";
        msPremium = "";
        mbReverseLogistics = false;
        mbDeleted = false;
        mnFkBillOfLadingStatusId = 0;
        mnFkCompanyBranchId = 0;
        mnFkInputOutputCountry_n = 0;
        mnFkGrossWeightUnit = 0;
        mnFkEnviromentalInsurer_n = 0;
        mnFkMerchandiseInsurer_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moBolTransportationMode = null;
        maBolLocations = new ArrayList<>();
        maBolMerchandises = new ArrayList<>();
        
        moDataCfd = null;
        
        msXtaCtyCode = "";
        msXtaGrossWeightUnitCode = "";
        msXtaTaxRegime = "";
        moDataEnvironmentalInsurer = null;
        moDataMerchandiseInsurer = null;
        
        mnAuxCfdId = 0;
        msAuxCfdExportation = "";
        msAuxCfdCfdiRelacionado33TipoRelacion = "";
        msAuxCfdCfdiRelacionado33Uuid = ""; 
        moAuxCfdRelatedDocs = null;
        moAuxDbmsDataEmisor = null;
        moAuxDbmsDataEmisorSucursal = null;
        
        mnLastDbActionResult = 0;
        mnDbmsErrorId = 0;
        msDbmsError = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBillOfLadingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkBillOfLadingId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_bol), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBillOfLadingId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT b.*, c.id_cfd FROM " + getSqlTable() + " AS b " +
                "LEFT OUTER JOIN trn_cfd AS c on b.id_bol = c.fid_bol_n " +
                "WHERE id_bol = " + pk[0];
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            msBillOfLadingUuid = resultSet.getString("bol_uuid");
            msBillOfLadingType = resultSet.getString("bol_tp");
            msSeries = resultSet.getString("ser");
            msNumber = resultSet.getString("num");
            mtDate = resultSet.getTimestamp("dt");
            msFiscalIdReceptor = resultSet.getString("fiscal_id_rec");
            mbInternationalBol = resultSet.getBoolean("int_bol");
            msInputOutputBol = resultSet.getString("input_output_bol");
            msInputOutputWay = resultSet.getString("input_output_way_key");
            mdTotalDistance = resultSet.getDouble("distance_tot");
            mdGrossWeight = resultSet.getDouble("gross_weight");
            mnMerchandiseNodes = resultSet.getInt("merch_nodes");
            msEnvironmentalInsurerPolicy = resultSet.getString("environmental_ins_policy");
            msMerchandiseInsurerPolicy = resultSet.getString("merchandise_ins_policy");
            msPremium = resultSet.getString("premium");
            mbReverseLogistics = resultSet.getBoolean("b_rev_log");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBillOfLadingStatusId = resultSet.getInt("fk_st_bol");
            mnFkCompanyBranchId = resultSet.getInt("fk_cob");
            mnFkInputOutputCountry_n = resultSet.getInt("fk_input_output_cty_n");
            mnFkGrossWeightUnit = resultSet.getInt("fk_gross_weight_unit");
            mnFkEnviromentalInsurer_n = resultSet.getInt("fk_environmental_ins_n");
            mnFkMerchandiseInsurer_n = resultSet.getInt("fk_merchandise_ins_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mnAuxCfdId = resultSet.getInt("id_cfd");
            msAuxCfdExportation = !mbInternationalBol ? DCfdi40Catalogs.ClaveExportacionNoAplica : DCfdi40Catalogs.ClaveExportacionDefinitivaA1;
            
            mbRegistryNew = false;
        }
        
        // Read transportation mode:
        
        msSql = "SELECT id_transp_mode "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " "
                + "ORDER BY id_transp_mode ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            moBolTransportationMode = new SDbBolTransportationMode();
            moBolTransportationMode.read(session, new int[] { mnPkBillOfLadingId, resultSet.getInt("id_transp_mode") } ) ;
        }
        
        // Read locations:
        
        msSql = "SELECT id_location "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_LOCATION) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " "
                + "ORDER BY id_location ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbBolLocation location = new SDbBolLocation(this);
            location.read(session, new int[] { mnPkBillOfLadingId, resultSet.getInt("id_location") } );
            maBolLocations.add(location);
        }
        
        // Read merchandises:
        
        msSql = "SELECT id_merch "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " "
                + "ORDER BY id_merch ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbBolMerchandise merch = new SDbBolMerchandise();
            merch.read(session, new int[] { mnPkBillOfLadingId, resultSet.getInt("id_merch") } );
            maBolMerchandises.add(merch);
        }
        
        // Read Insurers
        
        if (mnFkEnviromentalInsurer_n != 0) {
            moDataEnvironmentalInsurer = new SDbInsurer();
            moDataEnvironmentalInsurer.read(session, new int[] { mnFkEnviromentalInsurer_n });
        }
        if (mnFkMerchandiseInsurer_n != 0) {
            moDataMerchandiseInsurer = new SDbInsurer();
            moDataMerchandiseInsurer.read(session, new int[] { mnFkMerchandiseInsurer_n });
        }
        
        // Read cfd
        
        if (mnAuxCfdId != 0) {
            moDataCfd = new SDataCfd();
            moDataCfd.read(new int[] { mnAuxCfdId }, statement);
        }
        
        updateSatCtyCode(session);
        updateGrossWeightUnitCode(session);
        readBizPartner(session, mnFkCompanyBranchId);
        
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
            msBillOfLadingUuid = "CCC" + java.util.UUID.randomUUID().toString().substring(3).toUpperCase();
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkBillOfLadingId + ", " + 
                "'" + msBillOfLadingUuid + "', " + 
                "'" + msBillOfLadingType + "', " + 
                "'" + msSeries + "', " + 
                "'" + msNumber + "', " + 
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                "'" + msFiscalIdReceptor + "', " + 
                (mbInternationalBol ? 1 : 0) + ", " + 
                "'" + msInputOutputBol + "', " + 
                "'" + msInputOutputWay + "', " + 
                mdTotalDistance + ", " + 
                mdGrossWeight + ", " + 
                mnMerchandiseNodes + ", " + 
                "'" + msEnvironmentalInsurerPolicy + "', " + 
                "'" + msMerchandiseInsurerPolicy + "', " + 
                "'" + msPremium + "', " + 
                (mbReverseLogistics ? 1 : 0) + ", " + 
                (mbDeleted ? 1 : 0) + ", " + 
                mnFkBillOfLadingStatusId + ", " + 
                mnFkCompanyBranchId + ", " + 
                (mnFkInputOutputCountry_n == 0 ? "NULL, " : mnFkInputOutputCountry_n + ", ") + 
                mnFkGrossWeightUnit + ", " + 
                (mnFkEnviromentalInsurer_n == 0 ? "NULL, " : mnFkEnviromentalInsurer_n + ", ") + 
                (mnFkMerchandiseInsurer_n == 0 ? "NULL, " : mnFkMerchandiseInsurer_n + ", ") + 
                mnFkUserInsertId + ", " + 
                mnFkUserUpdateId + ", " + 
                "NOW()" + ", " + 
                "NOW()" + " " + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                //"id_bol = " + mnPkBillOfLadingId + ", " +
                //"bol_uuid = '" + msBillOfLadingUuid + "', " +
                "bol_tp = '" + msBillOfLadingType + "', " +
                "ser = '" + msSeries + "', " +
                "num = '" + msNumber + "', " +
                "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                "fiscal_id_rec = '" + msFiscalIdReceptor + "', " +
                "int_bol = " + (mbInternationalBol ? 1 : 0) + ", " +
                "input_output_bol = '" + msInputOutputBol + "', " +
                "input_output_way_key = '" + msInputOutputWay + "', " +
                "distance_tot = " + mdTotalDistance + ", " +
                "gross_weight = " + mdGrossWeight + ", " +
                "merch_nodes = " + mnMerchandiseNodes + ", " +
                "environmental_ins_policy = '" + msEnvironmentalInsurerPolicy + "', " +
                "merchandise_ins_policy = '" + msMerchandiseInsurerPolicy + "', " +
                "premium = '" + msPremium + "', " +
                "b_rev_log = " + (mbReverseLogistics ? 1 : 0) + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_st_bol = " + mnFkBillOfLadingStatusId + ", " +
                "fk_cob = " + mnFkCompanyBranchId + ", " +
                "fk_input_output_cty_n = " + (mnFkInputOutputCountry_n == 0 ? "NULL, " : mnFkInputOutputCountry_n + ", ") +
                "fk_gross_weight_unit = " + mnFkGrossWeightUnit + ", " +
                "fk_environmental_ins_n = " + (mnFkEnviromentalInsurer_n == 0 ? "NULL, " : mnFkEnviromentalInsurer_n + ", ") +
                "fk_merchandise_ins_n = " + (mnFkMerchandiseInsurer_n == 0 ? "NULL, " : mnFkMerchandiseInsurer_n + ", ") +
                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                //"ts_usr_ins = " + "NOW()" + ", " +
                "ts_usr_upd = " + "NOW()" + " " +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save transportation mode:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE_EXTRA) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " ";
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " ";
        session.getStatement().execute(msSql);
        
        moBolTransportationMode.setPkBillOfLadingId(mnPkBillOfLadingId);
        moBolTransportationMode.setRegistryNew(true);
        moBolTransportationMode.save(session);
        
        // Save locations:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_LOCATION) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " ";
        session.getStatement().execute(msSql);
        
        for (SDbBolLocation location : maBolLocations) {
            location.setPkBillOfLadingId(mnPkBillOfLadingId);
            location.setRegistryNew(true);
            location.save(session);
        }
        
        // Save merchandises
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH_QTY) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " ";
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " ";
        session.getStatement().execute(msSql);
        
        for (SDbBolMerchandise merchandise : maBolMerchandises) {
            merchandise.setPkBillOfLadingId(mnPkBillOfLadingId);
            merchandise.setRegistryNew(true);
            merchandise.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBillOfLading clone() throws CloneNotSupportedException {
        SDbBillOfLading registry = new SDbBillOfLading();
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
        registry.setBillOfLadingUuid(this.getBillOfLadingUuid());
        registry.setBillOfLadingType(this.getBillOfLadingType());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setFiscalIdReceptor(this.getFiscalIdReceptor());
        registry.setInternationalBol(this.isInternationalBol());
        registry.setInputOutputBol(this.getInputOutputBol());
        registry.setInputOutputWay(this.getInputOutputWay());
        registry.setTotalDistance(this.getTotalDistance());
        registry.setGrossWeight(this.getGrossWeight());
        registry.setMerchandiseNodes(this.getMerchandiseNodes());
        registry.setEnviromentalInsurerPolicy(this.getEnviromentalInsurerPolicy());
        registry.setMerchandiseInsurerPolicy(this.getMerchandiseInsurerPolicy());
        registry.setPremium(this.getPremium());
        registry.setReverseLogistics(this.isReverseLogistics());
        registry.setDeleted(this.isDeleted());
        registry.setFkBillOfLadingStatusId(this.getFkBillOfLadingStatusId());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkInputOutputCountry_n(this.getFkInputOutputCountry_n());
        registry.setFkGrossWeightUnit(this.getFkGrossWeightUnit());
        registry.setFkEnviromentalInsurer_n(this.getFkEnviromentalInsurer_n());
        registry.setFkMerchandiseInsurer_n(this.getFkMerchandiseInsurer_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        if (this.getBolTransportationMode() != null) {
            registry.setBolTransportationMode(this.getBolTransportationMode().clone());
        }
        
        for (SDbBolLocation loc : this.getBolLocations()) {
            registry.getBolLocations().add(loc.clone());
        }
        for (SDbBolMerchandise merch : this.getBolMerchandises()) {
            registry.getBolMerchandises().add(merch.clone());
        }
        
        registry.setDataCfd(this.getDataCfd()); 
        
        registry.setXtaCtyCode(this.getXtaCtyCode());
        registry.setXtaGrossWeightUnitCode(this.getXtaGrossWeightUnitCode());
        registry.setXtaTaxRegime(this.getXtaTaxRegime());
        registry.setDataEnvironmentalInsurer(this.getDataEnvironmentalInsurer());
        registry.setDataMerchandiseInsurer(this.getDataMerchandiseInsurer());
        
        registry.setAuxCfdId(this.getAuxCfdId());
        registry.setAuxCfdExportation(this.getAuxCfdExportation());
        registry.setAuxCfdCfdiRelacionado33TipoRelacion(this.getAuxCfdCfdiRelacionado33TipoRelacion());
        registry.setAuxCfdCfdiRelacionado33Uuid(this.getAuxCfdCfdiRelacionado33Uuid());
        registry.setAuxCfdRelatedDocs(this.getCfdiRelacionados()); // este miembro no se clona
        registry.setAuxDbmsDataEmisor(this.getAuxDbmsDataEmisor()); // el clon comparte este registro que es de sólo lectura
        registry.setAuxDbmsDataEmisorSucursal(this.getAuxDbmsDataEmisorSucursal()); // el clon comparte este registro que es de sólo lectura

        return registry;
    }
    
    private boolean testDisable(Connection connection, java.lang.String psMsg, int pnAction) throws java.sql.SQLException, java.lang.Exception {
        int i;
        int[] anPeriodKey = null;
        String sMsg = psMsg;
        CallableStatement oCallableStatement = null;

        if (pnAction == SDbConsts.ACTION_DELETE && mbDeleted) {
            mnDbmsErrorId = 1;
            msDbmsError = sMsg + "¡El documento ya está eliminado!";
            throw new Exception(msDbmsError);
        }
        else if (pnAction == SDbConsts.ACTION_ANNUL && mnFkBillOfLadingStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            mnDbmsErrorId = 2;
            msDbmsError = sMsg + "¡El documento ya está anulado!";
            throw new Exception(msDbmsError);
        }
        else if (mbSystem) {
            mnDbmsErrorId = 11;
            msDbmsError = sMsg + "¡El documento es de sistema!";
            throw new Exception(msDbmsError);
        }
        
        else if (pnAction == SDbConsts.ACTION_DELETE && moDataCfd != null && (moDataCfd.isStamped())) {
            mnDbmsErrorId = 21;
            msDbmsError = sMsg + "¡El documento está timbrado!";
            throw new Exception(msDbmsError);
        }
        else if (mnFkBillOfLadingStatusId != SDataConstantsSys.TRNS_ST_DPS_EMITED) {
            mnDbmsErrorId = 41;
            msDbmsError = sMsg + "¡El documento debe tener estatus 'emitido'!";
            throw new Exception(msDbmsError);
        }
        else {

            // Check that document's date belongs to an open period:

            i = 1;
            anPeriodKey = SLibTimeUtilities.digestYearMonth(mtDate);
            oCallableStatement = connection.prepareCall("{ CALL fin_year_per_st(?, ?, ?) }");
            oCallableStatement.setInt(i++, anPeriodKey[0]);
            oCallableStatement.setInt(i++, anPeriodKey[1]);
            oCallableStatement.registerOutParameter(i++, java.sql.Types.INTEGER);
            oCallableStatement.execute();

            if (oCallableStatement.getBoolean(i - 1)) {
                mnDbmsErrorId = 101;
                msDbmsError = sMsg + "¡El período contable de la fecha del documento está cerrado!";
                throw new Exception(msDbmsError);
            }
        }
        
        return true;
    }
    
    public int disable(Connection connection) {
        String sSql;
        Statement oStatement;
        mnQueryResultId = SLibConsts.UNDEFINED;

        try {
            oStatement = connection.createStatement();

            // Set BOL as annuled:

            if (testDisable(connection, "No se pudo anular ", SDbConsts.ACTION_ANNUL)) {
            mnFkBillOfLadingStatusId = SDataConstantsSys.TRNS_ST_DPS_ANNULED;

            sSql = "UPDATE log_bol SET fk_st_bol = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", "  +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", ts_usr_upd = NOW() " +
                    "WHERE id_bol = " + mnPkBillOfLadingId + " ";
            oStatement.execute(sSql);
            if (moDataCfd != null) {
                moDataCfd.annul(connection);
            }

            mnQueryResultId = SLibConstants.DB_ACTION_ANNUL_OK;
            }
            
        }
        
        catch (Exception e) {
            mnQueryResultId = SLibConstants.DB_ACTION_ANNUL_ERROR;
            String msDbmsError = SLibConstants.MSG_ERR_DB_REG_ANNUL;
            msDbmsError += "\n" + e.toString();
            SLibUtilities.renderException(msDbmsError, e);
        }

        return mnQueryResultId;
    }
    
    private cfd.ver3.ccp20.DElementCartaPorte complemento20() {
        
        // Encabezado:
        
        cfd.ver3.ccp20.DElementCartaPorte ccp = new cfd.ver3.ccp20.DElementCartaPorte();
        ccp.getAttTransInternac().setString(mbInternationalBol ? DCfdi40Catalogs.TextoSí : DCfdi40Catalogs.TextoNo);
        if (mbInternationalBol) {
            ccp.getAttEntradaSalidaMerc().setString(msInputOutputBol);
            ccp.getAttViaEntradaSalida().setString(msInputOutputWay);
            ccp.getAttPaisOrigenDestino().setString(msXtaCtyCode);
        }
        ccp.getAttTotalDistRec().setDouble(mdTotalDistance);
        
        // Ubicaciones:
        
        ArrayList<cfd.ver3.ccp20.DElementUbicacion> ubicaciones = ccp.getEltUbicaciones().getEltUbicaciones();
        for (SDbBolLocation location : maBolLocations) {
            cfd.ver3.ccp20.DElementUbicacion origen = null;
            cfd.ver3.ccp20.DElementUbicacion destino = null;
            
            if (location.getXtaIsOrigin()) {
                origen = new cfd.ver3.ccp20.DElementUbicacion();
                origen.getAttTipoUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónOrigen);
                origen.getAttIDUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónOrigenPrefijoId + location.getDataBizPartnerBranchAddress().getAddressCode());
                if (location.getDataBizPartner().getFiscalId().isEmpty()) {
                    origen.getAttNumRegIdTrib().setString(location.getDataBizPartner().getFiscalId());
                    origen.getAttNombreRemitenteDestinatario().setString(location.getDataBizPartner().getBizPartner());
                    origen.getAttResidenciaFiscal().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    origen.getAttRFCRemitenteDestinatario().setString(location.getDataBizPartner().getFiscalId());
                }
                origen.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateDeparture_n());
                
                // Domicilio: 
                
                cfd.ver3.ccp20.DElementDomicilio domicilio = origen.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsBolNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getDataBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getDataBizPartnerBranchAddress().getZipCode()); 
                if (location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode() != null) {
                    SDataBolZipCode bolZipCode = location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode();
                    if (bolZipCode.getDbmsBolCounty() != null) {
                        domicilio.getAttMunicipio().setString(bolZipCode.getDbmsBolCounty().getPkCountyCode());
                    }
                    if (bolZipCode.getDbmsBolLocality() != null) {
                        domicilio.getAttLocalidad().setString(bolZipCode.getDbmsBolLocality().getPkLocalityCode());
                    }
                }
            }
            
            if (location.getXtaIsDestination()) {
                destino = new cfd.ver3.ccp20.DElementUbicacion();
                destino.getAttTipoUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónDestino);
                destino.getAttIDUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónDestinoPrefijoId + location.getDataBizPartnerBranchAddress().getAddressCode());
                if (location.getDataBizPartner().getFiscalId().isEmpty()) {
                    destino.getAttNumRegIdTrib().setString(location.getDataBizPartner().getFiscalId());
                    destino.getAttNombreRemitenteDestinatario().setString(location.getDataBizPartner().getBizPartner());
                    destino.getAttResidenciaFiscal().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    destino.getAttRFCRemitenteDestinatario().setString(location.getDataBizPartner().getFiscalId());
                }
                destino.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateArrival_n());
                destino.getAttDistanciaRecorrida().setDouble(location.getDistance());
                
                // Domicilio: 
                
                cfd.ver3.ccp20.DElementDomicilio domicilio = destino.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsBolNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getDataBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getDataBizPartnerBranchAddress().getZipCode()); 
                if (location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode() != null) {
                    SDataBolZipCode zipCode = location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode();
                    if (zipCode.getDbmsBolCounty() != null) {
                        domicilio.getAttMunicipio().setString(zipCode.getDbmsBolCounty().getPkCountyCode());
                    }
                    if (zipCode.getDbmsBolLocality() != null) {
                        domicilio.getAttLocalidad().setString(zipCode.getDbmsBolLocality().getPkLocalityCode());
                    }
                }
            }
            
            if (origen != null) {
                ubicaciones.add(origen);
            }
            
            if (destino != null) {
                ubicaciones.add(destino);
            }
        }
        
        // Encabezado Mercancias:
        
        cfd.ver3.ccp20.DElementMercancias encabezadoMercancias = ccp.getEltMercancias();
        encabezadoMercancias.getAttPesoBrutoTotal().setDouble(mdGrossWeight);
        encabezadoMercancias.getAttUnidadPeso().setString(msXtaGrossWeightUnitCode);
        encabezadoMercancias.getAttNumTotalMercancias().setInteger(maBolMerchandises.size());
        
        // Mercancias:
        
        ArrayList<cfd.ver3.ccp20.DElementMercancia> mercancias = encabezadoMercancias.getEltMercancias();
        for (SDbBolMerchandise merch : maBolMerchandises) {
            cfd.ver3.ccp20.DElementMercancia mercancia = new cfd.ver3.ccp20.DElementMercancia();
            mercancia.getAttBienesTransp().setString(merch.getXtaItemClaveProdServ());
            mercancia.getAttDescripcion().setString(merch.getDataItem().getItem());
            mercancia.getAttCantidad().setDouble(merch.getQuantity());
            mercancia.getAttClaveUnidad().setString(merch.getXtaClaveUnidad());
            mercancia.getAttPesoEnKg().setDouble(merch.getXtaClaveUnidad().equals("KGM") ? merch.getQuantity() : merch.getWeight());
            mercancia.getAttFraccionArancelaria().setString(merch.getExternalUUID());
            
            // Cantidad transporta:
            
            ArrayList<SDbBolMerchandiseQuantity> qtyOr = new ArrayList<>();
            ArrayList<SDbBolMerchandiseQuantity> qtyDe = new ArrayList<>();
            for (SDbBolMerchandiseQuantity qty : merch.getChildBolMerchandiseQuantities()) {
                if (qty.getDataOriginBizPartnerBranchAddress() != null) {
                    qtyOr.add(qty);
                }
                if (qty.getDataDestinationBizPartnerBranchAddress() != null) {
                    qtyDe.add(qty);
                }
            }
            
            ArrayList<cfd.ver3.ccp20.DElementCantidadTransporta> transporta = mercancia.getEltCantidadTransporta();
            
            for (SDbBolMerchandiseQuantity or : qtyOr) {
                if (or.getQuantity() > 0) {
                    for (SDbBolMerchandiseQuantity de : qtyDe) {
                        if (de.getQuantity() > 0) {
                            cfd.ver3.ccp20.DElementCantidadTransporta elementTransp = new cfd.ver3.ccp20.DElementCantidadTransporta();
                            elementTransp.getAttIDOrigen().setString(DCfdi40Catalogs.CcpUbicaciónOrigenPrefijoId + or.getDataOriginBizPartnerBranchAddress().getAddressCode());
                            elementTransp.getAttIDDestino().setString(DCfdi40Catalogs.CcpUbicaciónDestinoPrefijoId + de.getDataDestinationBizPartnerBranchAddress().getAddressCode());
                            if (or.getQuantity() > de.getQuantity()) {
                                or.setQuantity(or.getQuantity() - de.getQuantity());                                
                                elementTransp.getAttCantidad().setDouble(de.getQuantity());
                                de.setQuantity(0);                                
                            }
                            else if (or.getQuantity() < de.getQuantity()) {
                                de.setQuantity(de.getQuantity() - or.getQuantity());
                                elementTransp.getAttCantidad().setDouble(or.getQuantity());
                                or.setQuantity(0);
                            }
                            else if (or.getQuantity() == de.getQuantity()) {
                                elementTransp.getAttCantidad().setDouble(or.getQuantity());
                                or.setQuantity(0);
                                de.setQuantity(0);
                            }
                            transporta.add(elementTransp);
                        }
                    }
                }
            }
            
            // Pedimentos:
            
            //ArrayList<DElementPedimentos> pedimentos = mercancia.getEltPedimentos();
            
            mercancias.add(mercancia);
        }
        
        // Autotransporte:
        
        cfd.ver3.ccp20.DElementAutotransporte autotransporte = encabezadoMercancias.getEltAutotransporte();
        autotransporte.getAttPermSCT().setString(moBolTransportationMode.getDataVehicle().getPermissonSctType());
        autotransporte.getAttNumPermisoSCT().setString(moBolTransportationMode.getDataVehicle().getPermissonSctNumber());
        
        // Identificación vehicular:
        
        cfd.ver3.ccp20.DElementIdentificacionVehicular idVehicular = autotransporte.getEltIdentificacionVehicular();
        idVehicular.getAttConfigVehicular().setString(moBolTransportationMode.getDataVehicle().getVehicleConfiguration());
        idVehicular.getAttPlacaVM().setString(moBolTransportationMode.getDataVehicle().getPlate());
        idVehicular.getAttAnioModeloVM().setInteger(moBolTransportationMode.getDataVehicle().getVehicleYear());
        
        // Seguros: 
        
        cfd.ver3.ccp20.DElementSeguros seguros = autotransporte.getEltSeguros();
        seguros.getAttAseguraRespCivil().setString(moBolTransportationMode.getDataVehicle().getXtaInsurerName());
        seguros.getAttPolizaRespCivil().setString(moBolTransportationMode.getDataVehicle().getInsurancePolicy());
        seguros.getAttAseguraMedAmbiente().setString(moDataEnvironmentalInsurer.getName());
        seguros.getAttPolizaMedAmbiente().setString(msEnvironmentalInsurerPolicy);
        seguros.getAttAseguraCarga().setString(moDataMerchandiseInsurer.getName());
        seguros.getAttPolizaCarga().setString(msMerchandiseInsurerPolicy);
        seguros.getAttPrimaSeguro().setDouble(SLibUtils.parseDouble(msPremium));
        
        // Remolques:
        
        cfd.ver3.ccp20.DElementRemolques elementRemolques = new cfd.ver3.ccp20.DElementRemolques();
        ArrayList<cfd.ver3.ccp20.DElementRemolque> remolques = new ArrayList<>();
        if (moBolTransportationMode.getDataTrailer1().getPkTrailerId() != 0) {
            cfd.ver3.ccp20.DElementRemolque remolque = new cfd.ver3.ccp20.DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getDataTrailer1().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getDataTrailer1().getPlate());
            remolques.add(remolque);
        }
        if (moBolTransportationMode.getDataTrailer2().getPkTrailerId() != 0) {
            cfd.ver3.ccp20.DElementRemolque remolque = new cfd.ver3.ccp20.DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getDataTrailer2().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getDataTrailer2().getPlate());
            remolques.add(remolque);
        }
        if (remolques.size() > 0) {
            elementRemolques.getEltRemolques().addAll(remolques);
            autotransporte.setEltRemolques(elementRemolques); 
        }
        
        // Figura transporte:
        
        ArrayList<cfd.ver3.ccp20.DElementTiposFigura> figuras = ccp.getEltFiguraTransporte().getEltTiposFigura();
        cfd.ver3.ccp20.DElementTiposFigura chofer = new cfd.ver3.ccp20.DElementTiposFigura();
        chofer.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteChofer);
        chofer.getAttNumLicencia().setString(moBolTransportationMode.getDataDriver().getDriverLicense()); 
        if (!moBolTransportationMode.getDataDriver().getFiscalId().isEmpty()) {
            chofer.getAttRFCFigura().setString(moBolTransportationMode.getDataDriver().getFiscalId());
        }
        else {
            chofer.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataDriver().getFiscalForeginId());
            chofer.getAttNombreFigura().setString(moBolTransportationMode.getDataDriver().getName());
            chofer.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataDriver().getDataCountry().getCountryCode());
        }
        figuras.add(chofer);
        
        // Propietario 1
        
        if (moBolTransportationMode.getDataOwner1().getPkBolPersonId() != 0) {
            cfd.ver3.ccp20.DElementTiposFigura propietario = new cfd.ver3.ccp20.DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransportePropietario);
            if (!moBolTransportationMode.getDataOwner1().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getDataOwner1().getFiscalId());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataOwner1().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner1().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataOwner1().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver3.ccp20.DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            cfd.ver3.ccp20.DElementPartesTransporte parte = new cfd.ver3.ccp20.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner1());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        // Propietario 2
        
        if (moBolTransportationMode.getDataOwner2().getPkBolPersonId() != 0) {
            cfd.ver3.ccp20.DElementTiposFigura propietario = new cfd.ver3.ccp20.DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransportePropietario);
            if (!moBolTransportationMode.getDataOwner2().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getDataOwner2().getFiscalId());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataOwner2().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner2().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataOwner2().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver3.ccp20.DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            cfd.ver3.ccp20.DElementPartesTransporte parte = new cfd.ver3.ccp20.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner2());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        // Arrendador 1
        
        if (moBolTransportationMode.getDataLessor1().getPkBolPersonId() != 0) {
            cfd.ver3.ccp20.DElementTiposFigura arrendador = new cfd.ver3.ccp20.DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteArrendador);
            if (!moBolTransportationMode.getDataLessor1().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getDataLessor1().getFiscalId());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataLessor1().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor1().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataLessor1().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver3.ccp20.DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            cfd.ver3.ccp20.DElementPartesTransporte parte = new cfd.ver3.ccp20.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessor1());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        // Arrendador 2
        
        if (moBolTransportationMode.getDataLessor2().getPkBolPersonId() != 0) {
            cfd.ver3.ccp20.DElementTiposFigura arrendador = new cfd.ver3.ccp20.DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteArrendador);
            if (!moBolTransportationMode.getDataLessor2().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getDataLessor2().getFiscalId());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataLessor2().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor2().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataLessor2().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver3.ccp20.DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            cfd.ver3.ccp20.DElementPartesTransporte parte = new cfd.ver3.ccp20.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessor2());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        if (moBolTransportationMode.getDataNotified().getPkBolPersonId() != 0) {
            cfd.ver3.ccp20.DElementTiposFigura notificado = new cfd.ver3.ccp20.DElementTiposFigura();
            notificado.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteNotificado);
            if (!moBolTransportationMode.getDataNotified().getFiscalId().isEmpty()) {
                notificado.getAttRFCFigura().setString(moBolTransportationMode.getDataNotified().getFiscalId());
            }
            else {
                notificado.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataNotified().getFiscalForeginId());
                notificado.getAttNombreFigura().setString(moBolTransportationMode.getDataNotified().getName());
                notificado.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataNotified().getDataCountry().getCountryCode());
            } 
            figuras.add(notificado);
        }
        
        for (SDbBolTransportationModeExtra tme : moBolTransportationMode.getBolTransportationModeExtra()) {
            cfd.ver3.ccp20.DElementTiposFigura figura = new cfd.ver3.ccp20.DElementTiposFigura();
            figura.getAttTipoFigura().setString(tme.getBolPerson().getDbmsBolPersonTypeCode());
            if (!tme.getBolPerson().getFiscalId().isEmpty()) {
                figura.getAttRFCFigura().setString(tme.getBolPerson().getFiscalId());
            }
            else {
                figura.getAttNumRegIdTribFigura().setString(tme.getBolPerson().getFiscalForeginId());
                figura.getAttNombreFigura().setString(tme.getBolPerson().getName());
                figura.getAttResidenciaFiscalFigura().setString(tme.getBolPerson().getDataCountry().getCountryCode());
            }
            figura.getAttNumLicencia().setString(tme.getBolPerson().getDriverLicense());
            if (!figura.getAttTipoFigura().getString().equals(DCfdi40Catalogs.ClaveFiguraTransporteNotificado) &&
                    !figura.getAttTipoFigura().getString().equals(DCfdi40Catalogs.ClaveFiguraTransporteChofer)) {
                ArrayList<cfd.ver3.ccp20.DElementPartesTransporte> partesTransporte = figura.getEltPartesTransporte();
                cfd.ver3.ccp20.DElementPartesTransporte parte = new cfd.ver3.ccp20.DElementPartesTransporte();
                parte.getAttParteTransporte().setString(tme.getTransportationPart());
                partesTransporte.add(parte);
            }
            figuras.add(figura);
        }
        
        return ccp;
    }
    
    private cfd.ver4.ccp30.DElementCartaPorte complemento30() {
        
        // Encabezado:
        
        cfd.ver4.ccp30.DElementCartaPorte ccp = new cfd.ver4.ccp30.DElementCartaPorte();
        ccp.getAttIdCCP().setString(msBillOfLadingUuid);
        ccp.getAttTransInternac().setString(mbInternationalBol ? DCfdi40Catalogs.TextoSí : DCfdi40Catalogs.TextoNo);
        if (mbInternationalBol) {
            ccp.getAttEntradaSalidaMerc().setString(msInputOutputBol);
            ccp.getAttViaEntradaSalida().setString(msInputOutputWay);
            ccp.getAttPaisOrigenDestino().setString(msXtaCtyCode);
        }
        ccp.getAttTotalDistRec().setDouble(mdTotalDistance);
        
        // Ubicaciones:
        
        ArrayList<cfd.ver4.ccp30.DElementUbicacion> ubicaciones = ccp.getEltUbicaciones().getEltUbicaciones();
        for (SDbBolLocation location : maBolLocations) {
            cfd.ver4.ccp30.DElementUbicacion origen = null;
            cfd.ver4.ccp30.DElementUbicacion destino = null;
            
            if (location.getXtaIsOrigin()) {
                origen = new cfd.ver4.ccp30.DElementUbicacion();
                origen.getAttTipoUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónOrigen);
                origen.getAttIDUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónOrigenPrefijoId + location.getDataBizPartnerBranchAddress().getAddressCode());
                if (location.getDataBizPartner().getFiscalId().isEmpty()) {
                    origen.getAttNumRegIdTrib().setString(location.getDataBizPartner().getFiscalId());
                    origen.getAttNombreRemitenteDestinatario().setString(location.getDataBizPartner().getBizPartner());
                    origen.getAttResidenciaFiscal().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    origen.getAttRFCRemitenteDestinatario().setString(location.getDataBizPartner().getFiscalId());
                }
                origen.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateDeparture_n());
                
                // Domicilio: 
                
                cfd.ver4.ccp30.DElementDomicilio domicilio = origen.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsBolNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getDataBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getDataBizPartnerBranchAddress().getZipCode()); 
                if (location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode() != null) {
                    SDataBolZipCode bolZipCode = location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode();
                    if (bolZipCode.getDbmsBolCounty() != null) {
                        domicilio.getAttMunicipio().setString(bolZipCode.getDbmsBolCounty().getPkCountyCode());
                    }
                    if (bolZipCode.getDbmsBolLocality() != null) {
                        domicilio.getAttLocalidad().setString(bolZipCode.getDbmsBolLocality().getPkLocalityCode());
                    }
                }
            }
            
            if (location.getXtaIsDestination()) {
                destino = new cfd.ver4.ccp30.DElementUbicacion();
                destino.getAttTipoUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónDestino);
                destino.getAttIDUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónDestinoPrefijoId + location.getDataBizPartnerBranchAddress().getAddressCode());
                if (location.getDataBizPartner().getFiscalId().isEmpty()) {
                    destino.getAttNumRegIdTrib().setString(location.getDataBizPartner().getFiscalId());
                    destino.getAttNombreRemitenteDestinatario().setString(location.getDataBizPartner().getBizPartner());
                    destino.getAttResidenciaFiscal().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    destino.getAttRFCRemitenteDestinatario().setString(location.getDataBizPartner().getFiscalId());
                }
                destino.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateArrival_n());
                destino.getAttDistanciaRecorrida().setDouble(location.getDistance());
                
                // Domicilio: 
                
                cfd.ver4.ccp30.DElementDomicilio domicilio = destino.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsBolNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getDataBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getDataBizPartnerBranchAddress().getZipCode()); 
                if (location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode() != null) {
                    SDataBolZipCode zipCode = location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode();
                    if (zipCode.getDbmsBolCounty() != null) {
                        domicilio.getAttMunicipio().setString(zipCode.getDbmsBolCounty().getPkCountyCode());
                    }
                    if (zipCode.getDbmsBolLocality() != null) {
                        domicilio.getAttLocalidad().setString(zipCode.getDbmsBolLocality().getPkLocalityCode());
                    }
                }
            }
            
            if (origen != null) {
                ubicaciones.add(origen);
            }
            
            if (destino != null) {
                ubicaciones.add(destino);
            }
        }
        
        // Encabezado Mercancias:
        
        cfd.ver4.ccp30.DElementMercancias encabezadoMercancias = ccp.getEltMercancias();
        encabezadoMercancias.getAttPesoBrutoTotal().setDouble(mdGrossWeight);
        encabezadoMercancias.getAttUnidadPeso().setString(msXtaGrossWeightUnitCode);
        encabezadoMercancias.getAttNumTotalMercancias().setInteger(maBolMerchandises.size());
        if (mbReverseLogistics) {
            encabezadoMercancias.getAttLogisticaInversaRecoleccionDevolucion().setString(DCfdi40Catalogs.TextoSí);
        }
        
        // Mercancias:
        
        ArrayList<cfd.ver4.ccp30.DElementMercancia> mercancias = encabezadoMercancias.getEltMercancias();
        for (SDbBolMerchandise merch : maBolMerchandises) {
            cfd.ver4.ccp30.DElementMercancia mercancia = new cfd.ver4.ccp30.DElementMercancia();
            mercancia.getAttBienesTransp().setString(merch.getXtaItemClaveProdServ());
            mercancia.getAttDescripcion().setString(merch.getDataItem().getItem());
            mercancia.getAttCantidad().setDouble(merch.getQuantity());
            mercancia.getAttClaveUnidad().setString(merch.getXtaClaveUnidad());
            mercancia.getAttPesoEnKg().setDouble(merch.getXtaClaveUnidad().equals("KGM") ? merch.getQuantity() : merch.getWeight());
            mercancia.getAttFraccionArancelaria().setString(merch.getExternalUUID());
            
            // Cantidad transporta:
            
            ArrayList<SDbBolMerchandiseQuantity> qtyOr = new ArrayList<>();
            ArrayList<SDbBolMerchandiseQuantity> qtyDe = new ArrayList<>();
            for (SDbBolMerchandiseQuantity qty : merch.getChildBolMerchandiseQuantities()) {
                if (qty.getDataOriginBizPartnerBranchAddress() != null) {
                    qtyOr.add(qty);
                }
                if (qty.getDataDestinationBizPartnerBranchAddress() != null) {
                    qtyDe.add(qty);
                }
            }
            
            ArrayList<cfd.ver4.ccp30.DElementCantidadTransporta> transporta = mercancia.getEltCantidadTransportas();
            
            for (SDbBolMerchandiseQuantity or : qtyOr) {
                if (or.getQuantity() > 0) {
                    for (SDbBolMerchandiseQuantity de : qtyDe) {
                        if (de.getQuantity() > 0) {
                            cfd.ver4.ccp30.DElementCantidadTransporta elementTransp = new cfd.ver4.ccp30.DElementCantidadTransporta();
                            elementTransp.getAttIDOrigen().setString(DCfdi40Catalogs.CcpUbicaciónOrigenPrefijoId + or.getDataOriginBizPartnerBranchAddress().getAddressCode());
                            elementTransp.getAttIDDestino().setString(DCfdi40Catalogs.CcpUbicaciónDestinoPrefijoId + de.getDataDestinationBizPartnerBranchAddress().getAddressCode());
                            if (or.getQuantity() > de.getQuantity()) {
                                or.setQuantity(or.getQuantity() - de.getQuantity());                                
                                elementTransp.getAttCantidad().setDouble(de.getQuantity());
                                de.setQuantity(0);                                
                            }
                            else if (or.getQuantity() < de.getQuantity()) {
                                de.setQuantity(de.getQuantity() - or.getQuantity());
                                elementTransp.getAttCantidad().setDouble(or.getQuantity());
                                or.setQuantity(0);
                            }
                            else if (or.getQuantity() == de.getQuantity()) {
                                elementTransp.getAttCantidad().setDouble(or.getQuantity());
                                or.setQuantity(0);
                                de.setQuantity(0);
                            }
                            transporta.add(elementTransp);
                        }
                    }
                }
            }
            
            // Documentación aduanera:
            
            //ArrayList<DElementDocumentacionAduanera> docAdu = mercancia.getEltDocumentacionAduanera();
            
            mercancias.add(mercancia);
        }
        
        // Autotransporte:
        
        cfd.ver4.ccp30.DElementAutotransporte autotransporte = encabezadoMercancias.getEltAutotransporte();
        autotransporte.getAttPermSCT().setString(moBolTransportationMode.getDataVehicle().getPermissonSctType());
        autotransporte.getAttNumPermisoSCT().setString(moBolTransportationMode.getDataVehicle().getPermissonSctNumber());
        
        // Identificación vehicular:
        
        cfd.ver4.ccp30.DElementIdentificacionVehicular idVehicular = autotransporte.getEltIdentificacionVehicular();
        idVehicular.getAttConfigVehicular().setString(moBolTransportationMode.getDataVehicle().getVehicleConfiguration());
        idVehicular.getAttPlacaVM().setString(moBolTransportationMode.getDataVehicle().getPlate());
        idVehicular.getAttAnioModeloVM().setInteger(moBolTransportationMode.getDataVehicle().getVehicleYear());
        idVehicular.getAttPesoBrutoVehicular().setDouble(moBolTransportationMode.getDataVehicle().getGrossWeight());
        
        // Seguros: 
        
        cfd.ver4.ccp30.DElementSeguros seguros = autotransporte.getEltSeguros();
        seguros.getAttAseguraRespCivil().setString(moBolTransportationMode.getDataVehicle().getXtaInsurerName());
        seguros.getAttPolizaRespCivil().setString(moBolTransportationMode.getDataVehicle().getInsurancePolicy());
        seguros.getAttAseguraMedAmbiente().setString(moDataEnvironmentalInsurer.getName());
        seguros.getAttPolizaMedAmbiente().setString(msEnvironmentalInsurerPolicy);
        seguros.getAttAseguraCarga().setString(moDataMerchandiseInsurer.getName());
        seguros.getAttPolizaCarga().setString(msMerchandiseInsurerPolicy);
        seguros.getAttPrimaSeguro().setDouble(SLibUtils.parseDouble(msPremium));
        
        // Remolques:
        
        cfd.ver4.ccp30.DElementRemolques elementRemolques = new cfd.ver4.ccp30.DElementRemolques();
        ArrayList<cfd.ver4.ccp30.DElementRemolque> remolques = new ArrayList<>();
        if (moBolTransportationMode.getDataTrailer1().getPkTrailerId() != 0) {
            cfd.ver4.ccp30.DElementRemolque remolque = new cfd.ver4.ccp30.DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getDataTrailer1().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getDataTrailer1().getPlate());
            remolques.add(remolque);
        }
        if (moBolTransportationMode.getDataTrailer2().getPkTrailerId() != 0) {
            cfd.ver4.ccp30.DElementRemolque remolque = new cfd.ver4.ccp30.DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getDataTrailer2().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getDataTrailer2().getPlate());
            remolques.add(remolque);
        }
        if (remolques.size() > 0) {
            elementRemolques.getEltRemolques().addAll(remolques);
            autotransporte.setEltRemolques(elementRemolques); 
        }
        
        // Figura transporte:
        
        ArrayList<cfd.ver4.ccp30.DElementTiposFigura> figuras = ccp.getEltFiguraTransporte().getEltTiposFigura();
        cfd.ver4.ccp30.DElementTiposFigura chofer = new cfd.ver4.ccp30.DElementTiposFigura();
        chofer.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteChofer);
        chofer.getAttNumLicencia().setString(moBolTransportationMode.getDataDriver().getDriverLicense()); 
        if (!moBolTransportationMode.getDataDriver().getFiscalId().isEmpty()) {
            chofer.getAttRFCFigura().setString(moBolTransportationMode.getDataDriver().getFiscalId());
            chofer.getAttNombreFigura().setString(moBolTransportationMode.getDataDriver().getName());
        }
        else {
            chofer.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataDriver().getFiscalForeginId());
            chofer.getAttNombreFigura().setString(moBolTransportationMode.getDataDriver().getName());
            chofer.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataDriver().getDataCountry().getCountryCode());
        }
        figuras.add(chofer);
        
        // Propietario 1
        
        if (moBolTransportationMode.getDataOwner1().getPkBolPersonId() != 0) {
            cfd.ver4.ccp30.DElementTiposFigura propietario = new cfd.ver4.ccp30.DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransportePropietario);
            if (!moBolTransportationMode.getDataOwner1().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getDataOwner1().getFiscalId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner1().getName());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataOwner1().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner1().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataOwner1().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp30.DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            cfd.ver4.ccp30.DElementPartesTransporte parte = new cfd.ver4.ccp30.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner1());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        // Propietario 2
        
        if (moBolTransportationMode.getDataOwner2().getPkBolPersonId() != 0) {
            cfd.ver4.ccp30.DElementTiposFigura propietario = new cfd.ver4.ccp30.DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransportePropietario);
            if (!moBolTransportationMode.getDataOwner2().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getDataOwner2().getFiscalId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner2().getName());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataOwner2().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner2().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataOwner2().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp30.DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            cfd.ver4.ccp30.DElementPartesTransporte parte = new cfd.ver4.ccp30.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner2());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        // Arrendador 1
        
        if (moBolTransportationMode.getDataLessor1().getPkBolPersonId() != 0) {
            cfd.ver4.ccp30.DElementTiposFigura arrendador = new cfd.ver4.ccp30.DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteArrendador);
            if (!moBolTransportationMode.getDataLessor1().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getDataLessor1().getFiscalId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor1().getName());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataLessor1().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor1().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataLessor1().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp30.DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            cfd.ver4.ccp30.DElementPartesTransporte parte = new cfd.ver4.ccp30.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessor1());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        // Arrendador 2
        
        if (moBolTransportationMode.getDataLessor2().getPkBolPersonId() != 0) {
            cfd.ver4.ccp30.DElementTiposFigura arrendador = new cfd.ver4.ccp30.DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteArrendador);
            if (!moBolTransportationMode.getDataLessor2().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getDataLessor2().getFiscalId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor2().getName());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataLessor2().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor2().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataLessor2().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp30.DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            cfd.ver4.ccp30.DElementPartesTransporte parte = new cfd.ver4.ccp30.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessor2());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        if (moBolTransportationMode.getDataNotified().getPkBolPersonId() != 0) {
            cfd.ver4.ccp30.DElementTiposFigura notificado = new cfd.ver4.ccp30.DElementTiposFigura();
            notificado.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteNotificado);
            if (!moBolTransportationMode.getDataNotified().getFiscalId().isEmpty()) {
                notificado.getAttRFCFigura().setString(moBolTransportationMode.getDataNotified().getFiscalId());
                notificado.getAttNombreFigura().setString(moBolTransportationMode.getDataNotified().getName());
            }
            else {
                notificado.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataNotified().getFiscalForeginId());
                notificado.getAttNombreFigura().setString(moBolTransportationMode.getDataNotified().getName());
                notificado.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataNotified().getDataCountry().getCountryCode());
            } 
            figuras.add(notificado);
        }
        
        for (SDbBolTransportationModeExtra tme : moBolTransportationMode.getBolTransportationModeExtra()) {
            cfd.ver4.ccp30.DElementTiposFigura figura = new cfd.ver4.ccp30.DElementTiposFigura();
            figura.getAttTipoFigura().setString(tme.getBolPerson().getDbmsBolPersonTypeCode());
            if (!tme.getBolPerson().getFiscalId().isEmpty()) {
                figura.getAttRFCFigura().setString(tme.getBolPerson().getFiscalId());
                figura.getAttNombreFigura().setString(tme.getBolPerson().getName());
            }
            else {
                figura.getAttNumRegIdTribFigura().setString(tme.getBolPerson().getFiscalForeginId());
                figura.getAttNombreFigura().setString(tme.getBolPerson().getName());
                figura.getAttResidenciaFiscalFigura().setString(tme.getBolPerson().getDataCountry().getCountryCode());
            }
            figura.getAttNumLicencia().setString(tme.getBolPerson().getDriverLicense());
            if (!figura.getAttTipoFigura().getString().equals(DCfdi40Catalogs.ClaveFiguraTransporteNotificado) &&
                    !figura.getAttTipoFigura().getString().equals(DCfdi40Catalogs.ClaveFiguraTransporteChofer)) {
                ArrayList<cfd.ver4.ccp30.DElementPartesTransporte> partesTransporte = figura.getEltPartesTransporte();
                cfd.ver4.ccp30.DElementPartesTransporte parte = new cfd.ver4.ccp30.DElementPartesTransporte();
                parte.getAttParteTransporte().setString(tme.getTransportationPart());
                partesTransporte.add(parte);
            }
            figuras.add(figura);
        }
        
        return ccp;
    }
    
    private cfd.ver4.ccp31.DElementCartaPorte complemento31() {
        
        // Encabezado:
        
        cfd.ver4.ccp31.DElementCartaPorte ccp = new cfd.ver4.ccp31.DElementCartaPorte();
        ccp.getAttIdCCP().setString(msBillOfLadingUuid);
        ccp.getAttTransInternac().setString(mbInternationalBol ? DCfdi40Catalogs.TextoSí : DCfdi40Catalogs.TextoNo);
        if (mbInternationalBol) {
            ccp.getAttEntradaSalidaMerc().setString(msInputOutputBol);
            ccp.getAttViaEntradaSalida().setString(msInputOutputWay);
            ccp.getAttPaisOrigenDestino().setString(msXtaCtyCode);
        }
        ccp.getAttTotalDistRec().setDouble(mdTotalDistance);
        
        // Ubicaciones:
        
        ArrayList<cfd.ver4.ccp31.DElementUbicacion> ubicaciones = ccp.getEltUbicaciones().getEltUbicaciones();
        for (SDbBolLocation location : maBolLocations) {
            cfd.ver4.ccp31.DElementUbicacion origen = null;
            cfd.ver4.ccp31.DElementUbicacion destino = null;
            
            if (location.getXtaIsOrigin()) {
                origen = new cfd.ver4.ccp31.DElementUbicacion();
                origen.getAttTipoUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónOrigen);
                origen.getAttIDUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónOrigenPrefijoId + location.getDataBizPartnerBranchAddress().getAddressCode());
                if (location.getDataBizPartner().getFiscalId().isEmpty()) {
                    origen.getAttNumRegIdTrib().setString(location.getDataBizPartner().getFiscalId());
                    origen.getAttNombreRemitenteDestinatario().setString(location.getDataBizPartner().getBizPartner());
                    origen.getAttResidenciaFiscal().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    origen.getAttRFCRemitenteDestinatario().setString(location.getDataBizPartner().getFiscalId());
                }
                origen.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateDeparture_n());
                
                // Domicilio: 
                
                cfd.ver4.ccp31.DElementDomicilio domicilio = origen.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsBolNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getDataBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getDataBizPartnerBranchAddress().getZipCode()); 
                if (location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode() != null) {
                    SDataBolZipCode bolZipCode = location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode();
                    if (bolZipCode.getDbmsBolCounty() != null) {
                        domicilio.getAttMunicipio().setString(bolZipCode.getDbmsBolCounty().getPkCountyCode());
                    }
                    if (bolZipCode.getDbmsBolLocality() != null) {
                        domicilio.getAttLocalidad().setString(bolZipCode.getDbmsBolLocality().getPkLocalityCode());
                    }
                }
            }
            
            if (location.getXtaIsDestination()) {
                destino = new cfd.ver4.ccp31.DElementUbicacion();
                destino.getAttTipoUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónDestino);
                destino.getAttIDUbicacion().setString(DCfdi40Catalogs.CcpUbicaciónDestinoPrefijoId + location.getDataBizPartnerBranchAddress().getAddressCode());
                if (location.getDataBizPartner().getFiscalId().isEmpty()) {
                    destino.getAttNumRegIdTrib().setString(location.getDataBizPartner().getFiscalId());
                    destino.getAttNombreRemitenteDestinatario().setString(location.getDataBizPartner().getBizPartner());
                    destino.getAttResidenciaFiscal().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    destino.getAttRFCRemitenteDestinatario().setString(location.getDataBizPartner().getFiscalId());
                }
                destino.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateArrival_n());
                destino.getAttDistanciaRecorrida().setDouble(location.getDistance());
                
                // Domicilio: 
                
                cfd.ver4.ccp31.DElementDomicilio domicilio = destino.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsBolNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getDataBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getDataBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getDataBizPartnerBranchAddress().getZipCode()); 
                if (location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode() != null) {
                    SDataBolZipCode zipCode = location.getDataBizPartnerBranchAddress().getDbmsDataBolZipCode();
                    if (zipCode.getDbmsBolCounty() != null) {
                        domicilio.getAttMunicipio().setString(zipCode.getDbmsBolCounty().getPkCountyCode());
                    }
                    if (zipCode.getDbmsBolLocality() != null) {
                        domicilio.getAttLocalidad().setString(zipCode.getDbmsBolLocality().getPkLocalityCode());
                    }
                }
            }
            
            if (origen != null) {
                ubicaciones.add(origen);
            }
            
            if (destino != null) {
                ubicaciones.add(destino);
            }
        }
        
        // Encabezado Mercancias:
        
        cfd.ver4.ccp31.DElementMercancias encabezadoMercancias = ccp.getEltMercancias();
        encabezadoMercancias.getAttPesoBrutoTotal().setDouble(mdGrossWeight);
        encabezadoMercancias.getAttUnidadPeso().setString(msXtaGrossWeightUnitCode);
        encabezadoMercancias.getAttNumTotalMercancias().setInteger(maBolMerchandises.size());
        if (mbReverseLogistics) {
            encabezadoMercancias.getAttLogisticaInversaRecoleccionDevolucion().setString(DCfdi40Catalogs.TextoSí);
        }
        
        // Mercancias:
        
        ArrayList<cfd.ver4.ccp31.DElementMercancia> mercancias = encabezadoMercancias.getEltMercancias();
        for (SDbBolMerchandise merch : maBolMerchandises) {
            cfd.ver4.ccp31.DElementMercancia mercancia = new cfd.ver4.ccp31.DElementMercancia();
            mercancia.getAttBienesTransp().setString(merch.getXtaItemClaveProdServ());
            mercancia.getAttDescripcion().setString(merch.getDataItem().getItem());
            mercancia.getAttCantidad().setDouble(merch.getQuantity());
            mercancia.getAttClaveUnidad().setString(merch.getXtaClaveUnidad());
            mercancia.getAttPesoEnKg().setDouble(merch.getXtaClaveUnidad().equals("KGM") ? merch.getQuantity() : merch.getWeight());
            mercancia.getAttFraccionArancelaria().setString(merch.getExternalUUID());
            
            // Cantidad transporta:
            
            ArrayList<SDbBolMerchandiseQuantity> qtyOr = new ArrayList<>();
            ArrayList<SDbBolMerchandiseQuantity> qtyDe = new ArrayList<>();
            for (SDbBolMerchandiseQuantity qty : merch.getChildBolMerchandiseQuantities()) {
                if (qty.getDataOriginBizPartnerBranchAddress() != null) {
                    qtyOr.add(qty);
                }
                if (qty.getDataDestinationBizPartnerBranchAddress() != null) {
                    qtyDe.add(qty);
                }
            }
            
            ArrayList<cfd.ver4.ccp31.DElementCantidadTransporta> transporta = mercancia.getEltCantidadTransportas();
            
            for (SDbBolMerchandiseQuantity or : qtyOr) {
                if (or.getQuantity() > 0) {
                    for (SDbBolMerchandiseQuantity de : qtyDe) {
                        if (de.getQuantity() > 0) {
                            cfd.ver4.ccp31.DElementCantidadTransporta elementTransp = new cfd.ver4.ccp31.DElementCantidadTransporta();
                            elementTransp.getAttIDOrigen().setString(DCfdi40Catalogs.CcpUbicaciónOrigenPrefijoId + or.getDataOriginBizPartnerBranchAddress().getAddressCode());
                            elementTransp.getAttIDDestino().setString(DCfdi40Catalogs.CcpUbicaciónDestinoPrefijoId + de.getDataDestinationBizPartnerBranchAddress().getAddressCode());
                            if (or.getQuantity() > de.getQuantity()) {
                                or.setQuantity(or.getQuantity() - de.getQuantity());                                
                                elementTransp.getAttCantidad().setDouble(de.getQuantity());
                                de.setQuantity(0);                                
                            }
                            else if (or.getQuantity() < de.getQuantity()) {
                                de.setQuantity(de.getQuantity() - or.getQuantity());
                                elementTransp.getAttCantidad().setDouble(or.getQuantity());
                                or.setQuantity(0);
                            }
                            else if (or.getQuantity() == de.getQuantity()) {
                                elementTransp.getAttCantidad().setDouble(or.getQuantity());
                                or.setQuantity(0);
                                de.setQuantity(0);
                            }
                            transporta.add(elementTransp);
                        }
                    }
                }
            }
            
            // Documentación aduanera:
            
            //ArrayList<DElementDocumentacionAduanera> docAdu = mercancia.getEltDocumentacionAduanera();
            
            mercancias.add(mercancia);
        }
        
        // Autotransporte:
        
        cfd.ver4.ccp31.DElementAutotransporte autotransporte = encabezadoMercancias.getEltAutotransporte();
        autotransporte.getAttPermSCT().setString(moBolTransportationMode.getDataVehicle().getPermissonSctType());
        autotransporte.getAttNumPermisoSCT().setString(moBolTransportationMode.getDataVehicle().getPermissonSctNumber());
        
        // Identificación vehicular:
        
        cfd.ver4.ccp31.DElementIdentificacionVehicular idVehicular = autotransporte.getEltIdentificacionVehicular();
        idVehicular.getAttConfigVehicular().setString(moBolTransportationMode.getDataVehicle().getVehicleConfiguration());
        idVehicular.getAttPlacaVM().setString(moBolTransportationMode.getDataVehicle().getPlate());
        idVehicular.getAttAnioModeloVM().setInteger(moBolTransportationMode.getDataVehicle().getVehicleYear());
        idVehicular.getAttPesoBrutoVehicular().setDouble(moBolTransportationMode.getDataVehicle().getGrossWeight());
        
        // Seguros: 
        
        cfd.ver4.ccp31.DElementSeguros seguros = autotransporte.getEltSeguros();
        seguros.getAttAseguraRespCivil().setString(moBolTransportationMode.getDataVehicle().getXtaInsurerName());
        seguros.getAttPolizaRespCivil().setString(moBolTransportationMode.getDataVehicle().getInsurancePolicy());
        seguros.getAttAseguraMedAmbiente().setString(moDataEnvironmentalInsurer.getName());
        seguros.getAttPolizaMedAmbiente().setString(msEnvironmentalInsurerPolicy);
        seguros.getAttAseguraCarga().setString(moDataMerchandiseInsurer.getName());
        seguros.getAttPolizaCarga().setString(msMerchandiseInsurerPolicy);
        seguros.getAttPrimaSeguro().setDouble(SLibUtils.parseDouble(msPremium));
        
        // Remolques:
        
        cfd.ver4.ccp31.DElementRemolques elementRemolques = new cfd.ver4.ccp31.DElementRemolques();
        ArrayList<cfd.ver4.ccp31.DElementRemolque> remolques = new ArrayList<>();
        if (moBolTransportationMode.getDataTrailer1().getPkTrailerId() != 0) {
            cfd.ver4.ccp31.DElementRemolque remolque = new cfd.ver4.ccp31.DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getDataTrailer1().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getDataTrailer1().getPlate());
            remolques.add(remolque);
        }
        if (moBolTransportationMode.getDataTrailer2().getPkTrailerId() != 0) {
            cfd.ver4.ccp31.DElementRemolque remolque = new cfd.ver4.ccp31.DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getDataTrailer2().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getDataTrailer2().getPlate());
            remolques.add(remolque);
        }
        if (remolques.size() > 0) {
            elementRemolques.getEltRemolques().addAll(remolques);
            autotransporte.setEltRemolques(elementRemolques); 
        }
        
        // Figura transporte:
        
        ArrayList<cfd.ver4.ccp31.DElementTiposFigura> figuras = ccp.getEltFiguraTransporte().getEltTiposFigura();
        cfd.ver4.ccp31.DElementTiposFigura chofer = new cfd.ver4.ccp31.DElementTiposFigura();
        chofer.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteChofer);
        chofer.getAttNumLicencia().setString(moBolTransportationMode.getDataDriver().getDriverLicense()); 
        if (!moBolTransportationMode.getDataDriver().getFiscalId().isEmpty()) {
            chofer.getAttRFCFigura().setString(moBolTransportationMode.getDataDriver().getFiscalId());
            chofer.getAttNombreFigura().setString(moBolTransportationMode.getDataDriver().getName());
        }
        else {
            chofer.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataDriver().getFiscalForeginId());
            chofer.getAttNombreFigura().setString(moBolTransportationMode.getDataDriver().getName());
            chofer.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataDriver().getDataCountry().getCountryCode());
        }
        figuras.add(chofer);
        
        // Propietario 1
        
        if (moBolTransportationMode.getDataOwner1().getPkBolPersonId() != 0) {
            cfd.ver4.ccp31.DElementTiposFigura propietario = new cfd.ver4.ccp31.DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransportePropietario);
            if (!moBolTransportationMode.getDataOwner1().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getDataOwner1().getFiscalId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner1().getName());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataOwner1().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner1().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataOwner1().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp31.DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            cfd.ver4.ccp31.DElementPartesTransporte parte = new cfd.ver4.ccp31.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner1());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        // Propietario 2
        
        if (moBolTransportationMode.getDataOwner2().getPkBolPersonId() != 0) {
            cfd.ver4.ccp31.DElementTiposFigura propietario = new cfd.ver4.ccp31.DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransportePropietario);
            if (!moBolTransportationMode.getDataOwner2().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getDataOwner2().getFiscalId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner2().getName());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataOwner2().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getDataOwner2().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataOwner2().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp31.DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            cfd.ver4.ccp31.DElementPartesTransporte parte = new cfd.ver4.ccp31.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner2());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        // Arrendador 1
        
        if (moBolTransportationMode.getDataLessor1().getPkBolPersonId() != 0) {
            cfd.ver4.ccp31.DElementTiposFigura arrendador = new cfd.ver4.ccp31.DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteArrendador);
            if (!moBolTransportationMode.getDataLessor1().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getDataLessor1().getFiscalId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor1().getName());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataLessor1().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor1().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataLessor1().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp31.DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            cfd.ver4.ccp31.DElementPartesTransporte parte = new cfd.ver4.ccp31.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessor1());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        // Arrendador 2
        
        if (moBolTransportationMode.getDataLessor2().getPkBolPersonId() != 0) {
            cfd.ver4.ccp31.DElementTiposFigura arrendador = new cfd.ver4.ccp31.DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteArrendador);
            if (!moBolTransportationMode.getDataLessor2().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getDataLessor2().getFiscalId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor2().getName());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataLessor2().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getDataLessor2().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataLessor2().getDataCountry().getCountryCode());
            }
            ArrayList<cfd.ver4.ccp31.DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            cfd.ver4.ccp31.DElementPartesTransporte parte = new cfd.ver4.ccp31.DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessor2());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        if (moBolTransportationMode.getDataNotified().getPkBolPersonId() != 0) {
            cfd.ver4.ccp31.DElementTiposFigura notificado = new cfd.ver4.ccp31.DElementTiposFigura();
            notificado.getAttTipoFigura().setString(DCfdi40Catalogs.ClaveFiguraTransporteNotificado);
            if (!moBolTransportationMode.getDataNotified().getFiscalId().isEmpty()) {
                notificado.getAttRFCFigura().setString(moBolTransportationMode.getDataNotified().getFiscalId());
                notificado.getAttNombreFigura().setString(moBolTransportationMode.getDataNotified().getName());
            }
            else {
                notificado.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getDataNotified().getFiscalForeginId());
                notificado.getAttNombreFigura().setString(moBolTransportationMode.getDataNotified().getName());
                notificado.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getDataNotified().getDataCountry().getCountryCode());
            } 
            figuras.add(notificado);
        }
        
        for (SDbBolTransportationModeExtra tme : moBolTransportationMode.getBolTransportationModeExtra()) {
            cfd.ver4.ccp31.DElementTiposFigura figura = new cfd.ver4.ccp31.DElementTiposFigura();
            figura.getAttTipoFigura().setString(tme.getBolPerson().getDbmsBolPersonTypeCode());
            if (!tme.getBolPerson().getFiscalId().isEmpty()) {
                figura.getAttRFCFigura().setString(tme.getBolPerson().getFiscalId());
                figura.getAttNombreFigura().setString(tme.getBolPerson().getName());
            }
            else {
                figura.getAttNumRegIdTribFigura().setString(tme.getBolPerson().getFiscalForeginId());
                figura.getAttNombreFigura().setString(tme.getBolPerson().getName());
                figura.getAttResidenciaFiscalFigura().setString(tme.getBolPerson().getDataCountry().getCountryCode());
            }
            figura.getAttNumLicencia().setString(tme.getBolPerson().getDriverLicense());
            if (!figura.getAttTipoFigura().getString().equals(DCfdi40Catalogs.ClaveFiguraTransporteNotificado) &&
                    !figura.getAttTipoFigura().getString().equals(DCfdi40Catalogs.ClaveFiguraTransporteChofer)) {
                ArrayList<cfd.ver4.ccp31.DElementPartesTransporte> partesTransporte = figura.getEltPartesTransporte();
                cfd.ver4.ccp31.DElementPartesTransporte parte = new cfd.ver4.ccp31.DElementPartesTransporte();
                parte.getAttParteTransporte().setString(tme.getTransportationPart());
                partesTransporte.add(parte);
            }
            figuras.add(figura);
        }
        
        return ccp;
    }

    @Override
    public int getCfdType() { // CFDI 3.3 & 4.0
        return SDataConstantsSys.TRNS_TP_CFD_BOL;
    }

    @Override
    public String getComprobanteVersion() { // CFDI 3.3 & 4.0
        return "" + DCfdConsts.CFDI_VER_40;
    }

    @Override
    public String getComprobanteSerie() { // CFDI 3.3 & 4.0
        return msSeries;
    }

    @Override
    public String getComprobanteFolio() { // CFDI 3.3 & 4.0
        return msNumber;
    }

    @Override
    public Date getComprobanteFecha() { // CFDI 3.3 & 4.0
        return mtDate;
    }

    @Override
    public String getComprobanteFormaPago() { // CFDI 3.3 & 4.0
        return "";
    }

    @Override
    public String getComprobanteCondicionesPago() { // CFDI 3.3 & 4.0
        return ""; 
    }

    @Override
    public double getComprobanteSubtotal() { // CFDI 3.3 & 4.0
        return 0;  
    }

    @Override
    public double getComprobanteDescuento() { // CFDI 3.3 & 4.0
        return 0;   
    }

    @Override
    public String getComprobanteMoneda() { // CFDI 3.3 & 4.0
        return DCfdi40Catalogs.ClaveMonedaXxx;  
    }

    @Override
    public double getComprobanteTipoCambio() { // CFDI 3.3 & 4.0
        return 0;   
    }

    @Override
    public double getComprobanteTotal() { // CFDI 3.3 & 4.0
        return 0;   
    }

    @Override
    public String getComprobanteTipoComprobante() { // CFDI 3.3 & 4.0
        return DCfdi40Catalogs.CFD_TP_T; 
    }
    
    @Override
    public String getComprobanteExportacion() { // CFDI 4.0
        return msAuxCfdExportation;
    }

    @Override
    public String getComprobanteMetodoPago() { // CFDI 3.3 & 4.0
        return ""; 
    }
    
    @Override
    public String getComprobanteLugarExpedicion() { // CFDI 3.3 & 4.0
        return moAuxDbmsDataEmisorSucursal.getDbmsBizPartnerBranchAddressOfficial().getZipCode();
    }

    @Override
    public String getComprobanteConfirmacion() { // CFDI 3.3 & 4.0
        return "";
    }

    @Override
    public String getCfdiRelacionados33TipoRelacion() { // CFDI 3.3
        return msAuxCfdCfdiRelacionado33TipoRelacion;
    }

    @Override
    public ArrayList<String> getCfdiRelacionados33() { // CFDI 3.3
        ArrayList<String> cfdis = null;
        
        if (!msAuxCfdCfdiRelacionado33Uuid.isEmpty()) {
            cfdis = new ArrayList<>();
            cfdis.add(msAuxCfdCfdiRelacionado33Uuid);
        }
        
        return cfdis;
    }
    
    @Override
    public STrnCfdRelatedDocs getCfdiRelacionados() { // CFDI 4.0
        return moAuxCfdRelatedDocs;
    }
    
    @Override
    public DElement getElementInformacionGlobal() { // CFDI 4.0
        return null;
    }
    
    @Override
    public int getEmisorId() { // CFDI 3.3 & 4.0
        return moAuxDbmsDataEmisor.getPkBizPartnerId();
    }

    @Override
    public int getEmisorSucursalId() { // CFDI 3.3 & 4.0
        return mnFkCompanyBranchId;
    }

    @Override
    public String getEmisorRegimenFiscal() { // CFDI 3.3 & 4.0
        return msXtaTaxRegime;
    }

    @Override
    public int getReceptorId() { // CFDI 3.3 & 4.0
        return moAuxDbmsDataEmisor.getPkBizPartnerId();
    }

    @Override
    public int getReceptorSucursalId() { // CFDI 3.3 & 4.0
        return mnFkCompanyBranchId;
    }

    @Override
    public String getReceptorResidenciaFiscal() { // CFDI 3.3 & 4.0
        return "";
    }

    @Override
    public String getReceptorNumRegIdTrib() { // CFDI 3.3 & 4.0
        return "";
    }
    
    @Override
    public String getReceptorUsoCFDI() { // CFDI 3.3 & 4.0
        return DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales; 
    }
    
    @Override
    public String getReceptorRegimenFiscal() { // CFDI 4.0
        return msXtaTaxRegime;
    }

    @Override
    public int getDestinatarioId() { // CFDI 3.3 & 4.0
        return SLibConstants.UNDEFINED; 
    }

    @Override
    public int getDestinatarioSucursalId() { // CFDI 3.3 & 4.0
        return SLibConstants.UNDEFINED; 
    }

    @Override
    public int getDestinatarioDomicilioId() { // CFDI 3.3 & 4.0
        return SLibConstants.UNDEFINED; 
    }

    @Override
    public ArrayList<SCfdDataConcepto> getElementsConcepto() throws Exception { // CFDI 3.3 & 4.0
        ArrayList<SCfdDataConcepto> conceptos = new ArrayList<>();
        for (SDbBolMerchandise merch : maBolMerchandises) {
            SCfdDataConcepto concepto = new SCfdDataConcepto(SDataConstantsSys.TRNS_TP_CFD_BOL);
            concepto.setClaveProdServ(merch.getXtaItemClaveProdServ());
            concepto.setNoIdentificacion("");
            concepto.setCantidad(merch.getQuantity());
            concepto.setClaveUnidad(merch.getXtaClaveUnidad());
            concepto.setUnidad("");
            concepto.setDescripcion(merch.getDataItem().getItem());
            concepto.setValorUnitario(0);
            concepto.setImporte(0);
            concepto.setDescuento(0);
            concepto.setObjetoImpuesto(DCfdi40Catalogs.ClaveObjetoImpNo);

            conceptos.add(concepto);
        }

        return conceptos;
    }

    @Override
    public ArrayList<SCfdDataImpuesto> getElementsImpuestos(float cfdiVersion) { // CFDI 3.3 & 4.0
        return null;    
    }

    @Override
    public DElement getElementComplemento() throws Exception { // CFDI 3.3 & 4.0
        cfd.ver40.DElementComplemento complemento = new cfd.ver40.DElementComplemento();
        
        if (moDataCfd != null) {
            switch (moDataCfd.getComplementVersion()) {
                case SDataConstantsSys.TRNS_CFD_BOL_VER_31:
                    complemento.getElements().add(complemento31());
                    break;
                case SDataConstantsSys.TRNS_CFD_BOL_VER_30:
                    complemento.getElements().add(complemento30());
                    break;
                case SDataConstantsSys.TRNS_CFD_BOL_VER_20:
                default:
                    complemento.getElements().add(complemento20());
                    break;
            }
        }
        return complemento;
    }

    @Override
    public DElement getElementAddenda() { // CFDI 3.3 & 4.0
        return null;
    }
}
