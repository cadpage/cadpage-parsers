package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PAClarionCountyFParser extends FieldProgramParser {
  
  public PAClarionCountyFParser() {
    super("CLARION COUNTY", "PA", 
          "( Address:ADDRCITY! Type:CALL! Xstreets:X! Narrative:INFO! INFO/N+ Common_Name:PLACE Caller_Name:NAME Caller_Phone:PHONE GPS:GPS Units:UNIT END " + 
          "| Report_Status:SKIP? Date/Time:DATETIME! CFS:SKIP! Incident_Numbers:ID! Caller:NAME! Caller_Phone:PHONE! Address:ADDRCITY " + 
               "Common_Name:PLACE? ( Lat/Lon:GPS! | Latitude:GPS1! Longitide:GPS2! ) CrossStreets:X? Fire_Call_Type:CALL! EMS_Call_Type:CALL/SLS! " + 
               "Fire_Quadrant:MAP! EMS_District:MAP/SLS! Unit_Status_Times:TIMES! Narrative:INFO INFO/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "clarioncounty911@oes.clarion.pa.us,rwolbert@oes.clarion.pa.us,clarioncounty911@dps.clarion.pa.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DELIM = Pattern.compile("\\n+| (?=Caller Phone:|Longitide:|EMS District:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "!:");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("[Incident");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern CITY_BORO_PTN = Pattern.compile(" Boro$", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = CITY_BORO_PTN.matcher(data.strCity).replaceFirst("");
    }
  }
  
  private static final Pattern TIMES_UNIT_PTN = Pattern.compile("Unit: *(\\S+)\t");
  private static final Pattern TIMES_UNIT_PFX_PTN = Pattern.compile("(?:^|(?<=\\d\\d:\\d\\d:\\d\\d))\\d+: [A-Za-z0-9: ]+$");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String times = "";
      Matcher match1 = TIMES_UNIT_PTN.matcher(field);
      int st = 0;
      do {
        String part1;
        String unit;
        if (match1.find(st)) {
          unit = match1.group(1);
          part1 = field.substring(st,match1.start());
          st = match1.end();
        } else {
          unit = null;
          part1 = field.substring(st);
          st = field.length();
        }
        
        part1 = TIMES_UNIT_PFX_PTN.matcher(part1).replaceFirst("");
        if (part1.length() > 0) {
          for (String part2 : part1.split("\t")) {
            times = append(times, "\n", part2);
            if (part2.startsWith("Cleared at:")) data.msgType = MsgType.RUN_REPORT;
          }
        }
        
        if (unit != null) {
          times = append(times, "\n", "Unit: " + unit);
        }
      } while (st < field.length());
      
      if (data.msgType == MsgType.RUN_REPORT) {
        data.strSupp = append(times, "\n", data.strSupp);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
  
  private static final Pattern INFO_DATE_PTN = Pattern.compile("^\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}");
  private static final Pattern INFO_TIME_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d[a-z]+- +");
  private class MyInfoField extends InfoField {
    public void parse(String field, Data data) {
      field = INFO_DATE_PTN.matcher(field).replaceFirst("");
      for (String part : INFO_TIME_PTN.split(field)) {
        part = part.trim();
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
