package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class ALHooverAParser extends FieldProgramParser {

  public ALHooverAParser() {
    super("HOOVER", "AL",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDRCITY! ADDITIONAL_INFORMATION:INFO! ID:ID! PRI:PRI! DATE:DATETIME! RUN_#:ID? MAP:MAP! UNIT:UNIT! ( CROSSSTREET:X! INFO:INFO/N! INFO/N+ | INFO:INFO/N! INFO/N+ CROSSSTREET:X! )");
  }

  @Override
  public String getFilter() {
    return "dispatchNWPS@ci.hoover.al.us,dispatchNWPS@hooveralabama.gov,arns@shelby911.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!CAD ALERT!")) return false;
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d)");
  private class  MyDateTimeField extends DateTimeField {
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
}
