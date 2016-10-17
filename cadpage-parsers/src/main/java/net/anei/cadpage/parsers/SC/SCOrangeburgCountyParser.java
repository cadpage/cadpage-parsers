package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;


public class SCOrangeburgCountyParser extends SmartAddressParser {

  private static final Pattern GRID_SLASH_PTN = Pattern.compile("\\bGRID (?:ON )?(\\d{1,2})/ ?([A-Z]\\d{1,2})\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern GRID_PTN = Pattern.compile("\\bGRIDS?[ :]+(?:ON )?(\\d{1,2}[ -][A-Z]\\d{1,2})\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CROSS_PTN = Pattern.compile("[/| ]*\\bCR?OSS\\b:?(?: OF\\b)? *", Pattern.CASE_INSENSITIVE);
  private static final Pattern AND_NEAR_PTN = Pattern.compile(" *(?: AND|[/&]) *NEAR +", Pattern.CASE_INSENSITIVE);
  private static final Pattern AT_MARK_PTN = Pattern.compile(" @ | AT ", Pattern.CASE_INSENSITIVE);
  private static final Pattern LAN_PTN = Pattern.compile("\\bLAN\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern INTERSECT_PTN = Pattern.compile(" *(?:THE )?INTERSECTION (?:OF ) *", Pattern.CASE_INSENSITIVE);
 
  public SCOrangeburgCountyParser() {
    super("ORANGEBURG COUNTY", "SC");
    setupMultiWordStreets(MWORD_STREET_LIST);
    addInvalidWords("FROM", "TO");
  }
  
  @Override
  public String getFilter() {
    return "obcdispatch@orangeburgcounty.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (body.startsWith(":oburgeoc:")) {
      setFieldList("INFO MAP");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.substring(10).trim();
      return true;
    }
    setFieldList("CALL ADDR APT CITY X MAP PLACE INFO");
    
    if (!body.startsWith(":obcdispatch:")) return false;
    body = body.substring(13).trim();

    body = GRID_SLASH_PTN.matcher(body).replaceAll("GRID $1-$2");
    Matcher match = GRID_PTN.matcher(body);
    if (match.find()) {
      data.strMap = match.group(1).replace(' ', '-').toUpperCase();
      body = append(body.substring(0,match.start()).trim(), " ", body.substring(match.end()).trim());
    }
    
    body = stripFieldEnd(body, "/");
    body = stripFieldEnd(body, "|");
    
    String origBody = body;
    
    // If the first part has an @ or AT marker, that marks the beginning of the address
    int status = 0;
    StartType st = StartType.START_CALL;
    boolean firstAt = true;
    while (true) {
      match = AT_MARK_PTN.matcher(body);
      if (firstAt) {
        firstAt = false;
        if (match.find()) {
          data.strCall = append(data.strCall, "/", body.substring(0,match.start()).trim());
          body = body.substring(match.end()).trim();
          st = StartType.START_ADDR;
        }
      } else {
        if (!match.find()) break;
        data.strPlace = append(data.strPlace, " - ", body.substring(0,match.start()).trim());
        body = body.substring(match.end()).trim();
      }
      String work = body;
      work = work.replace(",", "");
      work = CROSS_PTN.matcher(work).replaceAll(" XS: ");
      work = AND_NEAR_PTN.matcher(work).replaceAll(" / ");
      work = LAN_PTN.matcher(work).replaceAll("LN");
      work = INTERSECT_PTN.matcher(work).replaceAll(" ").trim();

      String left = "";
      int flags = 0;
      int pt = work.indexOf(" FOR ");
      if (pt >= 0) {
        left = work.substring(pt+1);
        work = work.substring(0,pt).trim();
        flags |= FLAG_ANCHOR_END;
        if (st == StartType.START_ADDR) flags |= FLAG_CHECK_STATUS;
      }
      Result res = parseAddress(st, flags, work);
      status = res.getStatus();
      if (status > STATUS_MARGINAL) {
        String call = data.strCall;
        data.strCall = "";
        res.getData(data);
        data.strCall = append(call, "/", data.strCall);
        left = append(res.getLeft(), " ", left);
        
        data.strAddress = stripFieldEnd(data.strAddress, " IN");
        
        left = stripFieldStart(left, "/");
        left = stripFieldStart(left, "|");
        if (data.strCall.length() == 0) {
          data.strCall = left;
        } else {
          data.strSupp = left;
        }
        return true;
      }
    }
    
    setFieldList("INFO MAP");
    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = origBody;
    data.strCall = "";
    data.strPlace = "";
    data.strAddress = "";
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ASH FILL",
    "ASH HILL",
    "BEACON TREE",
    "BECKY LOU",
    "BILL SALLEY",
    "BINNICKER BRIDGE",
    "BOGGY BRANCH",
    "BOYLESTON POND",
    "BREEZE HAVEN",
    "BROKEN ARROW",
    "CALVARY CHURCH",
    "COUNTY LINE",
    "CRAGGY BLUFF",
    "FERGUSON LANDING",
    "FIELD TWIN",
    "FIRE LANDING",
    "FOUNTAIN LAKE",
    "GABBY HALL",
    "GOSPEL HILL",
    "GREAT BRANCH",
    "HICKORY HILL",
    "IRICKS POND",
    "JAMESON FARM",
    "JOHN NUNN",
    "LAKE EDISTO",
    "MARTHA HEIGHTS",
    "MIDDLE WILLOW",
    "MILKY WAY",
    "NINETY SIX",
    "NUMBER SIX",
    "OLD NUMBER SIX",
    "PLUM TREE",
    "RAMBLING BRIDGE",
    "REDMOND MILL",
    "RIVER TURN",
    "RIVERS TURN",
    "SHILLINGS BRIDGE",
    "SHORT CUT",
    "SLAB LANDING",
    "ST MATTHEWS",
    "STEEPLE CHASE",
    "TERRY COURT",
    "TODD CREEK",
    "TUG TOWN",
    "TWIN LAKES",
    "WATER TANK"
  };
}
