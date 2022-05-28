package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABuckinghamCountyBParser extends DispatchA71Parser {

  public VABuckinghamCountyBParser() {
    super("BUCKINGHAM COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern ADDR_PTN = Pattern.compile("([^,]*), *([^,]*), Virginia, (\\d{5})");
  private static final Pattern COMMA_PTN = Pattern.compile(" *, *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;

    // Sometimes to format the address in the place field.  And sometimes this is the only address
    // we have to work with :(
    // And sometimes the address is present, but incomplete :( :(
    if (ADDR_PTN.matcher(data.strPlace).matches()) data.strAddress = "";
    if (data.strAddress.isEmpty()) {
      for (String part : COMMA_PTN.split(data.strPlace)) {
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else if (part.equals("Virginia")) {
        } else if (data.strCity.isEmpty()) {
          data.strCity = part;
        }
      }
      data.strPlace = "";
    }
    else if (data.strPlace.startsWith(data.strAddress)) {
      data.strPlace = "";
    }
    return true;
  }


}
