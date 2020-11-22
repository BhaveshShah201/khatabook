/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khatabook;

import java.io.FileOutputStream;
import khatabook.util.ValidationUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.Timer;
import khatabook.constant.Constants;
import khatabook.model.User;
import khatabook.util.IOUtil;

/**
 * FXML Controller class
 *
 * @author ME
 */
public class SignUpController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private Button backBtn;
    @FXML
    private TextField emailTxt;
    @FXML
    private PasswordField pwd;
    @FXML
    private TextField mobileTxt;
    @FXML
    private PasswordField conPwd;
    @FXML
    private Label errorMsg;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void backWindow(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
            Khatabook.stage.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void register(ActionEvent event) {

        try {
            String error = ValidationUtil.validateMobileNumber(mobileTxt.getText());
            error += "\n" + ValidationUtil.validateEmail(emailTxt.getText());
            error += "\n" + ValidationUtil.validatePwd(pwd.getText(), "");
            error += "\n" + ValidationUtil.validatePwd(pwd.getText(), "confirm");
            error += "\n" + ValidationUtil.validateConfirmPwd(conPwd.getText(), pwd.getText());

            error = error.trim();
            if (error.equalsIgnoreCase("")) {
                errorMsg.setText("");
                User user = new User();
                user.setMobileNumber(mobileTxt.getText());
                user.setEmail(emailTxt.getText());
                user.setPassword(pwd.getText());
                String hash = IOUtil.getMd5(user.getMobileNumber());
                if (!IOUtil.checkOrMakeDirectory(Constants.PATH + Constants.SEPRATOR + hash)) {
                    FileOutputStream fos = new FileOutputStream(Constants.PATH + Constants.SEPRATOR + hash + Constants.SEPRATOR + hash);
                    IOUtil.serializeAndSaveObject(fos, user, hash);
                    errorMsg.setText(mobileTxt.getText() + " Successfully Registered!!");
                    Timer timer = new Timer(2000, (java.awt.event.ActionEvent e) -> {
                        backWindow(null);
                    });
                    timer.start();
                } else {
                    errorMsg.setText(mobileTxt.getText() + " Already Registered!!");
                }
            } else {
                errorMsg.setText(error);
            }
        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void checkMobileNumber(KeyEvent event) {
        if (ValidationUtil.isMobileNumber(event.getCharacter(), mobileTxt.getText())) {
            event.consume();
        }
    }

}
