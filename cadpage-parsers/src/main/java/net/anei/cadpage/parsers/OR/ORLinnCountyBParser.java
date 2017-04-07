package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class ORLinnCountyBParser extends DispatchOSSIParser {
  
  public ORLinnCountyBParser() {
    super(CITY_CODES, "LINN COUNTY", "OR", 
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? CALL ADDR! ( PLACE CITY | CITY? ( PLACE MAP! | MAP? ) UNIT ) END )");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("\\d{4}", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALB",  "ALBANY",
      "BROW", "BROWNSVILLE",
      "CASC", "CASCADIA",
      "COR",  "CORVALLIS",
      "FSTR", "FOSTER",
      "GATE", "GATES",
      "HALS", "HALSEY",
      "HBRG", "HARRISBURG",
      "IDAN", "IDAHNA",
      "IND",  "INDEPENDENCE",
      "JEFF", "JEFFERSON",
      "LEB",  "LEBANON",
      "LYON", "LYONS",
      "MBG",  "MILLERSBURG",
      "MILL", "MILL CITY",
      "SCIO", "SCIO",
      "SHED", "SHEDD",
      "SILV", "SILVERTON",
      "SIS",  "SISTERS",
      "SOD",  "SODAVILLE",
      "SPF",  "SPRINGFIELD",
      "STY",  "STAYTON",
      "SWH",  "SWEET HOME",
      "TANG", "TANGENT"
  });
}
