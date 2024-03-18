package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class AZMaricopaCountyDParser extends DispatchH05Parser {

  public AZMaricopaCountyDParser() {
    super("MARICOPA COUNTY", "AZ",
          "Rip_and_Run_Report:SKIP! LOCATION_INFORMATION:EMPTY! Location:ADDR_CITY_X! Map_Link:EMPTY! Lat:GPS1! Long:GPS2! Phone:PHONE! " +
              "Quadrant:MAP! Call_Number:SKIP! Call_Type:CALL! Nature_of_Call:CALL/SDS! Caller:NAME! Call_Date/Time:DATETIME! " +
              "Call_Status_Times:EMPTY! TIMES+ Incident_Numbers:EMPTY! ID! Call_for_Service_Number:SKIP! Alerts:ALERT! " +
              "Narrative:EMPTY! INFO_BLK+ Dispatch_Order:EMPTY!");
  }

  @Override
  public String getFilter() {
    return "do-not-reply@srpmic-nsn.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_CITY_X")) return new MyAddressCityCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityCrossField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Cross Streets:");
      if (pt < 0) abort();
      data.strCross = field.substring(pt+15).trim();
      field = field.substring(0, pt).trim();
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
}


