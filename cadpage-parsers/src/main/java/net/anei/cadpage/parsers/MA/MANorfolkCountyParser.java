package net.anei.cadpage.parsers.MA;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;

/**
 * Norfolk County, MA
 */
public class MANorfolkCountyParser extends DispatchRedAlertParser {
  
  public MANorfolkCountyParser() {
    super("NORFOLK COUNTY","MA");
    setupMultiWordStreets(
        "CENTRAL PARK",
        "EAGLE'S NEST",
        "EAST CENTRAL",
        "FRANKLIN VILLAGE",
        "GLEN MEADOW",
        "MOUNTAIN ASH",
        "NORTH MAIN",
        "WEST CENTRAL",
        "WHITE DOVE"
   );
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body,  data)) return false;
    if (data.strPlace.contains(",") || data.strPlace.contains("&")) {
      data.strName = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "NAME PLACE");
  }
}
