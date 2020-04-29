
package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXRuskCountyAParser extends DispatchSouthernParser {

  public TXRuskCountyAParser() {
    super(CITY_LIST, "RUSK COUNTY", "TX", DSFLAG_OPT_DISPATCH_ID | DSFLAG_NO_PLACE | DSFLAG_CROSS_NAME_PHONE);
    addExtendedDirections();
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "CFS:");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "PLACE X CITY");
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_IMPLIED_INTERSECT;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("\\d+[A-Z]?|[A-Z]");
  private static final Pattern ADDR_X_PTN = Pattern.compile("(.*?) +(?:BETWEEN|CLOSE) +(.*)");
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      int ndx = field.indexOf('@');
      if (ndx >= 0) {
        data.strPlace = field.substring(ndx+1).trim();
        field = field.substring(0,ndx).trim();
      }
      Matcher match = ADDR_X_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCross = match.group(2);
      }
      super.parse(field, data);
      if (data.strPlace.length() == 0) {
        if (!ADDR_APT_PTN.matcher(data.strApt).matches()) {
          data.strPlace = data.strApt;
          data.strApt = "";
        }
      }
    }
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "EASTON",
    "HENDERSON",
    "KILGORE",
    "MOUNT ENTERPRISE",
    "NEW LONDON",
    "OVERTON",
    "REKLAW",
    "TATUM",

    // Unincorporated communities
    "CONCORD",
    "ELDERVILLE",
    "JOINERVILLE",
    "LAIRD HILL",
    "LANEVILLE",
    "LEVERETT'S CHAPEL",
    "MINDEN",
    "PRICE",
    "SELMAN CITY",
    "TURNERTOWN",
    
    // Nacogdoches County
    "CUSHING",
    "GARRISON"
  };
}
