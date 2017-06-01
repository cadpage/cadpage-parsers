package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHMercerCountyAParser extends DispatchEmergitechParser {
  
  public OHMercerCountyAParser() {
    super(true, CITY_LIST, "MERCER COUNTY", "OH");
    setupSpecialStreets("ERASTUS DURBIN");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern DIR_OF_PTN = Pattern.compile("\\b([NSEW])/OF\\b");
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" BTWN ", " BETWEEN ");
    body = DIR_OF_PTN.matcher(body).replaceAll("$1O");
    if (!super.parseMsg(body, data)) return false;
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, OHMercerCountyParser.CALL_CODES);
    data.strCross = data.strCross.replace(" BETWEEN: ", " BTWN ");
    data.strSupp = data.strSupp.replace(" BETWEEN: ", " BTWN ");
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  protected Result parseAddress(StartType sType, int flags, String address) {
    if (sType == StartType.START_PLACE) sType = StartType.START_ADDR;
    return super.parseAddress(sType, flags, address);
  }

  @Override
  public String adjustMapAddress(String addr) {
    return OHMercerCountyParser.adjustMapAddr(addr);
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "CELINA",

    // Villages
    "BURKETTSVILLE",
    "CHICKASAW",
    "COLDWATER",
    "FORT RECOVERY",
    "MENDON",
    "MONTEZUMA",
    "ROCKFORD",
    "ST HENRY",

    // Townships
    "BLACK CREEK",
    "BUTLER",
    "CENTER",
    "DUBLIN",
    "FRANKLIN",
    "GIBSON",
    "GRANVILLE",
    "HOPEWELL",
    "JEFFERSON",
    "LIBERTY",
    "MARION",
    "RECOVERY",
    "UNION",
    "WASHINGTON",

    // Unincorporated communities
    "CARTHAGENA",
    "CASSELLA",
    "CHATTANOOGA",
    "CRANBERRY PRAIRIE",
    "DURBIN",
    "ERASTUS",
    "HINTON",
    "MACEDON",
    "MARIA STEIN",
    "MERCER",
    "NEPTUNE",
    "PADUA",
    "PHILOTHEA",
    "SEBASTIAN",
    "SHARPSBURG",
    "SHIVELY",
    "SKEELS CROSSING",
    "SKEELS CROSSROADS",
    "ST JOSEPH",
    "ST PETER",
    "ST ROSE",
    "TAMA",
    "WABASH",
    "WENDELIN",
    
    // Alaglaize County
    "ST MARYS",
    
    // Miami County
    "TROY",
    
    // Wert County
    "VENEDOCIA"
  };
}
