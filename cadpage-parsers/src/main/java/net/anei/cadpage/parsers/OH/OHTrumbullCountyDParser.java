package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHTrumbullCountyDParser extends FieldProgramParser {

  public OHTrumbullCountyDParser() {
    super("TRUMBULL COUNTY", "OH",
          "UNIT PLACE CALL ADDR X INFO ( GPS1 GPS2 | ) URL! END");
  }

  @Override
  public String getFilter() {
    return "alerts@thinlineds.com";
  }

  private static final Pattern DELIM = Pattern.compile("\\*{2,}");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("*")) return false;
    body = body.substring(1).trim();
    return parseFields(DELIM.split(body), data);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN, true);
    if (name.equals("URL")) return new InfoUrlField("https://.*");
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("STA")) abort();
      field = field.replace(" / ", ",");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      super.parse(field, data);
    }
  }
}
