package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class AZYumaCountyParser extends MsgParser {
  
  public AZYumaCountyParser() {
    super("YUMA COUNTY", "AZ");
  }
  
  @Override
  public String getFilter() {
    return "yumacomm@rmetro.com";
  }
  
  private static final Pattern MASTER1 = Pattern.compile("(?:(CH *\\d+) )?([ A-Z0-9]+?)\\b *(?:ASSIGN:|ASSIGN - |ASSIGN FOR |\\. ) *(?:([A-Z0-9,. ]+?)(?: AREA OF | [-/] ))?(.*?) (?:[-/]|FOR (?:A )?(?:REPORT OF )?|REP AS )(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern MASTER3 = Pattern.compile("(CH *\\d+) (COMM ASSIG|[A-Z]+?) (.*?) FOR (.*?),? REPORTED AS (.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern UNIT_PTN = Pattern.compile("(?!US|RT|ST|AZ)([A-Z]+ *\\d+)[., ]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern UNIT_SPACE_PTN = Pattern.compile("([A-Z]+) +(\\d+)");
  private static final Pattern UNIT_COMMA_SPACE_PTN = Pattern.compile("[, .]+", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf("\n***");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("CH CALL UNIT ADDR APT PLACE");
      data.strChannel = getOptGroup(match.group(1)).toUpperCase().replace(" ", "");
      data.strCall = match.group(2).toUpperCase();
      String unit = match.group(3);
      String addr = match.group(4);
      data.strCall = append(data.strCall, " - ", match.group(5).trim());

      // If there was no marker between the unit and address
      // use a pattern match to separate them
      if (unit == null) {
        while (true) {
          match = UNIT_PTN.matcher(addr);
          if (!match.lookingAt()) break;
          data.strUnit = append(data.strUnit, ",", match.group(1).replace(" ","").toUpperCase());
          addr = addr.substring(match.end());
        }
      } 
      
      // If we did get two distinct fields, check to see if they have been reversed
      else {
        if (unitScore(addr) > unitScore(unit)) {
          String tmp = unit;
          unit = addr;
          addr = tmp;
        }
        data.strUnit = cleanUpUnit(unit);
      }
      parseAddressField(addr.trim(), data);
      return true;
    }
    
    if ((match = MASTER3.matcher(body)).matches()) {
      setFieldList("CH CALL ADDR APT PLACE UNIT");
      data.strChannel = match.group(1).toUpperCase().replace(" ",  "");
      data.strCall = match.group(2).toUpperCase();
      parseAddressField(match.group(3), data);
      data.strUnit = cleanUpUnit(match.group(4));
      data.strCall = append(data.strCall, " - ", match.group(5).trim());
      return true;
    }
    
    return false;
  }

  private static String cleanUpUnit(String unit) {
    unit = unit.trim().toUpperCase();
    unit = UNIT_SPACE_PTN.matcher(unit).replaceAll("$1$2");
    unit = UNIT_COMMA_SPACE_PTN.matcher(unit).replaceAll(",");
    return unit;
  }
  
  /**
   * Score field as unit vs address
   * @param field field to be scored
   * @return score value, higher scores are more likely to be units
   */
  private int unitScore(String field) {
    
    // Anything with a comma is probably a unit
    if (field.indexOf(',') >= 0) return +100;
    
    // Anything with a space, but no comma, is probably an address
    if (field.indexOf(' ') >= 0) return -100;
    
    // No space or comma.  See if it matches the unit pattern
    if (UNIT_SPACE_PTN.matcher(field+' ').matches()) return +90;
    
    // No idea :(
    return 0;
  }
  
  /**
   * Parse address field
   * @param field address field
   * @param data parsed data object
   */
  private void parseAddressField(String field, Data data) {
    field = field.trim();
    if (field.endsWith(")")) {
      int pt = field.indexOf('(');
      data.strPlace = field.substring(pt+1, field.length()-1).trim();
      field = field.substring(0,pt).trim();
    }
    else {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
    }
    
    parseAddress(field, data);
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    sAddress = stripFieldStart(sAddress, "AREA OF ");
    return super.adjustMapAddress(sAddress, cross);
  }

  private static final Pattern COUNTY_RD_PTN = Pattern.compile("\\bCOUNTY ROAD (\\d+)", Pattern.CASE_INSENSITIVE);
  @Override
  public String postAdjustMapAddress(String sAddress) {
    sAddress = COUNTY_RD_PTN.matcher(sAddress).replaceAll("COUNTY $1 ST");
    return super.postAdjustMapAddress(sAddress);
  }
}
