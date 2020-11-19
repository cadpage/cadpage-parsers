package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Bethlehem, NY
 */
public class NYBethlehemParser extends DispatchH05Parser {

  public NYBethlehemParser() {
    super("BETHLEHEM", "NY",
          "Date/Time:DATETIME! Incident_Number:ID! Priority:PRI! EMS_District:MAP? Business_Name_if_Applicable:PLACE! Address:ADDRCITY/S6! Google_Maps:EMPTY! GPS:GPS! " +
             "Nature_of_Call:CALL! EMS_Call_Type:PRI/L! EMS_Description:SKIP! Radio_Channel%EMPTY! Units:UNIT! INFO_BLK");
  }

  @Override
  public String getFilter() {
    return "@TOWNOFBETHLEHEM.ORG";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("Incident Number:", "<p>Incident Number:");
    int pt = body.indexOf(" ********");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, data.strApt);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d)", true);
    return super.getField(name);
  }
}
