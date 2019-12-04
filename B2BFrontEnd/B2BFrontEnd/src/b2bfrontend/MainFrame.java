package b2bfrontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

import b2baqjms.B2BAqJms;
import b2baqjms.IpMessage;

import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame {
    private BorderLayout layoutMain = new BorderLayout();
    private JPanel panelCenter = new JPanel();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenuItem menuFileConnect = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JLabel statusBar = new JLabel();
    private ImageIcon imageOpen = 
        new ImageIcon(MainFrame.class.getResource("openfile.gif"));
    private ImageIcon imageClose = 
        new ImageIcon(MainFrame.class.getResource("closefile.gif"));
    private ImageIcon imageHelp = 
        new ImageIcon(MainFrame.class.getResource("help.gif"));
    private JComboBox jCBFromParty = new JComboBox();
    private JLabel jLblFromParty = new JLabel();
    private JButton jBtnSubmit = new JButton();
    private JLabel jLblToParty = new JLabel();
    private JComboBox jCBToParty = new JComboBox();
    private JComboBox jCBBusinessAction = new JComboBox();
    private JComboBox jCBDocumentType = new JComboBox();
    private JComboBox jCBMessageType = new JComboBox();
    private JLabel jLblBusinessAction = new JLabel();
    private JLabel jLblDocumentType = new JLabel();
    private JLabel jLblMessageType = new JLabel();
    private JTextArea jTAPayload = new JTextArea();
    private JLabel jLblPayload = new JLabel();
    private JTextArea jTAAttachment = new JTextArea();
    private JLabel jLblAttachment = new JLabel();
    private JScrollPane jSPPayload;
    private JScrollPane jSPAttachment;
    private Connection jdbcConnection;
    private Vector documentTypes = new Vector();
    private Vector queues = new Vector();
    private JComboBox jCBQueue = new JComboBox();
    private JLabel jLblQueue = new JLabel();
    private JTextField jTFMsgId = new JTextField();
    private JTextField jTFInReplyToId = new JTextField();
    private JLabel jLblConsumer = new JLabel();
    private JLabel jLblMsgId = new JLabel();
    private JLabel jLblReplyToId = new JLabel();
    private JLabel jLblConversationId = new JLabel();
    private JTextField jTFConversationId = new JTextField();
    private JComboBox jCBConsumer = new JComboBox();

    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        this.setJMenuBar(menuBar);
        this.getContentPane().setLayout(layoutMain);
        panelCenter.setLayout(null);
        this.setSize(new Dimension(862, 696));
        this.setTitle("B2B Swing Front End");
        menuFile.setText("File");
        menuFileConnect.setText("Connect");
        menuFileConnect.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        fileConnect_ActionPerformed(ae);
                    }
                });
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
        statusBar.setText("Status");
        jCBFromParty.setBounds(new Rectangle(145, 10, 245, 20));
        jLblFromParty.setText("From Party");
        jLblFromParty.setBounds(new Rectangle(35, 10, 60, 15));
        jBtnSubmit.setText("Submit");
        jBtnSubmit.setBounds(new Rectangle(377, 580, 71, 23));
        jBtnSubmit.setActionCommand("jBtnSubmit");
        jBtnSubmit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jBtnSubmit_actionPerformed(e);
                    }
                });
        jLblToParty.setText("To Party");
        jLblToParty.setBounds(new Rectangle(35, 44, 60, 15));
        jCBToParty.setBounds(new Rectangle(145, 44, 245, 20));
        jCBBusinessAction.setBounds(new Rectangle(145, 78, 245, 20));
        jCBDocumentType.setBounds(new Rectangle(145, 111, 245, 20));
        jCBMessageType.setBounds(new Rectangle(145, 145, 45, 20));
        jLblBusinessAction.setText("Business Action");
        jLblBusinessAction.setBounds(new Rectangle(35, 78, 85, 15));
        jLblDocumentType.setText("Document Type");
        jLblDocumentType.setBounds(new Rectangle(35, 111, 85, 15));
        jLblMessageType.setText("Message Type");
        jLblMessageType.setBounds(new Rectangle(35, 145, 80, 15));
        jTAPayload.setBounds(new Rectangle(145, 180, 530, 195));
        jSPPayload = new JScrollPane(jTAPayload);
        jSPPayload.setBounds(new Rectangle(145, 180, 640, 195));
        jTAAttachment.setBounds(new Rectangle(145, 395, 535, 155));
        jSPAttachment = new JScrollPane(jTAAttachment);
        jSPAttachment.setBounds(new Rectangle(145, 395, 640, 155));
        jCBQueue.setBounds(new Rectangle(540, 10, 245, 20));
        jLblQueue.setText("Queue");
        jLblQueue.setBounds(new Rectangle(450, 10, 60, 15));
        jTFMsgId.setBounds(new Rectangle(540, 78, 245, 20));
        jTFInReplyToId.setBounds(new Rectangle(540, 111, 245, 20));
        jLblConsumer.setText("Consumer");
        jLblConsumer.setBounds(new Rectangle(450, 45, 60, 15));
        jLblMsgId.setText("Message ID");
        jLblMsgId.setBounds(new Rectangle(450, 75, 60, 15));
        jLblReplyToId.setText("Reply To Msg Id");
        jLblReplyToId.setBounds(new Rectangle(450, 110, 80, 15));
        jLblConversationId.setText("Conversation  Id");
        jLblConversationId.setBounds(new Rectangle(450, 145, 80, 15));
        jLblConversationId.setToolTipText("null");
        jTFConversationId.setBounds(new Rectangle(540, 145, 245, 20));
        jCBConsumer.setBounds(new Rectangle(540, 45, 245, 21));
        jLblAttachment.setText("Attachment");
        jLblAttachment.setBounds(new Rectangle(35, 395, 70, 15));
        jLblPayload.setText("Payload");
        jLblPayload.setBounds(new Rectangle(35, 180, 40, 15));

        panelCenter.add(jCBConsumer, null);
        panelCenter.add(jTFConversationId, null);
        panelCenter.add(jLblConversationId, null);
        panelCenter.add(jLblReplyToId, null);
        panelCenter.add(jLblMsgId, null);
        panelCenter.add(jLblConsumer, null);
        panelCenter.add(jTFInReplyToId, null);
        panelCenter.add(jTFMsgId, null);
        panelCenter.add(jLblQueue, null);
        panelCenter.add(jCBQueue, null);
        panelCenter.add(jLblAttachment, null);
        panelCenter.add(jSPAttachment, null);
        panelCenter.add(jLblPayload, null);
        panelCenter.add(jSPPayload, null);
        panelCenter.add(jLblMessageType, null);
        panelCenter.add(jLblDocumentType, null);
        panelCenter.add(jLblBusinessAction, null);
        panelCenter.add(jCBMessageType, null);
        panelCenter.add(jCBDocumentType, null);
        panelCenter.add(jCBBusinessAction, null);
        panelCenter.add(jLblToParty, null);
        panelCenter.add(jCBToParty, null);
        panelCenter.add(jLblFromParty, null);
        panelCenter.add(jCBFromParty, null);
        panelCenter.add(jBtnSubmit, null);

        menuFile.add(menuFileConnect);
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuHelp.add(menuHelpAbout);
        menuBar.add(menuHelp);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(panelCenter, BorderLayout.CENTER);
    }


    public void initFromParty(Connection jdbcConnection) throws Exception {
        jCBFromParty.removeAllItems();
        //String statement = "select name from tip_party_t where ishosted = 'Y'";
        String statement = 
            "select tid.value \n" + ",      tid.description \n" + 
            ",      pty.name \n" + ",      pty.ishosted\n" + 
            "from tip_tradingpartneridentific_rt tid \n" + 
            ",    tip_tradingpartneridentifi_1_t itp \n" + 
            ",    tip_party pty where pty.id = tid.tradingpartner \n" + 
            "and   itp.id = tid.partyidentificationtype \n" + 
            "and   itp.name = 'Name'\n" + 
            "order by pty.ishosted desc, pty.name asc";
        int fetched = 0;
        PreparedStatement ps = jdbcConnection.prepareStatement(statement);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            jCBFromParty.addItem(rs.getString("name"));
            fetched++;
        }
        rs.close();
        ps.close();
        jCBFromParty.repaint();
    }

    public void initToParty(Connection jdbcConnection) throws Exception {
        jCBToParty.removeAllItems();
        String statement = 
            "select tid.value \n" + ",      tid.description \n" + 
            ",      pty.name \n" + ",      pty.ishosted\n" + 
            "from tip_tradingpartneridentific_ra tid \n" + 
            ",    tip_tradingpartneridentifi_1_t itp \n" + 
            ",    tip_party pty where pty.id = tid.tradingpartner \n" + 
            "and   itp.id = tid.partyidentificationtype \n" + 
            "and   itp.name = 'Name'\n" + 
            "order by pty.ishosted desc, pty.name asc";
        int fetched = 0;
        PreparedStatement ps = jdbcConnection.prepareStatement(statement);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            jCBToParty.addItem(rs.getString("name"));
            fetched++;
        }
        rs.close();
        ps.close();
        jCBToParty.repaint();
    }

    public void InitBusinessAction(Connection jdbcConnection) throws Exception {
        jCBBusinessAction.removeAllItems();
        String statement = 
            "select distinct name from tip_businessaction_ra order by name";
        int fetched = 0;
        PreparedStatement ps = jdbcConnection.prepareStatement(statement);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            jCBBusinessAction.addItem(rs.getString("name"));
            fetched++;
        }
        rs.close();
        ps.close();
        jCBBusinessAction.repaint();
        jCBBusinessAction.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent ie) {
                        businessActionSelected(ie);
                    }
                });
    }

    public void InitDocumentType(Connection jdbcConnection, String businessAction) throws Exception {
        jCBDocumentType.removeAllItems();
        documentTypes.removeAllElements();
        String statement = 
            "select distinct dtp.name\n" + 
            ",      dtp.revision\n" + 
            "from tip_documenttype_ra dtp\n" + 
            ",    tip_businessaction_ra ban\n" + 
            "where dtp.businessaction = ban.id\n" + 
            "and   ban.name = :1\n" + 
            "order by dtp.name";
        int fetched = 0;
        PreparedStatement ps = jdbcConnection.prepareStatement(statement);
        ps.setString(1, businessAction);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            DocumentType docType = 
                new DocumentType(rs.getString("name"), rs.getString("revision"));
            documentTypes.addElement(docType);
            jCBDocumentType.addItem(docType.getDisplayName());
            fetched++;
        }
        rs.close();
        ps.close();
        jCBDocumentType.repaint();
        jCBDocumentType.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent ie) {
                        documentTypeSelected(ie);
                    }
                });
    }

    public void InitQueue(Connection jdbcConnection) throws Exception {
        {
            jCBQueue.removeAllItems();
            queues.removeAllElements();
            String statement = 
                "select que.owner\n" + ",      que.name\n" + "from all_queue_tables qtb\n" + 
                ",    all_queues que\n" + 
                "where que.queue_table=qtb.queue_table\n" + 
                "and  qtb.object_type='B2B.IP_MESSAGE_TYPE'\n" + 
                "and  que.enqueue_enabled like '%YES%'";
            int fetched = 0;
            queues = new Vector();
            PreparedStatement ps = jdbcConnection.prepareStatement(statement);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Queue queue = 
                    new Queue(rs.getString("owner"), rs.getString("name"));
                queues.addElement(queue);
                jCBQueue.addItem(queue.getDisplayName());
                fetched++;
            }
            rs.close();
            ps.close();
            jCBQueue.repaint();
        }
    }

    public void InitConsumer(Connection jdbcConnection, String docType, 
                             String docTypeRev) throws Exception {
        {
            jCBConsumer.removeAllItems();
            String statement = 
                "select distinct routing_id from\n" + "(\n" + "select to_char(nvl(dpv.stringvalue, 'B2BUSER')) routing_id\n" + 
                "from tip_documentdefinition_rt ddf\n" + 
                ",    tip_documentdefinitionusage ddu\n" + 
                ",    tip_documenttypeparameterva_rt dpv\n" + 
                ",    tip_parameter_rt par\n" + 
                ",    tip_documenttype_rt dtp\n" + 
                "where dpv.documenttypeparameter=par.id\n" + 
                "and dpv.documentdefinitionusage = ddu.id\n" + 
                "and ddf.id = ddu.documentdefinition\n" + 
                "and ddf.documenttype = dtp.id\n" + 
                "and par.name = 'DocumentRoutingID'\n" + 
                "and dtp.name = :1\n" + "and dtp.revision = :2\n"  + 
                "union\n" + 
                "select 'B2BUSER' routing_id \n" + 
                "from dual" + ")\n" +
                "order by decode(routing_id, 'B2BUSER',1,2), routing_id";
            PreparedStatement ps = jdbcConnection.prepareStatement(statement);
            ps.setString(1, docType);
            ps.setString(2, docTypeRev);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                jCBConsumer.addItem(rs.getString("routing_id"));
            }
            rs.close();
            ps.close();
            jCBConsumer.repaint();
        }
    }

    public void InitMessageType() throws Exception {
        jCBMessageType.removeAllItems();
        jCBMessageType.addItem("1");
        jCBMessageType.addItem("2");
        jCBMessageType.addItem("3");
        jCBMessageType.repaint();
    }


    public void InitPane() throws Exception {
    }

    void fileExit_ActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, new MainFrame_AboutBoxPanel1(), 
                                      "About", JOptionPane.PLAIN_MESSAGE);
    }

    public void documentTypeSelected(ItemEvent ie) {
        int documentType = jCBDocumentType.getSelectedIndex();
        int stateChange=ie.getStateChange();
        if (stateChange==ie.SELECTED&&documentType>-1){
            DocumentType docType = 
                (DocumentType)documentTypes.elementAt(documentType);
            String docTypeName = docType.getName();
            String docTypeRev = docType.getRevision();
            try {
                InitConsumer(jdbcConnection, docTypeName, docTypeRev);
            } catch (Exception initException) {
                statusBar.setText(initException.getMessage());
            }
        }
    }
    public void businessActionSelected(ItemEvent ie) {
        String businessAction = (String)jCBBusinessAction.getSelectedItem();
        try {
            InitDocumentType(jdbcConnection, businessAction);
            //documentTypeSelected(ie);
        } catch (Exception initException) {
            statusBar.setText(initException.getMessage());
        }
    }

    void connect() {
        statusBar.setText("");
        ConnectDialog connectDialog = new ConnectDialog();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = connectDialog.getSize();
        connectDialog.setLocation((screenSize.width - dialogSize.width) / 2, 
                                  (screenSize.height - dialogSize.height) / 2);
        connectDialog.setVisible(true);
        if ("S".equals(connectDialog.getStatus())) {
            statusBar.setText("Connected: " + connectDialog.getMessage());
            jdbcConnection = connectDialog.getConnection();
            try {
                initFromParty(jdbcConnection);
                initToParty(jdbcConnection);
                InitBusinessAction(jdbcConnection);
                String businessAction = (String)jCBBusinessAction.getSelectedItem();
                InitDocumentType(jdbcConnection, businessAction);
                InitMessageType();
                InitQueue(jdbcConnection);
                DocumentType docType = 
                    (DocumentType)documentTypes.elementAt(jCBDocumentType.getSelectedIndex());
                String docTypeName = docType.getName();
                String docTypeRev = docType.getRevision();
                InitConsumer(jdbcConnection, docTypeName, docTypeRev);
            } catch (Exception initException) {
                statusBar.setText(initException.getMessage());
            }
        } else {
            statusBar.setText("Not connected: " + connectDialog.getMessage());
        }
    }

    void fileConnect_ActionPerformed(ActionEvent e) {
        connect();
    }

    private void jBtnSubmit_actionPerformed(ActionEvent e) {
        B2BAqJms b2bAqJms = new B2BAqJms();
        String fromParty = (String)jCBFromParty.getSelectedItem();
        String toParty = (String)jCBToParty.getSelectedItem();
        String businessAction = (String)jCBBusinessAction.getSelectedItem();
        DocumentType docType = 
            (DocumentType)documentTypes.elementAt(jCBDocumentType.getSelectedIndex());
        String msgType = (String)jCBMessageType.getSelectedItem();
        Integer iMsgType = new Integer(msgType);
        Queue queue = (Queue)queues.elementAt(jCBQueue.getSelectedIndex());
        String msgId = jTFMsgId.getText();
        String inReplyTo = 
            jTFInReplyToId.getText() + ":" + jTFConversationId.getText();
        String consumer = (String)jCBConsumer.getSelectedItem();
        IpMessage ipMessage;
        try {
            //Create Message
            ipMessage = 
                    b2bAqJms.createMessage(jdbcConnection, fromParty, toParty, 
                                           businessAction, docType.getName(), 
                                           docType.getRevision(), inReplyTo, 
                                           msgId, iMsgType.intValue(), 
                                           jTAPayload.getText(), 
                                           jTAAttachment.getText());
            //Publish the Message
            b2bAqJms.publishMessages(jdbcConnection, queue.getOwner(), 
                                     queue.getName(), consumer, ipMessage);
            statusBar.setText("Message: " + msgId + " enqueued!");
        } catch (Exception f) {
            statusBar.setText(f.getMessage());
        }
    }
}
