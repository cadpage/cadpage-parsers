package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Eaton County, MI
 */
public class MIEatonCountyParser extends DispatchOSSIParser {
  
  public MIEatonCountyParser() {
    super(CITY_CODES, "EATON COUNTY", "MI",
           "( CANCEL | ( FYI | EMPTY ) SRC? ID? DATETIME? CALL )  ADDR! CITY? X+? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad@eatoncounty.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}", true);
    if (name.equals("ID")) return new IdField("\\d{7}|\\d{11}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    return super.getField(name);
  }
  
  private static final Pattern PRIORITY_PTN = Pattern.compile("Event spawned .* PRIORITY (\\d).*");
  private static final Pattern SPECIAL_PTN = Pattern.compile("\\bRESPONSE: \\*\\*\\*PRIORITY (\\d)\\*\\*\\*|\\bCode: ([-A-Z0-9]+):|\\bQuestions:|\\bAborted by Medical Priority with code:|\\bEvent spawned from ");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRIORITY_PTN.matcher(field);
      if (match.matches()) {
        data.strPriority = match.group(1);
        return;
      }
      match = SPECIAL_PTN.matcher(field);
      StringBuffer sb = new StringBuffer();
      while (match.find()) {
        String pri = match.group(1);
        if (pri != null) data.strPriority = pri;
        String code = match.group(2);
        if (code != null) data.strCode = code;
        match.appendReplacement(sb, " ");
      }
      match.appendTail(sb);
      
      field = sb.toString().replace("  +", " ").trim();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PRI CODE INFO";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ASYR", "ASSYRIA TWP",      // Barry County
      "BATH", "BATH",
      "BCRK", "BATTLE CREEK",
      "BKFD", "BROOKFIELD TWP",
      "BLVU", "BELLEVUE",
      "BNTN", "BENTON TWP",
      "BVTP", "BELLEVUE TWP",
      "CHAR", "CHARLOTTE",
      "CHST", "CHESTER TWP",
      "CRML", "CARMEL TWP",
      "DDAL", "DIMONDALE",
      "DEWT", "DEWITT",           // Clinton County
      "DLHI", "DELHI TWP",        // Ingham County
      "DLTA", "LANSING",
      "DOWL", "DOWLING",
      "EAGL", "EAGLE",            // Clinton County  
      "EATN", "EATON TWP",
      "ERCY", "EATON RAPIDS",
      "ERTP", "EATON RAPIDS TWP",
      "FRPT", "FREEPORT",
      "GDLG", "GRAND LEDGE",
      "HAST", "HASTINGS",
      "HMLN", "HAMLIN TWP",
      "HOLT", "HOLT",
      "IONA", "IONIA",            // Ionia County
      "KLMO", "KALAMO TWP",
      "LADE", "LANSING DELTA",
      "LAKO", "LAKE ODESSA",
      "LANS", "LANSING CITY",
      "LAWI", "LANSING SOUTH",
      "LTWP", "LANSING TWP",      // Ingham County
      "MARS", "MARSHALL",
      "MASN", "MASON",
      "MLKN", "MULLIKEN",
      "NASH", "NASHVILLE",
      "OLVT", "OLIVET",
      "ONID", "ONEIDA TWP",
      "ONON", "ONONDAGA",
      "PORT", "PORTLAND",         // Ionia County
      "PTVL", "POTTERVILLE",
      "RXND", "ROXAND TWP",
      "SNFD", "SUNFIELD",
      "SNTP", "SUNFIELD TWP",
      "SPRG", "SPRINGPORT",
      "VVIL", "VERMONTVILLE",
      "VVTP", "VERMONTVILLE TWP",
      "WATR", "WATERTOWN TWP",
      "WIND", "WINDSOR TWP",
      "WLTN", "WALTON TWP",
      "WOOD", "WOODLAND"
  });
}
