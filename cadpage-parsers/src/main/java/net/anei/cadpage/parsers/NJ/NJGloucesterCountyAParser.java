package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.Message;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

/**
 * Gloucester County, NJ (version A)
 */
public class NJGloucesterCountyAParser extends DispatchProphoenixParser {

  private static final Pattern FROM_ADDR_PTN = Pattern.compile("GC RSAN #(\\d+)");
  private static final Pattern SPECIAL_TRAIL_PTN = Pattern.compile(";.{2}$");

  private String fromAddress;

  public NJGloucesterCountyAParser() {
    super(CITY_CODES, CITY_LIST, "GLOUCESTER COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "gccad@co.gloucester.nj.us,777,@private.gloucesteralert.com,12101,411912";
  }

  @Override
  protected Data parseMsg(Message msg, int parseFlags) {
    fromAddress = msg.getFromAddress();
    return super.parseMsg(msg, parseFlags);
  }

  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(?<=\\}) | (?=(?:CrossStreet[12]|CommonName|Units|Comments) *:)");
  private static final Pattern TOWNSHIP_PTN = Pattern.compile("\\b(T)o(w)nshi(p)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String body, Data data) {

    // THere are some weird text options presumably introduced by forwarding services
    body = stripFieldStart(body, "Fwd:");
    body = stripFieldEnd(body, "=");
    body = stripFieldEnd(body, " STOP");

    Matcher match = FROM_ADDR_PTN.matcher(fromAddress);
    if (match.matches()) {
      body = "GC ALERT (#" + match.group(1) + ") " + body;
      match = SPECIAL_TRAIL_PTN.matcher(body);
      if (match.find()) body = body.substring(0,match.start()).trim();
    }

    if (!body.contains("\n")) {
      body = MISSING_BRK_PTN.matcher(body).replaceAll("\n");
    }
    body = TOWNSHIP_PTN.matcher(body).replaceAll("$1$2$3");
    return super.parseMsg(body, data);
  }

  private static final Pattern ACE_PTN = Pattern.compile("\\bACE\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = ACE_PTN.matcher(addr).replaceAll("ATLANTIC CITY EXPY");
    return super.adjustMapAddress(addr);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("ROWAN")) city = "GLASSBORO";
    else if (city.equalsIgnoreCase("BATSTO")) city = "HAMMONTON";
    return city;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "A", "CLAYTON",
      "B", "DEPTFORD TWP",
      "C", "EAST GREENWICH TWP",
      "CF","WEST DEPFORD TWP",
      "D", "ELK TWP",
      "E", "FRANKLIN TWP",
      "F", "GLASSBORO",
      "G", "GIBBSTOWN",
      "H", "HARRISON TWP",
      "I", "LOGAN TWP",
      "J", "MANTUA TWP",
      "K", "MONROE TWP",
      "L", "NATIONAL PARK",
      "M", "NEWFIELD",
      "N", "PAULSBORO",
      "O", "PITMAN",
      "P", "SOUTH HARRISON TWP",
      "Q", "SWEDESBORO",
      "R", "WASHINGTON TWP",
      "S", "WENONAH",
      "T", "WEST DEPTFORD TWP",
      "Q", "SWEDESBORO",
      "U", "WESTVILLE",
      "V", "WOODBURY",
      "W", "WOODBURY HEIGHTS",
      "X", "WOOLWICH TWP",
      "Y", "ROWAN",
      "04", "BUENA",
      "05", "BUENA VISTA TWP",
      "07", "EGG HARBOR CITY",
      "09", "ESTELL MANOR",
      "10", "FOLSOM",
      "23", "WEYMOUTH TWP",
      "AC", "ATLANTIC COUNTY",
      "CU", "CUMBERLAND COUNTY",
      "SA", "SALEM COUNTY"
  });

  private static final String[] CITY_LIST = new String[]{

    // Boroughs
    "CLAYTON",
    "GLASSBORO",
    "NATIONAL PARK",
    "NEWFIELD",
    "PAULSBORO",
    "PITMAN",
    "SWEDESBORO",
    "WENONAH",
    "WESTVILLE",
    "WOODBURY",
    "WOODBURY HEIGHTS",

    // Townships
    "DEPTFORD",
    "DEPTFORD TWP",
    "DEPTFORD TOWNSHIP",
    "EAST GREENWICH",
    "EAST GREENWICH TWP",
    "EAST GREENWICH TOWNSHIP",
    "ELK",
    "ELK TWP",
    "ELK TOWNSHIP",
    "FRANKLIN",
    "FRANKLIN TWP",
    "FRANKLIN TOWNSHIP",
    "GREENWICH",
    "GREENWICH TWP",
    "GREENWICH TOWNSHIP",
    "HARRISON",
    "HARRISON TWP",
    "HARRISON TOWNSHIP",
    "LOGAN",
    "LOGAN TWP",
    "LOGAN TOWNSHIP",
    "MANTUA",
    "MANTUA TWP",
    "MANTUA TOWNSHIP",
    "MONROE",
    "MONROE TWP",
    "MONROE TOWNSHIP",
    "SOUTH HARRISON",
    "SOUTH HARRISON TWP",
    "SOUTH HARRISON TOWNSHIP",
    "WASHINGTON",
    "WASHINGTON TWP",
    "WASHINGTON TOWNSHIP",
    "WEST DEPTFORD",
    "WEST DEPTFORD TWP",
    "WEST DEPTFORD TOWNSHIP",
    "WOOLWICH",
    "WOOLWICH TWP",
    "WOOLWICH TOWNSHIP",

    // Communities
    "ALMONESSON",
    "AURA",
    "BARNSBORO",
    "BECKETT",
    "BILLINGSPORT",
    "BRIDGEPORT",
    "CLARKSBORO",
    "CROSS KEYS",
    "EWAN",
    "FRANKLINVILLE",
    "GENLOCH",
    "GIBBSTOWN",
    "GOOD INTENT",
    "GREENFIELDS VILLAGE",
    "HARDINGVILLE",
    "HARRISONVILLE",
    "HURFFVILLE",
    "MALAGA",
    "MICKLETON",
    "MT ROYAL",
    "MOUNT ROYAL",
    "MULLICA HILL",
    "NEW BROOKLYN",
    "OAK VALLEY",
    "RED BANK",
    "REPAUPO",
    "RICHWOOD",
    "RICHWOOD",
    "SEWELL",
    "SEWELL WASHINGTON",
    "THOROFARE",
    "TURNERSVILLE",
    "VICTORY LAKES",
    "WILLIAMSTOWN",
    "WOLFERT",

    // Atlantic County
    "BUENA",
      "LANDISVILLE",
      "MINOTOLA",
    "BUENA VISTA TWP",
    "BUENA VISTA TOWNSHIP",
      "COLLINGS LAKES",
      "EAST VINELAND",
      "MILMAY",
      "NEWTONVILLE",
      "RICHLAND",
    "CORBIN CITY",
    "EGG HARBOR CITY",
      "CLARKS LANDING",
    "EGG HARBOR TWP",
    "EGG HARBOR TOWNSHIP",
      "BARGAINTOWN",
      "ENGLISH CREEK",
      "JEFFERS LANDING",
    "ESTELL MANOR",
      "HUNTERS MILL",
    "FOLSOM",
      "PENNY POT",
    "GALLOWAY TWP",
    "GALLOWAY TOWNSHIP",
      "ABSECON HIGHLANDS",
      "COLOGNE",
      "GERMANIA",
      "LEEDS POINT",
      "OCEANVILLE",
      "POMONA",
      "SMITHVILLE",
    "HAMILTON TWP",
    "HAMILTON TOWNSHIP",
      "MAYS LANDING",
      "MCKEE CITY",
      "MIZPAH",
    "HAMMONTON",
      "BATSTO",
      "DA COSTA",
      "DUTCHTOWN",
    "WEYMOUTH TWP",
    "WEYMOUTH TOWNSHIP",
      "DOROTHY",
      "WEYMOUTH",

    // Camden County
    "AUDUBON",
    "AUDUBON PARK",
    "BELLMAWR",
    "BARRINGTON",
    "BROOKLAWN",
    "CAMDEN",
    "CLEMENTON",
    "COLLINGSWOOD",
    "GLOUCESTER CITY",
    "RUNNEMEDE",
    "GLOUCESTER TWP",
    "GLOUCESTER TOWNSHIP",
      "BLACKWOOD",
      "BLEINHEIM",
      "CHEWS LANDING",
      "GENNNDORA",
      "GRENLOCH",
      "LAKELAND",
    "HI-NELLA",
    "HADDON HEIGHTS",
    "HADDON TWP",
    "HADDON TOWNSHIP",
    "LAUREL SPRINGS",
    "LINDENWOLD",
    "MAGNOLIA",
    "MOUNT EPHRAIM",
    "OAKLYN",
    "PINE HILL",
    "PINE VALLEY",
    "SOMERDALE",
    "STRATFORD",
    "WINSLOW",
    "WINSLOW TWP",
    "WINSLOW TOWNSHIP",
      "ALBION",
      "ANCORA",
      "BLUE ANCHOR",
      "BRADDOCK",
      "CEDAR BROOK",
      "ELM",
      "IVYSTONE FARMS",
      "SICKLERVILLE",
      "SICKLERVILLE WASHING",
      "TANSBORO",
      "WATERFORD WORKS",
      "WEST ATCO",

    // Cape May County
    "MARMORA",

    // Cumberland County
    "VINELAND",

    // Delaware County, PA
    "CHESTER",

    // Salem County
    "ALLOWAY TOWNSHIP",
      "ALDINE",
      "ALLOWAY",
      "ALLOWAY JUNCTION",
      "FRIESBURG",
      "MOWER",
      "OAKLAND",
      "PENTON",
      "REMSTERVILLE",
      "RIDDLETON",
    "CARNEYS POINT TWP",
    "CARNEYS POINT TOWNSHIP",
      "BIDDLES LANDING",
      "CARNEYS POINT",
      "HELMES COVE",
      "LAYTONS LAKE",
    "ELMER",
    "OLDMANS TWP",
    "OLDMANS TOWNSHIP",
      "AUBURN",
      "PEDRICKTOWN",
    "MANNINGTON TWP",
    "MANNINGTON TOWNSHIP",
      "ACTON",
      "HALTOWN",
      "MARSHALTOWN",
      "POINTERS",
      "PORTERTOWN",
      "SLAPES CORNER",
      "WELCHVILLE",
    "PENNS GROVE",
    "PENNSVILLE TWP",
    "PLENNSVILLE TOWNSHIP",
      "DEEPWATER",
      "PENNSVILLE",
    "PILESGROVE TWP",
    "ILESGROVE TOWNSHIP",
      "FRIENDSHIP",
    "PITTSGROVE TWP",
    "PITTSGROVE TOWNSHIP",
      "BROTMANVILLE",
      "CENTERTON",
      "NORMA",
      "OLIVET",
    "SALEM",
    "UPPER PITTSGROVE TWP",
    "UPPER PITTSGROVE TOWNSHIP",
      "DARETOWN",
      "MONROEVILLE",
      "WHIG LANE",
    "WOODSTOWN"
  };
}
