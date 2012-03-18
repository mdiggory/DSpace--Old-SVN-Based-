/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.identifier;

/**
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class IdentifierNotFoundException extends IdentifierException {

    public IdentifierNotFoundException() {
        super();
    }

    public IdentifierNotFoundException(String message) {
        super(message);
    }

    public IdentifierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdentifierNotFoundException(Throwable cause) {
        super(cause);
    }
}
