package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GADecaturCountyAParser extends SmartAddressParser {

  public GADecaturCountyAParser() {
      super("DECATUR COUNTY", "GA");
      setFieldList("CALL ADDR APT INFO");
      setupMultiWordStreets(
          "BOOSTER CLUB",
          "COOL SPRINGS"
      );
  }
  
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<![ \\*])(?=\\*)");
  private static final Pattern DISPATCHED_TO_PTN = Pattern.compile("(.*?) dispatched to (.*)");
  private static final Pattern JUNK_PTN = Pattern.compile("^(?:[- \\*\\.]+|AT +)", Pattern.CASE_INSENSITIVE);
  
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("DG911\n")) return false;
    body = body.substring(6).trim();
    int pt = body.indexOf("\n>---");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // Check on how  many newlines we have
    // if exactly two, first part is the call description
    // if more than two, reject
    
    String[] parts = body.split("\n");
    if (parts.length > 2) return false;
    if (parts.length == 2) {
      data.strCall = parts[0].trim();
      body = parts[1].trim();
    }
    
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    
    // If we have a call description already, see if it is duplicated in the rest of the message
    // either way, we won't be looking for it 
    StartType st = StartType.START_CALL;
    if (data.strCall.length() > 0) {
      st = StartType.START_ADDR;
      if (body.toUpperCase().startsWith(data.strCall.toUpperCase())) {
        body = body.substring(data.strCall.length()).trim();
        body = JUNK_PTN.matcher(body).replaceFirst("");
      }
      
      //  Occasionally both the call and address in the first part,
      //  in either order :(
      //  Now they have done twice in a different order :( :(
      else {
        pt = data.strCall.lastIndexOf(" - ");
        if (pt >= 0) {
          String part1 = data.strCall.substring(0,pt).trim();
          String part2 = data.strCall.substring(pt+3).trim();
          if (body.startsWith(part1)) {
            body = body.substring(part1.length()).trim();
            body = JUNK_PTN.matcher(body).replaceFirst("");
          }
          if (body.startsWith(part2)) {
            body = body.substring(part2.length()).trim();
            body = JUNK_PTN.matcher(body).replaceFirst("");
          }
          if (isValidAddress(part1)) {
            parseAddress(part1, data);
            data.strCall = part2;
            data.strSupp = body;
            return true;
          } else if (isValidAddress(part2)) {
           parseAddress(part2, data);
           data.strCall = part1;
           data.strSupp = body;
           return true;
          }
        }
      }
      
      // And sometimes the first line contains the address :(
      if (isValidAddress(data.strCall)) {
        String addr = data.strCall;
        data.strCall = "";
        parseAddress(addr, data);
        boolean first = true;
        for (String part : body.split(" - ")) {
          part = part.trim();
          if (part.equals(addr)) continue;
          if (first) {
            first = false;
            if (part.length() <= 40) {
              data.strCall = part;
            } else {
              data.strSupp = part;
            }
          }
          else {
            data.strSupp = append(data.strSupp, " - ", part);
          }
        }
        return true;
      }
    }
    
    Matcher match = DISPATCHED_TO_PTN.matcher(body);
    if (match.matches()) {
      data.strSupp = match.group(1).trim() + " Dispatched";
      body = match.group(2).trim();
    }
    
    pt = body.indexOf(" - ");
    if (pt >= 0) {
      parseAddress(body.substring(0,pt).trim(), data);
      String left = body.substring(pt+3).trim();
      if (data.strCall.length() > 0) {
        data.strSupp = append(data.strSupp, "\n", left);
      } else if (left.length() < 40) {
        data.strCall = left;
      } else {
        data.strSupp = append(data.strSupp, "\n", left);
      }
      return true;
    }
    Result res = parseAddress(st, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, body);
    if (res.isValid()) {
      res.getData(data);
      String left = res.getLeft();
      left = JUNK_PTN.matcher(left).replaceFirst("");
      if (data.strCall.length() == 0) {
        data.strCall = left;
      } else {
        data.strSupp = append(data.strSupp, "\n", left);
      }
    } else {
      if (data.strCall.length() > 0) {
        data.strSupp = append(data.strSupp, "\n", body);
      } else {
        data.msgType = MsgType.GEN_ALERT;
        data.strSupp = append(data.strSupp, "\n", body);
      }
    }
    return true;
  }
}
