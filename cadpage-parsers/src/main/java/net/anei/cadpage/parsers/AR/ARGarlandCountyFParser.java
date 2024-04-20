package net.anei.cadpage.parsers.AR;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARGarlandCountyFParser extends FieldProgramParser {


  public ARGarlandCountyFParser() {
    super("GARLAND COUNTY", "AR",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! City:CITY! State:ST! Zip_Code:ZIP! Cross-Street:X! ID:ID! DATE:DATETIME! INFO:INFO! INFO/N+ Cross-Street:SKIP");
  }

  private static final Pattern DELIM = Pattern.compile("\n|\\s+(?=State:|Zip Code:)");

  @Override
  public String getFilter() {
    return "dispatch@hotspringdem.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
}
