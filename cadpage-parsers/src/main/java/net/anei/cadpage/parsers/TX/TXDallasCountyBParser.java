package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXDallasCountyBParser extends DispatchA18Parser {
  
  public TXDallasCountyBParser() {
    super(CITY_LIST, "DALLAS COUNTY","TX");
    for (String city : CITY_LIST) {
      setupCities(city + " TX", city + " TEXAS");
    }
  }

  @Override
  public String getFilter() {
    return "wilmerfd@cityofwilmer.net,crimes@seagoville.us";
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d+)- *(.*)");
  private static final Pattern TEXAS_CITY_PTN = Pattern.compile("(.*?) +(?:TX|TEXAS)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0 && body.startsWith("-")) {
      body = subject + '\n' + body;
    }
    
    if (!super.parseMsg(body, data)) return false;
    
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    
    match = TEXAS_CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "CODE " + super.getProgram();
  }

  private static String[] CITY_LIST = new String[]{

      //cities
      
      "BALCH SPRINGS",
      "CEDAR HILL",
      "CARROLLTON",
      "COCKRELL HILL",
      "COMBINE",
      "COPPELL",
      "DALLAS",
      "DESOTO",
      "DUNCANVILLE",
      "FARMERS BRANCH",
      "FERRIS",
      "GARLAND",
      "GLENN HEIGHTS",
      "GRAND PRAIRIE",
      "GRAPEVINE",
      "HUTCHINS",
      "IRVING",
      "LANCASTER",
      "LEWISVILLE",
      "MESQUITE",
      "OVILLA",
      "RICHARDSON",
      "ROWLETT",
      "SACHSE",
      "SEAGOVILLE",
      "UNIVERSITY PARK",
      "WILMER",
      "WYLIE",

      //towns
      
      "ADDISON",
      "HIGHLAND PARK",
      "SUNNYVALE",

  //Unincorporated community

      "SAND BRANCH"

  };
}
