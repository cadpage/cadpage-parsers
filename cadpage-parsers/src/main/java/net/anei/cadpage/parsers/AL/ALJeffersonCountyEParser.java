
package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Jefferson County, AL
 */
public class ALJeffersonCountyEParser extends FieldProgramParser {

  public ALJeffersonCountyEParser() {
    super("JEFFERSON COUNTY", "AL",
          "Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! http:SKIP! EMPTY+? Call_Date/Time:DATETIME! Call_Type:CALL! Call_Priority:PRI! Fire_Area:FIRE_AREA! INFO/N+ Alerts:ALERTS! INFO/N+ Narrative:INFO/N+ Incident_Numbers:ID! INFO2/N+");
  }
    
  @Override
  public String getFilter() {
    return "langg@jeffcoal911.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification")) return false;
    subject = subject.substring(26).trim();
    subject = stripFieldStart(subject, ":");
    data.strCall = subject;
    
    body = body.replace(" Call Priority:", "\nCall Priority:");
    body = body.replace('\t', '\n');
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d [AP]M", DATE_TIME_FMT, true);
    if (name.equals("FIRE_AREA")) return new MyInfoField("Fire Area:");
    if (name.equals("ALERTS")) return new MyInfoField("Alerts:");
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }
  
  private static final DateFormat DATE_TIME_FMT= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
  
  private class MyInfoField extends InfoField {
    
    private String label;
    
    public MyInfoField(String label) {
      this.label = label;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strSupp = append(data.strSupp, "\n", label+field);
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?:Times: +)?Unit: *(.*)");
  private static final Pattern DISPATCHED_TIME_PTN = Pattern.compile("Dispatched at: (\\d\\d?/\\d\\d?/\\d(4)) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      do {
        Matcher match = UNIT_PTN.matcher(field);
        if (match.matches()) {
          data.strUnit = append(data.strUnit, " ", match.group(1));
          break;
        }
        
        match = DISPATCHED_TIME_PTN.matcher(field);
        if (match.matches()) {
          data.strDate = match.group(1);
          setTime(TIME_FMT, match.group(2), data);
          break;
        }
        
        if (field.startsWith("Enroute at: ")) data.msgType = MsgType.RUN_REPORT;
        
      } while (false);

      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT DATE TIME INFO";
    }
  }
}
