package com.flaviolisboa.sistema.constantes;

public enum Ordenacao {

    Natural(null),
    Ascendente("asc"),
    Descendente("desc");
    
    private final String valorJpa;

    private Ordenacao(String valorJpa) {
        this.valorJpa = valorJpa;
    }

    public String getValorJpa() {
        return valorJpa;
    }
    
}
