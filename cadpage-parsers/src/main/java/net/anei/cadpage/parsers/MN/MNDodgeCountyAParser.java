package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Dodge County, MN
 */

public class MNDodgeCountyAParser extends DispatchA27Parser {

  public MNDodgeCountyAParser() {
    super("DODGE COUNTY", "MN", "\\d{8}|RDM");
    setupGpsLookupTable(MNDodgeCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "adminstrator@co.dodge.mn.us";
  }

  private static final Pattern UNIT_ZEROS_PTN = Pattern.compile("(?:^|(?<= ))0+(?=.)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strUnit = UNIT_ZEROS_PTN.matcher(data.strUnit).replaceAll("");
    return true;
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }
}
