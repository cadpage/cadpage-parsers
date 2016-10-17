package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Placer County, CA
 */
public class CAPlacerCountyAParser extends FieldProgramParser {
  
  public CAPlacerCountyAParser() {
    this("PLACER COUNTY", "CA");
  }
  
  protected CAPlacerCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "ID MAP CALL ADDR PLACE GPS! Resources:UNIT! Remarks:INFO/N+");
  }
  
  @Override
  public String getAliasCode() {
    return "CAPlacerCounty";
  }
  
  @Override
  public String getFilter() {
    return "NEUCAD@FIRE.CA.GOV";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern DELIM = Pattern.compile("\n\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Page")) return false;
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Incident #(\\d{6})", true);
    if (name.equals("MAP")) return new MapField("RespArea *(\\S+)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      if (field.endsWith(")")) {
        pt = field.indexOf('(');
        if (pt >= 0) {
          String place = field.substring(pt+1, field.length()-1).trim();
          data.strPlace = append(data.strPlace, " - ", place);
          field = field.substring(0,pt).trim();
        }
      }
      pt = field.lastIndexOf(',');
      if (pt < 0)  abort();
      data.strSource = field.substring(pt+1).trim();
      field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT SRC PLACE";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("BTWN " )) {
        data.strCross = field.substring(5).trim();
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
  
  private static final Pattern INFO_CODE_PTN = Pattern.compile("Dispatch +([A-Z0-9]+)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher  match = INFO_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
      } else {
        data.strSupp = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("http://maps.google.com/\\?q=([-+]?\\d+\\.\\d{4,},[-+]?\\d+\\.\\d{4,})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1), data);
    }
  }
}
