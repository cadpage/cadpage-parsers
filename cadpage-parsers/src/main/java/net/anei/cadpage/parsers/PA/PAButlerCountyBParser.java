package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAButlerCountyBParser extends FieldProgramParser {

  public PAButlerCountyBParser() {
    super("BUTLER COUNTY", "PA",

        "DASHES! INCIDENT:EMPTY! Inc_Type:CALL! Mod_Circum:CALL/SDS! Priority:PRI! Area:MAP! County:CITY! LOCATION:EMPTY! Loc_Name:PLACE! Loc_Descr:PLACE/SDS! Location:ADDR! Municipality:CITY! Building:APT! Floor:APT! Apt/Unit:APT! Cross_Strs:X! PREMISE_HAZARD:HAZARD! COMMENTS:INFO! INFO/N+? UNITS_DISPATCHED:UNIT! UNIT/S+? Caller:NAME! Phone:PHONE! Created:SKIP! Inc_#:ID! END");
  }

  @Override
  public String getFilter() {
    return "10C@RCAD911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Dispatch Notification for Incident ")) return false;
    data.strCallId = subject.substring(35).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DASHES")) return new SkipField("-{20,}", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("HAZARD")) return new MyHazardField();
    return super.getField(name);
  }

  private static final Pattern PLACE_GPS_PTN = Pattern.compile("LAT: <([-+]?[.0-9]+)>  LONG: <([-+]?[.0-9]+)>");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      field = stripFieldEnd(field, " BORO");
      super.parse(field, data);
    }
  }

  private class MyHazardField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse("Hazard: " + field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern STREET_NO_PTN = Pattern.compile("^\\d+ ++(?!(?:AVE|RD|ST)\\b)");
  private static final Pattern HICKORY_KNLS_PTN = Pattern.compile("\\bHICKORY KNLS\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr, boolean cross) {
    if (cross) {
      addr = STREET_NO_PTN.matcher(addr).replaceFirst("");
    }
    addr = HICKORY_KNLS_PTN.matcher(addr).replaceAll("HICKORY KNOLLS LN");
    return addr;
  }
}
