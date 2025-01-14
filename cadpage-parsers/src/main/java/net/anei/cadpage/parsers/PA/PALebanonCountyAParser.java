package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lebanon County, PA
 */
public class PALebanonCountyAParser extends MsgParser {

  public PALebanonCountyAParser() {
    super("LEBANON COUNTY", "PA");
    setFieldList("ADDR APT CITY PLACE CALL CH UNIT BOX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY;
  }

  @Override
  public String getFilter() {
    return "cadpage@lcdes.org";
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern MASTER =
      Pattern.compile("([^-,]+)[-,] *(?:([A-Za-z ]+ (?:Township|Twp|Borough))|City of ([A-Za-z]+)) (?:(?:@ *)?(.*?) )??(?:(?:APT|Apt|apt|ROOM|Room|room|LOT|Lot|lot) +(\\S+) )?((?:Med Class\\d[- ]?|[A-Z]{2,3}-|911 |Electrical |MED |MVA |Outside Fire |Traffic Control\\b|<Call Type>).*?) (?:FG (\\d+) )?(\\S+) Fire-Box\\b ?(\\S*) EMS-Box\\b ?(\\S*)");

  @Override
  public boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = MSPACE_PTN.matcher(body).replaceAll(" ");

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    parseAddress(match.group(1).trim().replace('@', '&'), data);
    String city = match.group(2);
    if (city != null ) {
      if (city.endsWith(" Township")) {
        data.strCity = city.substring(0,city.length()-8) + "Twp";
      } else {
        data.strCity = stripFieldEnd(city, " Borough");
      }
    } else {
      data.strCity = match.group(3);
    }
    data.strPlace = getOptGroup(match.group(4));
    data.strApt = append(data.strApt, "-", getOptGroup(match.group(5)));

    data.strCall = match.group(6).trim();
    data.strChannel = getOptGroup(match.group(7));
    data.strUnit = match.group(8);
    data.strBox = append(match.group(9), "/", match.group(10));
    return true;
  }
}
