
package com.flaviolisboa.sistema.entidades;

import java.io.Serializable;

public interface Entidade<Id extends Serializable> extends Serializable {

    Id getId();
}
