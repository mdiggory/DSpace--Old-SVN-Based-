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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.sql.SQLException;
import java.util.List;

/**
 * The main service class used to reserver, register and resolve identifiers
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class IdentifierServiceImpl implements IdentifierService {

    private List<IdentifierProvider> providers;

    private HandleIdentifierProvider handleService;

    @Autowired
    @Required
    public void setHandleService(HandleIdentifierProvider handleService)
    {
       this.handleService = handleService;
       this.handleService.setParentService(this);
    }

   @Autowired
   @Required
   public void setProviders(List<IdentifierProvider> providers)
   {
       this.providers = providers;

       for(IdentifierProvider p : providers)
       {
           p.setParentService(this);
       }
   }

    /**
     * Reserves identifiers for the item
     * @param context dspace context
     * @param item dspace item
     */
    public void reserve(Context context, Item item) throws AuthorizeException, SQLException, IdentifierException {
        if(handleService != null){
            handleService.mint(context, item);
        }
        
        // Next resolve all other services
        for (IdentifierProvider service : providers){

            if(service.getClass().equals(HandleIdentifierProvider.class)) continue;

            service.mint(context, item);
        }
        //Update our item
        item.update();
    }

    public String register(Context context, Item item) throws AuthorizeException, SQLException, IdentifierException {
        if(handleService != null){
            handleService.register(context, item);
        }

        //We need to commit our context because one of the providers might require the handle created above
        // Next resolve all other services
        for (IdentifierProvider service : providers){
            if(!(service instanceof HandleIdentifierProvider))
                service.register(context, item);
        }
        //Update our item
        item.update();

        return null;
    }

    public DSpaceObject resolve(Context context, String identifier) throws IdentifierNotFoundException, IdentifierNotResolvableException{
        for (IdentifierProvider service : providers) {
            if(service.supports(identifier))
            {
                DSpaceObject result = service.resolve(context, identifier);
                if (result != null)
                    return result;
            }

        }
        return null;
    }


    public void delete(Context context, Item item) throws AuthorizeException, SQLException, IdentifierException {
       for (IdentifierProvider service : providers) {
            service.delete(context, item);
        }
    }

}
