/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning;

import org.dspace.content.Item;
import org.dspace.core.Context;

/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public interface ItemVersionProvider {
    public Item createNewItemAndAddItInWorkspace(Context c, Item item);
//    public Item createNewItemAndAddItInWorkspace(Context c, Item itemToCopy, Item itemToWire);
    public void deleteVersionedItem(Context c, Version versionToDelete, VersionHistory history);
//    public Item createNewItemAndAddItInWorkspace(Context c, Item itemToCopy, Item itemToWire, Version version);
    public boolean isResponsible();

    Item updateItemState(Context c, Item itemNew, Item previousItem);
}
