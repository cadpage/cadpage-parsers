package net.anei.cadpage.parsers.IN;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
 * Hamilton County, IN (variant A)
 */
public class INHamiltonCountyAParser extends FieldProgramParser {
 
  public INHamiltonCountyAParser() {
    super("HAMILTON COUNTY", "IN",
           "Unit:SKIP? Status:STATUS? Location:ADDR/SXa! Quad:MAP! Units:UNIT! Type:CALL NOC:CALL/SDS  Narr:INFO CFS:ID Coord:GPS Venue:CITY Inc_#:ID");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@hamiltoncounty.in.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
 
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = data.strCode;
    if (data.strCall.length() == 0) data.strCall = "ALERT";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("STATUS")) return new SkipField("Dispatched|Arrived", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      field = field.replaceAll("  +", " ");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Nature of Call:");
      super.parse(field,  data);
    }
  }
}
