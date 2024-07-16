package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLSantaRosaCountyParser extends FieldProgramParser {

  public FLSantaRosaCountyParser() {
    super("SANTA ROSA COUNTY", "FL",
          "( Location:ADDR_GPS! X! Nature_Code:CALL! SKIP+? COUNTY ID! " +
          "| NAT:CALL! LOC:ADDR_GPS! X! " +
          "| Nature:CALL! Address:ADDR City:CITY! Placename:PLACE! Caution_Notes:ALERT! Comments:INFO! INFO/N+ Latitude:GPS1! Longitude:GPS2! " +
                  "Primary_Agency:SRC! Agencies_Assigned:SKIP! Units:UNIT! Incident_ID:ID! Priority:PRI! Opened:SKIP! Caller_Phone_Number:PHONE! " +
                  "Call_Back_Name:NAME! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "@santarosa911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_GPS")) return new MyAddressGPSField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("COUNTY")) return new SkipField("County ?#", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("(.*?) +<https?://maps.google.com/maps\\?q=(.*)>");
  private class MyAddressGPSField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        setGPSLoc(match.group(2), data);
      }
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT GPS";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "(");
      field = stripFieldEnd(field, ")");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
