package net.anei.cadpage.parsers.IL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILDuPageCountyDParser extends FieldProgramParser {
  
  public ILDuPageCountyDParser() {
    super("DUPAGE COUNTY", "IL", 
          "Unit:UNIT! Address:ADDR! Info:CALL! Disp_at:DATETIME! CAD_Call:ID! END");
  }
  
  @Override
  public String getFilter() {
    return "acdccadmon@gmail.com";
  }
  
  private static final Pattern DELIM = Pattern.compile("\n-");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Unit ")) return false;
    return parseFields(DELIM.split(body), data);  
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
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
