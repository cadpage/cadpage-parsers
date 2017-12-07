package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMontgomeryCountyC2Parser extends FieldProgramParser {
  
  public OHMontgomeryCountyC2Parser() {
    super("MONTGOMERY COUNTY", "OH", 
          "Location:ADDR! Loc_Name:PLACE! Loc_Descr:PLACE! City:CITY! Building:APT! Subdivision:APT! Floor:APT! Apt/Unit:APT! Zip_Code:ZIP! Cross_Strs:X! Area:MAP! Sector:MAP/D! Beat:MAP/D! DASHES! INCIDENT:EMPTY! Inc_#:SKIP! Priority:PRI! Inc_Type:CODE! Descr:CALL! Mod_Circum:CALL/SDS! Created:TIMEDATE! Caller:NAME! Phone:PHONE! DASHES! UNITS_DISPATCHED:EMPTY! UNIT! DASHES! PERSONNEL_DISPATCHED:EMPTY! SKIP! COMMENTS:EMPTY! INFO/N+? DASHES!");
  }
  
  @Override
  public String getFilter() {
    return "@mcohiosheriff.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Dispatch Notification for Incident ([A-Z]{2}\\d{14})");
  private static final String MARKER = "--------------------------------------------------\n\n\t\t\tINCIDENT DETAILS\nLOCATION:\n";
  private static final Pattern DELIM = Pattern.compile("\\s*\n\\s*");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    if (!body.startsWith(MARKER)) return false;
    body = body.substring(MARKER.length());
    
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    if (name.equals("DASHES")) return new SkipField("-{10,}");
    return super.getField(name);
  }
  
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d\\d?:\\d\\d:\\d\\d [AP]M) (\\d\\d?/\\d\\d?/\\d{4})");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      setTime(TIME_FMT, match.group(1), data);
      data.strDate = match.group(2);
    }
  }
}