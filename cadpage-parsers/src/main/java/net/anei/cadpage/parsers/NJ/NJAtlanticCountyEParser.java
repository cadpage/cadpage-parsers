package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NJAtlanticCountyEParser extends MsgParser {

  public NJAtlanticCountyEParser() {
    super("ATLANTIC COUNTY", "NJ");
    setFieldList("ID CALL PLACE ADDR APT CITY ST UNIT INFO");
  }

  @Override
  public String getFilter() {
    return "AtlanticSheriffPaging@Enfwebmail.onmicrosoft.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("I-\\d{4}-\\d{6}");
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(.*?) +([A-Z]{2})(?: +\\d{5})?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;

    if (body.endsWith(" -")) body += ' ';
    int pt = body.indexOf("\nActive Units:");
    if (pt >= 0) {
      String units = body.substring(pt+14).trim();
      body = body.substring(0,pt).trim();

      pt = units.indexOf(" - ");
      if (pt >= 0) {
        data.strSupp = units.substring(pt+3).trim();
        units = units.substring(0,pt).trim();
      }
      data.strUnit = units;
    }
    else if ((pt = body.indexOf("  - ")) >= 0){
      data.strSupp = body.substring(pt+4).trim();
      body = body.substring(0,pt).trim();
    }

    else {

    }

    pt = body.indexOf(" @ ");
    if (pt < 0) return false;
    data.strCall = body.substring(0,pt).trim();
    body = body.substring(pt+3).trim();

    String city;
    pt = body.indexOf(" - ");
    int pt2 = body.indexOf(',');
    if (pt2 >= 0) {
      if (pt >= 0 && pt < pt2) {
        data.strPlace = body.substring(0,pt).trim();
        pt += 3;
      } else {
        pt = 0;
      }
      city = body.substring(pt2+1).trim();
      body = body.substring(pt, pt2).trim();
    } else {
      city = null;
    }

    parseAddress(body, data);

    if (city != null) {
      pt = city.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", city.substring(pt+3).trim());
        city = city.substring(0,pt).trim();
      }
      Matcher match = CITY_ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        city =  match.group(1);
        data.strState = match.group(2);
      } else if (city.startsWith("AC ")) {
        data.strPlace = append(data.strPlace, " - ", city.substring(3).trim());
        city = "ATLANTIC CITY";
      }
      data.strCity = city;
    }

    return true;
  }
}
