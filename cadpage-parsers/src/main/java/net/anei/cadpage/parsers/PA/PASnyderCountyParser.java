package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class PASnyderCountyParser extends DispatchB3Parser {
  
  private static final Pattern CAD_PTN = Pattern.compile(" C[Aa][Dd][ :](?=\\d)");
  private static final Pattern COUNTY_PTN = Pattern.compile("([JMNU]C)\\b\\.? *(.*)");
  private static final Pattern NOT_NAME_PTN = Pattern.compile(".*[ ,][MNJU]C\\b.*");
  
  private boolean good;

  public PASnyderCountyParser() {
    super(CITY_LIST, "SNYDER COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "SNYDER911@snydercounty.org,5705419455";
  }
  
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
    if (data.strCity.equalsIgnoreCase("SELINGROVE")) data.strCity = "SELINSGROVE";
    
    String county = CITY_COUNTY_TABLE.getProperty(data.strCity);
    Matcher match = COUNTY_PTN.matcher(data.strName);
    if (match.matches()) {
      county = convertCodes(match.group(1), COUNTY_CODES);
      data.strName = match.group(2);
    }
    if (county != null) data.strCity = append(data.strCity, ", ", county);
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
    "BEAVERTOWN BORO",
    "FREEBURG BORO",
    "MCCLURE BORO",
    "MIDDLEBURG BORO",
    "SELINGROVE BORO",   // typo
    "SELINSGROVE BORO",
    "SHAMOKIN DAM BORO",
    
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
    "GREENWOOD TWP",
    "MONROE TWP",
    "SUSQUEHANNA TWP",
    
    // Mifflin County
    "DECATUR TWP",
    
    // Northumberland County
    "DELEWARE TWP",
    "LOWER AUGUSTA TWP",
    "NORTHUMBERLAND",
    "NORTHUMBERLAND BORO",
    "SUNBURY",
    "SUNBURY CITY",
    "UPPER AUGUSTA TWP",
    
    // Union County
    "HARTLEY TWP",
    "MIFFLINBURG BORO",
    "LIMESTONE TWP",
    "NEW BERLIN BORO",
    
   
    "PERRY COUNTY"
 };
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "JC", "JUNIATA COUNTY",
      "MC", "MIFFLIN COUNTY",
      "NC", "NORTHUMBERLAND COUNTY",
      "UC", "UNION COUNTY"
  });
  
  private static final Properties CITY_COUNTY_TABLE = buildCodeTable(new String[]{
      "GREENWOOD TWP",      "JUNIATA COUNTY",
      "SUSQUEHANNA TWP",    "JUNIATA COUNTY",
      
      "DECATUR TWP",        "MIFFLIN COUNTY",
      
      "DELEWARE TWP",       "NORTHUMBERLAND COUNTY",
      "LOWER AUGUSTA TWP",  "NORTHUMBERLAND COUNTY",
      "NORTHUMBERLAND",     "NORTHUMBERLAND COUNTY",
      "SUNBURY",            "NORTHUMBERLAND COUNTY",
      "SUNBURY CITY",       "NORTHUMBERLAND COUNTY",
      "UPPER AUGUSTA TWP",  "NORTHUMBERLAND COUNTY",
      
      "MIFFLINBURG",        "UNION COUNTY",
      "HARTLEY TWP",        "UNION COUNTY",
      "LIMESTONE TWP",      "UNION COUNTY",
      "NEW BERLIN",         "UNION COUNTY"
  });
}
