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
    return "wavescalls@me.com";
  }
  
  protected void parseSpecialField(String field, Data data) {
    Parser p = new Parser(field);
    data.strSource = getPatternValue(p, AGENCY_PTN);
    data.strCross = getPatternValue(p, CROSS_STREET_PTN);
    data.strUnit = getPatternValue(p, UNIT_PTN);
  }
  private static final Pattern AGENCY_PTN = Pattern.compile("\n *Agency: *(.*)(?=\n)");
  private static final Pattern CROSS_STREET_PTN = Pattern.compile("\n *Cross Street: *(.*)(?=\n)");
  private static final Pattern UNIT_PTN = Pattern.compile("\n *Responding Units: *\n* *(.*?)(?=\n|\\*\\*|$)");
  
  private String getPatternValue(Parser p, Pattern ptn) {
    Matcher match = p.getMatcher(ptn);
    return (match == null ? "" : match.group(1).trim());
  }
  
  protected String specialFieldNames() {
    return "SRC X UNIT";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "TCM", "CAMILLUS",
      "TGD", "GEDDES",
      "VCM", "CAMILLUS",
  });
}
	