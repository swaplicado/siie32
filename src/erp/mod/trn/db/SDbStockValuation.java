/*
 * Para cambiar esta licencia, consulte el archivo LICENSE en la raíz del proyecto.
 * Para cambiar esta plantilla de archivo, elija Herramientas | Plantillas
 * y abra la plantilla en el editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
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
 * Clase que representa la valuación de inventario.
 * Permite calcular, guardar, leer y eliminar valoraciones de inventario,
 * así como agrupar y calcular costos de movimientos de inventario.
 * 
 * @author Edwin Carmona
 */
public class SDbStockValuation extends SDbRegistryUser {
    
    // Llave primaria de la valuación de inventario
    protected int mnPkStockValuationId;
    // Fecha de inicio de la valuación
    protected Date mtDateStart;
    // Fecha de fin de la valuación
    protected Date mtDateEnd;
    //protected boolean mbDeleted;
    //protected int mnFkUserInsertId;
    //protected int mnFkUserUpdateId;
    //protected Date mtTsUserInsert;
    //protected Date mtTsUserUpdate;
    
    // Llave auxiliar para la póliza contable asociada
    protected Object[] moAuxRecordPk;
    // Bandera auxiliar para eliminar todas las valoraciones
    protected boolean mbAuxIsAllDelete;
    // Bandera auxiliar para insertar todas las valoraciones
    protected boolean mbAuxIsAllInsert;

    /**
     * Constructor de la clase.
     */
    public SDbStockValuation() {
        super(SModConsts.TRN_STK_VAL);
    }
    
    /**
     * Calcula la fecha de inicio de la valuación con base en la fecha de corte y ciertas condiciones.
     * 
     * @param session Sesión de la GUI para ejecutar consultas SQL.
     * @param cutoffDate Fecha de corte para la valuación.
     * @return Fecha de inicio calculada.
     * @throws java.sql.SQLException
     * @throws java.lang.Exception 
     */
    public static Date computeStartDate(SGuiSession session, Date cutoffDate) throws SQLException, Exception {
        ResultSet resultSet;

        Date tDateStart = null;

        String msSql = "SELECT DATE_ADD(dt_end, INTERVAL 1 DAY) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                + "WHERE NOT b_del "
                + "AND YEAR(dt_end) = " + (new SimpleDateFormat("yyyy")).format(cutoffDate) + " "
                + "ORDER BY dt_end DESC";
        
        resultSet = session.getStatement().getConnection().createStatement().executeQuery(msSql);
        if (resultSet.next()) {
            tDateStart = resultSet.getDate(1);
        }
        else {
            Calendar cal = Calendar.getInstance();

            // Establecer la fecha en el objeto Calendar
            cal.setTime(cutoffDate);

            // Establecer el mes y el día del primer día del año
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            // Obtener el primer día del año como objeto Date
            tDateStart = cal.getTime();
        }

        return tDateStart;
    }
    
    /**
     * Verifica si existen valoraciones de inventario no eliminadas posteriores a una fecha dada.
     * 
     * @param session Sesión de la GUI para ejecutar consultas SQL.
     * @param dtEnd Fecha de fin de la valuación.
     * @return true si se puede eliminar la valuación, false en caso contrario.
     * @throws java.sql.SQLException 
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
    
    /**
     * Agrupa y suma los valores de cantidad consumida y costo real de los movimientos de inventario.
     * 
     * @param stockValuations Lista de movimientos de valuación de inventario.
     * @return Lista agrupada y sumada de movimientos de valuación.
     */
    private List<SDbStockValuationMvt> groupAndSumStockValuations(ArrayList<SDbStockValuationMvt> stockValuations) {
        // Agrupar por fecha de movimiento, id de ítem, id de unidad, id de sucursal, id de almacén y id de valuación
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

                    // Sumar cantidad consumida y costo real
                    double sumQuantityConsumption = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getQuantityMovement)
                            .sum();
                    double sumCostR = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getCost_r)
                            .sum();

                    // Configurar el nuevo objeto SDbStockValuationMvt
                    summedValuation.setDateMove(group.get(0).getDateMove());  // Tomar la fecha del primer elemento del grupo
                    summedValuation.setFkItemId(group.get(0).getFkItemId());  // Tomar el ID del ítem del primer elemento del grupo
                    summedValuation.setFkUnitId(group.get(0).getFkUnitId());  // Tomar el ID de la unidad del primer elemento del grupo
                    summedValuation.setFkStockValuationId(group.get(0).getFkStockValuationId());  // Tomar el ID de la valuación del primer elemento del grupo
                    summedValuation.setQuantityMovement(sumQuantityConsumption);
                    summedValuation.setCost_r(SLibUtils.roundAmount(sumCostR));
                    summedValuation.setFkCompanyBranchId(group.get(0).getFkCompanyBranchId());
                    summedValuation.setFkWarehouseId(group.get(0).getFkWarehouseId());

                    return summedValuation;
                })
                .collect(Collectors.toList());
    }
    
    
    /**
     * Agrupa los movimientos por DIOG (documento de inventario) y calcula el costo unitario promedio.
     * 
     * @param stockValuations Lista de movimientos de valuación de inventario.
     * @return Lista agrupada con el costo unitario promedio por grupo.
     */
    private List<SDbStockValuationMvt> groupByDiogEtyAndAverage(ArrayList<SDbStockValuationMvt> stockValuations) {
        // Agrupar por año, documento y entrada de DIOG
        Map<String, List<SDbStockValuationMvt>> groupedValuations = stockValuations.stream()
                .collect(Collectors.groupingBy(valuation ->
                        valuation.getFkDiogYearOutId_n() + "_" +
                        valuation.getFkDiogDocOutId_n() + "_" +
                        valuation.getFkDiogEntryOutId_n()));

        // Crear nuevos objetos SDbStockValuationMvt con el costo unitario promedio
        return groupedValuations.values().stream()
                .map(group -> {
                    SDbStockValuationMvt averaged = new SDbStockValuationMvt();

                    // Calcular el costo unitario promedio
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
    
    /**
     * Agrupa los movimientos por DIOG y calcula el costo unitario como suma de costos entre suma de cantidades.
     * 
     * @param stockValuations Lista de movimientos de valuación de inventario.
     * @return Lista agrupada con el costo unitario calculado por grupo.
     */
    private List<SDbStockValuationMvt> groupByDiogEtyAndCalculateCost(ArrayList<SDbStockValuationMvt> stockValuations) {
        // Agrupar por año, documento y entrada de DIOG
        Map<String, List<SDbStockValuationMvt>> groupedValuations = stockValuations.stream()
                .collect(Collectors.groupingBy(valuation ->
                        valuation.getFkDiogYearOutId_n() + "_" +
                        valuation.getFkDiogDocOutId_n() + "_" +
                        valuation.getFkDiogEntryOutId_n()));

        // Crear nuevos objetos SDbStockValuationMvt con el costo unitario calculado
        return groupedValuations.values().stream()
                .map(group -> {
                    SDbStockValuationMvt averaged = new SDbStockValuationMvt();

                    // Sumar costo real y cantidad
                    double costR = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getCost_r)
                            .sum();
                    double quantityR = group.stream()
                            .mapToDouble(SDbStockValuationMvt::getQuantityMovement)
                            .sum();

                    // Configurar el nuevo objeto SDbStockValuationMvt
                    averaged.setFkDiogYearOutId_n(group.get(0).getFkDiogYearOutId_n());  // Tomar el año del primer elemento del grupo
                    averaged.setFkDiogDocOutId_n(group.get(0).getFkDiogDocOutId_n());  // Tomar doc del primer elemento del grupo
                    averaged.setFkDiogEntryOutId_n(group.get(0).getFkDiogEntryOutId_n());  // Tomar id ety del primer elemento del grupo
                    averaged.setCostUnitary(costR / quantityR);

                    return averaged;
                })
                .collect(Collectors.toList());
    }
    
    // Métodos setter y getter para los atributos de la clase
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

    /**
     * Guarda la valuación de inventario en la base de datos.
     * Si es un registro nuevo, lo inserta; si no, lo actualiza.
     * Además, realiza los procesos de creación de movimientos, consumos, agrupaciones, ajustes y generación de pólizas.
     * 
     * @param session Sesión de la GUI para ejecutar consultas SQL.
     * @throws SQLException
     * @throws Exception
     */
    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            if (! mbAuxIsAllInsert) {
                mtDateStart = SDbStockValuation.computeStartDate(session, mtDateEnd);
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
                // Crear movimientos de ajuste de inventario
                List<SDbStockValuationMvt> lMvtAdjs = SStockValuationAdjustsUtils.makeStockValuationAdjusts(session, mtDateStart, mtDateEnd, mnPkStockValuationId);
                System.out.println("Creando entries...");
                // Crear entradas de inventario
                SStockValuationUtils.createEntries(session, SModSysConsts.TRNS_CT_IOG_IN, mtDateStart, mtDateEnd, mnPkStockValuationId);

                // Consumir salidas de inventario
                ArrayList<SDbStockValuationMvt> lConsumptions = SStockValuationUtils.consumeEntries(session, SModSysConsts.TRNS_CT_IOG_OUT, mtDateStart, mtDateEnd, mnPkStockValuationId);
                System.out.println("Guardando consumos...");
                for (SDbStockValuationMvt consumption : lConsumptions) {
                    consumption.save(session);
                    if (consumption.getLogMessage() != null && ! consumption.getLogMessage().isEmpty()) {
                        SStockValuationLogUtils.logConsume(mtDateStart, mtDateEnd, consumption, consumption.getLogMessage());
                    }
                }
                
                System.out.println("Agrupando consumos...");
                ArrayList<SDbStockValuationMvt> lGrouped = new ArrayList<>();
                lGrouped.addAll(groupByDiogEtyAndCalculateCost(lConsumptions));
                System.out.println("Actualizando costos en stock...");
                for (SDbStockValuationMvt oRow : lGrouped) {
                    SStockValuationUtils.updateTrnStockRowCostByDiog(session, oRow.getFkDiogYearOutId_n(), 
                                            oRow.getFkDiogDocOutId_n(), 
                                            oRow.getFkDiogEntryOutId_n(), 
                                            oRow.getCostUnitary(),
                                            SStockValuationUtils.CREDIT);
                }
                lConsumptions.addAll(0, lMvtAdjs);
                System.out.println("Ajustando inventario...");
                List<SDataDiog> lDiogs = SStockValuationAdjustsUtils.makeStockAdjusts(session, mtDateStart, lMvtAdjs);
                SDbStockValuationDiogAdjust oValDiogAdj;
                for (SDataDiog oDiog : lDiogs) {
                    oValDiogAdj = new SDbStockValuationDiogAdjust(mnPkStockValuationId, oDiog.getPkYearId(), oDiog.getPkDocId());
                    oValDiogAdj.save(session);
                }
                
                System.out.println("Generando pólizas...");
                SStockValuationRecordUtils.makeRecordEntriesFromConsumptions(session, moAuxRecordPk, mtDateStart, lConsumptions);
                System.out.println("Terminado.");
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
    
    /**
     * Elimina (marca como eliminado) la valuación de inventario.
     * Solo elimina si no existen valoraciones posteriores o si la bandera auxiliar lo permite.
     * 
     * @param session Sesión de la GUI para ejecutar consultas SQL.
     * @throws SQLException 
     */
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

    /**
     * Crea una copia del registro de valuación de inventario.
     * 
     * @return Copia del registro.
     * @throws CloneNotSupportedException 
     */
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
