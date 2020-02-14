package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXDallasCountyBParser extends DispatchA18Parser {
  
  public TXDallasCountyBParser() {
    super(TXDallasCountyParser.CITY_LIST, "DALLAS COUNTY","TX");
    for (String city : TXDallasCountyParser.CITY_LIST) {
      setupCities(city + " TX", city + " TEXAS");
    }
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
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
    
    int pt = body.indexOf("\n---\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    if (!super.parseMsg(body, data)) return false;
    
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    
    match = TEXAS_CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    
    if (data.strPlace.equalsIgnoreCase("TEXAS")) data.strPlace = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "CODE " + super.getProgram();
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "5900 S IH 45",         "+32.587831,-96.675068"
  });
}
