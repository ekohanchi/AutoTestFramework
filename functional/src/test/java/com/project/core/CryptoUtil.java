package com.project.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CryptoUtil {
	private static String CONFIGPASSWORDS = "configPasswords";
	private static String STORE_PASSWORD = "STORE_PASSWORD";
	private static String KEY_PASSWORD = "KEY_PASSWORD";
	private static String USER_HOME = "user.home";
	private static String KEYSTORE_FILE = ".keystore";
	private static String VAULT_FILE = ".vault";
	private static boolean initiated = false;
	private static Cipher encrypt = null;
	private static Cipher decrypt = null;
	private static final String MARKER = "ENC(%s)";
	private static final Pattern MARKER_PATTERN = Pattern.compile("^ENC\\((.+)\\)$");
	private static Properties properties = new Properties();

	private static void initiate() {
		if (!initiated) {
			synchronized (CryptoUtil.class) {
				if (!initiated) {
					try {
						KeyStore keyStore = loadKeyStore();
						Key key = keyStore.getKey(CONFIGPASSWORDS, getKeyPassword());
						encrypt = Cipher.getInstance("AES");
						encrypt.init(Cipher.ENCRYPT_MODE, key);
						decrypt = Cipher.getInstance("AES");
						decrypt.init(Cipher.DECRYPT_MODE, key);
						initiated = true;
					} catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException
							| NoSuchPaddingException | InvalidKeyException ex) {
						throw new RuntimeException("ERROR loading configPasswords file: ", ex);
					}
				}
			}

		}
	}

	private static KeyStore loadKeyStore() {
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("JCEKS");
			File file = new File(System.getProperty(USER_HOME), KEYSTORE_FILE);
			if (file.exists()) {
				try (InputStream is = new FileInputStream(file)) {
					keyStore.load(is, getStorePassword());
				}
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return keyStore;
	}

	private static byte[] decodeBase64(String string) {
		Matcher matcher = MARKER_PATTERN.matcher(string);
		if (matcher.matches()) {
			return Base64.getDecoder().decode(matcher.group(1));
		} else {
			throw new RuntimeException(
					"String provided [" + string + "] is not encrypted by " + CryptoUtil.class.toString());
		}
	}

	public static String decryptString(String encryptedString) {
		initiate();
		try {
			return new String(decrypt.doFinal(decodeBase64(encryptedString)));
		} catch (Exception e) {
			throw new RuntimeException("Unable to decrypt string provided: " + encryptedString, e);
		}
	}

	public static String encryptString(String stringToEncrypt) {
		initiate();
		try {
			byte[] encryptedBytes = encrypt.doFinal(stringToEncrypt.getBytes());
			return String.format(MARKER, Base64.getEncoder().encodeToString(encryptedBytes));
		} catch (Exception e) {
			throw new RuntimeException("Unable to encrypt text provided: " + stringToEncrypt, e);
		}
	}

	private static char[] getStorePassword() {
		try {
			File file = new File(System.getProperty(USER_HOME), VAULT_FILE);
			properties.load(new FileReader(file));
			return properties.getProperty(STORE_PASSWORD).toCharArray();
		} catch (IOException e) {
			// We should fail immediately, we can't work without the .valut file
			throw new RuntimeException(e);
		}
	}
	
	private static char[] getKeyPassword() {
		try {
			File file = new File(System.getProperty("user.home"), ".sqevault");
			properties.load(new FileReader(file));
			return properties.getProperty(KEY_PASSWORD).toCharArray();
		} catch (IOException e) {
			// We should fail immediately, we can't work without the .valut file
			throw new RuntimeException(e);
		}
	}
}
