package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OHClarkCountyBParser extends SmartAddressParser {
  
  public OHClarkCountyBParser() {
    super(CITY_LIST, "CLARK COUNTY", "OH");
    setFieldList("PLACE ADDR APT CITY X CALL INFO ID");
    removeWords("CL", "UNIT");
  }
  private static final Pattern MASTER = Pattern.compile("-?(.*?)(?:-(\\d{4}))?");
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])/B\\b");
  private static final Pattern LEAD_CALL_PREFIX_PTN = Pattern.compile("(SQUAD ON STAND ?BY)[ /]+");
  private static final Pattern MARK_PTN = Pattern.compile("(?<! W)/ |(?=BETWEEN )| (MEDIC )?FOR A ", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_CLEANER = Pattern.compile("(?: ?[/-] )?(.*?)");
  private static final Pattern DELIMITER_MATCHER = Pattern.compile("(.*?)(?: APT (\\w+) | \\- | / )(.*?)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Way to promiscous
    if (!isPositiveId()) return false;
        
    //remove leading dash and trailing -nnnn
    Matcher mat = MASTER.matcher(body);
    if (mat.matches()) {
      body = mat.group(1).trim();
      data.strCallId = getOptGroup(mat.group(2));
    }
    
    // Remove slashes in dir-bounds
    body = DIR_BOUND_PTN.matcher(body).replaceAll("$1B");
    
    // Check for leading call prefix
    String prefix = "";
    mat = LEAD_CALL_PREFIX_PTN.matcher(body);
    if (mat.lookingAt()) {
      prefix = mat.group(1);
      body = body.substring(mat.end());
    }
    
    // Check for a / or "BETWEEN" marking the end of the address
    Matcher match = MARK_PTN.matcher(body);
    if (match.find()) {
      String callPrefix = match.group(1);
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, body.substring(0,match.start()).trim(), data);
      String call = body.substring(match.end()).trim();
      if (callPrefix != null) call = append(callPrefix.trim(), " - ", call);
      data.strCall = call;
      
      // If terminated by BETWEEN, try to parse a cross street
      if (data.strCall.startsWith("BETWEEN ")) {
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_CHECK_STATUS | FLAG_NO_IMPLIED_APT, data.strCall.substring(8).trim());
        if (res.isValid()) {
          res.getData(data);
          data.strCall = res.getLeft();
        }
      }
    }
    
    // Otherwise, see what the SAP makes of this
    else {
      parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT|FLAG_IGNORE_AT|FLAG_NO_CITY, body, data);
      data.strCall = getLeft();
      
      // If we didn't find a trailing call description, see if we can find a leading call
      if(data.strCall.equals("")) {
        Result res = parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ|FLAG_NO_IMPLIED_APT|FLAG_IGNORE_AT|FLAG_NO_CITY, body);
        if (res.isValid()) {
          data.strAddress = data.strApt = data.strCross = data.strPlace = "";
          res.getData(data);
          data.strSupp = res.getLeft();
        }
      }
      
      //if that failed, look for " - ", " / ", or " APT \\w+", and use it to mark end of addr field
      if(data.strCall.equals("")) {
        Matcher delMat = DELIMITER_MATCHER.matcher(body);
        if(delMat.matches()) {
          data.strAddress = data.strApt = data.strCross = data.strPlace = "";
          data.strAddress = delMat.group(1);
          if (delMat.group(2) != null) data.strApt = delMat.group(2); //if delimiter was apt then give its value to strApt
          data.strCall = delMat.group(3);
        }
      
        //last resort, now try parseaddress with FLAG_OPT_ST_SFX
        else {
          data.strAddress = data.strApt = data.strCross = data.strPlace = "";
          parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT|FLAG_OPT_STREET_SFX|FLAG_IGNORE_AT|FLAG_NO_CITY, body, data);
          data.strCall = getLeft();
        }
      }
      
      // See if the call description starts with a city
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, data.strCall, data);
      data.strCall = getLeft();
    }
    
    // However we got here, an number cross street should be reattached to the call description
    if (NUMERIC.matcher(data.strCross).matches()) {
      data.strCall = append(data.strCross, " ", data.strCall);
      data.strCross = "";
    }
    
    // Append call prefix
    data.strCall = append(prefix, " - ", data.strCall);
    
    //if call is still empty, make it a general report
    if(data.strCall.equals("")) {
      data.strAddress = data.strApt = data.strCross = data.strPlace = "";
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
    } else {
      //remove leading " / " or " - " from call (which only occurs as a result of parseaddress, so luckily it only needs to be here.)
      Matcher callMat = CALL_CLEANER.matcher(data.strCall);
      if(callMat.matches()) data.strCall = callMat.group(1);
    }
    
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "NEW CARLISLE",
    "SPRINGFIELD",

    // Villages
    "CATAWBA",
    "CLIFTON",
    "DONNELSVILLE",
    "ENON",
    "NORTH HAMPTON",
    "SOUTH CHARLESTON",
    "SOUTH VIENNA",
    "TREMONT CITY",

    // Townships
    "BETHEL TWP",
    "GERMAN TWP",
    "GREEN TWP",
    "HARMONY TWP",
    "MAD RIVER TWP",
    "MADISON TWP",
    "MOOREFIELD TWP",
    "PIKE TWP",
    "PLEASANT TWP",
    "SPRINGFIELD TWP",

    // Census-designated places
    "CRYSTAL LAKES",
    "GREEN MEADOWS",
    "HOLIDAY VALLEY",
    "NORTHRIDGE",
    "PARK LAYNE"
  };

}
