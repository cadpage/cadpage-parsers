package net.anei.cadpage.parsers.NY;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class NYWyomingCountyParser extends SmartAddressParser {
  
  public NYWyomingCountyParser() {
    super(CITY_CODES, "WYOMING COUNTY", "NY");
    setFieldList("CALL ADDR APT CITY X INFO");
  }
  
  @Override
  public String getFilter() {
    return "wyco911@wyomingco.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\n");
    if (flds.length < 2) return false;

    data.strCall = flds[0];
    Parser p = new Parser(flds[1]);
    String sAddr = p.get("(Between ");
    data.strCross = p.get(')');
    data.strSupp = p.get();
    for (int j = 2; j<flds.length; j++) data.strSupp = append(data.strSupp,"\n", flds[j].trim());
    
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, sAddr, data);
    return true;
    
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARCT", "ARCADE",
      "ATTT", "ATTICA",
      "BENT", "BENNINGTON",
      "CAST", "CASTILE",
      "COVT", "COVINGTON",
      "EAGT", "EAGLE",
      "FRET", "FREEDOM",
      "GAIT", "GAINSVEILLE",
      "GENT", "GENESEE FALLS",
      "JAVT", "JAVA",
      "MIDT", "MIDDLEBURY",
      "ORAT", "ORANGEVILLE",
      "PERT", "PERRY",
      "PIKT", "PIKE",
      "WART", "WARSAW",
      "WEAT", "WEATHERSFIELD",
      
      "ARCV", "ARCADE",
      "ATTV", "ATTICA",
      "CASV", "CASTILE",
      "GAIV", "GAINSVILLE",
      "PERV", "PERRY",
      "PIKV", "PIKE",
      "SILV", "SILVER SPRINGS",
      "STRV", "STRYKERSVILLE",
      "WARV", "WARSAW",
      "WYOV", "WYOMING"
  });
}
