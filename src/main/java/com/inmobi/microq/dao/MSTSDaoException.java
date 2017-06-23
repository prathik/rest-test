package com.inmobi.microq.dao;

import java.sql.SQLException;

/**
 * @author prathik.raj
 */
public class MSTSDaoException extends Exception {

    public MSTSDaoException(String s) {
        super(s);
    }

    public MSTSDaoException(Throwable e) {
        super(e);
    }
}
