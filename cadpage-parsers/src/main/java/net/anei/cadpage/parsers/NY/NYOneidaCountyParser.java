package net.anei.cadpage.parsers.NY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;



public class NYOneidaCountyParser extends DispatchA13Parser {

  public NYOneidaCountyParser() {
    super(CITY_LIST, "ONEIDA COUNTY", "NY", A13_FLG_TRAIL_PLACE);
    addRoadSuffixTerms("KNLS");
  }

  @Override
  public String getFilter() {
    return "dispatch@ocgov.net,@oc911.org,messaging@iamresponding.com,777,888";
  }

  private static final Pattern NON_ASCII_PTN = Pattern.compile("[^\\p{ASCII}]");
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("^[io?¿][^A-Z]*|^(?:&#\\d+;)+");
  private static final Pattern EXTRA_NL_PTN = Pattern.compile(", *\n");
  private static final Pattern T_CITY_PTN = Pattern.compile("\\b(?:T/|T/O +|/T +|TOWN OF ) *(CONSTANTIA|NORWAY|OHIO|RUSSIA)\\b");
  private static final Pattern EFF_BREATHING_PTN = Pattern.compile("(EFFECTIVE BREATHING NOT VERIFIED)[, ]+(35)");
  private static final Pattern MARKER = Pattern.compile("(?:(.*?)([^A-Z0-9]{1,4}))?\\b(Dispatched|Acknowledge|Enroute|En Route Hosp|On +Scene)([^A-Z0-9]{1,4})(?=[A-Z0-9])");
  private static final Pattern CODE_PTN = Pattern.compile("^([A-Za-z_ ]+ - )?(\\d\\d[A-Z]\\d\\d) ?- *");
  private static final Pattern KNLS = Pattern.compile("\\bKNLS\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern NEAR_PTN = Pattern.compile("[/;]? *(Near:.*)");
  private static final Pattern REAL_APT_PTN = Pattern.compile("(?:FLR|BLDG|LOT|SIDE).*");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(?:(\\d+[A-Z]?|\\d+[A-Z]* FLR)[_ ]+)?(.*?)(?:-(\\d+[A-Z]?|\\d+[A-Z]* FLR))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Eliminate NYMadisonCountyB alerts
    if (subject.equals("SEVAC")) return false;
    if (subject.equals("LINCOLN VOLUNTEER FIRE DEPT")) return false;

    // Eliminate NYMadisonCountyGLAS alerts
    if (subject.equals("Greater Lenox")) return false;
    if (subject.equals("LINCOLN VOLUNTEER FIRE DEPT")) return false;

    data.strSource = subject;

    body = LEAD_JUNK_PTN.matcher(body).replaceFirst("");
    body = NON_ASCII_PTN.matcher(body).replaceAll("");

    body = EXTRA_NL_PTN.matcher(body).replaceAll(", ");
    body = stripFieldEnd(body, "#[0-0]");
    body = stripFieldStart(body, ",");

    // Check for truncated city at end of line
    int pt = body.lastIndexOf(',');
    String newCity = body.substring(pt+1).trim();
    newCity = expandCity(newCity);
    if (newCity != null) body = body.substring(0,pt+1) + ' ' + newCity;

    // As if things weren't bad enough, we also have to deal with IAR alterations
    // Fortunately this all seems to be limited to one agency for now
    if (body.startsWith(">") || body.startsWith(", ")) body = "Dispatched " + body;

    // Sigh, the contagion seems to be spreading ...
    else if (subject.equals("Oneida Castle Fire")) {
      if (body.startsWith("patched")) body = "Dis" + body;
    }

    else if (subject.equals("Clinton Fire") || subject.equals("Durhamville Fire") ||
             subject.equals("Remsen Fire") || subject.equals("Deerfield Fire")) {
      if (!body.contains("Dispatched") && !body.contains("Acknowledge")) body = "Dispatched , " + body;
    }

    // Occasional use of T/ or T/O for Town of messes everything up
    body = T_CITY_PTN.matcher(body).replaceAll(" $1");

    // Clean out extraneous comma
    body = EFF_BREATHING_PTN.matcher(body).replaceAll("$1 $2");

    // Format always has some field delimiters, but they
    // seem to change with the phase of the moon. There is always a "Dispatched"
    // field followed by a delimiter, followed (hopefully) by a alphanumeric character.
    // so we use this pattern to find and identify the delimiter
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;

    // The delimiter preceding and following the Dispatched term should be the same
    // If they are not, see if we can reduce them to a common delimiter by removing
    // whitespace.  If we cannot do that, return failure
    String delimA = match.group(2);
    String status = match.group(3);
    String delim = match.group(4);
    if (delimA != null && !delimA.equals(delim)) {
      delimA = delimA.trim();
      if (delimA.length() == 0) {
        body = body.substring(match.start(3));
      } else {
        delim = delim.trim();
        if (delim.length() == 0) return false;
        else if (!delim.equals(delimA)) return false;
      }
    }

    // If we identified a delimiter that is something other than a single
    // blank, use it to break the rest of the page into a call and an address
    // portion
    String[] flds;
    if (!delim.equals(" ")) {
      int maxFlds = body.startsWith(status) ? 3 : 4;
      flds = body.split(delim, maxFlds);
    }

    // If the identified delimiter was a single blank, we will use the smart
    // address parser to find the break between the call and the first address
    else {

      List<String> fldList = new ArrayList<String>();
      String prefix = match.group(1);
      if (prefix != null) fldList.add(prefix.trim());
      fldList.add(status);

      String sCall, sAddr;
      body = body.substring(match.end()).trim();
      pt = body.indexOf('@');
      if (pt >= 0) {
        sCall = body.substring(0,pt).trim();
        sAddr = body.substring(pt).trim();
      } else {
        pt = body.indexOf(',');
        int pt2 = body.indexOf('(');
        if (pt < 0) pt = body.length();
        if (pt2 < 0) pt2 = body.length();
        pt = Math.min(pt, pt2);
        while (pt > 0 && body.charAt(pt-1) == ' ') pt--;
        sAddr = body.substring(0,pt);
        String sExtra = body.substring(pt);
        Result res = parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, sAddr);
        if (!res.isValid()) return false;
        sCall = res.getAddressPrefix();
        sAddr = res.getFullAddress();
        if (sCall == null || sAddr == null) return false;
        sAddr = sAddr + sExtra;
      }

      fldList.add(sCall);
      fldList.add(sAddr);
      flds = fldList.toArray(new String[fldList.size()]);
    }

    if (!parseFields(flds, data)) return false;

    // Separate call code from call field
    match = CODE_PTN.matcher(data.strCall);
    if (match.find()) {
      data.strCode = match.group(2);
      String prefix = match.group(1);
      String call = data.strCall.substring(match.end());
      if (prefix != null) call = prefix + call;
      data.strCall = call;
    }

    // Clean up address
    data.strAddress = KNLS.matcher(data.strAddress).replaceAll("KNOLLE");

    // They sometimes put near: info in cross street field
    match = NEAR_PTN.matcher(data.strCross);
    if (match.find()) {
      data.strCross = data.strCross.substring(0,match.start()).trim();
      data.strSupp = match.group(1);
    }

    // See if the apt field looks more like a place name
    data.strApt = stripFieldEnd(data.strApt, "_");
    if (data.strApt.length() >= 8 &&
        !REAL_APT_PTN.matcher(data.strApt).matches()) {
      String place = data.strApt;
      match = APT_PLACE_PTN.matcher(place);
      if (match.matches()) {
        data.strApt = append(getOptGroup(match.group(1)), "-", getOptGroup(match.group(3)));
        place = match.group(2).trim();
      } else {
        data.strApt = "";
      }
      place = stripFieldEnd(place," NY");
      if (data.strCity.length() > 0) {
        place = stripFieldEnd(place, data.strCity);
      }
      if (isCity(place)) {
        if (data.strCity.length() == 0) {
          data.strCity = place;
        }
      }
      else if (!place.equals(data.strPlace)) {
        data.strPlace = append(place, " - ", data.strPlace);
      }
    }


    // Check for and remove OUTSIDE from city
    data.strCity = stripFieldEnd(data.strCity, " NY");
    data.strCity = stripFieldEnd(data.strCity, " VILLAGE");
    data.strCity = stripFieldEnd(data.strCity, " VILLA");
    data.strCity = stripFieldEnd(data.strCity, " OUTSIDE");
    data.strCity = stripFieldEnd(data.strCity, " INSIDE");

    if (data.strCity.equalsIgnoreCase("HERLK CO") ||
        data.strCity.equalsIgnoreCase("HERIMER CO")) data.strCity = "HERKIMER CO";
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC CODE " + super.getProgram() + " INFO";
  }

  /**
   * Expand a possibly abbreviated city
   * @param sPart3 possibly abbreviated city
   * @return restored city
   */
  private String expandCity(String city) {
    if (city.length() == 0) return null;
    city = city.toUpperCase();
    SortedSet<String> set = CITY_SET.tailSet(city);
    if (set.isEmpty()) return null;
    String result = set.first();
    if (!result.startsWith(city)) return null;
    if (result.length() == city.length()) return null;
    return result;
  }

  private TreeSet<String> CITY_SET = new TreeSet<String>(Arrays.asList(CITY_LIST));

  private static final String[] CITY_LIST = new String[]{
    "ANNSVILLE",
    "AUGUSTA",
    "AVA",
    "BARNEVELD",
    "BARNEVELD VILLAGE",
    "BOONVILLE",
    "BOONVILLE VILLAGE",
    "BRIDGEWATER",
    "BRIDGEWATER VILLAGE",
    "BROOKFIELD",
    "CAMDEN",
    "CAMDEN VILLAGE",
    "CLARK MILLS",
    "CLAYVILLE",
    "CLAYVILLE VILLAGE",
    "CLINTON",
    "CLINTON VILLAGE",
    "DEERFIELD",
    "FLORENCE",
    "FLOYD",
    "FORESTPORT",
    "HOLLAND PATENT",
    "HOLLAND PATENT VILLA",
    "HOLLAND PATENT VILLAGE",
    "KIRKLAND",
    "LEE",
    "LEE CENTER",
    "MARCY",
    "MARSHALL",
    "MCCONNELLSVILLE",
    "MCCONNELLSVILLE VILLAGE",
    "N BROOKFIELD",
    "NEW HARTFORD",
    "NEW HARTFORD VILLAGE",
    "NEW YORK MILLS",
    "NEW YORK MILLS VILLAGE",
    "ONEIDA CASTLE",
    "ONEIDA CASTLE VILLAGE",
    "ORISKANY",
    "ORISKANY VILLAGE",
    "ORISKANY FALLS",
    "ORISKANY FALLS VILLAGE",
    "PARIS",
    "PROSPECT",
    "PROSPECT VILLAGE",
    "REMSEN",
    "REMSEN VILLAGE",
    "ROME",
    "ROME INSIDE",
    "ROME OUTSIDE",
    "SANGERFIELD",
    "SHERRILL",
    "SHERRILL CITY",
    "STEUBEN",
    "SYLVAN BEACH",
    "SYLVAN BEACH VILLAGE",
    "TRENTON",
    "UTICA",
    "VERNON",
    "VERNON VILLAGE",
    "VERONA",
    "VIENNA",
    "WATERVILLE",
    "WATERVILLE VILLAGE",
    "WESTERN",
    "WESTMORELAND",
    "WHITESBORO",
    "WHITESBORO VILLAGE",
    "WHITESTOWN",
    "YORKVILLE",
    "YORKVILLE VILLAGE",

    "ANNSVILLE NY",
    "AUGUSTA NY",
    "AVA NY",
    "BARNEVELD NY",
    "BARNEVELD VILLAGE NY",
    "BOONVILLE NY",
    "BOONVILLE VILLAGE NY",
    "BRIDGEWATER NY",
    "BRIDGEWATER VILLAGE NY",
    "BROOKFIELD NY",
    "CAMDEN NY",
    "CAMDEN VILLAGE NY",
    "CLARK MILLS NY",
    "CLAYVILLE NY",
    "CLAYVILLE VILLAGE NY",
    "CLINTON NY",
    "CLINTON VILLAGE NY",
    "DEERFIELD NY",
    "FLORENCE NY",
    "FLOYD NY",
    "FORESTPORT NY",
    "HOLLAND PATENT NY",
    "HOLLAND PATENT VILLA NY",
    "HOLLAND PATENT VILLAGE NY",
    "KIRKLAND NY",
    "LEE NY",
    "MARCY NY",
    "MARSHALL NY",
    "MCCONNELLSVILLE NY",
    "MCCONNELLSVILLE VILLAGE NY",
    "N BROOKFIELD NY",
    "NEW HARTFORD NY",
    "NEW HARTFORD VILLAGE NY",
    "NEW YORK MILLS NY",
    "NEW YORK MILLS VILLAGE NY",
    "ONEIDA CASTLE NY",
    "ONEIDA CASTLE VILLAGE NY",
    "ORISKANY NY",
    "ORISKANY VILLAGE NY",
    "ORISKANY FALLS NY",
    "ORISKANY FALLS VILLAGE NY",
    "PARIS NY",
    "PROSPECT NY",
    "PROSPECT VILLAGE NY",
    "REMSEN NY",
    "REMSEN VILLAGE NY",
    "ROME NY",
    "ROME INSIDE NY",
    "ROME OUTSIDE NY",
    "SANGERFIELD NY",
    "SHERRILL NY",
    "SHERRILL CITY NY",
    "STEUBEN NY",
    "SYLVAN BEACH NY",
    "SYLVAN BEACH VILLAGE NY",
    "TRENTON NY",
    "UTICA NY",
    "VERNON NY",
    "VERNON VILLAGE NY",
    "VERONA NY",
    "VIENNA NY",
    "WATERVILLE NY",
    "WATERVILLE VILLAGE NY",
    "WESTERN NY",
    "WESTMORELAND NY",
    "WHITESBORO NY",
    "WHITESBORO VILLAGE NY",
    "WHITESTOWN NY",
    "YORKVILLE NY",
    "YORKVILLE VILLAGE NY",

    // Madison County
    "CANASTOTA",
    "CANASTOTA VILLAGE",

    // Herkimer County
    "HERKIMER COUNTY",
    "HERKIMER CO",
    "HERIMER CO",
    "HERLK CO",  // spelling??
    "NORWAY",
    "OHIO",
    "RUSSIA",

    "POLAND",
    "POLAND VILLAGE",

    // Oswego County
    "CONSTANTIA"

  };
}
