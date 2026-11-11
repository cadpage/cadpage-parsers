package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDSaintMarysCountyDParser extends FieldProgramParser {

  public MDSaintMarysCountyDParser() {
    super("SAINT MARYS COUNTY", "MD",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! DST:ST! MAP:MAP! INFO:INFO! INFO/N+ DATE:DATETIME! CFS:ID! END");
  }

  @Override
  public String getFilter() {
    return "mvfdcompany2@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body , Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)T(\\d\\d:\\d\\d:\\d\\d)");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }
}
