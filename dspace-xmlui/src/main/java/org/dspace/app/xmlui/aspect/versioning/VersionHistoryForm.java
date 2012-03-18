/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.versioning;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.HandleUtil;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.*;
import org.dspace.content.DCValue;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.MetadataSchema;
import org.dspace.core.ConfigurationManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.utils.DSpace;
import org.dspace.versioning.Version;
import org.dspace.versioning.VersionHistory;
import org.dspace.versioning.VersioningService;

import java.sql.SQLException;

/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class VersionHistoryForm extends AbstractDSpaceTransformer {
    /** Language strings */
    private static final Message T_dspace_home = message("xmlui.general.dspace_home");

    private static final Message T_head2 = message("xmlui.aspect.versioning.VersionHistoryForm.head2");
    private static final Message T_column1 = message("xmlui.aspect.versioning.VersionHistoryForm.column1");
    private static final Message T_column2 = message("xmlui.aspect.versioning.VersionHistoryForm.column2");
    private static final Message T_column3 = message("xmlui.aspect.versioning.VersionHistoryForm.column3");
    private static final Message T_column4 = message("xmlui.aspect.versioning.VersionHistoryForm.column4");
    private static final Message T_column5 = message("xmlui.aspect.versioning.VersionHistoryForm.column5");
    private static final Message T_column6 = message("xmlui.aspect.versioning.VersionHistoryForm.column6");
    private static final Message T_title = message("xmlui.aspect.versioning.VersionHistoryForm.title");
    private static final Message T_trail = message("xmlui.aspect.versioning.VersionHistoryForm.trail");
    private static final Message T_submit_update = message("xmlui.aspect.versioning.VersionHistoryForm.update");
    private static final Message T_submit_restore = message("xmlui.aspect.versioning.VersionHistoryForm.restore");
    private static final Message T_submit_delete = message("xmlui.aspect.versioning.VersionHistoryForm.delete");
    private static final Message T_submit_cancel = message("xmlui.aspect.versioning.VersionHistoryForm.return");


    public void addPageMeta(PageMeta pageMeta) throws WingException{
        pageMeta.addMetadata("title").addContent(T_title);
        pageMeta.addTrailLink(contextPath + "/", T_dspace_home);
        //pageMeta.addTrailLink(contextPath + "/admin/item", T_item_trail);
        pageMeta.addTrail().addContent(T_trail);
    }


    public void addBody(Body body) throws WingException{
        boolean isItemView=(parameters.getParameterAsInteger("itemID",-1)==-1);
        Item item = getItem(isItemView);

        if(item==null) return;

        VersionHistory versionHistory = retrieveVersionHistory(item);
        if(versionHistory!=null){
            Division main = createMain(body);
            createTable(main, versionHistory, isItemView, item);

            if(!isItemView){
                addButtons(main, versionHistory);
                main.addHidden("versioning-continue").setValue(knot.getId());
            }

            Para note = main.addPara();
            note.addContent("* Selected Version");
        }
    }


    private Item getItem(boolean isItemView) throws WingException{
        try {
            if(isItemView){
                DSpaceObject dso = null;
                dso = HandleUtil.obtainHandle(objectModel);
                if (!(dso instanceof Item)) return null;
                Item item = (Item) dso;
                return item;
            }
            return Item.find(context, parameters.getParameterAsInteger("itemID",-1));
        } catch (SQLException e) {
            throw new WingException(e);
        }


    }

    private VersionHistory retrieveVersionHistory(Item item) throws WingException {
        VersioningService versioningService = new DSpace().getSingletonService(VersioningService.class);
        return versioningService.findVersionHistory(context, item.getID());

    }


    private Division createMain(Body body) throws WingException {
        Division main = body.addInteractiveDivision("view-verion-history", contextPath+"/item/versionhistory", Division.METHOD_POST, "view version history");
        main.setHead(T_head2);
        return main;
    }

    private void createTable(Division main, VersionHistory history, boolean isItemView, Item item) throws WingException {
        Table table = main.addTable("versionhistory", 1, 1);
        
        Row header = table.addRow(Row.ROLE_HEADER);
        header.addCell().addContent(T_column1);
        header.addCell().addContent(T_column2);
        //header.addCell().addContent(T_column3);
        header.addCell().addContent(T_column4);
        header.addCell().addContent(T_column5);

        if(!isItemView){
            header.addCell().addContent(T_column6);
            header.addCell().addContent("");
            header.addCell().addContent("");
        }

        if(history != null){
            for(Version version : history.getVersions()){

                if(isItemInSubmission(version.getItem())) continue;

                Row row = table.addRow(null, Row.ROLE_DATA,"metadata-value");
                if(!isItemView){
                    CheckBox remove = row.addCell().addCheckBox("remove");
				    remove.setLabel("remove");
				    remove.addOption(version.getVersionId());
                }

                Cell cell = row.addCell();
                addItemIdentifier(cell, item, version, row);

                row.addCell().addContent(version.getVersionNumber());
                //row.addCell().addContent(version.getEperson().getEmail());
                row.addCell().addContent(version.getVersionDate().toString());
                row.addCell().addContent(version.getSummary());


                if(!isItemView)
                    row.addCell().addXref(contextPath + "/item/versionhistory?versioning-continue="+knot.getId()+"&versionID="+version.getVersionId() +"&itemID="+ version.getItem().getID() + "&submit_update", T_submit_update);
            }
        }

    }


    private boolean isItemInSubmission(Item item) {
        try{
            TableRow row = DatabaseManager.querySingleTable(context, "workspaceitem", "SELECT * FROM workspaceitem WHERE item_id= ?", item.getID());
            if (row != null) return true;

            row = DatabaseManager.querySingleTable(context, "workflowitem", "SELECT collection_id FROM workflowitem WHERE item_id= ?", item.getID());
            if (row != null) return true;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return false;

    }

    private void addItemIdentifier(Cell cell, Item item, Version version, Row row) throws WingException {
        String itemHandle = version.getItem().getHandle();

        DCValue[] identifiers = version.getItem().getMetadata(MetadataSchema.DC_SCHEMA, "identifier", null, Item.ANY);
        String itemIdentifier=null;
        if(identifiers!=null && identifiers.length > 0)
            itemIdentifier = identifiers[0].value;

        if(itemIdentifier!=null)
            cell.addXref(ConfigurationManager.getProperty("dspace.baseUrl") + "/resource/" + itemIdentifier, itemIdentifier);
        else
            cell.addXref(ConfigurationManager.getProperty("dspace.baseUrl") + "/handle/" + itemHandle, itemHandle);

        if(item.getID()==version.getItemID()) cell.addContent("*");
    }

    private void addButtons(Division main, VersionHistory history) throws WingException {
        Para actions = main.addPara();

        /*if(history!=null && history.getVersions().size() > 0)
            actions.addButton("submit_delete").setValue(T_submit_delete);*/

        actions.addButton("submit_cancel").setValue(T_submit_cancel);		

    }
}
