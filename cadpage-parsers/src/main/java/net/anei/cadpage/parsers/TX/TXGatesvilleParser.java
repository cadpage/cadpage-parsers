package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class TXGatesvilleParser extends DispatchA37Parser {

  public TXGatesvilleParser() {
    super("GatesvilleDispatch", CITY_LIST, "GATESVILLE", "TX");
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("GFD:active911 Gatesville Dispatch ")) {
      body = "GatesvilleDispatch:" + body.substring(34);
    }
    // TODO Auto-generated method stub
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean parseMessageField(String field, Data data) {
    data.strSupp = field;
    return true;
  }

  @Override
  protected boolean parseLocationField(String field, Data data) {
    int pt = field.lastIndexOf('-');
    if (pt >= 0) {
      String part1 = field.substring(0,pt).trim();
      String part2 = field.substring(pt+1).trim();
      if (isCity(part2)) {
        data.strCity = part2;
        parseAddress(part1, data);
        return true;
      }
      if (isValidAddress(part1)) {
        parseAddress(part1, data);
        data.strPlace = part2;
        return true;
      }
    }

    return super.parseLocationField(field, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " PLACE INFO";
  }

  private static final String[] CITY_LIST = new String[]{
    "COPPERAS COVE",
    "EVANT",
    "FLAT",
    "FORT HOOD",
    "GATESVILLE",
    "JONESBORO",
    "MOUND",
    "OGLESBY",
    "PEARL",
    "PIDCOKE",
    "PURMELA",
    "SOUTH MOUNTAIN",
    "THE GROVE",
    "TURNERSVILLE"  };
}
