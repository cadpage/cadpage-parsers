package net.anei.cadpage.parsers.AR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ARBentonCountyAParser extends FieldProgramParser {

  public ARBentonCountyAParser() {
    super("BENTON COUNTY", "AR",
          "Fire_Box:BOX! Location:ADDRCITYST! Latitude:GPS1! Longitude:GPS2! Cross_streets:X! CFS_#:ID! Details:INFO! Units:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@bentoncountyar.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    body = body.replace(" CFS # ", " CFS #:");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NULL")) return;
      super.parse(field, data);
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
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
}
