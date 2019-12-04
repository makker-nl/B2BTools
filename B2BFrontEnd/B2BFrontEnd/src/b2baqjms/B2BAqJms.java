package b2baqjms;

import java.io.OutputStream;
import java.io.Writer;

import java.sql.Connection;

import java.sql.SQLException;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import javax.naming.NamingException;

import javax.swing.JTextArea;

import oracle.AQ.AQAgent;

import oracle.jms.AQjmsAgent;
import oracle.jms.AQjmsConstants;
import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsSession;

import oracle.jms.AQjmsTopicConnectionFactory;
import oracle.jms.AQjmsTopicPublisher;
import oracle.jms.AdtMessage;

import oracle.sql.BLOB;
import oracle.sql.CLOB;


public class B2BAqJms {
    public B2BAqJms() {
    }

    public static IpMessage createMessage(Connection jdbcConnection, 
                                          String fromParty, String toParty, 
                                          String actionName, 
                                          String docTypeName, 
                                          String docTypeRevision, 
                                          String inReplyToMsgId, String msgId, 
                                          int msgType, String payload, 
                                          String attachment) throws Exception {
        CLOB payloadCLOB;
        BLOB attachmentBLOB;
        IpMessage ipMessage;
        System.out.println("Create Message");
        try {
            // Create the payload for the message
            ipMessage = new IpMessage();
            ipMessage.setFromParty(fromParty);
            ipMessage.setToParty(toParty);
            ipMessage.setActionName(actionName);
            ipMessage.setDoctypeName(docTypeName);
            ipMessage.setDoctypeRevision(docTypeRevision);
            ipMessage.setInreplytoMsgId(inReplyToMsgId);
            ipMessage.setMsgId(msgId);
            ipMessage.setMsgType(msgType);
            payloadCLOB = 
                    OracleDBConnection.createCLOB(jdbcConnection, payload);
            ipMessage.setPayload(payloadCLOB);
            attachmentBLOB = 
                    OracleDBConnection.createBLOB(jdbcConnection, attachment);
            ipMessage.setAttachment(attachmentBLOB);
            System.out.println("ipMessage Created: " + ipMessage.toString());
            return (ipMessage);
        } catch (Exception p_exp) {
            p_exp.printStackTrace();
            throw new Exception("AQ-1021");
        }
    }

    private static void printMessage(IpMessage ipMessage) throws Exception {
        // Create the payload for the message
        String payload;
        String attachment;
        System.out.println("Print Message");
        try {
            System.out.println("From Party        : " + 
                               ipMessage.getFromParty());
            System.out.println("To Party          : " + 
                               ipMessage.getToParty());
            System.out.println("Action            : " + 
                               ipMessage.getActionName());
            System.out.println("Doc Type          : " + 
                               ipMessage.getDoctypeName());
            System.out.println("Doc Type Rev      : " + 
                               ipMessage.getDoctypeRevision());
            System.out.println("In Reply To Msg Id: " + 
                               ipMessage.getInreplytoMsgId());
            System.out.println("Msg Id            : " + ipMessage.getMsgId());
            System.out.println("Message Type      : " + 
                               ipMessage.getMsgType());
            payload = OracleDBConnection.readCLOB(ipMessage.getPayload());
            System.out.println("Payload           : ");
            System.out.println(payload);
            attachment = 
                    OracleDBConnection.readBLOB(ipMessage.getAttachment());
            System.out.println("Attachment        : ");
            System.out.println(attachment);
        } catch (Exception p_exp) {
            p_exp.printStackTrace();
            throw new Exception("AQ-1025");
        }
    }

    public static void publishMessages(Connection jdbcConnection, 
                                       String userName, String ipOutQueueName, 
                                       String consumer, 
                                       IpMessage ipMessage) throws Exception {
        System.out.println("Publish Message");
        TopicIpQueue topicOutQueue;
        //Create Topic based on jdbcConnection
        topicOutQueue = new TopicIpQueue(userName, ipOutQueueName);
        topicOutQueue.createTopicSession(jdbcConnection);
        //Create Publisher
        topicOutQueue.createPublisher();
        //Set Receipient
        topicOutQueue.setRecipient(consumer);
        //Publish Message
        topicOutQueue.publishMessage(ipMessage);
    }

    public static void receiveMessage(Connection jdbcConnection, 
                                      String userName, 
                                      String ipInQueueName) throws Exception {
        System.out.println("Receive Message");
        TopicIpQueue topicInQueue;
        IpMessage ipMessage;
        //Create Topic based on jdbcConnection
        topicInQueue = new TopicIpQueue(userName, ipInQueueName);
        topicInQueue.createTopicSession(jdbcConnection);
        //Create Receiver
        topicInQueue.createReceiver("B2BAQJMSUSER", "");
        //Receive Message
        ipMessage = topicInQueue.receiveMessage();
        //Print Message
        if (ipMessage != null)
            printMessage(ipMessage);
        else
            System.out.println("No Message Received");
    }


}
