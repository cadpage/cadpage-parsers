package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDQueenAnnesCountyAParser extends SmartAddressParser {

  public MDQueenAnnesCountyAParser() {
    super("QUEEN ANNES COUNTY", "MD");
    setFieldList("BOX CALL CITY ADDR APT PLACE INFO CH UNIT");
    setupCallList(MDQueenAnnesCountyBParser.CALL_LIST);
    addNauticalTerms();
    removeWords("NEW");
    setupMultiWordStreets(MDQueenAnnesCountyBParser.MWORD_STREET_LIST);
    setupProtectedNames("4-H PARK");
  }

  @Override
  public String getFilter() {
    return "qac911@qac.org,QA911com@qac.org,@c-msg.net,qac911@gmail.com";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("BROADNECK", "BROAD NECK");
    return super.adjustMapAddress(addr);
  }

  private static final Pattern PREFIX_PTN = Pattern.compile("^(?:qac911|QA911com):");
  private static final Pattern MARKER = Pattern.compile("^(?:(?:qac911|QA911com):\\*)?[DG] ");
  private static final Pattern UNIT_PTN = Pattern.compile(" +([A-Z]{1,2}\\d{2})$");
  private static final Pattern BOX_PTN = Pattern.compile("(\\d{1,2}-\\d{1,2}|AACO|CARO|KENT|TALB|OTHE)-? (.*?)((?: +(?:COMMERCIAL|MEDICAL|STILL|WATER RESCUE|RESCUE))?(?: +LOCAL)?)(?: +BOX)?(?: \\1)?(?: (Q\\d{2}))?");
  private static final Pattern PAREN_PTN = Pattern.compile(" *\\((.*?)\\) *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Eliminate MDQueenAnnesCountyC alerts
    if (body.contains(" | ")) return false;

    // See if part of message has been split into subject
    if (subject.startsWith("*") && subject.endsWith("DUE")) {
      Matcher match = PREFIX_PTN.matcher(body);
      int pt = 0;
      if (match.lookingAt()) pt = match.end();
      body = body.substring(0,pt) +  subject + ':' + body.substring(pt);
    }

    Matcher match = MARKER.matcher(body);
    if (match.find()) body = body.substring(match.end());

    // Strip UNIT from end of text
    int pt = body.lastIndexOf("DUE:");
    if (pt >= 0) {
      data.strUnit = body.substring(pt+4).trim();
      body = body.substring(0,pt).trim();
    } else {
      match = UNIT_PTN.matcher(body);
      if (match.find()) {
        data.strUnit = match.group(1);
        body = body.substring(0, match.start()).trim();
      }
    }

    // Strip box number from front of message and box description from end
    match = BOX_PTN.matcher(body);
    if (match.matches()) {
      data.strBox = append(match.group(3).trim(), " ", match.group(1));
      body = match.group(2).trim();
      data.strChannel = getOptGroup(match.group(4));
    }

    // Sometimes they put valid street names in parenthesis, which messes up
    // the address detection logic.
    match = PAREN_PTN.matcher(body);
    if (match.find() && isValidAddress(match.group(1))) {
      body = body.substring(0,match.start()) + ' ' + match.group(1) + ' ' + body.substring(match.end());
      body = body.trim();
    }

    // For several reasons,the FLAG_AT_BOTH logic doesn't quite work, so we
    // will have to check for an @ and do the had work ourselves
    pt = body.indexOf('@');
    if (pt < 0) {

      // No @ -  If there is a TRANSFER/COVER tag, it marks the end of the address
      pt = body.indexOf("TRANSFER/COVER");
      if (pt >= 0) {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, body.substring(0,pt).trim(), data);
        data.strSupp = body.substring(pt).trim();
      } else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT , body, data);
        data.strSupp = getLeft();
      }
    }

    else {

      // There is an @ - Split message body by the first @ and try to parser
      // both sides as an address
      String part1 = body.substring(0,pt).trim();
      String part2 = body.substring(pt+1).trim();
      Result res1 = parseAddress(StartType.START_CALL, FLAG_CHECK_STATUS | FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_OPT_STREET_SFX | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, part1);
      Result res2 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_IGNORE_AT | FLAG_OPT_STREET_SFX | FLAG_NO_IMPLIED_APT, part2);
      if (res2.getStatus() > res1.getStatus()) {

        // Second part is the address
        // Split first part into call and place name
        res2.getData(data);
        data.strSupp = res2.getLeft();
        String call = MDQueenAnnesCountyBParser.CALL_LIST.getCode(part1, true);
        if (call == null) {
          data.strCall = part1;
        } else {
          data.strCall = call;
          data.strPlace = part1.substring(call.length()).trim();
        }
      }

      else {

        // First part is address
        // Entire second part is place name
        res1.getData(data);
        data.strPlace = part2;
      }
    }

    MDQueenAnnesCountyBParser.fixMutualAidCalls(data);

    // If not address was found, move place to address
    if (data.strAddress.length() == 0) {
      String addr = data.strPlace;
      data.strPlace = "";
      parseAddress(addr, data);
    }

    // Box is required, unless this was a mutual aid call
    return data.strBox.length() > 0 || data.strCall.startsWith("MUTUAL AID");
  }
}
