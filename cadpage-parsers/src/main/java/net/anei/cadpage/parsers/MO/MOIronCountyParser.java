package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOIronCountyParser extends FieldProgramParser {
  
  public MOIronCountyParser() {
    super("IRON COUNTY", "MO", 
          "ADDR CALL! Description:INFO! INFO/N+ Dispatch:DATETIME! UNIT! Call_Number:ID END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@wccd911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD CALL")  && !subject.equals("WCCD Call Report")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d)( [AP]M)?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Dispatch:");
      if (pt >= 0) field = field.substring(0,pt).trim();
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      String aa = match.group(3);
      if (aa != null) {
        setTime(TIME_FMT, time + aa, data);
      } else {
        data.strTime = time;
      }
    }
  }
}
