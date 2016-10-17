package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCEdgecombeCountyParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("Edgecombe(?:911|Central):");
  private static final Pattern CALL_CODE_UNIT_PTN = Pattern.compile("(.*) CODE (\\d) (.*)");
  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*?) ([A-Z]*\\d[ ,A-Z0-9]*)");
  
  public NCEdgecombeCountyParser() {
    super(CITY_LIST, "EDGECOMBE COUNTY", "NC",
          "ADDR EMPTY EMPTY CITY EMPTY EMPTY EMPTY EMPTY EMPTY CALL EMPTY EMPTY UNIT END");
  }
  
  @Override
  public String getFilter() {
    return "@co.edgecombe.nc.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    
    String[] flds =  body.split("\n");
    if (flds.length > 10) return parseFields(flds, data);
    
    setFieldList("ADDR APT CITY CALL PRI UNIT");
    parseAddress(StartType.START_ADDR, body.replace(" @ ", " / ").replace("//", "/"), data);
    body = getLeft();
    if (body.length() == 0) return false;
    
    // If there is a priority field separating the call description and units
    // things are easy
    match = CALL_CODE_UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strCall = match.group(1).trim();
      data.strPriority = match.group(2);
      data.strUnit = match.group(3).trim();
    }
    
    // Else look for a unit group following the description
    else if ((match = CALL_UNIT_PTN.matcher(body)).matches()) {
      data.strCall = match.group(1).trim();
      data.strUnit = match.group(2);
    }
    
    // Otherwise everything goes in call description
    else {
      data.strCall = body;
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return CH_PTN.matcher(addr).replaceAll("CHURCH");
  }
  private static final Pattern CH_PTN = Pattern.compile("\\bCH\\b");
  
  private static final String[] CITY_LIST = new String[]{
    "BATTLEBORO",
    "CONETOE",
    "LEGGETT",
    "MACCLESFIELD",
    "PINETOPS",
    "PRINCEVILLE",
    "ROCKY MOUNT",
    "SHARPSBURG",
    "SOUTH WHITAKERS",
    "SPEED",
    "TARBORO",
    "WHITAKERS",
    "HOUSEVILLE",
    
    "TARBORO TWP",
    "LOWER CONETOE TWP",
    "UPPER CONETOE TWP",
    "DEEP CREEK TWP",
    "LOWER FISHING CREEK TWP",
    "UPPER FISHING CREEK TWP",
    "SWIFT CREEK TWP",
    "SPARTA TWP",
    "OTTER CREEK TWP",
    "LOWER TOWN CREEK TWP",
    "WALNUT CREEK TWP",
    "ROCKY MOUNT TWP",
    "COKEY TWP",
    "UPPER TOWN CREEK TWP",
    
    // Nash County
    "NASHVILLE",

    "BAILEY",
    "CASTALIA",
    "GOLD ROCK",
    "GOLDROCK",
    "DORTCHES",
    "MIDDLESEX",
    "MOMEYER",
    "RED OAK",
    "SPRING HOPE",
    "ZEBULON",

    "CORINTH",

    "BAILEY TWP",
    "BATTLEBORO TWP",
    "CASTALIA TWP",
    "COOPERS TWP",
    "DRY WELLS TWP",
    "FERRELLS TWP",
    "GRIFFINS TWP",
    "JACKSON TWP",
    "MANNINGS TWP",
    "NASHVILLE TWP",
    "NORTH WHITAKERS TWP",
    "OAK LEVEL TWP",
    "RED OAK TWP",
    "SPRING HOPE TWP",
    
    // Pitt County
    "FOUNTAIN",
    "GREENVILLE",
    
    // Wilson County
    "ELM CITY",
    
    // Wayne County
    "STONY CREEK",
  };
}
