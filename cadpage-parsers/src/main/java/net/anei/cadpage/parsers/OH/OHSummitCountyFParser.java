package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHSummitCountyFParser  extends FieldProgramParser {

  public OHSummitCountyFParser() {
    super(CITY_CODES, "SUMMIT COUNTY", "OH",
          "CALL ADDRCITY! MAP");
  }

  @Override
  public String getFilter() {
    return "fdall@ems-cad.cityofgreen.org,fs1cad@cityofgreen.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP")) return new MapField("SS_MABAS_Zones, *(.*)", true);
    return super.getField(name);
  }

  public class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        String cross = field.substring(pt+1,field.length()-1).trim();
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      if (data.strApt.startsWith("Near:")) {
        data.strPlace = data.strApt;
        data.strApt = "";
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY X";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "PPM",    "AKRON"
  });
}
