package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYSuffolkCountyGParser extends FieldProgramParser {
  
  public NYSuffolkCountyGParser() {
    super("SUFFOLK COUNTY", "NY",
          "CALL! TOA:TIMEDATE! ADDR! PLACE APT CS:X SRC ID INFO+? UNIT UNIT+");
    setupMultiWordStreets("INDIAN HEAD");
  }
  
  @Override
  public String getFilter() {
    return "commackfd@gmail.com,commackfd2@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), 3, data);
  }
  
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('-', '/');
      super.parse(field, data);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("'")) field = field.substring(0,field.length()-1).trim();
      Parser p = new Parser(field);
      String state = p.getLast(',');
      if (!state.equals("NY")) abort();
      data.strCity = p.getLast(',');
      String address = p.get();
      if (address.length() == 0) abort();
      parseAddress(address, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  private class MyUnitField extends InfoField {
    public MyUnitField() {
      super("[A-Z]+-?\\d+", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
}
