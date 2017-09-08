package net.anei.cadpage.parsers.FL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Sarasota County, FL
 */
public class FLSarasotaCountyParser extends FieldProgramParser {
  
  public FLSarasotaCountyParser() {
    super(CITY_CODES, "SARASOTA COUNTY", "FL",
        "EVENT_TYPE:CALL! Location:ADDR/S AREA:MAP? MUN:CITY? TIME:TIME! LAT:GPS1? LONG:GPS2? Disp:UNIT");
  }

  @Override
  public String getFilter() {
    return "777,888,93001";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("SARASOTA COUNTY ")) body = body.substring(16).trim();
    body = body.replace("LONG:", " LONG:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern MAP_PTN = Pattern.compile(" (G\\d{3})$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      
      Matcher match = MAP_PTN.matcher(field);
      if (match.find()) {
        data.strMap = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
      
      if (data.strMap.length() == 0) {
        match = MAP_PTN.matcher(data.strAddress);
        if (match.find()) {
          data.strMap = match.group(1);
          data.strAddress = data.strAddress.substring(0,match.start()).trim();
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " MAP PLACE";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHAR", "CHARLOTTE COUNTY",
      "NCTY", "",
      "NPRT", "NORTH PORT",
      "SCTY", "",
      
      "SARA", "SARASOTA"
  });
}
