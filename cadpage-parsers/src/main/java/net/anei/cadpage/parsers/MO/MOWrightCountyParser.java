package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOWrightCountyParser extends FieldProgramParser {
  
  public MOWrightCountyParser() {
    super("WRIGHT COUNTY", "MO", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY/S6! CITY:CITY? X_STREET:X! LAT/LON:GPS? NOTE:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@wrightcounty911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strApt.equals("AVE")) {
      data.strApt = "";
    } else if (data.strApt.startsWith("BOX")) {
      data.strAddress = append(data.strAddress, " ", data.strApt);
      data.strApt = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
    
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
}
