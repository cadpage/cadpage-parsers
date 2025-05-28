package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIAlpenaCountyAParser extends DispatchOSSIParser {

  public MIAlpenaCountyAParser() {
    super("ALPENA COUNTY", "MI",
          "( CANCEL ADDR " +
          "| ID SRC DATETIME CALL PRI UNIT ADDR X X PLACE NAME PHONE! INFO/N+? GPS1 GPS2 END )");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseMsg("CAD:" + body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PRI")) return new PriorityField("[1-9P]", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9,]+", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private static final Pattern TRUNC_GPS_PTN = Pattern.compile("[-+]?[0-9.]+");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField(+2)) return false;
      if (!GPS_PTN.matcher(field).matches()) {
        return (isLastField(+1) && TRUNC_GPS_PTN.matcher(field).matches());
      }
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
