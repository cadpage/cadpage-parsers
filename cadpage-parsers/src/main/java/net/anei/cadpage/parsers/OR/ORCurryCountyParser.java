package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ORCurryCountyParser extends SmartAddressParser {

  public ORCurryCountyParser() {
    super(CITY_LIST, "CURRY COUNTY", "OR");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setFieldList("ADDR PLACE APT CITY CALL");
  }

  @Override
  public String getFilter() {
    return "station1dispatch@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf('/');
    if (pt < 0) return false;
    data.strCall = body.substring(pt+1).trim();
    if (data.strCall.length() == 0) data.strCall = subject;
    body = body.substring(0,pt).trim();

    pt = body.lastIndexOf(',');
    if (pt < 0) return false;
    data.strCity = body.substring(pt+1).trim();
    if (!isCity(data.strCity)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "Curry County";
    body = body.substring(0,pt).trim();

    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, body, data);
    return true;
  }

  private static final Pattern HWY_101_NS_PTN = Pattern.compile("\\b(HWY 101) [NS]\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    return HWY_101_NS_PTN.matcher(addr).replaceAll("$1");
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("MP")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "INDIAN SANDS",                         "+42.156770,-124.361102",
      "MILL BEACH",                           "+42.049202,-124.292419",
      "SPORTHAVEN BEACH",                     "+42.042736,-124.265781"
  });

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BROOKINGS",
    "GOLD BEACH",
    "PORT ORFORD",

    // Census-designated places
    "HARBOR",
    "LANGLOIS",
    "NESIKA BEACH",
    "PISTOL RIVER",

    // Unincorporated communities
    "AGNESS",
    "BAGNELL FERRY",
    "CARPENTERVILLE",
    "DENMARK",
    "HUNTER CREEK",
    "ILLAHE",
    "MARIAL",
    "OPHIR",
    "PLUM TREES",
    "SIXES",
    "WEDDERBURN",

    "UNINCORPORATED"
  };
}
