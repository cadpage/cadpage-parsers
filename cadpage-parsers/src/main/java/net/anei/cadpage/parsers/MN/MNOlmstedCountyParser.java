package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Olmsted County, MN
 */
public class MNOlmstedCountyParser extends SmartAddressParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("OLMSTED COUNTY \\((.*?)\\)\\(MN\\)");
  
  public MNOlmstedCountyParser() {
    super(CITY_LIST, "OLMSTED COUNTY", "MN");
    setFieldList("SRC CALL ADDR APT CITY PLACE INFO");
    removeWords("PLACE");
  }
  
  @Override
  public String getFilter() {
    return "@rochestermn.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // New calls have a distinctive subject
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1).trim();
  
      parseAddress(StartType.START_CALL, FLAG_FALLBACK_ADDR, body, data);
      if (data.strCall.length() == 0) {
        data.strCall = getLeft();
      } else {
        data.strSupp = getLeft();
      }
      return true;
    }
    
    // Otherwise drop back to the old logic
    // Not enough identification features to positively identify as a text page
    // so we require that identification be done externally
    if (!isPositiveId()) return false;

    // If no body (which gets to us as no subject) treat as special case
    if (subject.length() == 0) {
      Parser p = new Parser(body);
      parseAddress(StartType.START_CALL, FLAG_PREF_TRAILING_DIR | FLAG_ANCHOR_END, p.get("  "), data);
      if (data.strCall.length() == 0) data.strCall = p.get("  ");
      data.strSupp = p.get();
    }
    
    else {
      if (body.toUpperCase().startsWith(subject.toUpperCase())) {
        subject = body.substring(0,subject.length()).trim();
        body = body.substring(subject.length()).trim();
        if (body.toUpperCase().startsWith("AT ")) body = body.substring(3).trim();
      }
      
      Parser p = new Parser(subject);
      String sub = p.get("  ");
      Result res = parseAddress(StartType.START_CALL, FLAG_IMPLIED_INTERSECT | FLAG_PREF_TRAILING_DIR | FLAG_ANCHOR_END, sub);
      data.strPlace = p.get();
      
      if (res.isValid()) {
        res.getData(data);
        String[] flds = body.split("\n");
        data.strCall = flds[0].trim();
        for (int ii = 1; ii<flds.length; ii++) {
          data.strSupp = append(data.strSupp, " / ", flds[ii].trim());
        }
      }
      
      else {
        data.strCall = sub;
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_PREF_TRAILING_DIR, body, data);
        data.strSupp = getLeft();
      }
    }
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "BYRON",
    "CHATFIELD",
    "DOVER",
    "EYOTA",
    "ORONOCO",
    "PINE ISLAND",
    "ROCHESTER",
    "STEWARTVILLE",

    // Townships
    "CASCADE TWP",
    "DOVER TWP",
    "ELMIRA TWP",
    "EYOTA TWP",
    "FARMINGTON TWP",
    "HAVERHILL TWP",
    "HIGH FOREST TWP",
    "KALMAR TWP",
    "MARION TWP",
    "NEW HAVEN TWP",
    "ORION TWP",
    "ORONOCO TWP",
    "PLEASANT GROVE TWP",
    "QUINCY TWP",
    "ROCHESTER TWP",
    "ROCK DELL TWP",
    "SALEM TWP",
    "VIOLA TWP",
  
    // Unicorporated 
    "CHESTER",
    "DANESVILLE",
    "DOUGLAS",
    "GENOA",
    "HIGH FOREST",
    "PLEASANT GROVE",
    "POST TOWN",
    "POTSDAM",
    "RINGE",
    "SALEM CORNERS",
    "SHANTY TOWN",
    "SIMPSON",
    "VIOLA"
  };
}
