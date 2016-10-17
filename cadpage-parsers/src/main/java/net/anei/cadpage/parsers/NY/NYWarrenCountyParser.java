package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Warren County, NY
 */
public class NYWarrenCountyParser extends FieldProgramParser {
  public NYWarrenCountyParser() {
    super(CITY_LIST, "WARREN COUNTY", "NY",
          "SRC CALL INFO EMPTY? ADDR/SXP! GPS");
  }
    
  @Override
  public String getFilter() {
    return "alerts@sheriff.co.warren.ny.us";
  }

  private static final Pattern GOOGLE_MAP_URL_PATTERN
    = Pattern.compile("(.*?)\\(http://maps\\.google\\.com/\\?q=([^)]*)\\)", Pattern.DOTALL);
	@Override
	protected boolean parseMsg(String body, Data data) {
	  Matcher m = GOOGLE_MAP_URL_PATTERN.matcher(body);
	  if (m.matches()) body = m.group(1)+"\n"+m.group(2);
	  return parseFields(body.split("\n"), data);
	}	  
	
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("\\*ALERT +([A-Z]+)\\*", true);
    return super.getField(name);
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final String[] CITY_LIST = {
     "BOLTON",
     "CHESTER",
     "GLENS FALLS",
     "HAGUE",
     "HORICON",
     "JOHNSBURG",
     "LAKE GEORGE",
     "LAKE GEORGE",
     "LAKE LUZERNE",
     "QUEENSBURY",
     "STONY CREEK",
     "THURMAN",
     "WARRENSBURG"
  };
}