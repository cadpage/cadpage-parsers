package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Onondaga County, NY (Variant B)
 */
public class NYOnondagaCountyBParser extends FieldProgramParser {
  
  private boolean skipDupEventInfo = false;

  public NYOnondagaCountyBParser() {
    super("ONONDAGA COUNTY", "NY",
           "DATE TIME ID SKIP CALL ADDR X PLACE INFO! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "adi@mcfd.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    skipDupEventInfo = false;
    return parseFields(body.split("\n"), 9, data);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("-")) field = field.substring(0,field.length()-1);
      else {
        int pt = field.indexOf('-');
        if (pt >= 0) {
          data.strApt = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private static Pattern REMOVE_INFO_PTN = Pattern.compile("\\bResponder script:|\\(Specific Diagnosis\\)\\.|\\bCaller Statement:|\\b\\d\\d(?:/\\d\\d){0,2} \\d\\d:\\d\\d:\\d\\d\\b|'Unsubscribe'");
  private static Pattern SKIP_INFO_PTN = Pattern.compile("|SPECIAL ADDRESS COMMENT:|ACCESS INFORMATION:|_+|\\[.*\\]|Reconfigured on another card:.*|.*\\bPATROL_.*|.*_BEAT\\b.*");
  private static Pattern PRIORITY_PTN = Pattern.compile("Response text: ([A-Z]).*");
  private static Pattern NAME_PTN = Pattern.compile("[A-Z]+(?: [A-Z]+)?, ?[A-Z]+(?: [A-Z]+)?");
  private static Pattern UNIT_PTN = Pattern.compile("([A-Z]+[0-9]+(?:,[A-Z]+[0-9]+)*),?");
  private static Pattern CHANNEL_PTN = Pattern.compile("[A-Z0-9]*TAC[A-Z0-9]*");
  private static Pattern SRC_PTN = Pattern.compile("[A-Z]{2}F(?: +[A-Z]{3})*");
  private static Pattern MAP_GPS_PTN = Pattern.compile("([A-Z]+ (?:- )?Sector) ([-+]?\\d+\\.\\d{6} [-+]?\\d+\\.\\d{6})", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      if (skipDupEventInfo) {
        if (field.startsWith("End of Duplicate")) skipDupEventInfo = false;
        return;
      }
      
      if (field.startsWith("Duplicate Event:")) {
        skipDupEventInfo = true;
        return;
      }
      
      
      field = REMOVE_INFO_PTN.matcher(field).replaceAll("").trim().replaceAll("  +", " ");
      field = field.replace("Number of patients:", "#PAT:");
      if (SKIP_INFO_PTN.matcher(field).matches()) return;
      if (data.strName.length() == 0) {
        Matcher match = NAME_PTN.matcher(field);
        if (match.matches()) {
          data.strName = field;
          return;
        }
      }
      if (data.strPriority.length() == 0) {
        Matcher match = PRIORITY_PTN.matcher(field);
        if (match.matches()) {
          data.strPriority = match.group(1);
          return;
        }
      }
      Matcher match = CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = append(data.strChannel, ",", field);
        return;
      }
      match = UNIT_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = append(data.strUnit, ",", match.group(1));
        return;
      }
      match = SRC_PTN.matcher(field);
      if (match.matches()) {
        data.strSource = append(data.strSource, " ", field);
        return;
      }
      match = MAP_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strMap = match.group(1);
        setGPSLoc(match.group(2), data);
        return;
      }
      field = cleanWirelessCarrier(field);
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO MAP GPS PRI CH UNIT SRC NAME";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new NameField("");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
}
	