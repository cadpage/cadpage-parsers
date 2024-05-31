package net.anei.cadpage.parsers.dispatch;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchRedAlertParser extends SmartAddressParser {


  public DispatchRedAlertParser(String defCity, String defState) {
    this(null, defCity, defState);
  }


  public DispatchRedAlertParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setFieldList("CALL CODE INFO ADDR APT CITY BOX X MAP PLACE TIME");
  }

  @Override
  public String getFilter() {
    return "paging@alpinesoftware.com,@rednmxcad.com,REDALERT";
  }

  private static final Pattern TIME_INFO_MARK = Pattern.compile("\\. ?\\. ?([\\d:]+)(?: +AddInfo: *(.*))?$");
  private static final Pattern END_ADDR_PTN = Pattern.compile(" S:| CROSS:| c/s:");
  private static final Pattern LEAD_INTERSECT_PTN = Pattern.compile(".*(?: AND|[/&])", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_INTERSECT_PTN = Pattern.compile("(?:AND |[/&]).*", Pattern.CASE_INSENSITIVE);
  private static final Pattern CODE_PATTERN = Pattern.compile("\\b\\d{1,2}-?[A-Z]-?\\d{1,2}[A-Z]?\\b");
  private static final Pattern XSTREET_PTN = Pattern.compile("(.*)\\(X-Streets:(.*)\\)");
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("#?\\b([NSEW])/(B)D?\\b");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Strip off leading slash
    body = stripFieldStart(body, "/");

    // Look for the trailing time signature
    // If we find it, strip it off.
    boolean ok = false;
    String extraInfo = "";
    Matcher match = TIME_INFO_MARK.matcher(body);
    if (match.find()) {
      ok = true;
      String sTime = match.group(1);
      if (sTime.length() >= 5) {
        if (sTime.length() < 8) sTime = sTime.substring(0,5);
        data.strTime = sTime;
        extraInfo = getOptGroup(match.group(2));
      }
      body = body.substring(0,match.start()).trim();
    }

    // Call is sometimes in square brackets, which got treated as a subject
    // in which case it needs to be restored
    if (subject.length() > 0) body = subject + " " + body;

    // Also must have at " at " keyword which we will change to "LOC:"
    // If there happen to be more than one, only change the last one
    match = END_ADDR_PTN.matcher(body);
    int pt = match.find() ? match.start() : body.length();
    pt = body.lastIndexOf(" at ", pt);
    if (pt >= 0) {
      body = body.substring(0, pt) + " LOC: " + body.substring(pt+4);
    }

    body = "TYPE:" + body.replace("c/s:", "CROSS:").replace(" c/s ", " CROSS:").replace(" CS:", " CROSS:").replace(" Box ", " Box:").replace(" B:", " O:").replaceAll("\\s+", " ");
    Properties props = parseMessage(body, new String[]{"TYPE","LOC","CROSS","Box", "Map", "O", "- S"});

    String sAddress = props.getProperty("LOC");
    if (sAddress == null) {
      if (!ok) return false;
      sAddress = props.getProperty("TYPE", "");
      sAddress = sAddress.replace("C/O","C%O").replace('@', '&');
      parseAddress(StartType.START_CALL, sAddress, data);
      if (data.strAddress.length() == 0) return false;
      String info = getLeft();
      if (TRAIL_INTERSECT_PTN.matcher(info).matches()) {
        data.strAddress = data.strAddress + ' ' + info;
        info = "";
      }
      data.strCall = data.strCall.replace("C%O", "C/O");
      data.strAddress = data.strAddress.replace("C%O", "C/O");
      info = info.replace("C%O", "C/O");
      data.strSupp = append(data.strSupp, " / ", info);
    } else {
      sAddress = stripFieldStart(sAddress, "intersection of ");
      pt = sAddress.lastIndexOf(",");
      if (pt >= 0) {
        data.strCity = sAddress.substring(pt+1).trim();
        sAddress = sAddress.substring(0,pt).trim();
      }

      sAddress = DIR_BOUND_PTN.matcher(sAddress).replaceAll("$1$2");

      // Check for X-Streets term
      match = XSTREET_PTN.matcher(sAddress);
      if (match.matches()) {
        sAddress = match.group(1).trim();
        data.strCross = match.group(2).trim();
      }

      // Protect C/O sequence form being treated as an intersection
      int flags = FLAG_ANCHOR_END;
      if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
      sAddress = sAddress.replace("C/O", "C%%O").replace('@', '&');
      parseAddress(StartType.START_PLACE, flags, sAddress, data);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      if (LEAD_INTERSECT_PTN.matcher(data.strPlace).matches()) {
        data.strAddress = data.strPlace + ' ' + data.strAddress;
        data.strPlace = "";
      }
      data.strAddress = data.strAddress.replace("%%", "/");

      String sCall = props.getProperty("TYPE", "");
      int ipt = sCall.indexOf(':');
      if (ipt >= 0) {
        String info = sCall.substring(ipt+1).trim();
        sCall = sCall.substring(0,ipt).trim();
        match = CODE_PATTERN.matcher(info);
        if (match.find()) {
          data.strCode = info.substring(match.start(), match.end());
          info = cutOut(info, match.start(), match.end());
        }
        info = stripFieldStart(info, "- ");
        data.strSupp = append(info, " / ", data.strSupp);
      }
      data.strCall = sCall.replaceAll("\\. \\.", "-");
    }

    data.strPlace = append(data.strPlace, " - ", props.getProperty("O", ""));
    data.strBox = props.getProperty("Box", "");
    String sCross = props.getProperty("CROSS");
    if (sCross != null) {
      ok = true;
      sCross = stripFieldStart(sCross, "BET ");
      sCross = stripTrailInfo(sCross, data);
      data.strCross = sCross;
    }

    String sMap = props.getProperty("Map");
    if (sMap != null) {
      sMap = stripTrailInfo(sMap, data);
      data.strMap = sMap;
    }

    data.strSupp = append(data.strSupp, "\n", extraInfo);

    return ok;
  }

  private String stripTrailInfo(String field, Data data) {
    int pt = field.indexOf(',');
    if (pt >= 0) {
      data.strSupp = append(data.strSupp, " / ", field.substring(pt+1).trim());
      field = field.substring(0,pt).trim();
    }
    return field;
  }
}
