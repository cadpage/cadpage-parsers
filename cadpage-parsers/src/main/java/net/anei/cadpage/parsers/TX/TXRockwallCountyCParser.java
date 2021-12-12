package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH04Parser;

public class TXRockwallCountyCParser extends DispatchH04Parser {

  public TXRockwallCountyCParser() {
    super("ROCKWALL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "@rockwall.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO_BLK")) return new MyInfoBlockField();
    return super.getField(name);
  }

  protected class MyInfoBlockField extends BaseInfoBlockField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("This email ")) return;
      super.parse(field,  data);
    }
  }
}
