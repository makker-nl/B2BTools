package b2baqjms;

import java.sql.SQLException;
import java.sql.Connection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.Datum;
import oracle.sql.REF;
import oracle.sql.STRUCT;

public class IpMessageRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "B2B.IP_MESSAGE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final IpMessageRef _IpMessageRefFactory = new IpMessageRef();

  public static ORADataFactory getORADataFactory()
  { return _IpMessageRefFactory; }
  /* constructor */
  public IpMessageRef()
  {
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _ref;
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    IpMessageRef r = new IpMessageRef();
    r._ref = (REF) d;
    return r;
  }

  public static IpMessageRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (IpMessageRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to IpMessageRef: "+exn.toString()); }
  }

  public IpMessage getValue() throws SQLException
  {
     return (IpMessage) IpMessage.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(IpMessage c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
