package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MDSaintMarysCountyBParser extends DispatchH05Parser {

  public MDSaintMarysCountyBParser() {
    super("SAINT MARYS COUNTY", "MD",
          "Common_Name:PLACE! Address:ADDRCITY! Call_Type:CALL! Call_Date/Time:DATETIME! Units:EMPTY! UNITS<+ GPS:GPS! " +
                "Box:BOX! Radio_Channel:CH! Dispatch_Update:INFO! CFS_Number:SKIP! Incident_#:ID! Narrative:INFO/N! INFO/N+",
                "tr");
  }

  @Override
  public String getFilter() {
    return "noreply@stmaryscountymd.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("UNITS")) return new MyUnitField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {

    private int cnt = 0;

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("<|") && field.endsWith("|>")) {
        cnt = 0;
      } else {
        cnt++;
        if (cnt == 2) {
          data.strUnit = append(data.strUnit, ",", field);
        } else if (cnt == 3) {
          if (!data.strSource.contains(field)) {
            data.strSource = append(data.strSource, ",", field);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "UNIT SRC";
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
}
