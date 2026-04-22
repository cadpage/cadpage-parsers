package net.anei.cadpage.parsers.WV;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Berkeley County, WV
 */
public class WVBerkeleyCountyParser extends MsgParser {

  public WVBerkeleyCountyParser() {
    super("BERKELEY COUNTY", "WV");
    setFieldList("ADDR CITY ST APT CODE CALL");
  }

  @Override
  public String getFilter() {
    return "alerts@berkeleywv.org";
  }

  private static final Pattern MASTER1 = Pattern.compile("Address(.*?) Title(?:(\\d\\d) +)?(.*)");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Probably transient new format
    Matcher match = MASTER1.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1).trim();
    data.strCode = getOptGroup(match.group(2));
    data.strCall = match.group(3);

    int pt = addr.indexOf('(');
    if (pt >= 0 ) {
      String city = stripFieldEnd(addr.substring(pt+1).trim(), ")");
      addr = addr.substring(0,pt).trim();

      pt = city.indexOf(',');
      if (pt >= 0) {
        String state = city.substring(pt+1).toUpperCase();
        city = city.substring(0,pt).trim();
        if (STATE_PTN.matcher(state).matches()) data.strState = state;
      }
      String tmp = CITY_CODES.getProperty(city.toUpperCase());
      if (tmp != null) {
        city = tmp;
        pt = city.indexOf('/');
        if (pt >= 0) {
          String state = city.substring(pt+1);
          city = city.substring(0,pt);
          if (data.strState.isEmpty()) data.strState = state;
        }
      }
      data.strCity = city;
    }

    parseAddress(addr, data);
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BERK CO", "Berkley Co",
      "FRED CO", "Frederick Co/VA",
      "JEFF CO", "Jeffesion Co",
      "MORG CO", "Morgan Co",
      "WASH CO", "Washington Co/MD"
  });
}
