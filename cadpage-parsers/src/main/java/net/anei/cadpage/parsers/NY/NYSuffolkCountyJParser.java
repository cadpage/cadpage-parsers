package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYSuffolkCountyJParser extends FieldProgramParser {
  
  public NYSuffolkCountyJParser() {
    super(CITY_CODES, "SUFFOLK COUNTY", "NY", 
          "Case#:ID! Alarm_Level:PRI! CFS_Description:CALL! Location:ADDRCITY! Common_Place:PLACE! Cross_Streets:X! Juris:MAP! Municipality:CITY! Incident_Recv_DateTime:DATETIME! Contain_DateTime:EMPTY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@southamptontownny.gov";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Incident# : (\\S+) Resource# : (\\S+) For Activity Dispatch Resource");
  private static final Pattern DELIM = Pattern.compile("\n+|\\|");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strUnit = match.group(2);
    
    int pt = body.indexOf("\nThis email, ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    body = body.replace(" :- ", ":");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("=>> Comment Record|Comment DateTime:.*|Origin ID:.*|-*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CA RH", "CALVERTON",
      "EQ SH", "EAST QUOGUE",
      "FL SH", "FLANDERS",
      "NH SH", "NORTHAMPTON",
      "RI RH", "RIVERHEAD",
      "RS SH", "RIVERSIDE"
  });
}
