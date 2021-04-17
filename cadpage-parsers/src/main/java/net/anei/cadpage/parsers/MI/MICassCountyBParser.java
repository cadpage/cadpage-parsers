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
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
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
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CALV", "CALVIN TWP",
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

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "16241 CENTER ST",      "+41.840833,-85.879722"

  });
}
