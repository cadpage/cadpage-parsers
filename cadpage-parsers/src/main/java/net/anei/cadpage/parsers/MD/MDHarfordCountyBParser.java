package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlert2Parser;



public class MDHarfordCountyBParser extends DispatchRedAlert2Parser {
  
  private static final Pattern OOC_MUT_AID_BOX = Pattern.compile("(.*) ([BCY]C[FM]B \\d\\d-\\d\\d) (MD|PA)");
  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*?) +(PA|MD)");
  
  public MDHarfordCountyBParser() {
    super(CITY_LIST, "HARFORD COUNTY","MD");
  }

  @Override
  public String getFilter() {
    return "harfordcty@rednmxcad.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strCall.indexOf(' ');
    
    if (pt < 0) return false;
    data.strUnit = data.strCall.substring(0,pt);
    data.strCall = data.strCall.substring(pt+1).trim();
    
    if (data.strCity.length() == 0) {
      Matcher match = OOC_MUT_AID_BOX.matcher(data.strAddress);
      if (match.matches()) {
        data.strAddress = match.group(1);
        String box = match.group(2);
        switch (box.charAt(0)) {
        case 'B':
          data.strCity = "BALTIMORE COUNTY";
          break;
        case 'C':
          data.strCity = "CECIL COUNTY";
          break;
        case 'Y':
          data.strCity = "YORK COUNTY";
          break;
        }
        data.strBox = append(box, "-", data.strBox);
        data.strState = match.group(3);
      }
    }
    Matcher match = CITY_ST_PTN.matcher(data.strCity);
    if (match.matches()) {
      data.strCity = match.group(1);
      data.strState = match.group(2);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "UNIT CALL").replace("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new  String[]{

    "ABERDEEN MD",
    "HAVRE DE GRACE MD",
    "BEL AIR MD",
    
    "ABERDEEN PROVING GROUND MD",
    "EDGEWOOD MD",
    "FALLSTON MD",
    "JARRETTSVILLE MD",
    "JOPPATOWNE MD",
    "PERRYMAN MD",
    "ABINGDON MD",
    "BELCAMP MD",
    "CHURCHVILLE MD",
    "DARLINGTON MD",
    "FOREST HILL MD",
    "PYLESVILLE MD",
    "STREET MD",
    "WHITEFORD MD",
    "WHITE HALL MD",
    "MONKTON MD",
    "BALDWIN MD",
    
  };
}
