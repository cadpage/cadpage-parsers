package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class TNOliverSpringsParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" +- +");
  
  public TNOliverSpringsParser() {
    super("OLIVER SPRINGS", "TN",
           "ADDR! CALL:CALL! REC:SKIP! DISP:TIME! NOTE:INFO! TRK#:ID");
  }
  
  @Override
  public String getFilter() {
    return "page@ospd.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
     body = body.replace('\n', ' ');
     return parseFields(DELIM.split(body), 5, data);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get(','), data);
      data.strCity = p.get(',');
      data.strState = p.get(',');
      if (data.strState.equals(data.defState)) data.strState = ""; 
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
}
