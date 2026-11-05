package net.anei.cadpage.parsers.MI;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIWayneCountyDParser extends FieldProgramParser {

  public MIWayneCountyDParser() {
    super("WAYNE COUNTY", "MI",
          "MARK SRC MAP ADDRCITY/S6 GPS? X SKIP CALL NAME PHONE SKIP! INFO/N+? ID ID/L+? UNIT UNIT/C+? DATETIME! END");
  }

  @Override
  public String getFilter() {
    return "noreply-TylerCAD@Notifications.michigan.gov";
  }

  private Set<String> idSet, unitSet;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    idSet = new HashSet<>();
    unitSet = new HashSet<>();
    try {
      return parseFields(body.split("\n"), data);
    } finally {
      idSet = null;
      unitSet = null;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField("Michigan State Police", true);
    if (name.equals("MAP")) return new MapField("\\d{3,4}", true);
    if (name.equals("ADDRCITY"))  return new MyAddressCityField();
    if (name.equals("GPS")) return new GPSField("[-+]?\\d+\\.\\d+/[-+]?\\d+\\.\\d+", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("--", "/");
      super.parse(field, data);
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("\\d{4}-\\d{8} \\([A-Z]*\\d+\\)");

  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      StringBuilder sb = new StringBuilder(data.strCallId);
      for (String part : field.split(",")) {
        part = part.trim();
        if (!ID_PTN.matcher(part).matches()) return false;
        if (idSet.add(part)) {
          if (!sb.isEmpty()) sb.append(',');
          sb.append(part);
        }
      }
      data.strCallId = sb.toString();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      StringBuilder sb = new StringBuilder(data.strUnit);
      for (String part : field.split(",")) {
        part = part.trim();
        if (unitSet.add(part)) {
          if (!sb.isEmpty()) sb.append(',');
          sb.append(part);
        }
      }
      data.strUnit = sb.toString();
    }

  }
}
