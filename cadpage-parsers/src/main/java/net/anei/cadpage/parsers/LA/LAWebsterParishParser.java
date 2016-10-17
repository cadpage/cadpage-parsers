package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAWebsterParishParser extends net.anei.cadpage.parsers.FieldProgramParser {
		
  public LAWebsterParishParser() {
    super("WEBSTER PARISH", "LA", 
          "CALL CALL2/L+? ADDR! INFO/L+? X-St:X END");
  }
  
  @Override
  public String getFilter() {
    return "text@webstersheriff.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("*")) return false;
    int pt = body.indexOf('*', 1);
    if (pt < 0) return false;
    String source = body.substring(1,pt).trim();
    body = body.substring(pt+1).trim();
    pt = source.indexOf('-');
    if (pt >= 0) source = source.substring(0,pt).trim();
    data.strSource = source;
    
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("BRUSH FIRE");
    return super.getField(name);
  }
}
