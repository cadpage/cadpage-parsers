package net.anei.cadpage.parsers.NC;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCWilsonCountyParser extends DispatchOSSIParser {

  public NCWilsonCountyParser() {
    super(CITY_CODES, "WILSON COUNTY", "NC",
          "ID? CODE? CALL ADDR! CITY? ( PLACE SRC | SRC? )  X+? INFO+");
  }

  @Override
  public String getFilter() {
    return "@Wilson-co.com,930010";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    body = stripFieldStart(body, "/ Text Message / ");

    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (subject.contains(";")) body = "CAD:" + subject + ' ' + body.substring(4);
    return super.parseMsg(body,  data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6,}", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Za-z]\\d\\d[A-Za-z]?");
    if (name.equals("SRC")) return new SourceField("\\d{1,2}[A-Z0-9]|[A-Z]\\d|NAS", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (US_264_DIR.matcher(field).matches()) {
        parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }


  @Override
  public String adjustMapAddress(String address) {
    Matcher match = US_264_DIR.matcher(address);
    if (match.find()) {
      StringBuffer bf = new StringBuffer();
      do {
        match.appendReplacement(bf, "US 264");
        if (match.group(1) != null) bf.append(" ALT");
      } while (match.find());
      match.appendTail(bf);
      address = bf.toString();
    }
    return address;
  }
  private static final Pattern US_264_DIR = Pattern.compile("\\bUS +264(A?)(?: +[NESW])?\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ANGI", "ANGIER",
      "APEX", "APEX",
      "AYDE", "AYDEN",
      "BALY", "BAILEY",
      "BC",   "BLACK CREEK",
      "BENS", "BENSON",
      "BTLB", "BATTLEBORO",
      "CARY", "CARY",
      "CAST", "CASTALIA",
      "CH",   "CHARLOTTE",
      "CLAY", "CLAYTON",
      "CNTY", "COUNTY",
      "COL",  "SC COLUMBIA",
      "CREE", "CREEDMOOR",
      "CRT",  "CARTERET COUNTY",
      "DUDL", "DUDLEY",
      "DUR",  "DURHAM",
      "EC",   "ELM CITY",
      "EDGE", "EDGECOMBE COUNTY",
      "EUR",  "EUREKA",
      "FARM", "FARMVILLE",
      "FOUN", "FOUNTAIN",
      "FOUR", "FOUR OAKS",
      "FRMT", "FREMONT",
      "FUQ",  "FUQUAY VARINA",
      "GAR",  "GARNER",
      "GOL",  "GOLDSBORO",
      "GREE", "GREENE COUNTY",
      "GRIF", "GRIFTON",
      "GVIL", "GREENVILLE",
      "HALI", "HALIFAX COUNTY",
      "HOLL", "HOLLESTER",
      "HOOK", "HOOKERTON",
      "HS",   "HOLLY SPRINGS",
      "JOHN", "JOHNSTON COUNTY",
      "KINS", "KINSTON",
      "KNIG", "KNIGHTDALE",
      "KNLY", "KENLY",
      "LAGR", "LAGRANGE",
      "LOUI", "LOUISBURG",
      "LUC",  "LUCAMA",
      "MAC",  "MACCLESFIELD",
      "MCRO", "MICRO",
      "MORR", "MORRISVILLE",
      "MSEX", "MIDDLESEX",
      "MTOL", "MOUNT OLIVE",
      "NASH", "NASH COUNTY",
      "NB",   "NEW BERN",
      "NEWH", "NEW HILL",
      "NHC",  "NORTHAMPTON COUNTY",
      "NVLE", "NASHVILLE",
      "OTHE", "OTHER COUNTY",
      "PERQ", "PERQUIMANS",
      "PIKE", "PIKEVILLE",
      "PINE", "PINETOPS",
      "PITT", "PITT COUNTY",
      "PRIN", "PRINCETON",
      "RAL",  "RALEIGH",
      "RMT",  "ROCKY MOUNT",
      "ROLE", "ROLESVILLE",
      "SAR",  "SARATOGA",
      "SEVE", "SEVENSPRINGS",
      "SH",   "SPRING HOPE",
      "SHAR", "SHARPSBURG",
      "SIMS", "SIMS",
      "SNO",  "SNOW HILL",
      "STBG", "STANTONSBURG",
      "TARB", "TARBORO",
      "WAKE", "WAKE COUNTY",
      "WALS", "WALSTONBURG",
      "WAYN", "WAYNE COUNTY",
      "WEND", "WENDELL",
      "WF",   "WAKE FOREST",
      "WHIT", "WHITAKERS",
      "WIN",  "WINTERVILLE",
      "WLSN", "WILSON",
      "WS",   "WILLOW SPRING",
      "YVLE", "YOUNGSVILLE",
      "ZEB",  "ZEBULON"

  });
}