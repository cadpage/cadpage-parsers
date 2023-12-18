package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class ALRussellCountyBParser extends FieldProgramParser {

  public ALRussellCountyBParser() {
    super("RUSSELL COUNTY", "AL",
          "ADDRESS:ADDRCITYST! CROSS_STREETS:X! Y:GPS1! X:GPS2! AGENCY:SRC! OCA:NONE! CFS:ID! INCIDENT:CALL! DETAILS:INFO! DATE/TIME:DATETIME! END");
  }

  @Override
  public String getFilter() {
    return "cad@covington911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("NONE")) return new SkipField("None");
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
