package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWilliamsCountyBParser extends FieldProgramParser {

  public OHWilliamsCountyBParser() {
    super(CITY_CODES, "WILLIAMS COUNTY", "OH",
          "( CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! Date:TIMEDATE! Unit:UNIT! "  +
          "| FIRE_CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! " +
          ") INFO:INFO/N! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "mlevy@wmsco.org,info@sundance-sys.com,BryanCAD";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d:\\d\\d +\\d\\d?/\\d\\d?/\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = translateAddress(field);
      int pt = field.indexOf(" to ");
      if (pt >= 0) {
        data.strSupp = "Dest: " + field.substring(pt+4).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " INFO";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.matches()) return;
      super.parse(field, data);
    }
  }

  private String translateAddress(String addr) {
    Matcher match = ADDR_TRANSLATE_PTN.matcher(addr);
    if (match.find()) {
      StringBuilder sb = new StringBuilder();
      do {
        String term = match.group();
        String rep = ADDR_TRANSLATE_TABLE.getProperty(term);
        if (rep == null) rep = term;
        match.appendReplacement(sb, rep);
      } while (match.find());
      match.appendTail(sb);
      addr = sb.toString();
    }
    return addr;
  }

  private static final Pattern ADDR_TRANSLATE_PTN = Pattern.compile("\\b(?:[NSEW]S|BCH|WCGH)\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties ADDR_TRANSLATE_TABLE = buildCodeTable(new String[] {
      "N/S",    "NORTH SIDE",
      "S/S",    "SOUTH SIDE",
      "E/S",    "EAST SIDE",
      "W/S",    "WEST SIDE",
      "BCH",    "BRYAN HOSPITAL",
      "WCGH",   "MONTPELIER HOSPITAL"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALV", "ALVORDTON",
      "ANG", "ANGOLA/IN",
      "ARC", "ARCHBOLD",
      "AUB", "AUBURN/IN",
      "BLK", "BLAKESLEE",
      "BRY", "BRYAN",
      "BUT", "BUTLER/IN",
      "CAM", "CAMDEN/MI",
      "DEF", "DEFIANCE",
      "DEL", "DELTA",
      "EDG", "EDGERTON",
      "EDO", "EDON",
      "EVA", "EVANSPORT",
      "FAM", "FARMER",
      "FAY", "FAYETTE",
      "FRE", "FREMONT/IN",
      "HAM", "HAMILTON/IN",
      "HIC", "HICKSVILLE",
      "HIL", "HILLSDALE/MI",
      "HOL", "HOLIDAY CITY",
      "HUD", "HUDSON/MI",
      "KUN", "KUNKLE",
      "LIB", "LIBERTY CENTER",
      "MAR", "MARK CENTER",
      "MET", "METZ",
      "MN",  "MONTGOMERY/MI",
      "MON", "MONTPELIER",
      "MOR", "MORENCI/MI",
      "NAP", "NAPOLEON",
      "NEY", "NEY",
      "PIO", "PIONEER",
      "STR", "STRYKER",
      "WAL", "WALDRON/MI",
      "WAT", "WATERLOO/IN",
      "WAU", "WAUSEON",
      "WEU", "WEST UNITY"

  });
}
