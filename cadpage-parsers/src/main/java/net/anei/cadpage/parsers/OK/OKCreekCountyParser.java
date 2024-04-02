package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKCreekCountyParser extends FieldProgramParser {

  public OKCreekCountyParser() {
    super("CREEK COUNTY", "OK",
          "CFS:ID! CALL! ADDRCITYST! GPS! PHONE Received:DATETIME! INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("^(?:Call Notes:|\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d -[A-Z]+)\\b *| *\\[[A_Z]+\\]$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }

}
