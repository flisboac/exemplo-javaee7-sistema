
package com.flaviolisboa.sistema.constantes;

public class Constantes {

    public static final String NomeBundleMensagens = "mensagens";
    
    public static class Email {
        public static final int TamanhoMinimo = 2;
        public static final int Tamanho = 256;
        public static final String Regexp = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
    }
    
    public static class Nome {
        public static final int TamanhoMinimo = 2;
        public static final int Tamanho = 256;
    }
}
