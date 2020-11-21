/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khatabook.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author ME
 */



public class User implements Externalizable{
    
    private static final long serialVersionUID = 6529685098267757690L;
    
    private String mobileNumber;
    
    private String email;
    
    private String password;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        oo.writeObject(this.mobileNumber);
        oo.writeObject(this.email);
        oo.writeObject(this.password);
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        this.mobileNumber = (String)oi.readObject();
        this.email = (String)oi.readObject();
        this.password  = (String)oi.readObject();
    }

    @Override
    public String toString() {
        return "User{" + "mobileNumber=" + mobileNumber + ", email=" + email + ", password=" + password + '}';
    }
}
