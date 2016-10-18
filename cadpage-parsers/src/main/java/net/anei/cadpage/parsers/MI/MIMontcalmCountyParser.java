package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIMontcalmCountyParser extends DispatchOSSIParser {
  
  public MIMontcalmCountyParser() {
    super(CITY_CODES, "MONTCALM COUNTY", "MI",
          "( CANCEL ADDR CITY/Y! " + 
          "| UNIT ID? CALL ( CITY X+? ADDR! | ADDR! ( CITY/Y! | APT CITY/Y! | ) X+? ) " + 
          "| FYI? DATETIME? ADDR ( ID | CALL! X+? ) ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cad@mydomain.com";
  }
  
  private String saveAddress;

  @Override
  protected boolean parseMsg(String body, Data data) {
    saveAddress = null;
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "EMS ALERT";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:\\b[A-Z]+\\d+|BELDM|GRATM)\\b,?)+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("\\d{7}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      saveAddress = field;
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (checkAddress(field) != STATUS_STREET_NAME) return false;
      super.parse(field, data);
      return true;
    }
    
  }
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Event spawned from "); 
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-Comments:");
      if (field.equals(saveAddress)) return;
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BUSH", "BUSHNELL TWP",
      "CARS", "CARSON CITY",
      "CATO", "CATO TWP",
      "DOUG", "DOUGLASS TWP",
      "EDMO", "EDMORE",
      "EURE", "EUREKA TWP",
      "EVER", "EVERGREEN TWP",
      "FAIR", "FAIRPLAIN TWP",
      "GREE", "GREENVILLE",
      "HOME", "HOME TWP",
      "HOWA", "HOWARD CITY",
      "LAKE", "LAKEVIEW",
      "MAPL", "MAPLE VALLEY TWP",
      "MONT", "MONTCALM TWP",
      "PIER", "PIERSON",
      "REYN", "REYNOLDS TWP",
      "SHER", "SHERIDAN",
      "STAN", "STANTON",
      "SIDN", "SIDNEY TWP"
  });
}
