package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Montgomery County, IL
 */
public class ILMontgomeryCountyParser extends DispatchEmergitechParser {
  
  public ILMontgomeryCountyParser() {
    super("911:", CITY_LIST, "MONTGOMERY COUNTY", "IL");
  }
  
  public String getFilter() {
    return "911@911.montgomeryco.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCross.startsWith("MAP PAGE")) {
      data.strMap = data.strCross.substring(8).trim();
      data.strCross = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace(" X ", " X MAP ");
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "COFFEEN",
    "HILLSBORO",
    "LITCHFIELD",
    "NOKOMIS",
    "WITT",

    // Villages
    "BUTLER",
    "COALTON",
    "DONNELLSON",
    "FARMERSVILLE",
    "FILLMORE",
    "HARVEL",
    "IRVING",
    "OHLMAN",
    "PANAMA",
    "RAYMOND",
    "SCHRAM CITY",
    "TAYLOR SPRINGS",
    "WAGGONER",
    "WALSHVILLE",
    "WENONAH",

    // Unincorporated communities
    "CHAPMAN",
    "HONEY BEND",
    "KORTCAMP",
    "VAN BURENSBURG",
    "ZANESVILLE",
    "ZENOBIA",

    // Townships
    "AUDUBON TWP",
    "BOIS D'ARC TWP",
    "BUTLER GROVE TWP",
    "EAST FORK TWP",
    "FILLMORE TWP",
    "GRISHAM TWP",
    "HARVEL TWP",
    "HILLSBORO TWP",
    "IRVING TWP",
    "NOKOMIS TWP",
    "NORTH LITCHFIELD TWP",
    "PITMAN TWP",
    "RAYMOND TWP",
    "ROUNTREE TWP",
    "SOUTH FILLMORE TWP",
    "SOUTH LITCHFIELD TWP",
    "WALSHVILLE TWP",
    "WITT TWP",
    "ZANESVILLE TWP"
  };
}
