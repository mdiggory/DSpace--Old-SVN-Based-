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
 * Versioning Service is the key API for
 * interacting with Versioning of DSpace Items.  Versions exist
 * independent of the Items they are associated with, An item
 * Version History can include Items that no longer exist.  But the
 * best practice will be to remove Items through this interface by
 * interacting with their version history.
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public interface VersioningService {


    /**
     * Create a new version of an Item using its Item ID
     * Comments may be added or edited  after the version
     * is created.
     *
     * @param c
     * @param itemId
     * @return Version of Item
     */
    Version createNewVersion(Context c, int itemId);

    /**
     * Create a new version of an Item using its Item ID with a
     * reason why it has been created.
     * Comments may be added or edited  after the version
     * is created.
     *
     * @param c
     * @param itemId
     * @param summary
     * @return
     */
    Version createNewVersion(Context c, int itemId, String summary);

    /**
     * Remove the identified version of the Item.  Current implementation
     * Will delete associated Item as well.
     *
     * @param c
     * @param versionID
     */
    void removeVersion(Context c, int versionID);

    /**
     * Remove the identified version of the Item.  Current implementation
     * Will delete associated Item as well.
     *
     * @param c
     * @param item
     */
    void removeVersion(Context c, Item item);

    /**
     * Get the specified Version of the Item
     * @param c
     * @param versionID
     * @return
     */
    Version getVersion(Context c, int versionID);

    /**
     * Make the specified version of the Item the most current
     * version, this will copy the specified version metadata
     * and bitstream/bundle associations to the new version.
     *
     * @param c
     * @param versionID
     * @return
     */
    Version restoreVersion(Context c, int versionID);

    /**
     * Make the specified version of the Item the most current
     * version, this will copy the specified version metadata
     * and bitstream/bundle associations to the new version.
     *
     * @param c
     * @param versionID
     * @param summary
     * @return
     */
    Version restoreVersion(Context c, int versionID, String summary);

    /**
     * Will retireve the version History for an Item
     * @param c
     * @param itemId
     * @return
     */
    VersionHistory findVersionHistory(Context c, int itemId);

    /**
     * Will update the Summary for the specified version.
     *
     * @param c
     * @param itemId
     * @param summary
     * @return
     */
    Version updateVersion(Context c, int itemId, String summary);

    /**
     * Returns the version that is associated with this item.
     * @param c
     * @param item
     * @return
     */
    Version getVersion(Context c, Item item);
}
