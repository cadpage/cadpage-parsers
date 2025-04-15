package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNStLouisCountyBParser extends FieldProgramParser {

  public MNStLouisCountyBParser() {
    super(CITY_LIST, "ST LOUIS COUNTY", "MN",
          "GPS ADDR/S PLACE CALL INFO! END");
  }

  @Override
  public String getFilter() {
    return "STLCAD@StLouisCountyMN.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Inform CAD Page")) return false;
    return parseFields(body.split(" \\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("\\*(\\d{2})(\\d{6}) \\*(\\d{2})(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4), data);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AURORA",
      "BABBITT",
      "BIWABIK",
      "BROOKSTON",
      "BUHL",
      "CHISHOLM",
      "COOK",
      "DULUTH",
      "ELY",
      "EVELETH",
      "FLOODWOOD",
      "GILBERT",
      "HERMANTOWN",
      "HIBBING",
      "HOYT LAKES",
      "IRON JUNCTION",
      "KINNEY",
      "LEONIDAS",
      "MCKINLEY",
      "MEADOWLANDS",
      "MOUNTAIN IRON",
      "ORR",
      "PROCTOR",
      "RICE LAKE",
      "TOWER",
      "VIRGINIA",
      "WINTON",

      // Townships
      "ALANGO TWP",
      "ALBORN TWP",
      "ALDEN TWP",
      "ANGORA TWP",
      "ARROWHEAD TWP",
      "AULT TWP",
      "BALKAN TWP",
      "BASSETT TWP",
      "BEATTY TWP",
      "BIWABIK TWP",
      "BREITUNG TWP",
      "BREVATOR TWP",
      "CAMP 5 TWP",
      "CANOSIA TWP",
      "CEDAR VALLEY TWP",
      "CHERRY TWP",
      "CLINTON TWP",
      "COLVIN TWP",
      "COTTON TWP",
      "CRANE LAKE TWP",
      "CULVER TWP",
      "DULUTH TWP",
      "EAGLES NEST TWP",
      "ELLSBURG TWP",
      "ELMER TWP",
      "EMBARRASS TWP",
      "FAIRBANKS TWP",
      "FAYAL TWP",
      "FIELD TWP",
      "FINE LAKES TWP",
      "FLOODWOOD TWP",
      "FREDENBERG TWP",
      "FRENCH TWP",
      "GNESEN TWP",
      "GRAND LAKE TWP",
      "GREAT SCOTT TWP",
      "GREENWOOD TWP",
      "HALDEN TWP",
      "INDUSTRIAL TWP",
      "KABETOGAMA TWP",
      "KELSEY TWP",
      "KUGLER TWP",
      "LAKEWOOD TWP",
      "LAVELL TWP",
      "LEIDING TWP",
      "LINDEN GROVE TWP",
      "MCDAVITT TWP",
      "MEADOWLANDS TWP",
      "MIDWAY TWP",
      "MORCOM TWP",
      "MORSE TWP",
      "NESS TWP",
      "NEW INDEPENDENCE TWP",
      "NORMANNA TWP",
      "NORTH STAR TWP",
      "NORTHLAND TWP",
      "OWENS TWP",
      "PEQUAYWAN TWP",
      "PIKE TWP",
      "PORTAGE TWP",
      "PRAIRIE LAKE TWP",
      "SANDY TWP",
      "SOLWAY TWP",
      "STONEY BROOK TWP",
      "STURGEON TWP",
      "TOIVOLA TWP",
      "VAN BUREN TWP",
      "VERMILION LAKE TWP",
      "WAASA TWP",
      "WHITE TWP",
      "WILLOW VALLEY TWP",
      "WUORI TWP",

      // Unorganized territories
      "ANGLEWORM LAKE",
      "BEAR HEAD LAKE",
      "BIRCH LAKE",
      "CAMP A LAKE",
      "CRAB LAKE",
      "DARK RIVER",
      "GHEEN",
      "HAY LAKE",
      "HEIKKALA LAKE",
      "HUSH LAKE",
      "JANETTE LAKE",
      "LEANDER LAKE",
      "LINWOOD LAKE",
      "MARION LAKE",
      "MCCORMACK",
      "MUD HEN LAKE",
      "NETT LAKE",
      "NORTHEAST ST. LOUIS",
      "NORTHWEST ST. LOUIS",
      "PFEIFFER LAKE",
      "PICKET LAKE",
      "POTSHOT LAKE",
      "SAND LAKE",
      "SLIM LAKE",
      "STURGEON RIVER",
      "SUNDAY LAKE",
      "TIKANDER LAKE",
      "WHITEFACE RESERVOIR",

      // Census-designated places
      "MAHNOMEN",
      "NETT LAKE",
      "SOUDAN",

      // Unincorporated communities
      "ALBORN",
      "ANGORA",
      "ASH LAKE",
      "BASSETT",
      "BEAR RIVER",
      "BENGAL",
      "BRIMSON",
      "BRITT",
      "BURNETT",
      "BURNTSIDE",
      "BUYCK",
      "CANYON",
      "CELINA",
      "CENTRAL LAKES",
      "CHERRY",
      "CLOVER VALLEY",
      "COTTON",
      "CRANE LAKE",
      "CULVER",
      "CUSSON",
      "ELDES CORNER",
      "ELMER",
      "EMBARRASS",
      "FAIRBANKS",
      "FLORENTON",
      "FORBES",
      "FOUR CORNERS",
      "FRENCH RIVER",
      "GHEEN",
      "GHEEN CORNER",
      "GLENDALE",
      "GOWAN",
      "GREANEY",
      "IDINGTON",
      "INDEPENDENCE",
      "ISLAND LAKE",
      "KABETOGAMA",
      "KEENAN",
      "KELSEY",
      "LINDEN GROVE",
      "MAKINEN",
      "MARKHAM",
      "MCCOMBER",
      "MEADOW BROOK",
      "MELRUDE",
      "MUNGER",
      "PALMERS",
      "PALO",
      "PAYNE",
      "PEARY",
      "PETREL",
      "PEYLA",
      "PINEVILLE",
      "PROSIT",
      "RAMSHAW",
      "ROBINSON",
      "ROLLINS",
      "SAGINAW",
      "SAX",
      "SHAW",
      "SHERMAN CORNER",
      "SIDE LAKE",
      "SILICA",
      "SIMAR",
      "SKIBO",
      "STURGEON",
      "TAFT",
      "TOIVOLA",
      "TWIG",
      "VERMILION DAM",
      "WAKEMUP",
      "WHITEFACE",
      "WOLF",
      "ZIM",

      // Ghost towns
      "CARSON LAKE",
      "COSTIN VILLAGE",
      "ELCOR",
      "FERMOY",
      "SPINA"

  };
}
