package org.casadeguara.testes;

import java.security.NoSuchAlgorithmException;
import org.casadeguara.utilitarios.Criptografia;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CriptografiaTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private Criptografia criptografia;
    
    @Before
    public void setup() {
        criptografia = new Criptografia();
    }
    
    @Test
    public void criptografandoSenhaComMD5() throws NoSuchAlgorithmException {
        String senha = "123";
        String resultado = criptografia.aplicar("MD5", senha);

        Assert.assertEquals(
                    "202CB962AC59075B964B07152D234B70", 
                    resultado);
    }
    
    @Test
    public void criptografandoSenhaComSHA256() throws NoSuchAlgorithmException {
        String senha = "123";
        String resultado = criptografia.aplicar("SHA-256", senha);
        
        Assert.assertEquals(
                "A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3", 
                resultado);
    }
    
    @Test
    public void criptografandoSenhaComSHA512() throws NoSuchAlgorithmException {
        String senha = "123";
        String resultado = criptografia.aplicar("SHA-512", senha);
        
        Assert.assertEquals(
                "3C9909AFEC25354D551DAE21590BB26E38D53F2173B8D3DC3EEE4C047E7AB1C1EB8B85103E3BE7BA613B31BB5C9C36214DC9F14A42FD7A2FDB84856BCA5C44C2", 
                resultado);
    }
    
    @Test
    public void criptografandoSenhaAlgoritmoDesconhecido() throws NoSuchAlgorithmException {
        thrown.expect(NoSuchAlgorithmException.class);
        criptografia.aplicar("y", "123");
    }
}
