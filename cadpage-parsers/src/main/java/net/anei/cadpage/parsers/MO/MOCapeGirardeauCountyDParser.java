package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOCapeGirardeauCountyDParser extends FieldProgramParser {

  public MOCapeGirardeauCountyDParser() {
    super("CAPE GIRARDEAU COUNTY", "MO",
          "CALL CALL/SDS ID ADDRCITYST GPS INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-replyzuercher@cityofcapegirardeau.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CFS - Unit Assigned - #")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("NONE_NONE")) return new SkipField("None None", true);
    if (name.equals("ID")) return new IdField("[A-Z]+\\d{8}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None None")) return;
      int pt = field.indexOf(" OTHER CALLS:");
      if (pt >= 0) field =  field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
