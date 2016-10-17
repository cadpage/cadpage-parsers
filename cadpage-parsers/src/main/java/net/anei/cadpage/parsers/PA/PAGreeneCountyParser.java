package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class PAGreeneCountyParser extends SmartAddressParser {

  public PAGreeneCountyParser() {
    super("GREENE COUNTY", "PA");
    setFieldList("CALL ADDR CITY APT INFO X MAP PLACE DATE TIME ID UNIT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MW_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "911_Call_Center@co.greene.pa.us";
  }

  private static Pattern MASTER = Pattern.compile("(.*?)(?: ,(\\d+) [A-Z]*)?#:(.*?)X:/?(.*?)/?GD:(.*?)ZN:(.*?)CP:(.*?)(?:(\\d{2}/\\d{2}/\\d{4}) (\\d{1,2}:\\d{2}:\\d{2}))?MI#:(\\d{9})(.*)");

  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("ALERT")) return false;
    
    //cut off all from the first \n on
    int ni = body.indexOf("\n");
    if (ni >= 0) body = body.substring(0, ni);
    
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;

    parseAddress(StartType.START_CALL,  FLAG_ANCHOR_END | FLAG_START_FLD_NO_DELIM, mat.group(1).trim(), data);
    data.strCity = getOptGroup(CITY_LIST.getProperty(getOptGroup(mat.group(2))));
    data.strApt = mat.group(3).trim();
    parseAddress(StartType.START_ADDR,  FLAG_ANCHOR_END | FLAG_ONLY_CROSS, mat.group(4), data);
    data.strMap = append(mat.group(5).trim(), " ", mat.group(6).trim());
    data.strPlace = mat.group(7).trim();
    data.strDate = getOptGroup(mat.group(8));
    data.strTime = getOptGroup(mat.group(9));
    data.strCallId = mat.group(10);
    data.strUnit = getOptGroup(mat.group(11));
    return true;
  }
  
  private static final Properties CITY_LIST = buildCodeTable(new String[]{
      "03", "CENTER TWP",
      "05", "CUMBERLAND TWP",
      "06", "DUNKARD TWP",
      "07", "FRANKLIN TWP",
      "13", "JACKSON TWP",
      "14", "JEFFERSON",
      "15", "JEFFERSON TWP",
      "17", "WAYNESBURG",
      "19", "PERRY TWP",
      "20", "RICES LANDING",
      "21", "RICHHILL TWP",
      "23", "WASHINGTON TWP",
      "24", "WAYNESBURG",
      "26", "WAYNESBURG",
      "29", "WHITELEY TWP"
  });
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN ALS",
      "ABDOMINAL PAIN BLS",
      "ASSAULT ALS",
      "ASSAULT BLS",
      "BACK PAIN ALS",
      "BACK PAIN BLS",
      "CARD ARREST ADULT",
      "CHEST PAIN ALS",
      "CHEST PAIN BLS",
      "CHIMNEY FIRE",
      "DIABETIC ALS",
      "DIABETIC BLS",
      "DIFF BREATHING ALS",
      "DIFF BREATHING BLS",
      "FALLS ALS",
      "FALLS BLS",
      "FIRE ALARM COMMERC",
      "FIRE ALARM RESIDEN",
      "FIRE CARBON MONO",
      "FIRE FIELD",
      "FIRE HAZMAT",
      "FIRE MUTUAL AID",
      "FIRE REKINDLE",
      "FIRE STRUCTURE RES",
      "FIRE STRUCTURE UNK",
      "FIRE VEHICLE",
      "FIRE/ODOR INVEST",
      "GENERAL SICK ALS",
      "GENERAL SICK BLS",
      "HEADACHE ALS",
      "HEADACHE BLS",
      "HEART PROBLEMS ALS",
      "HEART PROBLEMS BLS",
      "MVA NO INJURY",
      "MVA W/ INJURY ALS",
      "MVA W/ INJURY BLS",
      "MVA W/ INTRAP",
      "MVA W/ UNK INJURY",
      "OVERDOSE ALS",
      "OVERDOSE BLS",
      "PSYCH EMERG ALS",
      "PSYCH EMERG BLS",
      "PUBLIC SERVICE EMS",
      "PUBLIC SERVICE FIR",
      "SEIZURES ALS",
      "SEIZURES BLS",
      "STROKE CVA ALS",
      "STROKE CVA BLS",
      "SUICIDE ATEMPT ALS",
      "SUICIDE ATEMPT BLS",
      "TRASH FIRE",
      "TRAMTIC INJURY ALS",
      "TRAMTIC INJURY BLS",
      "UNCONSCIOUS ALS",
      "UNCONSCIOUS BLS",
      "UNKNOWN FIRE",
      "UNK PROBLEM ALS",
      "UNK PROBLEM BLS");
  
  private static final String[] MW_STREET_LIST = new String[]{
      "GRIMES RUN",
      "HUNTINGTON WOODS",
      "MT VIEW GARDENS",
      "ROY FURMAN",
      "SMITH CREEK",
      "SOUTH BRANCH MUDDY CREEK"
  };

}
