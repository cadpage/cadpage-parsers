package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class OHButlerCountyAParser extends FieldProgramParser {
  public OHButlerCountyAParser() {
    super("BUTLER COUNTY", "OH",
          "Master_Incident_Number:ID! Response_Date:DATETIME! Response_Area:MAP Incident_Type:CODE Problem:CALL! Priority_Number:SKIP Priority_Description:PRI Location_Name:PLACE! Address:ADDR! Apartment:APT! City:CITY! Location_Type:SKIP Longitude:GPS3/d! Latitude:GPS3/d! Cross_Street:X! Call_Back_Phone:PHONE! Caller_Name:NAME! Caller_Location_Name:SKIP! INFO/N+ All_Units_Assigned:UNIT! Case_Number:ID! EMD_Used:SKIP",
          FLDPROG_ANY_ORDER);
  }

  @Override
  public String getFilter() {
    return "noreply@butlersheriff.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String times;

  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("BCSO Dispatch")) return false;
    body = body.replace("*Response Area:", " *Response Area:");
    times = "";
    if (!parseFields(body.split("\\*"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME"))  return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }


  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:Call \\w+ Performed by):.*|\\w+:");
  private static final Pattern INFO_TIME_PTN = Pattern.compile("(?:Time First Unit Assigned|Time First Unit Arrived|(Time First Call Cleared)):.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (INFO_JUNK_PTN.matcher(field).matches()) return;

      Matcher match = INFO_TIME_PTN.matcher(field);
      if (match.matches()) {
        times = append(times, "\n", field);
        if (match.group(1) != null && !field.endsWith(":")) data.msgType = MsgType.RUN_REPORT;
        return;
      }
      super.parse(field, data);
    }
  }
}
