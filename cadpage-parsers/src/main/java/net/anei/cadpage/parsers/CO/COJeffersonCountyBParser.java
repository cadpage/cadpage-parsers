package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Jefferson County, CO
 */
public class COJeffersonCountyBParser extends SmartAddressParser {
  
  private static final Pattern SRC_PREFIX_PTN = Pattern.compile("(?:PV|EC)(?:FD)?[- /]+(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern MM_PTN = Pattern.compile("(MM \\d+) +(.*)");
  private static final Pattern DELIM_PTN = Pattern.compile(" +- +| *//+ *");

  public COJeffersonCountyBParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "CO");
    setFieldList("PLACE ADDR APT X CITY CALL INFO");
    setupMultiWordStreets(
        "BLUE CREEK",
        "DENVER WEST CO MILLS",
        "DEN WEST COLO MILLS",
        "ELK CREEK",
        "JEFFERSON CO",
        "MT VERNON",
        "OEHLMANN PARK",
        "WISP CREEK"
    );
  }

  @Override
  public String getFilter() {
    return "@jeffco.us,jnicholaou@pleasantviewfire.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = DEN_WEST_CO_MILLS_PTN.matcher(addr).replaceAll("DENVER WEST COLORADO MILLS");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern DEN_WEST_CO_MILLS_PTN = Pattern.compile("(?:DEN|DENVER) (?:W|WEST) (?:CO|COLO|COLORADO) MILLS", Pattern.CASE_INSENSITIVE);
  
  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Reject anything that looks like it is for COJefersonCountyA
    
    if (body.startsWith("Add:") || body.startsWith("Alarm #")) return false;
    
    // Reject subject that looks like forwarded message header
    if (subject.startsWith("Forwarded")) subject = "";
    
    // Strip off leading source prefix
    Matcher match = SRC_PREFIX_PTN.matcher(body);
    if (match.matches()) body = match.group(1);
    
    body = stripFieldEnd(body, "//");

    // So many different page structures
    // Let start with the ones that have a subject
    if (subject.length() > 0) {
      
      // Is the subject a valid address
      subject = subject.replace("//", "/");
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, subject);
      if (res.isValid()) {
        
        // OK, that is the address, split the message body between a
        // call description and supp info
        res.getData(data);
        parseCallAndInfo(body, data);
        return true;
      }
      
      // Subject is not a valid address.  More later
      // See if we can break address out of text line
      String parts[] = DELIM_PTN.split(body, 2);
      res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, parts[0]);
      if (!res.isValid() && !isPositiveId()) return false;
      data.strCall = subject;
      res.getData(data);
      if (parts.length > 1) data.strSupp = parts[1];
      return true;
    }
    
    // No subject
    
    // See if we can break this up into different parts
    String parts[] = DELIM_PTN.split(body, 3);
    if (parts.length > 1) {
      
      // We can.  First two parts will be call and address in either order
      Result res1 = parseAddress(StartType.START_PLACE, parts[0]);
      Result res2 = parseAddress(StartType.START_ADDR, parts[1]);
      if (res1.getStatus() >= res2.getStatus()) {
        data.strCall = parts[1];
      } else {
        data.strCall = parts[0];
        res1 = res2;
      }
      res1.getData(data);
      
      // Address has to be valid, or we have to have confirmed this dispatch msg
      if (!res1.isValid() && !isPositiveId()) return false;
      
      // If we did not find an address use the place field instead
      if (data.strAddress.length() == 0) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }

      
      // Any remaining data goes into info
      if (parts.length > 2) {
        data.strSupp = parts[2];
      }
      return true;
    }
    
    // No such luck
    // Use the address parser to break things out
    parseAddress(StartType.START_PLACE, FLAG_IGNORE_AT, body, data);
    if (!isValidAddress()) return false;
    String left = getLeft();
    
    // Numeric cross streets get reassiged
    if (NUMERIC.matcher(data.strCross).matches()) {
      left = append(data.strCross, " ", left);
      data.strCross = "";
    }
    
    match = MM_PTN.matcher(left);
    if (match.matches()) {
      data.strAddress = append(data.strAddress, " ", match.group(1));
      left = match.group(2);
    }
    if (left.length() == 0) {
      data.strCall = data.strPlace;
      data.strPlace = "";
      return true;
    }
    parseCallAndInfo(left, data);
    return true;
  }

  /**
   * Split message text between call description and info
   * @param body message text
   * @param data data object
   */
  protected void parseCallAndInfo(String body, Data data) {
    int pt = body.indexOf("//");
    if (pt >= 0) {
      data.strCall = body.substring(0,pt).trim();
      data.strSupp = body.substring(pt+2).trim();
    } else {
      if (body.length() <= 30) {
        data.strCall = body;
      } else {
        data.strSupp = body;
      }
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LAK", "LAKEWOOD"
  });
}
  





