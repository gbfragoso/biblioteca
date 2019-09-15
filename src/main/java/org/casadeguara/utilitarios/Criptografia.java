package org.casadeguara.utilitarios;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe responsável por criptografar textos
 * 
 * @author Gustavo
 * @since 3.0
 */
public class Criptografia {

    /**
     * Aplica um determinado algoritmo de criptografia em um texto
     * 
     * @param algoritmo Algoritmo a ser utilizado. Ex: SHA-512
     * @param senha Texto a ser criptografado
     * @return Texto criptografado
     */
    public String aplicar(String algoritmo, String senha) throws NoSuchAlgorithmException{
        byte[] text = senha.getBytes(StandardCharsets.UTF_8);
        byte[] digestedText = MessageDigest.getInstance(algoritmo).digest(text);

        return byteArrayToString(digestedText);
    }

    /**
     * Transforma um texto codificado em hexadecimal em uma string
     * @param digestedText Texto codificado
     * @return String resultante
     */
    private String byteArrayToString(byte[] digestedText) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : digestedText) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        return hexString.toString();
    }
    
}
