package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJMorrisCountyBParser extends FieldProgramParser {

  public NJMorrisCountyBParser() {
    super("MORRIS COUNTY", "NJ",
          "CAD:ID! Dt:DATETIME! Loc:ADDR! Cross:X! Apt:APT! Mun:CITY! CFS:CALL! Cmnts:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "donotreply@lawsoftweb.onmicrosoft.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d(:\\d\\d)? [AP]M)");
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("hh:mm aa");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      DateFormat fmt = match.group(3) != null ? TIME_FMT1 : TIME_FMT2;
      setTime(fmt, match.group(2), data);
    }
  }
}
