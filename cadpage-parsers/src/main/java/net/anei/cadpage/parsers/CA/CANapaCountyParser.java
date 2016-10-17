package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Napa County, CA
 */
public class CANapaCountyParser extends FieldProgramParser {
  
  public CANapaCountyParser() {
    super("NAPA COUNTY", "CA",
          "CALL ADDR ID INFO! RA:UNIT! GPS UNIT Cmd:SRC Tac:CH");
  }
  
  @Override
  public String getFilter() {
    return "Lnucad@fire.ca.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Page")) return false;
    
    // Don't know what this is, but it gets in the way
    int pt = body.lastIndexOf(" CB#:");
    if (pt < 0) return false;
    body = body.substring(0,pt).trim();
    return parseFields(body.split(";"), data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getOptional('@');
      String city = p.getLastOptional(',');
      if (city.endsWith("_CITY")) city = city.substring(0,city.length()-5);
      data.strCity = city.replace('_', ' ').trim();
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " CITY";
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Inc# ")) abort();
      field = field.substring(5).trim();
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
}
