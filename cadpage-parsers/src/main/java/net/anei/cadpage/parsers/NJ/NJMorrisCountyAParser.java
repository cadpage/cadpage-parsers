package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class NJMorrisCountyAParser extends SmartAddressParser {

  private static final Pattern MASTER_PTN =
    Pattern.compile("(.*?)[ \n]\\[+([-A-Za-z& ]+)(?:\\.*\\d+)?\\]+ \\(([-A-Z0-9\\\\/\\. ]+)\\) - (.*)", Pattern.DOTALL);

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*), *\\d{5}");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE)(?![A-Z'])[- ]*((?:ABOVE +)?[^ ]+(?: [A-Z]\\b)?) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern PLACE_CODE_PTN = Pattern.compile("\\(\\d+\\)");
  private static final Pattern BLD_APT_PTN = Pattern.compile("\\b(?:BLDG?|BUILD|UNIT) [^ ]+(?: (?:FLR?|FLOOR) [^ ]+)?(?: (?:APT|RM|ROOM|SUITE) [^ ]+)?\\b");
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*?)-?\\b(?:APT|LOT|RM|ROOM|SUITE)\\b(?!'S|$) *(.+)");
  private static final Pattern ID_TIME_PTN = Pattern.compile("\\b(?:([A-Z]\\d{6,}) +)?(\\d\\d:\\d\\d)$");
  private static final Pattern ID_PTN = Pattern.compile("[A-Z]\\d{6,}");

  public NJMorrisCountyAParser() {
    super(OOC_CITY_LIST, "MORRIS COUNTY", "NJ");
    setFieldList("PLACE APT ADDR CITY CALL UNIT INFO CODE ID TIME");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("NEW", "BLOCK");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.morris.nj.us,mcdispatch@optimum.net,iamresponding.com,@c-msg.net,Dispatch,MadisonDispatch@rosenet.org,cadrelay@denvillepolice.org,morrisdispatch@co.morris.nj.us,712,777";
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    Matcher match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;

    String sAddress = match.group(1).trim();
    String city = match.group(2).trim();
    data.strCall = match.group(3).trim();
    String sExtra = match.group(4);

    int pt = sAddress.indexOf('\n');
    if (pt >= 0) {
      data.strCall = append(data.strCall, " - ", sAddress.substring(0,pt).trim());
      sAddress = sAddress.substring(pt+1).trim();
    }

    sAddress = stripFieldStart(sAddress, "***");

    // Strip off trailing zip code
    match = ADDR_ZIP_PTN.matcher(sAddress);
    if (match.matches()) sAddress = match.group(1).trim();

    Parser p = new Parser(sAddress);
    String place = p.getOptional(',');
    String apt1 = "";
    String apt2 = p.getOptional(',');
    sAddress = p.get();

    // Check for leading APT in the place field
    if ((match = APT_PLACE_PTN.matcher(place)).matches()) {
      data.strApt = match.group(1);
      place = match.group(2);
    }

    // If we have a place field, see what we can do with it
    boolean ooc = false;
    if (place.length() > 0) {

      // Special handling required for OOC mutual aid calls
      String upshift = sAddress.toUpperCase();
      if (upshift.endsWith(" COUNTY") || upshift.endsWith(" CO")) {
        ooc = true;

        // Address is really the city
        // Place is really the address
        city = sAddress;
        sAddress = place;
        place = "";
      }

      // Normal in-county call
      else {

        // Try to split apt out of place
        if ((match = PLACE_CODE_PTN.matcher(place)).find()) {
          apt1 = place.substring(match.end()).trim();
          place =  place.substring(0,match.start()).trim();
        }

        if ((match = BLD_APT_PTN.matcher(place)).find()) {
          apt1 = append(match.group(), " - ", apt1);
          place = append(place.substring(0,match.start()).trim(), " - ", place.substring(match.end()).trim());
        }
        else if ((match = PLACE_APT_PTN.matcher(place)).find()) {
          place = match.group(1).trim();
          apt1 = append(match.group(2), " - ", apt1);
        }

        // A numeric place name is probably a street number that got separated from
        // it's street name
        if (NUMERIC.matcher(place).matches()) {
          if (sAddress.length() == 0 || !Character.isDigit(sAddress.charAt(0))) {
            sAddress = append(place, " ", sAddress);
            place = "";
          }
        }
      }

      data.strPlace = place;
    }

    // See what we can do with the address
    // First see if the address parser can identify a city at the end of the address
    // If it can not, parser everything and treat whatever is left as a city name
    sAddress = stripFieldEnd(sAddress, " NJ");
    StartType st = data.strPlace.length() == 0 ? StartType.START_PLACE : StartType.START_ADDR;
    Result res = parseAddress(st, FLAG_ANCHOR_END, sAddress);
    if (res.getCity().length() > 0) {
      res.getData(data);
    } else {
      parseAddress(st, sAddress, data);
      String left = getLeft();
      if (left.startsWith("/") || left.startsWith("&")) {
        data.strAddress = data.strAddress + " & " + left.substring(1).trim();
      } else if (ooc) {
        data.strCity = left;
      } else {
        data.strApt = append(data.strApt, " ", left);
      }
      if (data.strCity.length() == 0) data.strCity = city;
    }
    if (st == StartType.START_PLACE && data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }

    // Combine all of the apt values we have located
    data.strApt = append(apt1, " - ", data.strApt);
    data.strApt = append(data.strApt, " - ", apt2);

    // And clean up the city name
    if (data.strCity.endsWith(" Co")) data.strCity += "unty";
    else if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    else {
      data.strCity = stripFieldEnd(data.strCity, " Boro");
      data.strCity = stripFieldEnd(data.strCity, " Bor");
      data.strCity = convertCodes(data.strCity,  CITY_CODES);
    }

    // Strip ID and time from end extra data
    match = ID_TIME_PTN.matcher(sExtra);
    if (match.find()) {
      sExtra = sExtra.substring(0,match.start()).trim();
      data.strCallId = getOptGroup(match.group(1));
      data.strTime = match.group(2);
    }

    String[] flds = sExtra.split("\n");
    if (flds.length > 1) {
      data.strUnit = flds[0].trim();
      for (int ndx = 1; ndx < flds.length; ndx++) {
        String fld = flds[ndx].trim();
        if (data.strCallId.isEmpty() && ID_PTN.matcher(fld).matches()) {
          data.strCallId = fld;
        } else if (data.strCode.isEmpty() && fld.startsWith("Response Code:")) {
          data.strCode = fld.substring(15).trim();
        } else {
          data.strSupp = append(data.strSupp, "\n", fld);
        }
      }
    }

    else {
      p = new Parser(sExtra);
      data.strUnit = p.getLastOptional(" - ");
      data.strSupp = p.get();
    }
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "VAN HOUTON"
  };

  private static final String[] OOC_CITY_LIST = new String[]{

    // Essex County
    "LIVINGSTON",

    // Sussex County
    "ANDOVER TWP",
    "FREEDON",    // dispatch typos
    "FREDON",
    "HOPATCONG",
    "LAFAYETTE TWP",
    "SANDSTON",   // dispatch typo
    "SANDYSTON",
    "SPARTA",
    "WANTAGE",

    // Mercer County
    "PRINCETON",

    // Union County
    "BERKLEY HEIGHTS",
    "BERKLEY HIEGHTS", // Misspelled
    "NEW PROVIDENCE",

    // Warren County
    "FRANKLIN TWP",
    "FRELINGHUYSEN",
    "INDEPENDENCE TWP",
    "PHILLIPSBURG"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BERKELEY HIEGHTS", "BERKELEY HEIGHTS",
      "Morris Plns",    "Morris Plains",
      "MtArlington",    "Mt Arlington",
      "SANDSTON",       "SANDYSTON",
      "FREEDON",        "FREDON"
  });
}
