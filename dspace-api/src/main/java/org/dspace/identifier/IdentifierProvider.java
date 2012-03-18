/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.identifier;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public abstract class IdentifierProvider {

    protected IdentifierService parentService;

    protected ConfigurationService configurationService;

    @Autowired
    @Required
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setParentService(IdentifierService parentService) {
        this.parentService = parentService;
    }

    public abstract boolean supports(String identifier);

    public abstract String register(Context context, DSpaceObject item) throws IdentifierException;

    public abstract String mint(Context context, DSpaceObject dso) throws IdentifierException;

    public abstract DSpaceObject resolve(Context context, String identifier, String... attributes) throws IdentifierNotFoundException, IdentifierNotResolvableException;;

    public abstract void delete(Context context, DSpaceObject dso) throws IdentifierException;
}
