package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCPenderCountyBParser extends FieldProgramParser {
  
  public NCPenderCountyBParser() {
    super("PENDER COUNTY", "NC", 
          "ID:ID! Incident_Code:CALL! Address:ADDRCITY! Latitude:GPS1! Longitude:GPS2! Cross_Streets:X! Nearest_Intersection:SKIP! Zone:MAP! Beat:MAP2! Common_Location:PLACE! Contact_Number:PHONE! Units:UNIT! Use_Cation:ALERT! Call_Details:INFO");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@pendersheriff.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("ID:")) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP2")) return new MyMap2Field();
    if (name.equals("ALERT")) return new MyAlertField();
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*), *([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field,  data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private class MyMap2Field extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0 || field.equalsIgnoreCase("NO")) return;
      data.strAlert = getRelativeField(0);
    }
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^| *; *)\\d\\d?/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String part : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
