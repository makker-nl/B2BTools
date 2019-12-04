package cyclonefrontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import javax.swing.JTextField;


public class FrameGenMMD extends JFrame {
    private CycloneProperties props = new CycloneProperties();
    private CPAFile cpaFile;
    private Vector fromPartyIDs;
    private Vector toPartyIDs;
    private NodeSelection fromCollabRoles;
    private NodeSelection toCollabRoles;
    private NodeSelection serviceBindings;
    private Vector services = new Vector();
    private SimpleDateFormat formatter = 
        new SimpleDateFormat("yyyyMMddhhmmss");

    private BorderLayout layoutMain = new BorderLayout();
    private JPanel panelCenter = new JPanel();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JLabel statusBar = new JLabel();
    private JButton submitMessage = new JButton();
    private JTextArea messageContent = new JTextArea();
    private JScrollPane messageContentSp;
    private JComboBox fromParty = new JComboBox();
    private JLabel lblFromParty = new JLabel();
    private JLabel lblToParty = new JLabel();
    private JComboBox toParty = new JComboBox();
    private JComboBox fromPartyId = new JComboBox();
    private JLabel lblFromPartyId = new JLabel();
    private JComboBox toPartyId = new JComboBox();
    private JLabel lblToPartyId = new JLabel();
    private JComboBox fromRole = new JComboBox();
    private JLabel lblFromRole = new JLabel();
    private JLabel lblToRole = new JLabel();
    private JComboBox toRole = new JComboBox();
    private JLabel lblService = new JLabel();
    private JComboBox service = new JComboBox();
    private JComboBox action = new JComboBox();
    private JLabel lblAction = new JLabel();
    private JTextField refToMessageId = new JTextField();
    private JLabel lblRefToMessageId = new JLabel();
    private JLabel lblConversationId = new JLabel();
    private JTextField conversationId = new JTextField();
    private JButton loadMessage = new JButton();
    private JFileChooser fileChooser;

    public FrameGenMMD() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFromParty(Vector partyNames) {
        String name;
        for (int i = 0; i < partyNames.size(); i++) {
            name = (String)partyNames.elementAt(i);
            fromParty.addItem(name);
        }
        fromParty.repaint();
        fromParty.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent ie) {
                        fromPartySelected(ie);
                    }
                });
    }

    private void initFromPartyId(int fromPartyIndex) {
        fromPartyId.removeAllItems();
        fromPartyIDs = cpaFile.getPartyIDsVec(fromPartyIndex);
        int fromIndex = fromPartyIDs.indexOf(fromPartyIDs.firstElement());
        int toIndex = fromPartyIDs.indexOf(fromPartyIDs.lastElement());
        PartyID partyId;
        for (int i = fromIndex; i <= toIndex; i++) {
            partyId = (PartyID)fromPartyIDs.elementAt(i);
            fromPartyId.addItem(partyId.getDisplay());
        }
    }

    private void initFromRoles(int fromPartyIndex) {
        fromRole.removeAllItems();
        fromCollabRoles = cpaFile.getCollabRoles(fromPartyIndex);
        if (fromCollabRoles.getNodesSelected() > 0) {
            NodeSelected collabRole = fromCollabRoles.getFirstNodeSel();
            String roleName = cpaFile.getRoleName(collabRole);
            fromRole.addItem(roleName);
            while (fromCollabRoles.hasNextNode()) {
                collabRole = fromCollabRoles.getNextNodeSel();
                roleName = cpaFile.getRoleName(collabRole);
                fromRole.addItem(roleName);
            }
        }
        fromRole.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent ie) {
                        fromRoleSelected(ie);
                    }
                });
    }

    private void initService(NodeSelected fromCollabRole) {
        serviceBindings = cpaFile.getServiceBindings(fromCollabRole);
        service.removeAllItems();
        services.removeAllElements();
        if (serviceBindings.getNodesSelected() > 0) {
            NodeSelected serviceBinding = serviceBindings.getFirstNodeSel();
            CPAService cpaService = cpaFile.getService(serviceBinding);
            service.addItem(cpaService.getDisplayName());
            services.addElement(cpaService);
            while (serviceBindings.hasNextNode()) {
                serviceBinding = serviceBindings.getNextNodeSel();
                cpaService = cpaFile.getService(serviceBinding);
                service.addItem(cpaService.getDisplayName());
                services.addElement(cpaService);
            }
        }
        service.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent ie) {
                        serviceSelected(ie);
                    }
                });
    }

    private void initAction(NodeSelected serviceBinding) {
        action.removeAllItems();
        Vector actionNames = cpaFile.getActionNames(serviceBinding);
        String name;
        for (int i = 0; i < actionNames.size(); i++) {
            name = (String)actionNames.elementAt(i);
            action.addItem(name);
        }

    }

    public void fromPartySelected(ItemEvent ie) {
        int fromPartyIndex = fromParty.getSelectedIndex();
        int stateChange = ie.getStateChange();
        if (stateChange == ie.SELECTED && fromPartyIndex > -1) {
            try {
                initFromPartyId(fromPartyIndex);
                initFromRoles(fromPartyIndex);
            } catch (Exception initException) {
                statusBar.setText(initException.getMessage());
            }
        }
    }

    public void fromRoleSelected(ItemEvent ie) {
        int fromRoleIndex = fromRole.getSelectedIndex();
        int stateChange = ie.getStateChange();
        if (stateChange == ie.SELECTED && fromRoleIndex > -1) {
            try {
                initService(fromCollabRoles.getNodeByIDSel(fromRoleIndex));
            } catch (Exception initException) {
                statusBar.setText(initException.getMessage());
            }
        }
    }

    public void serviceSelected(ItemEvent ie) {
        int serviceIndex = service.getSelectedIndex();
        int stateChange = ie.getStateChange();
        if (stateChange == ie.SELECTED && serviceIndex > -1) {
            try {
                NodeSelected ServiceBinding = 
                    serviceBindings.getNodeByIDSel(serviceIndex);
                initAction(ServiceBinding);
            } catch (Exception initException) {
                statusBar.setText(initException.getMessage());
            }
        }
    }

    private void initToParty(Vector partyNames) {
        String name;
        for (int i = 0; i < partyNames.size(); i++) {
            name = (String)partyNames.elementAt(i);
            toParty.addItem(name);
        }
        toParty.setSelectedIndex(partyNames.indexOf(partyNames.lastElement()));
        toParty.repaint();
        toParty.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent ie) {
                        toPartySelected(ie);
                    }
                });
    }

    private void initToPartyId(int toPartyIndex) {
        toPartyId.removeAllItems();
        toPartyIDs = cpaFile.getPartyIDsVec(toPartyIndex);
        int fromIndex = toPartyIDs.indexOf(toPartyIDs.firstElement());
        int toIndex = toPartyIDs.indexOf(toPartyIDs.lastElement());
        PartyID partyId;
        for (int i = fromIndex; i <= toIndex; i++) {
            partyId = (PartyID)toPartyIDs.elementAt(i);
            toPartyId.addItem(partyId.getDisplay());
        }
    }

    private void initToRoles(int toPartyIndex) {
        toRole.removeAllItems();
        toCollabRoles = cpaFile.getCollabRoles(toPartyIndex);
        if (fromCollabRoles.getNodesSelected() > 0) {
            NodeSelected collabRole = toCollabRoles.getFirstNodeSel();
            String roleName = cpaFile.getRoleName(collabRole);
            toRole.addItem(roleName);
            while (toCollabRoles.hasNextNode()) {
                collabRole = toCollabRoles.getNextNodeSel();
                roleName = cpaFile.getRoleName(collabRole);
                toRole.addItem(roleName);
            }
        }
    }

    public void toPartySelected(ItemEvent ie) {
        int toPartyIndex = toParty.getSelectedIndex();
        int stateChange = ie.getStateChange();
        if (stateChange == ie.SELECTED && toPartyIndex > -1) {
            try {
                initToPartyId(toPartyIndex);
                initToRoles(toPartyIndex);
            } catch (Exception initException) {
                statusBar.setText(initException.getMessage());
            }
        }
    }

    private void processCPA() {
        String cpaFilePath = props.getCPAFilePath();
        String error;
        if (cpaFilePath == null) {
            error = "CPA Filepath property not set or property file could not be found!";
        } else {
            cpaFile = new CPAFile(cpaFilePath);
            error = cpaFile.getError();
        }
        if (error == null) {
            Vector partyNames = cpaFile.getPartyNames();
            initFromParty(partyNames);
            int fromPartyIndex = fromParty.getSelectedIndex();
            initFromPartyId(fromPartyIndex);
            initFromRoles(fromPartyIndex);
            initToParty(partyNames);
            int toPartyIndex = toParty.getSelectedIndex();
            initToPartyId(toPartyIndex);
            initToRoles(toPartyIndex);
            initService(fromCollabRoles.getNodeByIDSel(fromRole.getSelectedIndex()));
            NodeSelected ServiceBinding = 
                serviceBindings.getNodeByIDSel(service.getSelectedIndex());
            initAction(ServiceBinding);
        } else {
            statusBar.setText(error);
        }
    }

    private void jbInit() throws Exception {
        this.setJMenuBar(menuBar);
        this.getContentPane().setLayout(layoutMain);
        panelCenter.setLayout(null);
        this.setSize(new Dimension(1028, 714));
        this.setTitle("Cyclone Swing FrontEnd");
        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        fileExit_ActionPerformed(ae);
                    }
                });
        menuHelp.setText("Help");
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        helpAbout_ActionPerformed(ae);
                    }
                });
        statusBar.setText("");
        submitMessage.setText("Submit Message");
        submitMessage.setBounds(new Rectangle(565, 600, 155, 25));
        submitMessage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        submitMessage_actionPerformed(e);
                    }
                });
        messageContent.setBounds(new Rectangle(130, 260, 865, 410));
        messageContentSp = new JScrollPane(messageContent);
        messageContentSp.setBounds(new Rectangle(130, 260, 865, 330));
        fromParty.setBounds(new Rectangle(130, 5, 575, 20));
        lblFromParty.setText("From Party");
        lblFromParty.setBounds(new Rectangle(25, 10, 100, 15));
        lblToParty.setText("To Party");
        lblToParty.setBounds(new Rectangle(25, 85, 100, 15));
        lblToParty.setToolTipText("null");
        toParty.setBounds(new Rectangle(130, 80, 575, 20));
        fromPartyId.setBounds(new Rectangle(130, 30, 260, 20));
        lblFromPartyId.setText("From Party Id");
        lblFromPartyId.setBounds(new Rectangle(25, 35, 100, 15));
        toPartyId.setBounds(new Rectangle(130, 105, 260, 20));
        lblToPartyId.setText("To Party Id");
        lblToPartyId.setBounds(new Rectangle(25, 110, 100, 15));
        lblToPartyId.setToolTipText("null");
        fromRole.setBounds(new Rectangle(130, 55, 260, 20));
        lblFromRole.setText("From Role");
        lblFromRole.setBounds(new Rectangle(25, 60, 100, 15));
        lblFromRole.setToolTipText("null");
        lblToRole.setText("To Role");
        lblToRole.setBounds(new Rectangle(25, 135, 100, 14));
        toRole.setBounds(new Rectangle(130, 130, 260, 20));
        lblService.setText("Service");
        lblService.setBounds(new Rectangle(25, 160, 100, 14));
        service.setBounds(new Rectangle(130, 155, 260, 20));
        action.setBounds(new Rectangle(130, 180, 260, 20));
        lblAction.setText("Action");
        lblAction.setBounds(new Rectangle(25, 185, 100, 14));
        refToMessageId.setBounds(new Rectangle(130, 205, 575, 20));
        lblRefToMessageId.setText("Ref To Msg Id");
        lblRefToMessageId.setBounds(new Rectangle(25, 210, 100, 14));
        lblConversationId.setText("Conversation Id");
        lblConversationId.setBounds(new Rectangle(25, 235, 100, 14));
        lblConversationId.setToolTipText("null");
        conversationId.setBounds(new Rectangle(130, 230, 575, 20));
        loadMessage.setText("Load Message");
        loadMessage.setBounds(new Rectangle(350, 600, 155, 23));
        loadMessage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loadMessage_actionPerformed(e);
                    }
                });
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuHelp.add(menuHelpAbout);
        menuBar.add(menuHelp);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(panelCenter, BorderLayout.CENTER);
        processCPA();
        panelCenter.add(loadMessage, null);
        panelCenter.add(conversationId, null);
        panelCenter.add(lblConversationId, null);
        panelCenter.add(lblRefToMessageId, null);
        panelCenter.add(refToMessageId, null);
        panelCenter.add(lblAction, null);
        panelCenter.add(action, null);
        panelCenter.add(service, null);
        panelCenter.add(lblService, null);
        panelCenter.add(toRole, null);
        panelCenter.add(lblToRole, null);
        panelCenter.add(lblFromRole, null);
        panelCenter.add(fromRole, null);
        panelCenter.add(lblToPartyId, null);
        panelCenter.add(toPartyId, null);
        panelCenter.add(lblFromPartyId, null);
        panelCenter.add(fromPartyId, null);
        panelCenter.add(toParty, null);
        panelCenter.add(lblToParty, null);
        panelCenter.add(lblFromParty, null);
        panelCenter.add(fromParty, null);
        panelCenter.add(messageContentSp, null);
        panelCenter.add(submitMessage, null);
    }

    void fileExit_ActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, new FrameGenMMD_AboutBoxPanel1(), 
                                      "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void generateMMD(String msgFilePath, String mmdFilePath) {
        MMDFile mmdFile = 
            new MMDFile(msgFilePath, cpaFile.getCpaId(), (PartyID)fromPartyIDs.elementAt(fromPartyId.getSelectedIndex()), 
                        (PartyID)toPartyIDs.elementAt(toPartyId.getSelectedIndex()), 
                        (String)fromRole.getSelectedItem(), 
                        (String)toRole.getSelectedItem(), 
                        (CPAService)services.elementAt(service.getSelectedIndex()), 
                        (String)action.getSelectedItem(), 
                        refToMessageId.getText(), conversationId.getText(), 
                        msgFilePath);
        mmdFile.generate();
        mmdFile.setMmdFilePath(mmdFilePath);
        mmdFile.save();
        if (mmdFile.getError() == null) {
            statusBar.setText("MMDFile " + mmdFile.getMmdFilePath() + 
                              " succesfully saved!");
        } else {
            statusBar.setText("MMDFile " + mmdFile.getMmdFilePath() + 
                              " could not be saved: " + mmdFile.getError());
        }

    }

    private void submitMessage_actionPerformed(ActionEvent e) {
        if (!"".equals(messageContent.getText()) && 
            messageContent.getText() != null) {
            String msgOutputDir = props.getMsgOutputDir();
            String mmdOutputDir = props.getMMDOutputDir();
            PartyID partyId = 
                (PartyID)fromPartyIDs.elementAt(fromPartyId.getSelectedIndex());
            String partyIdName = partyId.getId();
            String BusinessAction = (String)action.getSelectedItem();
            String msgFilePath = 
                msgOutputDir + "/" + partyIdName + BusinessAction + 
                formatter.format(new Date()) + ".xml";
            String mmdFilePath = 
                mmdOutputDir + "/" + "MMD_" + partyIdName + BusinessAction + 
                formatter.format(new Date()) + ".mmd";
            TextFile messageFile = 
                new TextFile(messageContent.getText(), msgFilePath);
            messageFile.save();
            if (messageFile.getError() == null) {
                statusBar.setText("File " + messageFile.getFilePath() + 
                                  " succesfully saved!");
                generateMMD(msgFilePath, mmdFilePath);
            } else {
                statusBar.setText("File " + messageFile.getFilePath() + 
                                  " could not be saved: " + 
                                  messageFile.getError());
            }
        } else {
            statusBar.setText("First load Message Content");
        }

    }

    public File chooseFile(String dftDirectory ) {
        File selectedFile;
        // Lazy creation: don't create the JFileChooser until it is needed
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            // This javax.swing.filechooser.FileFilter displays only HTML files
            FileFilter filter = new FileFilter() {
                    public boolean accept(File f) {
                        String fn = f.getName();
                        if (fn.endsWith(".xml") || f.isDirectory())
                            return true;
                        else
                            return false;
                    }

                    public String getDescription() {
                        return "XML Files";
                    }
                };
            fileChooser.setFileFilter(filter);
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(dftDirectory));                
        }
        // Ask the user to choose a file.
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // If they didn't click "Cancel", then try to display the file.
            selectedFile = fileChooser.getSelectedFile();

        } else {
            selectedFile = null;
        }
        return selectedFile;
    }

    private void loadMessage_actionPerformed(ActionEvent e) {
        File selectedFile = chooseFile(props.getDftDataDir());
        if (selectedFile != null) {
            TextFile messageFile = new TextFile(selectedFile);
            messageFile.load();
            messageContent.setText(messageFile.getText());
            if (messageFile.getError() == null) {
                statusBar.setText("File " + messageFile.getFilePath() + 
                                  " succesfully loaded!");
            } else {
                statusBar.setText("File " + messageFile.getFilePath() + 
                                  " could not be loaded: " + 
                                  messageFile.getError());
            }
        }

    }
}
