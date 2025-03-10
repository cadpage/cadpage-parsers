package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MSHarrisonCountyEParser extends DispatchH05Parser {

  public MSHarrisonCountyEParser() {
    super("HARRISON COUNTY", "MS",
          "Call_Date:DATETIME! Call_Address:ADDRCITY! Qualifier:APT! Cross_Streets:X! Common_Name:PLACE! LAT_&_LONG:GPS! " +
              "Call_Type:CALL! Incident_Number:ID! Units_Assigned:UNIT! Status_Times:EMPTY! TIMES+ Narrative:EMPTY! INFO_BLK+");
    setBreakChar(';');
  }

  @Override
  public String getFilter() {
    return "FDALERTS@GULFPORT-MS.GOV";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (!data.strApt.isEmpty()) {
      data.strAddress = stripFieldEnd(data.strAddress, ' '+data.strApt);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
