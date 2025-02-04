package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PASomersetCountyBParser extends DispatchH05Parser {

  public PASomersetCountyBParser() {
    super("SOMERSET COUNTY", "PA",
          "Call_for_Service_#:ID! Call_Date/Time:DATETIME! Location:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! Map:EMPTY! " +
              "Caller:NAME! Phone:PHONE! FIRE_Zone:MAP! EMS_District:MAP/L! Incident_Numbers:ID! Radio_Channel:CH! All_Units:UNIT! " +
              "Dispatch_Order:EMPTY SKIP+ Status_Times:EMPTY TIMES+ Narrative:EMPTY! INFO_BLK+");

  }

  @Override
  public String getFilter() {
    return "cadreports@fcema.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
