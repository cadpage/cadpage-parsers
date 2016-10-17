package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALJeffersonCountyGParser extends DispatchSouthernParser {
    
  public ALJeffersonCountyGParser() {
    super(ALJeffersonCountyParser.CITY_LIST, "JEFFERSON COUNTY", "AL",
          "ADDR/S APT EMPTY EMPTY EMPTY UNIT ID TIME CALL! INFO+");
    setupCities("UNINCORPORATED");
     
  }
  
  private static final Pattern OCA_PTN = Pattern.compile("\\bOCA: *[\\d-]+$");
      
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = OCA_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{6}");
    return super.getField(name);
  }

}
