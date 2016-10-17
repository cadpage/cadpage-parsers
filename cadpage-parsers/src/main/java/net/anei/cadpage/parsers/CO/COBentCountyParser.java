package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class COBentCountyParser extends SmartAddressParser {  
  public COBentCountyParser() {
    super(CITY_LIST, "BENT COUNTY", "CO");
    setFieldList("SRC CALL ADDR X APT PLACE CITY INFO");
  }
  
  @Override
  public String getFilter() {
    return "misty.hall@bentcounty.net,coem820@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    parseSubject(subject.trim(), data);
    return parseBody(body, data);
  }

  private static final String AGENCY_PATTERN_S
    = "bcas|bchcc|cca|lafd";
  private static final String SANTA_FE_TRAIL_INN = "santa fe trail inn";
  private static final int SFTI_L = SANTA_FE_TRAIL_INN.length();
  private static final Pattern AGENCY_PATTERN
    = Pattern.compile("(?i)("+AGENCY_PATTERN_S+"(?: & (?:"+AGENCY_PATTERN_S+"))?)(.*)");
  private void parseSubject(String subject, Data data) {
    Matcher m = AGENCY_PATTERN.matcher(subject);
    if (m.matches()) {
      data.strSource = m.group(1);
      subject = m.group(2).trim();
    }
    String c = CALL_LIST.getCode(subject);
    if (c != null) {
      data.strCall = c;
      subject = subject.substring(c.length()).trim();
    }
    
    if (SFTI_L <= subject.length() && subject.substring(0, SFTI_L).equalsIgnoreCase(SANTA_FE_TRAIL_INN)) {
      data.strPlace = subject;
      return;
    }
    Result r = parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_OPT_STREET_SFX, subject);
    if (r.isValid())
      r.getData(data);
    else
      data.strPlace = subject;
  }

  private boolean parseBody(String body, Data data) {
    if (data.strAddress.length() == 0) {
      Result r = parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_OPT_STREET_SFX, body);
      if (r.getStatus() >= STATUS_FULL_ADDRESS) {
        r.getData(data);
        body = r.getLeft();
      }
    }
    data.strSupp = body;
    return true;
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
    "CORRECTION",
    "Siren test",
    "ambualnce page",
    "ambulaNCE PAGE",
    "ambulance  page",
    "ambulance barn needs driver",
    "ambulance page",
    "vehicle accident"
  );
  
  private static final String[] CITY_LIST = {
      "ABLE",
      "BOGGSVILLE",
      "CADDOA",
      "FORT LYON",
      "HASTY",
      "HILTON",
      "LAS ANIMAS",
      "MARLMAN",
      "MCCLAVE",
      "MELINA",
      "NINAVIEW"
  };
}
