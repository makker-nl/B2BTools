package b2baqjms;

import java.sql.Connection;

import java.sql.SQLException;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import oracle.AQ.AQAgent;

import oracle.jms.AQjmsAgent;
import oracle.jms.AQjmsConstants;
import oracle.jms.AQjmsSession;
import oracle.jms.AQjmsTopicConnectionFactory;
import oracle.jms.AQjmsTopicPublisher;
import oracle.jms.AQjmsTopicReceiver;
import oracle.jms.AdtMessage;

import oracle.sql.ORADataFactory;


public class TopicIpQueue {
    //TopicConnectionFactory topicConnFactory;
    TopicConnection topicConnection;
    TopicSession topicSession;
    Topic ipQueue;
    AQjmsAgent[] recipientList;
    AdtMessage adtMsg;
    AQjmsTopicPublisher topicPublisher;
    AQjmsTopicReceiver  topicReceiver;

    String userName, ipQueueName;

    public TopicIpQueue(String userName, String ipQueueName) {
        this.userName = userName;
        this.ipQueueName = ipQueueName;
    }

    public void createTopicSession(Connection jdbcConnection) throws Exception {
        // A Topic Connection must be created to the database where the queue
        // resides. It is retrieved using the method createTopicConnection
        // present in Topic Connection Factory.
        // The syntax of this method used here is
        // createTopicConnection(Connection jdbcConnection)
        try {
            this.topicConnection = 
                    AQjmsTopicConnectionFactory.createTopicConnection(jdbcConnection);
            System.out.println("Topic Connecion Created");
            // A topic session is required for publishing the messages. Destination
            // objects are created from a Session object using domain
            // specific session methods.
            // The syntax of this method is
            // createTopicSession(boolean newSession, int properties)
            // where newSession tells if a new JMS Session need to be created
            // properties tells the properties of the Session like
            // if acknowledgement is required or not, etc

            this.topicSession = topicConnection.createTopicSession(true, 0);
            System.out.println("Topic Session Created");

            // Start the connection to publish messages
            topicConnection.start();
            System.out.println("Topic Session started");
            //Create Topic
            createTopic();
            //Create ADT Message
            // An AdtMessage is a message whose body contains an Oracle ADT type
         // object. It is used to send a message that contains a Java object
         // that maps to an Oracle Object type. Queue table
             // with payload type as the Oracle Object Type can be filled using this.
             // The AdtMessage payload can be read and written using
             // the getAdtPayload and setAdtPayload methods.
             // Along with the payload an ADTMessage can contain additional header
             // fields called properties and a header. These properties can then be
             // used in message selectors to select specific messages.
             adtMsg = ((AQjmsSession)topicSession).createAdtMessage();
             System.out.println("adtMsg Created");
        } catch (JMSException jmsExp) {
            jmsExp.printStackTrace();
            throw new Exception("AQ-1013");
        }
    }

    public void createTopic() throws Exception {
        try {
            // Topic maps to multi consumer queue in AQ.
            // Gets the topic for the queue using the Session object.
            // The syntax of this method is
            // getTopic(String userName, String queueName)
            this.ipQueue = 
                    ((AQjmsSession)topicSession).getTopic(userName, ipQueueName);
            System.out.println("Topic Created");
        } catch (JMSException jmsExp) {
            jmsExp.printStackTrace();
            throw new Exception("AQ-1014");
        }
    }

    public void createPublisher() throws Exception {
        try {
            // Publisher is created to publish the messages for a particular topic.
            // The syntax of this method is
            // createPublisher(Topic topicName)
            // where topicName tells the name of the topic for which messages are
            // published.
            topicPublisher = 
                    (AQjmsTopicPublisher)topicSession.createPublisher(ipQueue);
            System.out.println("topicPublisher Created");
        } catch (JMSException p_jmsExp) {
            p_jmsExp.printStackTrace();
            throw new Exception("AQ-1017");
        } catch (Exception p_exp) {
            p_exp.printStackTrace();
            throw new Exception("AQ-1018");
        }
    }
    public void createReceiver(String receiver, String messageSelector) throws Exception {
        try {
            //ORADataFactory dataFactory = IpMessage.getORADataFactory();
            // Receiver is created to receive the messages for a particular topic.
            topicReceiver = ((AQjmsSession) topicSession).createTopicReceiver(ipQueue, receiver, messageSelector, IpMessage.getORADataFactory());
            System.out.println("topicReceiver Created");
        } catch (JMSException p_jmsExp) {
            p_jmsExp.printStackTrace();
            throw new Exception("AQ-1019");
        } catch (Exception p_exp) {
            p_exp.printStackTrace();
            throw new Exception("AQ-1020");
        }
    }
    public void setRecipient(String recipient) throws Exception {
        try {
            //Create Recipient
            recipientList = new AQjmsAgent[1];
            recipientList[0] = 
                    new AQjmsAgent(recipient, this.userName + "." + this.ipQueueName, 
                                   AQAgent.DEFAULT_AGENT_PROTOCOL);

            System.out.println("Recipient created");
        } catch (SQLException exp) {
            exp.printStackTrace();
            throw new Exception("AQ-1018");
        }
    }

    public void publishMessage(IpMessage ipMessage) throws Exception {
        try {

            // Clears a message's properties. The message header fields and body
            // are not cleared.Property values are set prior to sending a message.
            // When a client receives a message, its properties are in read-only
            // mode. If a client attempts to set properties at this point, an
            // Exception is thrown. If clearProperties is called, the properties
            // can now be both read from and written to.
            this.adtMsg.clearProperties();

            // Assigns the Java object to the ADT Message.
            // The syntax of this method is
            // setAdtPayload(ORAData payload)
            // where payload object must implement ORAData.It must be a Java object
            // that represents the ADT that is defined as the
            // queue /topic payload type
            adtMsg.setAdtPayload(ipMessage);
            // Set the priority of the ADT message. The permitted values for
            // priority are 0, 1, 2, 3...9, 9 being the highest priority.
            adtMsg.setJMSPriority(1);


            // The publisher created publishes the message. All the subscribers
            // who have subscribed to this topic will receive the message.
            // The syntax of this method is
            // publish(Topic topicName, Message messName,int deliveryMode,
            //         int priority, long timeToLive)
            // where topicName gives the name of the topic to which the publisher
            // is publishing the message
            // messName gives the name of the message
            // deliveryMode can be either PERSISTENT or NON_PERSISTENT.
            // Oracle AQ support persistent message delivery.
            // priority tells the priority of the message in integer and
            // timeToLive helps to calculate the expiration period of the message.
            // When a message is sent, its expiration time is calculated as the
            // sum of the time-to-live value specified on the send method and the
            // current GMT. If the time-to-live is specified as zero, expiration
            // is set to zero, which is to say, the message does not expire.
            topicPublisher.publish(ipQueue, adtMsg, recipientList, 
                                   DeliveryMode.PERSISTENT, 1, 
                                   AQjmsConstants.EXPIRATION_NEVER);
            System.out.println("Message Published");

            // Commit the JMS session.
            topicSession.commit();
            System.out.println("Commit");
        } catch (JMSException jmsExp) {
            jmsExp.printStackTrace();
            throw new Exception("AQ-1015");
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new Exception("AQ-1016");
        }
    }
    public IpMessage receiveMessage() throws Exception {
        try {
            
            IpMessage ipMessage;
            //Receive the message
            //adtMsg = (AdtMessage)topicReceiver.receive();
            adtMsg = (AdtMessage)topicReceiver.receiveNoWait();
            // Get payload out of message
            if (adtMsg!=null)
            {
              System.out.println("Message Received");
              ipMessage = (IpMessage)adtMsg.getAdtPayload();
              System.out.println("Payload extracted");
              // Commit the JMS session.
              topicSession.commit();
              System.out.println("Commit");
              return(ipMessage);
            }
            else return(null);
        } catch (JMSException jmsExp) {
            jmsExp.printStackTrace();
            throw new Exception("AQ-1021");
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new Exception("AQ-1022");
        }
    }
}
