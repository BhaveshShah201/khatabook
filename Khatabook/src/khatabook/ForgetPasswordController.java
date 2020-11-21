/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khatabook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
import khatabook.util.IOUtil;
import khatabook.util.ValidationUtil;

/**
 *
 * @author ME
 */
public class ForgetPasswordController {

    @FXML
    private AnchorPane pane;
    @FXML
    private Button backBtn;
    @FXML
    private PasswordField pwd;
    @FXML
    private TextField mobileTxt;
    @FXML
    private PasswordField conPwd;
    @FXML
    private Label errorMsg;
    @FXML
    private PasswordField oldPwd;

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void backWndow(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
            Khatabook.stage.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void resetPassword(ActionEvent event) {
        try {
            String error = ValidationUtil.validateMobileNumber(mobileTxt.getText());
            error += "\n" + ValidationUtil.validatePwd(oldPwd.getText(), "old");
            error += "\n" + ValidationUtil.validatePwd(pwd.getText(), "");
            error += "\n" + ValidationUtil.validatePwd(conPwd.getText(), "confirm");

            String hash = IOUtil.getMd5(mobileTxt.getText());
            error = error.trim();
            User user = null;
            if (error.equalsIgnoreCase("")) {
                errorMsg.setText("");
                if (!IOUtil.checkOrMakeDirectory(Constants.PATH + Constants.SEPRATOR + hash)) {
                    errorMsg.setText("User " + mobileTxt.getText() + " Not Registered!!");
                } else {
                    FileInputStream fi = new FileInputStream(Constants.PATH + Constants.SEPRATOR + hash + Constants.SEPRATOR + hash);
                    user = IOUtil.deserializeAndFetchObject(fi, hash, pwd.getText());
                    error = ValidationUtil.validateLoginPwd(oldPwd.getText(), user.getMobileNumber(), "old");
                }
            }
            if (error.equalsIgnoreCase("") && null != user) {
                user.setPassword(pwd.getText());
                FileOutputStream fos = new FileOutputStream(Constants.PATH + Constants.SEPRATOR + hash + Constants.SEPRATOR + hash);
                IOUtil.serializeAndSaveObject(fos, user, hash);
            } else {
                errorMsg.setText(error);
            }
        } catch (IOException | ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            errorMsg.setText("Wrong password. Try again!!");
            Logger.getLogger(IOUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void checkMobileNumber(KeyEvent event) {
        if (ValidationUtil.isMobileNumber(event.getCharacter(), mobileTxt.getText())) {
            event.consume();
        }
    }

}
