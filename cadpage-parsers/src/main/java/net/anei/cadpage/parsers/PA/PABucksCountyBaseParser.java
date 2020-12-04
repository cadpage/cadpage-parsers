package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA7Parser;

/*
Bucks County, PA
Base parser with methods needed by all Bucks COunty Parsers

 */


abstract class PABucksCountyBaseParser extends DispatchA7Parser {

  public PABucksCountyBaseParser() {
    this(null);
  }

  public PABucksCountyBaseParser(String program) {
    super(INIT_TOWN_CODE, TOWN_CODES, CITY_CODES, "BUCKS COUNTY", "PA", program);
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (subject.equals("CAD Incident") && body.trim().startsWith("<!doctype html>\n")) {
      int pt1 = body.indexOf("\n<br>Text:");
      if (pt1 < 0) return false;
      pt1 += 10;
      int pt2 = body.indexOf("\n<br></p>\n", pt1);
      if (pt2 < 0) return false;
      body = body.substring(pt1, pt2).trim();
      body = body.replace("\n<br>", "\n");
      body = body.replace("<br>", "\n");
      return parseMsg(subject, body, data);
    }

    return super.parseHtmlMsg(subject, body, data);
  }


  @Override
  protected void parseAddress(StartType sType, int flags, String address, Data data) {
    address = cleanupAddress(address);
    super.parseAddress(sType, flags, address, data);
  }

  @Override
  protected void parseAddress(String address, Data data) {
    address = cleanupAddress(address);
    super.parseAddress(address, data);
  }

  /**
   * Clean dispatch typos and abbreviations in address
   * @param sAddr original address
   * @return cleaned up address
   */
  private String cleanupAddress(String sAddr) {
    // Clean up dispatch boo boos
    sAddr = sAddr.replace("FRIER RD", "FREIER RD");
    sAddr = sAddr.replace("WHITE BRIAR", "WHITEBRIAR");
    sAddr = expandStreet("COLD SPRING CREAMERY", sAddr);
    sAddr = expandStreet("FALLSINGTON TULLYTOWN", sAddr);
    return sAddr;
  }

  private static final int INIT_TOWN_CODE = 21;
  private static final String[] TOWN_CODES = new String[]{
    /*21*/ "BEDMINSTER TWP",
    /*22*/ "BENSALEM TWP",
    /*23*/ "BRIDGETON TWP",
    /*24*/ "BRISTOL",
    /*25*/ "BRISTOL TWP",
    /*26*/ "BUCKINGHAM TWP",
    /*27*/ "CHALFONT",
    /*28*/ "DOYLESTOWN",
    /*29*/ "DOYLESTOWN TWP",
    /*30*/ "",
    /*31*/ "DUBLIN",
    /*32*/ "DURHAM TWP",
    /*33*/ "EAST ROCKHILL",
    /*34*/ "FALLS TWP",
    /*35*/ "HAYCOCK TWP",
    /*36*/ "HILLTOWN TWP",
    /*37*/ "HULMEVILLE",
    /*38*/ "IVYLAND",
    /*39*/ "LANGHORNE",
    /*40*/ "",
    /*41*/ "LANGHORNE MANOR",
    /*42*/ "LOWER MAKEFIELD TWP",
    /*43*/ "LOWER SOUTHAMPTON TWP",
    /*44*/ "MIDDLETOWN TWP",
    /*45*/ "MILFORD TWP",
    /*46*/ "MORRISVILLE",
    /*47*/ "NEW BRITAIN",
    /*48*/ "NEW BRITAIN TWP",
    /*49*/ "NEW HOPE",
    /*50*/ "",
    /*51*/ "NEWTOWN",
    /*52*/ "NEWTOWN TWP",
    /*53*/ "NOCKAMIXON TWP",
    /*54*/ "NORTHAMPTON TWP",
    /*55*/ "PENNDEL",
    /*56*/ "PERKASIE",
    /*57*/ "PLUMSTEAD TWP",
    /*58*/ "QUAKERTOWN",
    /*59*/ "RICHLAND TWP",
    /*60*/ "",
    /*61*/ "RICHLANDTOWN",
    /*62*/ "RIEGELSVILLE",
    /*63*/ "SELLERSVILLE",
    /*64*/ "SILVERDALE",
    /*65*/ "SOLEBURY TWP",
    /*66*/ "SPRINGFIELD TWP",
    /*67*/ "TELFORD",
    /*68*/ "TINICUM TWP",
    /*69*/ "TRUMBAUERSVILLE",
    /*70*/ "",
    /*71*/ "TULLYTOWN",
    /*72*/ "UPPER MAKEFIELD TWP",
    /*73*/ "UPPER SOUTHAMPTON TWP",
    /*74*/ "WARMINSTER TWP",
    /*75*/ "WARRINGTON TWP",
    /*76*/ "WARWICK TWP",
    /*77*/ "WEST ROCKHILL TWP",
    /*78*/ "WRIGHTSTOWN TWP",
    /*79*/ "YARDLEY"

    // City numbers from 81-99 are used by county wide agencies and could be
    // anywhere in the county.  So we just leave them out of the table.
  };

  protected String getOutsideCity(String code) {
    return CITY_CODES.getProperty(code);
  }

  // City codes is only used for out of county mutual aid calls
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BRYN ATHE",        "BRYN ATHYN",
      "EWING TWP",        "EWING TWP,NJ",
      "EWING TWP NJ",     "EWING TWP,NJ",
      "EWING NJ",         "EWING TWP,NJ",
      "DELA TWP",         "DELAWARE TWP",
      "HATBORO",          "HATBORO",
      "HATFIL",           "HATFIELD",
      "HATFIELD TWP",     "HATFIELD TWP",
      "HOLLAND TWP",      "HOLLAND TWP,NJ",
      "HOPEWELL TWP",     "HOPEWELL TWP",
      "HORSHAM TWP",      "HORSHAM TWP",
      "KINGWOOD TWP",     "KINGWOOD TWP,NJ",
      "LAMBERTVILL",      "LAMBERTVILLE,NJ",
      "LAMBERTVILLE",     "LAMBERTVILLE,NJ",
      "LAWRENCE TWP",     "LAWRENCE TWP,NJ",
      "LAWRENCEVILLE",    "LAWRENCEVILLE,NJ",
      "LAWRENCEVILLE NJ", "LAWRENCEVILLE,NJ",
      "LOWER MILFORD TWP","LOWER MILFORD TWP",
      "LOWER MORELAND",   "LOWER MORELAND TWP",
      "LSAUC TWP",        "LOWER SAUCON TWP",
      "LOWE SAUCON",      "LOWER SAUCON TWP",
      "LOWER SAUCO",      "LOWER SAUCON TWP",
      "LOWER SAUCON",     "LOWER SAUCON TWP",
      "LOWER SAUCON TAP", "LOWER SAUCON TWP",
      "LOWER SAUCON TWN", "LOWER SAUCON TWP",
      "LOWER SAUCON TWP", "LOWER SAUCON TWP",
      "LWR SAU",          "LOWER SAUCON TWP",
      "LWR SAUCON",       "LOWER SAUCON TWP",
      "MOTGOMERY",        "MONTGOMERY TWP",
      "MONTGOMERY",       "MONTGOMERY TWP",
      "MONTGOMER TWP",    "MONTGOMERY TWP",
      "MONTGOMERY TWP",   "MONTGOMERY TWP",
      "MONTG TWP",        "MONTGOMERY TWP",
      "MONTGO TWP",       "MONTGOMERY TWP",
      "MONT TWP",         "MONTGOMERY TWP",
      "PENNSBURG",        "PENNSBURG",
      "SALFORD TWP",      "SALFORD TWP",
      "UPPER HANOVE",     "UPPER HANOVER TWP",
      "UPPRE HANNOVER",   "UPPER HANOVER TWP",
      "UPPER SOUTHAMPTON TWP", "UPPER SOUTHAMPTON TWP",
      "UPRSAUC",          "UPPER SAUCON TWP"
  });
}
