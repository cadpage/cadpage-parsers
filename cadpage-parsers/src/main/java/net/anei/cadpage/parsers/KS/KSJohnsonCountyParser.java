package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * Johnson County, KS
 */
public class KSJohnsonCountyParser extends FieldProgramParser {

  public KSJohnsonCountyParser() {
    super("JOHNSON COUNTY", "KS",
           "SRC Add:ADDR! Apt:APT Loc:PLACE Nature:CALL! Grid:MAP! Incident:ID Cross:X");
  }
  
  @Override
  public String getFilter() {
    return "93001,ecc1@jocogov.org,ecc2@jocogov.org,ecc3@jocogov.org,ecc4@jocogov.org,@jocofd1.org,@jocoems.org,2183500185";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replaceAll("Incident#", "Incident:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ":");
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      super.parse(field, data);
    }
  }
}
