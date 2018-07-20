package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MICassCountyBParser extends DispatchOSSIParser {
  
  public MICassCountyBParser() {
    super(CITY_CODES, "CASS COUNTY", "MI", 
        "CALL ADDR ( PLACE PLACE CITY! | PLACE CITY! | CITY! | EMPTY+? ) X+? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CassCounty911@casscounty.org";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.contentEquals("911 Info")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CASS", "CASSOPOLIS",
      "DOW",  "DOWAGIAC",
      "EDW",  "EDWARDSBURG",
      "HOWA", "HOWARD TWP",
      "JONE", "JONES",
      "MAR",  "MARCELLUS",
      "NILE", "NILES",
      "UNIO", "UNION",
      "VAND", "VANDALIA",
      "WHIT", "WHITE PIGEON"
  });
}
