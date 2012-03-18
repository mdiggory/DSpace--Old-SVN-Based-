/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.identifier;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Constants;
import org.dspace.core.Context;

import java.sql.SQLException;


/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class InternalIdentifierProvider extends IdentifierProvider {

    private static final String SLASH = "/";
    private static final String TOMBSTONE = "tombstone";

    private String[] supportedPrefixes = new String[]{"info:dspace/", "dspace:/"};


    public boolean supports(String identifier)
    {
        for(String prefix : supportedPrefixes){
            if(identifier.startsWith(prefix))
                return true;
        }

        return false;
    }




    public String register(Context context, DSpaceObject item) throws IdentifierException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String mint(Context context, DSpaceObject dso) throws IdentifierException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    //identifier = info:dspace/item/10
    public DSpaceObject resolve(Context context, String identifier, String... attributes) throws IdentifierNotFoundException, IdentifierNotResolvableException{

        if(identifier.contains(TOMBSTONE))
            throw new IdentifierNotResolvableException();

        // item/10
        String temp = identifier.substring(identifier.indexOf(SLASH)+1);

        String[] typeAndId = temp.split(SLASH);

        try {
            return DSpaceObject.find(context, Constants.getTypeID(typeAndId[0]), Integer.parseInt(typeAndId[1]));
        } catch (SQLException e) {
            throw new RuntimeException("Cannot find DspaceObject: " + identifier);
        }
    }

    public void delete(Context context, DSpaceObject dso) throws IdentifierException {
        //To change body of implemented methods use File | Settings | File Templates.
    }





}
