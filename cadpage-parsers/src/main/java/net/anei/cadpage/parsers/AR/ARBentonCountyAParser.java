package net.anei.cadpage.parsers.AR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ARBentonCountyAParser extends FieldProgramParser {

  public ARBentonCountyAParser() {
    super("BENTON COUNTY", "AR",
          "( SELECT/1 Fire_Box:BOX! | CALL ) Location:ADDRCITYST! Latitude:GPS1! Longitude:GPS2! Cross_streets:X! CFS_#:ID! " +
                "Details:INFO! ( SELECT/1 Units:UNIT! END | INFO/N+? NONE UNIT NONE END )");
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
    body = body.replace("CFS # ", "CFS #:");
    if (subject.startsWith("CFS - Unit Assigned - ")) {
      setSelectValue("2");
      return super.parseFields(body.split("\n"), data);
    } else {
      setSelectValue("1");
      data.strCall = subject;
      return super.parseMsg(body, data);
    }
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("NONE"))  return new SkipField("None", true);
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

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
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
