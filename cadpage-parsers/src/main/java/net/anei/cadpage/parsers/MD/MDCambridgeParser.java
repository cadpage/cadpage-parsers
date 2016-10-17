package net.anei.cadpage.parsers.MD;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDCambridgeParser extends FieldProgramParser {

  public MDCambridgeParser(){
    super(CITY_CODES, "CAMBRIDGE", "MD",
           "CT:ADDR/S0C! BOX:BOX! DUE:UNIT!");
  }

  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    
    if (!body.startsWith("DOR911:") ) return false;
    body = body.substring(7).trim();
    if (body.endsWith(":DC")) body = body.substring(0,body.length()-3).trim();
    return super.parseMsg(body, data);
  }
    
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Everything changes if this is a Mutual aid call
      int ipt = field.indexOf(" @MA ");
      if (ipt >= 0) {
        data.strCall = "Mutual Aid: " + field.substring(0, ipt).trim();
        int ipt2 = field.indexOf(':', ipt);
        if (ipt2 < 0) ipt2 = field.length();
        data.strCity = field.substring(ipt+5, ipt2).trim();
        field = field.substring(ipt2+1).trim().replaceAll("@", "");
        parseAddress(field, data);
      }
  
      else {
        super.parse(field, data);
      }
    }
  }
    
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = 
    buildCodeTable(new String[]{
        "CAMB","Cambridge"
  });
}