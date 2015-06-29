
package com.flaviolisboa.sistema.excecoes;

import javax.ejb.ApplicationException;

@ApplicationException
public class SistemaException extends RuntimeException {

    public SistemaException() {
    }

    public SistemaException(String message) {
        super(message);
    }

    public SistemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SistemaException(Throwable cause) {
        super(cause);
    }

    public SistemaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
