package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA8Parser;




public class NYOnondagaCountyCParser extends DispatchA8Parser {

  public NYOnondagaCountyCParser() {
    super(CITY_CODES, "ONONDAGA COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "wavescalls@me.com,NedrowDispatch@fdcmsincidents.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\t', ' ');
    body = (body + ' ').replace("ASAP ** ", "ASAP *** ").replace(" NL ", " ** ").trim();
    return super.parseMsg(body, data);
  }

  private static final Pattern AGENCY_PTN = Pattern.compile("\n *Agency: *(.*)(?=\n)");
  private static final Pattern CROSS_STREET_PTN = Pattern.compile("\n *Cross Street: *(.*)(?=\n)");
  private static final Pattern UNIT_PTN = Pattern.compile("\n *Responding Units: *\n* *(.*?)(?=\n|\\*\\*|$)");

  @Override
  protected void parseSpecialField(String field, Data data) {
    Parser p = new Parser(field);
    data.strSource = getPatternValue(p, AGENCY_PTN);
    data.strCross = getPatternValue(p, CROSS_STREET_PTN);
    data.strUnit = getPatternValue(p, UNIT_PTN);
  }

  private String getPatternValue(Parser p, Pattern ptn) {
    Matcher match = p.getMatcher(ptn);
    return (match == null ? "" : match.group(1).trim());
  }

  @Override
  protected String specialFieldNames() {
    return "SRC X UNIT";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "TCM", "CAMILLUS",
      "TGD", "GEDDES",
      "VCM", "CAMILLUS",

      "13078", "JAMESVILLE",
      "13084", "LA FAYETTE",
      "13120", "NEDROW",
      "13202", "SYRACUSE",
      "13203", "SYRACUSE",
      "13204", "SYRACUSE",
      "13205", "SYRACUSE",
      "13207", "SYRACUSE",
      "13210", "SYRACUSE",
      "13215", "SYRACUSE",
      "13224", "MEADOWBROOK",
      "13244", "SYRACUSE"
  });
}
