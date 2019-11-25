package net.anei.cadpage.parsers.CO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CODouglasCountyCParser extends FieldProgramParser {
  
  public CODouglasCountyCParser() {
    super("DOUGLAS COUNTY", "CO", 
          "CALL! LOC_Name:ADDR! Units:UNIT! Time:DATETIME! Quadrant:MAP! GPS! END");
  }
  
  @Override
  public String getFilter() {
    return "cmcnay@dcsheriff.net";
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
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\S{3} (\\S{3} \\d\\d) (\\d\\d:\\d\\d:\\d\\d) \\S{3} (\\d{4})");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("MMM dd yyyy");
  private class MyDateTimeField extends DateTimeField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      setDate(DATE_FMT, match.group(1) + ' ' + match.group(3), data);
      data.strTime = match.group(2);
    }
  }
}
