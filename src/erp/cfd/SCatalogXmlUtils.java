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
 *
 * @author Juan Barajas
 */
public abstract class SCatalogXmlUtils {
    
    /**
     * Return XML for catalog indicated 
     * @param client SGuiClient
     * @param catalogId catalog to process
     * @param dateStart
     * @return
     * @throws Exception 
     */
    private static String getCatalogXml(final SGuiClient client, final int catalogId, final Date dateStart) throws Exception {
        ResultSet resultSet = null;
        String sql = "";
        String xml = "";

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
     * Obtain entries all for catalog indicated.
     * @param xml 
     * @param dateStart
     * @return
     * @throws Exception 
     */
    public static ArrayList<SCatalogXmlEntry> getCatalogs(final String xml, final Date dateStart) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Node node = null;
        Node nodeChild = null;
        Vector<Node> nodeChilds = null;
        NamedNodeMap namedNodeMapChild = null;
        ArrayList<SCatalogXmlEntry> aCatalogues = new ArrayList<SCatalogXmlEntry>();
        SCatalogXmlEntry catalogue = null;
        
        // Catalog:

        node = SXmlUtils.extractElements(doc, "Catalog").item(0);
        nodeChilds = SXmlUtils.extractChildElements(node, "Entry");

        for (int i = 0; i < nodeChilds.size(); i++) {
            catalogue = new SCatalogXmlEntry();
            
            nodeChild = nodeChilds.get(i);
            namedNodeMapChild = nodeChild.getAttributes();

            catalogue.setCode(SXmlUtils.extractAttributeValue(namedNodeMapChild, "code", true));
            catalogue.setName(SXmlUtils.extractAttributeValue(namedNodeMapChild, "name", true));
            catalogue.setDateStart(DUtilUtils.DbmsDateFormatDate.parse(SXmlUtils.extractAttributeValue(namedNodeMapChild, "dt_sta", true)));
            
            if (catalogue.getDateStart().compareTo(dateStart) <= 0) {
                aCatalogues.add(catalogue);
            }
        }
        
        return aCatalogues;
    }
    
    /**
     * Get all the entries for the given catalog, as an array of type SFormComponentItem to be added to a comboBox.
     * @param client SGuiClient.
     * @param catalogId catalog ID.
     * @param dateStart date start of validity.
     * @return
     * @throws Exception 
     */
    public static Vector<SFormComponentItem> getComponentItems(final SGuiClient client, final int catalogId, final Date dateStart) throws Exception {
        String xmlCatalog = "";
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();
        ArrayList<SCatalogXmlEntry> aCatalogs = new ArrayList<SCatalogXmlEntry>();

        xmlCatalog = getCatalogXml(client, catalogId, dateStart);

        if (!xmlCatalog.isEmpty()) {
            aCatalogs = getCatalogs(xmlCatalog, dateStart);
        }

        items.add(new SFormComponentItem("", "Seleccionar una opción"));
        for (SCatalogXmlEntry item : aCatalogs) {
            items.add(new SFormComponentItem(item.getCode(), item.getName()));
        }
        
        return items;
    }
    
    /**
     * Get the name for the indicated code of a certain catalog.
     * @param client SGuiClient.
     * @param catalogId catalog ID.
     * @param dateStart date start of validity.
     * @param code code to search.
     * @return
     * @throws Exception 
     */
    public static String getNameEntry(final SGuiClient client, final int catalogId, final Date dateStart, final String code) throws Exception {
        ArrayList<SCatalogXmlEntry> aCatalogs = new ArrayList<SCatalogXmlEntry>();
        String name = "";
        String xmlCatalog = "";
        
        xmlCatalog = getCatalogXml(client, catalogId, dateStart);

        if (!xmlCatalog.isEmpty()) {
            aCatalogs = getCatalogs(xmlCatalog, dateStart);
        }
        
        for (SCatalogXmlEntry item : aCatalogs) {
            if (item.getCode().compareTo(code) == 0) {
                name = item.getName();
            }
        }
        
        return name;
    }
}
