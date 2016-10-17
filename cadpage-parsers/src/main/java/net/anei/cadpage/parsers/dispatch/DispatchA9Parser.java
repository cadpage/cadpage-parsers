package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA9Parser extends FieldProgramParser {
  
  private static final String START_MARKER = "Rip and Run Report\n\n~\n";
  
  public DispatchA9Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }
  
  public DispatchA9Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "Location_Information:INFO? Location:ADDR/SXa! Additional_Location_Information:APT? INFO/N Common_Name:PLACE? Venue:CITY? SKIP X Call_Information:SKIP? Phone:PHONE? Station:SRC? Quadrant:MAP? District:MAP? Call_Number:ID! Call_Type:CALL! Priority:PRI Caller:NAME TIMES+? Incident_Number(s)%EMPTY Units_Sent:SKIP UNIT Narrative:SKIP INFO2/N+");
  }
  
  String times;

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\t", " ");
    int pt = body.indexOf(START_MARKER);
    if (pt < 0) return false;
    if (pt > 0 && body.charAt(pt-1)!='\n') return false;
    
    body = body.substring(pt+START_MARKER.length()).trim();
    body = body.replace('~', ' ');
    times = "";
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    times = null;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("TIMES")) return new BaseTimesField();
    if (name.equals("INFO2")) return new BaseInfo2Field();
    return super.getField(name);
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class BaseMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strMap)) return;
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  private Pattern TIMES_PTN = Pattern.compile("([A-Za-z/ ]+) Date/Time:(?: *(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d))?");
  private class BaseTimesField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (!match.matches()) return;
      times = append(times, "\n", field);
      String type = match.group(1).trim();
      if (type.equals("Dispatch")) {
        data.strDate = match.group(2);
        data.strTime = match.group(3);
      } else if (type.equals("Clear") && match.group(2) != null) {
        data.msgType = MsgType.RUN_REPORT;
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
  
  private static final Pattern INFO_MARKER = Pattern.compile("^\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d ");
  private class BaseInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_MARKER.matcher(field);
      if (!match.lookingAt()) return;
      field = field.substring(match.end()).trim();
      super.parse(field, data);
    }
  }
}
