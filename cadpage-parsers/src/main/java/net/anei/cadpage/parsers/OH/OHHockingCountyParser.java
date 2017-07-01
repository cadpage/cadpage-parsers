package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Hocking County, OH
 */
public class OHHockingCountyParser extends DispatchEmergitechParser {

  public OHHockingCountyParser() {
    super(CITY_LIST, "HOCKING COUNTY", "OH", TrailAddrType.INFO);
  }

  @Override
  public String getFilter() {
    return "hockingcounty911@gmail.com,Perry911@perrycountyohio.net";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "Perry911:");
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("SOUTH BLOOMING")) city = "SOUTH BLOOMINGVILLE";
    return city;
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "LOGAN",
    "WEST LOGAN",

    // Villages
    "BUCHTEL",
    "LAURELVILLE",
    "MURRAY CITY",

    // Townships
    "BENTON TWP",
    "FALLS TWP",
    "GOOD HOPE TWP",
    "GREEN TWP",
    "LAUREL TWP",
    "MARION TWP",
    "PERRY TWP",
    "SALT CREEK TWP",
    "STARR TWP",
    "WARD TWP",
    "WASHINGTON TWP",

    // Census-designated places
    "CARBON HILL",
    "HAYDENVILLE",
    "HIDEAWAY HILLS",
    "ROCKBRIDGE",

    // Unincorporated communities
    "EWING",
    "ILESBORO",
    "SAND RUN",
    "SOUTH BLOOMING",
    "SOUTH BLOOMINGVILLE",
    "UNION FURNACE",
    "SOUTH PERRY",
    
    // Perry County
    "PERRY COUNTY",
    
    // Vilages
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

    // Townships
    "BEARFIELD TWP",
    "CLAYTON TWP",
    "COAL TWP",
    "HARRISON TWP",
    "HOPEWELL TWP",
    "JACKSON TWP",
    "MADISON TWP",
    "MONDAY CREEK TWP",
    "MONROE TWP",
    "PIKE TWP",
    "PLEASANT TWP",
    "READING TWP",
    "SALT LICK TWP",
    "THORN TWP",

    // Census-designated places
    "ROSE FARM",
    "THORNPORT",

    // Other unincorporated communities
    "BRISTOL",
    "BUCKINGHAM",
    "CHALFANTS",
    "CHAPEL HILL",
    "CLARKSVILLE",
    "CROSSENVILLE",
    "GLASS ROCK",
    "MCCUNEVILLE",
    "MCLUNEY",
    "MILLERTOWN",
    "MILLIGAN",
    "MOUNT PERRY",
    "MOXAHALA",
    "NEW READING",
    "OAKFIELD",
    "PORTERSVILLE",
    "REHOBOTH",
    "SALTILLO",
    "SEGO",
    "SULPHUR SPRINGS",
    "WHIPSTOWN",

    // Ghost towns
    "DICKSONTON",
    "SAN TOY",

    
    // Athens County
    "ATHENS COUNTY",
    "NELSONVILLE"
  };
}
  