package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class OHRichlandCountyBParser extends DispatchOSSIParser {
  
  public OHRichlandCountyBParser() {
    super(CITY_CODES, "RICHLAND COUNTY", "OH", 
          "SRC CALL UNIT? ADDR CITY BOX! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@richlandcountyoh.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Message Forwarded by PageGate")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("\\d{2,3}", true);
    if (name.equals("UNIT")) return new UnitField("[0-9[A-Z],]+", true);
    if (name.equals("BOX")) return new BoxField("\\(S\\) *(.*?) +\\(N\\)", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEL",  "BELLVILLE",
      "LUC",  "LUCAS",
      "MANS", "MANSFIELD",
      "PERR", "PERRY TWP"
  });
}
