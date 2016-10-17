package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILMcHenryCountyParser extends FieldProgramParser {
  
  public ILMcHenryCountyParser() {
    super("MCHENRY COUNTY", "IL",
           "OCA:ID? Type:CALL! OCA:ID? Date:DATETIME! Loca:ADDR! Apt:APT? City:CITY! Cros:X INFO+ Dist:MAP NAR:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "DoNotReply@mcetsb.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith(" Dispatch")) return false;
    data.strSource = subject.substring(0,subject.length()-9).trim();
    if (!body.startsWith("#\n")) return false;
    body = body.substring(2).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[\\d\\d/\\d\\d/\\d{4} .*\\]|\\[.* are related\\.\\]|\\*\\* EMD .*|\\(Cloned from.*\\)");
  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|#) *(.*)");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("\\d{3}[- ]\\d{3}[- ]\\d{4}");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("ALI [XY] Coordinate: ([-+]?\\d+\\.\\d{4,})");
  private class MyInfoField extends InfoField {
    
    private String gpsLoc;
    
    @Override
    public void parse(String field, Data data) {
      
      if (data.strSupp.length() == 0) gpsLoc = null;
      
      field = stripFieldEnd(field, ",");
      
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      
      if (field.startsWith("Landmark:")) {
        field = stripFieldStart(field, ",");
        field = stripFieldStart(field, "-");
        data.strPlace = append(data.strPlace, " - ", field.substring(9).trim());
        return;
      }
      
      field = stripFieldStart(field, "Landmark Comment:");
      field = stripFieldStart(field, "Geo Comment:");
      
      Matcher match = INFO_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return;
      }
      
      if (INFO_PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
        return;
      }
      
      match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        String gps = match.group(1);
        if (gpsLoc == null) {
          gpsLoc = gps;
        } else {
          setGPSLoc(gpsLoc+','+match.group(1), data);
          gpsLoc = null;
        }
        return;
      }

      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE PHONE APT GPS";
    }
  }
}
