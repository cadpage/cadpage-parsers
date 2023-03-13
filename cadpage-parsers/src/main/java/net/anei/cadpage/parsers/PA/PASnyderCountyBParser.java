package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PASnyderCountyBParser extends FieldProgramParser {
  
  public PASnyderCountyBParser() {
    this("SNYDER COUNTY");
  }
  
  PASnyderCountyBParser(String defCity) {
    super(defCity, "PA", 
          "CALL PLACE ADDRCITY GPS1 GPS2 APT X! Box_Area:BOX! Notes:INFO! Units_Due:UNIT END");
  }
  
  @Override
  public String getFilter() {
    return "CSR911@csr911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";"), data)) return false;
    data.strCity = stripFieldEnd(data.strCity,  " BORO");
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private static final Pattern COUNTY_PTN = Pattern.compile("[A-Z ]+ CO(?:UNTY)?", Pattern.CASE_INSENSITIVE);
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(",")) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else if (data.strCity.isEmpty()) {
          data.strCity = part;
        } else if (!COUNTY_PTN.matcher(part).matches()) {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }

}
