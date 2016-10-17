package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYNassauCountyCParser extends FieldProgramParser {

  public NYNassauCountyCParser() {
    super("NASSAU COUNTY", "NY",
           "ADDR/SP CS:X TOA:SKIP");
  }
  
  @Override
  public String getFilter() {
    return "backup@westburyfd.xohost.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (! body.startsWith("* ")) return false;
    int pt = body.indexOf(" * ", 2);
    if (pt < 0) return false;
    data.strCall =  body.substring(2,pt).trim();
    body = body.substring(pt+3).trim();
    
    return super.parseMsg(body, data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt1 = field.indexOf("  ");
      if (pt1 >= 0) {
        int pt2 = field.lastIndexOf("  ");
        if (pt1 == pt2) {
          if (field.startsWith("CODE ")) {
            pt2 = field.length();
          } else {
            pt1 = -2;
          }
        }
        if (pt1 >= 0) data.strCall = append(data.strCall, " ", field.substring(0,pt1).trim());
        if (pt2 < field.length()) data.strPlace = field.substring(pt2+2).trim();
        field = field.substring(pt1+2,pt2).trim();
      }
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
}


