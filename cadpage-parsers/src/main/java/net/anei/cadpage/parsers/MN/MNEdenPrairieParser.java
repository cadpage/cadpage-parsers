package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MNEdenPrairieParser extends SmartAddressParser {

  public MNEdenPrairieParser() {
    super(CITY_LIST, "EDEN PRAIRIE", "MN");
    
    setFieldList("CH MAP ADDR APT PLACE CITY CALL ID");
  }
  
  private static Pattern MASTER = Pattern.compile("(?:(.+?)  +)?(?:(Quad \\d+/\\d+) +)?(.+)");
  private static Pattern CITY_SPACER = Pattern.compile("(?<! )(?=[A-Z][a-z])");
  private static Pattern CALL_CALLID = Pattern.compile("(.*) (\\d{4}-\\d{8})");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Dispatch Information")) return false;
    
    //match master and get groups 2 and 3 for later
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    String g2 = mat.group(2);
    String g3 = mat.group(3);
    
    //CH?
    data.strChannel = getOptGroup(mat.group(1));
    
    //MAP - will be either "Quad \\d/\\d" or a city name
    if (g2 != null) data.strMap = g2;
    else { //if Quad construct is absent, check if group 3 starts with a city
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, g3);
      data.strMap = res.getCity();
      if (data.strMap.length() > 0) g3 = res.getLeft();
    }

    //ADDR PLACE CITY
    g3 = CITY_SPACER.matcher(g3).replaceFirst(" "); //add a space in front of city if needed
    parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD, g3, data);
    if (getStatus() < STATUS_STREET_NAME) return false;
    data.strPlace = getPadField();
    
    //CALL ID - if pat doesn't match (never happens) put it all in call
    String left = getLeft();
    mat = CALL_CALLID.matcher(left);
    if (mat.matches()) {
      data.strCall = mat.group(1);
      data.strCallId = mat.group(2);
    } else data.strCall = left;
      
    return true;
  }
  
  private static String[] CITY_LIST = new String[]{"EDEN PRAIRIE","MINNETONKA"};

}
