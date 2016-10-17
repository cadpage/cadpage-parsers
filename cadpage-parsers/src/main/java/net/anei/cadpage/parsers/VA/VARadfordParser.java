package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

public class VARadfordParser extends DispatchSouthernPlusParser {
    
  public VARadfordParser() {
    super(CITY_LIST, "RADFORD", "VA",
          "ADDR/SP APT? X? NAME? ID CALL! INFO+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("Dispatch:")) return false;
    body = body.substring(9).trim();
    if (subject.equals("Text Message")) subject = "";
    return super.parseMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("\\d{1,4}", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field,  "=");
      field = stripFieldEnd(field, "=");
      field = field.replace('=', '&');
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{
    "RADFORD"
  };
}