package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Hocking County, OH
 */
public class OHHockingCountyParser extends FieldProgramParser {

  public OHHockingCountyParser() {
    super("HOCKING COUNTY", "OH",
          "ID CALL ADDRCITYST APT PLACE GPS1 GPS2 UNIT INFO! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@hocking.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{7}", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PFX_PTN = Pattern.compile("^(?:APT|APARTMENT|CABIN|UNIT|ROOM|RM) *", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = APT_PFX_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}|None");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Pattern UNIT_SEP_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_SEP_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_SEP_PTN = Pattern.compile("[, ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String line : INFO_SEP_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
