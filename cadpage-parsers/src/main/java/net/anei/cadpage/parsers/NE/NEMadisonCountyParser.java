package net.anei.cadpage.parsers.NE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class NEMadisonCountyParser extends SmartAddressParser {

  public NEMadisonCountyParser() {
    super(CITY_LIST, "MADISON COUNTY", "NE");
    setFieldList("SRC CALL ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("(.*?), NE(?:BRASKA)? \\d{5}\\b *(.*)");
  private static final Pattern CITY_PTN = Pattern.compile("[ A-Z]+");
  private static final Pattern PREFIX = Pattern.compile("((?:.*? )?(?:FIRE AND RESCUE|FIRE|RESCUE)) (?:IS )?(?:NEEDED|REQUESTED)(?: (?:TO|AT))? +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith(" Dispatch")) {
      data.strSource = subject.substring(0,subject.length()-9).trim();
    } else if (subject.isEmpty() && body.startsWith("NE911 ")) {
      body = body.substring(6).trim();
    } else {
      return false;
    }

    int flags = 0;
    Matcher match = ST_ZIP_PTN.matcher(body);
    if (match.matches()) {
      String city = match.group(1).trim();
      data.strState = "NE";
      body = match.group(2);
      if (CITY_PTN.matcher(city).matches()) {
        data.strCity = city;
        flags |= FLAG_NO_CITY;
      } else {
        data.strSupp = body;
        body = city;
        flags |= FLAG_ANCHOR_END;
      }
    }
    match = PREFIX.matcher(body);
    if (match.lookingAt()) {
      data.strCall = match.group(1);
      body = body.substring(match.end());
      if (body.startsWith("FOR ")) {
        body = body.substring(4).trim();
        String call = data.strCall;
        data.strCall = "";
        parseAddress(StartType.START_CALL, flags, body, data);
        data.strCall = append(call, " - ", data.strCall);
      } else {
        parseAddress(StartType.START_ADDR, flags, body, data);
      }
    }
    else {
      parseAddress(StartType.START_CALL, flags, body, data);
    }
    data.strSupp = append(getLeft(), "\n", data.strSupp);
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BATTLE CREEK",
      "MADISON",
      "NEWMAN GROVE",
      "NORFOLK",
      "TILDEN",

      // Village
      "MEADOW GROVE",

      // Unincorporated communities
      "EMERICK",
      "ENOLA",
      "KALAMAZOO",
      "WARNERVILLE"
  };
}
