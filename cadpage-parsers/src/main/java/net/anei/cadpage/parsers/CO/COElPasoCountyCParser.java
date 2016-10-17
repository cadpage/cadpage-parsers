package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class COElPasoCountyCParser extends MsgParser {
  
  public COElPasoCountyCParser() {
    super("EL PASO COUNTY", "CO");
    setFieldList("CALL ADDR APT PLACE CITY X INFO");
  }
  
  @Override
  public String getFilter() {
    return "ept@ept911.info";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    FParser p = new FParser(body);
    data.strCall = p.get(18);
    parseAddress(p.get(38), data);
    if (!p.check("*Apt/Loc:")) return false;
    data.strApt = append(data.strApt, "-", p.get(8));
    String place = p.get(18);
    if (NUMERIC.matcher(place).matches()) {
      data.strApt = append(data.strApt, "-", place);
    } else {
      data.strPlace = place;
    }
    data.strCity = p.get(15);
    if (!p.check("*Cross:")) return false;
    data.strCross = p.get(40);
    if (!p.check("INFO")) return false;
    data.strSupp = stripFieldEnd(p.get(), ",");
    return true;
  }
}
