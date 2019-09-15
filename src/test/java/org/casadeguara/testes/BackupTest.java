package org.casadeguara.testes;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

public class BackupTest {
    
    @Test
    public void creatingHotBackup() {
        String filename = "biblioteca_";
        String date = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
        
        Assert.assertTrue(new File("backup/"+filename+date+".backup").exists());
    }
}
