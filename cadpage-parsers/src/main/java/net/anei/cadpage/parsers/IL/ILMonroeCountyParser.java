package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILMonroeCountyParser extends FieldProgramParser {

  public ILMonroeCountyParser() {
    super(CITY_LIST, "MONROE COUNTY", "IL", 
          "( WARNING EMPTY+? | ) ID ADDR/S6! CALL INFO/N+");
  } 

  @Override
  public String getFilter() {
    return "MONROECO@OMNIGO.COM";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strCall.length() == 0) data.strCall = subject;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("WARNING")) return new SkipField("WARNING:.*", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{5}", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "COLUMBIA",
      "WATERLOO",

      // Villages
      "FULTS",
      "HECKER",
      "MAEYSTOWN",
      "VALMEYER",

      // Unincorporated communities
      "AMES",
      "BURKSVILLE",
      "BURKSVILLE STATION",
      "CHAFLIN BRIDGE",
      "FOSTER POND",
      "FOUNTAIN",
      "HARRISONVILLE",
      "MADONNAVILLE",
      "MERRIMAC",
      "MONROE CITY",
      "NEW HANOVER",
      "RENAULT",
      "ST JOE",
      "TIPTON",
      "WARTBURG"
  };
}
