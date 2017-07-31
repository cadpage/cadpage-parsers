package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAMontereyCountyBParser extends FieldProgramParser {
  
  public CAMontereyCountyBParser() {
    super("MONTEREY COUNTY", "CA", 
          "( RES:UNIT! CLOSE:ID! CALL! ADDRCITY! DSP:TIME! AIQ:SKIP! END " +
          "| CALL ADDRCITY UNIT PLACE X MAP GPS! ID! Tac:CH! AIR/N GRD/N UNIT! INFO/N )");
  }
  
  @Override
  public String getFilter() {
    return "8319983288,beuecc@fire.ca.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(" <a ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("; "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Inc# +(\\d{6})", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new GPSField("X:.* Y:.*", true);
    if (name.equals("AIR")) return new InfoField("Air:.*", true);
    if (name.equals("GRD")) return new InfoField("Grd:.*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = data.strCity.replace('_', ' ');
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NO TEXT")) return;
      super.parse(field, data);
    }
  }
}
