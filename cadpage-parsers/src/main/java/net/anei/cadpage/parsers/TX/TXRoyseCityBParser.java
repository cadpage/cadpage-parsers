package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class TXRoyseCityBParser extends DispatchH05Parser {

  public TXRoyseCityBParser() {
    super("ROYSE CITY", "TX",
          "Units_Assigned:UNIT! Call_Type:CALL! Business_Name:PLACE! Location:ADDRCITY! Cross_Streets:X! Box:BOX! Narrative:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "donotreply@rockwallcountytexas.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("This is an automated message");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
