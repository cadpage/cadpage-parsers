package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA67Parser;

public class IAWarrenCountyBParser extends DispatchA67Parser {
  
  public IAWarrenCountyBParser() {
    this("WARREN COUNTY", "IA");
  }
  
  IAWarrenCountyBParser(String defCity, String defState) {
    super("Westcom:", CITY_LIST, defCity, defState, A67_OPT_PLACE | A67_OPT_CROSS, 
          "(?:V[FG]|CLV|UI)\\d+[A-Z]?", "(?:\\b[A-Z]+\\d+\\b *)+"); 
  }
  
  @Override
  public String getAliasCode() {
    return "IAWarrenCountyB";
  }
  
  @Override
  public String getFilter() {
    return "Westcom@wdm-ia.com";
  }
  
  private static final String[] CITY_LIST = new  String[]{
    
    "WARREN COUNTY",
    
    // Cities
    "ACKWORTH",
    "BEVINGTON",
    "CARLISLE",
    "CUMMING",
    "DES MOINES",
    "HARTFORD",
    "INDIANOLA",
    "LACONA",
    "MARTENSDALE",
    "MILO",
    "NEW VIRGINIA",
    "NORWALK",
    "SANDYVILLE",
    "SPRING HILL",
    "ST. MARYS",
    "WEST DES MOINES",

    // Unincorporated communities
    "BEECH",
    "CHURCHVILLE",
    "COOL",
    "LIBERTY CENTER",
    "PROLE",

    // Townships
    "ALLEN",
    "BELMONT",
    "GREENFIELD",
    "JACKSON",
    "JEFFERSON",
    "LIBERTY",
    "LINCOLN",
    "LINN",
    "OTTER",
    "PALMYRA",
    "RICHLAND",
    "SQUAW",
    "UNION",
    "VIRGINIA",
    "WHITE BREAST",
    "WHITE OAK",
    
    
    "DALLAS COUNTY",
    
    // Cities
    "ADEL",
    "BOUTON",
    "CLIVE",
    "DALLAS CENTER",
    "DAWSON",
    "DE SOTO",
    "DEXTER",
    "GRANGER",
    "GRIMES",
    "LINDEN",
    "MINBURN",
    "PERRY",
    "REDFIELD",
    "URBANDALE",
    "VAN METER",
    "WAUKEE",
    "WEST DES MOINES",
    "WOODWARD",

    // Unincorporated community
    "BOONEVILLE",

    // Townships
    "ADAMS",
    "ADEL",
    "BEAVER",
    "BOONE",
    "COLFAX",
    "DALLAS",
    "DES MOINES",
    "GRANT",
    "LINCOLN",
    "LINN",
    "SPRING VALLEY",
    "SUGAR GROVE",
    "UNION",
    "VAN METER",
    "WALNUT",
    "WASHINGTON"
  };
}
