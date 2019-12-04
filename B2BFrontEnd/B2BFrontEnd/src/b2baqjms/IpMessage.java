package b2baqjms;

import java.sql.SQLException;
import java.sql.Connection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.Datum;
import oracle.sql.STRUCT;
import oracle.jpub.runtime.MutableStruct;

public class IpMessage implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "B2B.IP_MESSAGE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,12,12,12,12,12,4,2005,2004 };
  protected static ORADataFactory[] _factory = new ORADataFactory[10];
  protected static final IpMessage _IpMessageFactory = new IpMessage();

  public static ORADataFactory getORADataFactory()
  { return _IpMessageFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[10], _sqlType, _factory); }
  public IpMessage()
  { _init_struct(true); }
  public IpMessage(String msgId, String inreplytoMsgId, String fromParty, String toParty, String actionName, String doctypeName, String doctypeRevision, Integer msgType, oracle.sql.CLOB payload, oracle.sql.BLOB attachment) throws SQLException
  { _init_struct(true);
    setMsgId(msgId);
    setInreplytoMsgId(inreplytoMsgId);
    setFromParty(fromParty);
    setToParty(toParty);
    setActionName(actionName);
    setDoctypeName(doctypeName);
    setDoctypeRevision(doctypeRevision);
    setMsgType(msgType);
    setPayload(payload);
    setAttachment(attachment);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(IpMessage o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new IpMessage();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getMsgId() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setMsgId(String msgId) throws SQLException
  { _struct.setAttribute(0, msgId); }


  public String getInreplytoMsgId() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setInreplytoMsgId(String inreplytoMsgId) throws SQLException
  { _struct.setAttribute(1, inreplytoMsgId); }


  public String getFromParty() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setFromParty(String fromParty) throws SQLException
  { _struct.setAttribute(2, fromParty); }


  public String getToParty() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setToParty(String toParty) throws SQLException
  { _struct.setAttribute(3, toParty); }


  public String getActionName() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setActionName(String actionName) throws SQLException
  { _struct.setAttribute(4, actionName); }


  public String getDoctypeName() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setDoctypeName(String doctypeName) throws SQLException
  { _struct.setAttribute(5, doctypeName); }


  public String getDoctypeRevision() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setDoctypeRevision(String doctypeRevision) throws SQLException
  { _struct.setAttribute(6, doctypeRevision); }


  public Integer getMsgType() throws SQLException
  { return (Integer) _struct.getAttribute(7); }

  public void setMsgType(Integer msgType) throws SQLException
  { _struct.setAttribute(7, msgType); }


  public oracle.sql.CLOB getPayload() throws SQLException
  { return (oracle.sql.CLOB) _struct.getOracleAttribute(8); }

  public void setPayload(oracle.sql.CLOB payload) throws SQLException
  { _struct.setOracleAttribute(8, payload); }


  public oracle.sql.BLOB getAttachment() throws SQLException
  { return (oracle.sql.BLOB) _struct.getOracleAttribute(9); }

  public void setAttachment(oracle.sql.BLOB attachment) throws SQLException
  { _struct.setOracleAttribute(9, attachment); }

}
