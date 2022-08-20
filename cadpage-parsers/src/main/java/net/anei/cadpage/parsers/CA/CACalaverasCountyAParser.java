package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;


/**
 * Calaveras County, CA
 */
public class CACalaverasCountyAParser extends FieldProgramParser {
  
  
  public CACalaverasCountyAParser() {
    this("CALAVERAS COUNTY");
  }
  
  protected CACalaverasCountyAParser(String defCity) {
    super(defCity, "CA",
          "ID CALL ADDRCITY CALL/SDS GPS! http:SKIP! Resources:UNIT! Command:CH! Tac:CH/L! Air:CH/L! Grd:CH/L! END");
  }
  
  @Override
  public String getFilter() {
    return "tcucad@FIRE.CA.GOV";
  }
  
  @Override
  public String getAliasCode() {
    return "CACalaverasCountyA";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DELIM = Pattern.compile("\n+");
  private static final Pattern MASTER = 
      Pattern.compile("Inc# (\\d+):([^:]+):(?:([^@]+)@)?(.+?) *, *([A-Z_]+) *(?:\\(([^]]*?)\\) *)?:Map +([^:]*):(?: :)? LAT/LONG (X: [-+]?\\d+ \\d+\\.\\d+ +Y: [-+]?\\d+ \\d+\\.\\d+): ([^:]*)(?::([^:]*))?(?::.*)?");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Incident #")) {
      return parseFields(DELIM.split(body), data);
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    setFieldList("ID CALL PLACE ADDR APT CITY MAP GPS INFO UNIT");
    data.strCallId = match.group(1);
    data.strCall = match.group(2);
    data.strPlace = getOptGroup(match.group(3));
    parseAddress(match.group(4).trim(), data);
    data.strCity = match.group(5).replace('_', ' ').trim();
    data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(6)));
    data.strMap = match.group(7).trim();
    setGPSLoc(match.group(8), data);
    data.strSupp = match.group(9).trim();
    data.strUnit = getOptGroup(match.group(10));
    
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Incident #(\\d+)", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
      data.strCity = data.strCity.replace('_', ' ');
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("Lat: *(.*?); Long: *(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1)+','+match.group(2), data);
    }
  }
}
