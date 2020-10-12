package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class KYLoganCountyAParser extends DispatchB2Parser {
  
  public KYLoganCountyAParser() {
    super("911-CENTER:",CITY_LIST, "LOGAN COUNTY", "KY");
    setupMultiWordStreets(
        "JAMES ROSE",
        "KENNY STRATTON"
   );
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@logancounty.ky.gov";
  }

  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace('@', '&').replace(" AT ", " & ");
    return super.parseAddrField(field, data);
  }

  @Override
  protected CodeSet buildCallList() {
    return new CodeSet(
        "UNKNOWN",
        "ALARM",
        "AUTOMOBILE ACCIDENT",
        "AUTOMOBILE ACCIDENT W/INJURIES",
        "FIRE BRUSH",
        "LIFTING ASSISTANCE",
        "CHEST PAIN",
        "DIFF BREATHING, SOA",
        "MEDICAL / ALL OTHER",
        "SMOKE INVESTIGATION"
    );
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ADAIRVILLE",
    "AUBURN",
    "LEWISBURG",
    "OLMSTEAD",
    "RUSSELLVILLE"
  };
}
