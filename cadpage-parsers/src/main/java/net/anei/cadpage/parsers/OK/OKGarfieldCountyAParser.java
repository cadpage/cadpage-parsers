package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class OKGarfieldCountyAParser extends SmartAddressParser {

  public OKGarfieldCountyAParser() {
    super("GARFIELD COUNTY", "OK");
    setFieldList("CALL ADDR APT PLACE INFO");
//    setupSpecialStreets("SKELETON", "SOUTHGATE");
  }

  @Override
  public String getFilter() {
    return "911firedispatch@enid.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("gc911|Message From 911|911 ?-.*");
  private static final Pattern LEAD_PTN = Pattern.compile("9-?1-?1 TO |911[- >]*");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    Matcher match = LEAD_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()).trim();
    body = stripFieldEnd(body, " x:gc 911");
    body = body.replace('-', ' ');
    parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT,  body, data);
    if (data.strCall.isEmpty()) {
      data.strCall = getLeft();
    } else {
      data.strSupp = getLeft();
    }
    return true;
  }

  @Override
  public String adjustMapAddress(String address) {
    return HWY412_PTN.matcher(address).replaceAll("US 412");
  }
  private static final Pattern HWY412_PTN = Pattern.compile("\\bHWY *412\\b", Pattern.CASE_INSENSITIVE);
}
