
package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCColumbusCountyParser extends DispatchSouthernParser {

  public NCColumbusCountyParser() {
    super(CITY_LIST, "COLUMBUS COUNTY", "NC",
          DSFLG_OPT_DISP_ID | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE2 | DSFLG_OPT_X | DSFLG_OPT_NAME | DSFLG_OPT_PHONE | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_OPT_TIME);
    setupSpecialStreets("HWY 701 N BY PASS");
    setupProtectedNames("ROUGH AND READY RD");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.equals("SHOPPING CTR")) {
      data.strAddress = append(data.strAddress, " ", data.strApt);
      data.strApt = "";
    }
    data.strCity = convertCodes(data.strCity.toUpperCase(), MISSPELLED_CITY_TABLE);
    if (SC_CITY_SET.contains(data.strCity)) data.strState = "SC";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }



  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
  }



  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "CHADBURN",   "CHADBOURN",
      "NICOLS",     "NICHOLS"
  });

  private static final Set<String> SC_CITY_SET = new HashSet<String>(Arrays.asList(
      "MARION COUNTY",
      "MARION CO",
      "NICHOLS"
  ));

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "TABOR CITY",
    "WHITEVILLE",

    // Towns
    "BOARDMAN",
    "BOLTON",
    "BRUNSWICK",
    "CERRO GORDO",
    "CHADBOURN",
    "CHADBURN",   // Misspelled
    "CLARENDON",
    "FAIR BLUFF",
    "FAIRBLUFF",
    "LAKE WACCAMAW",
    "SANDYFIELD",

    // Townships
    "BOGUE TWP",
    "BOLTON TWP",
    "BUG HILL TWP",
    "CERRO GORDO TWP",
    "CHADBOURN TWP",
    "FAIR BLUFF TWP",
    "LEES TWP",
    "RANSOM TWP",
    "SOUTH WILLIAMS TWP",
    "TATUMS TWP",
    "WACCAMAW TWP",
    "WELCH CREEK TWP",
    "WESTERN PRONG TWP",
    "WILLIAMS TWP",
    "WHITEVILLE TWP",

    // Census-designated places
    "DELCO",
    "EVERGREEN",
    "HALLSBORO",
    "RIEGELWOOD",

    // Unincorporated areas
    "ACME",
    "CHERRY GROVE",
    "EVERGREEN",
    "NAKINA",
    "OLYPHIC",
    "PIREWAY",
    "RIVERVIEW",
    "SELLERSTOWN",

    // Bladen County
    "BLADEN COUNTY",
    "BLADEN CO",
    "HOLLOW TWP",
    "WHITE OAK TWP",
    "TURNBULL TWP",
    "ABBOTTSBURG",
    "BLADENBORO",
    "BUTTERS",
    "CARVERS",
    "CLARKTON",
    "COUNCIL",
    "DUBLIN",
    "ELIZABETHTOWN",
    "GARLAND",
    "HARRELLS",
    "IVANHOE",
    "KELLY",
    "RIEGEL WOOD",
    "ST PAUL",
    "TAR HEEL",
    "WHITE OAK",

    // Brunswick County
    "BRUNSWICK COUNTY",
    "BRUNSWICK CO",
    "SHALLOTTE TWP",
    "WACCAMAW TWP",
    "LOCKWOODS FOLLY TWP",
    "TOWN CREEK TWP",
    "NORTHWEST TWP",
    "ASH",
    "NORTHWEST",
    "SANDY CREEK",

    // Horry County
    "HORRY COUNTY",
    "HORRY CO",

    // Marion County, SC
    "MARION COUNTY",
    "MARION CO",
    "NICHOLS",
    "NICOLS" ,   // Misspelled

    // Pender County
    "PENDER COUNTY",
    "PENDER CO",
    "CANETUCK TWP",

    // Robeson County
    "ROBESON COUNTY",
    "ROBESON CO",
    "BRITTS TWP",
    "EAST HOWELLSVILLE TWP",
    "ORRUM TWP",
    "STERLINGS TWP",
    "WISHART TWP",
    "LUMBERTON",
    "ORRUM",
    "PROCTORVILLE"
  };
}
