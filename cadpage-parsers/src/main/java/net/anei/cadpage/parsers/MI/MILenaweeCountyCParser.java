package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MILenaweeCountyCParser extends DispatchH05Parser {

  public MILenaweeCountyCParser() {
    super("LENAWEE COUNTY", "MI",
          "EMPTY+? CALL DATETIME PLACE? ADDRCITY/S6 ( X | PLACE X ) https:SKIP! INFO_BLK/Z+? UNIT! Alerts:ALERT! Caller:NAME! Caller's_TX:PHONE! Incident_#:EMPTY! ID! TIMES+");
  }

  @Override
  public String getFilter() {
    return "Lenawee@lenawee.mi.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.lastIndexOf("*This is an automated RNR*");
    if (pt >=  0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      return parse(false, field, data);
    }

    @Override
    public void parse(String field, Data data) {
      parse(true, field, data);
    }

    private boolean parse(boolean force, String field, Data data) {
      if (!force && !field.contains(",") && !field.startsWith("LAT:")) return false;
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, data.strApt);
      return true;
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;
      super.parse(field, data);
      return true;
    }
  }

}
