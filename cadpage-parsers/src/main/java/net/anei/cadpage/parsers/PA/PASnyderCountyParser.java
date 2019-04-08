package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class PASnyderCountyParser extends DispatchB3Parser {
  
  private boolean good;

  public PASnyderCountyParser() {
    super(CITY_LIST, "SNYDER COUNTY", "PA", B2_FORCE_CALL_CODE);
    setupDoctorNames("SODHI");
  }
  
  @Override
  public String getFilter() {
    return "SNYDER911@snydercounty.org,5705419455";
  }
  
  private static final Pattern CAD_PTN = Pattern.compile(" C[Aa][Dd][ :](?=\\d)");
  private static final Pattern COUNTY_PTN = Pattern.compile("([A-Z]C)\\b\\.? *(.*)");
  private static final Pattern NOT_NAME_PTN = Pattern.compile(".*[ ,][MNJU]C\\b.*");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    good = body.startsWith("SNYDER911:") || body.startsWith("SYNDER911:");
    if (good) body = body.substring(10).trim();
    if (subject.equals("EVENT")) {
      subject = "";
      body = "EVENT:" + body;
    }

    body = CAD_PTN.matcher(body).replaceAll(" Cad:");
    body = body.replace(" MAP:", "Map:");
    body = body.replace(" Map:Grids:", " Map: Grids:");
    body = body.replace(" Map:00000,000", " Map: Grids:00000,000");
    if (! super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    data.strCity = stripFieldStart(data.strCity, "BORO OF ");
    data.strCity = convertCodes(data.strCity.toUpperCase(), MISSPELLED_CITIES);
    
    data.strName = stripFieldStart(data.strName, "/");
    Matcher match = COUNTY_PTN.matcher(data.strName);
    if (match.matches()) {
      String county = COUNTY_CODES.getProperty(match.group(1));
      if (county != null) {
        if (data.strCity.length() == 0) data.strCity = county;
        data.strName = match.group(2).trim();
      }
    }
    return true;
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    if (good) return true;
    return super.isPageMsg(body);
  }

  @Override
  protected boolean notName(String name) {
    return NOT_NAME_PTN.matcher(name).matches();
  }

  private static final String[] CITY_LIST = new String[]{
    "BEAVERTOWN",
    "BEAVERTOWN BORO",
    "BORO OF VEAVERTOWN",
    "FREEBURG",
    "FREEBURG BORO",
    "BORO OF FREEBURG",
    "MCCLURE",
    "MCCLURE BORO",
    "BORO OF MCCLURE",
    "MIDDLEBURG",
    "MIDDLEBURG BORO",
    "BORO OF MIDDLEBURG",
    "SELINGROVE",        // typo
    "SELINGROVE BORO",   // typo
    "SELINSGROVE",
    "SELINSGROVE BORO",
    "BORO OF SELINSGROVE",
    "SHAMOKIN DAM",
    "SHAMOKIN DAM BORO",
    "BORO OF SHAMOKIN DAM",
    
    "ADAMS TWP",
    "BEAVER TWP",
    "CENTER TWP",
    "CHAPMAN TWP",
    "FRANKLIN TWP",
    "JACKSON TWP",
    "MIDDLECREEK TWP",
    "MONROE TWP",
    "PENN TWP",
    "PERRY TWP",
    "SPRING TWP",
    "UNION TWP",
    "WASHINGTON TWP",
    "WEST BEAVER TWP",
    "WEST PERRY TWP",
    
    // Juniata County
    "JUNIATA COUNTY",
    
    "DELAWARE TWP",
    "FAYETTE TWP",
    "FERMANAGH TWP",
    "GREENWOOD TWP",
    "MONROE TWP",
    "MILFORD TWP",
    "SUSQUEHANNA TWP",
    "TURBETT TWP",
    "WALKER TWP",
    
    "MIFFLINTOWN",
    "MIFFLINTOWN BORO",
    "BORO OF MIFFLINTOWN",
    "PORT ROYAL",
    "PORT ROYAL BORO",
    "BORO OF PORT ROYAL",
    "THOMPSONTOWN",
    "THOMPSONTOWN BORO",
    "BORO OF THOMPSONTOWN",
    
    "MCALISTERVILLE",
    "MCALLISTERVILLE",
    "MEXICO",
    "RICHFIELD",
    
    // Mifflin County
    "MIFFLIN COUNTY",
    
    "ARMAGH TWP",
    "BROWN TWP",
    "DECATUR TWP",
    "DECATOR TWP",  // misspelled
    "DECATURE TWP",  // Misspelled
    "DERRY TWP",
    
    "BURNHAM",
    "BURNHAM BORO",
    "BORO OF BURNHAM",
    "JUANIATA TERRACE",
    "JUNIATA TERRACE BORO",
    "BORO OF JUNIATA TERRACE BORO",

    "HIGHLAND PARK",
    "MILROY",
    "REEDSVILLE",
    "YEAGERTOWN",
    
    // Monotour County
    "MONOTOUR COUNTY", 
    
    "DERRY VALLEY TWP",
    "LIBERTY TWP",
    "LIMESTONE TWP",
    "MAHONING TWP",
    
    "DANVILLE",
    "DANVILLE BORO",
    "BORO OF DANVILLE",
    
    "MECHANICSVILLE",
    
    // Northumberland County
    "NORTHUMBERLAND COUNTY",
    
    "COAL TWP",
    "DELEWARE TWP",
    "EAST CAMERON TWP",
    "EAST CHILLISQUAQUE TWP",
    "JACKSON TWP",
    "JORDAN TWP",
    "LEWIS TWP",
    "LITTLE MAHONOY TWP",
    "LOWER AUGUST TWP",
    "LOWER AUGUSTA TWP",
    "LOWER MAHONOY TWP",
    "MT CARMEL TWP",
    "POINT TWP",
    "RALPHO TWP",
    "ROCKEFELLER TWP",
    "RUSH TWP",
    "SHAMOKIN TWP",
    "TURBOT TWP",
    "UPPER AUGUSTA TWP",
    "UPPER MAHONOY TWP",
    "WASHINGTON TWP",
    "WEST CAMERON TWP",
    "WEST CHILLISQUAQUE TWP",
    "ZERBE TWP",
    
    "HERNDON",
    "HERNDON BORO",
    "BORO OF HERNDON",
    "KULPMONT",
    "KULPMONT BORO",
    "BORO OF KULPMONT",
    "MARION HEIGHTS",
    "MARION HEIGHTS BORO",
    "BORO OF MARION HEIGHTS",
    "MILTON",
    "MILTON BORO",
    "BORO OF MILTON",
    "MT CARMEL",
    "MT CARMEL BORO",
    "BORO OF MT CARMEL",
    "NORTHUMBERLAND",
    "NORTHUMBERLAND BORO",
    "BORO OF NORTHUMBERLAND",
    "RIVERSIDE",
    "RIVERSIDE BORO",
    "BORO OF RIVERSIDE",
    "SNYDERTOWN",
    "SYNDERTOWN BORO",
    "BORO OF SNYDERTOWN",
    "TURBOTVILLE",
    "TURBOTVILLE BORO",
    "BORO OF TURBOTVILLE",
    "WATSONTOWN",
    "WATSONTOWN BORO",
    "BORO OF WATSONTOWN",
    
    "SHAMOKIN",
    "SUNBURY",
    "SUNBURY CITY",

    "ATLAS",
    "DALMATIA",
    "DEWART",
    "DORNSIFE",
    "EDGEWOOD",
    "ELYSBURG",
    "FAIRVIEW-FERNDALE",
    "KAPP HEIGHTS",
    "KEISER",
    "MARSHALLTON",
    "MONTANDON",
    "PAXINOS",
    "RANSHAW",
    "STRONG",
    "THARPTOWN",
    "TREVORTON",
    
    // Perry County **
    "PERRY COUNTY",
    
    "GREENWOOD TWP",
    "BUFFALO TWP",
    "GREENWOOD TWP",
    "JUNIATA TWP",
    "HOWE TWP",
    "LIVERPOOL TWP",
    "MILLER TWP",
    "OUVER TWP",
    "TUSCARORA TWP",
    "WATTS TWP",
    
    "LIVERPOOL",
    "LIVERPOOL BORO",
    "BORO OF LIVERPOOL",
    "MILLERSTOWN",
    "MILLERSTOWN BORO",
    "BORO OF MILLERSTOWN",
    "NEW BUFFALO",
    "NEW BUFFALO BORO",
    "BORO OF NEW BUFFALO",
    "NEWPORT",
    "NEWPORT BORO",
    "BORO OF NEWPORT",
    
    // Union County
    "UNION COUNTY",
    
    "BUFFALO TWP",
    "EAST BUFFALO TWP",
    "GREGG TWP",
    "HARTLEY TWP",
    "KELLY TWP",
    "LEWIS TWP",
    "LIMESTONE TWP",
    "UNION TWP",
    "WEST BUFFALO TWP",
    "WHITE DEER TWP",
    
    "HARLETON",
    "HARLETON BORO",
    "HARTLETON BORO",
    "BORO OF HARLETON",
    "LEWISBURG",
    "LEWISBURG BORO",
    "BORO OF LEWISBURG",
    "MIFFLINBURG",
    "MIFFLINBURG BORO",
    "BORO OF MIFFLINBURG",
    "MILTON",
    "MILTON BORO",
    "BORO OF MILTOON",
    "NEW BERLIN",
    "NEW BERLIN BORO",
    "NEWNEW BERLIN BORO",
    "BORO OF NEW BERLIN",
    
    "LINNTOWN",
    
    // Far far away
    "HORSHUM CITY"
 };
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "DECATOR TWP",        "DECATUR TWP",
      "DECATURE TWP",       "DECATUR TWP",
      "HARTLETON",          "HARLETON",
      "HORSHUM CITY",       "HORSHAM",
      "LOWER AUGUST TWP",   "LOWER AUGUSTA TWP",
      "MCALLISTERVILLE",    "MCALISTERVILLE",
      "NEWNEW BERLIN",      "NEW BERLIN",
      "SELINGROVE",         "SELINSGROVE"
  });
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "JC", "JUNIATA COUNTY",
      "MC", "MIFFLIN COUNTY",
      "NC", "NORTHUMBERLAND COUNTY",
      "PC", "PERRY COUNTY",
      "UC", "UNION COUNTY"
  });
}
