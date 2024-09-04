package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VAFredericksburgParser extends DispatchOSSIParser {

  public VAFredericksburgParser() {
    super(CITY_CODES, "FREDERICKSBURG", "VA",
          "( CANCEL ADDR! CITY INFO+ " +
          "| FYI? ADDR PLACE? CITY GPS1 GPS2! CALL2 ID2 UNIT? X X END " +
          ")");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID2")) return new IdField("\\d+");
    if (name.equals("CALL2")) return new CallField("[A-Z]+", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|MEDI", true);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static Properties CITY_CODES = buildCodeTable(new String[] {
      "FRE",  "FREDERICKSBURG",
      "MAF",  "FREDERICKSBURG",
      "SPO",  "FREDERICKSBURG"
  });
}
