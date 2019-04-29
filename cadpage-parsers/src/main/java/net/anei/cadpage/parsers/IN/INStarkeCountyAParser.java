package net.anei.cadpage.parsers.IN;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

/**
 * Starke County, IN
 */
public class INStarkeCountyAParser extends DispatchA29Parser {

  public INStarkeCountyAParser() {
    super(INStarkeCountyParser.CITY_LIST, "STARKE COUNTY", "IN");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "HAMLET INDUSTRIAL",
        "LONG LANE",
        "RANNELLS E",
        "ST LOUIS"
    );
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@co.starke.in.us,messaging@iamresponding.com";
  }
  
  private static final Pattern ADDR_HALF_PTN = Pattern.compile("[ /](\\d+ 1/2), *");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)\\(([ A-Z]+)\\)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // The put commas following 1/2 street numbers, which really messes up the address parsing
    body = ADDR_HALF_PTN.matcher(body).replaceFirst(" $1 ");
    if (!super.parseMsg(body, data)) return false;
    Matcher match = ADDR_CITY_PTN.matcher(data.strAddress);
    if (match.matches()) {
      String city = match.group(2).trim();
      if (isCity(city)) {
        data.strAddress = match.group(1).trim();
        data.strCity = city;
      }
    }
    if (data.strCity.length() == 0) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, data.strSupp, data);
    }
    data.strCity = convertCodes(data.strCity.toUpperCase(), INStarkeCountyParser.CITY_FIXES);
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    return INStarkeCountyParser.baseAdjustMapAddress(addr);
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ALARM-BURGLAR-RESIDENTIAL",
      "ASSIST ANOTHER AGENCY",
      "CHEST PAINS",
      "CO2 DETECTOR ALARM",
      "DOMESTIC COMPLAINT",
      "FOLLOW-UP INVESTIGATION",
      "HARRASSMENT",
      "INTOXICATED PERSON",
      "RESCUE/AMBULANCE",
      "FAMILY PROBLEMS",
      "FIRE-BRUSH",
      "FIRE-GENERAL",
      "FIRE-HAZMATS INVOLVED",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "FIRE ALARM",
      "LIFT ASSIST",
      "LIFTING ASSISTANCE",
      "MISCELLANEOUS",
      "MISSING PERSON",
      "NOTIFICATIONS",
      "OVERDOSE",
      "POSS. STROKE",
      "RAPE",
      "ROAD CONDITION",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS VEHICLE",
      "TRAFFIC ACCIDENT-INJURIES",
      "TRAFFIC ACCIDENT-PROPERTY DAMAGE",
      "TROUBLE BREATHING",
      "BURN COMPLAINT",
      "911 CELLPHONE CALL",
      "TRAFFIC COMPLAINT",
      "911 HANG UP/ MISDIAL",
      "GAS ODOR",
      "GAS SPILL",
      "MEDICAL",
      "MEDICAL TRANSFER",
      "CONTROL BURN",
      "DECEASED PERSON",
      "SUICIDAL SUBJECT",
      "TRAFFIC ACCIDENT SLIDE OFF NO DAMAGE",
      "TRAFFIC ACCIDENT UNKNOWN",
      "TRAFFIC HAZARD",
      "FIRE PAGER MESSAGE",
      "UNATTENDED DEATH",
      "WARRANT CHECK",
      "WELFARE CHECK"
  );
}
