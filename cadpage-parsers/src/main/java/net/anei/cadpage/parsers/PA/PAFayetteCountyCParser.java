package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAFayetteCountyCParser extends FieldProgramParser {
  
  public PAFayetteCountyCParser() {
    super("FAYETTE COUNTY", "PA", 
          "( Date/Time:DATETIME! Call_Type:CALL! Location:ADDRCITY! Additional_Location:ADD_INFO! Cross_Streets:X! Caller_Name:NAME! " + 
              "( Narrative:INFO! INFO/N+ Units:UNIT! Talkgroup:CH! END " + 
              "| Caller_Phone:PHONE? Units:UNIT! Talkgroup:CH! Narrative:INFO! Common_Name:PLACE? Latitude:GPS1? Longitude:GPS2? INFO/N+ ) " + 
          "| Call_Time:DATETIME! Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE! Closest_Intersection:X! Additional_Location_Info:ADD_INFO! " + 
              "Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! Priority:PRI! Status:SKIP! Quadrant:MAP! District:MAP/L! Beat:MAP/L! " + 
              "CFS_Number:ID! Primary_Incident:SKIP! Radio_Channel:CH! Narrative:INFO! INFO/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@fcema.org,messaging@iamresponding.com,alerts@fcema.ealertgov.com";
  }
  
  private static final Pattern MARKER = Pattern.compile("(?:(?:FAYETTE|Fayette)-911/[A-Za-z0-9/\\\\]+\n)?\\[Fayette911\\]\n");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String[] parts = subject.split("\\|");
    if (parts.length > 1) {
      subject = parts[0].trim();
      body = '[' + parts[1].trim() + "]\n" + body;
    }
    if (!subject.equals("Dispatch")) {
      data.strSource = subject;
    }
    
    if (!body.startsWith("Call Time:")) {
      Matcher match = MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      body = body.substring(match.end()).trim();
    }
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADD_INFO")) return new MyAddInfoField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
    }
  }
  
  private static final Pattern ADD_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) +(.*)|[A-Z]|\\d{1,4}");
  private class MyAddInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ADD_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
        return;
      }
      data.strPlace = field;
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
