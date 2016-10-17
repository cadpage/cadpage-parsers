
package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Jefferson County, AL (F)
 */
public class ALJeffersonCountyFParser extends FieldProgramParser {

  public ALJeffersonCountyFParser() {
    super("JEFFERSON COUNTY", "AL",
          "Incident_Type:CALL! Incident_Location:ADDRCITY! Location_Notes:INFO/N+ Incident_Number:ID! Date:DATE! Time:TIME! Priority:PRI! Cross_Streets:X Remarks:INFO/N+");
  }
    
  @Override
  public String getFilter() {
    return "automailer@velocityps.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = body.replace(" Time:", "\nTime:");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("[") && field.endsWith("]")) return;
      if (field.startsWith("Dispatched:")) data.msgType = MsgType.RUN_REPORT;
      if (field.startsWith("CROSS STREET OF ")) {
        data.strCross = field.substring(15).trim();
        return;
      }
      if (field.startsWith("CROSS STREETS ARE ")) {
        field = field.substring(18).trim().replaceAll("  +", " ");
        if (field.equals("AND")) return;
        field = stripFieldEnd(field, " AND").replace(" AND ", " / ");
        data.strCross = field;
        return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "X " + super.getFieldNames();
    }
  }
}
