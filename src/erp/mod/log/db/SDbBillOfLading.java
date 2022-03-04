/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import cfd.DCfdConsts;
import cfd.DElement;
import cfd.ver3.ccp20.DElementAutotransporte;
import cfd.ver3.ccp20.DElementCantidadTransporta;
import cfd.ver3.ccp20.DElementCartaPorte;
import cfd.ver3.ccp20.DElementDomicilio;
import cfd.ver3.ccp20.DElementIdentificacionVehicular;
import cfd.ver3.ccp20.DElementMercancia;
import cfd.ver3.ccp20.DElementMercancias;
import cfd.ver3.ccp20.DElementPartesTransporte;
import cfd.ver3.ccp20.DElementRemolque;
import cfd.ver3.ccp20.DElementRemolques;
import cfd.ver3.ccp20.DElementSeguros;
import cfd.ver3.ccp20.DElementTiposFigura;
import cfd.ver3.ccp20.DElementUbicacion;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DElementComplemento;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mloc.data.SDataZipCode;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataCfd;
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
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBillOfLading extends SDbRegistryUser implements erp.cfd.SCfdXmlCfdi33, Serializable {
    
    protected int mnPkBillOfLadingId;
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
//    protected boolean mbDeleted;
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
    
    protected SDataCfd moDbmsDataCfd;
    
    protected String msXtaCtyCode;
    protected String msXtaGrossWeightUnitCode;
    protected String msXtaTaxRegime;
    protected SDbInsurer moXtaEnvironmentalInsurer;
    protected SDbInsurer moXtaMerchandiseInsurer;
    
    protected int mnAuxCfdId;
    protected String msAuxCfdCfdiRelacionadosTipoRelacion;
    protected String msAuxCfdCfdiRelacionadoUuid; // available when CFDI is not stored in SIIE, e.g., third-party
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
                merch.addMerchandiseQuantity(o.getBolMerchandiseQuantity().get(0));
                //merch.addMerchandiseQuantity(o.getBolMerchandiseQuantity().get(o.getBolMerchandiseQuantity().size() - 1));
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
                merch.removeMerchandiseQuantity(o.getBolMerchandiseQuantity().get(0));
                if (merch.maBolMerchandiseQuantity.isEmpty()) {
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
    public void setDbmsDataCfd (SDataCfd o) { moDbmsDataCfd = o;}
    
    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
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
    public SDataCfd getDbmsDataCfd() { return moDbmsDataCfd; }

    public void setBolTransportationMode(SDbBolTransportationMode o) { moBolTransportationMode = o; }
    public void setBolLocations(ArrayList<SDbBolLocation> v) { maBolLocations = v; }
    public void setBolMerchandises(ArrayList<SDbBolMerchandise> v) { maBolMerchandises = v; }
    
    public SDbBolTransportationMode getBolTransportationMode() { return moBolTransportationMode; }
    public ArrayList<SDbBolLocation> getBolLocations() { return maBolLocations; }
    public ArrayList<SDbBolMerchandise> getBolMerchandises() { return maBolMerchandises; }
    
    public void setXtaCtyCode(String s) { msXtaCtyCode = s; }
    public void setXtaGrossWeightUnitCode(String s) { msXtaGrossWeightUnitCode = s; }
    public void setXtaTaxRegime(String s) { msXtaTaxRegime = s; }
    public void setXtaEnvironmentalInsurer(SDbInsurer o) { moXtaEnvironmentalInsurer = o; } 
    public void setXtaMerchandiseInsurer(SDbInsurer o) { moXtaMerchandiseInsurer = o; } 
    
    public String getXtaCtyCode() { return msXtaCtyCode; }
    public String getXtaGrossWeightUnitCode() { return msXtaGrossWeightUnitCode; }
    public String getXtaTaxRegime() { return msXtaTaxRegime; }
    public SDbInsurer getXtaEnvironmentalInsurer() { return moXtaEnvironmentalInsurer; }
    public SDbInsurer getXtaMerchandiseInsurer() { return moXtaMerchandiseInsurer; }
    
    public void setAuxCfdId(int i) { mnAuxCfdId = i; }
    public void setAuxCfdCfdiRelacionadosTipoRelacion(String s) { msAuxCfdCfdiRelacionadosTipoRelacion = s; }
    public void setAuxCfdCfdiRelacionadoUuid(String s) { msAuxCfdCfdiRelacionadoUuid = s; }
    public void setAuxDbmsDataEmisor(SDataBizPartner o) { moAuxDbmsDataEmisor = o; }
    public void setAuxDbmsDataEmisorSucursal(SDataBizPartnerBranch o) { moAuxDbmsDataEmisorSucursal = o; }
    
    public int getAuxCfdId() { return mnAuxCfdId; }
    public String getAuxCfdCfdiRelacionadosTipoRelacion() { return msAuxCfdCfdiRelacionadosTipoRelacion; }
    public String getAuxCfdCfdiRelacionadoUuid() { return msAuxCfdCfdiRelacionadoUuid; }
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
        
        moBolTransportationMode = new SDbBolTransportationMode();
        maBolLocations = new ArrayList<>();
        maBolMerchandises = new ArrayList<>();
        
        moDbmsDataCfd = null;
        
        msAuxCfdCfdiRelacionadosTipoRelacion = "";
        msAuxCfdCfdiRelacionadoUuid = ""; 
        mnAuxCfdId = 0;
        
        msXtaCtyCode = "";
        msXtaGrossWeightUnitCode = "";
        msXtaTaxRegime = "";
        moXtaEnvironmentalInsurer = new SDbInsurer();
        moXtaMerchandiseInsurer = new SDbInsurer();
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
            
            mbRegistryNew = false;
        }
        
        // Read transportation mode:
        
        msSql = "SELECT id_bol, id_transp_mode FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE) + " WHERE id_bol = " + mnPkBillOfLadingId + " ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            moBolTransportationMode.read(session, new int[] { resultSet.getInt("id_bol"), resultSet.getInt("id_transp_mode") } ) ;
        }
        
        // Read locations:
        
        msSql = "SELECT id_bol, id_location FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_LOCATION) + " WHERE id_bol = " + mnPkBillOfLadingId + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbBolLocation location = new SDbBolLocation(this);
            location.read(session, new int[] { resultSet.getInt("id_bol"), resultSet.getInt("id_location") } );
            maBolLocations.add(location);
        }
        
        // Read merchandises:
        
        msSql = "SELECT id_bol, id_merch FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH) + " WHERE id_bol = " + mnPkBillOfLadingId + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbBolMerchandise merch = new SDbBolMerchandise();
            merch.read(session, new int[] { resultSet.getInt("id_bol"), resultSet.getInt("id_merch") } );
            maBolMerchandises.add(merch);
        }
        
        // Read Insurers
        
        if (mnFkEnviromentalInsurer_n != 0) {
            moXtaEnvironmentalInsurer.read(session, new int[] { mnFkEnviromentalInsurer_n });
        }
        if (mnFkMerchandiseInsurer_n != 0) {
            moXtaMerchandiseInsurer.read(session, new int[] { mnFkMerchandiseInsurer_n });
        }
        
        // Read cfd
        
        if (mnAuxCfdId != 0) {
            moDbmsDataCfd = new SDataCfd();
            moDbmsDataCfd.read(new int[] { mnAuxCfdId }, statement);
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
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkBillOfLadingId + ", " + 
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
        
        moBolTransportationMode.setPkBillOfLadingId(mnPkBillOfLadingId);
        moBolTransportationMode.save(session);
        
        // Save locations:
        
        for (SDbBolLocation location : maBolLocations) {
            location.setPkBillOfLadingId(mnPkBillOfLadingId);
            location.save(session);
        }
        
        // Save merchandises
        
        for (SDbBolMerchandise merchandise : maBolMerchandises) {
            merchandise.setPkBillOfLadingId(mnPkBillOfLadingId);
            merchandise.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbBillOfLading registry = new SDbBillOfLading();
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
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

        registry.setBolTransportationMode(this.getBolTransportationMode());
        registry.setBolLocations(this.getBolLocations());
        registry.setBolMerchandises(this.getBolMerchandises());
        
        registry.setDbmsDataCfd(this.getDbmsDataCfd());
        
        registry.setXtaCtyCode(this.getXtaCtyCode());
        registry.setXtaGrossWeightUnitCode(this.getXtaGrossWeightUnitCode());
        registry.setXtaTaxRegime(this.getXtaTaxRegime());
        registry.setXtaEnvironmentalInsurer(this.getXtaEnvironmentalInsurer());
        registry.setXtaMerchandiseInsurer(this.getXtaMerchandiseInsurer());
        
        registry.setAuxCfdId(this.getAuxCfdId());
        registry.setAuxCfdCfdiRelacionadosTipoRelacion(this.getAuxCfdCfdiRelacionadosTipoRelacion());
        registry.setAuxCfdCfdiRelacionadoUuid(this.getAuxCfdCfdiRelacionadoUuid());
        registry.setAuxDbmsDataEmisor(this.getAuxDbmsDataEmisor());
        registry.setAuxDbmsDataEmisorSucursal(this.getAuxDbmsDataEmisorSucursal());

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
        
        else if (pnAction == SDbConsts.ACTION_DELETE && moDbmsDataCfd != null && (moDbmsDataCfd.isStamped())) {
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
            if (moDbmsDataCfd != null) {
                moDbmsDataCfd.annul(connection);
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

    @Override
    public int getCfdType() {
        return SDataConstantsSys.TRNS_TP_CFD_BOL;
    }

    @Override
    public String getComprobanteVersion() {
        return "" + DCfdConsts.CFDI_VER_33;
    }

    @Override
    public String getComprobanteSerie() {
        return msSeries;
    }

    @Override
    public String getComprobanteFolio() {
        return msNumber;
    }

    @Override
    public Date getComprobanteFecha() {
        return mtDate;
    }

    @Override
    public String getComprobanteFormaPago() {
        return "";
    }

    @Override
    public String getComprobanteCondicionesPago() {
        return "";  // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public double getComprobanteSubtotal() {
        return 0;   // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public double getComprobanteDescuento() {
        return 0;   // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public String getComprobanteMoneda() {
        return DCfdi33Catalogs.ClaveMonedaXxx;  // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public double getComprobanteTipoCambio() {
        return 0;   // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public double getComprobanteTotal() {
        return 0;   // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public String getComprobanteTipoComprobante() {
        return DCfdi33Catalogs.CFD_TP_T; // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public String getComprobanteMetodoPago() {
        return ""; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }
    
    @Override
    public String getComprobanteLugarExpedicion() {
        return moAuxDbmsDataEmisorSucursal.getDbmsBizPartnerBranchAddressOfficial().getZipCode();
    }

    @Override
    public String getComprobanteConfirmacion() {
        return "";
    }

    @Override
    public String getCfdiRelacionadosTipoRelacion() {
        return msAuxCfdCfdiRelacionadosTipoRelacion;
    }

    @Override
    public ArrayList<String> getCfdiRelacionados() {
        ArrayList<String> cfdis = null;
        
        if (!msAuxCfdCfdiRelacionadoUuid.isEmpty()) {
            cfdis = new ArrayList<>();
            cfdis.add(msAuxCfdCfdiRelacionadoUuid);
        }
        
        return cfdis;
    }

    @Override
    public int getEmisorId() {
        return moAuxDbmsDataEmisor.getPkBizPartnerId();
    }

    @Override
    public int getEmisorSucursalId() {
        return mnFkCompanyBranchId;
    }

    @Override
    public String getEmisorRegimenFiscal() {
        return msXtaTaxRegime;
    }

    @Override
    public int getReceptorId() {
        return moAuxDbmsDataEmisor.getPkBizPartnerId();
    }

    @Override
    public int getReceptorSucursalId() {
        return mnFkCompanyBranchId;
    }

    @Override
    public String getReceptorResidenciaFiscal() {
        return "";
    }

    @Override
    public String getReceptorNumRegIdTrib() {
        return "";
    }

    @Override
    public String getReceptorUsoCFDI() {
        return DCfdi33Catalogs.CFDI_USO_POR_DEF; // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public int getDestinatarioId() {
        return SLibConstants.UNDEFINED; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public int getDestinatarioSucursalId() {
        return SLibConstants.UNDEFINED; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public int getDestinatarioDomicilioId() {
        return SLibConstants.UNDEFINED; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public ArrayList<SCfdDataConcepto> getElementsConcepto() throws Exception {
        ArrayList<SCfdDataConcepto> conceptos = new ArrayList<>();
        for (SDbBolMerchandise merch : maBolMerchandises) {
            SCfdDataConcepto concepto = new SCfdDataConcepto(SDataConstantsSys.TRNS_TP_CFD_BOL);
            concepto.setClaveProdServ(merch.getXtaItemClaveProdServ());
            concepto.setNoIdentificacion("");
            concepto.setCantidad(merch.getQuantity());
            concepto.setClaveUnidad(merch.getXtaClaveUnidad());
            concepto.setUnidad("");
            concepto.setDescripcion(merch.getXtaItem().getItem());
            concepto.setValorUnitario(0);
            concepto.setImporte(0);
            concepto.setDescuento(0);

            conceptos.add(concepto);
        }

        return conceptos;
    }

    @Override
    public ArrayList<SCfdDataImpuesto> getElementsImpuestos(float cfdiVersion) {
        return null;    // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public DElement getElementComplemento() throws Exception {
        DElementComplemento complemento = new DElementComplemento();
        
        // Encabezado:
        
        DElementCartaPorte ccp = new DElementCartaPorte();
        ccp.getAttTransInternac().setString(mbInternationalBol ? DCfdi33Catalogs.TxtSí : DCfdi33Catalogs.TxtNo);
        if (mbInternationalBol) {
            ccp.getAttEntradaSalidaMerc().setString(msInputOutputBol);
            ccp.getAttViaEntradaSalida().setString(msInputOutputWay);
            ccp.getAttPaisOrigenDestino().setString(msXtaCtyCode);
        }
        ccp.getAttTotalDistRec().setDouble(mdTotalDistance);
        
        // Ubicaciones:
        
        ArrayList<DElementUbicacion> ubicaciones = ccp.getEltUbicaciones().getEltUbicaciones();
        for (SDbBolLocation location : maBolLocations) {
            DElementUbicacion origen = null;
            DElementUbicacion destino = null;
            if (location.getXtaIsOrigin()) {
                origen = new DElementUbicacion();
                origen.getAttTipoUbicacion().setString("Origen");
                origen.getAttIDUbicacion().setString(DCfdi33Catalogs.PrefijoClaveOrigen + location.getXtaBizPartnerBranchAddress().getAddressCode());
                if (location.getXtaBizPartner().getFiscalId().isEmpty()) {
                    origen.getAttNumRegIdTrib().setString(location.getXtaBizPartner().getFiscalId());
                    origen.getAttNombreRemitenteDestinatario().setString(location.getXtaBizPartner().getBizPartner());
                    origen.getAttResidenciaFiscal().setString(location.getXtaBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    origen.getAttRFCRemitenteDestinatario().setString(location.getXtaBizPartner().getFiscalId());
                }
                origen.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateDeparture_n());
                
                // Domicilio: 
                
                DElementDomicilio domicilio = origen.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getXtaBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getXtaBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getXtaBizPartnerBranchAddress().getZipCode()); 
                if (location.getXtaBizPartnerBranchAddress().getDbmsDataZipCode() != null) {
                    SDataZipCode zipCode = location.getXtaBizPartnerBranchAddress().getDbmsDataZipCode();
                    if (zipCode.getDbmsCounty() != null) {
                        domicilio.getAttMunicipio().setString(zipCode.getDbmsCounty().getPkCountyCode());
                    }
                    if (zipCode.getDbmsLocality() != null) {
                        domicilio.getAttLocalidad().setString(zipCode.getDbmsLocality().getPkLocalityCode());
                    }
                }
            }
            if (location.getXtaIsDestination()) {
                destino = new DElementUbicacion();
                destino.getAttTipoUbicacion().setString("Destino");
                destino.getAttIDUbicacion().setString(DCfdi33Catalogs.PrefijoClaveDestino + location.getXtaBizPartnerBranchAddress().getAddressCode());
                if (location.getXtaBizPartner().getFiscalId().isEmpty()) {
                    destino.getAttNumRegIdTrib().setString(location.getXtaBizPartner().getFiscalId());
                    destino.getAttNombreRemitenteDestinatario().setString(location.getXtaBizPartner().getBizPartner());
                    destino.getAttResidenciaFiscal().setString(location.getXtaBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode()); 
                }
                else {
                    destino.getAttRFCRemitenteDestinatario().setString(location.getXtaBizPartner().getFiscalId());
                }
                destino.getAttFechaHoraSalidaLlegada().setDatetime(location.getDateArrival_n());
                destino.getAttDistanciaRecorrida().setDouble(location.getDistance());
                
                // Domicilio: 
                
                DElementDomicilio domicilio = destino.getEltDomicilio();
                if (location.getDbmsBizPartnerBranchNeighborhood() != null) {
                    domicilio.getAttColonia().setString(location.getDbmsBizPartnerBranchNeighborhood().getDbmsNeighborhood().getNeighborhoodCode());
                }
                domicilio.getAttEstado().setString(location.getXtaBizPartnerBranchAddress().getDbmsDataState().getStateCode());
                domicilio.getAttPais().setString(location.getXtaBizPartnerBranchAddress().getDbmsDataCountry().getCountryCode());
                domicilio.getAttCodigoPostal().setString(location.getXtaBizPartnerBranchAddress().getZipCode()); 
                if (location.getXtaBizPartnerBranchAddress().getDbmsDataZipCode() != null) {
                    SDataZipCode zipCode = location.getXtaBizPartnerBranchAddress().getDbmsDataZipCode();
                    if (zipCode.getDbmsCounty() != null) {
                        domicilio.getAttMunicipio().setString(zipCode.getDbmsCounty().getPkCountyCode());
                    }
                    if (zipCode.getDbmsLocality() != null) {
                        domicilio.getAttLocalidad().setString(zipCode.getDbmsLocality().getPkLocalityCode());
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
        
        DElementMercancias encabezadoMercancias = ccp.getEltMercancias();
        encabezadoMercancias.getAttPesoBrutoTotal().setDouble(mdGrossWeight);
        encabezadoMercancias.getAttUnidadPeso().setString(msXtaGrossWeightUnitCode);
        encabezadoMercancias.getAttNumTotalMercancias().setInteger(maBolMerchandises.size());
        
        // Mercancias:
        
        ArrayList<DElementMercancia> mercancias = encabezadoMercancias.getEltMercancias();
        for (SDbBolMerchandise merch : maBolMerchandises) {
            DElementMercancia mercancia = new DElementMercancia();
            mercancia.getAttBienesTransp().setString(merch.getXtaItemClaveProdServ());
            mercancia.getAttDescripcion().setString(merch.getXtaItem().getItem());
            mercancia.getAttCantidad().setDouble(merch.getQuantity());
            mercancia.getAttClaveUnidad().setString(merch.getXtaClaveUnidad());
            mercancia.getAttPesoEnKg().setDouble(merch.getXtaClaveUnidad().equals("KGM") ? merch.getQuantity() : merch.getWeight());
            mercancia.getAttFraccionArancelaria().setString(merch.getExternalUUID());
            
            // Cantidad transporta:
            
            ArrayList<SDbBolMerchandiseQuantity> qtyOr = new ArrayList<>();
            ArrayList<SDbBolMerchandiseQuantity> qtyDe = new ArrayList<>();
            for (SDbBolMerchandiseQuantity qty : merch.getBolMerchandiseQuantity()) {
                if (qty.getXtaOriginBizPartnerBranchAddress() != null) {
                    qtyOr.add(qty);
                }
                if (qty.getXtaDestinationBizPartnerBranchAddress() != null) {
                    qtyDe.add(qty);
                }
            }
            
            ArrayList<DElementCantidadTransporta> transporta = mercancia.getEltCantidadTransporta();
            
            for (SDbBolMerchandiseQuantity or : qtyOr) {
                if (or.getQuantity() > 0) {
                    for (SDbBolMerchandiseQuantity de : qtyDe) {
                        if (de.getQuantity() > 0) {
                            DElementCantidadTransporta elementTransp = new DElementCantidadTransporta();
                            elementTransp.getAttIDOrigen().setString(DCfdi33Catalogs.PrefijoClaveOrigen + or.getXtaOriginBizPartnerBranchAddress().getAddressCode());
                            elementTransp.getAttIDDestino().setString(DCfdi33Catalogs.PrefijoClaveDestino + de.getXtaDestinationBizPartnerBranchAddress().getAddressCode());
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
            
            //ArrayList<DElementPedimentos> pedimento = mercancia.getEltPedimentos();
            
            mercancias.add(mercancia);
        }
        
        // Autotransporte:
        
        DElementAutotransporte autotransporte = encabezadoMercancias.getEltAutotransporte();
        autotransporte.getAttPermSCT().setString(moBolTransportationMode.getXtaVehicle().getPermissonSctType());
        autotransporte.getAttNumPermisoSCT().setString(moBolTransportationMode.getXtaVehicle().getPermissonSctNumber());
        
        // Identificación vehicular:
        
        DElementIdentificacionVehicular idVehicular = autotransporte.getEltIdentificacionVehicular();
        idVehicular.getAttConfigVehicular().setString(moBolTransportationMode.getXtaVehicle().getVehicleConfiguration());
        idVehicular.getAttPlacaVM().setString(moBolTransportationMode.getXtaVehicle().getPlate());
        idVehicular.getAttAnioModeloVM().setInteger(moBolTransportationMode.getXtaVehicle().getVehicleYear());
        
        // Seguros: 
        
        DElementSeguros seguros = autotransporte.getEltSeguros();
        seguros.getAttAseguraRespCivil().setString(moBolTransportationMode.getXtaVehicle().getXtaInsurerName());
        seguros.getAttPolizaRespCivil().setString(moBolTransportationMode.getXtaVehicle().getInsurancePolicy());
        seguros.getAttAseguraMedAmbiente().setString(moXtaEnvironmentalInsurer.getName());
        seguros.getAttPolizaMedAmbiente().setString(msEnvironmentalInsurerPolicy);
        seguros.getAttAseguraCarga().setString(moXtaMerchandiseInsurer.getName());
        seguros.getAttPolizaCarga().setString(msMerchandiseInsurerPolicy);
        seguros.getAttPrimaSeguro().setString(msPremium);
        
        // Remolques:
        
        DElementRemolques elementRemolques = new DElementRemolques();
        ArrayList<DElementRemolque> remolques = new ArrayList<>();
        if (moBolTransportationMode.getXtaTrailer1().getPkTrailerId() != 0) {
            DElementRemolque remolque = new DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getXtaTrailer1().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getXtaTrailer1().getPlate());
            remolques.add(remolque);
        }
        if (moBolTransportationMode.getXtaTrailer2().getPkTrailerId() != 0) {
            DElementRemolque remolque = new DElementRemolque();
            remolque.getAttSubTipoRem().setString(moBolTransportationMode.getXtaTrailer2().getTrailerSubtype());
            remolque.getAttPlaca().setString(moBolTransportationMode.getXtaTrailer2().getPlate());
            remolques.add(remolque);
        }
        if (remolques.size() > 0) {
            elementRemolques.getEltRemolques().addAll(remolques);
            autotransporte.setEltRemolques(elementRemolques); 
        }
        
        
        // Figura transporte:
        
        ArrayList<DElementTiposFigura> figuras = ccp.getEltFiguraTransporte().getEltTiposFigura();
        DElementTiposFigura chofer = new DElementTiposFigura();
        chofer.getAttTipoFigura().setString(DCfdi33Catalogs.ClaveChofer);
        chofer.getAttNumLicencia().setString(moBolTransportationMode.getXtaDriver().getDriverLicense()); 
        if (!moBolTransportationMode.getXtaDriver().getFiscalId().isEmpty()) {
            chofer.getAttRFCFigura().setString(moBolTransportationMode.getXtaDriver().getFiscalId());
        }
        else {
            chofer.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getXtaDriver().getFiscalForeginId());
            chofer.getAttNombreFigura().setString(moBolTransportationMode.getXtaDriver().getName());
            chofer.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getXtaDriver().getXtaCountry().getCountryCode());
        }
        figuras.add(chofer);
        
        if (moBolTransportationMode.getXtaOwner().getPkBolPersonId() != 0) {
            DElementTiposFigura propietario = new DElementTiposFigura();
            propietario.getAttTipoFigura().setString(DCfdi33Catalogs.ClavePropietario);
            if (!moBolTransportationMode.getXtaOwner().getFiscalId().isEmpty()) {
                propietario.getAttRFCFigura().setString(moBolTransportationMode.getXtaOwner().getFiscalId());
            }
            else {
                propietario.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getXtaOwner().getFiscalForeginId());
                propietario.getAttNombreFigura().setString(moBolTransportationMode.getXtaOwner().getName());
                propietario.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getXtaOwner().getXtaCountry().getCountryCode());
            }
            ArrayList<DElementPartesTransporte> partesTransporte = propietario.getEltPartesTransporte(); 
            DElementPartesTransporte parte = new DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartOwner());
            partesTransporte.add(parte);
            figuras.add(propietario);
        }
        
        if (moBolTransportationMode.getXtaLessee().getPkBolPersonId() != 0) {
            DElementTiposFigura arrendador = new DElementTiposFigura();
            arrendador.getAttTipoFigura().setString(DCfdi33Catalogs.ClaveArrendador);
            if (!moBolTransportationMode.getXtaLessee().getFiscalId().isEmpty()) {
                arrendador.getAttRFCFigura().setString(moBolTransportationMode.getXtaLessee().getFiscalId());
            }
            else {
                arrendador.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getXtaLessee().getFiscalForeginId());
                arrendador.getAttNombreFigura().setString(moBolTransportationMode.getXtaLessee().getName());
                arrendador.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getXtaLessee().getXtaCountry().getCountryCode());
            }
            ArrayList<DElementPartesTransporte> partesTransporte = arrendador.getEltPartesTransporte();
            DElementPartesTransporte parte = new DElementPartesTransporte();
            parte.getAttParteTransporte().setString(moBolTransportationMode.getTransportationPartLessee());
            partesTransporte.add(parte);
            figuras.add(arrendador);
        }
        
        if (moBolTransportationMode.getXtaNotified().getPkBolPersonId() != 0) {
            DElementTiposFigura notificado = new DElementTiposFigura();
            notificado.getAttTipoFigura().setString(DCfdi33Catalogs.ClaveNotificado);
            if (!moBolTransportationMode.getXtaNotified().getFiscalId().isEmpty()) {
                notificado.getAttRFCFigura().setString(moBolTransportationMode.getXtaNotified().getFiscalId());
            }
            else {
                notificado.getAttNumRegIdTribFigura().setString(moBolTransportationMode.getXtaNotified().getFiscalForeginId());
                notificado.getAttNombreFigura().setString(moBolTransportationMode.getXtaNotified().getName());
                notificado.getAttResidenciaFiscalFigura().setString(moBolTransportationMode.getXtaNotified().getXtaCountry().getCountryCode());
            } 
            figuras.add(notificado);
        }
        
        complemento.getElements().add(ccp);
        return complemento;
    }

    @Override
    public DElement getElementAddenda() {
        return null;
    }
}
