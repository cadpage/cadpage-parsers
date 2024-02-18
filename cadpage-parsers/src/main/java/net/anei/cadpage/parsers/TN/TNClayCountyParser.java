package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNClayCountyParser extends FieldProgramParser {

  public TNClayCountyParser() {
    this("CLAY COUNTY", "TN");
  }

  protected TNClayCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "CFS_Number:ID! How_Received:SKIP! Call_Taker:SKIP! Incident:CALL! Add'l_Incident_Code:CODE! Call_Details:INFO! Caution?:ALERT! " +
                "Lat:GPS1! Long:GPS2! Name:NAME! Address:ADDRCITYST! Common_Name:PLACE! Cross_Streets:X! Calling_Number:PHONE! " +
                "Location_Details:PLACE! Law_Zone:MAP! Departments:SRC! END");
  }

  @Override
  public String getFilter() {
    return "serviceacct@opecd.com";
  }

  @Override
  public boolean parseFields(String[] flds, Data data) {
    for (int ndx = 0; ndx < flds.length; ndx++) {
      flds[ndx] = stripFieldEnd(flds[ndx], "None");
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - Log - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("No")) return;
      if (field.equalsIgnoreCase("Yes")) field = "** USE CAUTION **";
      super.parse(field, data);
    }
  }
}
