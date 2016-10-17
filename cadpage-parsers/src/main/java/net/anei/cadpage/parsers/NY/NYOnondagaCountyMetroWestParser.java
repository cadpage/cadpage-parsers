package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class NYOnondagaCountyMetroWestParser extends DispatchProQAParser {
  
  public NYOnondagaCountyMetroWestParser() {
    super(CITY_LIST, "ONONDAGA COUNTY", "NY",
          "CALL PRI CALL/SDS CALL/L+? ADDR/Z CITY! INFO/N+? ID!");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Onondaga County
      "CAMILLUS",
      "CICERO",
      "CLAY",
      "DEWITT",
      "ELBRIDGE",
      "ELBRIDGE",
      "FABIUS",
      "GEDDES",
      "LAFAYETTE",
      "LYSANDER",
      "MANLIUS",
      "MARCELLUS",
      "ONONDAGA",
      "OTISCO",
      "POMPEY",
      "SALINA",
      "SKANEATELES",
      "SPAFFORD",
      "TULLY",
      "VAN BUREN",
      "BALDWINSVILLE",
      "CAMILLUS",
      "EAST SURACUSE",
      "ELBRIDGE",
      "FAYETTEVILLE",
      "JORDAN",
      "LIVERPOOL",
      "MINOA",
      "NORTH SYRACUSE",
      "SOLVAY",
      "SYRACUSE",

      // Cayuga County
      "AUBURN",
      "AURELIUS",
      "AURORA",
      "BRUTUS",
      "CATO",
      "CONQUEST",
      "FAIR HAVEN",
      "FLEMING",
      "GENOA",
      "IRA",
      "LEDYARD",
      "LOCKE",
      "MENTZ",
      "MERIDIAN",
      "MONTEZUMA",
      "MORAVIA",
      "MORAVIA",
      "NILES",
      "OWASCO",
      "PORT BYRON",
      "SCIPIO",
      "SEMPRONIUS",
      "SENNETT",
      "SPRINGPORT",
      "STERLING",
      "SUMMERHILL",
      "THROOP",
      "UNION SPRINGS",
      "VENICE",
      "VICTORY",
      "WEEDSPORT",

      // Madison County
      "ONEIDA",
      "BROOKFIELD",
      "CAZENOVIA",
      "DERUYTER",
      "EASTON",
      "FENNER",
      "GEORGETOWN",
      "HAMILTON",
      "LEBANON",
      "LENOX",
      "LINCOLN",
      "MADISON",
      "NELSON",
      "SMITHFIELD",
      "STOCKBRIDGE",
      "SULLIVAN",
      "CANASTOTA",
      "CHITTENANGO",
      "EARLVILLE",
      "MORRISVILLE",
      "MUNNSVILLE",
      "WAMPSVILLE",

      // Thompson County
      "ITHACA"

  };
}
