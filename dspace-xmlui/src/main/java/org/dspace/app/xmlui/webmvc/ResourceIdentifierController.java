/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.webmvc;

import org.dspace.app.xmlui.utils.ContextUtil;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.identifier.IdentifierNotFoundException;
import org.dspace.identifier.IdentifierNotResolvableException;
import org.dspace.identifier.IdentifierService;
import org.dspace.utils.DSpace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;


/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */

@Controller
@RequestMapping("/resource")
public class ResourceIdentifierController {

    public static final String DSPACE_OBJECT = "dspace.object";
    private static final String RESOURCE = "/resource";
    private static final String METS = "mets";
    private static final String DRI = "DRI";

    @RequestMapping("/**")
    public String processHandle(HttpServletRequest request) {
        String resourceIdentifier=null;
        try {

            String requestUri = request.getRequestURI().toString();

            resourceIdentifier = requestUri.substring(requestUri.indexOf(RESOURCE) + RESOURCE.length() + 1);

            Context context = ContextUtil.obtainContext(request);

            IdentifierService dis = new DSpace().getSingletonService(IdentifierService.class);

            if (dis == null)
                throw new RuntimeException("Cannot instantiate IdentifierService. Problem with spring configuration!");

            DSpaceObject dso = dis.resolve(context, resourceIdentifier);

            if (dso == null) throw new RuntimeException("Cannot find Item!");

            request.setAttribute(DSPACE_OBJECT, dso);

            return "forward:/handle/" + dso.getHandle();

        } catch (SQLException e) {
            return "forward:/error";

        } catch (IdentifierNotResolvableException e) {
            return "forward:/tombstone";

        } catch (IdentifierNotFoundException e) {
            request.setAttribute("identifier", resourceIdentifier);
            return "forward:/identifier-not-found";
        }
    }

    @RequestMapping("/**/mets.xml")
    public String processMETSHandle(HttpServletRequest request) {
        try {

            String requestUri = request.getRequestURI().toString();

            String resourceIdentifier = requestUri.substring(requestUri.indexOf(RESOURCE) + RESOURCE.length() + 1);
            resourceIdentifier = resourceIdentifier.substring(0, resourceIdentifier.indexOf(METS) - 1);

            Context context = ContextUtil.obtainContext(request);

            IdentifierService dis = new DSpace().getSingletonService(IdentifierService.class);

            DSpaceObject dso = dis.resolve(context, resourceIdentifier);

            if (dso == null) return null;

            request.setAttribute(DSPACE_OBJECT, dso);

            return "forward:/metadata/handle/" + dso.getHandle() + "/mets.xml";

        } catch (SQLException e) {
            return "forward:/error";
        } catch (IdentifierNotResolvableException e) {
            return "forward:/tombstone";

        } catch (IdentifierNotFoundException e) {
            return "forward:/identifier-not-found";
        }
    }

    @RequestMapping("/**/DRI")
    public String processDRIHandle(HttpServletRequest request) {
        try {

            String requestUri = request.getRequestURI().toString();

            String resourceIdentifier = requestUri.substring(requestUri.indexOf(RESOURCE) + RESOURCE.length() + 1);
            resourceIdentifier = resourceIdentifier.substring(0, resourceIdentifier.indexOf(DRI) - 1);

            Context context = ContextUtil.obtainContext(request);

            IdentifierService dis = new DSpace().getSingletonService(IdentifierService.class);

            DSpaceObject dso = dis.resolve(context, resourceIdentifier);

            if (dso == null) return null;

            request.setAttribute(DSPACE_OBJECT, dso);

            return "forward:/DRI/handle/" + dso.getHandle();
        } catch (SQLException e) {
            return "forward:/error";

        } catch (IdentifierNotResolvableException e) {
            return "forward:/tombstone";

        } catch (IdentifierNotFoundException e) {
            return "forward:/identifier-not-found";
        }
    }
}
