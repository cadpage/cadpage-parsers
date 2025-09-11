package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class GARabunCountyParser extends FieldProgramParser {
 
  public GARabunCountyParser() {
    super("RABUN COUNTY", "GA", 
          "CFS_Number:ID! Incident_Type:CALL! Location_Details:PLACE! Address:ADDRCITYST! Address_Name:PLACE! Cross_Streets:X! " + 
              "CFS_Latitude:GPS1! CFS_Longitude:GPS2! Please_respond_immediately.%EMPTY! END");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    return parseFields(body.split(" // "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
 }
