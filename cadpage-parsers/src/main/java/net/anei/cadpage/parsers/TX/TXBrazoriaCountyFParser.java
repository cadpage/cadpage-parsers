package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBrazoriaCountyFParser extends FieldProgramParser {

  public TXBrazoriaCountyFParser() {
    super("BRAZORIA COUNTY", "TX",
          "CALL:CALL! TIME:TIMEDATE! ADDR:ADDR! CITY:CITY! INFO:SKIP! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("; "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    return super.getField(name);
  }

  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) on (\\d\\d/\\d\\d/\\d\\d)");
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      data.strDate = match.group(2);
    }
  }
}
