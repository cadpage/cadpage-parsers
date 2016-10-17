package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TXHaysCountyBParser extends FieldProgramParser {
  
  public TXHaysCountyBParser() {
    super("HAYS COUNTY", "TX", 
          "SRC_ID CALL ADDR CITY_ST BOX INFO1 UNIT GPS1 GPS2 CODE TIME_LOG!");
  }
  
  @Override
  public String getFilter() {
    return "noreply@sanmarcostx.gov,noreply@cisusa.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("; ", 11), 11, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC_ID")) return new MySourceIdField();
    if (name.equals("CITY_ST")) return new MyCityStateField();
    if (name.equals("BOX")) return new BoxField("|Alrm Box +(.*)", true);
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("UNIT")) return new UnitField("|Units +(.*)", true);
    if (name.equals("GPS1")) return new GPSField(1, "|Lat +(.*)", true);
    if (name.equals("GPS2")) return new GPSField(2, "|Lon +(.*)", true);
    if (name.equals("CODE")) return new CodeField("|Code +(.*)", true);
    if (name.equals("TIME_LOG")) return new MyTimeLogField();
    return super.getField(name);
  }
  
  private static final Pattern SRC_ID_PTN = Pattern.compile("([A-Z]{2,6}) (\\d{12})");
  private class MySourceIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = SRC_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1);
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }
  
  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*?) +([A-Z]{2})");
  private class MyCityStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strState = match.group(2);
      }
      data.strCity = field;
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("All") || field.length() == 0) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  private static final Pattern TIME_LOG_PTN = Pattern.compile("Time Reported: (\\d\\d:\\d\\d:\\d\\d) *(?:Time Completed: (\\d\\d:\\d\\d:\\d\\d) *)?(?:Logs +<<Logs>>)? *(.*)");
  private static final Pattern LOG_BRK_PTN = Pattern.compile(" +(?=\\d\\d:\\d\\d:\\d\\d +)");
  private class MyTimeLogField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_LOG_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      String compTime = match.group(2);
      if (compTime != null) {
        data.msgType = MsgType.RUN_REPORT;
        String times = "Time Reported: " + data.strTime + "\nTime Completed: " + compTime;
        data.strSupp = append(times, "\n", data.strSupp);
      }
      String logs = LOG_BRK_PTN.matcher(match.group(3)).replaceAll("\n");
      data.strSupp = append(data.strSupp, "\n",  logs);
    }
    
    @Override
    public String getFieldNames() {
      return "TIME INFO";
    }
    
  }
}
