package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class CASolanoCountyDParser extends DispatchA3Parser {

  public CASolanoCountyDParser() {
    super("SOLANO COUNTY", "CA",
          "ID! Line2:ADDR! Line3:APT Line4:APT Line5:CITY! Line6:X! Line7:X! Line8:MAP! Line9:INFO1! Line10:CODE! Line11:CALL! Line12:NAME! Line13:PHONE! Line14:UNIT! Line15:MAP! Line16:INFO/N! Line17:INFO/N! Line18:INFO/N!",
          FA3_NBH1_BOX | FA3_NBH2_BOX);
    setBreakChar('=');
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity, CITY_CODES);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "SUI",  "SUISUN CITY",
      "VCVL", "VACAVILLE"
  });
}
