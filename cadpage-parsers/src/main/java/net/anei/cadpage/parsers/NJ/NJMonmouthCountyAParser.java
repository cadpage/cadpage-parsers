package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

/**
 * Monmouth County, NJ
 */
public class NJMonmouthCountyAParser extends DispatchA11Parser {
  
  public NJMonmouthCountyAParser() {
    super(CITY_CODES, "MONMOUTH COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "MCSOPageNotification@mcsonj.org,MCSOCallComplete@MCSONJ.ORG,info@rooseveltfire.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.startsWith("Call Complete Notification") || body.startsWith("Incident #: ")) {
      setFieldList("CALL PLACE INFO");
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    
    if (subject.equals("MCSO Page Notification") || isPositiveId()) {
      String[] fields = body.split("\n");
      if (fields.length < 6) fields = body.split("  +"); 
      return super.parseFields(fields, 6, data);
    }
    else return false;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "12", "ENGLISHTOWN",
      "15", "FREEHOLD",
      "16", "FREEHOLD",
      "18", "HOLMDEL",
      "19", "HOWELL",
      "23", "MONROE",
      "25", "LONG BRANCH",
      "26", "MANALAPAN",
      "28", "MARLBORO",
      "32", "MILLSTONE",
      "33", "MONMOUTH BEACH",
      "36", "TINTON FALLS",
      "41", "ROOSEVELT",
      "43", "SEA BRIGHT",
      "51", "MONROE",
      "82", "ALLENTOWN",
      "84", "COLTS NECK",
      "86", "MATAWAN"
  });
}
