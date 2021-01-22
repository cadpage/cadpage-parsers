package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Lebanon County, PA
 */
public class PALebanonCountyAParser extends SmartAddressParser {

  public PALebanonCountyAParser() {
    super("LEBANON COUNTY", "PA");
    setFieldList("SRC TIME DATE ADDR APT CITY X PLACE PRI CALL CH UNIT BOX INFO");
    removeWords("ALY", "PLAZA", "TERRACE");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY;
  }

  @Override
  public String getFilter() {
    return "km911alert@gmail.com,km911@fastmail.fm,7176798487";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("([ A-Za-z0-9]+)@(\\d\\d:\\d\\d)");
  private static final Pattern CALL_PREFIX_PTN = Pattern.compile(" (?:Med Class ?(\\d+) |((?:<Call Type>|\\(Call Type\\)|Aircraft|BOX IN SERVICE|CARDIAC|CHEST PAIN|DIFFICULTY|FIRE|\\b(?:[A-Z][a-z]+ )?Fire Company Transfer|GENERAL|MED|MVA|Non-Emergency|PC U|[Ss]ick [Pp]erson|Stand-by|TEST CALL|TRAF|Traffic|TRANSFER|Transfer|Land Search&Rescue|Unresponsive)[- ])|(?<=[ a-z]|^|PLAZA|AUTOMOTIVE|SCHOOL)((?!APT|ST)[A-Z]{2,6} ?-(?!Box) ?))");
  private static final Pattern CALL_PREFIX_PTN2 = Pattern.compile(" *(?:FIRE )?(?:AFA|AIRCFT|BC|MV|SC) ?- *");
  private static final Pattern BOX_PTN =  Pattern.compile(" Fire-Box (?:([-0-9]+) )?EMS-Box(?: ([-0-9]+))?");
  private static final Pattern CLASS_PTN = Pattern.compile("(?:[* ]+|^)(?:EMS|Med) Class ?(\\d)[* ]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELIMS = Pattern.compile("[, ]+");
  private static final Pattern CH_PTN = Pattern.compile("[/ ]*(?:\\b|/)(FG)[- 0]*(\\d+|HAZMAT)\\b *", Pattern.CASE_INSENSITIVE);
  private static final Pattern UNIT_PTN = Pattern.compile("(?:[,/ ]+|^)([A-Z]+[0-9]+(?:-\\d+|ST\\d){0,2}|[0-9]+[A-Z]+|\\d+-\\d*[A-Z]?|ARC|CH?|DUTY?|EMSCO180PALMCITY|FASP[A-Z]*|Firs\\?a7|L|LLEMSOnoSta|LLEMSPalmyraSta|NOPD|PSP-N|REQ|SQ?|TRAFFIC?|FG[ -]?\\d+)$", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1).toUpperCase().replace(" ", "");
      data.strTime = match.group(2);
    }

    // Look for a priority/call prefix pattern that marks the
    // end of the address
    String sCallPfx;
    if ((match = CALL_PREFIX_PTN2.matcher(body)).find()) {
      sCallPfx = match.group();
    }
    else if ((match = CALL_PREFIX_PTN.matcher(body)).find()) {
      data.strPriority = getOptGroup(match.group(1));
      sCallPfx = match.group(2);
      if (sCallPfx == null) sCallPfx = match.group(3);
    }
    else {
      if (data.strSource.length() == 0) return false;
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }

    String sAddress = stripFieldEnd(body.substring(0,match.start()).trim(), "-");
    String sTail = body.substring(match.end()).trim();

    // Parse leading city
    String left = "";
    sAddress = parseCity(true, sAddress, data);
    if (data.strCity.isEmpty()) {
      sAddress = parseCity(false, sAddress, data);
      if (!data.strCity.isEmpty()) {
        left = sAddress;
        sAddress = data.strAddress;
        data.strAddress = "";
      }
    }

    sAddress = sAddress.replace('@', '&');
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT | FLAG_ANCHOR_END, sAddress, data);

    while (left.length() > 0) {
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, left);
      if (!res.isValid()) break;
      String save = data.strCross;
      res.getData(data);
      data.strCross = append(save, ", ", data.strCross);
      left = res.getLeft();
    }
    data.strPlace = left;

    // See if the apt had picked up a wayward city
    fixApt(data);

    // That is it for the address section, now work on the tail including the call description
    // Start by stripping off the combined box number

    match = BOX_PTN.matcher(sTail);
    if (! match.find()) {
      pt = sTail.indexOf(" Fire-");
      if (pt >= 0) sTail = sTail.substring(0,pt).trim();
    } else {
      String fireBox = match.group(1);
      String emsBox =  match.group(2);
      fireBox = (fireBox != null ? "Fire:"+fireBox : "");
      emsBox = (emsBox != null ? "EMS:" + emsBox : "");
      data.strBox = append(fireBox, " ", emsBox);
      sTail = sTail.substring(0,match.start()).trim();
    }

    // If there is a class priority it separates the call description from the units
    String unit = "";
    sTail = stripFieldEnd(sTail, ",");
    if (sTail.length() > 0) {
      match = CLASS_PTN.matcher(sTail);
      if (match.find()) {
        data.strPriority = match.group(1);
        unit = sTail.substring(match.end()).trim().toUpperCase();
        sTail = sTail.substring(0, match.start()).trim();
        unit = DELIMS.matcher(unit).replaceAll(" ").trim();
      }

      // If there is not class priority, we need to strip off individual units

      else {
        while (true) {
          match = UNIT_PTN.matcher(sTail);
          if (!match.find()) break;
          unit = append(match.group(1).toUpperCase().replace(' ', '-'), " ", unit);
          sTail = sTail.substring(0,match.start()).trim();
        }
      }

      // Strip leading channel from unit info
      while ((match = CH_PTN.matcher(unit)).lookingAt()) {
        data.strChannel = match.group(1) + '-' + match.group(2);
        unit = unit.substring(match.end());
      }
      data.strUnit = unit;
    }

    data.strCall = ((sCallPfx == null ? "" : sCallPfx) + sTail).trim();
    if (data.strCall.length() == 0) data.strCall = "Med";

    // Sometimes channel is embedded in the call description
    if (data.strChannel.isEmpty()) {
      match = CH_PTN.matcher(data.strCall);
      if (match.find()) {
        data.strChannel = (match.group(1) + '-' + match.group(2)).toUpperCase();
        data.strCall = append(data.strCall.substring(0, match.start()), " - ", data.strCall.substring(match.end()));
      }
    }

    // Make some validity checks to require **SOMETHING** beyond a simple call prefix match
    return data.strCity.length() > 0 || data.strBox.length() > 0 || data.strUnit.length() > 0 || data.strCall.startsWith("BOX IN SERVICE");
  }

  private static final String COUNTY_PTN_S = "([BDLS]C|[A-Z]+ CO(?:UNTY)?)\\.?";
  private static final String CITY_PTN_S = "(?:City of ([A-Z]+)|((?:(?:NORTH|SOUTH|EAST|WEST|UPPER|LOWER|[NESW]) )?(?:(?:LITTLE|MOUNT|MT|NEW|PORT|ST) )?(?:[A-Z]+|COLD SPRING|DEER LAKE|PALO ALTO|SCHUYLKILL HAVEN|PINE GROVE|SINKING SPRING|TERRE HILL) (?:BORO(?:UGH)?|TWP|TOWNSHIP|CITY)))";
  private static final Pattern COUNTY_CITY_PTN = Pattern.compile("[, ]*\\b(?:"+COUNTY_PTN_S+"[-/,= ]+" + CITY_PTN_S + "|" + CITY_PTN_S + "(?:[-/,= ]+" + COUNTY_PTN_S + ")?)\\b[, ]*", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_BORO_PTN = Pattern.compile("(.*?) +BORO(?:UGH)?", Pattern.CASE_INSENSITIVE);

  private String parseCity(boolean lead, String sAddress, Data data) {

    // We usually have pretty good luck using a pattern to identify the country and city
    Matcher match = COUNTY_CITY_PTN.matcher(sAddress);
    if (lead) {
      if (!match.lookingAt()) return sAddress;
    } else {
      boolean found;
      do {
        found = match.find();
      } while (found && (sAddress.charAt(match.start()) != ',' && match.end() != sAddress.length()));

      if (!found) return sAddress;
      data.strAddress = sAddress.substring(0, match.start());
    }

    String city, county;
    county = match.group(1);
    if (county !=  null) {
      city = match.group(2);
      if (city == null) city = match.group(3);
    } else {
      city = match.group(4);
      if (city == null) city = match.group(5);
      county = match.group(6);
    }

    Matcher match2 = CITY_BORO_PTN.matcher(city);
    if (match2.matches()) city = match2.group(1);

    if (county != null) {
      if (county.length() == 2) county = COUNTY_CODES.getProperty(county);
      city = city + ", " + county;
    }
    data.strCity = city;

    sAddress = sAddress.substring(match.end());
    return sAddress;
  }

  private static final Pattern APT_CITY_PTN = Pattern.compile("([BDLS]C) +(.*)");
  private static final Pattern APT_CITY_PTN2 = Pattern.compile("([A-Z ]+ CO(?:UNTY)?)[-, ]+(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern GOOD_APT_PTN = Pattern.compile("[A-Z0-9]{1,5}|.* FL(?:OOR)?", Pattern.CASE_INSENSITIVE);

  /**
   * Fix apartment information
   * @param data
   */
  private void fixApt(Data data) {
    if (data.strApt.isEmpty()) return;
    if (data.strCity.isEmpty()) {
      Matcher match = APT_CITY_PTN.matcher(data.strApt);
      if (match.matches()) {
        data.strCity = match.group(2) + ", " + COUNTY_CODES.getProperty(match.group(1));
        data.strApt = "";
      } else if ((match = APT_CITY_PTN2.matcher(data.strApt)).matches()) {
        data.strCity = match.group(2) + ", " + match.group(1);
        data.strApt = "";
      }
    }
    if (!GOOD_APT_PTN.matcher(data.strApt).matches()) {
      data.strPlace = append(stripFieldStart(data.strApt, "-"), " - ", data.strPlace);
      data.strApt = "";
    }
  }

  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "BC", "BERKS COUNTY",
      "DC", "DAUPHIN COUNTY",
      "LC", "LANCASTER COUNTY",
      "SC", "SCHUYLKILL COUNTY"
  });
}
