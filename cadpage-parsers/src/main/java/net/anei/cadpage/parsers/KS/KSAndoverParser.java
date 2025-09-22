package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class KSAndoverParser extends FieldProgramParser {

  public KSAndoverParser() {
    super("ANDOVER", "KS",
          "CALL ADDRCITYST PLACE! ( Call_Details:INFO! INFO/N+? GPS1 GPS2! X/Z! CAD#:ID! Call_Time:DATETIME! " +
                                 "| INFO/N+? GPS1 GPS2 X/Z ID/Z DATETIME! ) EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "noreply@andoverks.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty() || !body.startsWith(subject)) return false;
    return parseFields(body.split(";", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("\\d+", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern INFO_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) {
        data.strSupp = append(data.strSupp, "\n", match.group(1));
      } else {
        data.strSupp = append(data.strSupp, "; ", field);
      }
    }
  }
}
