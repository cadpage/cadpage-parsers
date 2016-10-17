package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLCoralSpringsParser extends SmartAddressParser {
  public FLCoralSpringsParser() {
    super("CORAL SPRINGS", "FL");
    setFieldList("CALL ADDR CITY APT PLACE X MAP");
  }

  @Override
  public String getFilter() {
    return "paging@coralsprings.org";
  }

  private static final int
    CALL_BEGIN = 0,
    CALL_END = 35,
    ADDRESS_BEGIN = CALL_END,
    ADDRESS_END = 90,
    CITY_BEGIN = ADDRESS_END,
    CITY_END = 109,
    X1_BEGIN = CITY_END,
    X1_END = 149,
    X2_BEGIN = X1_END,
    X2_END = 188,
    MAP_BEGIN = X2_END;
  
  private static final String SUBJECT_STRING
    = "Email Copy from Emergin Integration Suite";
  private static final Pattern ALL_BUT_LAST_PARAGRAPH_PATTERN
    = Pattern.compile("(.*)\\n\\n.*", Pattern.DOTALL);
  private static final Pattern MUTUAL_AID_PATTERN
    = Pattern.compile("MUTUAL +AID.*");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean mutualAid = false;
    if (!subject.equals(SUBJECT_STRING)) return false;
    Matcher m = ALL_BUT_LAST_PARAGRAPH_PATTERN.matcher(body);
    if (m.matches()) body = m.group(1).trim();
    m = MUTUAL_AID_PATTERN.matcher(body);
    if (m.matches()) {
      // Besides Call field, Mutual Aid calls contain only a (truncated) Address field
      int n = ADDRESS_END - body.length();
      mutualAid = true;
      body += String.format("%"+n+"s", " ");
    }
    else
      if (body.length() < MAP_BEGIN
          || body.charAt(CALL_BEGIN) == ' '
          || (body.charAt(ADDRESS_BEGIN) == ' '
              && body.charAt(ADDRESS_BEGIN+1) == ' ')
          || body.charAt(ADDRESS_END-1) != ' '
          || body.charAt(CITY_BEGIN) == ' '
          || body.charAt(CITY_END-1) != ' '
          // X1 seems to be required.  X2 is optional
          || body.charAt(X1_BEGIN) == ' '
          || body.charAt(X1_END-1) != ' '
          || body.charAt(X2_END-1) != ' '
          || body.charAt(MAP_BEGIN) == ' ') {
        data.strCall = "GENERAL ALERT";
        data.strPlace = body;
        return true;
      }
    
    data.strCall = body.substring(CALL_BEGIN, CALL_END).trim();
    parseAddressField(body.substring(ADDRESS_BEGIN, ADDRESS_END), data);
    if (!mutualAid) {
      data.strCity = body.substring(CITY_BEGIN, CITY_END).trim();
      data.strCross = append(data.strCross, "/", body.substring(X1_BEGIN, X1_END).trim());
      data.strCross = append(data.strCross, "/", body.substring(X2_BEGIN, X2_END).trim());
      data.strMap = body.substring(MAP_BEGIN);
    }
    
    return true;
  }
  
  private static final Pattern DECIMAL_APT_PATTERN
    = Pattern.compile("(.*)\\s*\\.([^ ]+)\\s*");
  private void parseAddressField(String field, Data data) {
    Matcher m = DECIMAL_APT_PATTERN.matcher(field);
    if (m.matches()) {
      field = m.group(1);
      data.strApt = m.group(2);
    }
    if (field.substring(0, 1).equals(" "))
      parseAddress(field.substring(1), data);
    else
      parseAddress(StartType.START_PLACE, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, field, data);
  }
}
