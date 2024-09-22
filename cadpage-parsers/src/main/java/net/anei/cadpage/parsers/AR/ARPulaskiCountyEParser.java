package net.anei.cadpage.parsers.AR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARPulaskiCountyEParser extends DispatchA71Parser {

  public ARPulaskiCountyEParser() {
    super("PULASKI COUNTY", "AR");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " NEW TOWN");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("NLR")) city = "NORTH LITTLE ROCK";
    return city;
  }

  private static final Pattern CTF_PTN = Pattern.compile("\\bCTF\\b", Pattern.CASE_INSENSITIVE);
  @Override
  public String adjustMapAddress(String addr) {
    return CTF_PTN.matcher(addr).replaceAll("CUTOFF RD");
  }
}
