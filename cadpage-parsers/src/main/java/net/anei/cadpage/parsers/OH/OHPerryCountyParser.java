package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHPerryCountyParser extends DispatchEmergitechParser {

  public OHPerryCountyParser() {
    super("Perry911:", CITY_LIST, "PERRY COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "@perrycountyohio.net";
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = TWP_RD_PTN.matcher(addr).replaceAll("TOWNSHIP HWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern TWP_RD_PTN = Pattern.compile("\\b(?:MONDAY CREEK|SALT LICK|[A-Z]+) TWP RD\\b");
  
  private static final String[] CITY_LIST = new String[] { 
    
   //villages
    "CORNING",
    "CROOKSVILLE",
    "GLENFORD",
    "HEMLOCK",
    "JUNCTION CITY",
    "NEW LEXINGTON",
    "NEW STRAITSVILLE",
    "RENDVILLE",
    "ROSEVILLE",
    "SHAWNEE",
    "SOMERSET",
    "THORNVILLE",

    //townships
    "BEARFIELD TWP",
    "CLAYTON TWP",
    "COAL TWP",
    "HARRISON TWP",
    "HOPEWELL TWP",
    "JACKSON TWP",
    "MADISON TWP",
    "MONDAY CREEK TWP",
    "MONDAY CREEK",
    "MONROE TWP",
    "PIKE TWP",
    "PLEASANT TWP",
    "READING TWP",
    "SALT LICK TWP",
    "THORN TWP",
   
    //other communities
    "BRISTOL",
    "GLASS ROCK",
    "MCLUNEY",
    "MILLIGAN",
    "MOUNT PERRY",
    "MOXAHALA",
    "PORTERSVILLE",
    "REHOBOTH",
    "WHIPSTOWN",
    
    //ghost town
    "SAN TOY",
    
    // Athens Counthy
    "AMES TWP",
    "CHAUNCEY TWP",
    "TRIMBLE TWP",
    "YORK TWP",
    "AMESVILLE",
    "DOVER",
    "JACKSONVILLE",
    "GLOUSTER",
    "NELSONVILLE",
    "TRIMBLE",
    
    // Fairfield County
    "PLEASANT TWP",
    "RICHLAND TWP",
    "RUSH CREEK TWP",
    "WALNUT TWP",
    "BUCKEYE LAKE",
    "BREMEN",
    "LANCASTER",
    "MILLERSPORT",
    "PLEASANTVILLE",
    "RUSHVILLE",
    "THURSTON",
    "WEST RUSHVILLE",
    
    // Hocking County
    "FALLS TWP",
    "GREEN TWP",
    "MARION TWP",
    "WARD TWP",
    "BUCHTEL",
    "FALLS",
    "LOGAN",
    "MURRAY CITY",
    
    // Licking County
    "BOWLING GREEN",
    "FRANKLIN TWP",
    "HOPEWELL TWP",
    "LICKING TWP",
    "UNION TWP",
    "GRATIOT",
    "HARBOR HILLS",
    "HEBRON",
    
    // Muskingum County
    "BRUSH CREEK TWP",
    "CLAY TWP",
    "FALLS TWP",
    "HOPEWELL TWP",
    "NEWTON TWP",
    "SPRINGFIELD TWP",
    "SOUTH ZANESVILLE",
    "ROSEVILLE",
    "ZANESVILLE",
    
    // Morgan County
    "DEERFIELD TWP",
    "HOMER TWP",
    "UNION TWP",
    "YORK TWP"
  };
}