package net.anei.cadpage.parsers.TN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNWilliamsonCountyCParser extends FieldProgramParser {
  
  public TNWilliamsonCountyCParser() {
    super("WILLIAMSON COUNTY", "TN", 
          "PN:CALL! ADD:ADDR! CITY:CITY! LAT:GPS1! LON:GPS2! APT:APT! X_ST:X! LOCATION:PLACE! TIME:DATETIME! CN:ID! UNITS:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "911-Center@williamson-tn.org";
  }
  
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=[ap]m)(?=CN:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = MISSING_BLANK_PTN.matcher(body).replaceFirst(" ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d [ap]m)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
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
