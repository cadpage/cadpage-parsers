package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class WICalumetCountyCParser extends DispatchA52Parser {
  
  private static final Pattern ID_INFO_PTN = Pattern.compile("([A-Z]\\d{9})\\b[- ]*(.*)");

  public WICalumetCountyCParser() {
    this("CALUMET COUNTY");
  }
  
  @Override
  public String getAliasCode() {
    return "WICalumetCountyC";
  }

  WICalumetCountyCParser(String county) {
    super(county, "WI");
  }
  
  @Override
  public String getFilter() {
    return "Hiplink@co.calumet.wi.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = ID_INFO_PTN.matcher(data.strSupp);
    if (match.matches()) {
      if (data.strCallId.length() == 0) data.strCallId = match.group(1);
      data.strSupp = match.group(2);
    }
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = HOUSE_NBR_PTN.matcher(addr).replaceAll("$1 $2");
    addr = CTY_TK_PTN.matcher(addr).replaceAll("COUNTY ROAD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern HOUSE_NBR_PTN = Pattern.compile("^([NWSE])(\\d+)\\b");
  private static final Pattern CTY_TK_PTN = Pattern.compile("\\BCTY TK\\b");
}
