package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARWashingtonCountyBParser extends MsgParser {

  public ARWashingtonCountyBParser() {
    this("WASHINGTON COUNTY", "AR");
  }

  protected ARWashingtonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("ID PRI CALL ADDR APT CH TIME GPS PLACE CITY UNIT INFO");
  }

  @Override
  public String getAliasCode() {
    return "ARWashingtonCountyB";
  }

  @Override
  public String getFilter() {
    return "tritech@centralems.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d+\\) \\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d-\\[\\d+\\] *");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Inform CAD Paging")) return false;

    FParser fp = new FParser(body);

    data.strCallId = fp.getOptional("Call Information:", 0, 20, 21);
    if (data.strCallId == null) return false;

    if (!fp.checkBlanks(3)) return false;
    fp.checkBlanks(1);
    data.strPriority = fp.get(17);

    if (!fp.check("response reference ")) return false;
    data.strCall = fp.get(33);

    if (!fp.check(" at  ")) return false;
    fp.checkBlanks(1);
    parseAddress(fp.get(60), data);
    if (!fp.checkBlanks(140)) return false;

    if (fp.check(" Apt.")) {
      data.strApt = append(data.strApt, "-", fp.get(9));
    }

    if (!fp.check(" Ops Channel") && !fp.check("  Ops Channel")) return false;
    data.strChannel = fp.get(30);

    if (!fp.check("Time") && !fp.check(" Time ")) return false;
    data.strTime = fp.get(5);
    if (!TIME_PTN.matcher(data.strTime).matches()) return false;

    if (!fp.check("Lat/Long:")) return false;
    fp.checkBlanks(2);
    String gps1 = fp.get(8);
    if (!fp.checkBlanks(2)) return false;
    String gps2 = fp.get(8);
    if (!fp.checkBlanks(2)) return false;
    setGPSLoc(addDec(gps1)+','+addDec(gps2), data);

    if (!fp.check("Location Name:")) return false;
    data.strPlace = fp.get(40);
    if (!fp.checkBlanks(360)) return false;

    if (!fp.check("City:")) return false;
    data.strCity = fp.get(35);

    if (!fp.check("Units:")) {
      data.strUnit = fp.get();
    } else {
      data.strUnit = fp.get(30);

      if (!fp.check("Comments:")) return false;
      String info = fp.get();
      Matcher match = INFO_HDR_PTN.matcher(info);
      if(match.lookingAt()) info = info.substring(match.end());
      data.strSupp = MSPACE_PTN.matcher(info).replaceAll("\n");
    }

    return true;
  }

  private static String addDec(String fld) {
    int pt = fld.length()-6;
    if (pt >= 0) fld = fld.substring(0,pt)+'.'+fld.substring(pt);
    return fld;
  }

  private static final Pattern WC_NN_PTN = Pattern.compile("\\bWC \\d+\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    return WC_NN_PTN.matcher(addr).replaceAll("").trim();
  }
}
