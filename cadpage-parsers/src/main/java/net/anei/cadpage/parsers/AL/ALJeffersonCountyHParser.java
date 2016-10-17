package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALJeffersonCountyHParser extends DispatchSouthernParser {
    
  public ALJeffersonCountyHParser() {
    super(ALJeffersonCountyParser.CITY_LIST, "JEFFERSON COUNTY", "AL",
          "ADDR/S APT EMPTY EMPTY ID TIME CALL! INFO+");
  }
  
  private static final Pattern OCA_PTN = Pattern.compile("\\bOCA: *[\\d-]+$");
  private static final Pattern MM_PTN = Pattern.compile(" MM, *");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = OCA_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    body = MM_PTN.matcher(body).replaceAll(" MM ");
    if (!super.parseMsg(body, data)) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{9}");
    return super.getField(name);
  }

}
