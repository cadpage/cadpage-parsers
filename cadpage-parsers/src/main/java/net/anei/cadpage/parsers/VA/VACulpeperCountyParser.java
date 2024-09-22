package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VACulpeperCountyParser extends DispatchOSSIParser {

  public VACulpeperCountyParser() {
    super("CULPEPER COUNTY", "VA",
           "( CANCEL ADDR | FYI? BOX CH? CALL ADDR MAP? ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@culpepercounty.gov";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("BOX"))  return new BoxField("(?:Text Message)?(\\d{4})", true);
    if (name.equals("CH")) return new ChannelField("\\d+[- ]?[A-Z]", true);
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }

  // And we need a special MAP field that will append two map data fields
  private static final Pattern MAP_PTN = Pattern.compile("(?:(.*?)[/ ]+?)?(\\d{1,2}-?[A-Z]\\d)(?: - MAP PAGE)?");
  private static final Pattern SPECIAL_MAP_PTN = Pattern.compile("(?:(.*?)[ /]+)?(?:([A-Z]+) POST OFFICE MAP|MAP PAGE|MP)");
  private class MyMapField extends MapField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.matches()) return false;
      String place = getOptGroup(match.group(1));
      data.strMap = match.group(2);

      match = SPECIAL_MAP_PTN.matcher(place);
      if (match.matches()) {
        data.strPlace = getOptGroup(match.group(1));
        data.strCity = getOptGroup(match.group(2));
      } else {
        data.strPlace = place;
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "PLACE CITY MAP";
    }
  }
}
