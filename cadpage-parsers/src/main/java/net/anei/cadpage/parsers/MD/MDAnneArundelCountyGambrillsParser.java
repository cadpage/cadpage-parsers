package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDAnneArundelCountyGambrillsParser extends FieldProgramParser {

  public MDAnneArundelCountyGambrillsParser() {
    super("ANNE ARUNDEL COUNTY", "MD",
          "CODE:CODE! CALL:CALL! ADDR:ADDR/SXP! CITY:CITY! GPS:GPS! TIME:TIME! DATE:DATE! X:X! ID:ID! MAP:MAP! UNIT:UNIT! CH:CH! BOX:BOX! INFO:EMPTY! INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("DATE")) return new MyDateField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d)(\\d\\d)");

  private static String cvtDateTime(String field, char sep) {
    Matcher match = DATE_TIME_PTN.matcher(field);
    if (!match.matches()) return null;
    return match.group(1) + sep + match.group(2);
  }

  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      field = cvtDateTime(field, ':');
      if (field == null) abort();
      data.strTime = field;
    }
  }

  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      field = cvtDateTime(field, '/');
      if (field == null) abort();
      data.strDate = field;
    }
  }

}
