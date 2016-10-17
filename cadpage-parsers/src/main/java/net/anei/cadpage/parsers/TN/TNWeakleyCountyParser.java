package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class TNWeakleyCountyParser extends DispatchEmergitechParser {
  
  public TNWeakleyCountyParser() {
    super(true, CITY_LIST, "WEAKLEY COUNTY", "TN",TrailAddrType.INFO);
    setupProtectedNames("D AND C SUBDIVISION");
    setupSpecialStreets("GREENFIELD HWY 54", "GREEN FIELD HWY 54", "SHARON HWY 89");
  }
  
  private static final Pattern[] RUN_NUMBER_PTNS = new Pattern[]{
    Pattern.compile("^RUN ?# +(\\d+)\\s+"),
    Pattern.compile("^((?:\\d{2}(?: +|\\.))?\\d+)\\s+"),
    Pattern.compile(" LOCATION:(\\d{2}\\.\\d+)\\s+"),
    Pattern.compile(" +((?:\\d{2}\\.)?\\d+)\\s+LOCATION:"),
    Pattern.compile(" LOCATION *((?:\\d{2}\\.)?\\d+) : "),
    Pattern.compile("\\s+((?:\\d{2}\\.)?\\d{3,})$")
  };
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!body.startsWith(":")) return false;
    body = body.substring(1).trim();
    
    // There are several ways they slip an call ID into the alert message that 
    // need to be removed
    body = stripFieldStart(body, ":");
    for (Pattern ptn : RUN_NUMBER_PTNS) {
      Matcher match = ptn.matcher(body);
      if (match.find()) {
        int mst = match.start();
        int mend = match.end();
        data.strCallId = match.group(1).replaceAll(" +", ".");
        if (mst == 0) {
          body = body.substring(mend).trim();
        } else if (mend == body.length()) {
          body = body.substring(0,mst);
        } else {
          body = body.substring(0,mst) + " LOCATION: " + body.substring(mend).trim();
        }
        break;
      }
    }
    
    body = body.replace('\n', ' ');
    if (!super.parseMsg(body, data)) return false;
    
    // They put all kinds of odd things in the apt field
    if (data.strApt.equals("MAIN NUMBER")) {
      data.strApt = "";
    } else {
      data.strApt = stripFieldStart(data.strApt, "APT ");
      data.strApt = stripFieldStart(data.strApt, "UNIT ");
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = GREENFIELD_HWY_54_PTN.matcher(addr).replaceAll("GREEN FIELD HWY");
    addr = SHARON_HWY_89_PTN.matcher(addr).replaceAll("SHARON HWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern GREENFIELD_HWY_54_PTN = Pattern.compile("\\bGREEN ?FIELD HWY 54\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SHARON_HWY_89_PTN = Pattern.compile("\\bSHARON HWY 89\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("UTMARTIN")) return "MARTIN";
    return city;
  }

  private static String[] CITY_LIST = new String[]{
    "DRESDEN",
    "DUKEDOM",
    "GARDNER",
    "GLEASON",
    "GREENFIELD",
    "LATHAM",
    "MARTIN",
    "MCKENZIE",
    "PALMERSVILLE",
    "SHARON",
    "UTMARTIN",
    
    // Henry County
    "COTTAGE GROVE",
    
    // Madison County
    "JACKSON"
  };

}
