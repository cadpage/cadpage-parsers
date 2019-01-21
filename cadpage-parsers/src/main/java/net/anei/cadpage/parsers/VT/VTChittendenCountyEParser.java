package net.anei.cadpage.parsers.VT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VTChittendenCountyEParser extends DispatchH05Parser {
  
  public VTChittendenCountyEParser() {
    super("CHITTENDEN COUNTY", "VT", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! CROSS_STREETS:X! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY INFO_BLK+? INCIDENT:ID! APT:INFOX! LAT:GPS1! LON:GPS2! END");
  }
  
  @Override
  public String getFilter() {
    return "Fire@burlingtonvt.gov,chayes@bpdvt.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFOX")) return new MyInfoXField();
    return super.getField(name);
  }
  
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}|");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (STATE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyInfoXField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }
}
