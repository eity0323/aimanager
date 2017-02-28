package com.sien.lib.share;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class AESUtil {

	public static String encryptPassword(String clearText) {
		if (!ShareConfig.isEncryptByAESForData) {
			return clearText;
		}
		try {
			DESKeySpec keySpec = new DESKeySpec(ShareConfig.DB_SECRET_KEY.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String encrypedPwd = Base64.encodeToString(cipher.doFinal(clearText.getBytes("UTF-8")), Base64.DEFAULT);
			return encrypedPwd;
		} catch (Exception e) {
		}
		return clearText;
	}

	public static String decryptPassword(String encryptedPwd) {
		if (!ShareConfig.isEncryptByAESForData) {
			return encryptedPwd;
		}
		try {
			DESKeySpec keySpec = new DESKeySpec(ShareConfig.DB_SECRET_KEY.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			byte[] encryptedWithoutB64 = Base64.decode(encryptedPwd, Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
			return new String(plainTextPwdBytes);
		} catch (Exception e) {
		}
		return encryptedPwd;
	}

}
