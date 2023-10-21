package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTGallatinCountyAParser extends FieldProgramParser {

  public MTGallatinCountyAParser() {
    super("GALLATIN COUNTY", "MT",
          "CALL UNIT PLACE APT ADDRCITYST INFO ( END |  MAP? GPS ID INFO/N! END )");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\\|", -1);
    for (int ndx = 0; ndx < flds.length; ndx++) {
      if (flds[ndx].equals("None")) flds[ndx] = "";
    }
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(",None");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:ALT|LOT|RM|ROOM|UNIT) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",")) return false;
      parse(field, data);
      return true;
    }
  }

  private static final Pattern LEAD_COMMA_PTN = Pattern.compile("^[, ]+");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = LEAD_COMMA_PTN.matcher(field).replaceFirst("");
      field = stripFieldStart(field, "CAD NOTES ");
      field = stripFieldEnd(field, " CAD NOTES None");
      field = field.replace(" CAD  NOTES ", " ");
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
