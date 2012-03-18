/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.versioning;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.util.HashUtil;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.DSpaceValidity;
import org.dspace.app.xmlui.utils.HandleUtil;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Options;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DCValue;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.eperson.Group;
import org.dspace.utils.DSpace;
import org.dspace.versioning.VersionHistory;
import org.dspace.versioning.VersioningService;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

/**
 *
 * Navigation for Versioning of Items.
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class VersioningNavigation extends AbstractDSpaceTransformer implements CacheableProcessingComponent
{
    private static final Message T_context_head 				= message("xmlui.administrative.Navigation.context_head");

    private static final Message T_context_create_version= message("xmlui.aspect.versioning.VersioningNavigation.context_create_version");
    private static final Message T_context_show_version_history= message("xmlui.aspect.versioning.VersioningNavigation.context_show_version_history");




    /** Cached validity object */
	private SourceValidity validity;

	/** exports available for download */
	java.util.List<String> availableExports = null;

	 /**
     * Generate the unique cache key.
     *
     * @return The generated key hashes the src
     */
    public Serializable getKey()
    {
        Request request = ObjectModelHelper.getRequest(objectModel);

        // Special case, don't cache anything if the user is logging
        // in. The problem occures because of timming, this cache key
        // is generated before we know whether the operation has
        // succeded or failed. So we don't know whether to cache this
        // under the user's specific cache or under the anonymous user.

        if (request.getParameter("login_email")    != null ||
             request.getParameter("login_password") != null ||
               request.getParameter("login_realm")    != null )
        {
            return "0";
        }

        String key;
        if (context.getCurrentUser() != null)
        {
        	key = context.getCurrentUser().getEmail();
        	if(availableExports!=null && availableExports.size()>0){
        		for(String fileName:availableExports){
        			key+= ":"+fileName;
        		}
        	}
        }
        else
        	key = "anonymous";

        return HashUtil.hash(key);
    }

    /**
     * Generate the validity object.
     *
     * @return The generated validity object or <code>null</code> if the
     *         component is currently not cacheable.
     */
    public SourceValidity getValidity()
    {
    	if (this.validity == null)
    	{
    		// Only use the DSpaceValidity object is someone is logged in.
    		if (context.getCurrentUser() != null)
    		{
		        try {
		            DSpaceValidity validity = new DSpaceValidity();

		            validity.add(eperson);

		            Group[] groups = Group.allMemberGroups(context, eperson);
		            for (Group group : groups)
		            {
		            	validity.add(group);
		            }

		            this.validity = validity.complete();
		        }
		        catch (SQLException sqle)
		        {
		            // Just ignore it and return invalid.
		        }
    		}
    		else
    		{
    			this.validity = NOPValidity.SHARED_INSTANCE;
    		}
    	}
    	return this.validity;
    }

    public void addOptions(Options options) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException{
    	/* Create skeleton menu structure to ensure consistent order between aspects,
    	 * even if they are never used
    	 */
        options.addList("browse");
        options.addList("account");

        List context = options.addList("context");


        // Context Administrative options  for Versioning
        DSpaceObject dso = HandleUtil.obtainHandle(objectModel);
    	if (dso instanceof Item)
    	{
    		Item item = (Item) dso;
    		if (item.canEdit())
    		{
                context.setHead(T_context_head);
                if(!isItemADataFile(item) ){
                    if(isCurrentEpersonItemOwner(item) || canCurrentEPersonEditTheITem(item)){
                    
                        if(isLatest(item) && item.isArchived()){
                            context.addItem().addXref(contextPath+"/item/version?itemID="+item.getID(), T_context_create_version);
                        }

                        if(hasVersionHistory(item))
                            context.addItem().addXref(contextPath+"/item/versionhistory?itemID="+item.getID(), T_context_show_version_history);
                    }
                }
            }
    	}
    }


    /**
     * recycle
     */
    public void recycle()
    {
        this.validity = null;
        super.recycle();
    }


    private boolean isCurrentEpersonItemOwner(Item item) throws SQLException {
        if(item.getSubmitter().getID() ==eperson.getID())
            return true;
        return false;
    }

    private boolean canCurrentEPersonEditTheITem(Item item) throws SQLException {
        return item.canEdit();
    }


    private boolean isItemADataFile(Item item){
        DCValue[] values = item.getMetadata("dc.relation.ispartof");
        if(values==null || values.length==0)
            return false;
        return true;
    }

    private boolean isLatest(Item item){
        VersioningService versioningService = new DSpace().getSingletonService(VersioningService.class);
        VersionHistory history = versioningService.findVersionHistory(context, item.getID());
        return (history==null || history.getLatestVersion().getItem().getID() == item.getID());
    }


    private boolean hasVersionHistory(Item item){
        VersioningService versioningService = new DSpace().getSingletonService(VersioningService.class);
        VersionHistory history = versioningService.findVersionHistory(context, item.getID());
        return (history!=null);
    }

}
