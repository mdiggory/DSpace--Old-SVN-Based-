/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.identifier;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public interface IdentifierService {

    void reserve(Context context, Item item) throws AuthorizeException, SQLException, IdentifierException;

    String register(Context context, Item item) throws AuthorizeException, SQLException, IdentifierException;

    DSpaceObject resolve(Context context, String identifier) throws IdentifierNotFoundException, IdentifierNotResolvableException;

    void delete(Context context, Item item) throws AuthorizeException, SQLException, IdentifierException;
}
