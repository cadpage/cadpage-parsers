package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCRutherfordCountyParser extends FieldProgramParser {
  
  public NCRutherfordCountyParser() {
    super("RUTHERFORD COUNTY", "NC",
           "Location:ADDR! APT/ROME:APT? City:CITY! Call_Type:CALL! Line11:INFO? Units:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "paging@rutherfordcountync.gov";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    do {
      if (body.startsWith("Paging:")) {
        body = body.substring(7).trim();
        break;
      }
      if (subject.endsWith("PageGate")) break;
      return false;
    } while (false);
    body = body.replace("\n", "").replace('=', ':');
    return super.parseFields(body.split("\\*"), data);
  }
}
