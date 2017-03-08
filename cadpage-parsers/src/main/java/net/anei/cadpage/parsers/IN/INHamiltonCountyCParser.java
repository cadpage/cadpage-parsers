package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INHamiltonCountyCParser extends FieldProgramParser {
  
  public INHamiltonCountyCParser() {
    super("HAMILTON COUNTY", "IN", 
          "Call_Type:CALL! Status:SKIP! Address:ADDRCITY! Common_Name:PLACE! Closest_Intersection:X! Additional_Location_Info:INFO! City:CITY! Nature_of_Call:CALL/SDS! Units:UNIT! Quadrant:MAP! Narrative:SKIP! CC_Text:CALL/SDS Caller_Statement:INFO Coord:GPS! ID! END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@hamiltoncounty.in.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DELIM = Pattern.compile("\n|(?=Common Name:|Additional Location Info:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("HAMILTON COUNTY:\n")) return false;
    body = body.substring(17).trim();
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
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
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Inc#")) abort();
      field = field.substring(4).trim();
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }
}
