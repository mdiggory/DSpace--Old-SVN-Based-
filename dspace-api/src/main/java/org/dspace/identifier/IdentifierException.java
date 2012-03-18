/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.identifier;

/**
 * An exception used by the identifier framework
 *
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class IdentifierException extends Exception{

    public IdentifierException() {
        super();
    }

    public IdentifierException(String message) {
        super(message);
    }

    public IdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdentifierException(Throwable cause) {
        super(cause);
    }
}
