package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARPulaskiCountyCParser extends SmartAddressParser {

  public ARPulaskiCountyCParser() {
    super(CITY_LIST, "PULASKI COUNTY", "AR");
    setFieldList("ID CALL ADDR APT CITY ST PLACE ALERT CH INFO");
  }

  @Override
  public String getFilter() {
    return "smtp@pcso.org";
  }

  private static final Pattern ID_PTN = Pattern.compile("CFS\\d{4}-\\d{5}");
  private static final Pattern CH_PTN = Pattern.compile("(\\d+|[A-Z]{2,4} (?:FIRE )?\\d+|ONE)\\b *(.*)");
  private static final Pattern CANCEL_PTN = Pattern.compile("(.*) (CANCEL(?: RESPONSE)?)");
  private static final Pattern DIR_PTN = Pattern.compile("\\b([NSEW])/B\\b");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("(AR)(?: +\\d{5})?\\b *");
  private static final Pattern ALERT_PTN = Pattern.compile("(.*?) *(?:Alert:|Officer Safety:|Premise Information:) *(.*)");
  private static final Pattern ADDR_EXT_PTN = Pattern.compile("MM.*|EXIT.*|[NSEW]B");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) +(\\S*)|\\d{1,4}[A-Z]?|[A-Z]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!ID_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;

    String cancel = "";
    int pt = body.indexOf(" Fire Channel");
    if (pt >= 0) {
      String channel = body.substring(pt+13).trim();
      body = body.substring(0,pt).trim();
      Matcher match = CH_PTN.matcher(channel);
      if (match.matches()) {
        data.strChannel = match.group(1);
        channel = match.group(2);
      }
      data.strSupp = stripFieldStart(channel, "/");
    }

    else {
      Matcher match = CANCEL_PTN.matcher(body);
      if (!match.matches()) return false;
      body = match.group(1).trim();
      cancel = match.group(2);
    }

    Parser p = new Parser(body);
    data.strCall = append(cancel, " - ", p.get(','));
    String addr = p.get(',');
    if (addr.length() == 0) return false;
    addr = addr.replace('@', '&');
    addr = DIR_PTN.matcher(addr).replaceAll("$1B");
    addr = stripFieldEnd(addr, " None");


    String city = p.get(',');
    String extra = p.get();

    if (city.isEmpty() && extra.isEmpty() &&
        !addr.contains("/") && !addr.contains("&")) {
      parseAddress(StartType.START_ADDR, FLAG_NO_CITY, addr, data);
      String place = getLeft();
      if (place.startsWith("MM") || place.contains("EXIT")) {
        data.strAddress = append(data.strAddress, " ", place);
      } else if (place.equals("WHITE OAK CROSSING")) {
        data.strAddress = append(data.strAddress, " & ", place);
      } else if (data.strAddress.endsWith(" ARCH ST") && place.startsWith("PIKE")) {
        data.strAddress = append(data.strAddress, " ", "PIKE");
        data.strPlace = place.substring(4).trim();
      } else {
        data.strPlace = place;
      }
    } else {
      parseAddress(addr, data);
    }

    if (extra.isEmpty()) {
      extra = city;
    } else {
      Matcher match = ST_ZIP_PTN.matcher(extra);
      if (match.lookingAt()) {
        data.strCity = city;
        data.strState = match.group(1);
        extra = extra.substring(match.end());
      } else {
        extra = append(city, ", ", extra);
      }
    }

    if (data.strCity.isEmpty()) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, extra, data);
      extra = getLeft();
    }

    Matcher match = ALERT_PTN.matcher(extra);
    if (match.matches()) {
      extra =  match.group(1);
      data.strAlert = match.group(2);
    }
    if (!extra.equals("None")) {
      extra = stripFieldEnd(extra, " None");
      if (ADDR_EXT_PTN.matcher(extra).matches()) {
        data.strAddress = append(data.strAddress, " ", extra);
      } else if ((match = APT_PTN.matcher(extra)).matches()) {
        String apt = match.group(1);
        if (apt == null) apt = extra;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, " - ", extra);
      }
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CAMMACK VILLAGE",
      "JACKSONVILLE",
      "LITTLE ROCK",
      "MAUMELLE",
      "NORTH LITTLE ROCK",
      "SHERWOOD",
      "WRIGHTSVILLE",

      // Town
      "ALEXANDER",

      // Census-designated places
      "COLLEGE STATION",
      "GIBSON",
      "HENSLEY",
      "LANDMARK",
      "MCALMONT",
      "NATURAL STEPS",
      "ROLAND",
      "SCOTT",
      "SWEET HOME",
      "WOODSON",

      // Other communities
      "CRYSTAL HILL",
      "GRAVEL RIDGE",
      "IRONTON",
      "LITTLE ITALY",
      "MABELVALE",
      "MARCHE",
      "PANKEY",
      "WOODYARDVILLE",

      // Lonoke County
      "CABOT",
      "ENGLAND",

      // Saline County
      "PARON"
  };
}
