package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA61Parser extends FieldProgramParser {

  private String prefix;

  public DispatchA61Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }
 
  public DispatchA61Parser(String prefix, String defCity, String defState) {
    super(defCity, defState,
          "Case#:SKIP! Julian_Incident#:ID! Priority:PRI? Agency_Type:SKIP! Agency_Code:SRC? Alarm_Level:SKIP! receive_Source:SKIP! Primary_Officer:SKIP! Agency_Code:SRC? CFS:CODE! CFS_Description:CALL! Location:ADDR! Common_Place:PLACE! Cross_Streets:X! Zone:MAP! Grid:MAP? Area_Of_Responsibility:SKIP! Juris:SKIP! Municipality:CITY! Incident_Recv_DateTime:DATETIME! INFO+");
    this.prefix = prefix;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Incident# : ([A-Z0-9]+) (?:Resource# : [A-Z0-9]+ )?For Activity (Dispatch|Finish|Arrive)(?: Resource)?");
  private static final Pattern DELIM_PTN = Pattern.compile("\n\n?|\\|");
  
  private enum InfoType {COMMENT, NAME, ACTIVITY };
  private InfoType infoType;
  private  String times;
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    if (!match.group(2).equals("Dispatch")) data.msgType = MsgType.RUN_REPORT;
    
    if (prefix != null) {
      if (!body.startsWith(prefix)) return false;
      body = body.substring(prefix.length()).trim();
    }
    
    infoType = null;
    times = "";
    body = body.replace(":-", ":");
    if (!parseFields(DELIM_PTN.split(body), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n\n", data.strSupp); 
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("/")) field = field.substring(1).trim();
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern MULT_BLANKS_PTN = Pattern.compile(" {2,}");
  private class MyInfoField extends InfoField {
    
    private String unit, status;
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("=>> ")) {
        field = field.substring(4).trim();
        if (field.equals("Comment Record")) {
          infoType = InfoType.COMMENT;
        } else if (field.equals("Name Record")) {
          infoType = InfoType.NAME;
        } else if (field.equals("Resource Activity Record")) {
          infoType = InfoType.ACTIVITY;
          unit = status = null;
        } else {
          infoType = null;
        }
        return;
      }
      
      if (infoType == null) return;
      int pt = field.indexOf(':');
      if (pt < 0) return;
      String key = field.substring(0,pt).trim();
      String value = field.substring(pt+1).trim();
      switch (infoType) {
      
      case COMMENT:
        if (key.equals("Comment")) {
          data.strSupp = append(data.strSupp, "\n", value);
        }
        break;
        
      case NAME:
          if (key.equals("First Name")) {
            data.strName = value;
          } else if (key.equals("Last Name")) {
            data.strName = append(value, ", ", data.strName);
          } else if (key.equals("Middle Name")) {
            data.strName = append(data.strName, " ", value);
            data.strName = MULT_BLANKS_PTN.matcher(data.strName).replaceAll(" ");
            data.strName = cleanWirelessCarrier(data.strName);
          } else if (key.endsWith("Phone")) {
            data.strPhone = append(data.strPhone, " / ", value);
          }
        break;

      case ACTIVITY:
        if (key.equals("Resource#")) {
          unit = status;
          status = value;
        } else if (key.equals("Resource Status")) {
          if (value.equals("FI")) {
            unit = status;
            status = "Finish";
          }
        } else if (key.equals("Status DateTime")) {
          if (unit != null && status != null) {
            times = append(times, "\n", value + "  " + unit + "  " + status);
          }
          unit = status = null;
        }
        break;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO NAME PHONE";
    }
  }
}