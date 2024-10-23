package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MNNoblesCountyParser extends SmartAddressParser {

  public MNNoblesCountyParser() {
    super(CITY_LIST, "NOBLES COUNTY", "MN");
    setFieldList("CALL ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "noreply@co.nobles.mn.us";
  }

  private static final Pattern MASTER = Pattern.compile("([^,]+), *([A-Za-z ]*), *([A-Z]{2})(?: +(\\d{5}))?\\b *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strCall = subject;

    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      parseAddress(match.group(1).trim(), data);
      String city = match.group(2);
      data.strState = match.group(3);
      String zip = match.group(4);
      data.strSupp = match.group(5);
      if (city.isEmpty() && zip != null) city = zip;
      data.strCity = city;
      return true;
    }

    else {
      int pt = body.indexOf(',');
      if (pt < 0) return false;
      parseAddress(body.substring(0,pt).trim(), data);
      body = body.substring(pt+1).trim();
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
      data.strSupp = getLeft();
      return !data.strCity.isEmpty();
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ADRIAN",
      "BIGELOW",
      "BREWSTER",
      "DUNDEE",
      "ELLSWORTH",
      "KINBRAE",
      "LISMORE",
      "ROUND LAKE",
      "RUSHMORE",
      "WILMONT",
      "WORTHINGTON",

      // Census-designated place
      "LEOTA",

      // Unincorporated communities
      "ORG",
      "PFINGSTEN",
      "RANSOM",
      "READING",
      "ST KILIAN",

      // Townships
      "BIGELOW TWP",
      "BLOOM TWP",
      "DEWALD TWP",
      "ELK TWP",
      "GRAHAM LAKES TWP",
      "GRAND PRAIRIE TWP",
      "HERSEY TWP",
      "INDIAN LAKE TWP",
      "LARKIN TWP",
      "LEOTA TWP",
      "LISMORE TWP",
      "LITTLE ROCK TWP",
      "LORAIN TWP",
      "OLNEY TWP",
      "RANSOM TWP",
      "SEWARD TWP",
      "SUMMIT LAKE TWP",
      "WESTSIDE TWP",
      "WILMONT TWP",
      "WORTHINGTON TWP"
  };

}
