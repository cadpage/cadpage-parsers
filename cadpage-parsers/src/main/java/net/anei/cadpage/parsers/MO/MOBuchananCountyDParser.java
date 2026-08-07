package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class MOBuchananCountyDParser extends FieldProgramParser {

  public MOBuchananCountyDParser() {
    super("BUCHANAN COUNTY", "MO",
          "Call_Type:CALL! Common_Name:PLACE? Call_Address:ADDRCITYST! Latitude:GPS1! Longitude:GPS2! Case_Number:ID! Call_Date/Time:DATETIME! Units_Assigned:UNIT! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cad@tblsys.com";
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        String call1 = field.substring(0,pt).trim();
        String call2 = field.substring(pt+3);
        if (call1.contains(call2)) {
          field = call1;
        } else if (call2.contains(call1)) {
          field = call2;
        }
      }
      super.parse(field, data);
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", USA");
      super.parse(field, data);
      int pt = data.strAddress.indexOf(',');
      if (pt >= 0) data.strAddress = data.strAddress.substring(0,pt).trim();
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\[\\d\\d:\\d\\d\\] *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
