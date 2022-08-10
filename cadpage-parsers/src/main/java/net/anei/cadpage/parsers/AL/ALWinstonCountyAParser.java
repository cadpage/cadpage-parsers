package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class ALWinstonCountyAParser extends DispatchA65Parser {

  public ALWinstonCountyAParser() {
    super(CITY_LIST, "WINSTON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "@911email.net,dispatch@911comm2.info,winstonsheriff.org,@winston911.net";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBreakIns() { return true; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strSource.length() == 0) data.expectMore = true;

    if (data.strCity.length() == 0) {
      int pt = data.strAddress.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = data.strAddress.substring(pt+1).trim();
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
    }

    data.strCity = convertCodes(data.strCity, FIX_CITY_TABLE);
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "HALEYVILLE",
    "HALEYVILE",  // Misspelled
    "HVILLE",     // Misspelled

    // Towns
    "ADDISON",
    "ARLEY",
    "DOUBLE SPRINGS",
    "DOUBLE SPRING",  // Misspelled
    "DOUBLE SRINGS",  // Misspelled
    "DOUBLESPRINGS",  // Misspelled
    "LYNN",
    "NATURAL BRIDGE",
    "NAUVOO",

    // Unincorporated communities
    "BOAR TUSH",
    "DELMAR",
    "GLEN MARY",
    "HOUSTON",
    "POSEY FIELD",

    // Ghost towns
    "BATTS NEST",
    "BOOGER TREE",
    "CORINTH",

    // ?????
    "BLACK POND",
    "HELICON",
    "HELICAN",

    // Cullman County
    "CULLMAN CO",
    "CRANE HILL",
    "CULLMAN",
    "LOGAN",

    // Lawrence County
    "LAWRENCE CO"
  };

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "DOUBLE SPRING",  "DOUBLE SPRINGS",
      "DOUBLE SRINGS",  "DOUBLE SPRINGS",
      "DOUBLESPRINGS",  "DOUBLE SPRINGS",
      "HALEYVILE",      "HALEYVILLE",
      "HVILLE",         "HALEYVILLE",
      "HELICAN",        "HELICON"
  });

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "BLACK POND",     "DOUBLE SPRINGS",
      "HELICON",        "ARLEY"
  });
}
