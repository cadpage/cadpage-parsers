package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Clearfield County, PA
 */
public class PAClearfieldCountyAParser extends FieldProgramParser {

  public PAClearfieldCountyAParser() {
    super("CLEARFIELD COUNTY", "PA",
           "Inc:CALL! Add:ADDR! City:CITY! Units:UNIT SRC DATETIME END");
  }

  @Override
  public String getFilter() {
    return "alerts@clearfieldalerts.com,Clearfield Alerts";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Clearfield Alert")) return false;
    if (!parseFields(body.split("\n"), 3, data)) return false;
    data.strCity = convertCodes(data.strCity, CITY_CODES);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_FMT);
    return super.getField(name);
  }
  private static final DateFormat DATE_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BURN_TWP",   "BURNSIDE TWP",
      "CHEST_CAM",  "CHEST TWP",
      "FALLS_JEF",  "FALLS CREEK",
      "HORTN_ELK",  "HORTON TWP",
      "READE_CAM",  "READE TWP",
      "SNDER_JEF",  "SNYDER TWP",
      "WASH_JEFF",  "WASHINGTON TWP",
      "WHTE_CAM",   "WHITE TWP",
      "WINSL_JEF",  "WINSLOW TWP"
  });
}
