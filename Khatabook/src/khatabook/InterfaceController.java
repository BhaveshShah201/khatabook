/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khatabook;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import khatabook.constant.Constants;
import khatabook.model.User;
import khatabook.util.GoogleDriveUtil;
import khatabook.util.IOUtil;
import khatabook.util.SyncGoogleDriveUtil;
import khatabook.util.ValidationUtil;

/**
 *
 * @author ME
 */
public class InterfaceController implements Initializable {

    @FXML
    private Hyperlink signUpLink;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private TextField mobileTxt;
    @FXML
    private PasswordField pwd;
    @FXML
    private Label errorMsg;

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void login(ActionEvent event) {
        try {
            String error = ValidationUtil.validateMobileNumber(mobileTxt.getText());
            error += "\n" + ValidationUtil.validatePwd(pwd.getText(), "");

            String hash = IOUtil.getMd5(mobileTxt.getText());
            error = error.trim();
            if (error.equalsIgnoreCase("")) {
                errorMsg.setText("");
                if (!IOUtil.checkOrMakeDirectory(Constants.PATH + Constants.SEPRATOR + hash)) {
                    errorMsg.setText("User " + mobileTxt.getText() + " Not Registered!!");
                } else {
                    FileInputStream fi = new FileInputStream(Constants.PATH + Constants.SEPRATOR + hash + Constants.SEPRATOR + hash);
                    User user = IOUtil.deserializeAndFetchObject(fi, hash, pwd.getText());
                    error = ValidationUtil.validateLoginPwd(mobileTxt.getText(), user.getMobileNumber(), "");
                }
            }
            if (error.equalsIgnoreCase("")) {
                System.out.println("successfully logged in!!");
            } else {
                errorMsg.setText(error);
            }
        } catch (IOException | ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            errorMsg.setText("Wrong password. Try again!!");
            Logger.getLogger(IOUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
            int x = process.waitFor();
            if (x == 0) {
                System.out.println("Connection Successful,Output was " + x);
                SyncGoogleDriveUtil.syncDataWithGoogleDrive();
            } else {
                System.out.println("Internet Not Connected,Output was " + x);
            }
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void signUpLoad(MouseEvent event) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            rightPane.getChildren().removeAll();
            rightPane.getChildren().setAll(fxml);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadForgetPassword(ActionEvent event) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("ForgetPassword.fxml"));
            rightPane.getChildren().removeAll();
            rightPane.getChildren().setAll(fxml);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void checkMobileNumber(KeyEvent event) {
        if (ValidationUtil.isMobileNumber(event.getCharacter(), mobileTxt.getText())) {
            event.consume();
        }

    }
}
