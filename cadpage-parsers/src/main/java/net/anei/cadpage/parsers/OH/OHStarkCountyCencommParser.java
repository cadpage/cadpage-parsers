package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;



public class OHStarkCountyCencommParser extends DispatchA13Parser {

  public OHStarkCountyCencommParser() {
    this("STARK COUNTY", "OH");
  }

  OHStarkCountyCencommParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState);
    setupSaintNames("ABIGAIL", "FRANCIS");
    setupMultiWordStreets("ST ABIGAIL", "ST FRANCIS", "ST FRANCAIS", "CORPORATE WOODS");
  }

  @Override
  public String getFilter() {
    return "@neohio.twcbc.com,@neo.rr.com";
  }

  private static final Pattern NON_ASCII_PTN = Pattern.compile("[^\\p{ASCII}]");
  private static final Pattern SUBJECT_MASTER = Pattern.compile("ALL CALLS|DISPATCHED CALLS|EMS TIMES|FIRES CALLS");
  private static final Pattern FIX_NUMBER_PTN = Pattern.compile("\\b\\d*(?:2 ND|3 RD)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern YOFM_PTN = Pattern.compile("\\b(\\d+) */ *([MF])\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TOWNSHIP_PTN = Pattern.compile("\\bTOWNSHIP\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern FIELD_DELIM_PTN = Pattern.compile("\\s*-\\ss+|\\s+-\\s*|\\s*[:;\n]\\s*");
  private static final Pattern MASTER1_PTN = Pattern.compile("(.*?) +(?:AT|ON) +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern RUN_PTN =  Pattern.compile("RUN[ #]+(\\d+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Clean up an non-ASCII characters
    body = NON_ASCII_PTN.matcher(body).replaceAll("");

    // Check for "normal" alerts
    if (SUBJECT_MASTER.matcher(subject).matches()) {
      return parseMsg(body, data);
    }

    // Otherwise, to sloppy to continue without positive confirmation
    if (!isPositiveId()) return false;
    if (body.contains(" COMMENTS:")) return false;

    setFieldList("CALL ADDR APT CITY PLACE ID INFO");

    Matcher match = FIX_NUMBER_PTN.matcher(body);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        match.appendReplacement(sb, match.group().replace(" ", ""));
      } while (match.find());
      match.appendTail(sb);
      body = sb.toString();
    }

    match = YOFM_PTN.matcher(body);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        match.appendReplacement(sb, match.group(1) + " YO" + match.group(2).toUpperCase());
      } while (match.find());
      match.appendTail(sb);
      body = sb.toString();
    }

    body = body.replace("\n.", "\n");
    body = TOWNSHIP_PTN.matcher(body).replaceAll("TWP");

    // See if this is a delimited field alert
    String[] flds = FIELD_DELIM_PTN.split(body);
    if (flds.length > 1) {

      // Yep, first run throught the parts looking for a city name.
      // Field in front of the city will be the address field
      int addrNdx = -1;
      int infoNdx = -1;
      Result bestResult = null;
      for (int ndx = 1; ndx < flds.length; ndx++) {
        String part = flds[ndx];
        String place = null;
        int pt = part.indexOf('/');
        if (pt >= 0) {
          place = part.substring(pt+1).trim();
          part = part.substring(0,pt).trim();
        }
        if (isCity(part)) {
          addrNdx = ndx-1;
          infoNdx = ndx+1;
          String addr = flds[addrNdx].replace(',', ' ');
          bestResult = parseAddress(StartType.START_ADDR, FLAG_OPT_STREET_SFX | FLAG_NO_CITY, addr);
          data.strCity = part;
          if (place != null) data.strPlace = place;
          break;
        }
      }

      // No luck, run through all of the parts looking for the best address
      if (bestResult == null) {
        for (int ndx = 0; ndx < flds.length; ndx++) {
          String addr = flds[ndx].replace(',', ' ');
          Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_OPT_STREET_SFX, addr);
          if (bestResult == null || res.getStatus() > bestResult.getStatus()) {
            bestResult = res;
            addrNdx = ndx;
            infoNdx = ndx+1;
          }
        }
        if (bestResult.getStatus() <= STATUS_NOTHING) bestResult = null;
      }

      if (bestResult != null) {
        for (int ndx = 0; ndx < addrNdx; ndx++) {
          data.strCall = append(data.strCall, " - ", flds[ndx]);
        }

        bestResult.getData(data);
        String left = bestResult.getLeft();
        if (data.strCross.length() > 0) {
          left = append(data.strCross, " ", left);
          data.strCross = "";
        }
        data.strSupp = left;

        for (int ndx = infoNdx; ndx<flds.length; ndx++) {
          String part = flds[ndx];
          if (data.strCallId.isEmpty()) {
            match = RUN_PTN.matcher(part);
            if (match.matches()) {
              data.strCallId = match.group(1);
              continue;
            }
          }
          if (data.strCall.length() == 0) {
            data.strCall = part;
          } else if (data.strCallId.isEmpty() && data.strCall.length() + part.length() <= 47) {
            data.strCall = append(data.strCall, " - ", part);
          } else {
            data.strSupp = append(data.strSupp, " - ", part);
          }
        }
        fixCity(data);
        return true;
      }
    }

    body = body.replace('\n', ' ');
    match = MASTER1_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = match.group(1);
      parseAddress(StartType.START_ADDR, FLAG_OPT_STREET_SFX, match.group(2), data);
      data.strSupp = getLeft();
      fixCity(data);
      return true;
    }

    parseAddress(StartType.START_CALL, FLAG_OPT_STREET_SFX, body, data);
    if (!isValidAddress()) return false;
    String left = getLeft();
    if (data.strCross.length() > 0) {
      left = append(data.strCross, " ", left);
      data.strCross = "";
    }
    if (data.strCall.length() == 0) {
      data.strCall = left;
    } else {
      data.strSupp = left;
    }
    fixCity(data);
    return true;
  }

  private void fixCity(Data data) {
    data.strCity = MBLANK_PTN.matcher(data.strCity).replaceAll(" ");
    String city = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (city != null) data.strCity = city;
  }


  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) return tmp;
    return city;
  }

  private static final String[] CITY_LIST = new String[]{

    // Stark County

    // Cities
    "ALLIANCE",
    "CANAL FULTON",
    "CANTON",
    "LOUISVILLE",
    "LOUSVILLE",   // Misspelled
    "LOUSIVILLE",  // Misspelled
    "MASSILLON",
    "NORTH CANTON",

    // Villages
    "BEACH CITY",
    "BREWSTER",
    "EAST CANTON",
    "EAST SPARTA",
    "HARTVILLE",
    "HILLS AND DALES",
    "LIMAVILLE",
    "MAGNOLIA",
    "MINERVA",
    "MEYERS LAKE",
    "NAVARRE",
    "WAYNESBURG",
    "WILMOT",

    // Townships
    "BETHLEHEM TWP",
    "BETHLEHEM",
    "CANTON TWP",
    "CANTON",
    "JACKSON TWP",
    "JACKSON",
    "LAKE TWP",
    "LAKE",
    "LAWRENCE TWP",
    "LAWRENCE",
    "LEXINGTON TWP",
    "LEXINGTON",
    "MARLBORO TWP",
    "MARLBORO",
    "MALBORO TWP",  // Mispelled
    "NIMISHILLEN TWP",
    "NIMISHILLEN",
    "OSNABURG TWP",
    "OSNABURG",
    "OSNANBURG TWP",  // Mispelled
    "PARIS TWP",
    "PARIS",
    "PERRY TWP",
    "PERRY",
    "PIKE TWP",
    "PIKE",
    "PLAIN TWP",
    "PLAIN",
    "SANDY TWP",
    "SANDY",
    "SUGAR CREEK TWP",
    "SUGAR CREEK",
    "TUSCARAWAS TWP",
    "TUSCARAWAS",     // Abbreviated
    "TUSC TWP",       // Abbrevidated
    "WASHINGTON TWP",
    "WASHINGTON",
    "WASINGTON TWP",  // Mispelled

    // Census-designated places
    "GREENTOWN",
    "NORTH LAWRENCE",
    "NORTH AWRENCE",   // Mispelled
    "PERRY HEIGHTS",
    "RICHVILLE",
    "ROBERTSVILLE",
    "UNIONTOWN",

    // Unincorporated communities
    "AVONDALE",
    "CAIRO",
    "CRYSTAL SPRINGS",
    "EAST GREENVILLE",
    "FREEBURG",
    "GREENTOWN",
    "JUSTUS",
    "MAPLETON",
    "MARCHAND",
    "MARLBORO",
    "MAXIMO",
    "MCDONALDSVILLE",
    "MIDDLEBRANCH",
    "NEW BALTIMORE",
    "NEW FRANKLIN",
    "NEWMAN",
    "NORTH INDUSTRY",
    "PARIS",
    "PIGEON RUN",
    "SIPPO",
    "WACO",

    // Mahoning County

    // Cities
    "CAMPBELL",
    "CANFIELD",
    "COLUMBIANA",
    "SALEM",
    "STRUTHERS",
    "YOUNGSTOWN",

    //Villages
    "BELOIT",
    "CRAIG BEACH",
    "LOWELLVILLE",
    "NEW MIDDLETOWN",
    "POLAND",
    "SEBRING",
    "WASHINGTONVILLE",

    // Townships
    "AUSTINTOWN TWP",
    "AUSTINTOWN",
    "BEAVER TWP",
    "BEAVER",
    "BERLIN TWP",
    "BERLIN",
    "BOARDMAN TWP",
    "BOARDMAN",
    "CANFIELD TWP",
    "CANFIELD",
    "COITSVILLE TWP",
    "COITSVILLE",
    "ELLSWORTH TWP",
    "ELLSWORTH",
    "GOSHEN TWP",
    "GOSHEN",
    "GREEN TWP",
    "GREEN",
    "JACKSON TWP",
    "JACKSON",
    "MILTON TWP",
    "MILTON",
    "POLAND TWP",
    "POLAND",
    "SMITH TWP",
    "SMITH",
    "SPRINGFIELD TWP",
    "SPRINGFIELD",

    // Census-designated places
    "AUSTINTOWN",
    "BOARDMAN",
    "DAMASCUS",
    "MAPLE RIDGE",
    "MINERAL RIDGE",

    // Unincorporated communities
    "BERLIN CENTER",
    "BLANCO",
    "CALLA",
    "COITSVILLE CENTER",
    "EAST LEWISTOWN",
    "ELLSWORTH",
    "FREDERICKSBURG",
    "GARFIELD",
    "GREENFORD",
    "HICKORY CORNERS",
    "KNAUFVILLE",
    "LAKE MILTON",
    "LOCUST GROVE",
    "NEW ALBANY",
    "NEW BUFFALO",
    "NEW SPRINGFIELD",
    "NORTH BENTON",
    "NORTH JACKSON",
    "NORTH LIMA",
    "OHLTOWN",
    "PATMOS",
    "PETERSBURG",
    "POLAND CENTER",
    "ROSEMONT",
    "SNODES",
    "WEST AUSTINTOWN",
    "WOODWORTH",

    // Portage County

    // Cities
    "AURORA",
    "KENT",
    "RAVENNA",
    "STREETSBORO",
    "TALLMADGE",

    // Villages
    "BRADY LAKE",
    "GARRETTSVILLE",
    "HIRAM",
    "MANTUA",
    "MOGADORE",
    "SUGAR BUSH KNOLLS",
    "WINDHAM",

    // Townships
    "ATWATER TWP",
    "ATWATER",
    "BRIMFIELD TWP",
    "BRIMFIELD",
    "CHARLESTOWN TWP",
    "CHARLESTOWN",
    "DEERFIELD TWP",
    "DEERFIELD",
    "EDINBURG TWP",
    "EDINBURG",
    "FRANKLIN TWP",
    "FRANKLIN",
    "FREEDOM TWP",
    "FREEDOM",
    "HIRAM TWP",
    "HIRAM",
    "MANTUA TWP",
    "MANTUA",
    "NELSON TWP",
    "NELSON",
    "PALMYRA TWP",
    "PALMYRA",
    "PARIS TWP",
    "PARIS",
    "RANDOLPH TWP",
    "RANDOLPH",
    "RAVENNA TWP",
    "RAVENNA",
    "ROOTSTOWN TWP",
    "ROOTSTOWN",
    "SHALERSVILLE TWP",
    "SHALERSVILLE",
    "SUFFIELD TWP",
    "SUFFIELD",
    "WINDHAM TWP",
    "WINDHAM",

    // Census-designated places
    "ATWATER",
    "BRIMFIELD",

    // Unincorporated communities
    "CAMPBELLSPORT",
    "CHARLESTOWN",
    "DIAMOND",
    "EDINBURG",
    "FREEDOM",
    "FREEDOM STATION",
    "HIRAM RAPIDS",
    "LLOYD",
    "MAHONING",
    "MISHLER",
    "NEW MILFORD",
    "PALMYRA",
    "PARIS",
    "SHALERSVILLE",
    "SUFFIELD",
    "WAYLAND",
    "YALE",

    // Carroll County
    "CARROLL CO",
    "ROSE TWP",

    // Columbiana County
    "COLUMBIAN CO",
    "NORTH GEORGETOWN",

    // Summit County
    "SUMMIT CO",
    "SPRINGFIELD TWP",
    "SPRINGFIELD",

    // Tuscarawas County
    "TUSC CO",
    "TUSC COUNTY",
    "SANDY TWP TUSC CO",

    // Wayne County
    "BAUGHMAN TWP",
    "BAUGMAN TWP",     // Misspelled
    "DALTON"

  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "BAUGMAN TWP",        "BAUGHMAN TWP",
      "LOUSIVILLE",         "LOUISVILLE",
      "LOUSVILLE",          "LOUISVILLE",
      "MALBORO TWP",        "MARLBORO TWP",
      "NORTH AWRENCE",      "NORTH LAWRENCE",
      "OSNANBURG TWP",      "OSNABURG TWP",
      "SANDY TWP TUSC CO",  "SANDY TWP",
      "TUSC CO",            "TUSCARAWAS COUNTY",
      "TUSC COUNTY",        "TUSCARAWAS COUNTY",
      "TUSC TWP",           "TUSCARAWAS TWP",
      "WASINGTON TWP",      "WASHINGTON TWP"
  });

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "NORTH GEORGETOWN",   "BELOIT"
  });
}
