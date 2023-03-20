
package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jefferson County, AL
 */
public class ALJeffersonCountyEParser extends DispatchH05Parser {

  public ALJeffersonCountyEParser() {
    super("JEFFERSON COUNTY", "AL",
          "Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! https:SKIP! EMPTY+? Call_Date/Time:DATETIME! Call_Type:CALL_PRI! Fire_Area:FIRE_AREA! INFO/N+ Alerts:ALERTS! INFO/N+ Incident_Numbers:ID! Units:UNIT! Times:EMPTY! TIMES/N+");
  }
    
  @Override
  public String getFilter() {
    return "FireDesk@jeffcoal911.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("CALL_PRI")) return new MyCallPriorityField();
    if (name.equals("FIRE_AREA")) return new MyInfoField("Fire Area:");
    if (name.equals("ALERTS")) return new MyInfoField("Alerts:");
    return super.getField(name);
  }
  
  private static final Pattern CALL_PRI_PTN = Pattern.compile("(.*?), , Call Priority: *(.*?), ,");
  private class MyCallPriorityField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PRI_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strPriority = match.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL PRI";
    }
  }
  
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
}
