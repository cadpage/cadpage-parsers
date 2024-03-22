package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAArmstrongCountyEParser extends DispatchH05Parser {

  public PAArmstrongCountyEParser() {
    super("ARMSTRONG COUNTY", "PA",
          "EMS_CFS_Type:SKIP! Fire_CFS_Type:SKIP! Nature_of_Call:CALL! Narrative:EMPTY! INFO_BLK+ Location:ADDRCITY! " +
              "Call_Date/Time:DATETIME! Call_Number:ID! Fire_Quandrant:MAP! Ems_District:MAP/L! Status_Times:EMPTY TIMES+");
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "noreply@co.armstrong.pa.us";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
}
