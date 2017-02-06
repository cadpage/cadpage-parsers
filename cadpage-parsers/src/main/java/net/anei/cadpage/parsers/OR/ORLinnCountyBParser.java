package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORLinnCountyBParser extends FieldProgramParser {
  
  public ORLinnCountyBParser() {
    super(CITY_CODES, "LINN COUNTY", "OR", 
          "( CANCEL ADDR CITY! " +
          "| FYI CALL ADDR CITY! ) INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL");
    if (name.equals("FYI")) return new SkipField("FYI:");
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
