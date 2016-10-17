
package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCColumbusCountyParser extends DispatchSouthernParser {

  public NCColumbusCountyParser() {
    super(CITY_LIST, "COLUMBUS COUNTY", "NC", DSFLAG_DISPATCH_ID | DSFLAG_LEAD_PLACE | DSFLAG_CROSS_NAME_PHONE);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupProtectedNames("ROUGH AND READY RD");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.equals("SHOPPING CTR")) {
      data.strAddress = append(data.strAddress, " ", data.strApt);
      data.strApt = "";
    }
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "A D HINSON",
    "ANDREW JACKSON",
    "BRICK CITY",
    "C H BUFFKIN",
    "CARL SIMMONS",
    "CARL STEPHENS",
    "CLARENDON CHADBOURN",
    "CLYDE NORRIS",
    "COLUMBUS CORNERS",
    "COVEY RUN",
    "COX TOWN",
    "ERIC L COOK",
    "F M CARTRET",
    "FARM POND",
    "FLOWERS PRIDGEN",
    "FRANK NORRIS",
    "GARLAND DUNCAN",
    "GLENN BUFFKIN",
    "GREEN ACRES",
    "GREEN HILL",
    "GREEN SEA",
    "HORACE COX",
    "J K POWELL",
    "JAMES B WHITE",
    "JK POWELL",
    "JOE BROWN",
    "JOHN COX",
    "KENNY JORDAN",
    "L DUNCAN",
    "L F HINSON",
    "LEBANON CHURCH",
    "LESLIE NEWSOME",
    "LESTER SMITH",
    "LOVE MILL",
    "LOVETT BUFFKIN",
    "M M RAY",
    "MARTIN LUTHER KING",
    "MILL BRANCH CH",
    "MILL POND",
    "MILLER FARM",
    "MINOS MEARES",
    "PINE CIRCLE",
    "PINE LOG",
    "PINE NEEDLE",
    "R C SELLERS",
    "R CRIBB",
    "RALPH SPIVEY",
    "RICHLAND PARK",
    "RIDGELAND ACRES",
    "SAWMILL APARTMENTS",
    "SCHOOL HOUSE",
    "SHORT CUT",
    "SHUG NORRIS",
    "SLIPPERY LOG",
    "SWAMP FOX",
    "VINEGAR LOOP",
    "WALTER TODD",
    "WILL ANDERSON",
    "WILL INMAN"
  };

  private static final String[] CITY_LIST = new String[]{
    "BOARDMAN",
    "BOLTON",
    "BRUNSWICK",
    "CERRO GORDO",
    "CHADBOURN",
    "CLARENDON",
    "FAIR BLUFF",
    "LAKE WACCAMAW",
    "SANDYFIELD",
    "TABOR CITY",
    "WHITEVILLE"
    
  };
}
