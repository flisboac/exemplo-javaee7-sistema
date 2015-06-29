
package com.flaviolisboa.sistema.excecoes;

import javax.ejb.ApplicationException;

@ApplicationException
public class ConsultaNaoImplementadaException extends PersistenciaException {

    public ConsultaNaoImplementadaException() {
    }

    public ConsultaNaoImplementadaException(String message) {
        super(message);
    }

    public ConsultaNaoImplementadaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsultaNaoImplementadaException(Throwable cause) {
        super(cause);
    }

    public ConsultaNaoImplementadaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
