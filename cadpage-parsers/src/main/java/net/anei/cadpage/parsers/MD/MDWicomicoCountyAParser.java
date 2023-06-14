package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;
/**
 * Wicomico County, MD
 */
public class MDWicomicoCountyAParser extends DispatchOSSIParser {

  private static final Pattern ID_MARK = Pattern.compile("^\\d+:");

  public MDWicomicoCountyAParser() {
    super(CITY_CODES, "WICOMICO COUNTY", "MD",
           "ID CALL ADDR! ( CITY X X | ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cad@wicomicocounty.org";
  }

  @Override
  public String adjustMapAddress(String address) {
    return BROWN_ST_EXT_PTN.matcher(address).replaceAll("BROWN ST");
  }
  private static final Pattern BROWN_ST_EXT_PTN = Pattern.compile("\\bBROWN ST EXT\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String body, Data data) {

    // Strip off leading sequence number
    Matcher match = ID_MARK.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();

    if (!body.startsWith("CAD:")) body = "CAD:" + body;

    if (!super.parseMsg(body, data)) return false;

    // Fix state if necessary
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1);
      data.strCity = data.strCity.substring(0,pt);
      if (data.strState.length() == 0) data.defState = "";
    }
    if (data.strCity.equals("DELMAR")) data.defState = "";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }



  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALLE", "ALLEN",
      "EDEN", "EDEN",
      "BIVA", "BIVALVE",
      "DELM", "DELMAR/",   // Can in either Delaware or Maryland
      "FRUI", "FRUITLAND",
      "GALE", "GALESTOWN",
      "HEBR", "HEBRON",
      "LAUR", "LAUREL/DE",
      "MARD", "MARDELA",
      "NANT", "NANTICOKE",
      "PARS", "PARSONBURG",
      "PITT", "PITTSVILLE",
      "POWE", "POWELLVILLE",
      "QUAN", "QUANTICO",
      "RHOD", "RHODESDALE",
      "SALI", "SALISBURY",
      "SEAF", "SEAFORD",
      "SHAR", "SHARPTOWN",
      "TYAS", "TYASKIN",
      "WHIT", "WHITEHAVEN",
      "WILL", "WILLARDS"
  });
}
