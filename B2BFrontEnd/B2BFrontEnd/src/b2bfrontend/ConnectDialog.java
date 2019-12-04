package b2bfrontend;

import b2baqjms.OracleDBConnection;

import java.awt.Dimension;
import java.awt.Rectangle;

import java.awt.TextField;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConnectDialog extends JDialog {
    private JButton jBtnConnect = new JButton();
    private JLabel jLblUseName = new JLabel();
    private JLabel jLblPassword = new JLabel();
    private JLabel jLblConnectString = new JLabel();
    private JTextField jTFUserName = new JTextField();
    private Connection connection;
    private String message = new String();
    private String status = new String();
    private JComboBox jCBConnect = new JComboBox();
    private JPasswordField passwordField = new JPasswordField();
    B2BProperties props = new B2BProperties();

    public ConnectDialog() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setTitle("Connect");
        this.setModal(true);
        this.setLayout(null);
        this.setSize(new Dimension(400, 235));
        jBtnConnect.setText("Connect");
        jBtnConnect.setToolTipText("Connect to Database");
        jBtnConnect.setBounds(new Rectangle(140, 140, 85, 23));
        jBtnConnect.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        connect(ae);
                    }
                });
        jLblUseName.setText("User Name");
        jLblUseName.setBounds(new Rectangle(10, 25, 85, 15));
        jLblPassword.setText("Password");
        jLblPassword.setBounds(new Rectangle(10, 65, 85, 15));
        jLblConnectString.setText("Connect String");
        jLblConnectString.setBounds(new Rectangle(10, 100, 85, 15));
        jTFUserName.setText("b2b");
        jTFUserName.setBounds(new Rectangle(100, 25, 270, 20));
        jCBConnect.setBounds(new Rectangle(100, 100, 270, 21));
        passwordField.setBounds(new Rectangle(100, 62, 270, 21));
        String message = new String();
        this.getContentPane().add(passwordField, null);
        this.getContentPane().add(jCBConnect, null);
        this.getContentPane().add(jTFUserName, null);
        this.getContentPane().add(jLblConnectString, null);
        this.getContentPane().add(jLblPassword, null);
        this.getContentPane().add(jLblUseName, null);
        this.getContentPane().add(jBtnConnect, null);
        initJBConnect();
    }


    private void initJBConnect() {
        //jCBConnect.addItem(props.getConnect(Locale.getDefault(), 1));
        //jCBConnect.addItem(props.getConnect(Locale.getDefault(), 2));
        int i = 1;
        boolean klaar;
        String connect;
        do {
            connect = props.getConnect(Locale.getDefault(), i);
            if (connect == null) {
                klaar = true;
            } else {
                i++;
                klaar = false;
                jCBConnect.addItem(connect);
            }
        } while (!klaar);

    }

    public void setJTFUserName(JTextField jTFUserName) {
        this.jTFUserName = jTFUserName;
    }

    public JTextField getJTFUserName() {
        return jTFUserName;
    }

    public String getUserName() {
        return jTFUserName.getText();
    }

    public void setPassword(JPasswordField password) {
        this.passwordField = password;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public String getPassword() {
        String password = new String(passwordField.getPassword());
        return password;
    }


    public String getConnectString() {
        String name=(String)jCBConnect.getSelectedItem();
        String connectString = props.getProperty(Locale.getDefault(), name);
        return connectString;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public Connection getConnection() {
        return connection;
    }

    void connect(ActionEvent e) {
        try {
            connection = 
                    OracleDBConnection.getConnection(getUserName(), getPassword(), 
                                                     getConnectString());
            message = "Connected";
            status = "S";
        } catch (SQLException se) {
            message = se.getMessage();
            status = "E";
        }
        this.setVisible(false);
    }


}
