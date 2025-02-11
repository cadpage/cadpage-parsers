package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIShiawasseeCountyParser extends DispatchOSSIParser {

  public MIShiawasseeCountyParser() {
    super("SHIAWASSEE COUNTY", "MI",
          "SRC? CALL ADDR! X+? SRC? INFO/N+? ( DATETIME UNIT | UNIT ) UNIT/C+? INFO/N+? GPS1 GPS2");
    setupSaintNames("MARYS");
  }

  @Override
  public String getFilter() {
    return "CAD@shiawassee.net,CAD@shiawassee.local";
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
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,2}[FP]D", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b[A-Z]{1,4}\\d*\\b,?)+", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);

    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }
}
