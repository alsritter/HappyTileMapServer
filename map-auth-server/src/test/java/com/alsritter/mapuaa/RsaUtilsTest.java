package com.alsritter.mapuaa;

import com.alsritter.common.util.RsaUtils;
import org.junit.jupiter.api.Test;

class RsaUtilsTest {

    private String privateFilePath = "C:\\Users\\alsritter\\Desktop\\HappyTileMap\\HappyTileMapServer\\map-auth-server\\src\\main\\resources\\rsa\\id_key_rsa";
    private String publicFilePath = "C:\\Users\\alsritter\\Desktop\\HappyTileMap\\HappyTileMapServer\\map-auth-server\\src\\main\\resources\\rsa\\id_key_rsa.pub";

    private String SECRET = "VGhpcyBpcyBhbiBBbHNyaXR0ZXIgcHJvamVjdA==";

    @Test
    void generateKey() throws Exception {
        RsaUtils.generateKey(publicFilePath, privateFilePath, SECRET, 2048);
    }

    @Test
    void getPublicKey() throws Exception {
        System.out.println(RsaUtils.getPublicKey(publicFilePath));
    }

    @Test
    void getPrivateKey() throws Exception {
        System.out.println(RsaUtils.getPrivateKey(privateFilePath));
    }
}
