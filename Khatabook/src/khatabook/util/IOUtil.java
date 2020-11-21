/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khatabook.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import khatabook.constant.Constants;
import khatabook.model.User;

/**
 *
 * @author ME
 */
public class IOUtil {

    public static void makeParentDirectory() {
        checkOrMakeDirectory(Constants.PATH);
    }

    public static boolean checkOrMakeDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
            return false;
        }
        return true;
    }

    public static void serializeAndSaveObject(FileOutputStream fos, User user, String hash) throws IOException {
        // Serialize and save the user 
        try {
            // generate symmetric key
            SecretKey SecretKey = new SecretKeySpec(Arrays.copyOf(user.getPassword().getBytes(), 16), Constants.KEY_ALGO);
            // create cipher
            Cipher cipher = Cipher.getInstance(SecretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, SecretKey);

            // create sealed object
            SealedObject sealedUser = new SealedObject(user, cipher);

            // Create stream
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            CipherOutputStream cos = new CipherOutputStream(bos, cipher);
            try (ObjectOutputStream oos = new ObjectOutputStream(cos)) {
                oos.writeObject(sealedUser);
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            Logger.getLogger(IOUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            fos.close();
        }
    }

    public static User deserializeAndFetchObject(FileInputStream fi, String hash, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
        // Serialize and save the user 
        User user = new User();
        try {
            
            // generate symmetric key
            SecretKey SecretKey = new SecretKeySpec(Arrays.copyOf(password.getBytes(), 16), Constants.KEY_ALGO);

            // turn the mode of cipher to decryption
            Cipher cipher = Cipher.getInstance(SecretKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, SecretKey);

            // create stream
            CipherInputStream cipherInputStream = new CipherInputStream(fi, cipher);
            ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
            SealedObject sealedObject = (SealedObject) inputStream.readObject();
            user = (User) sealedObject.getObject(cipher);
        }
        finally{
            fi.close();
        }
        return user;
    }

    public static String getMd5(String input) {
        try {
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value 
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
