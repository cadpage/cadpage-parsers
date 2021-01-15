package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NJSalemCountyCParser extends MsgParser {

  public NJSalemCountyCParser() {
    this("SALEM COUNTY", "NJ");
  }

  public NJSalemCountyCParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("ID CALL PLACE ADDR APT CITY ST UNIT INFO");
  }

  @Override
  public String getAliasCode() {
    return "NJSalemCountyC";
  }

  @Override
  public String getFilter() {
    return "E_Messaging@salemcountynj.gov";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z}]{2,3}-\\d{4}-\\d{5,6}");
  private static final Pattern MASTER = Pattern.compile("([-;/ .()A-Z0-9]+) @ +(.*)", Pattern.DOTALL);
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([, A-Z]+) (NJ|DE)\\b(?: *\\d{0,5})?(?: - (.*?))?[-. ]*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;

    body = stripFieldEnd(body, "...");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    body = match.group(2);

    String addr, left;
    int pt = body.indexOf(',');
    if (pt >= 0) {
      addr = body.substring(0,pt).trim();
      left = body.substring(pt+1).trim();
    } else if (body.startsWith("OUT OF COUNTY - ")) {
      addr = "OUT OF COUNTY";
      left = body.substring(16).trim();
    } else return false;

    if (!addr.startsWith("OUT OF COUNTY")) {
      pt = addr.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = addr.substring(0, pt).trim();
        addr = addr.substring(pt+3).trim();
        if (addr.startsWith("OUT OF COUNTY")) {
          String tmp = data.strPlace;
          data.strPlace = addr;
          addr = tmp;
        }
      }
    }
    parseAddress(addr, data);

    pt = left.indexOf("\nActive Units:");
    pt = left.indexOf(" - ", pt+1);
    if (pt >= 0) {
      data.strSupp = left.substring(pt+3).trim();
      left = left.substring(0,pt).trim();
    }

    pt = left.indexOf('\n');
    if (pt >= 0) {
      String unit = left.substring(pt+1).trim();
      left = left.substring(0,pt).trim();
      if (unit.startsWith("Active Units:")) {
        data.strUnit = unit.substring(13).trim();
      } else if (!"Active Units:".startsWith(unit)) return false;
    }

    match = CITY_ST_ZIP_PTN.matcher(left);
    if (match.matches()) {
      left = match.group(1).trim();
      data.strState = match.group(2);
      String extra = match.group(3);
      if (extra != null) {
        extra = extra.trim();
        if (data.strAddress.startsWith("OUT OF COUNTY") || data.strPlace.startsWith("OUT OF COUNTY")) {
          data.strPlace = append(data.strPlace, " - ", data.strAddress);
          data.strAddress = "";
          parseAddress(extra, data);
        } else {
          data.strSupp = append(extra, "\n", data.strSupp);
        }
      }
    }
    data.strCity = stripFieldEnd(left, " BORO");

    return true;
  }
}
