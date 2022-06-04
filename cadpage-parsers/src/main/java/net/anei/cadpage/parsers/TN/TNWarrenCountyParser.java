package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class TNWarrenCountyParser extends DispatchEmergitechParser {

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("WarrenCo911:\\[(\\d+)\\]--? *");

  public TNWarrenCountyParser() {
    super("warrenco911:", true, CITY_LIST, "WARREN COUNTY", "TN", TrailAddrType.PLACE);
  }

  @Override
  public String getFilter() {
    return "warrenco911@benlomand.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (super.parseMsg(body, data)) return true;

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    setFieldList("UNIT INFO");
    data.initialize(this);
    data.strUnit = match.group(1);
    body = body.substring(match.end());
    body = body.replace(" W INJURY", " W-INJURY");
    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = body;
    return true;
  }

  private static final String[] CITY_LIST = new String[]{

    // Communities
    "CENTERTOWN",
    "MCMINNVILLE",
    "MORRISON",
    "VIOLA",

    // Unincorporated communities
    "BETHANY",
    "CAMPAIGN",
    "DAYLIGHT",
    "DIBRELL",
    "EASTSIDE",
    "FAIRVIEW",
    "HARRISON FERRY",
    "HIAWASSEE",
    "IRVING COLLEGE",
    "JACKSBORO",
    "LUCKY",
    "NEWTOWN",
    "ROCK ISLAND",
    "SMARTT",
    "VERVILLA",
    "WAYSIDE",

    // DeKalb County
    "SMITHVILLE"

  };
}
