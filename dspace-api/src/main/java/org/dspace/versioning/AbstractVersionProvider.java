/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.*;

import java.sql.SQLException;

/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 */
public abstract class AbstractVersionProvider {


    protected void copyMetadata(Item itemNew, Item nativeItem){
        DCValue[] md = nativeItem.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        for (int n = 0; n < md.length; n++){


            if( (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("relation") &&  (md[n].qualifier!=null && md[n].qualifier.equals("haspart")) )
                    || (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("relation") &&  (md[n].qualifier!=null && md[n].qualifier.equals("ispartof")) )
                      || (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("identifier"))
                        || (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("relation") &&  (md[n].qualifier!=null && md[n].qualifier.equals("isversionof")) )
                         || (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("date") &&  (md[n].qualifier!=null && md[n].qualifier.equals("accessioned")) )
                          || (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("date") &&  (md[n].qualifier!=null && md[n].qualifier.equals("available")) )
                           || (md[n].schema.equals(MetadataSchema.DC_SCHEMA)  && md[n].element.equals("description") &&  (md[n].qualifier!=null && md[n].qualifier.equals("provenance")) ) )
                continue;


            itemNew.addMetadata(md[n].schema, md[n].element, md[n].qualifier, md[n].language,
            md[n].value);
        }
    }

    protected void createBundlesAndAddBitstreams(Item itemNew, Item nativeItem) throws SQLException, AuthorizeException {
        for(Bundle b : nativeItem.getBundles()){
            Bundle bundleNew = itemNew.createBundle(b.getName());
            bundleNew.setPrimaryBitstreamID(b.getPrimaryBitstreamID());

            for(Bitstream bitstream : b.getBitstreams()){
                bundleNew.addBitstream(bitstream);
            }
        }
    }
}
