package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class NJAtlanticCountyBParser extends DispatchProphoenixParser {

  public NJAtlanticCountyBParser() {
    super(CITY_CODES, CITY_LIST, "ATLANTIC COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "CADAlert@townshipofhamilton.com,support@Prophoenix.com,CADAlert@townshipofhamilton.com,noreply@gtpd.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.endsWith(" EHT")) {
      data.strAddress = data.strAddress.substring(0, data.strAddress.length()-4).trim();
      data.strCity = "EGG HARBOR TWP";
    }
    return true;
  }

  @Override
  public String adjustMapAddress(String address) {
    address = stripFieldStart(address, "AREA ");
    return super.adjustMapAddress(address);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EH", "EGG HARBOR",
      "EHT", "EGG HARBOR TWP",
      "eh", "EGG HARBOR",
      "HT", "HAMILTON",
      "ht", "HAMILTON"
  });

  private static final String[] CITY_LIST = new String[]{
      "ABSECON",
      "ATLANTIC CITY",
      "BRIGANTINE",
      "BUENA",
        "LANDISVILLE",
        "MINOTOLA",
      "BUENA VISTA TOWNSHIP",
        "COLLINGS LAKES",
        "EAST VINELAND",
        "MILMAY",
        "NEWTONVILLE",
        "RICHLAND",
      "CORBIN CITY",
      "EGG HARBOR CITY",
        "CLARKS LANDING",
      "EGG HARBOR TOWNSHIP",
        "BARGAINTOWN",
        "ENGLISH CREEK",
        "JEFFERS LANDING",
      "ESTELL MANOR",
        "HUNTERS MILL",
      "FOLSOM",
        "PENNY POT",
      "GALLOWAY TOWNSHIP",
        "GALLOWAY",
        "ABSECON HIGHLANDS",
        "COLOGNE",
        "CONOVERTOWN",
        "GERMANIA",
        "LEEDS POINT",
        "OCEANVILLE",
        "POMONA",
        "SMITHVILLE",
      "HAMILTON TOWNSHIP",
        "MAYS LANDING",
        "MCKEE CITY",
        "MIZPAH",
      "HAMMONTON",
        "DA COSTA",
        "DUTCHTOWN",
      "LINWOOD",
      "LONGPORT",
      "MARGATE CITY",
      "MULLICA TOWNSHIP",
        "ELWOOD",
        "NESCO",
        "SWEETWATER",
      "NORTHFIELD",
      "PLEASANTVILLE",
      "PORT REPUBLIC",
      "SOMERS POINT",
      "VENTNOR CITY",
      "WEYMOUTH TOWNSHIP",
        "DOROTHY",
        "WEYMOUTH",
  };
}
