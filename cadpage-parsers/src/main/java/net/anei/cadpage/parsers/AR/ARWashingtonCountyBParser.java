package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class ARWashingtonCountyBParser extends MsgParser {

  public ARWashingtonCountyBParser() {
    this("WASHINGTON COUNTY", "AR");
  }

  protected ARWashingtonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
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

  private static final Pattern MASTER = Pattern.compile("(?:(\\d{6}-\\d{6}) +(?:SPACE)?)?Call Information: *(\\S+) +response reference (.*?) +at +(.*?) +Ops Channel *(.*?) +Time (\\d\\d:\\d\\d)Lat/Long: *(\\d\\d)(\\d{6}) +(\\d\\d)(\\d{6}) +Location Name: *(.*?) +City: *(.*?) +(.*)");
  private static final Pattern GEN_ALERT_MASTER = Pattern.compile("(\\d{6}-\\d{6}) +,(\\S+) +,(?:True|False) \\d+\\) \\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d-\\[\\d+\\] +(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Inform CAD Paging")) return false;

    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID PRI CALL ADDR APT CH TIME GPS PLACE CITY UNIT");
      data.strCallId = getOptGroup(match.group(1));
      data.strPriority = match.group(2);
      data.strCall = match.group(3);
      parseAddress(match.group(4), data);
      data.strChannel = match.group(5);
      data.strTime = match.group(6);
      setGPSLoc(match.group(7)+'.'+match.group(8)+','+match.group(9)+'.'+match.group(10), data);
      data.strPlace = match.group(11);
      data.strCity = match.group(12);
      data.strUnit = match.group(13);
      return true;
    }

    match = GEN_ALERT_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID PRI INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCallId = match.group(1);
      data.strPriority =  match.group(2);
      data.strSupp = match.group(3);
      return true;
    }
    return false;
  }

  private static final Pattern WC_NN_PTN = Pattern.compile("\\bWC \\d+\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    return WC_NN_PTN.matcher(addr).replaceAll("").trim();
  }
}
