package net.anei.cadpage.parsers.WV;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

/**
 * Morgan County, WV
 */
public class WVMorganCountyAParser extends DispatchB3Parser {
  
  private static final Pattern MARKER = Pattern.compile("^\\d+:911MORGANCOUNTYWV@GMAIL.COM:");

  public WVMorganCountyAParser() {
    super(MARKER, CITY_CODES, "MORGAN COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "911MORGANCOUNTYWV@GMAIL.COM";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // A subject is required.  But there have been cases where the
    // subject contained a unmatched left parent that messes up the
    // persparser paren counting.  We will try to fix that.
    if (subject.length() == 0) {
      if (!body.startsWith("(")) return false;
      int pt = body.indexOf(')');
      if (pt < 0) return false;
      subject = body.substring(1,pt).trim();
      body = body.substring(pt+1).trim();
      if (subject.length() == 0) return false;
    }
    body = body.replace('\n', ' ');
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.length() == 0 && data.strCall.startsWith("MUTUAL AID TO ")) {
      data.strCity = data.strCall.substring(14);
    }
    return true;
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{

    // Cities and Towns
    "BATH ",                    "BATH ",
    "BERKELEY",                 "BERKELEY SPRINGS",
    "PAW PAW",                  "PAW PAW",
   
    // Unincorporated communities
    "BERRYVILL",                "BERRYVILLE",
    "BURNT FAC",                "BURNT FACTORY",
    "CAMPBELLS",                "CAMPBELLS",
    "CHERRY RU" ,               "CHERRY RUN",
    "DOE GULLY",                "DOE GULLY",
    "DUCKWALL",                 "DUCKWALL",
    "GREAT CAC",                "GREAT CACAPON", 
    "GREEN RID",                "GREEN RIDGE",
    "GREENWOOD",                "GREENWOOD",
    "HANCOCK",                  "HANCOCK",
    "HANSROTE",                 "HANSROTE",
    "HOLTON",                   "HOLTON",
    "JEROME",                   "JEROME",
    "JIMTOWN",                  "JIMTOWN",
    "JOHNSONS",                 "JOHNSONS MILL",
    "LARGENT",                  "LARGENT",
    "LINEBURG",                 "LINEBURG",
    "MAGNOLIA",                 "MAGNOLIA",
    "MOUNT TRI",                "MOUNT TRIMBLE",
    "NEW HOPE",                 "NEW HOPE",
    "NORTH BER",                "NORTH BERKELEY",
    "OAKLAND",                  "OAKLAND",
    "OMPS",                     "OMPS",
    "ORLEANS C",                "ORLEANS CROSS ROADS",
    "REDROCK C",                "REDROCK CROSSING",
    "RIDERSVIL",                "RIDERSVILLE",
    "RIDGE",                    "RIDGE",
    "ROCK GAP",                 "ROCK GAP",
    "SIR JOHNS",                "SIR JOHNS RUN",
    "SLEEPY CR",                "SLEEPY CREEK",
    "SMITH CRO",                "SMITH CROSSROADS",
    "SPOHRS CR",                "SPOHRS CROSSROADS",
    "STOTLERS",                 "STOTLERS CROSSROADS",
    "UNGER",                    "UNGER",
    "WOODMONT",                 "WOODMONT",
    "WOODROW",                  "WOODROW",
    
    // Berkeley County
    "HEDGESVIL",                "HEDGESVILLE"
  });
}
