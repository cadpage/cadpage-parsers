package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNAnokaCountyBParser extends MsgParser {

  public MNAnokaCountyBParser() {
    super("ANOKA COUNTY", "MN");
    setFieldList("ID CODE CALL ADDR APT PLACE CITY NAME INFO MAP GPS");
  }

  @Override
  public String getFilter() {
    return "InformCAD@paging.acw-psds.org,Informcad@acw-psds.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean noParseSubjectFollow() { return true; }
    };
  }

  private static final Pattern TRAIL_MAP_GPS_PTN =
      Pattern.compile("(?:[A-Z].{30}\\d{8}  \\d{8}|(?:Andover|Blaine|Brooklyn Center|Brooklyn Park|Centennial|Champlin|Columbia Heights|Coon Rapids|Dayton|East Bethel|Elk River|Fridley|Forest Lake|Ham Lake|Isanti Fire|Lino Lakes|Linwood|Maple Grove|New Brighton|Nowthen|Oak Grove|Ramsey|SLP/MV|White Bear Lake Fire) \\S+|Anoka|Bethel|Champlin|Forest Lake|Hilltop|Hugo Fire|Lake Johanna Fire|Lexington|Nowthen|Oak Grove Law|St Anthony|St Francis)$");

  private static final Pattern LEAD_ID_PTN = Pattern.compile("[A-Z]{2}[A-Z0-9]\\d{7}");
  private static final Pattern INFO_SKIP_PTN = Pattern.compile("\\[\\d\\]");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile("(.*)(\\d{2}[A-Z]-[A-Z]{2}\\d{4})");
  private static final Pattern ID_PTN = Pattern.compile("\\d{11}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Paging")) return false;

    // Fix the missing blank gap that occurs when a very long alert is split between multiple messages

    if (isMultiMsg()) {
      Matcher match = TRAIL_MAP_GPS_PTN.matcher(body);
      if (match.find()) {
        int pt = body.indexOf("[1]");
        if (pt >= 0) {
          pt += 500;
          String tail = match.group();
          StringBuilder sb = new StringBuilder(body);
          sb.setLength(sb.length()-tail.length());
          while (sb.length() < pt) sb.append(' ');
          sb.append(tail);
          body = sb.toString();
        }
      }
    }

    FParser p = new FParser(body);

    do {
      if (p.check("FIRE/RESCUE PAGE:")) {
        String id = p.lookahead(0, 10);
        if (LEAD_ID_PTN.matcher(id).matches()) {
          data.strCallId = id;
          p.skip(10);
        }
        break;
      }

      if (p.check("Active 911:")) {
        data.strCallId = p.get(10);
        break;
      }

      String id = p.get(11);
      if (ID_PTN.matcher(id).matches() && p.checkBlanks(2)) {
        data.strCallId =  id;
        break;
      }

      return false;
    } while (false);

    if (p.check(" ")) return false;
    String call = p.get(30);
    int pt = call.indexOf('-');
    if (pt >= 0) {
      data.strCode = call.substring(0,pt).trim();
      call = call.substring(pt+1).trim();
    }
    data.strCall = call;

    parseAddress(p.get(40), data);

    String apt = p.get(5);
    if (apt.startsWith("#")) apt = apt.substring(1).trim();
    data.strApt = append(data.strApt, "-", apt);

    data.strPlace = p.get(20);
    data.strCity = p.get(20);

    p.setOptional();

    if (!p.check("[1] ")) {
      if (p.checkAhead(10,  "[1] ")) {
        data.strMap = p.get(10);
        if (data.strMap.equals("NOT FOUND")) data.strMap = "";
      } else {
        data.strName = cleanWirelessCarrier(p.get(80));
      }
      if (!p.check("[1] ")) return false;
    }

    String info = p.get(496);
    data.strMap = append(data.strMap, "/", p.get(30));
    String gps1 = p.get(8);
    if (!p.check("  ")) return false;
    String gps2 = p.get(8);
    setGPSLoc(cvtGps(gps1)+','+cvtGps(gps2), data);

    info = INFO_SKIP_PTN.matcher(info).replaceAll("").trim();
    if (data.strCallId.length() == 0) {
      Matcher match = TRAIL_ID_PTN.matcher(info);
      if (match.matches()) {
        info = match.group(1).trim();
        data.strCallId = append(data.strCallId, "/", match.group(2));
      }
    }
    data.strSupp = info;

    if (data.strMap.length() == 0) data.expectMore = true;
    return true;
  }

  private static String cvtGps(String gps) {
    int pt = gps.length()-6;
    if (pt >= 0) gps = gps.substring(0, pt)+'.'+gps.substring(pt+1);
    return gps;
  }
}
