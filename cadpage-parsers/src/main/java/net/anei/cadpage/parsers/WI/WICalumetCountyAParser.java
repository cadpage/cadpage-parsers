package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;



public class WICalumetCountyAParser extends DispatchPrintrakParser {

  private static final Pattern HOUSE_NBR_PTN = Pattern.compile("^([NWSE])(\\d+)\\b");

  public WICalumetCountyAParser() {
    this("CALUMET COUNTY");
  }

  public WICalumetCountyAParser(String county) {
    super(CITY_TABLE, county, "WI");
  }

  @Override
  public String getAliasCode() {
    return "WICalumetCountyA";
  }

  @Override
  public String getFilter() {
    return "Admin.Foxcomm@co.calumet.wi.us,finstad_rr@co.brown.wi.us,@browncountywi.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n<div");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\n******");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (!super.parseMsg(body, data)) return false;

    // There are two address adjustments that need to be made.
    // First need to insert a space into a N3345 type address
    data.strAddress = HOUSE_NBR_PTN.matcher(data.strAddress).replaceFirst("$1 $2");

    // Next we have to replace "CTY TK" with "COUNTY RD"
    data.strAddress = data.strAddress.replaceFirst("CTY TK", "COUNTY RD");

    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = CTH_PTN.matcher(addr).replaceAll("COUNTY ROAD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern CTH_PTN = Pattern.compile("\\bCTH\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      // Brown County
      "ASHW", "ASHWAUBENON",
      "CGB",  "GREEN BAY",
      "TGB",  "GREEN BAY",
      "DENM", "DENMARK",
      "NEWD", "NEW DENMARK",
      "VWRI", "WRIGHTSTOWN",
      "TWRI", "WRIGHTSTOWN",
      "ROCK", "ROCKLAND",
      "DEPE", "DEPERE",
      "PULA", "PULASKI",
      "LEDG", "LEDGEVIEW",
      "HOWA", "HOWARD",
      "HOBA", "HOBART",
      "SCOT", "SCOTT",
      "EATO", "EATON",
      "PITT", "PITTSVILLE",
      "BELL", "BELLEVEUE",
      "HUMB", "HUMBOLDT",
      "ALLO", "ALLOUEZ",
      "SUAM", "SUAMICO",
      "LAWR", "LAWRENCE",
      "GLEN", "GLENMORE",
      "HOLL", "HOLLAND",
      "MORR", "MORRISON",

      // Calumet County
      "CMBT", "BROTHERTON",
      "CMCB", "BRILLION",
      "CMCC", "CHILTON",
      "CMCK", "KIEL",
      "CMCN", "NEW HOLSTEIN",
      "CMCT", "CHARLESTOWN",
      "CMHV", "HARRISON",
      "CMTB", "BRILLION",
      "CMSV", "SHERWOOD",
      "CMTH", "HARRISON",
      "CMTN", "NEW HOLSTEIN",
      "CMTR", "RANTOUL",
      "CMTS", "STOCKBRIDGE",
      "CMTW", "WOODVILLE",
      "CMVH", "HILBERT",
      "CMVP", "POTTER",
      "CMVS", "STOCKBRIDGE",

      // Outagamie County
      "APPL", "APPLETON",
      "KAUC", "KAUKAUNA",
      "SEYC", "SEYMOUR",
      "NEWL", "NEW LONDON",
      "BLCT", "BLACK CREEK",
      "BUCT", "BUCHANAN",
      "CENT", "CENTER",
      "COLV", "COMBINED LOCKS",
      "DALT", "DALE",
      "DRCT", "DEER CREEK",
      "ELLT", "ELLINGTON",
      "FRET", "FREEDOM",
      "GRCT", "GRAND CHUTE",
      "GRVT", "GREENVILLE",
      "HORT", "HORTONVILLE",
      "HORV", "HORTONVILLE",
      "KAUT", "KAUKAUNA",
      "KIMV", "KIMBERLY",
      "LIBT", "LIBERTY",
      "LTCV", "LITTLE CHUTE",
      "MPCT", "MAPLE CREEK",
      "ONET", "ONEIDA",
      "OSBT", "OSBORN",
      "SEYT", "SEYMOUR",
      "VANT", "VANDENBROEK",

      // Winnebago County
      "WALG", "ALOGMA",
      "WBLA", "BLACK WOLF",
      "WCLA", "CLAYTON",
      "WCME", "MENASHA",
      "WMEN", "MENASHA",
      "WNEE", "NEENAH",
      "WNEK", "NEKIMI",
      "WNEP", "NEPEUSKUN",
      "WOMR", "OMRO",
      "WOSH", "OSHKOSH",
      "WPOY", "POYGAN",
      "WRUS", "RUSHFORD",
      "WTME", "MENASHA",
      "WTNE", "NEENAH",
      "WTOM", "OMRO",
      "WTOS", "OSHKOSH",
      "WTWN", "WINNECONNE",
      "WUTI", "UTICA",
      "WVIN", "VINLAND",
      "WWCR", "WINCHESTER",
      "WWNE", "WINNECONNE",
      "WWOL", "WOLF RIVER"
  });
}
