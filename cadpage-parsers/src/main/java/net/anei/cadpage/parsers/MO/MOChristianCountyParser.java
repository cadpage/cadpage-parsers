package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Christian County, MO
 */
public class MOChristianCountyParser extends DispatchPrintrakParser {
  
  private static final Pattern TRAIL_DATE_TIME_PTN = Pattern.compile(" +(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d)$");
  
  public MOChristianCountyParser() {
    super(CITY_LIST, "CHRISTIAN COUNTY", "MO", FLG_VERSION_2);
  }
  
  @Override
  public String getFilter() {
    return "ccespage@cces911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("INC:")) return false;
    Matcher match = TRAIL_DATE_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      body = body.substring(0,match.start());
    }
    if (!super.parseMsg(body, data)) return false;
    
    // It seems that intersections are going in the place field
    if (data.strPlace.contains(" / ")) {
      data.strAddress = "";
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String sAddr) {
    String[] parts =   sAddr.split("&");
    if (parts.length > 1) {
      String result = "";
      for (String part : parts) {
        part = BOUND_PTN.matcher(part).replaceAll("").trim();
        if (NUMERIC.matcher(part).matches()) part = "RT " + part;
        result = append(result, " & ", part);
      }
      sAddr = result;
    }
    return sAddr;
  }
  private static final Pattern BOUND_PTN = Pattern.compile("(?<=\\b|\\d)[NSEW]B(?=\\b|\\d)");
  
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BILLINGS",
    "BOAZ",
    "BATTLEFIELD",
    "BRADLEYVILLE",
    "BROOKLINE",
    "BRUNER",
    "CHADWICK",
    "CHESTNUTRIDGE",
    "CLEVER",
    "CRANE",
    "ELKHEAD",
    "FREMONT HILLS",
    "FORDLAND",
    "FORSYTH",
    "GALENA",
    "GARRISON",
    "HIGHLANDVILLE",
    "HURLEY",
    "KELTNER",
    "LINDEN",
    "MARIONVILLE",
    "MT VERNON",
    "NIXA",
    "OLDFIELD",
    "OZARK",
    "PONCE DE LEON",
    "REEDS SPRING",
    "REPUBLIC",
    "ROGERSVILLE",
    "SADDLEBROOKE",
    "SPARTA",
    "SPOKANE",
    "SPRINGFIELD",
    "TANEYVILLE",
    
    "LAWRENCE COUNTY",
  };
}
