package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHHamiltonCountyCParser extends MsgParser {

  public OHHamiltonCountyCParser() {
    super("HAMILTON COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "inforad@cincinnati-oh.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD page")) return false;

    FParser fp = new FParser(body);

    if (fp.check("Unit: ")) {
      setFieldList("UNIT CALL ID ADDR APT PRI CH PLACE");

      data.strUnit = fp.get(7);

      if (!fp.check("Incident: ")) return false;
      data.strCall = fp.get(11);

      data.strCallId = fp.get(15);

      if (!fp.check(" Address: ")) return false;
      parseAddress(fp.get(40), data);

      if (!fp.check("Apartment: ")) return false;
      data.strApt = append(data.strApt, "-", fp.get(10));

      if (!fp.check("Alarm Level: ")) return false;
      data.strPriority = fp.get(2);

      if (!fp.check("TAC channel: ")) return false;
      data.strChannel = fp.get(6);

      if (!fp.check("Location: ")) return false;
      data.strPlace = fp.get();
      return true;
    }

    else if (fp.check("Incident: ")) {
      setFieldList("CALL ID ADDR APT PRI CH PLACE UNIT INFO GPS");
      data.strCall = fp.get(31);
      data.strCallId = fp.get(15);

      if (!fp.check("     Address: ")) return false;
      parseAddress(fp.get(25), data);

      if (!fp.check("Apartment: ")) return false;
      data.strApt = append(data.strApt, "-", fp.get(9));

      if (!fp.check("Alarm Level: ")) return false;
      data.strPriority = fp.get(2);

      if (!fp.check("TAC channel: ")) return false;
      data.strChannel = fp.get(30);

      if (!fp.check("Location: ")) return false;
      data.strPlace = fp.get(21);

      if (!fp.check("Units: ")) return false;
      data.strUnit = fp.get(51);

      if (!fp.check("Comment: ")) return false;
      data.strSupp = fp.get(250);

      if (!fp.check("Lat:")) return false;
      String gps1 = fp.get(8);
      if (!fp.check("  Long:")) return false;
      String gps2 = fp.get(8);
      setGPSLoc(setDec(gps1)+','+setDec(gps2), data);

      if (fp.get().length() > 0) return false;
      return true;
    }

    return false;
  }

  private static final String setDec(String fld) {
    int pt = fld.length()-6;
    if (pt >= 0) fld = fld.substring(0,pt)+'.'+fld.substring(pt);
    return fld;
  }

}
