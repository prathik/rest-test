package com.inmobi.microq;

import java.sql.SQLException;

/**
 * @author prathik.raj
 */
public class MSTestException extends Exception {

    public MSTestException(String s) {
        super(s);
    }

    public MSTestException(Throwable e) {
        super(e.getMessage());
    }
}
