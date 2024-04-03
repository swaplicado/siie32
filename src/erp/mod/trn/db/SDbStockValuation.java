/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockValuation extends SDbRegistryUser {
    
    protected int mnPkStockValuationId;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    //protected boolean mbDeleted;
    //protected int mnFkUserInsertId;
    //protected int mnFkUserUpdateId;
    //protected Date mtTsUserInsert;
    //protected Date mtTsUserUpdate;
    
    protected Object[] moAuxRecordPk;
    protected boolean mbAuxIsAllDelete;
    protected boolean mbAuxIsAllInsert;

    public SDbStockValuation() {
        super(SModConsts.TRN_STK_VAL);
    }
    
    /**
     * The function computes the start date based on the end date and some conditions.
     * 
     * @param session The session parameter is an object of type SGuiSession, which is used to
     * establish a connection to the database and execute SQL queries.
     */
    private void computeStartDate(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mtDateStart = null;

        msSql = "SELECT DATE_ADD(dt_end, INTERVAL 1 DAY) "
                + "FROM " + getSqlTable() + " "
                + "WHERE NOT b_del "
                + "AND YEAR(dt_end) = " + (new SimpleDateFormat("yyyy")).format(mtDateEnd) + " "
                + "ORDER BY dt_end DESC";
        
        resultSet = session.getStatement().getConnection().createStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mtDateStart = resultSet.getDate(1);
        }
        else {
            Calendar cal = Calendar.getInstance();

            // Establecer la fecha en el objeto Calendar
            cal.setTime(mtDateEnd);

            // Establecer el mes y el día del primer día del año
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            // Obtener el primer día del año como objeto Date
            mtDateStart = cal.getTime();
        }
    }
    
    /**
     * The function checks if there are any non-deleted stock valuations after a given end date.
     * 
     * @param session The session parameter is an instance of the SGuiSession class, which represents
     * the user's session in the application. It is used to access the database connection and execute
     * SQL queries.
     * @param dtEnd The dtEnd parameter is a Date object that represents the end date for the
     * valuation.
     * @return The method is returning a boolean value.
     */
    private boolean canDeleteValuation(SGuiSession session, final Date dtEnd) throws SQLException {
        String sql = "SELECT id_stk_val FROM " + 
                SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " " +
                "WHERE NOT b_del "
                + "AND dt_sta > '" + SLibUtils.DbmsDateFormatDate.format(dtEnd) + "' "
                + "AND year(dt_sta) = year('" + SLibUtils.DbmsDateFormatDate.format(dtEnd) + "');";
        
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        
        return ! res.next();
    }
    
    // Método para agrupar y sumar los valores mdQuantityConsumption y mdCost_r
    private List<SDbStockValuationMvt> groupAndSumStockValuations(ArrayList<SDbStockValuationMvt> stockValuations) {
        // Agrupar por mtDateMove, mnFkItemId, mnFkUnitId, mnFkMaterialRequestItemRef_n, mnFkStockValuationId
        Map<String, List<SDbStockValuationMvt>> groupedValuations = stockValuations.stream()
                .collect(Collectors.groupingBy(valuation ->
                        valuation.getDateMove() + "_" +
                        valuation.getFkItemId() + "_" +
                        valuation.getFkUnitId() + "_" +
                        valuation.getFkCompanyBranchId() + "_" +
                        valuation.getFkWarehouseId() + "_" +
                        valuation.getFkStockValuationId()));

        // Crear nuevos objetos SDbStockValuationMvt con los valores sumados
        return groupedValuations.values().stream()
                .map(group -> {
                    SDbStockValuationMvt summedValuation = new SDbStockValuationMvt();

                    // Sumar mdQuantityConsumption y mdCost_r
                    double sumQuantityConsumption = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getQuantityMovement)
                            .sum();
                    double sumCostR = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getCost_r)
                            .sum();

                    // Configurar el nuevo objeto SDbStockValuationMvt
                    summedValuation.setDateMove(group.get(0).getDateMove());  // Tomar la fecha del primer elemento del grupo
                    summedValuation.setFkItemId(group.get(0).getFkItemId());  // Tomar el ID del item del primer elemento del grupo
                    summedValuation.setFkUnitId(group.get(0).getFkUnitId());  // Tomar el ID de la unidad del primer elemento del grupo
                    summedValuation.setFkStockValuationId(group.get(0).getFkStockValuationId());  // Tomar el ID de la valoración del stock del primer elemento del grupo
                    summedValuation.setQuantityMovement(sumQuantityConsumption);
                    summedValuation.setCost_r(SLibUtils.roundAmount(sumCostR));
                    summedValuation.setFkCompanyBranchId(group.get(0).getFkCompanyBranchId());
                    summedValuation.setFkWarehouseId(group.get(0).getFkWarehouseId());

                    return summedValuation;
                })
                .collect(Collectors.toList());
    }
    
    
    /**
     * The function takes a list of SDbStockValuationMvt objects, groups them based on certain
     * criteria, and returns a new list of SDbStockValuationMvt objects with the average cost unitary
     * value for each group.
     * 
     * @param stockValuations An ArrayList of SDbStockValuationMvt objects.
     * @return The method is returning a List of SDbStockValuationMvt objects.
     */
    private List<SDbStockValuationMvt> groupByDiogEtyAndAverage(ArrayList<SDbStockValuationMvt> stockValuations) {
        // Agrupar por mtDateMove, mnFkItemId, mnFkUnitId, mnFkMaterialRequestItemRef_n, mnFkStockValuationId
        Map<String, List<SDbStockValuationMvt>> groupedValuations = stockValuations.stream()
                .collect(Collectors.groupingBy(valuation ->
                        valuation.getFkDiogYearOutId_n() + "_" +
                        valuation.getFkDiogDocOutId_n() + "_" +
                        valuation.getFkDiogEntryOutId_n()));

        // Crear nuevos objetos SDbStockValuationMvt con los valores sumados
        return groupedValuations.values().stream()
                .map(group -> {
                    SDbStockValuationMvt averaged = new SDbStockValuationMvt();

                    // Sumar mdQuantityConsumption y mdCost_r
                    OptionalDouble avCostUnitary = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getCostUnitary)
                            .average();

                    // Configurar el nuevo objeto SDbStockValuationMvt
                    averaged.setFkDiogYearOutId_n(group.get(0).getFkDiogYearOutId_n());  // Tomar el año del primer elemento del grupo
                    averaged.setFkDiogDocOutId_n(group.get(0).getFkDiogDocOutId_n());  // Tomar doc del primer elemento del grupo
                    averaged.setFkDiogEntryOutId_n(group.get(0).getFkDiogEntryOutId_n());  // Tomar id ety del primer elemento del grupo
                    averaged.setCostUnitary(avCostUnitary.getAsDouble());

                    return averaged;
                })
                .collect(Collectors.toList());
    }
    
    public void setPkStockValuationId(int n) { mnPkStockValuationId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxRecordPk(Object[] o) { moAuxRecordPk = o; }
    public void setAuxIsAllDelete(boolean b) { mbAuxIsAllDelete = b; }
    public void setAuxIsAllInsert(boolean b) { mbAuxIsAllInsert = b; }
    
    public int getPkStockValuationtId() { return mnPkStockValuationId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Object[] getAuxRecordPk() { return moAuxRecordPk; }
    public boolean getAuxIsAllDelete() { return mbAuxIsAllDelete; }
    public boolean getAuxIsAllInsert() { return mbAuxIsAllInsert; }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationId = 0;
        mtDateStart = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxIsAllDelete = false;
        mbAuxIsAllInsert = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val = " + mnPkStockValuationId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValuationId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValuationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockValuationId = resultSet.getInt("id_stk_val");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msSql = "SELECT * FROM trn_stk_val_acc WHERE fk_stk_val = " + mnPkStockValuationId + " "
                    + "AND NOT b_del LIMIT 1;";
            
            ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(msSql);
            if (res.next()) {
                moAuxRecordPk = new Object[] {
                                                res.getInt("fk_fin_rec_year"),
                                                res.getInt("fk_fin_rec_per"),
                                                res.getInt("fk_fin_rec_bkc"),
                                                res.getString("fk_fin_rec_tp_rec"),
                                                res.getInt("fk_fin_rec_num")
                                            };

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
            computePrimaryKey(session);
            if (! mbAuxIsAllInsert) {
                computeStartDate(session);
            }
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkStockValuationId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_stk_val = " + mnPkValuationId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " "  + 
                    getSqlWhere();
        }
        
        try {
            session.getStatement().getConnection().setAutoCommit(false);
            
            if (moAuxRecordPk != null) {
                session.getStatement().getConnection().createStatement().execute(msSql);
                SStockValuationUtils.createEntries(session, SModSysConsts.TRNS_CT_IOG_IN, mtDateStart, mtDateEnd, mnPkStockValuationId);

                ArrayList<SDbStockValuationMvt> lConsumptions = SStockValuationUtils.consumeEntries(session, SModSysConsts.TRNS_CT_IOG_OUT, mtDateStart, mtDateEnd, mnPkStockValuationId);
                System.out.println("Guardando consumos...");
                for (SDbStockValuationMvt consumption : lConsumptions) {
                    consumption.save(session);
                }
                
                // The above code is grouping a list of consumptions and then updating the cost of
                // stock rows based on the grouped data. It first creates an empty ArrayList called
                // lGrouped. Then, it adds the grouped data to the lGrouped list using the
                // groupByDiogEtyAndAverage method. Finally, it iterates over each row in the lGrouped
                // list and updates the cost of the stock row using the updateTrnStockRowCostByDiog
                // method.
                System.out.println("Agrupando consumos...");
                ArrayList<SDbStockValuationMvt> lGrouped = new ArrayList<>();
                lGrouped.addAll(groupByDiogEtyAndAverage(lConsumptions));
                for (SDbStockValuationMvt oRow : lGrouped) {
                     SStockValuationUtils.updateTrnStockRowCostByDiog(session, oRow.getFkDiogYearOutId_n(), 
                                            oRow.getFkDiogDocOutId_n(), 
                                            oRow.getFkDiogEntryOutId_n(), 
                                            oRow.getCostUnitary(), 
                                            1d, 
                                            SStockValuationUtils.CREDIT);
                }
                System.out.println("Generando pólizas...");
                SStockValuationRecordUtils.makeRecordEntriesFromConsumptions(session, moAuxRecordPk, mtDateStart, lConsumptions);
            }
            else {
                throw new Exception("Se desconoce el identificador de la póliza contable.");
            }
            
            session.getStatement().getConnection().commit();
        }
        catch (SQLException ex) {
            session.getStatement().getConnection().rollback();
            Logger.getLogger(SDbStockValuation.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
        catch (Exception ex) {
            session.getStatement().getConnection().rollback();
            Logger.getLogger(SDbStockValuation.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();

        try {
            session.getStatement().getConnection().setAutoCommit(false);

            if (mbDeleted && (this.canDeleteValuation(session, mtDateEnd) || mbAuxIsAllDelete)) {
                SStockValuationUtils.deleteValuation(session, mnPkStockValuationId);
                session.getStatement().execute(msSql);
            }

            session.getStatement().getConnection().commit();
        }
        catch (SQLException ex) {
            session.getStatement().getConnection().rollback();
            Logger.getLogger(SDbStockValuation.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuation registry = new SDbStockValuation();
        
        registry.setPkStockValuationId(this.getPkStockValuationtId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
