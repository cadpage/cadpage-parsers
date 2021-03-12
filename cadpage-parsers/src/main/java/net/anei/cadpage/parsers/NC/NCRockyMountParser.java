package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Rocky Mount, NC
 */
public class NCRockyMountParser extends DispatchOSSIParser {

  public NCRockyMountParser() {
    super(CITY_CODES, "ROCKY MOUNT", "NC",
          "( CANCEL ADDR CITY! " +
          "| FYI ID? ADDR CALL! X+? ) INFO/N+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@rockymountnc.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "RM",  "ROCKY MOUNT"
  });

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "396 PARK AVE",                         "+35.945500,-77.786200"

  });
}
