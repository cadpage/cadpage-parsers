package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHMiamiCountyParser extends FieldProgramParser {

  public OHMiamiCountyParser() {
    super(CITY_CODES, "MIAMI COUNTY", "OH",
           "SKIP SKIP UNIT DISP ADDRCITY SRC INFO+? ID! X DATETIME");
  }
  
  @Override
  public String getFilter() {
    return "911dispatch@miamicounty911.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Message from 911")) return false;
    return parseFields(body.split("\n"), 8, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DISP")) return new SkipField("DISP", true);
    if (name.equals("ID")) return new IdField("\\d\\dSPE\\d{4}", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
    "BRO", "Brown Twp",
    "FLE", "Fletcher Village"
  });
}
