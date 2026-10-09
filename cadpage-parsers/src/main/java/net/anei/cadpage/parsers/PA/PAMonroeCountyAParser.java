package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAMonroeCountyAParser extends FieldProgramParser {

  public PAMonroeCountyAParser() {
    super(PAMonroeCountyParser.CITY_LIST, "MONROE COUNTY", "PA",
          "( PHONE_CALL NAME PHONE STARS INFO/N+? STARS ADDRCITY! " +
          "| CALL ( PRI_N ADDRCITY PLACE X_ST:X! " +
                 "| ALARM_LEVEL:PRI ADDRCITY PLACE X_ST:X " +
                 ") " +
            "( Lat/Lon:GPS UNIT! | GPS UNIT! | UNIT GPS! ) INFO/N+ " +
          ")");
    removeWords("ROAD", "FS", "SQ");
    setupSpecialStreets("SUNSET STRIP");
  }

  @Override
  public String getFilter() {
    return "notify@monroeco911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String addressLine;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD MSG")) return false;
    addressLine = "";
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PHONE_CALL")) return new CallField("PHONE CALL", true);
    if (name.equals("STARS")) return new SkipField("\\*{3,}", true);
    if (name.equals("PRI_N")) return new PriorityField("\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      addressLine = field;
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(addressLine)) return;
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    public MyGPSField() {
      super("\\d\\d\\.\\d+ / -\\d\\d\\.\\d+|-361 / -361", true);
    }
    @Override
    public void parse(String field, Data data) {
      field = field.replace('/', ',');
      super.parse(field, data);
    }
  }
}
