/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning;

import org.dspace.content.Item;
import org.dspace.eperson.EPerson;

import java.util.Date;

/**
 * Version Domain Model Definition
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public interface Version {

    /**
     * EPerson that made Version
     * @return EPerson
     */
    public EPerson getEperson();

    /**
     * Item id associated with this Version
     * @return item id
     */
    public int getItemID();

    /**
     * Date this version was Minted.
     * @return Version Date
     */
    public Date getVersionDate();

    /**
     * Version Sequence Number of this Version
     * @return Sequence id
     */
    public int getVersionNumber();

    /**
     * A descriptive text summary of the Version
     *
     * @return summary description
     */
    public String getSummary();

    /**
     * The Overall Version History ID to find
     * all the versions in the History
     * @return version history ID
     */
    public int getVersionHistoryID();

    /**
     * The Database ID of this Version
     * @return Sequence id
     */
    public int getVersionId();

    /**
     * The actual Item Backing this version
     * @return versions Item.
     */
    public Item getItem();   
}

