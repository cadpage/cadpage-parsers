package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchH04Parser extends HtmlProgramParser {
  
  public DispatchH04Parser(String defCity, String defState) {
    super(defCity, defState, 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! Cross_Streets:X? Lat_/_Long:GPS? ID:ID! PRI:PRI? DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@rockwall.com";
  }
  
  private String times;
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    times = null;
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n\n", data.strSupp);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*?,.*), *(.*)");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(",")) return;
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+|\\d\\d?:\\d\\d:\\d\\d");
  private static final Pattern INFO_TIMES_MARK_PTN = Pattern.compile("\\d{5}: .*");
  private class BaseInfoField extends InfoField {
    
    private boolean enabled = false;
    
    @Override
    public void parse(String field, Data data) {
      if (INFO_DATE_TIME_PTN.matcher(field).matches()) {
        enabled = false;
        return;
      }
      if (field.equals("-")) {
        enabled = true;
        return;
      }
      if (!enabled) {
        int pt = field.indexOf(" - ");
        if (pt >= 0) {
          field = field.substring(pt+3).trim();
          enabled = true;
        }
      }
      
      if (times == null && INFO_TIMES_MARK_PTN.matcher(field).matches()) {
        times = field;
        return;
      }
      
      if (times != null) {
        if (field.startsWith("Cleared:")) data.msgType = MsgType.RUN_REPORT;
        times = append(times, "\n", field);
      }
      
      else if (enabled) {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
  }

}
