package net.anei.cadpage.parsers.PA;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PAColumbiaCountyAParser extends FieldProgramParser {

  public PAColumbiaCountyAParser() {
    super(CITY_LIST, "COLUMBIA COUNTY", "PA",
          "E:ID! ET:CALL! ST:CALL! LOC:ADDR/S! MAP:MAP! T:TIME! N:NAME! PH:PHONE! S:SKIP! C:INFO!");
  }

  private static Pattern LUZERNE_COUNTY_PTN = Pattern.compile("\\bLUZERNE\\b");
  
  private String times;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    times = "";
    if (subject.startsWith("Times - ") || subject.startsWith("Notification - ")) data.msgType = MsgType.RUN_REPORT;
    if (!super.parseFields(body.split(","), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n", data.strSupp);
    }
    
    // If no city found, see if we can identify count from name field
    if (data.strCity.length() == 0) {
      if (LUZERNE_COUNTY_PTN.matcher(data.strName).find()) data.strCity = "LUZERNE COUNTY";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    String result = super.getProgram();
    if (getSelectValue().equals("1")) result = "CALL UNIT MAP " + result;
    return result;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
    
  private class MyIdField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      
      // Split field by blanks.  All but the last piece are regular units
      Parser p = new Parser(field);
      while (true) {
        String unit = p.getOptional(' ');
        if (unit.length() == 0) break;
        addUnit(unit, data);
      }
      field = p.get();
      
      // They combine primary unit and the call ID in the call ID field.
      // Splitting them out is tricky because number of digits in both 
      // fields is variable.  The key identifier is that the call ID
      // starts with the current year, so that is what we will check for
      Calendar cal = new GregorianCalendar();
      cal.setTime(new Date());
      int year = cal.get(Calendar.YEAR);
      int p1 = field.indexOf(Integer.toString(year));
      int p2 = field.indexOf(Integer.toString(year-1));
      int brk = (p2 < 0 ? p1 : p1 < 0 ? p2 : Math.min(p1, p2));
      if (brk > 0) {
        addUnit(field.substring(0,brk), data);
        data.strCallId = field.substring(brk);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }
  }

  private static Pattern NON_ASCII = Pattern.compile("[^ -~]");

  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      // remove non ASCII characters from call
      field = NON_ASCII.matcher(field).replaceAll("");
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private static Pattern CITY_PATTERN = Pattern.compile("(.*?) *(?:[:;\\[\\(] *(.*?)(?: BORO)?[\\)\\]]?)?");

  private class MyAddressField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher mat = CITY_PATTERN.matcher(field);
      if (!mat.matches()) abort();
      field = mat.group(1);
      data.strCity = getOptGroup(mat.group(2));
      if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
      if (data.strCity.length() > 0) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY";
    }
  }

  private static Pattern DATE_TIME_UNIT_PTN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}) (\\d{2}:\\d{2}:\\d{2}) '?([A-Z0-9_]+) : ([A-Z]+).*");

  private class MyInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      for (String part : field.split("\n")) {
        part = part.trim();
        Matcher mat = DATE_TIME_UNIT_PTN.matcher(part);
        if (mat.matches()) {
          times = append(times, "\n", part);
          if (data.strTime.length() == 0) {
            data.strDate = mat.group(1);
            data.strTime = mat.group(2);
          }
          addUnit(mat.group(3), data);
          if (mat.group(4).equals("AVAIL")) data.msgType = MsgType.RUN_REPORT;
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT INFO";
    }
  }
  
  /**
   * Add new unit to unit fields if it is not already present
   * @param unit new unit
   * @param data data object
   */
  private static void addUnit(String unit, Data data) {
    Pattern ptn = Pattern.compile("\\b" + unit + "\\b");
    if (ptn.matcher(data.strUnit).find()) return;
    data.strUnit = append(data.strUnit, " ", unit);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("TWO AND ONE HALF", "2 1/2");
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BEAVER TWP",
    "BENTON TWP",
    "BRIAR CREEK TWP",
    "CATAWISSA TWP",
    "CLEVELAND TWP",
    "CONYNGHAM TWP",
    "FISHING CREEK TWP",
    "FRANKLIN TWP",
    "GREENWOOD TWP",
    "HEMLOCK TWP",
    "JACKSON TWP",
    "LOCUST TWP",
    "MADISON TWP",
    "MAIN TWP",
    "MIFFLIN TWP",
    "MONTOUR TWP",
    "MOUNT PLEASANT TWP",
    "NORTH CENTRE TWP",
    "ORANGE TWP",
    "PINE TWP",
    "ROARING CREEK TWP",
    "SCOTT TWP",
    "SOUTH CENTRE TWP",
    "SUGARLOAF TWP",
    
    // Luzerne County
    "BEAR CREEK TWP",
    "BLACK CREEK TWP",
    "BUCK TWP",
    "BUTLER TWP",
    "CONYNGHAM TWP",
    "DALLAS TWP",
    "DENNISON TWP",
    "DORRANCE TWP",
    "EXETER TWP",
    "FAIRMONT TWP",
    "FAIRMOUNT TWP",
    "FAIRVIEW TWP",
    "FOSTER TWP",
    "FRANKLIN TWP",
    "HANOVER TWP",
    "HAZLE TWP",
    "HOLLENBACK TWP",
    "HUNLOCK TWP",
    "HUNTINGTON TWP",
    "JACKSON TWP",
    "JENKINS TWP",
    "KINGSTON TWP",
    "LAKE TWP",
    "LEHMAN TWP",
    "NESCOPECK TWP",
    "NEWPORT TWP",
    "PITTSTON TWP",
    "PLAINS TWP",
    "PLYMOUTH TWP",
    "RICE TWP",
    "ROSS TWP",
    "SALEM TWP",
    "SLOCUM TWP",
    "SUGARLOAF TWP",
    "UNION TWP",
    "WILKES-BARRE TWP",
    "WRIGHT TWP"

  };
}