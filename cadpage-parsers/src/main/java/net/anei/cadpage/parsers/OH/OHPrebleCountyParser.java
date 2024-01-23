package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHPrebleCountyParser extends FieldProgramParser {

  public OHPrebleCountyParser() {
    super("PREBLE COUNTY", "OH",
          "Nature:CODE! Description:CALL! Responding_Agency:SRC! Units:UNIT! Location:ADDRCITYST! " +
              "Added_Location_Details:PLACE! Common_/_Business_Name:PLACE! Cross_Streeets:X! Nearest_intersction:X! Call_Details_/_Notes:INFO! Use_Caution:ALERT END");
  }

  @Override
  public String getFilter() {
    return "services@preblecountysheriff.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("From Preble County")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ',').replace(" ", "");
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ',').replace(" ", "");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strCross.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
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
      if (field.equalsIgnoreCase("YES")) field = "*** USE CAUTION ***";
      super.parse(field, data);
    }
  }
}
