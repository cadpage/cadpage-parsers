package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class SCAndersonCountyAParser extends DispatchB2Parser {
  
  private static final Pattern POLICE_MASTER = Pattern.compile("([-_A-Z0-9]+): *(\\d{10}) +([-A-Z]+) +(.*)");
  private static final Pattern UNIT_PTN = Pattern.compile("^(?:ANDERSON CO 911|AND 911|active911|([-a-z0-9]+)):");
 
  public SCAndersonCountyAParser() {
    super(SCAndersonCountyParser.CITY_LIST, "ANDERSON COUNTY", "SC");
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    body = encode(body);
    
    // Check for a police dispatched alert.  These have a different page format
    if (subject.equals("Return Phone")) {
      setFieldList("UNIT PHONE CODE CALL ADDR APT X INFO");
      body = body.toUpperCase();
      Matcher match = POLICE_MASTER.matcher(body);
      if (!match.matches()) return false;
      data.strUnit = match.group(1);
      if (data.strUnit.startsWith("ACTIVE911_")) data.strUnit = data.strUnit.substring(10);
      data.strPhone = match.group(2);
      data.strCode = match.group(3);
      body = match.group(4);
      
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_CROSS_FOLLOWS, body, data);
      body = getLeft();
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body, data);
      data.strSupp = getLeft();
    }
    
    else {
      // See if this is one of our pages
      Matcher match = UNIT_PTN.matcher(body);
      if (!match.find()) return false;
      data.strUnit = getOptGroup(match.group(1));
      body = body.substring(match.end()).trim();
      
      if (!body.contains(" Cad:")) {
        data.strCall = "GENERAL ALERT";
        if (subject.length() > 0) body = '(' + subject + ") " + body;
        data.strPlace = decode(body);
        return true;
      }
      
      if (subject.equals("EVENT")) {
        body = "EVENT: " + body;
      } else if (subject.length() > 0) {
        body = '(' + subject + ')' + body;
      }
      
      if (!body.contains(" Cad:")) {
        data.strCall = "GENERAL ALERT";
        data.strPlace = decode(body);
        return true;
      }
      
      // Call superclass parser
      body = body.replace('@', '&').replace("//", "/");
      if (!super.parseMsg(body, data)) return false;
    }
    data.strAddress = decode(data.strAddress);
    data.strCross = decode(data.strCross);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    return encode(sAddress);
  }

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return decode(sAddress);
  }
  
  private String encode(String sAddress) {
    return sAddress.replace("SIX AND TWENTY", "SIX_AND_TWENTY").replace("D AND M", "D_AND_M");
  }
  
  private String decode(String sAddress) {
    return sAddress.replace("SIX_AND_TWENTY", "SIX AND TWENTY").replace("D_AND_M", "D AND M");
  }
}
