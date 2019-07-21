package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class NCEdgecombeCountyBParser extends DispatchA3Parser {
  
  public NCEdgecombeCountyBParser() {
    super(2, NCEdgecombeCountyParser.CITY_LIST, "EDGECOMBE COUNTY", "NC", FA3_LANDMARK_PLACE | FA3_NBH1_MAP | FA3_NBH2_MAP);
  }
  
  private static final Pattern ALT_MASTER = Pattern.compile(": (.*?) ([A-Za-z0-9,]+) Line19=(.*) Line20=(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = ALT_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT CITY CALL UNIT INFO");
      body = match.group(1).trim();
      data.strUnit = match.group(2);
      data.strSupp = append(match.group(3).trim(), " / ", match.group(4).trim());
      parseAddress(StartType.START_ADDR, body, data);
      data.strCall = getLeft();
      return data.strCall.length() > 0;
    }
    
    else {
      return super.parseMsg(body, data);
    }
  }

}
