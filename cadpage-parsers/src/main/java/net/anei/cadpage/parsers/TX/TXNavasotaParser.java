package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class TXNavasotaParser extends DispatchA37Parser {

  public TXNavasotaParser() {
    super(null, CITY_LIST, "NAVASOTA", "TX");
  }

  @Override
  protected boolean parseMessageField(String field, Data data) {
    data.strSupp = field;
    return true;
  }
  
  @Override
  protected boolean parseLocationField(String field, Data data) {
    field = stripFieldStart(field, "\\L ");
    field = stripFieldEnd(field," TX");
    return super.parseLocationField(field, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "NAVASOTA"
  };
}
