package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHHamiltonCountyCParser extends MsgParser {

  public OHHamiltonCountyCParser() {
    super("HAMILTON COUNTY", "OH");
    setFieldList("UNIT CALL ID ADDR APT PRI CH PLACE INFO GPS");
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

    data.strUnit = fp.get(5);

    if (!fp.check("Incident: ")) return false;
    data.strCall = fp.get(31);
    data.strCallId = fp.get(20);

    if (!fp.check("Address: ")) return false;
    parseAddress(fp.get(25), data);

    if (!fp.check("Apartment: ")) return false;
    data.strApt = append(data.strApt, "-", fp.get(10));

    if (!fp.check("Alarm Level: ")) return false;
    data.strPriority = fp.get(2);

    if (!fp.check("TAC channel: ")) return false;
    data.strChannel = fp.get(30);

    if (!fp.check("Location: ")) return false;
    data.strPlace = fp.get(21);

    if (fp.check("Units:  ")) {
      data.strUnit = fp.get(51);
    }

    if (!fp.check("Comment:  ")) return false;
    data.strSupp = fp.get(250);

    if (!fp.check("Lat: ")) return false;
    String gps1 = fp.get(8);
    if (!fp.check("  Long: ")) return false;
    String gps2 = fp.get(8);
    setGPSLoc(setDec(gps1)+','+setDec(gps2), data);

    if (fp.get().length() > 0) return false;
    return true;
  }

  private static final String setDec(String fld) {
    int pt = fld.length()-6;
    if (pt >= 0) fld = fld.substring(0,pt)+'.'+fld.substring(pt);
    return fld;
  }

}
