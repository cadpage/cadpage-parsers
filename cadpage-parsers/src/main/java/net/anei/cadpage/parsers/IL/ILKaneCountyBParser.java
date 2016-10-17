package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILKaneCountyBParser extends SmartAddressParser{

  private static final Pattern SOURCE_DATE_TIME= Pattern.compile("([A-Z]{2})(.*)(\\d?\\d/\\d?\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)");
  
  public ILKaneCountyBParser() {
    super(CITY_CODES, "KANE COUNTY", "IL");
    setupCallList(CALL_SET);
    setFieldList("SRC CALL PLACE ADDR APT CITY DATE TIME");
  }
 
  @Override
  public String getFilter() {
    return "EMERGIN@[172.21.10.21]";
  }
  
  @Override
  public boolean parseMsg(String city, String body, Data data) {
    Matcher sourceDateTimeMatcher = SOURCE_DATE_TIME.matcher(body);

    if (!sourceDateTimeMatcher.matches()) return false;

    data.strSource = sourceDateTimeMatcher.group(1);
    data.strDate = sourceDateTimeMatcher.group(3);
    data.strTime = sourceDateTimeMatcher.group(4);

    body = sourceDateTimeMatcher.group(2).trim();

    int ptCityStart = body.lastIndexOf(",");
    if (ptCityStart >= 0){
      city = body.substring(ptCityStart + 1).trim();
      data.strCity = convertCodes(city, CITY_CODES);
      body = body.substring(0, ptCityStart).trim();
    }

    int ptPlaceExit = body.indexOf(" / ");
    if (ptPlaceExit >= 0){
      String text = body.substring(0, ptPlaceExit);
      String call = CALL_SET.getCode(text, true);
      if (call != null) {
        data.strCall = call;
        data.strPlace = text.substring(call.length()).trim();
      } else {
        data.strCall = text;
      }
      body = body.substring(ptPlaceExit + 3).trim();
      parseAddress(StartType.START_ADDR, 0, body, data);
      data.strApt = append(data.strApt, "-", getLeft());
    }else {
      parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ, body, data);
      data.strApt = append(data.strApt, "-", getLeft());
    }
    return true;
  }
  
  CodeSet CALL_SET = new CodeSet(
    "ABDOMINAL PAIN",
    "ACCIDENT EXTR",
    "ACCIDENT UNKNOWN",
    "ACCIDENT W/INJURIES",
    "ALLERGIES/STING",
    "ASSAULT/SEX",
    "ASSIST",
    "ASSIST 0N",
    "CHEST PAIN",
    "CO DET NO ILLNESS",
    "BREATHING PROBLEMS",
    "BRUSH/GRASS/TREE",
    "DOWN/ARCHING WIRES",
    "DIABETIC PROBLEM",
    "FALLS",
    "FIRE ALARM FF CM/MF",
    "FIRE ALARM FF SF",
    "Full Still FIRE DEPARTMENT RECALL",
    "GAS LEAK IN",
    "GAS LEAK OUT",
    "General Alarm",
    "GENERAL ALARM",
    "GENERAL ALARM FIRE",
    "HEMORRHAGE/LACERATION",
    "LIFT ASSIST - NO INJURY",
    "LINE TROUBLE ALARM",
    "MEDICAL ALARM",
    "MUTUAL AID",
    "OUTSIDE FIRE",
    "OVERDOSE/POISONING",
    "PSYCHIATRIC/SUICIDE",
    "SEIZURE",
    "SICK PERSON",
    "STROKE",
    "TRAUMATIC INJURY",
    "UNCONSCIOUS/FAINTING",
    "UNKNOWN PROBLEM AMB",
    "FULL STILL",
    "FULL STILL ACCIDENT EXTR",
    "FULL STILL ALARM",
    "FULL STILL ALARM ACCIDENT EXTR",
    "FULL STILL ALARM ACCIDENT W/INJURIES",
    "FULL STILL ALARMFIRE ALARM FF CM/MF",
    "FULL STILL ALARMOTHER FIRE",
    "FULL STILL FIRE DEPARTMENT RECALL",
    "GENERAL ALARM",
    "GENERAL ALARM GENERAL ALARM FIRE",
    "STILL ALARM",
    "STILL ALARM ASSIST",
    "STILL ALARM MEDICAL ALARM",
    "STILL ALARM FIRE ALARM FF SF",
    "STILL ALARM BREATHING PROBLEMS",
    "STILL ALARM DIABETIC PROBLEM",
    "STILL ALARM UNKNOWN PROBLEM AMB",
    "STILL ALARM HEMORRHAGE/LACERATION",
    "STILL ALARM ALLERGIES/STING",
    "STILL ALARM OVERDOSE/POISONING",
    "STILL ALARM PSYCHIATRIC/SUICIDE",
    "STILL ALARM DOWN/ARCHING WIRES",
    "STILL ALARM BRUSH/GRASS/TREE",
    "STILL ALARM ASSAULT/SEX",
    "STILL ALARM UNCONSCIOUS/FAINTING",
    "STILL ALARM MUTUAL AID",
    "STILL ALARM SEIZURE",
    "STILL ALARM STILL ALARM",
    "STILL ALARM STROKE",
    "STILL ALARM SICK PERSON",
    "STILL ALARM CO DET NO ILLNESS",
    "STILL ALARM GAS LEAK IN",
    "STILL ALARM GAS LEAK OUT",
    "STILL ALARM ABDOMINAL PAIN",
    "STILL ALARM CHEST PAIN",
    "STILL ALARM TRAUMATIC INJURY",
    "STILL ALARM LIFT ASSIST - NO INJURY",
    "STILL ALARM FALLS",
    "STILL ALARM WASHDOWN",
    "STILL ALARM OUTSIDE FIRE"
  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
    "BATAV",              "BATAVIA",
    "BATAVI",             "BATAVIA",
    "GE",                 "GENEVA",
    "GENEVA",             "GENEVA",
    "MONTGOME",           "MONTGOMERY",
    "MONTGOM",            "MONTGOMERY",
    "N AURORA",           "NORTH AURORA",
    "ST CHAS",            "ST CHARLES",
    "ST CH",              "ST CHARLES",
    "ST",                 "ST CHARLES",
    "SUGAR GRV",          "SUGAR GROVE",
    "WEGO",               "OSWEGO",
    
  });
}