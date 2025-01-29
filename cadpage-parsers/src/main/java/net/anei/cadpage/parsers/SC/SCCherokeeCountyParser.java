package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class SCCherokeeCountyParser extends FieldProgramParser {

  public SCCherokeeCountyParser() {
    super("CHEROKEE COUNTY", "SC",
          "Location:PLACE! Address:ADDRCITYST! Cross_Streets:X! Address_Details:INFO! Call_Type:CALL! Call_Details:INFO/N! Pro_Qa_AGE:INFO/N! Pro_Qa_summary:INFO/N! External_Number:ID!");
  }

  @Override
  public String getFilter() {
    return "zuercher@cherokeecountysheriff.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern TRAIL_UNIT_GPS_PTN = Pattern.compile(" +Units (.*?) ([-+]?\\d{2}\\.\\d{6} [-+]?\\d{2}\\.\\d{6}|None None)$");

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nCONFIDENTIALITY NOTICE:");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = TRAIL_UNIT_GPS_PTN.matcher(body);
    if (!match.find()) return false;
    data.strUnit = match.group(1).trim().replace("; ", ",");
    body = body.substring(0, match.start());
    setGPSLoc(match.group(2), data);
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT GPS";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Intersection of ");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", None");
      super.parse(field, data);
    }
  }
}
