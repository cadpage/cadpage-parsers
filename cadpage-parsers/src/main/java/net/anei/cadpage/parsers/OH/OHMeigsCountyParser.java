package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class OHMeigsCountyParser extends SmartAddressParser {

  public OHMeigsCountyParser() {
    super(CITY_LIST, "MEIGS COUNTY", "OH");
    setFieldList("CALL ADDR APT CITY ST UNIT INFO ID PLACE");
  }

  @Override
  public String getFilter() {
    return "meigs911@meigsems.com";
  }

  private static final Pattern TRAIL_ID_PLACE_PTN = Pattern.compile("(.*) (CFS\\d{3,7})\\b *(.*)");
  private static final Pattern TRAIL_TIMES_PTN = Pattern.compile("(.*?) ((?:\\d{1,2}-[-A-Z0-9]+|PD-\\d{1,2}) - Assign \\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern SPECIAL_PTN = Pattern.compile("(.*) (09 Olive Township VFD)");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("[; ]+(\\d{1,2}-[-A-Z0-9]+|PD-\\d{1,2})$");
  private static final Pattern ADDR_CITY_ST_ZIP_PTN = Pattern.compile("([^,]+), *([ A-Z]+), *([A-Z]{2})(?: +\\d{5})\\b *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    // So much of this is optional, there there almost always is an ID number
    // followed by an optional place
    Matcher match = TRAIL_ID_PLACE_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strCallId = match.group(2);
      data.strPlace = match.group(3);
    }

    // Check for trailing time info
    String times = "";
    match = TRAIL_TIMES_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      times = match.group(2).replace("; ", "\n");
    }

    // There may or may not be an information detail block.  If there is not
    // it will be replaced by a "None" placeholder

    if (body.endsWith(" None")) {
      body = body.substring(0, body.length()-5).trim();
    } else {
      String[] parts = INFO_BRK_PTN.split(body);
      if (parts.length > 1) {
        body = parts[0];
        for (int j = 1; j < parts.length; j++) {
          data.strSupp = append(data.strSupp, "\n", parts[j]);
        }
      }
      else {
        match = SPECIAL_PTN.matcher(body);
        if (!match.matches()) return false;
        body = match.group(1).trim();
        data.strPlace = match.group(2);
      }
    }
    data.strSupp = append(data.strSupp, "\n", times);

    // There may or may not be some trailing units
    while (true) {
      match = TRAIL_UNIT_PTN.matcher(body);
      if (!match.find()) break;
      data.strUnit = append(match.group(1), ",", data.strUnit);
      body = body.substring(0, match.start());
    }

    // We should be down to address/city/state zip followed by an option message
    // With luck, everything will fit the standard pattern
    body = stripFieldEnd(body, ",");
    match = ADDR_CITY_ST_ZIP_PTN.matcher(body);
    if (match.matches()) {
      parseAddress(match.group(1).trim(), data);
      data.strCity =  match.group(2).trim();
      data.strState = match.group(3);
      body = match.group(4);
    }

    // No such luck.  See if we have a comma terminating the address
    else {
      int pt = body.indexOf(',');
      if (pt >= 0) {
        parseAddress(body.substring(0,pt).trim(), data);
        body = body.substring(pt+1).trim();
        if (body.endsWith(" None")) {
          data.strCity = body.substring(0, body.length()-5).trim();
          body = "None";
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
          body = getLeft();
        }
      }

      // Hope this never happens
      else {
        return false;
      }
      if (!body.equals("None")) {
        data.strSupp = append(body, "\n", data.strSupp);
      }
    }
    return true;
  }




  private static final String[] CITY_LIST = new String[]{

      //Villages

      "MIDDLEPORT",
      "POMEROY",
      "RACINE",
      "RUTLAND",
      "SYRACUSE",

      //Townships

      "BEDFORD",
      "CHESTER",
      "COLUMBIA",
      "LEBANON",
      "LETART",
      "OLIVE",
      "ORANGE",
      "RUTLAND",
      "SALEM",
      "SALISBURY",
      "SCIPIO",
      "SUTTON",

      //Unincorporated communities

      "APPLE GROVE",
      "CHESTER",
      "DARWIN",
      "LANGSVILLE",
      "LONG BOTTOM",
      "PORTLAND",
      "REEDSVILLE",
      "TUPPERS PLAINS",

      // Athens County
      "ALBANY",
      "SHADE",
      "COOLVILLE"
  };
}
