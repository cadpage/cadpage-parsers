package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOAudrainCountyParser extends DispatchA33Parser {


  public MOAudrainCountyParser() {
    super("AUDRAIN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "iti_notifications@audrain911.org";
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*), *(MO)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\u007f", "").replace("\u000c", "");
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CITY_ST_PTN.matcher(data.strCross);
    if (match.matches()) {
      data.strCity = append(data.strCity, " ", match.group(1).trim());
      data.strState = match.group(2);
      data.strCross = "";
    }
    else if (data.strCross.equals("MO")) {
      data.strState = data.strCross;
      data.strCross = "";
    }
    else if (data.strCross.endsWith("COUNTY")) {
      data.strCity = append(data.strCity, " ", data.strCross);
      data.strCross = "";
    }
    return true;
  }


}