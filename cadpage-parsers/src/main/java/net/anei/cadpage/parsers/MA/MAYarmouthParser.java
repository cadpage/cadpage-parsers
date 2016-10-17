package net.anei.cadpage.parsers.MA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MAYarmouthParser extends MsgParser {
  
  private static final Pattern PRIORITY_PTN = Pattern.compile("Medical Priority (\\d)/\\d");
  private static final Pattern MAP_PTN = Pattern.compile("\\d_(?:North|East|South|West)_\\d");
  
  public MAYarmouthParser() {
    super("YARMOUTH", "MA");
    setFieldList("ADDR PLACE PRI MAP CALL INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\n+");
    if (flds.length < 2) return false;
    if (flds[0].length() == 0) return false;
    if (!Character.isDigit(flds[0].charAt(0))) return false;
    
    boolean found = false;
    String field = flds[0].trim();
    if (field.startsWith("0 ")) field = field.substring(2).trim();
    parseAddress(field, data);
    for (int ndx = 1; ndx<flds.length; ndx++) {
      field = flds[ndx].trim();
      
      if (field.length() == 0) continue;
      if (field.equals("NO NAME") ||
          field.equals("YARMOUTH") ||
          field.equals("Notes")) {
        found = true;
        continue;
      }
      
      if (data.strPriority.length() == 0) {
        Matcher match =  PRIORITY_PTN.matcher(field);
        if (match.matches()) {
          found = true;
          data.strPriority = match.group(1);
          continue;
        }
      }
      
      if (data.strMap.length() == 0 && MAP_PTN.matcher(field).matches()) {
        found = true;
        data.strMap = field;
        continue;
      }
      
      if (!found) {
        found = true;
        data.strPlace = field;
        continue;
      }
      
      if (data.strCall.length() == 0) {
        data.strCall = field;
      } else {
        data.strSupp = append(data.strSupp, " / ", field);
      }
    }
    
    if (data.strCall.length() == 0) {
      data.strCall = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }
}
