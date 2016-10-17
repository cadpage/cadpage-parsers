package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * fAlamance county, NC
 */
public class NCAlamanceCountyParser extends DispatchOSSIParser {
  
  public NCAlamanceCountyParser() {
    super(CITY_CODES, "ALAMANCE COUNTY", "NC",
           "ID?: CALL ADDR! APT? CITY/Y X X INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@alamance-nc.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.contains(";")) {
      int pt = body.indexOf(":CAD:");
      if (pt < 0) return false;
      pt += 5;
      body = body.substring(0,pt) + subject + ' ' +  body.substring(pt);
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }
  
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("DIST")) {
        data.strSupp = field;
        return;
      }
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO APT";
    }
  }
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|UNIT|#)[- ]*(.*)");
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GRAH",  "GRAHAM",
      "GREE",  "GREEN LEVEL",
      "HAW",   "HAW RIVER",
      "MEB",   "MEBANE",
      "BURL",  "BURLINGTON",
      "GIB",   "GIBSONVILLE",
      "ELON",  "ELON",
      "LIB",   "LIBERTY",
      "SWEP",  "SWEPSONVILLE",
      "SNOW",  "SNOW CAMP",
      
      // Guilford County
      "WHIT",  "WHITSETT"
  });
}
