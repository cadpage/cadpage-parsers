
package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Baldwin County, AL
 */
public class ALGenevaCountyAParser extends DispatchOSSIParser {

  public ALGenevaCountyAParser() {
    super("GENEVA COUNTY", "AL",
          "CALL CALL2? ADDR! X+? INFO+");
  }
    
  @Override
  public String getFilter() {
    return "CAD@smtp.gmail.com,34Central@smtp.gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" CT ST ", " COURT ST ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    return super.getField(name);
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strCall.equals("EMS") && !data.strCall.equals("FIRE")) return false;
      data.strCall = append(data.strCall, " - ", field);
      return true;
    }
  }
}
