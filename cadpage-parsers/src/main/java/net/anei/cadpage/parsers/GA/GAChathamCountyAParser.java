package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Chatham County, GA
 */

public class GAChathamCountyAParser extends DispatchProQAParser {
  
  public GAChathamCountyAParser() {
    super("CHATHAM COUNTY", "GA", 
          "ID! ID2/L ADDR APT CITY GPS1 GPS2 UNIT CALL! INFO/N+", true);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.msgType == MsgType.RUN_REPORT && data.strSupp.startsWith("Alert:")) data.msgType = MsgType.GEN_ALERT;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID2")) return new IdField("E2020(.*)|", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("UNIT")) return new UnitField("\\d\\d- *(.*)", true);
    return super.getField(name);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("(.*)% [NSEW]");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1), data);
    }
  }
}
