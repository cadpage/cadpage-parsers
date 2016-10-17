package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class INJeffersonCountyParser extends DispatchEmergitechParser {
  
  public INJeffersonCountyParser() {
    super(true, CITY_LIST, "JEFFERSON COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "@jeffersoncounty.in.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    body = subject + ": " + body;
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities and towns
    "BROOKSBURG",
    "DUPONT",
    "HANOVER",
    "MADISON",

    // Unincorporated towns
    "CANAAN",
    "KENT",
    "DEPUTY",

    // Townships
    "GRAHAM TWP",
    "HANOVER TWP",
    "LANCASTER TWP",
    "MADISON TWP",
    "MILTON TWP",
    "MONROE TWP",
    "REPUBLICAN TWP",
    "SALUDA TWP",
    "SHELBY TWP",
    "SMYRNA TWP"
  };
}
