package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Washington County, OR (Variant B)
 * Also Clackamas County
 */
public class ORWashingtonCountyBParser extends ORWashingtonCountyBaseParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("PW(\\d+)");
  
  public ORWashingtonCountyBParser() {
    this("WASHINGTON COUNTY", "OR");
  }
  
  public ORWashingtonCountyBParser(String defCity, String defState) {
    super(ORWashingtonCountyParser.CITY_LIST, defCity, defState,
          "ADDR/SC! MAP:MAP UNIT:UNIT! INC:ID! DAREA:SKIP! RD:BOX! BEAT/STATION:BOX! LAT:GPS! LON:GPS!");
  }
  
  @Override
  public String getAliasCode() {
    return "ORWashingtonCountyB";
  }
  
  @Override
  public String getFilter() {
    return "TibITSAlertingSystem@wccca.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    if (!super.parseMsg(subject,  body, data)) return false;
    String gpsLoc = data.strGPSLoc;
    data.strGPSLoc = "";
    setGPSLoc(gpsLoc, data);
    return true;
  }

  
  private static final Pattern ADDR_TRAIL_PTN = Pattern.compile(" +\\((.*?)\\)$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_TRAIL_PTN.matcher(field);
      if (match.find()) {
        String cross = match.group(1).trim();
        field = field.substring(0,match.start());
        if (!cross.equals("WCCA 911")) data.strCross = cross;
      }
      if (field.endsWith(", OR")) field = field.substring(0,field.length()-4).trim();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
  
  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      data.strBox = append(field, "-", data.strMap);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      data.strGPSLoc = append(data.strGPSLoc, ",", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
}
