package net.anei.cadpage.parsers.VT;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class VTLamoilleCountyParser extends FieldProgramParser {
  private String timeString;
  private boolean unitInfo;
  private Map<String, String> unitName;
  
  public VTLamoilleCountyParser() {
    this("LAMOILLE COUNTY", "VT");
  }
  
  VTLamoilleCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "SKIP SKIP SKIP SKIP SKIP SKIP Address:ADDRCITY Incident_Number:ID! Call_Type:CALL Narratives:INFO+");
  }
  
  @Override
  public String getAliasCode() {
    return "VTGLamoilleCounty";
  }

  @Override
  public String getFilter() {
    return "valcournotification@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    timeString = "";
    unitInfo = false;
    unitName = new HashMap<String, String>();
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(timeString, "\n", data.strSupp);
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("\\d\\d[A-Z]{2,5}\\d{6}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDRESS_CITY_PATTERN
    = Pattern.compile("(.*?)(?:,([^,]*?))?(?:, *([A-Za-z]{2}))?(?:, *(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = ADDRESS_CITY_PATTERN.matcher(field);
      if (!m.matches()) abort();   // Can not happen!!
      String addr = m.group(1);
      addr = addr.replace(',', '&');
      parseAddress(addr, data);
      data.strCity = getOptGroup(m.group(2));
      if (data.strCity.equals("")) data.strCity = getOptGroup(m.group(4));
      data.strState = getOptGroup(m.group(3));
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" ST";
    }
  }
  
  private static final Pattern UNIT_INFO_PATTERN_1
  = Pattern.compile("Unit Id +(\\d):(.*)"),
                              UNIT_INFO_PATTERN_2
  = Pattern.compile("(Dispatched|Enroute|On Scene|Cleared) +(\\d): +(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d)"),
                              UNIT_INFO_PATTERN_3
  = Pattern.compile("(?:Medical|Fire) \\d:.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String uName = "";
      if (field.equals("")) return;
      if (field.startsWith("-----") && field.endsWith("-----")) return;
      if (field.equals("Units")) {
        unitInfo = true;
        return;
      }
      if (unitInfo) {
        Matcher m = UNIT_INFO_PATTERN_3.matcher(field);
        if (m.matches()) return;
        m = UNIT_INFO_PATTERN_1.matcher(field);
        if (m.matches()) {
          uName = m.group(2).trim();
          unitName.put(m.group(1), uName);
          data.strUnit = append(data.strUnit, ",", uName);
          return;
        }
        m = UNIT_INFO_PATTERN_2.matcher(field);
        if (m.matches()) {
          String entry = m.group(1);
          if (data.strTime.length() == 0 && entry.equals("Dispatched")) {
            data.strDate = m.group(3);
            data.strTime = m.group(4);
          }
          if (entry.equals("On Scene")) data.msgType = MsgType.RUN_REPORT;
          uName = getOptGroup(unitName.get(m.group(2)));
          if (uName == null) uName = "Unit "+m.group(2);
          field = uName+" "+entry+" "+m.group(3)+" "+m.group(4);
          timeString = append(timeString, "\n", field);
        }
        return;
      }
      data.strSupp = append(data.strSupp, "\n", field);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" UNIT DATE TIME";
    }
  }
}