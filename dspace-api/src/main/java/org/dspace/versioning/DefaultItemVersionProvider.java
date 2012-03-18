/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.Context;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 */
public class DefaultItemVersionProvider extends AbstractVersionProvider implements ItemVersionProvider {


    //TODO implment the following method
    public Item createNewItemAndAddItInWorkspace(Context context, Item nativeItem) {
          try{
            WorkspaceItem workspaceItem = WorkspaceItem.create(context, nativeItem.getOwningCollection(), false);
            Item itemNew = workspaceItem.getItem();
            copyMetadata(itemNew, nativeItem);
            createBundlesAndAddBitstreams(itemNew, nativeItem);
            itemNew.update();
            return itemNew;
        }catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }catch (AuthorizeException e) {
           throw new RuntimeException(e.getMessage(), e);
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteVersionedItem(Context c, Version versionToDelete, VersionHistory history) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isResponsible() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Item updateItemState(Context c, Item itemNew, Item previousItem) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
