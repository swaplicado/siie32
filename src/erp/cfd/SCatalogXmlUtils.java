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
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.xml.SXmlUtils;

/**
 * Class from management of catalogs from SAT.
 * @author Juan Barajas
 */
public abstract class SCatalogXmlUtils {
    
    /**
     * Return XML for catalog indicated.
     * @param client SGuiClient
     * @param catalogId catalog to process (i.e. TRNS_CFD_CAT_ .. or TRNS_CFD_CCE_CAT_)
     * @param dateStart date start of validity.
     * @return catalog indicated in XML format
     * @throws Exception 
     */
    private static String getCatalogXml(final SGuiClient client, final int catalogId, final Date dateStart) throws Exception {
        String xml = "";
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT xml " +
               "FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_CFD_CAT) + " " +
               "WHERE id_cfd_cat = " + catalogId + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' " + 
               "ORDER BY dt_sta DESC ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            xml = resultSet.getString("xml");
        }
        
        return xml;
    }
    
    /**
     * Obtain options all for catalog indicated.
     * @param xml Catalog in XML format.
     * @param dateStart date start of validity.
     * @return options all for catalog.
     * @throws Exception 
     */
    public static ArrayList<SCatalogXmlEntry> getCatalogOptions(final String xml, final Date dateStart) throws Exception {
        SCatalogXmlEntry catalog = null;
        Node node = null;
        Node nodeChild = null;
        Vector<Node> nodeChilds = null;
        NamedNodeMap namedNodeMapChild = null;
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        ArrayList<SCatalogXmlEntry> catalogs = new ArrayList<SCatalogXmlEntry>();
        
        // Catalog:

        node = SXmlUtils.extractElements(doc, "Catalog").item(0);
        nodeChilds = SXmlUtils.extractChildElements(node, "Entry");

        for (int i = 0; i < nodeChilds.size(); i++) {
            catalog = new SCatalogXmlEntry();
            
            nodeChild = nodeChilds.get(i);
            namedNodeMapChild = nodeChild.getAttributes();

            catalog.setCode(SXmlUtils.extractAttributeValue(namedNodeMapChild, "code", true));
            catalog.setName(SXmlUtils.extractAttributeValue(namedNodeMapChild, "name", true));
            catalog.setDateStart(DUtilUtils.DbmsDateFormatDate.parse(SXmlUtils.extractAttributeValue(namedNodeMapChild, "dt_sta", true)));
            
            if (catalog.getDateStart().compareTo(dateStart) <= 0) {
                catalogs.add(catalog);
            }
        }
        
        return catalogs;
    }
    
    /**
     * Get all the entries for the given catalog, as an array of type SFormComponentItem to be added to a comboBox.
     * @param client SGuiClient.
     * @param catalogId catalog ID to process (i.e. TRNS_CFD_CAT_ .. or TRNS_CFD_CCE_CAT_)
     * @param dateStart date start of validity.
     * @return options from catalog as a vector.
     * @throws Exception 
     */
    public static Vector<SFormComponentItem> getComponentItems(final SGuiClient client, final int catalogId, final Date dateStart) throws Exception {
        String xmlCatalog = "";
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();
        ArrayList<SCatalogXmlEntry> catalogs = new ArrayList<SCatalogXmlEntry>();

        xmlCatalog = getCatalogXml(client, catalogId, dateStart);

        if (!xmlCatalog.isEmpty()) {
            catalogs = getCatalogOptions(xmlCatalog, dateStart);
        }

        items.add(new SFormComponentItem("", "(Seleccionar una opción)"));
        for (SCatalogXmlEntry item : catalogs) {
            items.add(new SFormComponentItem(item.getCode(), item.getName()));
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
    public static String getCatalogOptionName(final SGuiClient client, final int catalogId, final Date dateStart, final String code) throws Exception {
        String optionName = "";
        String xmlCatalog = "";
        ArrayList<SCatalogXmlEntry> catalogs = new ArrayList<SCatalogXmlEntry>();
        
        xmlCatalog = getCatalogXml(client, catalogId, dateStart);

        if (!xmlCatalog.isEmpty()) {
            catalogs = getCatalogOptions(xmlCatalog, dateStart);
        }
        
        for (SCatalogXmlEntry item : catalogs) {
            if (item.getCode().compareTo(code) == 0) {
                optionName = item.getName();
            }
        }
        
        return optionName;
    }
}
