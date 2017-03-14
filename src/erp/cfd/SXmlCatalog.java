/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.util.DUtilUtils;
import erp.lib.form.SFormComponentItem;
import erp.mod.SModConsts;
import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.xml.SXmlUtils;

/**
 * Class from management of catalogs from SAT.
 * @author Juan Barajas
 */
public class SXmlCatalog {
    
    private SGuiSession moSession;
    private HashMap<Integer, ArrayList> CatalogsMap = new HashMap<Integer, ArrayList>();

    public SXmlCatalog(SGuiSession session) throws Exception {
        moSession = session;
        computeCatalogs();
    }
    
    /**
     * Obtain options all for catalog indicated.
     * @param session
     * @throws Exception 
     */
    private void computeCatalogs() {
        String sql = "";
        String xml = "";
        int catalogId = 0;
        ResultSet resultSet = null;
        SCatalogXmlEntry catalog = null;
        Node node = null;
        Node nodeChild = null;
        Vector<Node> nodeChilds = null;
        NamedNodeMap namedNodeMapChild = null;
        DocumentBuilder docBuilder = null;
        Document doc = null;
        ArrayList<SCatalogXmlEntry> catalogs = null;
        
        // Catalog:
        
        try {
            sql = "SELECT id_cfd_cat, xml " +
                   "FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_CFD_CAT) + " " +
                   "WHERE b_del = 0 " + 
                   "ORDER BY id_cfd_cat, dt_sta DESC ";

            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                xml = resultSet.getString("xml");
                catalogId = resultSet.getInt("id_cfd_cat");
                catalogs = new ArrayList<SCatalogXmlEntry>();

                docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

                node = SXmlUtils.extractElements(doc, "Catalog").item(0);
                nodeChilds = SXmlUtils.extractChildElements(node, "Entry");

                for (int i = 0; i < nodeChilds.size(); i++) {
                    catalog = new SCatalogXmlEntry();

                    nodeChild = nodeChilds.get(i);
                    namedNodeMapChild = nodeChild.getAttributes();

                    catalog.setCode(SXmlUtils.extractAttributeValue(namedNodeMapChild, "code", true));
                    catalog.setName(SXmlUtils.extractAttributeValue(namedNodeMapChild, "name", true));
                    catalog.setDateStart(DUtilUtils.DbmsDateFormatDate.parse(SXmlUtils.extractAttributeValue(namedNodeMapChild, "dt_sta", true)));

                    catalogs.add(catalog);
                }
                CatalogsMap.put(catalogId, catalogs);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    /**
     * Get all the entries for the given catalog, as an array of type SFormComponentItem to be added to a comboBox.
     * @param client SGuiClient.
     * @param catalogId catalog ID to process (i.e. TRNS_CFD_CAT_ .. or TRNS_CFD_CCE_CAT_)
     * @param dateStart date start of validity.
     * @return options from catalog as a vector.
     * @throws Exception 
     */
    public Vector<SFormComponentItem> getComponentItems(final SGuiClient client, final int catalogId, final Date dateStart) throws Exception {
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();
        ArrayList<SCatalogXmlEntry> catalogs = new ArrayList<SCatalogXmlEntry>();
        
        catalogs = CatalogsMap.get(catalogId);

        items.add(new SFormComponentItem("", "(Seleccionar una opción)"));
        
        for (SCatalogXmlEntry item : catalogs) {
            if (item.getDateStart().compareTo(dateStart) <= 0) {
                items.add(new SFormComponentItem(item.getCode(), item.getName()));
            }
        }
        
        return items;
    }
    
    /**
     * Get the name for the indicated code of a certain catalog.
     * @param client SGuiClient.
     * @param catalogId catalog ID to process (i.e. TRNS_CFD_CAT_ .. or TRNS_CFD_CCE_CAT_)
     * @param dateStart date start of validity.
     * @param code code to search.
     * @return
     * @throws Exception 
     */
    public String getCatalogOptionName(final SGuiClient client, final int catalogId, final Date dateStart, final String code) throws Exception {
        String optionName = "";
        ArrayList<SCatalogXmlEntry> catalogs = new ArrayList<SCatalogXmlEntry>();
        
        catalogs = CatalogsMap.get(catalogId);
        
        for (SCatalogXmlEntry item : catalogs) {
            if (item.getCode().compareTo(code) == 0) {
                optionName = item.getName();
            }
        }
        
        return optionName;
    }
}
