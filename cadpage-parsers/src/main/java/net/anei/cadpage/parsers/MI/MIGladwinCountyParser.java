package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIGladwinCountyParser extends DispatchOSSIParser {

  public MIGladwinCountyParser() {
    super(CITY_CODES, "GLADWIN COUNTY", "MI",
          "FYI? SRC? CALL ADDR CITY! UNIT+? INFO/N+? GPS1 GPS2 NAME END");
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
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("GPS1")) return new GPSField(1, "[-+]?\\d{2,3}\\.\\d+", true);
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    public MyUnitField() {
      super("[A-Z]{4}", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strUnit)) return;
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "NES",  "NESTER TWP",
      "NEST", "NESTER TWP"
  });

}
