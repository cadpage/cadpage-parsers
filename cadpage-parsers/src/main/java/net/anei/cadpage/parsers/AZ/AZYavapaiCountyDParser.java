package net.anei.cadpage.parsers.AZ;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;

public class AZYavapaiCountyDParser extends SmartAddressParser {

  public AZYavapaiCountyDParser() {
    super("YAVAPAI COUNTY", "AZ");
  }
  
  @Override
  public String getFilter() {
    return "@Rmetro.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (parseDispatch(body, data)) return true;
    if (parseCancel(body, data)) return true;
    if (parseRunReport(body, data)) return true;
    return false;
  }
  
  private boolean parseDispatch(String body, Data data) {
    FParser fp = new FParser(body);
    String unit = fp.get(7);
    if (fp.check(" ")) return false;
    String call = fp.get(35);
    if (fp.check(" ")) return false;
    String addr = fp.get(30);
    if (!fp.check("APT#")) return false;
    String apt = fp.get(4);
    if (!fp.check("CITY:")) return false;
    String city = fp.get(14);
    if (!fp.check("MAP:")) return false;
    String map = fp.get(8);
    if (!fp.check("AUTHORITY:")) return false;
    fp.skip(28);
    if (!fp.check("CROSS:")) return false;
    String cross = fp.get(33);
    if (!fp.check("RUN#:")) return false;
    String callId = fp.get();
    
    data.strUnit = unit;
    data.strCall = call;
    parseAddress(addr, data);
    data.strApt = append(data.strApt, "-", apt);
    data.strCity = convertCodes(city, CITY_CODES);
    data.strMap = map;
    data.strCross = cross;
    data.strCallId = callId;
    
    setFieldList("UNIT CALL ADDR APT CITY MAP X ID");
    return true;
  }
  
  private boolean parseCancel(String body, Data data) {
    FParser fp = new FParser(body);
    String unit = fp.get(18);
    if (fp.check(" ")) return false;
    String callId = fp.get(20);
    if (fp.check(" ")) return false;
    String addr = fp.get(40);
    if (!appendInfoLine(fp, "DISP:", 17, data)) return false;
    if (!appendInfoLine(fp, "SCNE:", 17, data)) return false;
    if (!appendInfoLine(fp, "CNCL:", 17, data)) return false;
    if (!fp.check("REASON:")) {
      data.strSupp = "";
      return false;
    }
    data.strCall = "CANCEL:"+fp.get();
  
    data.strUnit = unit;
    data.strCallId = callId;
    parseAddress(addr, data);
  
    setFieldList("UNIT ID ADDR APT INFO CALL");
    return true;
  }

  private boolean parseRunReport(String body, Data data) {
    FParser fp = new FParser(body);
    String unit = fp.get(10);
    if (fp.check(" ")) return false;
    String callId = fp.get(20);
    if (fp.check(" ")) return false;
    String addr = fp.get(40);
    if (!appendInfoLine(fp, "DISP:", 10, data)) return false;
    if (!appendInfoLine(fp, "ENRT:", 10, data)) return false;
    if (!appendInfoLine(fp, "SCNE:", 10, data)) return false;
    if (!appendInfoLine(fp, "AVAIL:", 999, data)) return false;

    data.msgType = MsgType.RUN_REPORT;
    data.strUnit = unit;
    data.strCallId = callId;
    parseAddress(addr, data);
    
    setFieldList("UNIT ID ADDR APT INFO");
    return true;
  }

  private boolean appendInfoLine(FParser fp, String label, int len, Data data) {
    if (!fp.checkAhead(0, label)) {
      data.strSupp = "";
      return false;
    }
    data.strSupp = append(data.strSupp, "\n", fp.get(len));
    return true;
  }

  Properties CITY_CODES = buildCodeTable(new String[]{
      "CORDES JUNCTIO", "CORDES JUNCTION",
  });
}
